package javax.microedition.lcdui;

import dr.web.Host;
import java.util.Arrays;

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
    public static final int FONT_STATIC_TEXT = 0;
    public static final int FONT_INPUT_TEXT = 1;

    private static final String FAMILY = "'Nokia Sans S60', 'Nokia Sans', Tahoma, Verdana, Arial, sans-serif";
    private static final Font[] CACHE = new Font[3 * 8 * 3];

    private final int face;
    private final int style;
    private final int size;
    private final int height;
    private final int ascent;
    final String css;
    private final int[] charWidths = new int[256];

    private Font(int face, int style, int size) {
        this.face = face;
        this.style = style;
        this.size = size;
        // Approximates the S60 3rd edition QVGA system fonts the game was laid out for.
        int px;
        if (size == SIZE_SMALL) {
            px = 12;
            height = 14;
            ascent = 11;
        } else if (size == SIZE_LARGE) {
            px = 16;
            height = 19;
            ascent = 15;
        } else {
            px = 14;
            height = 16;
            ascent = 13;
        }
        css = ((style & STYLE_ITALIC) != 0 ? "italic " : "") + ((style & STYLE_BOLD) != 0 ? "bold " : "") + px + "px "
                + (face == FACE_MONOSPACE ? "monospace" : FAMILY);
        Arrays.fill(charWidths, -1);
    }

    public static Font getDefaultFont() {
        return getFont(FACE_SYSTEM, STYLE_PLAIN, SIZE_MEDIUM);
    }

    public static Font getFont(int fontSpecifier) {
        return getDefaultFont();
    }

    public static Font getFont(int face, int style, int size) {
        int faceIndex = face == FACE_MONOSPACE ? 1 : face == FACE_PROPORTIONAL ? 2 : 0;
        int sizeIndex = size == SIZE_SMALL ? 1 : size == SIZE_LARGE ? 2 : 0;
        int index = (faceIndex * 8 + (style & 7)) * 3 + sizeIndex;
        Font font = CACHE[index];
        if (font == null) {
            font = new Font(face, style & 7, size);
            CACHE[index] = font;
        }
        return font;
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

    public boolean isPlain() {
        return style == STYLE_PLAIN;
    }

    public boolean isBold() {
        return (style & STYLE_BOLD) != 0;
    }

    public boolean isItalic() {
        return (style & STYLE_ITALIC) != 0;
    }

    public boolean isUnderlined() {
        return (style & STYLE_UNDERLINED) != 0;
    }

    public int getHeight() {
        return height;
    }

    public int getBaselinePosition() {
        return ascent;
    }

    public int charWidth(char ch) {
        if (ch < 256) {
            int w = charWidths[ch];
            if (w < 0) {
                w = Host.measure(css, String.valueOf(ch));
                charWidths[ch] = w;
            }
            return w;
        }
        return Host.measure(css, String.valueOf(ch));
    }

    public int charsWidth(char[] ch, int offset, int length) {
        return stringWidth(new String(ch, offset, length));
    }

    public int stringWidth(String str) {
        return str.isEmpty() ? 0 : Host.measure(css, str);
    }

    public int substringWidth(String str, int offset, int len) {
        return stringWidth(str.substring(offset, offset + len));
    }
}
