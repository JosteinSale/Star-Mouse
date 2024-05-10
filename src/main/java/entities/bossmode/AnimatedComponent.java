package entities.bossmode;
import static utils.HelpMethods2.GetAnimationArray;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main_classes.Game;

/** A default class for an animated component. Can be extended.
 * It represents a single animated component which cannot be interacted with.
 * It takes a list of AnimationInfo-objects, and a single spritesheet.
 * A default updateAnimation()- and draw()-method is implemented, 
 * and should be accessed by the super-keyword if extended.
 * 
 * Change the animation action as needed.
 * Change the xPos and yPos as needed.
 * 
 * NOTE: the animation row (in the spritesheet) for an animation is INDEPENDENT 
 * from the animation action (index in aniInfo-list).
 */
public class AnimatedComponent {
   protected BufferedImage[][] animations;
   private ArrayList<AnimationInfo> aniInfo;
   private int aniIndex;
   private int aniTick;
   protected int aniAction;
   protected float xPos;
   protected float yPos;
   protected int spriteW;
   protected int spriteH;

   /** Use this constructor if you don't have a preexisting animation array */
   public AnimatedComponent(
      BufferedImage spriteSheet, int spriteW, int spriteH, int rows, int cols,
      ArrayList<AnimationInfo> aniInfo, float xPos, float yPos) {
         this.animations = GetAnimationArray(spriteSheet, rows, cols, spriteW, spriteH);
         this.aniInfo = aniInfo;
         this.xPos = xPos;
         this.yPos = yPos;
         this.spriteH = spriteH;
         this.spriteW = spriteW;
      }
   
   /** Sets the animation according to the index in the animationInfo-list */
   public void setAnimation(int newAction) {
      // If we're setting a new animation
      if ((newAction != this.aniAction)) {
         // Reset aniTick
         this.aniTick = 0;
         // If it should reverse
         if (aniInfo.get(newAction).reverse) {
            // Set aniIndex to last in animation.  
            this.aniIndex =  aniInfo.get(newAction).nrOfFrames - 1;
         }
         else {
            // Set aniIndex to 0
            this.aniIndex = 0;
         }
      }
      this.aniAction = newAction;
   }
   
   public void updateAnimations() {
      AnimationInfo animation = aniInfo.get(aniAction);
      if (animation.reverse) {
         this.reverseAnimatiom(animation);
      }
      else {
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

   public void draw(Graphics g) {
      int row = aniInfo.get(aniAction).aniRow;
      g.drawImage(
         this.animations[row][aniIndex], 
         (int) (xPos * Game.SCALE), 
         (int) (yPos* Game.SCALE) , 
         (int) (spriteW * 3 * Game.SCALE),
         (int) (spriteH * 3 * Game.SCALE), null);
      }
}
