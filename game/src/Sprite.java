import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class Sprite {
   private static int[] pixelBuffer = new int[4096];
   private byte[] moduleSizes;
   private byte[] frameModuleCounts;
   private short[] frameModuleStarts;
   public byte[] frameRects;
   private byte[] frameModules;
   public byte[] animFrameCounts;
   public short[] animFrameStarts;
   public byte[] animFrames;
   private int[][] palettes;
   private int paletteCount;
   public int currentPalette;
   private boolean hasTransparency;
   private short pixelFormat;
   public byte[] moduleData;
   private short[] moduleDataOffsets;
   public Image[][] moduleImages;
   public static byte[] mcTableUnused;

   public final void load(byte[] var1, int var2) {
      try {
         System.gc();
         var2++;
         var2++;
         var2++;
         var2++;
         var2++;
         var2++;
         var2++;
         int i5 = var1[6] & 255;
         var2++;
         short short1;
         if ((short1 = (short)(i5 + ((var1[7] & 255) << 8))) > 0) {
            this.moduleSizes = new byte[short1 << 1];
            System.arraycopy(var1, 8, this.moduleSizes, 0, this.moduleSizes.length);
            var2 = 8 + this.moduleSizes.length;
         }

         short short2;
         if ((short2 = (short)((var1[var2++] & 255) + ((var1[var2++] & 255) << 8))) > 0) {
            this.frameModules = new byte[short2 << 2];
            System.arraycopy(var1, var2, this.frameModules, 0, this.frameModules.length);
            var2 += this.frameModules.length;
         }

         if ((short2 = (short)((var1[var2++] & 255) + ((var1[var2++] & 255) << 8))) > 0) {
            this.frameModuleCounts = new byte[short2];
            this.frameModuleStarts = new short[short2];

            for (int i = 0; i < short2; i++) {
               this.frameModuleCounts[i] = var1[var2++];
               int k4 = ++var2;
               var2++;
               this.frameModuleStarts[i] = (short)((var1[k4] & 255) + ((var1[var2++] & 255) << 8));
            }

            int k1 = short2 << 2;
            this.frameRects = new byte[k1];

            for (int j1 = 0; j1 < k1; j1++) {
               this.frameRects[j1] = var1[var2++];
            }
         }

         short short3;
         if ((short3 = (short)((var1[var2++] & 255) + ((var1[var2++] & 255) << 8))) > 0) {
            this.animFrames = new byte[short3 * 5];
            System.arraycopy(var1, var2, this.animFrames, 0, this.animFrames.length);
            var2 += this.animFrames.length;
         }

         if ((short2 = (short)((var1[var2++] & 255) + ((var1[var2++] & 255) << 8))) > 0) {
            this.animFrameCounts = new byte[short2];
            this.animFrameStarts = new short[short2];

            for (int l1 = 0; l1 < short2; l1++) {
               this.animFrameCounts[l1] = var1[var2++];
               int l4 = ++var2;
               var2++;
               this.animFrameStarts[l1] = (short)((var1[l4] & 255) + ((var1[var2++] & 255) << 8));
            }
         }

         if (short1 <= 0) {
            System.gc();
         } else {
            short3 = (short)((var1[var2++] & 255) + ((var1[var2++] & 255) << 8));
            this.paletteCount = var1[var2++] & 255;
            short2 = (short)(var1[var2++] & 255);
            this.palettes = new int[16][];

            for (int j = 0; j < this.paletteCount; j++) {
               this.palettes[j] = new int[short2];
               switch (short3) {
                  case -30584:
                     for (int k2 = 0; k2 < short2; k2++) {
                        int k3;
                        if ((
                              (k3 = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8) + ((var1[var2++] & 255) << 16) + ((var1[var2++] & 255) << 24))
                                 & 0xFF000000
                           )
                           != -16777216) {
                           this.hasTransparency = true;
                        }

                        this.palettes[j][k2] = k3;
                     }
                     break;
                  case 17476:
                     for (int j2 = 0; j2 < short2; j2++) {
                        int j3;
                        if (((j3 = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8)) & 61440) != 61440) {
                           this.hasTransparency = true;
                        }

                        this.palettes[j][j2] = (j3 & 61440) << 16
                           | (j3 & 61440) << 12
                           | (j3 & 3840) << 12
                           | (j3 & 3840) << 8
                           | (j3 & 240) << 8
                           | (j3 & 240) << 4
                           | (j3 & 15) << 4
                           | j3 & 15;
                     }
                     break;
                  case 21781:
                     for (int i2 = 0; i2 < short2; i2++) {
                        int i3 = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8);
                        int j4 = -16777216;
                        if ((i3 & 32768) != 32768) {
                           j4 = 0;
                           this.hasTransparency = true;
                        }

                        this.palettes[j][i2] = j4 | (i3 & 31744) << 9 | (i3 & 992) << 6 | (i3 & 31) << 3;
                     }
                     break;
                  case 25861:
                     for (int k = 0; k < short2; k++) {
                        int l = (var1[var2++] & 255) + ((var1[var2++] & 255) << 8);
                        int i1 = -16777216;
                        if (l == 63519) {
                           i1 = 0;
                           this.hasTransparency = true;
                        }

                        this.palettes[j][k] = i1 | (l & 63488) << 8 | (l & 2016) << 5 | (l & 31) << 3;
                     }
               }
            }

            this.pixelFormat = (short)((var1[var2++] & 255) + ((var1[var2++] & 255) << 8));
            if (short1 > 0) {
               this.moduleDataOffsets = new short[short1];
               short short4 = 0;
               int l2 = var2;

               for (int l3 = 0; l3 < short1; l3++) {
                  short short5 = (short)((var1[l2++] & 255) + ((var1[l2++] & 255) << 8));
                  this.moduleDataOffsets[l3] = (short)short4;
                  l2 += short5;
                  short4 += short5;
               }

               this.moduleData = new byte[short4];

               for (int i4 = 0; i4 < short1; i4++) {
                  short short6 = (short)((var1[var2++] & 255) + ((var1[var2++] & 255) << 8));
                  System.arraycopy(var1, var2, this.moduleData, this.moduleDataOffsets[i4] & 65535, short6);
                  var2 += short6;
               }
            }

            System.gc();
         }
      } catch (Exception exception) {
         exception.printStackTrace();
      }
   }

   public final void buildModuleImages(int var1, int var2, int var3, int var4) {
      if (this.moduleSizes != null) {
         if (var3 == -1) {
            var3 = (this.moduleSizes.length >> 1) - 1;
         }

         if (this.moduleImages == null) {
            this.moduleImages = new Image[this.paletteCount][];
         }

         if (this.moduleImages[var1] == null) {
            this.moduleImages[var1] = new Image[this.moduleSizes.length >> 1];
         }

         var2 = this.currentPalette;
         this.currentPalette = var1;
         System.gc();

         for (int i1 = 0; i1 <= var3; i1++) {
            int i = i1 << 1;
            int j = this.moduleSizes[i] & 255;
            i = this.moduleSizes[i + 1] & 255;
            int[] aint;
            if (j > 0 && i > 0 && (aint = this.decodeModule(i1)) != null) {
               boolean flag = false;
               int k = j * i;

               for (int l = 0; l < k; l++) {
                  if ((aint[l] & 0xFF000000) != -16777216) {
                     flag = true;
                     break;
                  }
               }

               this.moduleImages[var1][i1] = Image.createRGBImage(aint, j, i, flag);
            }
         }

         System.gc();
         this.currentPalette = var2;
         System.gc();
      }
   }

   public final void freeFirstPaletteImages(int var1) {
      if (this.moduleSizes != null) {
         if (this.moduleImages != null) {
            if (this.moduleImages[0] != null) {
               for (int i = 0; i < this.moduleImages[0].length; i++) {
                  this.moduleImages[0][i] = null;
               }

               this.moduleImages[0] = null;
               this.paletteCount--;
            }
         }
      }
   }

   public final String toString() {
      new String();
      int i = 0;

      for (int j = 0; j < this.moduleSizes.length / 2; j++) {
         i += 2 * (this.moduleSizes[j * 2] & 255) * (this.moduleSizes[j * 2 + 1] & 255);
      }

      return "raw/full: " + this.moduleData.length + "/" + i;
   }

   public final int getAnimFrameDuration(int var1, int var2) {
      return this.animFrames[(this.animFrameStarts[var1] + var2) * 5 + 1] & 0xFF;
   }

   public final int getAnimFrameCount(int var1) {
      return this.animFrameCounts[var1] & 0xFF;
   }

   public final void paintAnimFrame(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      var2 = (this.animFrameStarts[var2] + var3) * 5;
      var3 = this.animFrames[var2] & 255;
      if ((var6 & 32) != 0) {
         var7 = (var6 & 1) != 0 ? 0 + this.animFrames[var2 + 2] : 0 - this.animFrames[var2 + 2];
         var8 = (var6 & 2) != 0 ? 0 + this.animFrames[var2 + 3] : 0 - this.animFrames[var2 + 3];
      }

      this.paintFrame(var1, var3, var4 - var7, var5 - var8, var6 ^ this.animFrames[var2 + 4] & 15, var7, var8);
   }

   public final void paintFrame(Graphics var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      int i = this.frameModuleCounts[var2] & 255;

      for (int j = 0; j < i; j++) {
         int i2 = var7;
         int l1 = var6;
         int k1 = var5;
         int j1 = var4;
         int i1 = var3;
         int l = j;
         int k = var2;
         Graphics graphics = var1;
         Sprite sprite = this;
         k = this.frameModuleStarts[k] + l << 2;
         l = sprite.frameModules[k + 3] & 255;
         int j2 = sprite.frameModules[k] & 255;
         i1 = (k1 & 1) != 0 ? i1 - sprite.frameModules[k + 1] : i1 + sprite.frameModules[k + 1];
         j1 = (k1 & 2) != 0 ? j1 - sprite.frameModules[k + 2] : j1 + sprite.frameModules[k + 2];
         if ((l & 16) != 0) {
            sprite.paintFrame(graphics, j2, i1, j1, k1 ^ l & 15, l1, i2);
         } else {
            if ((k1 & 1) != 0) {
               i1 -= sprite.moduleSizes[j2 << 1] & 255;
            }

            if ((k1 & 2) != 0) {
               j1 -= sprite.moduleSizes[(j2 << 1) + 1] & 255;
            }

            sprite.paintModule(graphics, j2, i1, j1, k1 ^ l & 15);
         }
      }
   }

   public final void paintModule(Graphics var1, int var2, int var3, int var4, int var5) {
      int i = var2 << 1;
      int j = this.moduleSizes[i] & 255;
      i = this.moduleSizes[i + 1] & 255;
      if (j > 0 && i > 0) {
         Image image = null;
         if (this.moduleImages != null && this.moduleImages[this.currentPalette] != null) {
            image = this.moduleImages[this.currentPalette][var2];
         }

         if (image == null) {
            int[] aint;
            if ((aint = this.decodeModule(var2)) == null) {
               return;
            }

            image = Image.createRGBImage(aint, j, i, this.hasTransparency);
         }

         j = image.getWidth();
         i = image.getHeight();
         Graphics graphics;
         Image image1;
         byte b0;
         byte b1;
         int k;
         int l;
         byte b2;
         if ((var5 & 1) != 0) {
            if ((var5 & 2) != 0) {
               graphics = var1;
               image1 = image;
               b0 = 0;
               b1 = 0;
               k = j;
               l = i;
               b2 = 3;
            } else {
               graphics = var1;
               image1 = image;
               b0 = 0;
               b1 = 0;
               k = j;
               l = i;
               b2 = 2;
            }
         } else if ((var5 & 2) != 0) {
            graphics = var1;
            image1 = image;
            b0 = 0;
            b1 = 0;
            k = j;
            l = i;
            b2 = 1;
         } else {
            graphics = var1;
            image1 = image;
            b0 = 0;
            b1 = 0;
            k = j;
            l = i;
            b2 = 0;
         }

         graphics.drawRegion(image1, b0, b1, k, l, b2, var3, var4, 0);
      }
   }

   private int[] decodeModule(int var1) {
      if (this.moduleData != null && this.moduleDataOffsets != null) {
         int i = var1 << 1;
         int j = this.moduleSizes[i] & 255;
         i = this.moduleSizes[i + 1] & 255;
         int[] aint = pixelBuffer;
         int[] aint1;
         if ((aint1 = this.palettes[this.currentPalette]) == null) {
            return null;
         }

         byte[] abyte = this.moduleData;
         var1 = this.moduleDataOffsets[var1] & '\uffff';
         int k = 0;
         i = j * i;
         if (this.pixelFormat != 10225) {
            if (this.pixelFormat == 5632) {
               while (k < i) {
                  aint[k++] = aint1[abyte[var1] >> 4 & 15];
                  aint[k++] = aint1[abyte[var1] & 15];
                  var1++;
               }
            } else if (this.pixelFormat == 1024) {
               while (k < i) {
                  aint[k++] = aint1[abyte[var1] >> 6 & 3];
                  aint[k++] = aint1[abyte[var1] >> 4 & 3];
                  aint[k++] = aint1[abyte[var1] >> 2 & 3];
                  aint[k++] = aint1[abyte[var1] & 3];
                  var1++;
               }
            } else if (this.pixelFormat == 512) {
               while (k < i) {
                  aint[k++] = aint1[abyte[var1] >> 7 & 1];
                  aint[k++] = aint1[abyte[var1] >> 6 & 1];
                  aint[k++] = aint1[abyte[var1] >> 5 & 1];
                  aint[k++] = aint1[abyte[var1] >> 4 & 1];
                  aint[k++] = aint1[abyte[var1] >> 3 & 1];
                  aint[k++] = aint1[abyte[var1] >> 2 & 1];
                  aint[k++] = aint1[abyte[var1] >> 1 & 1];
                  aint[k++] = aint1[abyte[var1] & 1];
                  var1++;
               }
            } else if (this.pixelFormat == 22018) {
               while (k < i) {
                  aint[k++] = aint1[abyte[var1++] & 0xFF];
               }
            } else if (this.pixelFormat == 22258) {
               while (k < i) {
                  int j1;
                  if ((j1 = abyte[var1++] & 255) > 127) {
                     int k1 = j1 - 128;

                     while (k1-- > 0) {
                        aint[k++] = aint1[abyte[var1++] & 0xFF];
                     }
                  } else {
                     j = aint1[abyte[var1++] & 0xFF];

                     while (j1-- > 0) {
                        aint[k++] = j;
                     }
                  }
               }
            }
         } else {
            while (k < i) {
               int l;
               if ((l = abyte[var1++] & 255) > 127) {
                  j = abyte[var1++] & 255;
                  j = aint1[j];
                  int i1 = l - 128;

                  while (i1-- > 0) {
                     aint[k++] = j;
                  }
               } else {
                  aint[k++] = aint1[l];
               }
            }
         }

         return aint;
      } else {
         return null;
      }
   }

   public final void unload(boolean var1) {
      this.moduleSizes = null;
      this.frameModuleCounts = null;
      this.frameModuleStarts = null;
      this.frameRects = null;
      this.frameModules = null;
      this.animFrameCounts = null;
      this.animFrameStarts = null;
      this.animFrames = null;
      if (this.palettes != null) {
         for (int i = 0; i < this.palettes.length; i++) {
            this.palettes[i] = null;
         }

         this.palettes = null;
      }

      this.moduleData = null;
      this.moduleDataOffsets = null;
      if (var1 && this.moduleImages != null) {
         for (int k = 0; k < this.moduleImages.length; k++) {
            if (this.moduleImages[k] != null) {
               for (int j = 0; j < this.moduleImages[k].length; j++) {
                  this.moduleImages[k][j] = null;
               }
            }
         }

         this.moduleImages = null;
      }
   }
}
