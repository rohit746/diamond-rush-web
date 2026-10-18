package javax.microedition.lcdui;

import difftest.Trace;
import dr.web.Png;

/** Images are numbered in creation order; their pixels go into the trace when created. */
public class Image {
    private static int nextId = 1000;

    final int id;
    private final int width;
    private final int height;
    private final boolean mutable;

    private Image(int width, int height, boolean mutable) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException();
        }
        this.id = nextId++;
        this.width = width;
        this.height = height;
        this.mutable = mutable;
    }

    public static Image createImage(int width, int height) {
        Image image = new Image(width, height, true);
        Trace.op(40, image.id, width, height);
        return image;
    }

    public static Image createImage(byte[] data, int offset, int length) {
        Png png = Png.decode(data, offset, length);
        return createRGBImage(png.argb, png.width, png.height, true);
    }

    public static Image createRGBImage(int[] rgb, int width, int height, boolean processAlpha) {
        if (rgb.length < width * height) {
            throw new ArrayIndexOutOfBoundsException();
        }
        Image image = new Image(width, height, false);
        Trace.data(41, rgb, 0, width * height);
        Trace.op(41, image.id, width, height, processAlpha ? 1 : 0);
        return image;
    }

    public Graphics getGraphics() {
        if (!mutable) {
            throw new IllegalStateException();
        }
        return new Graphics(id, width, height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isMutable() {
        return mutable;
    }
}
