import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;

public final class SoundManager implements Runnable, PlayerListener {
   private static int playingSound = -1;
   private static int requestedSound = -1;
   private static int playingPriority;
   private static int requestedPriority;
   private static int prefetchedSound = -1;
   private long playStartTime = 0L;
   private static Player[] players;
   public static boolean enabled;
   private static boolean shuttingDown;
   private static boolean stopRequested = false;
   private ByteArrayInputStream clipStream;
   private InputStream packStream;
   private byte[] packIndex;
   private Thread thread;

   public final void loadSound(int var1) {
      if (players == null) {
         players = new Player[21];
      }

      try {
         byte[] abyte = new byte[GameCanvas.readShort(this.packIndex, (var1 << 3) + 4)];
         this.packStream.read(abyte);
         this.clipStream = new ByteArrayInputStream(abyte);
         players[var1] = Manager.createPlayer(this.clipStream, "audio/midi");
         players[var1].addPlayerListener(this);
         players[var1].realize();
         ((VolumeControl)players[var1].getControl("VolumeControl")).setLevel(100);
         this.clipStream.close();
         this.clipStream = null;
         System.gc();
      } catch (Exception exception) {
      }
   }

   public final void startThread() {
      shuttingDown = false;
      this.thread = new Thread(this);
      this.thread.setPriority(1);
      this.thread.start();
   }

   public final void openPack() {
      if (players == null) {
         try {
            this.packStream = this.getClass().getResourceAsStream("/snd.f");
            this.packStream.skip(1L);
            this.packIndex = new byte[168];
            this.packStream.read(this.packIndex);
            return;
         } catch (Exception exception) {
            exception.printStackTrace();
         }
      }
   }

   public final void closePack() {
      try {
         playingSound = -1;
         prefetchedSound = -1;
         playingPriority = 0;
         this.packStream.close();
         this.packStream = null;
         this.packIndex = null;
         System.gc();
      } catch (Exception exception) {
      }
   }

   public final void shutdown() {
      if (players != null) {
         this.stop();
         int j = players.length;

         for (int i = 0; i < j; i++) {
            if (players[i] != null) {
               players[i].close();
               players[i] = null;
            }
         }
      }

      players = null;
      shuttingDown = true;
   }

   private static int priorityOf(int var0) {
      switch (var0) {
         case 0:
         case 5:
         case 6:
         case 10:
         case 14:
            return 10;
         case 1:
         case 2:
         case 4:
         case 15:
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
            return 30;
         case 3:
         case 7:
         case 8:
         case 9:
         case 11:
         case 12:
         case 13:
            return 20;
         default:
            return 0;
      }
   }

   public final synchronized void run() {
      while (!shuttingDown) {
         try {
            this.wait();
         } catch (Exception exception2) {
         }

         if (stopRequested) {
            try {
               int i = playingSound;
               boolean flag;
               switch (playingSound) {
                  case 0:
                  case 5:
                  case 6:
                  case 7:
                  case 8:
                  case 10:
                  case 11:
                  case 12:
                  case 13:
                  case 14:
                     flag = false;
                     break;
                  case 1:
                  case 2:
                  case 3:
                  case 4:
                  case 9:
                  default:
                     flag = true;
               }

               if (flag) {
                  players[playingSound].deallocate();
                  prefetchedSound = -1;
               }

               playingSound = -1;
               stopRequested = false;
            } catch (Exception exception1) {
            } finally {
               playingPriority = 0;
            }
         }

         if (requestedSound != -1) {
            try {
               if (prefetchedSound != -1 && requestedSound != prefetchedSound) {
                  players[prefetchedSound].deallocate();
                  prefetchedSound = -1;
               }

               if (prefetchedSound == -1) {
                  players[requestedSound].prefetch();
                  prefetchedSound = requestedSound;
               }

               players[requestedSound].start();
               playingSound = requestedSound;
               playingPriority = requestedPriority;
               this.playStartTime = System.currentTimeMillis();
            } catch (Exception exception) {
            } finally {
               requestedSound = -1;
            }
         }
      }
   }

   public final synchronized void play(int var1) {
      if (enabled) {
         if (playingSound != -1) {
            if (playingPriority >= priorityOf(var1)
               && (playingPriority != priorityOf(var1) || Math.abs(System.currentTimeMillis() - this.playStartTime) <= 50L)) {
               return;
            }

            stopRequested = true;
         } else {
            playingPriority = 0;
         }

         int i;
         if ((i = priorityOf(var1)) >= playingPriority) {
            requestedSound = var1;
            requestedPriority = i;
            this.notify();
         }
      }
   }

   public final synchronized void stop() {
      if (playingSound != -1) {
         stopRequested = true;
      }

      this.notify();
   }

   public static synchronized boolean isPlaying() {
      return playingSound != -1;
   }

   public final void playerUpdate(Player var1, String var2, Object var3) {
      if (players != null && playingSound != -1 && var2.equals("endOfMedia")) {
         this.stop();
      }
   }
}
