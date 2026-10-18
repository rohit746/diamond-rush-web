package javax.microedition.lcdui;

import dr.web.Host;

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

    private final Surface surface;
    private int tx;
    private int ty;
    // Clip rectangle in absolute surface coordinates, always within the surface.
    private int clipX;
    private int clipY;
    private int clipW;
    private int clipH;
    private int color;
    private Font font;
    private int strokeStyle;

    Graphics(Surface surface) {
        this.surface = surface;
        reset();
    }

    final void reset() {
        tx = 0;
        ty = 0;
        clipX = 0;
        clipY = 0;
        clipW = surface.width;
        clipH = surface.height;
        color = 0;
        font = Font.getDefaultFont();
        strokeStyle = SOLID;
    }

    public void translate(int x, int y) {
        tx += x;
        ty += y;
    }

    public int getTranslateX() {
        return tx;
    }

    public int getTranslateY() {
        return ty;
    }

    public int getColor() {
        return color;
    }

    public int getRedComponent() {
        return color >> 16 & 0xFF;
    }

    public int getGreenComponent() {
        return color >> 8 & 0xFF;
    }

    public int getBlueComponent() {
        return color & 0xFF;
    }

    public int getGrayScale() {
        return (getRedComponent() * 30 + getGreenComponent() * 59 + getBlueComponent() * 11) / 100;
    }

    public void setColor(int red, int green, int blue) {
        if ((red | green | blue) >>> 8 != 0) {
            throw new IllegalArgumentException();
        }
        color = red << 16 | green << 8 | blue;
    }

    public void setColor(int rgb) {
        color = rgb & 0xFFFFFF;
    }

    public void setGrayScale(int value) {
        setColor(value, value, value);
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font == null ? Font.getDefaultFont() : font;
    }

    public void setStrokeStyle(int style) {
        strokeStyle = style;
    }

    public int getStrokeStyle() {
        return strokeStyle;
    }

    public int getDisplayColor(int rgb) {
        return rgb & 0xFFFFFF;
    }

    // ---- clipping ----

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

    public void setClip(int x, int y, int width, int height) {
        x += tx;
        y += ty;
        int x0 = Math.max(x, 0);
        int y0 = Math.max(y, 0);
        int x1 = Math.min(x + width, surface.width);
        int y1 = Math.min(y + height, surface.height);
        clipX = x0;
        clipY = y0;
        clipW = Math.max(0, x1 - x0);
        clipH = Math.max(0, y1 - y0);
    }

    public void clipRect(int x, int y, int width, int height) {
        x += tx;
        y += ty;
        int x0 = Math.max(x, clipX);
        int y0 = Math.max(y, clipY);
        int x1 = Math.min(x + width, clipX + clipW);
        int y1 = Math.min(y + height, clipY + clipH);
        clipX = x0;
        clipY = y0;
        clipW = Math.max(0, x1 - x0);
        clipH = Math.max(0, y1 - y0);
    }

    private boolean applyPathClip() {
        if (clipW <= 0 || clipH <= 0) {
            return false;
        }
        surface.clip(clipX, clipY, clipW, clipH);
        surface.color(color);
        return true;
    }

    // ---- primitives ----

    public void fillRect(int x, int y, int width, int height) {
        if (width <= 0 || height <= 0) {
            return;
        }
        x += tx;
        y += ty;
        int x0 = Math.max(x, clipX);
        int y0 = Math.max(y, clipY);
        int x1 = Math.min(x + width, clipX + clipW);
        int y1 = Math.min(y + height, clipY + clipH);
        if (x0 >= x1 || y0 >= y1) {
            return;
        }
        surface.unclipped(x0, y0, x1, y1);
        surface.color(color);
        Host.fillRect(surface.ctx, x0, y0, x1 - x0, y1 - y0);
    }

    public void drawRect(int x, int y, int width, int height) {
        if (width < 0 || height < 0) {
            return;
        }
        fillRect(x, y, width + 1, 1);
        if (height > 0) {
            fillRect(x, y + height, width + 1, 1);
            if (height > 1) {
                fillRect(x, y + 1, 1, height - 1);
                fillRect(x + width, y + 1, 1, height - 1);
            }
        }
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        if (y1 == y2) {
            fillRect(Math.min(x1, x2), y1, Math.abs(x2 - x1) + 1, 1);
        } else if (x1 == x2) {
            fillRect(x1, Math.min(y1, y2), 1, Math.abs(y2 - y1) + 1);
        } else if (applyPathClip()) {
            Host.line(surface.ctx, x1 + tx, y1 + ty, x2 + tx, y2 + ty);
        }
    }

    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        if (width > 0 && height > 0 && applyPathClip()) {
            Host.roundRect(surface.ctx, x + tx, y + ty, width, height, arcWidth, arcHeight, true);
        }
    }

    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        if (width >= 0 && height >= 0 && applyPathClip()) {
            Host.roundRect(surface.ctx, x + tx, y + ty, width, height, arcWidth, arcHeight, false);
        }
    }

    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        if (width > 0 && height > 0 && applyPathClip()) {
            Host.arc(surface.ctx, x + tx, y + ty, width, height, startAngle, arcAngle, true);
        }
    }

    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        if (width >= 0 && height >= 0 && applyPathClip()) {
            Host.arc(surface.ctx, x + tx, y + ty, width, height, startAngle, arcAngle, false);
        }
    }

    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        if (applyPathClip()) {
            Host.triangle(surface.ctx, x1 + tx, y1 + ty, x2 + tx, y2 + ty, x3 + tx, y3 + ty);
        }
    }

    // ---- text ----

    public void drawString(String str, int x, int y, int anchor) {
        if (str == null) {
            throw new NullPointerException();
        }
        if (str.isEmpty()) {
            return;
        }
        if ((anchor & HCENTER) != 0) {
            x -= font.stringWidth(str) / 2;
        } else if ((anchor & RIGHT) != 0) {
            x -= font.stringWidth(str);
        }
        int baseline;
        if ((anchor & BASELINE) != 0) {
            baseline = y;
        } else if ((anchor & BOTTOM) != 0) {
            baseline = y - font.getHeight() + font.getBaselinePosition();
        } else {
            baseline = y + font.getBaselinePosition();
        }
        if (applyPathClip()) {
            Host.text(surface.ctx, str, x + tx, baseline + ty, font.css);
        }
    }

    public void drawSubstring(String str, int offset, int len, int x, int y, int anchor) {
        drawString(str.substring(offset, offset + len), x, y, anchor);
    }

    public void drawChar(char character, int x, int y, int anchor) {
        drawString(String.valueOf(character), x, y, anchor);
    }

    public void drawChars(char[] data, int offset, int length, int x, int y, int anchor) {
        drawString(new String(data, offset, length), x, y, anchor);
    }

    // ---- images ----

    public void drawImage(Image img, int x, int y, int anchor) {
        if (img == null) {
            throw new NullPointerException();
        }
        drawRegion(img, 0, 0, img.width, img.height, Transform.NONE, x, y, anchor);
    }

    public void drawRegion(Image src, int xSrc, int ySrc, int width, int height, int transform, int xDest,
            int yDest, int anchor) {
        if (src == null) {
            throw new NullPointerException();
        }
        if (xSrc < 0 || ySrc < 0 || xSrc + width > src.width || ySrc + height > src.height || transform < 0
                || transform > 7) {
            throw new IllegalArgumentException();
        }
        if (width <= 0 || height <= 0 || src.surface == surface) {
            return;
        }
        boolean swap = transform >= 4;
        int dw = swap ? height : width;
        int dh = swap ? width : height;
        if ((anchor & HCENTER) != 0) {
            xDest -= dw / 2;
        } else if ((anchor & RIGHT) != 0) {
            xDest -= dw;
        }
        if ((anchor & VCENTER) != 0) {
            yDest -= dh / 2;
        } else if ((anchor & BOTTOM) != 0) {
            yDest -= dh;
        }
        xDest += tx;
        yDest += ty;
        int x0 = Math.max(xDest, clipX);
        int y0 = Math.max(yDest, clipY);
        int x1 = Math.min(xDest + dw, clipX + clipW);
        int y1 = Math.min(yDest + dh, clipY + clipH);
        if (x0 >= x1 || y0 >= y1) {
            return;
        }
        // Map the visible destination rectangle back to the source rectangle it shows.
        int u0 = x0 - xDest;
        int u1 = x1 - xDest;
        int v0 = y0 - yDest;
        int v1 = y1 - yDest;
        int s0;
        int s1;
        int r0;
        int r1;
        switch (transform) {
            case Transform.NONE: s0 = u0; s1 = u1; r0 = v0; r1 = v1; break;
            case Transform.ROT90: s0 = v0; s1 = v1; r0 = height - u1; r1 = height - u0; break;
            case Transform.ROT180: s0 = width - u1; s1 = width - u0; r0 = height - v1; r1 = height - v0; break;
            case Transform.ROT270: s0 = width - v1; s1 = width - v0; r0 = u0; r1 = u1; break;
            case Transform.MIRROR: s0 = width - u1; s1 = width - u0; r0 = v0; r1 = v1; break;
            case Transform.MIRROR_ROT90: s0 = width - v1; s1 = width - v0; r0 = height - u1; r1 = height - u0; break;
            case Transform.MIRROR_ROT180: s0 = u0; s1 = u1; r0 = height - v1; r1 = height - v0; break;
            default: s0 = v0; s1 = v1; r0 = u0; r1 = u1; break; // TRANS_MIRROR_ROT270
        }
        surface.unclipped(x0, y0, x1, y1);
        if (transform == Transform.NONE) {
            Host.blit(surface.ctx, src.surface.canvas, xSrc + s0, ySrc + r0, s1 - s0, r1 - r0, x0, y0);
        } else {
            Host.blitTransformed(surface.ctx, src.surface.canvas, xSrc + s0, ySrc + r0, s1 - s0, r1 - r0, transform,
                    x0, y0);
        }
    }

    public void drawRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height,
            boolean processAlpha) {
        if (width <= 0 || height <= 0) {
            return;
        }
        Image tmp = Image.createRGBImage(rgbData, offset, scanlength, width, height, processAlpha);
        drawImage(tmp, x, y, TOP | LEFT);
    }

    public void copyArea(int xSrc, int ySrc, int width, int height, int xDest, int yDest, int anchor) {
        Image copy = Image.createImage(Image.wrap(surface), xSrc + tx, ySrc + ty, width, height, Transform.NONE);
        drawImage(copy, xDest, yDest, anchor);
    }
}
