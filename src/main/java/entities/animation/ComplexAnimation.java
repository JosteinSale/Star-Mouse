package entities.animation;

import static entities.animation.Animation.Type.LOOP_BACKWARDS;
import static entities.animation.Animation.Type.ONCE_BACKWARDS;

import java.util.HashMap;

import rendering.misc.SpriteInfo;

/**
 * The most flexible and complex class of animations.
 * For each action/row, it allows for:
 * - Customizing the animation types (looping, reverse, etc).
 * - Giving the action a name,
 * - Reusing rows (e.g. "walk" and "run" can use the same row, but have
 * different speeds).
 * It also has a position, and scale defaults to 1.
 */
public class ComplexAnimation extends PositionedAnimation {
   public HashMap<String, AnimationInfo> aniInfos;
   protected String currentAction;
   public SpriteInfo spriteInfo;

   /** Initializes the animation to the given initialAction */
   public ComplexAnimation(
         String spriteName, HashMap<String, AnimationInfo> aniInfo, String initialAction,
         int spriteW, int spriteH, int rows, int cols, float xPos, float yPos) {
      super(
            aniInfo.get(initialAction).aniRow, // initial row
            getStartCol(aniInfo.get(initialAction)), // initial col
            aniInfo.get(initialAction).speed, // initial speed
            xPos, yPos, 1, 1);

      this.aniInfos = aniInfo;
      this.currentAction = initialAction;
      this.spriteInfo = new SpriteInfo(spriteName, spriteW, spriteH, rows, cols);
   }

   /** If a new action, it aborts the current animation and starts the new one */
   public void setAnimation(String newAction) {
      if ((!newAction.equals(currentAction))) {
         currentAction = newAction;
         resetTick();
         AnimationInfo info = aniInfos.get(newAction);
         setAniTickPerFrame(info.speed);
         setCol(getStartCol(info));
         setRow(info.aniRow);
      }
   }

   private static int getStartCol(AnimationInfo info) {
      return (info.animationType == LOOP_BACKWARDS || info.animationType == ONCE_BACKWARDS)
            ? info.nrOfFrames - 1
            : 0;
   }

   public void updateAnimations() {
      AnimationInfo info = aniInfos.get(currentAction);
      this.play(info.animationType, info.nrOfFrames - 1);
   }
}
