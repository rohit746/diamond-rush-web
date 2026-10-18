package javax.microedition.lcdui;

import difftest.Trace;

/** Records every call (with the target surface) into the trace; keeps just enough state for the getters. */
public class Graphics {
    public static final int HCENTER = 1;
    public static final int VCENTER = 2;
    public static final int LEFT = 4;
    public static final int RIGHT = 8;
    public static final int TOP = 16;
    public static final int BOTTOM = 32;
    public static final int BASELINE = 64;
    public static final int SOLID = 0;
    public static final int DOTTED = 1;

    private final int surface;
    private final int width;
    private final int height;
    private int tx;
    private int ty;
    private int clipX;
    private int clipY;
    private int clipW;
    private int clipH;
    private int color;
    private int stroke;
    private Font font = Font.getDefaultFont();

    Graphics(int surface, int width, int height) {
        this.surface = surface;
        this.width = width;
        this.height = height;
        reset();
    }

    final void reset() {
        tx = 0;
        ty = 0;
        clipX = 0;
        clipY = 0;
        clipW = width;
        clipH = height;
        color = 0;
        stroke = SOLID;
        font = Font.getDefaultFont();
    }

    private void op(int code, int... args) {
        int[] all = new int[args.length + 1];
        all[0] = surface;
        System.arraycopy(args, 0, all, 1, args.length);
        Trace.op(code, all);
    }

    public void translate(int x, int y) {
        tx += x;
        ty += y;
        op(1, x, y);
    }

    public int getTranslateX() {
        return tx;
    }

    public int getTranslateY() {
        return ty;
    }

    public void setColor(int rgb) {
        color = rgb & 0xFFFFFF;
        op(2, color);
    }

    public void setColor(int r, int g, int b) {
        if ((r | g | b) >>> 8 != 0) {
            throw new IllegalArgumentException();
        }
        setColor(r << 16 | g << 8 | b);
    }

    public int getColor() {
        return color;
    }

    public void setGrayScale(int v) {
        setColor(v, v, v);
    }

    public void setFont(Font f) {
        font = f == null ? Font.getDefaultFont() : f;
        op(3, font.id);
    }

    public Font getFont() {
        return font;
    }

    public void setStrokeStyle(int style) {
        stroke = style;
        op(4, style);
    }

    public int getStrokeStyle() {
        return stroke;
    }

    public void setClip(int x, int y, int w, int h) {
        x += tx;
        y += ty;
        int x0 = Math.max(x, 0);
        int y0 = Math.max(y, 0);
        clipX = x0;
        clipY = y0;
        clipW = Math.max(0, Math.min(x + w, width) - x0);
        clipH = Math.max(0, Math.min(y + h, height) - y0);
        op(5, clipX, clipY, clipW, clipH);
    }

    public void clipRect(int x, int y, int w, int h) {
        x += tx;
        y += ty;
        int x0 = Math.max(x, clipX);
        int y0 = Math.max(y, clipY);
        int x1 = Math.min(x + w, clipX + clipW);
        int y1 = Math.min(y + h, clipY + clipH);
        clipX = x0;
        clipY = y0;
        clipW = Math.max(0, x1 - x0);
        clipH = Math.max(0, y1 - y0);
        op(6, clipX, clipY, clipW, clipH);
    }

    public int getClipX() {
        return clipX - tx;
    }

    public int getClipY() {
        return clipY - ty;
    }

    public int getClipWidth() {
        return clipW;
    }

    public int getClipHeight() {
        return clipH;
    }

    public void fillRect(int x, int y, int w, int h) {
        op(10, x, y, w, h);
    }

    public void drawRect(int x, int y, int w, int h) {
        op(11, x, y, w, h);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        op(12, x1, y1, x2, y2);
    }

    public void fillRoundRect(int x, int y, int w, int h, int aw, int ah) {
        op(13, x, y, w, h, aw, ah);
    }

    public void drawRoundRect(int x, int y, int w, int h, int aw, int ah) {
        op(14, x, y, w, h, aw, ah);
    }

    public void fillArc(int x, int y, int w, int h, int s, int a) {
        op(15, x, y, w, h, s, a);
    }

    public void drawArc(int x, int y, int w, int h, int s, int a) {
        op(16, x, y, w, h, s, a);
    }

    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        op(17, x1, y1, x2, y2, x3, y3);
    }

    public void drawString(String s, int x, int y, int anchor) {
        Trace.text(20, s, surface, x, y, anchor);
    }

    public void drawSubstring(String s, int offset, int len, int x, int y, int anchor) {
        drawString(s.substring(offset, offset + len), x, y, anchor);
    }

    public void drawChar(char c, int x, int y, int anchor) {
        drawString(String.valueOf(c), x, y, anchor);
    }

    public void drawChars(char[] data, int offset, int length, int x, int y, int anchor) {
        drawString(new String(data, offset, length), x, y, anchor);
    }

    public void drawImage(Image img, int x, int y, int anchor) {
        op(30, img.id, x, y, anchor);
    }

    public void drawRegion(Image src, int xs, int ys, int w, int h, int transform, int xd, int yd, int anchor) {
        if (xs < 0 || ys < 0 || xs + w > src.getWidth() || ys + h > src.getHeight() || transform < 0 || transform > 7) {
            throw new IllegalArgumentException();
        }
        op(31, src.id, xs, ys, w, h, transform, xd, yd, anchor);
    }

    public void drawRGB(int[] rgb, int offset, int scan, int x, int y, int w, int h, boolean alpha) {
        Trace.data(32, rgb, offset, Math.max(0, scan * (h - 1) + w));
        op(32, scan, x, y, w, h, alpha ? 1 : 0);
    }
}
