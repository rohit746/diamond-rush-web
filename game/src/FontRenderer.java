import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public final class FontRenderer {
   public static int measuredWidth;
   public static int measuredHeight;
   private static int fontSize = 0;
   public static int textColor = 0;
   public int unusedSpacing = 0;
   public int[] unusedInts1 = new int[2];
   public int[] unusedInts2 = new int[3];
   private static byte[] mcTableUnused;
   public Font font;

   public FontRenderer(int var1) {
      fontSize = var1;
      textColor = 16777215;
      this.font = Font.getFont(0, 1, var1);
      this.unusedInts1[0] = 4;
      this.unusedInts1[1] = 14;
      this.unusedInts2[0] = 0;
      this.unusedInts2[0] = 0;
      this.unusedInts2[0] = 0;
      InputStream inputstream = this.getClass().getResourceAsStream("/mc");
      mcTableUnused = new byte[256];

      try {
         inputstream.read(mcTableUnused);
         inputstream.close();
      } catch (Exception exception) {
      }
   }

   public static int getFontSize() {
      return fontSize;
   }

   public final void drawLines(Graphics var1, String var2, int var3, int var4, int var5) {
      String[] astring = splitLines(var2);
      int i = this.font.getHeight();

      for (int j = 0; j < astring.length; j++) {
         this.drawString(var1, astring[j], var3, var4 + j * i, var5);
      }
   }

   private static String[] toTrimmedArray(Vector var0) {
      String[] astring = new String[var0.size()];
      Enumeration enumeration = var0.elements();

      for (int i = 0; enumeration.hasMoreElements(); i++) {
         astring[i] = ((String)enumeration.nextElement()).trim();
      }

      return astring;
   }

   private static String[] splitLines(String var0) {
      Vector vector = new Vector();
      String s = "";

      for (int i = 0; i < var0.length(); i++) {
         char c0 = var0.charAt(i);
         s = s + c0;
         if (c0 == '\n' || i == var0.length() - 1) {
            vector.addElement(s);
            s = "";
         }
      }

      return toTrimmedArray(vector);
   }

   public final void measure(String var1) {
      measuredWidth = 0;
      String[] astring = splitLines(var1);

      for (int j = 0; j < astring.length; j++) {
         int i;
         if ((i = this.font.stringWidth(astring[j])) > measuredWidth) {
            measuredWidth = i;
         }
      }

      measuredHeight = astring.length * this.font.getHeight();
   }

   public final void drawStringColored(Graphics var1, String var2, int var3, int var4, int var5, int var6) {
      var4 -= 12;
      this.measure(var2);
      var1.setColor(var5);
      var1.setFont(this.font);
      var1.drawString(var2, var3, var4, 0);
   }

   public final void drawString(Graphics var1, String var2, int var3, int var4, int var5) {
      var4 -= 12;
      label16:
      if ((var5 & 43) != 0) {
         this.measure(var2);
         int i;
         int j;
         if ((var5 & 8) != 0) {
            i = var3;
            j = measuredWidth;
         } else {
            if ((var5 & 1) == 0) {
               break label16;
            }

            i = var3;
            j = measuredWidth >> 1;
         }

         var3 = i - j;
      }

      var1.setColor(16777215);
      var1.setFont(this.font);
      var1.drawString(var2, var3, var4, 0);
   }

   public final int getHeight() {
      return this.font.getHeight();
   }

   public static void resetColor() {
      textColor = 16777215;
   }

   public static void setColor(int var0) {
      textColor = var0;
   }

   public final int getTextHeight(String var1) {
      int i = 1;
      int j = var1.length();

      for (int k = var1.indexOf(10); k != -1; k = k < j - 1 ? var1.indexOf(10, k + 1) : -1) {
         i++;
      }

      return this.font.getHeight() * i;
   }
}
