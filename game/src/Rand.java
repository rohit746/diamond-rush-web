import java.util.Random;

public final class Rand {
   private static Random random = new Random(System.currentTimeMillis());

   public static int between(int var0, int var1) {
      return var0 + Math.abs(random.nextInt()) % (var1 - var0);
   }
}
