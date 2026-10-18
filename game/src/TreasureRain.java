import javax.microedition.lcdui.Graphics;

public final class TreasureRain {
   public static byte[][] grid;
   private static byte scrollY = 0;
   private static byte scrollSpeed = 4;
   private static int animFrame = 0;
   private static byte unusedC = 0;
   private static byte unusedD = 2;
   private static boolean unusedFlag = false;
   public static int animFrameCount = 0;

   public static void update(int var0) {
      if ((scrollY = (byte)(scrollY + scrollSpeed)) > 24) {
         scrollY = 0;

         for (int j = 9; j >= 1; j--) {
            for (int i = 0; i < 15; i++) {
               grid[i][j] = grid[i][j - 1];
            }
         }

         spawnRow(3);
      }
   }

   public static void spawnRow(int var0) {
      for (int i = 0; i < 15; i++) {
         grid[i][0] = 0;
      }

      int j = 0;

      for (int k = 0; k < var0; k++) {
         int l;
         do {
            l = Rand.between(0, 15);
         } while (grid[l][0] != 0);

         j = Rand.between(1, 3);
         grid[l][0] = (byte)j;
      }
   }

   public static void paint(Graphics var0) {
      for (int i = 0; i < 15; i++) {
         for (int j = 0; j < 10; j++) {
            Sprite sprite;
            Graphics graphics;
            int k;
            if (grid[i][j] == 1) {
               sprite = GameCanvas.sprites[GameCanvas.themedSpriteIndex(2)];
               graphics = var0;
               k = animFrame;
            } else {
               if (grid[i][j] != 2) {
                  continue;
               }

               sprite = GameCanvas.sprites[GameCanvas.themedSpriteIndex(2)];
               graphics = var0;
               k = 0;
            }

            sprite.paintFrame(graphics, k, i * 24, (j - 1) * 24 + scrollY, 0, 0, 0);
         }
      }

      animFrame = (animFrame + 1) % animFrameCount;
   }
}
