package javax.microedition.lcdui;

import dr.web.Host;
import dr.web.Png;
import dr.web.Res;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.teavm.jso.typedarrays.Int32Array;

public class Image {
    final Surface surface;
    final int width;
    final int height;
    private final boolean mutable;

    private Image(Surface surface, boolean mutable) {
        this.surface = surface;
        this.width = surface.width;
        this.height = surface.height;
        this.mutable = mutable;
    }

    static Image wrap(Surface surface) {
        return new Image(surface, true);
    }

    private static Surface blank(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException();
        }
        return new Surface(Host.createCanvas(width, height), width, height);
    }

    public static Image createImage(int width, int height) {
        Surface surface = blank(width, height);
        surface.color(0xFFFFFF);
        Host.fillRect(surface.ctx, 0, 0, width, height);
        return new Image(surface, true);
    }

    public static Image createImage(String name) throws IOException {
        byte[] data = Res.get(name);
        if (data == null) {
            throw new IOException(name);
        }
        return createImage(data, 0, data.length);
    }

    public static Image createImage(InputStream stream) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n;
        while ((n = stream.read(buffer)) > 0) {
            out.write(buffer, 0, n);
        }
        byte[] data = out.toByteArray();
        return createImage(data, 0, data.length);
    }

    public static Image createImage(byte[] data, int offset, int length) {
        Png png = Png.decode(data, offset, length);
        return fromArgb(png.argb, 0, png.width, png.width, png.height, true);
    }

    public static Image createImage(Image source) {
        if (!source.mutable) {
            return source;
        }
        return createImage(source, 0, 0, source.width, source.height, Transform.NONE);
    }

    public static Image createImage(Image image, int x, int y, int width, int height, int transform) {
        boolean swap = transform >= 4;
        Surface surface = blank(swap ? height : width, swap ? width : height);
        new Graphics(surface).drawRegion(image, x, y, width, height, transform, 0, 0, Graphics.TOP | Graphics.LEFT);
        return new Image(surface, false);
    }

    public static Image createRGBImage(int[] rgb, int width, int height, boolean processAlpha) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException();
        }
        if (rgb.length < width * height) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return fromArgb(rgb, 0, width, width, height, processAlpha);
    }

    static Image createRGBImage(int[] rgb, int offset, int scan, int width, int height, boolean processAlpha) {
        return fromArgb(rgb, offset, scan, width, height, processAlpha);
    }

    private static Image fromArgb(int[] argb, int offset, int scan, int width, int height, boolean alpha) {
        Surface surface = new Surface(Host.rgbImage(Int32Array.fromJavaArray(argb), offset, scan, width, height, alpha),
                width, height);
        return new Image(surface, false);
    }

    public Graphics getGraphics() {
        if (!mutable) {
            throw new IllegalStateException();
        }
        return new Graphics(surface);
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

    public void getRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height) {
        if (x < 0 || y < 0 || x + width > this.width || y + height > this.height) {
            throw new IllegalArgumentException();
        }
        Host.getRGB(surface.canvas, Int32Array.fromJavaArray(rgbData), offset, scanlength, x, y, width, height);
    }
}
