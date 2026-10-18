import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public final class Cutscene {
   private static long startDelayMs = 1000L;
   private static String spritesFile = "/demoSpr.bin";
   private static String scriptsFile = "/demo.f";
   public static Sprite[] sprites = null;
   public byte[][] steps = null;
   private int nextStepIndex = 0;
   private long startTime;
   public byte[] currentStep = null;
   private int stepTick;
   private boolean portraitVisible = false;
   private int portraitX = 0;
   private int portraitY = 0;
   private int portraitFrame = 0;
   private int portraitSprite = 0;
   private int keyHintFrame = -1;
   public boolean skipping = false;
   private static GameCanvas game = null;
   private short[] lineBreaks = new short[16];
   private int lineCount;

   public Cutscene(GameCanvas var1) {
      game = var1;
   }

   private void paintCommand(Graphics var1, byte[] var2, int var3) {
      if (var2 != null) {
         byte b0;
         switch (b0 = var2[var3]) {
            case 1:
               int l3 = this.stepTick;
               short short10 = (short)GameCanvas.readShort(var2, var3 + 2);
               short short13 = (short)GameCanvas.readShort(var2, var3 + 4);
               short short19 = (short)GameCanvas.readShort(var2, var3 + 6);
               short short15 = (short)GameCanvas.readShort(var2, var3 + 8);
               short short17 = (short)GameCanvas.readShort(var2, var3 + 10);
               if (short15 == 10000) {
                  short15 = (short)game.cameraTargetX;
               }

               if (short17 == 10000) {
                  short17 = (short)game.cameraTargetY;
               }

               if (l3 > short19) {
                  l3 = short19;
               }

               int l;
               int i1;
               label113: {
                  short10 = (short)(short10 - 148);
                  short13 = (short)(short13 - 77);
                  game.cameraTargetX = (short)((short10 * l3 + short15 * (short19 - l3)) / short19);
                  game.cameraTargetY = (short)((short13 * l3 + short17 * (short19 - l3)) / short19);
                  l = game.mapWidth * 24 - 320;
                  i1 = game.mapHeight * 24 - 240 + 62;
                  GameCanvas gamecanvas1;
                  int j4;
                  if (game.cameraTargetX > l) {
                     gamecanvas1 = game;
                     j4 = l;
                  } else {
                     if (game.cameraTargetX >= 0) {
                        break label113;
                     }

                     gamecanvas1 = game;
                     j4 = 0;
                  }

                  gamecanvas1.cameraTargetX = j4;
               }

               label107: {
                  GameCanvas gamecanvas;
                  int i4;
                  if (game.cameraTargetY > i1) {
                     gamecanvas = game;
                     i4 = i1;
                  } else {
                     if (game.cameraTargetY >= 0) {
                        break label107;
                     }

                     gamecanvas = game;
                     i4 = 0;
                  }

                  gamecanvas.cameraTargetY = i4;
               }

               game.cameraX = game.cameraTargetX;
               game.cameraY = game.cameraTargetY;
               if (game.cameraX > l) {
                  game.cameraX -= l;
               }

               if (game.cameraY > i1) {
                  game.cameraY = i1;
                  return;
               }

               if (game.cameraY < 0) {
                  return;
               }
               break;
            case 2:
               short short9 = (short)GameCanvas.readShort(var2, var3 + 6);
               short short5;
               if ((short5 = (short)GameCanvas.readShort(var2, var3 + 4)) == 10000) {
                  short5 = -320;
               }

               String s1 = null;

               try {
                  s1 = new String(var2, var3 + 11, (short)GameCanvas.readShort(var2, var3 + 2), "ISO-8859-1");
               } catch (Exception exception1) {
               }

               if (s1 != null) {
                  try {
                     int i2 = Integer.parseInt(s1.trim());
                     s1 = GameCanvas.tutorialStrings[i2];
                  } catch (Exception exception) {
                  }
               }

               int j2 = wrapText(s1, 302).length * GameCanvas.fontSmall.font.getHeight() + 2;
               GameCanvas.drawFramedBox(var1, short5, short9 - 11, 306, j2, 73, 0);
               drawWrapped(var1, s1, short5 + 2, short9 + 2, 302);
               if (GameCanvas.stateFrames / 2 % 4 < 2) {
                  var1.drawImage(GameCanvas.demoUiImages[0][9], short5 + 306 - 10, short9 + j2 + 2, 17);
               }

               var1.setClip(0, 0, 320, 240);
               break;
            case 4:
               int k3 = this.stepTick;
               short short14 = (short)GameCanvas.readShort(var2, var3 + 2);
               short short16 = (short)GameCanvas.readShort(var2, var3 + 4);
               short short8 = (short)GameCanvas.readShort(var2, var3 + 6);
               short short12 = (short)GameCanvas.readShort(var2, var3 + 6);
               short short18 = (short)GameCanvas.readShort(var2, var3 + 14);
               if (k3 > short18) {
                  k3 = short18;
               }

               short short4 = (short)((short8 * k3 + short14 * (short18 - k3)) / short18);
               short8 = (short)((short12 * k3 + short16 * (short18 - k3)) / short18);
               short12 = (short)GameCanvas.readShort(var2, var3 + 10);
               short short6 = (short)GameCanvas.readShort(var2, var3 + 12);
               sprites[short12].paintFrame(var1, short6, short4, short8, 0, 0, 0);
               return;
            case 12:
               int j3 = this.stepTick;
               var1.setColor(16777215);
               if (j3 > 5) {
                  j3 = 5;
               }

               int l1 = game.playerX * 24;
               var3 = game.playerY * 24;
               int k2 = this.portraitX;
               int l2 = this.portraitY;
               var1.fillRect(((l1 - game.cameraX) * (5 - j3) + k2 * j3) / 5, ((var3 - game.cameraY) * (5 - j3) + l2 * j3) / 5, j3 * 102 / 5, j3 * 38 / 5);
               return;
            case 13:
               int i3 = this.stepTick;
               short short7 = (short)GameCanvas.readShort(var2, var3 + 2);
               short short11 = (short)GameCanvas.readShort(var2, var3 + 4);
               short short3 = (short)GameCanvas.readShort(var2, var3 + 6);
               short short1 = (short)GameCanvas.readShort(var2, var3 + 8);
               short short2 = (short)GameCanvas.readShort(var2, var3 + 10);
               if (i3 > short3) {
                  i3 = short3;
               }

               short7 = (short)((short7 * i3 + short1 * (short3 - i3)) / short3);
               short11 = (short)((short11 * i3 + short2 * (short3 - i3)) / short3);
               if (b0 == 13) {
                  this.portraitX = short7;
                  this.portraitY = short11;
                  return;
               }
               break;
            case 18:
               if (var2[var3 + 7] != 0) {
                  int j = (var2[var3 + 3] & 255) + ((var2[var3 + 3 + 1] & 255) << 8) + ((var2[var3 + 3 + 2] & 255) << 16);
                  var1.setColor(j);
                  var1.fillRect(0, 0, 320, 240);
                  return;
               }
               break;
            case 27:
               int k = GameCanvas.fontSmall.font.stringWidth(GameCanvas.strings[30]) + 10;
               int i = GameCanvas.fontSmall.font.getHeight() + 4;
               String s = null;

               try {
                  s = new String(var2, var3 + 8, (short)GameCanvas.readShort(var2, var3 + 2), "ISO-8859-1");
               } catch (Exception exception3) {
               }

               if (s != null) {
                  try {
                     int j1 = Integer.parseInt(s.trim());
                     s = GameCanvas.tutorialStrings[j1];
                  } catch (Exception exception2) {
                  }
               }

               int k1 = wrapText(s, 300).length * GameCanvas.fontSmall.font.getHeight() + 6;
               GameCanvas.drawTitledBox(var1, 6, 126, 306, k1, 73, 1, k, i);
               drawWrapped(var1, s, 8, 139, 300);
               GameCanvas.fontSmall.drawString(var1, GameCanvas.strings[30], 19, 129, 20);
               if (GameCanvas.stateFrames / 2 % 4 < 2) {
                  var1.drawImage(GameCanvas.demoUiImages[0][9], 303, 126, 17);
                  return;
               }
         }
      }
   }

   private void updateTextCommand(byte[] var1, int var2) {
      switch (var1[var2]) {
         case 2:
            String s1 = null;

            try {
               s1 = new String(var1, var2 + 11, (short)GameCanvas.readShort(var1, var2 + 2), "ISO-8859-1");
            } catch (Exception exception1) {
            }

            this.computeLineBreaks(s1, 302);
            short short3 = (short)(var1[var2 + 9] & 0xFF);
            short short4 = (short)(var1[var2 + 8] & 0xFF);
            byte[] abyte;
            int i;
            byte b0;
            if (short3 + short4 >= this.lineCount) {
               abyte = var1;
               i = var2 + 10;
               b0 = 1;
            } else {
               abyte = var1;
               i = var2 + 9;
               b0 = (byte)(short3 + short4);
            }

            abyte[i] = b0;
         default:
            return;
         case 7:
            var1[var2 + 1] = 1;
            return;
         case 27:
            if (!GameCanvas.bool_a) {
               String s = null;

               try {
                  s = new String(var1, var2 + 8, (short)GameCanvas.readShort(var1, var2 + 2), "ISO-8859-1");
               } catch (Exception exception) {
               }

               this.computeLineBreaks(s, 276);
               short short2 = (short)(var1[var2 + 6] & 0xFF);
               short short1 = (short)(var1[var2 + 8] & 0xFF);
               if (short2 + short1 >= this.lineCount) {
                  var1[var2 + 7] = 1;
               } else {
                  var1[var2 + 6] = (byte)(short2 + short1);
               }
            }
      }
   }

   public final void updateText() {
      if (this.currentStep != null) {
         if (Math.abs(this.startTime - System.currentTimeMillis()) >= startDelayMs) {
            if (this.currentStep[0] == 0) {
               byte b0 = this.currentStep[1];
               int i = 2 + (b0 << 2);

               for (int j = 0; j < b0; j++) {
                  int k = GameCanvas.readInt(this.currentStep, 2 + (j << 2));
                  this.updateTextCommand(this.currentStep, i);
                  i += k;
               }
            } else {
               this.updateTextCommand(this.currentStep, 0);
            }
         }
      }
   }

   public final void paint(Graphics var1) {
      if (!this.skipping) {
         Cutscene cutscene = this;
         switch (this.currentStep == null ? -1 : cutscene.currentStep[0]) {
            case 25:
            case 26:
               if (this.steps == null || this.nextStepIndex >= this.steps.length) {
                  return;
               }
               break;
            default:
               var1.setColor(0);
               var1.fillRect(0, 0, 320, 31);
               var1.fillRect(0, 209, 320, 31);
               GameCanvas.fontSmall.drawString(var1, GameCanvas.strings[65], 2, 240 - (GameCanvas.fontSmall.font.getHeight() - 10), 36);
         }

         if (this.currentStep[0] == 0) {
            byte b0 = this.currentStep[1];
            int i = 2 + (b0 << 2);

            for (int j = 0; j < b0; j++) {
               int k = GameCanvas.readInt(this.currentStep, 2 + (j << 2));
               this.paintCommand(var1, this.currentStep, i);
               i += k;
            }
         } else {
            this.paintCommand(var1, this.currentStep, 0);
         }

         if (this.portraitVisible) {
            var1.setColor(0);
            var1.fillRect(this.portraitX - 3, this.portraitY - 3, 109, 45);
            sprites[this.portraitSprite]
               .paintAnimFrame(var1, 0, this.stepTick % sprites[this.portraitSprite].getAnimFrameCount(0), this.portraitX, this.portraitY, 0, 0, 0);
            sprites[0].paintFrame(var1, this.portraitFrame, this.portraitX, this.portraitY, 0, 0, 0);
            if (this.keyHintFrame >= 0) {
               if (GameCanvas.rightToLeft && this.keyHintFrame == 2) {
                  this.keyHintFrame = 7;
               }

               if (GameCanvas.rightToLeft && this.keyHintFrame == 6) {
                  this.keyHintFrame = 8;
               }

               sprites[1].paintFrame(var1, this.keyHintFrame, this.portraitX + 90, this.portraitY + -6, 0, 0, 0);
            }
         }
      }
   }

   public final byte[] restart() {
      byte[][] abyte = this.steps;
      Cutscene cutscene = this;

      for (int i = 0; i < abyte.length; i++) {
         cutscene.resetCommand(abyte[i], 0);
      }

      this.portraitVisible = false;
      this.portraitX = 0;
      this.portraitY = 0;
      this.portraitFrame = 0;
      this.portraitSprite = 0;
      this.keyHintFrame = -1;
      this.skipping = false;
      this.nextStepIndex = 0;
      this.startTime = System.currentTimeMillis();
      return this.nextStep();
   }

   private void resetCommand(byte[] var1, int var2) {
      if (var1[var2] != 0) {
         var1[var2 + 1] = 0;
         byte[] abyte;
         int l;
         byte b1;
         switch (var1[var2]) {
            case 2:
               var1[var2 + 9] = 0;
               putShort(var1, var2 + 4, (short)-320);
               abyte = var1;
               l = var2;
               b1 = 10;
               break;
            case 27:
               var1[var2 + 6] = 0;
               putShort(var1, var2 + 4, (short)-320);
               abyte = var1;
               l = var2;
               b1 = 7;
               break;
            default:
               return;
         }

         abyte[l + b1] = 0;
      } else {
         byte b0 = var1[var2 + 1];
         int i = var2 + 2 + (b0 << 2);

         for (int j = 0; j < b0; j++) {
            int k = GameCanvas.readInt(var1, var2 + 2 + (j << 2));
            this.resetCommand(var1, i);
            i += k;
         }
      }
   }

   public final byte[] nextStep() {
      this.stepTick = 0;
      Cutscene cutscene;
      byte[] abyte;
      if (this.steps != null && this.nextStepIndex < this.steps.length) {
         cutscene = this;
         abyte = this.steps[this.nextStepIndex++];
      } else {
         cutscene = this;
         abyte = null;
      }

      cutscene.currentStep = abyte;
      return this.currentStep;
   }

   public final void update() {
      if (this.currentStep[0] == 0) {
         byte b0 = this.currentStep[1];
         int i = 2 + (b0 << 2);

         for (int j = 0; j < b0; j++) {
            int k = GameCanvas.readInt(this.currentStep, 2 + (j << 2));
            this.updateCommand(this.currentStep, i);
            i += k;
         }
      } else {
         this.updateCommand(this.currentStep, 0);
      }

      this.stepTick++;
   }

   private void updateCommand(byte[] var1, int var2) {
      short short1 = var1[var2];
      if (this.skipping) {
         switch (short1) {
            case 5:
            case 9:
            case 10:
            case 25:
            case 26:
               break;
            default:
               var1[var2 + 1] = 1;
               return;
         }
      }

      switch (short1) {
         case 1:
            if ((short)GameCanvas.readShort(var1, var2 + 8) == 10000) {
               putShort(var1, var2 + 8, (short)game.cameraTargetX);
            }

            if ((short)GameCanvas.readShort(var1, var2 + 10) == 10000) {
               putShort(var1, var2 + 10, (short)game.cameraTargetY);
            }

            short1 = (short)GameCanvas.readShort(var1, var2 + 6);
            if (this.stepTick > short1) {
               var1[var2 + 1] = 1;
               return;
            }
            break;
         case 2:
            if ((short1 = (short)GameCanvas.readShort(var1, var2 + 4)) == 10000) {
               short1 = -306;
            }

            short1 = (short)(short1 + 30);
            if (var1[var2 + 10] == 0) {
               if (short1 > 7) {
                  short1 = 7;
               }
            } else if (short1 > 320) {
               var1[var2 + 1] = 1;
            }

            putShort(var1, var2 + 4, short1);
         case 3:
         case 7:
         case 19:
         case 20:
         case 21:
         case 22:
         case 23:
         case 24:
         default:
            break;
         case 4:
            short1 = (short)GameCanvas.readShort(var1, var2 + 14);
            if (this.stepTick > short1) {
               var1[var2 + 1] = 1;
               return;
            }
            break;
         case 5:
            GameCanvas.setObject((short)GameCanvas.readShort(var1, var2 + 2), (short)GameCanvas.readShort(var1, var2 + 4), var1[var2 + 6], 0);
            var1[var2 + 1] = 1;
            return;
         case 6:
            if (this.stepTick > GameCanvas.readInt(var1, var2 + 2)) {
               var1[var2 + 1] = 1;
               return;
            }
            break;
         case 8:
            return;
         case 9:
            GameCanvas.setObject(
               (short)GameCanvas.readShort(var1, var2 + 2), (short)GameCanvas.readShort(var1, var2 + 4), (byte)0, (short)GameCanvas.readShort(var1, var2 + 6)
            );
            var1[var2 + 1] = 1;
            return;
         case 10:
            byte b2 = var1[var2 + 2];
            if (this.stepTick == 0) {
               game.playerFlags = game.playerFlags & -8 | b2;
               game.playerAction = b2;
            } else if (game.moveProgress <= 0) {
               game.playerAction = 0;
               var1[var2 + 1] = 1;
            }

            game.updateCamera();
            return;
         case 11:
            this.portraitSprite = (short)GameCanvas.readShort(var1, var2 + 4);
            this.portraitFrame = (short)GameCanvas.readShort(var1, var2 + 2);
            var1[var2 + 1] = 1;
            return;
         case 12:
            short1 = (short)GameCanvas.readShort(var1, var2 + 2);
            short short5 = (short)GameCanvas.readShort(var1, var2 + 4);
            this.portraitX = short1;
            this.portraitY = short5;
            if (this.stepTick > 5) {
               var1[var2 + 1] = 1;
               this.portraitVisible = true;
               return;
            }
            break;
         case 13:
            if ((short)GameCanvas.readShort(var1, var2 + 8) == 10000) {
               putShort(var1, var2 + 8, (short)this.portraitX);
            }

            if ((short)GameCanvas.readShort(var1, var2 + 10) == 10000) {
               putShort(var1, var2 + 10, (short)this.portraitY);
            }

            short1 = (short)GameCanvas.readShort(var1, var2 + 6);
            if (this.stepTick > short1) {
               var1[var2 + 1] = 1;
               return;
            }
            break;
         case 14:
         case 15:
            this.portraitVisible = short1 == 14;
            var1[var2 + 1] = 1;
            return;
         case 16:
         case 17:
         case 18:
            if (this.stepTick % 2 == 0) {
               return;
            }

            int k = var2 + (short1 == 18 ? 2 : 4);
            byte b3 = var1[k];
            short short3 = -1;
            int i = var2 + (short1 == 18 ? 7 : 5);
            boolean flag = var1[i] != 0;
            if (short1 != 18) {
               short3 = (short)GameCanvas.readShort(var1, var2 + 2);
            }

            if (flag) {
               flag = false;
               this.keyHintFrame = -1;
               if (--b3 == 0) {
                  var1[var2 + 1] = 1;
                  if (short1 != 18) {
                     this.keyHintFrame = short1 == 16 ? short3 : -1;
                  }
               }
            } else if (b3 > 0) {
               flag = true;
               if (short1 != 18) {
                  this.keyHintFrame = short3;
               }
            } else {
               var1[var2 + 1] = 1;
            }

            var1[i] = (byte)(flag ? 1 : 0);
            var1[k] = b3;
            return;
         case 25:
            short1 = (short)GameCanvas.readShort(var1, var2 + 2);
            short short4 = (short)GameCanvas.readShort(var1, var2 + 4);
            byte b1 = var1[var2 + 6];
            byte b0 = var1[var2 + 7];
            GameCanvas.tileMap[short1][short4] = b0 << 8 | b1;
            var1[var2 + 1] = 1;
            return;
         case 26:
            short1 = (short)GameCanvas.readShort(var1, var2 + 2);
            short short2 = (short)GameCanvas.readShort(var1, var2 + 4);
            int j = GameCanvas.readInt(var1, var2 + 6);
            GameCanvas.objectState[short1][short2] = j;
            var1[var2 + 1] = 1;
            return;
         case 27:
            if (var1[var2 + 7] != 0) {
               var1[var2 + 1] = 1;
               return;
            }
      }
   }

   public final boolean isStepFinished() {
      boolean flag = true;
      if (this.currentStep[0] == 0) {
         byte b0 = this.currentStep[1];
         int i = 2 + (b0 << 2);

         for (int j = 0; j < b0; j++) {
            int k = GameCanvas.readInt(this.currentStep, 2 + (j << 2));
            if (!(flag = flag && this.currentStep[i + 1] == 1)) {
               break;
            }

            i += k;
         }
      } else {
         flag = this.currentStep[1] == 1;
      }

      return flag;
   }

   public final void load(int var1) {
      try {
         ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(GameCanvas.readPackEntry(scriptsFile, 0));
         byte[] abyte = new byte[4];
         bytearrayinputstream.read(abyte, 0, 2);
         boolean flag = false;

         do {
            bytearrayinputstream.read(abyte, 0, 2);
            if ((short)GameCanvas.readShort(abyte, 0) != var1) {
               bytearrayinputstream.skip(2L);
               bytearrayinputstream.read(abyte, 0, 4);
               int l1 = GameCanvas.readInt(abyte, 0);
               bytearrayinputstream.skip(l1);
            } else {
               bytearrayinputstream.read(abyte, 0, 2);
               short short1 = (short)GameCanvas.readShort(abyte, 0);
               bytearrayinputstream.read(abyte, 0, 4);
               abyte = new byte[GameCanvas.readInt(abyte, 0)];
               bytearrayinputstream.read(abyte);
               bytearrayinputstream.close();
               bytearrayinputstream = null;
               flag = true;
               int i = 0;
               int j = (short)GameCanvas.readShort(abyte, 0);
               i += 2;
               if (j != 0) {
                  short[] ashort = new short[j];

                  for (int k = 0; k < j; k++) {
                     ashort[k] = (short)GameCanvas.readShort(abyte, i);
                     i += 2;
                  }

                  for (int i1 = 1; i1 < j; i1++) {
                     short short16 = ashort[i1];
                     int j5 = i1;

                     while (true) {
                        int l = j5;
                        if (j5 <= 0 || ashort[l - 1] <= short16) {
                           ashort[l] = short16;
                           break;
                        }

                        ashort[l] = ashort[l - 1];
                        j5 = l - 1;
                     }
                  }

                  InputStream inputstream = this.getClass().getResourceAsStream(spritesFile);
                  byte[] abyte1 = new byte[4];
                  inputstream.read(abyte1, 0, 2);
                  short short2 = (short)GameCanvas.readShort(abyte1, 0);
                  if (sprites == null) {
                     sprites = new Sprite[short2];
                  }

                  for (int j1 = 0; j1 < j; j1++) {
                     short short3 = ashort[j1];
                     boolean flag1 = false;

                     while (!flag1) {
                        inputstream.read(abyte1, 0, 2);
                        short short4 = (short)GameCanvas.readShort(abyte1, 0);
                        if (short3 == short4) {
                           flag1 = true;
                        }

                        inputstream.read(abyte1, 0, 4);
                        int k1 = GameCanvas.readInt(abyte1, 0);
                        if (!flag1 && sprites[short3] != null) {
                           inputstream.skip(k1);
                        } else {
                           abyte1 = new byte[k1];
                           inputstream.read(abyte1);
                           sprites[short3] = new Sprite();
                           sprites[short3].load(abyte1, 0);
                           sprites[short3].buildModuleImages(0, 0, -1, -1);
                           sprites[short3].moduleData = null;
                        }
                     }
                  }

                  inputstream.close();
               }

               byte[][] abyte2 = new byte[short1][];
               int l2 = 0;
               byte[][] abyte3 = null;
               int i3 = 0;
               int j3 = 0;

               for (int k3 = 0; k3 < short1 || j3 != 0; k3++) {
                  byte[][] abyte4;
                  if (j3 == 0) {
                     abyte4 = abyte2;
                  } else {
                     abyte4 = abyte3;
                     j3--;
                     k3--;
                  }

                  int l3;
                  byte[] abyte5;
                  label95: {
                     l3 = abyte[i++] & 255;
                     abyte5 = null;
                     byte[] abyte6;
                     switch (l3) {
                        case 0:
                           abyte3 = new byte[j3 = (short)(abyte[i++] & 0xFF)][];
                           abyte6 = null;
                           break;
                        case 1:
                           j = (short)(GameCanvas.readShort(abyte, i) * 24);
                           i += 2;
                           short short28 = (short)(GameCanvas.readShort(abyte, i) * 24);
                           i += 2;
                           short short35 = (short)GameCanvas.readShort(abyte, i);
                           i += 2;
                           putShort(abyte5 = new byte[12], 2, (short)j);
                           putShort(abyte5, 4, short28);
                           putShort(abyte5, 6, short35);
                           putShort(abyte5, 8, (short)10000);
                           putShort(abyte5, 10, (short)10000);
                           break label95;
                        case 2:
                           short short34 = (short)(abyte[i++] & 0xFF);
                           short short27 = (short)GameCanvas.readShort(abyte, i);
                           i += 2;
                           short short15 = (short)GameCanvas.readShort(abyte, i);
                           i += 2;
                           putShort(abyte5 = new byte[short15 + 11], 2, short15);
                           putShort(abyte5, 4, (short)10000);
                           putShort(abyte5, 6, short27);
                           abyte5[8] = (byte)short34;
                           abyte5[9] = 0;
                           abyte5[10] = 0;
                           System.arraycopy(abyte, i, abyte5, 11, short15);
                           i += short15;
                        case 3:
                        case 8:
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                        case 23:
                        case 24:
                        default:
                           break label95;
                        case 4:
                           short short26 = (short)GameCanvas.readShort(abyte, i);
                           i += 2;
                           short short36 = (short)GameCanvas.readShort(abyte, i);
                           i += 2;
                           short short6 = (short)GameCanvas.readShort(abyte, i);
                           i += 2;
                           short short7 = (short)GameCanvas.readShort(abyte, i);
                           i += 2;
                           short short8 = (short)GameCanvas.readShort(abyte, i);
                           i += 2;
                           j = (short)GameCanvas.readShort(abyte, i);
                           i += 2;
                           short short33 = (short)GameCanvas.readShort(abyte, i);
                           i += 2;
                           putShort(abyte5 = new byte[16], 2, short26);
                           putShort(abyte5, 4, short36);
                           putShort(abyte5, 6, short6);
                           putShort(abyte5, 8, short7);
                           putShort(abyte5, 10, short8);
                           putShort(abyte5, 12, (short)j);
                           putShort(abyte5, 14, short33);
                           break label95;
                        case 5:
                           short short14 = (short)GameCanvas.readShort(abyte, i);
                           int j2 = i + 2;
                           short short25 = (short)GameCanvas.readShort(abyte, j2);
                           i = j2 + 2;
                           short short32 = (short)(abyte[i++] & 0xFF);
                           putShort(abyte5 = new byte[7], 2, short14);
                           putShort(abyte5, 4, short25);
                           abyte5[6] = (byte)short32;
                           break label95;
                        case 6:
                           j = GameCanvas.readInt(abyte, i);
                           i += 4;
                           putInt(abyte5 = new byte[6], 2, j);
                           break label95;
                        case 7:
                           abyte6 = new byte[2];
                           break;
                        case 9:
                           short short13 = (short)GameCanvas.readShort(abyte, i);
                           i += 2;
                           short short24 = (short)GameCanvas.readShort(abyte, i);
                           i += 2;
                           short short31 = (short)GameCanvas.readShort(abyte, i);
                           i += 2;
                           putShort(abyte5 = new byte[8], 2, short13);
                           putShort(abyte5, 4, short24);
                           putShort(abyte5, 6, short31);
                           break label95;
                        case 10:
                           short short12 = (short)(abyte[i++] & 0xFF);
                           (abyte5 = new byte[3])[2] = (byte)short12;
                           break label95;
                        case 11:
                           j = (short)GameCanvas.readShort(abyte, i);
                           i += 2;
                           short short23 = (short)GameCanvas.readShort(abyte, i);
                           i += 2;
                           putShort(abyte5 = new byte[6], 2, (short)j);
                           putShort(abyte5, 4, short23);
                           break label95;
                        case 12:
                           j = (short)GameCanvas.readShort(abyte, i);
                           i += 2;
                           short short22 = (short)GameCanvas.readShort(abyte, i);
                           i += 2;
                           putShort(abyte5 = new byte[6], 2, (short)j);
                           putShort(abyte5, 4, short22);
                           break label95;
                        case 13:
                           j = (short)GameCanvas.readShort(abyte, i);
                           i += 2;
                           short short21 = (short)GameCanvas.readShort(abyte, i);
                           i += 2;
                           short short30 = (short)GameCanvas.readShort(abyte, i);
                           i += 2;
                           putShort(abyte5 = new byte[12], 2, (short)j);
                           putShort(abyte5, 4, short21);
                           putShort(abyte5, 6, short30);
                           putShort(abyte5, 8, (short)10000);
                           putShort(abyte5, 10, (short)10000);
                           break label95;
                        case 14:
                        case 15:
                           abyte6 = new byte[2];
                           break;
                        case 16:
                        case 17:
                           j = (short)GameCanvas.readShort(abyte, i);
                           i += 2;
                           short short20 = (short)(abyte[i++] & 0xFF);
                           putShort(abyte5 = new byte[6], 2, (short)j);
                           putShort(abyte5, 4, short20);
                           abyte5[5] = 0;
                           break label95;
                        case 18:
                           short short19 = (short)(abyte[i++] & 0xFF);
                           int k4 = (abyte[i++] & 255) + ((abyte[i++] & 255) << 8) + ((abyte[i++] & 255) << 16);
                           putShort(abyte5 = new byte[8], 2, short19);
                           putInt(abyte5, 3, k4);
                           abyte5[7] = 0;
                           break label95;
                        case 25:
                           short short11 = (short)GameCanvas.readShort(abyte, i);
                           int i2 = i + 2;
                           short short18 = (short)GameCanvas.readShort(abyte, i2);
                           i = i2 + 2;
                           short short29 = (short)(abyte[i++] & 0xFF);
                           short short5 = (short)(abyte[i++] & 0xFF);
                           putShort(abyte5 = new byte[8], 2, short11);
                           putShort(abyte5, 4, short18);
                           abyte5[6] = (byte)short29;
                           abyte5[7] = (byte)short5;
                           break label95;
                        case 26:
                           short short10 = (short)GameCanvas.readShort(abyte, i);
                           i += 2;
                           short short17 = (short)GameCanvas.readShort(abyte, i);
                           i += 2;
                           int j4 = GameCanvas.readInt(abyte, i);
                           i += 4;
                           putShort(abyte5 = new byte[10], 2, short10);
                           putShort(abyte5, 4, short17);
                           putInt(abyte5, 6, j4);
                           break label95;
                        case 27:
                           short short9 = (short)GameCanvas.readShort(abyte, i);
                           i += 2;
                           putShort(abyte5 = new byte[short9 + 8], 2, short9);
                           putShort(abyte5, 4, (short)10000);
                           abyte5[6] = 0;
                           abyte5[7] = 0;
                           System.arraycopy(abyte, i, abyte5, 8, short9);
                           i += short9;
                           break label95;
                     }

                     abyte5 = abyte6;
                  }

                  if (abyte5 != null) {
                     abyte5[0] = (byte)l3;
                     abyte5[1] = 0;
                     if (abyte4 == abyte3) {
                        abyte4[i3] = abyte5;
                        i3++;
                     } else {
                        abyte4[l2] = abyte5;
                        l2++;
                     }
                  }

                  if (abyte4 == abyte3 && j3 == 0) {
                     int i4 = 0;

                     for (int l4 = 0; l4 < abyte3.length; l4++) {
                        i4 += abyte3[l4].length;
                     }

                     (abyte5 = new byte[2 + (abyte3.length << 2) + i4])[0] = 0;
                     abyte5[1] = (byte)abyte3.length;
                     int i5 = 2 + (abyte3.length << 2);

                     for (int k2 = 0; k2 < abyte3.length; k2++) {
                        putInt(abyte5, 2 + (k2 << 2), abyte3[k2].length);
                        System.arraycopy(abyte3[k2], 0, abyte5, i5, abyte3[k2].length);
                        i5 += abyte3[k2].length;
                     }

                     abyte2[l2] = abyte5;
                     i3 = 0;
                     abyte3 = null;
                     l2++;
                  }
               }

               this.steps = abyte2;
            }
         } while (!flag);
      } catch (Exception exception) {
      }
   }

   public static int drawWrappedCentered(Graphics var0, String var1, int var2, int var3, int var4) {
      String[] astring = wrapText(var1, var4);

      for (int i = 0; i < astring.length; i++) {
         GameCanvas.fontSmall.drawString(var0, astring[i], var2, var3, 17);
         var3 += GameCanvas.fontSmall.font.getHeight();
      }

      return var3;
   }

   private static void drawWrapped(Graphics var0, String var1, int var2, int var3, int var4) {
      var3 += 14;

      try {
         String[] astring = wrapText(var1, var4);

         for (int i = 0; i < astring.length; i++) {
            if (astring[i] == null) {
               return;
            }

            if (astring[i].trim().equals("") || astring[i].trim().equals(" ")) {
               return;
            }

            FontRenderer fontrenderer;
            Graphics graphics;
            String s;
            int j;
            int k;
            byte b0;
            if (GameCanvas.rightToLeft) {
               fontrenderer = GameCanvas.fontSmall;
               graphics = var0;
               s = astring[i];
               j = var2 + var4;
               k = var3;
               b0 = 8;
            } else {
               fontrenderer = GameCanvas.fontSmall;
               graphics = var0;
               s = astring[i];
               j = var2;
               k = var3;
               b0 = 4;
            }

            fontrenderer.drawString(graphics, s, j, k, b0);
            var3 += GameCanvas.fontSmall.font.getHeight();
         }
      } catch (Exception exception) {
         exception.printStackTrace();
      }
   }

   public static String[] wrapText(String var0, int var1) {
      Vector vector = new Vector();
      String s = "";
      String[] astring = split(var0, ' ');

      for (int i = 0; i < astring.length; i++) {
         String s1 = astring[i];
         if (GameCanvas.fontSmall.font.stringWidth(s1) >= var1) {
            String[] astring1;
            if ((astring1 = breakWord(s + s1, var1)).length > 1
               && (s1 = astring1[astring1.length - 1].trim()).length() == 1
               && (s1.equals(".") || s1.equals(",") || s1.equals(";") || s1.equals("?") || s1.equals("'") || s1.equals("\"") || s1.equals("!"))) {
               String s2 = astring1[astring1.length - 2];
               s1 = s2.charAt(s2.length() - 1) + s1;
               astring1[astring1.length - 2] = s2.substring(0, s2.length() - 1);
               astring1[astring1.length - 1] = s1;
            }

            String[] astring2 = astring1;

            for (int j = 0; j < astring2.length - 1; j++) {
               if (astring2[j] != null && !astring2[j].equals("") && !astring2[j].equals(" ")) {
                  vector.addElement(astring2[j]);
               }
            }

            s = "" + astring2[astring2.length - 1] + " ";
            if (i != astring.length - 1 || s == null || s.equals("") || s.equals(" ")) {
               continue;
            }
         } else {
            StringBuffer stringbuffer;
            if (GameCanvas.fontSmall.font.stringWidth(s + s1 + " ") < var1) {
               String s3;
               if (astring.length > 2
                  && i == astring.length - 2
                  && (s3 = astring[astring.length - 1].trim()).length() == 1
                  && GameCanvas.fontSmall.font.stringWidth(s + " " + s1 + " " + s3) >= var1
                  && (s3.equals(".") || s3.equals(",") || s3.equals(";") || s3.equals("?") || s3.equals("'") || s3.equals("\"") || s3.equals("!"))) {
                  if (s != null && !s.equals("") && !s.equals(" ")) {
                     vector.addElement(s);
                  }

                  s = "";
                  if ((s = s + s1 + " " + s3) != null && !s.equals("") && !s.equals(" ")) {
                     vector.addElement(s);
                  }
                  break;
               }

               stringbuffer = new StringBuffer();
            } else {
               if (s != null && !s.equals("") && !s.equals(" ")) {
                  vector.addElement(s);
               }

               s = "";
               stringbuffer = new StringBuffer();
            }

            s = stringbuffer.append(s).append(s1).append(" ").toString();
            if (i != astring.length - 1 || s == null || s.equals("") || s.equals(" ")) {
               continue;
            }
         }

         vector.addElement(s);
      }

      return toArray(vector);
   }

   private static String[] breakWord(String var0, int var1) {
      Vector vector = new Vector();
      String s = "";

      for (int i = 0; i < var0.length(); i++) {
         char c0 = var0.charAt(i);
         StringBuffer stringbuffer;
         String s1;
         if (GameCanvas.fontSmall.font.stringWidth(s + c0) < var1) {
            stringbuffer = new StringBuffer();
            s1 = s;
         } else {
            vector.addElement(s);
            stringbuffer = new StringBuffer();
            s1 = "";
         }

         s = stringbuffer.append(s1).append(c0).toString();
         if (i == var0.length() - 1 && s != null && !s.equals("") && !s.equals(" ")) {
            vector.addElement(s);
         }
      }

      return toArray(vector);
   }

   private static String[] toArray(Vector var0) {
      String[] astring = new String[var0.size()];
      Enumeration enumeration = var0.elements();

      for (int i = 0; enumeration.hasMoreElements(); i++) {
         String s;
         if (!(s = ((String)enumeration.nextElement()).trim()).equals("") && !s.equals(" ")) {
            astring[i] = s;
         }
      }

      return astring;
   }

   public static String[] split(String var0, char var1) {
      Vector vector = new Vector();
      String s = "";

      for (int i = 0; i < var0.length(); i++) {
         char c0;
         if ((c0 = var0.charAt(i)) != var1) {
            s = s + c0;
         }

         if (c0 == var1 || c0 == '\n' || i == var0.length() - 1) {
            vector.addElement(s);
            s = "";
         }
      }

      return toArray(vector);
   }

   private void computeLineBreaks(String var1, int var2) {
      try {
         this.lineCount = 0;
         int i = var1.length();
         int j = 0;
         int k = 0;

         for (int l = 0; l < i; l++) {
            if (var1.charAt(i - 1) == '\n') {
               return;
            }

            label70: {
               char c0;
               if ((c0 = var1.charAt(l)) > ' ') {
                  j += GameCanvas.fontSmall.font.charWidth((char)c0);
               } else {
                  if (c0 != ' ' || l + 1 >= i || var1.charAt(l + 1) != '?' && var1.charAt(l + 1) != '!' && var1.charAt(l + 1) != ':') {
                     if (c0 != ' ') {
                        if (c0 == '\n') {
                           j = 0;
                           this.lineBreaks[this.lineCount++] = (short)l;
                        }
                        continue;
                     }

                     j += GameCanvas.fontSmall.font.charWidth((char)c0);
                     break label70;
                  }

                  j += GameCanvas.fontSmall.font.charWidth((char)c0);
                  var1.charAt(l + 1);
                  j += GameCanvas.fontSmall.font.charWidth((char)c0);
               }

               if (j > var2) {
                  l = k - 1;
                  this.lineBreaks[this.lineCount++] = (short)l;
                  j = 0;
               }

               if (var1.charAt(l) != ' ') {
                  continue;
               }
            }

            k = l + 1;
         }

         this.lineBreaks[this.lineCount++] = (short)i;
      } catch (Exception exception) {
      }
   }

   private static void putShort(byte[] var0, int var1, short var2) {
      var0[var1] = (byte)var2;
      var0[var1 + 1] = (byte)(var2 >> 8);
   }

   private static void putInt(byte[] var0, int var1, int var2) {
      var0[var1] = (byte)var2;
      var0[var1 + 1] = (byte)(var2 >> 8);
      var0[var1 + 2] = (byte)(var2 >> 16);
      var0[var1 + 3] = (byte)(var2 >>> 24);
   }
}
