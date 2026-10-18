package dr.web;

import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.typedarrays.Int32Array;
import org.teavm.jso.typedarrays.Int8Array;

/** Bridge to web/host.js (global DRHost). Everything browser-specific lives on the JS side. */
public final class Host {
    private Host() {
    }

    @JSFunctor
    public interface Callback extends JSObject {
        void call();
    }

    @JSFunctor
    public interface KeyCallback extends JSObject {
        void call(int keyCode, boolean down);
    }

    // ---- lifecycle / misc ----

    @JSBody(script = "return DRHost.jarBytes();")
    public static native Int8Array jarBytes();

    @JSBody(script = "return DRHost.languages();")
    public static native String languages();

    @JSBody(params = "msg", script = "DRHost.log(msg);")
    public static native void log(String msg);

    @JSBody(params = "msg", script = "DRHost.fatal(msg);")
    public static native void fatal(String msg);

    @JSBody(params = "reason", script = "DRHost.exited(reason);")
    public static native void exited(String reason);

    @JSBody(params = "url", script = "DRHost.openUrl(url);")
    public static native void openUrl(String url);

    @JSBody(params = "ms", script = "DRHost.vibrate(ms);")
    public static native void vibrate(int ms);

    /** Suspends the calling Java thread until the next animation frame (or a timeout when hidden). */
    @Async
    public static native Void nextFrame();

    private static void nextFrame(AsyncCallback<Void> callback) {
        requestFrame(() -> callback.complete(null));
    }

    @JSBody(params = "cb", script = "DRHost.requestFrame(cb);")
    private static native void requestFrame(Callback cb);

    // ---- screen / input ----

    @JSBody(params = { "w", "h" }, script = "return DRHost.createScreen(w, h);")
    public static native JSObject createScreen(int w, int h);

    @JSBody(params = "cb", script = "DRHost.setKeyHandler(cb);")
    public static native void setKeyHandler(KeyCallback cb);

    // ---- 2D drawing ----

    @JSBody(params = { "w", "h" }, script = "return DRHost.createCanvas(w, h);")
    public static native JSObject createCanvas(int w, int h);

    @JSBody(params = "canvas", script = "return DRHost.context(canvas);")
    public static native JSObject context(JSObject canvas);

    @JSBody(params = { "ctx", "rgb" }, script = "DRHost.setColor(ctx, rgb);")
    public static native void setColor(JSObject ctx, int rgb);

    @JSBody(params = { "ctx", "x", "y", "w", "h" }, script = "ctx.fillRect(x, y, w, h);")
    public static native void fillRect(JSObject ctx, int x, int y, int w, int h);

    @JSBody(params = { "ctx", "x", "y", "w", "h" }, script = "DRHost.clip(ctx, x, y, w, h);")
    public static native void clip(JSObject ctx, int x, int y, int w, int h);

    @JSBody(params = "ctx", script = "ctx.restore();")
    public static native void unclip(JSObject ctx);

    @JSBody(params = { "ctx", "src", "sx", "sy", "w", "h", "dx", "dy" },
            script = "ctx.drawImage(src, sx, sy, w, h, dx, dy, w, h);")
    public static native void blit(JSObject ctx, JSObject src, int sx, int sy, int w, int h, int dx, int dy);

    @JSBody(params = { "ctx", "src", "sx", "sy", "w", "h", "t", "dx", "dy" },
            script = "DRHost.blitT(ctx, src, sx, sy, w, h, t, dx, dy);")
    public static native void blitTransformed(JSObject ctx, JSObject src, int sx, int sy, int w, int h, int t,
            int dx, int dy);

    @JSBody(params = { "ctx", "x1", "y1", "x2", "y2" }, script = "DRHost.line(ctx, x1, y1, x2, y2);")
    public static native void line(JSObject ctx, int x1, int y1, int x2, int y2);

    @JSBody(params = { "ctx", "x", "y", "w", "h", "aw", "ah", "fill" },
            script = "DRHost.roundRect(ctx, x, y, w, h, aw, ah, fill);")
    public static native void roundRect(JSObject ctx, int x, int y, int w, int h, int aw, int ah, boolean fill);

    @JSBody(params = { "ctx", "x", "y", "w", "h", "start", "arc", "fill" },
            script = "DRHost.arc(ctx, x, y, w, h, start, arc, fill);")
    public static native void arc(JSObject ctx, int x, int y, int w, int h, int start, int arc, boolean fill);

    @JSBody(params = { "ctx", "x1", "y1", "x2", "y2", "x3", "y3" },
            script = "DRHost.triangle(ctx, x1, y1, x2, y2, x3, y3);")
    public static native void triangle(JSObject ctx, int x1, int y1, int x2, int y2, int x3, int y3);

    @JSBody(params = { "ctx", "text", "x", "baseline", "font" }, script = "DRHost.text(ctx, text, x, baseline, font);")
    public static native void text(JSObject ctx, String text, int x, int baseline, String font);

    @JSBody(params = { "font", "text" }, script = "return DRHost.measure(font, text);")
    public static native int measure(String font, String text);

    @JSBody(params = { "argb", "offset", "scan", "w", "h", "alpha" },
            script = "return DRHost.rgbImage(argb, offset, scan, w, h, alpha);")
    public static native JSObject rgbImage(Int32Array argb, int offset, int scan, int w, int h, boolean alpha);

    @JSBody(params = { "canvas", "out", "offset", "scan", "x", "y", "w", "h" },
            script = "DRHost.getRGB(canvas, out, offset, scan, x, y, w, h);")
    public static native void getRGB(JSObject canvas, Int32Array out, int offset, int scan, int x, int y, int w, int h);

    // ---- storage ----

    @JSBody(params = "key", script = "return DRHost.storeGet(key);")
    public static native String storeGet(String key);

    @JSBody(params = { "key", "value" }, script = "DRHost.storeSet(key, value);")
    public static native void storeSet(String key, String value);

    @JSBody(params = "key", script = "DRHost.storeRemove(key);")
    public static native void storeRemove(String key);

    @JSBody(params = "prefix", script = "return DRHost.storeKeys(prefix);")
    public static native String storeKeys(String prefix);

    // ---- MIDI audio ----

    @JSBody(params = "bytes", script = "return DRHost.midiLoad(bytes);")
    public static native int midiLoad(Int8Array bytes);

    @JSBody(params = "id", script = "return DRHost.midiDuration(id);")
    public static native double midiDuration(int id);

    @JSBody(params = { "id", "from", "gain", "onEnd" }, script = "DRHost.midiPlay(id, from, gain, onEnd);")
    public static native void midiPlay(int id, double from, double gain, Callback onEnd);

    /** Stops playback and returns the position reached, in seconds. */
    @JSBody(params = "id", script = "return DRHost.midiStop(id);")
    public static native double midiStop(int id);

    @JSBody(params = { "id", "gain" }, script = "DRHost.midiGain(id, gain);")
    public static native void midiGain(int id, double gain);

    @JSBody(params = "id", script = "DRHost.midiFree(id);")
    public static native void midiFree(int id);
}
