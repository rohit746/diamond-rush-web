package javax.microedition.lcdui;

import dr.web.Host;
import org.teavm.jso.JSObject;

/**
 * A drawable HTML canvas plus the 2D-context state currently applied to it. Several Graphics objects may
 * target one surface, so the applied fill colour and clip are tracked here and only pushed to the context
 * when they change.
 */
final class Surface {
    final JSObject canvas;
    final JSObject ctx;
    final int width;
    final int height;

    private int appliedColor = -1;
    private boolean clipped;
    private int clipX;
    private int clipY;
    private int clipW;
    private int clipH;

    Surface(JSObject canvas, int width, int height) {
        this.canvas = canvas;
        this.ctx = Host.context(canvas);
        this.width = width;
        this.height = height;
    }

    void color(int rgb) {
        if (appliedColor != rgb) {
            appliedColor = rgb;
            Host.setColor(ctx, rgb);
        }
    }

    /** Prepares for an operation already clipped by hand to the given absolute rectangle. */
    void unclipped(int x0, int y0, int x1, int y1) {
        if (clipped && (x0 < clipX || y0 < clipY || x1 > clipX + clipW || y1 > clipY + clipH)) {
            Host.unclip(ctx);
            clipped = false;
            appliedColor = -1;
        }
    }

    /** Applies a context clip for operations that cannot be clipped by hand (text, lines, arcs). */
    void clip(int x, int y, int w, int h) {
        if (clipped) {
            if (x == clipX && y == clipY && w == clipW && h == clipH) {
                return;
            }
            Host.unclip(ctx);
            clipped = false;
            appliedColor = -1;
        }
        if (x <= 0 && y <= 0 && x + w >= width && y + h >= height) {
            return;
        }
        Host.clip(ctx, x, y, w, h);
        clipped = true;
        clipX = x;
        clipY = y;
        clipW = w;
        clipH = h;
    }
}
