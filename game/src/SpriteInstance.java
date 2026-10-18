import javax.microedition.lcdui.Graphics;

public final class SpriteInstance {
   public int x;
   public int y;
   private int stepX;
   private int stepY;
   public int flags;
   public Sprite sprite;
   public int anim;
   public int animFrame;
   public int frameTimer;
   private SpriteInstance parent;

   public SpriteInstance() {
   }

   public SpriteInstance(Sprite var1, int var2, int var3, SpriteInstance var4) {
      this.x = 0;
      this.y = 0;
      this.sprite = var1;
      this.parent = null;
   }

   public final void setAnim(int var1) {
      if (var1 != this.anim) {
         this.anim = var1;
         this.animFrame = 0;
         this.frameTimer = 0;
         this.stepX = 0;
         this.stepY = 0;
      }
   }

   public final void applyFrameStep() {
      int i = (this.sprite.animFrameStarts[this.anim] + this.animFrame) * 5;
      this.stepX = this.sprite.animFrames[i + 2];
      if ((this.flags & 1) != 0) {
         this.stepX = -this.stepX;
      }

      this.stepY = this.sprite.animFrames[i + 3];
      if ((this.flags & 2) != 0) {
         this.stepY = -this.stepY;
      }

      this.x = this.x + this.stepX;
      this.y = this.y + this.stepY;
   }

   public final boolean isAnimFinished() {
      int i;
      return this.animFrame != this.sprite.getAnimFrameCount(this.anim) - 1
         ? false
         : (i = this.sprite.getAnimFrameDuration(this.anim, this.animFrame)) == 0 || this.frameTimer == i - 1;
   }

   public final void paint(Graphics var1) {
      if (this.sprite != null) {
         int i = this.x;
         int j = this.y;
         SpriteInstance spriteinstance1 = this;

         SpriteInstance spriteinstance;
         while ((spriteinstance = spriteinstance1.parent) != null) {
            i += spriteinstance.x;
            j += spriteinstance.y;
            spriteinstance1 = spriteinstance;
         }

         i = i;
         j = j;
         if (this.frameTimer >= 0) {
            this.sprite.paintAnimFrame(var1, this.anim, this.animFrame, i, j, this.flags, 0, 0);
         } else if (this.anim >= 0) {
            this.sprite.paintModule(var1, this.anim, i, j, this.flags);
         } else {
            if (this.animFrame >= 0) {
               this.sprite.paintFrame(var1, this.animFrame, i, j, this.flags, 0, 0);
            }
         }
      }
   }

   public final void updateAnim() {
      if (this.sprite != null) {
         if (this.frameTimer >= 0) {
            int i;
            if ((i = this.sprite.getAnimFrameDuration(this.anim, this.animFrame)) != 0) {
               this.frameTimer++;
               if (i <= this.frameTimer) {
                  this.frameTimer = 0;
                  this.animFrame++;
                  if (this.animFrame >= this.sprite.getAnimFrameCount(this.anim)) {
                     this.animFrame = 0;
                     this.stepX = 0;
                     this.stepY = 0;
                  }
               }
            }
         }
      }
   }
}
