package javax.microedition.lcdui;

/** Fixed-pitch metrics: any deterministic answer works, as long as both builds get the same one. */
public final class Font {
    public static final int FACE_SYSTEM = 0;
    public static final int FACE_MONOSPACE = 32;
    public static final int FACE_PROPORTIONAL = 64;
    public static final int STYLE_PLAIN = 0;
    public static final int STYLE_BOLD = 1;
    public static final int STYLE_ITALIC = 2;
    public static final int STYLE_UNDERLINED = 4;
    public static final int SIZE_SMALL = 8;
    public static final int SIZE_MEDIUM = 0;
    public static final int SIZE_LARGE = 16;

    final int id;
    private final int face;
    private final int style;
    private final int size;
    private final int advance;
    private final int height;

    private Font(int face, int style, int size) {
        this.face = face;
        this.style = style;
        this.size = size;
        this.id = face * 100 + style * 10 + size;
        this.advance = size == SIZE_SMALL ? 6 : size == SIZE_LARGE ? 8 : 7;
        this.height = size == SIZE_SMALL ? 14 : size == SIZE_LARGE ? 19 : 16;
    }

    public static Font getDefaultFont() {
        return new Font(FACE_SYSTEM, STYLE_PLAIN, SIZE_MEDIUM);
    }

    public static Font getFont(int face, int style, int size) {
        return new Font(face, style, size);
    }

    public int getFace() {
        return face;
    }

    public int getStyle() {
        return style;
    }

    public int getSize() {
        return size;
    }

    public int getHeight() {
        return height;
    }

    public int getBaselinePosition() {
        return height - 3;
    }

    public int charWidth(char c) {
        return c == ' ' ? advance - 2 : advance;
    }

    public int charsWidth(char[] ch, int offset, int length) {
        return stringWidth(new String(ch, offset, length));
    }

    public int stringWidth(String s) {
        int w = 0;
        for (int i = 0; i < s.length(); i++) {
            w += charWidth(s.charAt(i));
        }
        return w;
    }

    public int substringWidth(String s, int offset, int len) {
        return stringWidth(s.substring(offset, offset + len));
    }
}
