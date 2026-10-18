package dr.web;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * Synchronous PNG decoder. MIDP's Image.createImage(byte[], ...) must return a decoded image immediately,
 * which the browser's own (asynchronous) decoders cannot do.
 */
public final class Png {
    private static final int IHDR = 0x49484452;
    private static final int PLTE = 0x504C5445;
    private static final int TRNS = 0x74524E53;
    private static final int IDAT = 0x49444154;
    private static final int IEND = 0x49454E44;

    private static final int[][] ADAM7 = {
        { 0, 0, 8, 8 }, { 4, 0, 8, 8 }, { 0, 4, 4, 8 }, { 2, 0, 4, 4 }, { 0, 2, 2, 4 }, { 1, 0, 2, 2 }, { 0, 1, 1, 2 }
    };

    public final int width;
    public final int height;
    public final int[] argb;

    private Png(int width, int height, int[] argb) {
        this.width = width;
        this.height = height;
        this.argb = argb;
    }

    public static Png decode(byte[] data, int offset, int length) {
        int end = offset + length;
        if (length < 8 || (data[offset] & 0xFF) != 0x89 || data[offset + 1] != 'P' || data[offset + 2] != 'N'
                || data[offset + 3] != 'G') {
            throw new IllegalArgumentException("Not a PNG image");
        }
        int width = 0;
        int height = 0;
        int depth = 0;
        int colorType = 0;
        int interlace = 0;
        int[] palette = null;
        byte[] trns = null;
        ByteArrayOutputStream idat = new ByteArrayOutputStream();
        int p = offset + 8;
        while (p + 8 <= end) {
            int chunkLength = readInt(data, p);
            int type = readInt(data, p + 4);
            int d = p + 8;
            if (chunkLength < 0 || d + chunkLength > end) {
                break;
            }
            if (type == IHDR) {
                width = readInt(data, d);
                height = readInt(data, d + 4);
                depth = data[d + 8] & 0xFF;
                colorType = data[d + 9] & 0xFF;
                interlace = data[d + 12] & 0xFF;
            } else if (type == PLTE) {
                palette = new int[chunkLength / 3];
                for (int i = 0; i < palette.length; i++) {
                    int q = d + i * 3;
                    palette[i] = 0xFF000000 | (data[q] & 0xFF) << 16 | (data[q + 1] & 0xFF) << 8 | (data[q + 2] & 0xFF);
                }
            } else if (type == TRNS) {
                trns = new byte[chunkLength];
                System.arraycopy(data, d, trns, 0, chunkLength);
            } else if (type == IDAT) {
                idat.write(data, d, chunkLength);
            } else if (type == IEND) {
                break;
            }
            p = d + chunkLength + 4;
        }
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("PNG without IHDR");
        }

        int channels;
        switch (colorType) {
            case 0: channels = 1; break;
            case 2: channels = 3; break;
            case 3: channels = 1; break;
            case 4: channels = 2; break;
            case 6: channels = 4; break;
            default: throw new IllegalArgumentException("Bad PNG color type " + colorType);
        }
        int bitsPerPixel = channels * depth;
        int filterStride = Math.max(1, bitsPerPixel / 8);

        int rawSize = 0;
        for (int pass = 0; pass < (interlace == 1 ? 7 : 1); pass++) {
            int[] geometry = passGeometry(interlace, pass, width, height);
            if (geometry[0] > 0 && geometry[1] > 0) {
                rawSize += geometry[1] * (1 + (geometry[0] * bitsPerPixel + 7) / 8);
            }
        }
        byte[] raw = inflate(idat.toByteArray(), rawSize);

        int[] out = new int[width * height];
        int maxSample = (1 << depth) - 1;
        int pos = 0;
        for (int pass = 0; pass < (interlace == 1 ? 7 : 1); pass++) {
            int[] geometry = passGeometry(interlace, pass, width, height);
            int pw = geometry[0];
            int ph = geometry[1];
            if (pw <= 0 || ph <= 0) {
                continue;
            }
            int[] layout = interlace == 1 ? ADAM7[pass] : new int[] { 0, 0, 1, 1 };
            int rowBytes = (pw * bitsPerPixel + 7) / 8;
            byte[] previous = new byte[rowBytes];
            byte[] row = new byte[rowBytes];
            for (int y = 0; y < ph; y++) {
                int filter = raw[pos++] & 0xFF;
                System.arraycopy(raw, pos, row, 0, rowBytes);
                pos += rowBytes;
                unfilter(filter, row, previous, filterStride);
                int outY = layout[1] + y * layout[3];
                for (int x = 0; x < pw; x++) {
                    int outX = layout[0] + x * layout[2];
                    out[outY * width + outX] = pixel(row, x, colorType, depth, maxSample, palette, trns);
                }
                byte[] swap = previous;
                previous = row;
                row = swap;
            }
        }
        return new Png(width, height, out);
    }

    private static int[] passGeometry(int interlace, int pass, int width, int height) {
        if (interlace != 1) {
            return new int[] { width, height };
        }
        int[] a = ADAM7[pass];
        return new int[] { (width - a[0] + a[2] - 1) / a[2], (height - a[1] + a[3] - 1) / a[3] };
    }

    private static byte[] inflate(byte[] compressed, int size) {
        Inflater inflater = new Inflater();
        inflater.setInput(compressed);
        byte[] raw = new byte[size];
        int n = 0;
        try {
            while (n < size && !inflater.finished()) {
                int got = inflater.inflate(raw, n, size - n);
                if (got == 0 && (inflater.needsInput() || inflater.needsDictionary())) {
                    break;
                }
                n += got;
            }
        } catch (DataFormatException e) {
            throw new IllegalArgumentException("Corrupt PNG data");
        } finally {
            inflater.end();
        }
        return raw;
    }

    private static void unfilter(int filter, byte[] row, byte[] prev, int bpp) {
        int n = row.length;
        switch (filter) {
            case 0:
                break;
            case 1:
                for (int i = bpp; i < n; i++) {
                    row[i] += row[i - bpp];
                }
                break;
            case 2:
                for (int i = 0; i < n; i++) {
                    row[i] += prev[i];
                }
                break;
            case 3:
                for (int i = 0; i < n; i++) {
                    int left = i >= bpp ? row[i - bpp] & 0xFF : 0;
                    row[i] += (byte) ((left + (prev[i] & 0xFF)) >> 1);
                }
                break;
            case 4:
                for (int i = 0; i < n; i++) {
                    int a = i >= bpp ? row[i - bpp] & 0xFF : 0;
                    int b = prev[i] & 0xFF;
                    int c = i >= bpp ? prev[i - bpp] & 0xFF : 0;
                    int pa = Math.abs(b - c);
                    int pb = Math.abs(a - c);
                    int pc = Math.abs(a + b - 2 * c);
                    int predictor = pa <= pb && pa <= pc ? a : pb <= pc ? b : c;
                    row[i] += (byte) predictor;
                }
                break;
            default:
                throw new IllegalArgumentException("Bad PNG filter " + filter);
        }
    }

    /** Reads sample {@code index} of a row (16-bit samples return their full value). */
    private static int sample(byte[] row, int index, int depth) {
        switch (depth) {
            case 8:
                return row[index] & 0xFF;
            case 16:
                return (row[index * 2] & 0xFF) << 8 | (row[index * 2 + 1] & 0xFF);
            default:
                int bit = index * depth;
                int shift = 8 - depth - (bit & 7);
                return (row[bit >> 3] & 0xFF) >> shift & ((1 << depth) - 1);
        }
    }

    private static int to8(int value, int depth, int maxSample) {
        return depth == 16 ? value >> 8 : depth == 8 ? value : value * 255 / maxSample;
    }

    private static int pixel(byte[] row, int x, int colorType, int depth, int maxSample, int[] palette, byte[] trns) {
        switch (colorType) {
            case 0: {
                int g = sample(row, x, depth);
                int alpha = trns != null && trns.length >= 2 && g == ((trns[0] & 0xFF) << 8 | (trns[1] & 0xFF)) ? 0 : 255;
                int v = to8(g, depth, maxSample);
                return alpha << 24 | v << 16 | v << 8 | v;
            }
            case 2: {
                int r = sample(row, x * 3, depth);
                int g = sample(row, x * 3 + 1, depth);
                int b = sample(row, x * 3 + 2, depth);
                int alpha = 255;
                if (trns != null && trns.length >= 6 && r == ((trns[0] & 0xFF) << 8 | (trns[1] & 0xFF))
                        && g == ((trns[2] & 0xFF) << 8 | (trns[3] & 0xFF))
                        && b == ((trns[4] & 0xFF) << 8 | (trns[5] & 0xFF))) {
                    alpha = 0;
                }
                return alpha << 24 | to8(r, depth, maxSample) << 16 | to8(g, depth, maxSample) << 8 | to8(b, depth, maxSample);
            }
            case 3: {
                int index = sample(row, x, depth);
                int color = palette != null && index < palette.length ? palette[index] : 0xFF000000;
                if (trns != null && index < trns.length) {
                    color = (color & 0xFFFFFF) | (trns[index] & 0xFF) << 24;
                }
                return color;
            }
            case 4: {
                int g = to8(sample(row, x * 2, depth), depth, maxSample);
                int a = to8(sample(row, x * 2 + 1, depth), depth, maxSample);
                return a << 24 | g << 16 | g << 8 | g;
            }
            default: {
                int r = to8(sample(row, x * 4, depth), depth, maxSample);
                int g = to8(sample(row, x * 4 + 1, depth), depth, maxSample);
                int b = to8(sample(row, x * 4 + 2, depth), depth, maxSample);
                int a = to8(sample(row, x * 4 + 3, depth), depth, maxSample);
                return a << 24 | r << 16 | g << 8 | b;
            }
        }
    }

    private static int readInt(byte[] data, int p) {
        return (data[p] & 0xFF) << 24 | (data[p + 1] & 0xFF) << 16 | (data[p + 2] & 0xFF) << 8 | (data[p + 3] & 0xFF);
    }
}
