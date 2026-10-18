import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public final class GloftDIRU extends MIDlet {
   public static byte[] versionBytes = null;
   private GameCanvas canvas;
   public Display display;
   public static String exitUrl = null;

   public GloftDIRU() {
      versionBytes = this.getAppProperty("MIDlet-Version").getBytes();
      this.canvas = new GameCanvas(this);
   }

   public final void startApp() {
      if (this.display == null) {
         this.display = Display.getDisplay(this);
      }

      this.display.setCurrent(this.canvas);
      this.canvas.resumeGame();
   }

   public final void pauseApp() {
      this.canvas.pauseGame();
      this.notifyPaused();
   }

   public final void destroyApp(boolean var1) throws MIDletStateChangeException {
      if (exitUrl != null) {
         try {
            this.platformRequest(exitUrl);
         } catch (Exception exception) {
         }
      }

      this.notifyDestroyed();
   }

   public final void exitGame() {
      GameCanvas.requestQuit();
      this.canvas.sound.shutdown();

      try {
         this.destroyApp(true);
      } catch (MIDletStateChangeException midletstatechangeexception) {
      }
   }
}
