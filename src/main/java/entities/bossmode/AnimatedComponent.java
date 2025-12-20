package entities.bossmode;

import java.util.HashMap;

/**
 * A default class for an animated component. Can be extended.
 * It represents a single animated component which cannot be interacted with.
 * It takes a collection of AnimationInfo-objects, and a single spritesheet.
 * 
 * Its typical use case is for entities with complex animations and behavior.
 * 
 * Change the animation action as needed.
 * Change the xPos and yPos as needed.
 * 
 */
public class AnimatedComponent {
   public HashMap<String, AnimationInfo> aniInfos;
   public int aniIndex;
   private int aniTick;
   public String aniAction;
   public float xPos;
   public float yPos;

   // Sprite info
   public String spriteName;
   public int spriteW;
   public int spriteH;
   public int rows;
   public int cols;

   public AnimatedComponent(
         String spriteName, HashMap<String, AnimationInfo> aniInfo, String initialAction,
         int spriteW, int spriteH, int rows, int cols, float xPos, float yPos) {
      this.spriteName = spriteName;
      this.aniAction = initialAction;
      this.spriteW = spriteW;
      this.spriteH = spriteH;
      this.rows = rows;
      this.cols = cols;
      this.aniInfos = aniInfo;
      this.xPos = xPos;
      this.yPos = yPos;
      this.spriteH = spriteH;
      this.spriteW = spriteW;
   }

   /** Sets the animation according to the index in the animationInfo-list */
   public void setAnimation(String newAction) {
      // If we're setting a new animation
      if ((!newAction.equals(this.aniAction))) {
         // Reset aniTick
         this.aniTick = 0;
         // If it should reverse
         if (aniInfos.get(newAction).reverse) {
            // Set aniIndex to last in animation.
            this.aniIndex = aniInfos.get(newAction).nrOfFrames - 1;
         } else {
            // Set aniIndex to 0
            this.aniIndex = 0;
         }
      }
      this.aniAction = newAction;
   }

   public void updateAnimations() {
      AnimationInfo animation = aniInfos.get(aniAction);
      if (animation.reverse) {
         this.reverseAnimatiom(animation);
      } else {
         this.regularAnimation(animation);
      }
   }

   private void regularAnimation(AnimationInfo animation) {
      this.aniTick++;
      if (this.aniTick > animation.speed) {
         this.aniTick = 0;
         this.aniIndex++;
         if (this.aniIndex > (animation.nrOfFrames - 1)) {
            this.aniIndex = animation.loopBackToIndex;
         }
      }
   }

   private void reverseAnimatiom(AnimationInfo animation) {
      this.aniTick++;
      if (this.aniTick > animation.speed) {
         this.aniTick = 0;
         this.aniIndex--;
         if (this.aniIndex < 0) {
            this.aniIndex = animation.loopBackToIndex;
         }
      }
   }

   public int getCurrentAniRow() {
      return aniInfos.get(aniAction).aniRow;
   }
}
