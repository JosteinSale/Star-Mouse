package entities.flying.enemies;

import java.awt.geom.Rectangle2D;
import java.util.Collections;

import entities.AnimationFrame;
import entities.flying.EntityInfo;
import main_classes.Game;

public class Centipede extends BaseEnemy {
   private final int nrOfMiddleSegments = 8;
   private final int distanceBetweenSegments = 60;
   private int attackPhaseTick = 0;

   public Centipede(Rectangle2D.Float hitbox, EntityInfo info, int startTimer) {
      super(hitbox, info, startTimer, null);
      constructHitboxes();
      constructAnimations();
   }

   private void constructHitboxes() {
      allHitboxes.clear();
      // 1. Head
      allHitboxes.add(hitbox);
      // 2. Middle segment
      for (int i = 1; i <= nrOfMiddleSegments; i++) {
         Rectangle2D.Float middleHitbox = new Rectangle2D.Float(
               hitbox.x,
               hitbox.y - (i * distanceBetweenSegments), // TODO - Make getX and getY method based on vector
               hitbox.width,
               hitbox.height);
         allHitboxes.add(middleHitbox);
      }
      // 3. Tail
      Rectangle2D.Float tailHitbox = new Rectangle2D.Float(
            hitbox.x,
            hitbox.y - ((nrOfMiddleSegments + 1) * distanceBetweenSegments), // TODO - Use getX and getY method
            hitbox.width,
            hitbox.height);
      allHitboxes.add(tailHitbox);
      Collections.reverse(allHitboxes);
   }

   private void constructAnimations() {
      allAnimations.clear();
      // 1. Head
      allAnimations.add(animation);
      // 2. Mmiddle segment
      for (int i = 0; i < nrOfMiddleSegments; i++) {
         AnimationFrame middleAnimation = new AnimationFrame(1, (nrOfMiddleSegments - i) % 8);
         allAnimations.add(middleAnimation);
      }
      // 3. Tail
      AnimationFrame tailAnimation = new AnimationFrame(2, 0);
      allAnimations.add(tailAnimation);
      Collections.reverse(allAnimations);
   }

   @Override
   protected void checkOnScreen(float levelYSpeed) {
      if (isDead()) {
         return;
      }
      for (Rectangle2D.Float hb : allHitboxes) {
         if (((hb.y + hb.height * 1.2) > 0) &&
               (hb.y - hb.height * 0.2 < Game.GAME_DEFAULT_HEIGHT)) {
            onScreen = true;
            return;
         }
      }
      onScreen = false;
   }

   @Override
   public boolean isOnScreen() {
      return onScreen && attackPhaseActive();
   }

   private boolean attackPhaseActive() {
      return attackPhaseTick > 0;
   }

   @Override
   protected void updateAniTick() {
      aniTick++;
      if (aniTick >= aniTickPerFrame) {
         aniTick = 0;
         for (AnimationFrame af : allAnimations) {
            af.nextFrame();
            if (af.getFrame() >= amountOfFrames()) {
               af.setFrame(0);
            }
         }
         // TODO - fix damage animation
      }

   }

   @Override
   protected void updateCustomBehavior(float levelYSpeed) {
      if (chargeTick >= chargeDone) {
         attackPhaseTick++;
         // TODO - Move hitboxes based on movement vector
      }
   }

   @Override
   protected void resetCustomVars() {
      attackPhaseTick = 0;
      constructHitboxes();
   }

   @Override
   protected int amountOfFrames() {
      return 8;
   }

   @Override
   public double getRotation() {
      // TODO - return rotation based on movement vector
      return 3.14;
   }

   @Override
   public boolean isSmall() {
      // TODO - check if teleport killing works. If not -> big enemy
      return true;
   }

}
