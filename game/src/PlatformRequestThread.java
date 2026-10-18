public final class PlatformRequestThread extends Thread {
   private final String url;

   public PlatformRequestThread(String var1) {
      this.url = var1;
   }

   public final void run() {
      boolean flag;
      try {
         flag = GameCanvas.midlet.getAppProperty("APP-RUNNING-ON-PLATFORMREQUEST").trim().equals("0");
      } catch (Exception exception1) {
         flag = false;
      }

      try {
         if (flag) {
            GloftDIRU.exitUrl = this.url;
            GameCanvas.gameState = 3;
         } else {
            GameCanvas.midlet.platformRequest(this.url);
         }
      } catch (Exception exception) {
         GloftDIRU.exitUrl = this.url;
      }
   }
}
