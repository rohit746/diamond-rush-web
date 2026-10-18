import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public final class Text {
   private static Text instance = null;
   private static DataInputStream stream = null;
   private static String locale;

   private Text() {
   }

   public static String replaceAll(String var0, String var1, String var2) {
      int i;
      do {
         if ((i = var0.indexOf(var1)) >= 0) {
            var0 = var0.substring(0, i) + var2 + var0.substring(i + var1.length());
         }
      } while (i >= 0);

      return var0;
   }

   public static String get(int var0) {
      return read(var0, null);
   }

   private static synchronized String read(int var0, String[] var1) {
      if (locale == null) {
         locale = System.getProperty("microedition.locale");
      }

      try {
         if (instance == null) {
            instance = new Text();
         }

         if (stream == null) {
            InputStream inputstream;
            if ((inputstream = instance.getClass().getResourceAsStream("/lang." + locale)) == null) {
               inputstream = instance.getClass().getResourceAsStream("/lang.xx");
            }

            if (inputstream == null) {
               return "X";
            }

            (stream = new DataInputStream(inputstream)).mark(512);
         }

         stream.skipBytes(var0 << 1);
         int i = stream.readUnsignedShort();
         stream.skipBytes(i - (var0 << 1) - 2);
         String s = stream.readUTF();
         if (!stream.markSupported()) {
            stream.close();
            stream = null;
         } else {
            try {
               stream.reset();
            } catch (IOException ioexception) {
               stream.close();
               stream = null;
            }
         }

         return s;
      } catch (IOException ioexception1) {
         stream = null;
         return "E";
      }
   }

   private static boolean matchesPattern(String var0, String var1, int var2, int var3) {
      while (var2 != var0.length() || var3 != var1.length()) {
         if (var2 == var0.length() || var3 == var1.length()) {
            return false;
         }

         switch (var1.charAt(var3)) {
            case '*':
               if (var3 == var1.length() - 1) {
                  return true;
               }

               if (matchesPattern(var0, var1, var2, var3 + 1)) {
                  return true;
               }

               var2++;
               break;
            case '?':
               var2++;
               var3++;
               break;
            default:
               if (var0.charAt(var2) != var1.charAt(var3)) {
                  return false;
               }

               var2++;
               var3++;
         }
      }

      return true;
   }

   private static StringBuffer readContinuation(InputStream var0) {
      StringBuffer stringbuffer = new StringBuffer();

      try {
         if ((char)var0.read() != ' ') {
            return stringbuffer;
         }

         int i;
         while ((i = var0.read()) >= 0) {
            if ((char)i != '\r') {
               if ((char)i == '\n') {
                  stringbuffer.append((Object)readContinuation(var0));
                  break;
               }

               stringbuffer.append((char)i);
            }
         }
      } catch (IOException ioexception) {
      }

      return stringbuffer;
   }

   static {
      String s = System.getProperty("microedition.platform");
      boolean flag = false;
      StringBuffer stringbuffer = new StringBuffer();
      if (instance == null) {
         instance = new Text();
      }

      label78: {
         InputStream inputstream;
         if ((inputstream = instance.getClass().getResourceAsStream("/META-INF/MANIFEST.MF")) != null) {
            label69:
            while (true) {
               while (true) {
                  try {
                     int i;
                     if ((i = inputstream.read()) < 0) {
                        break label78;
                     }

                     if ((char)i != '\r') {
                        if ((char)i != '\n') {
                           stringbuffer.append((char)i);
                        } else if (!stringbuffer.toString().trim().startsWith("Nokia-Platform:")) {
                           stringbuffer.delete(0, stringbuffer.length());
                        } else {
                           stringbuffer.append((Object)readContinuation(inputstream));
                           break;
                        }
                     }
                  } catch (IOException ioexception) {
                  }
               }

               String s2 = stringbuffer.toString().trim().substring(15);
               Vector vector = new Vector();
               int j;

               while ((j = s2.indexOf("@")) != -1) {
                  vector.addElement(s2.substring(0, j));
                  s2 = s2.substring(j + 1, s2.length());
               }

               vector.addElement(s2);
               int k = 0;

               while (true) {
                  if (k >= vector.size()) {
                     break label69;
                  }

                  String s1 = ((String)vector.elementAt(k)).trim();
                  if (matchesPattern(s, s1, 0, 0)) {
                     flag = true;
                     break label69;
                  }

                  k++;
               }
            }
         }

         if (!flag) {
            System.exit(0);
         }
      }

      locale = null;
   }
}
