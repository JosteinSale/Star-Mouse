package entities.flying.enemies;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.math.Vector2;

import entities.AnimationFrame;
import entities.flying.EntityInfo;
import main_classes.Game;

public class Centipede extends BaseEnemy {
   private final int nrOfMiddleSegments = 8;
   private final int distanceBetweenSegments = 60;
   private final Vector2 speedVector;
   private final Vector2 normalizedVector;
   private final double angle;
   private final Rectangle2D.Float onScreenArea;
   private boolean attackPhaseActive = false;

   // Wiggle movement
   private final int wiggleSpeed = 6;
   private final int wiggleDistance = 30;
   private double wiggleRotation;
   private ArrayList<Point.Float> hitboxCenters;

   public Centipede(Rectangle2D.Float hitbox, EntityInfo info, int startTimer, Vector2 directionVector) {
      super(hitbox, info, startTimer, null);
      this.maxHP = 150;
      HP = maxHP;
      this.TAKING_DAMAGE = 3;
      this.speedVector = directionVector;
      this.normalizedVector = normalizeVector(directionVector);
      this.angle = calculateAngle(normalizedVector);
      this.onScreenArea = new Rectangle2D.Float(
            -200, -100, Game.GAME_DEFAULT_WIDTH + 400, Game.GAME_DEFAULT_HEIGHT + 200);
      constructHitboxes();
      constructAnimations();
      constructWiggleStuff();
   }

   private Vector2 normalizeVector(Vector2 vector) {
      Vector2 v = vector.cpy();
      if (v.len() != 0) {
         v.nor();
      }
      return v;
   }

   private double calculateAngle(Vector2 vector) {
      // adjust by -90 degrees so sprite's native facing aligns with movement
      return Math.atan2(vector.y, vector.x) + Math.PI / 2.0;
   }

   private void constructHitboxes() {
      allHitboxes.clear();
      // 1. Head
      allHitboxes.add(hitbox);
      // 2. Middle segment
      for (int i = 1; i <= nrOfMiddleSegments; i++) {
         Rectangle2D.Float middleHitbox = new Rectangle2D.Float(
               hitbox.x + getAdjustedXOffset(i * distanceBetweenSegments),
               hitbox.y + getAdjustedYOffset(i * distanceBetweenSegments),
               hitbox.width,
               hitbox.height);
         allHitboxes.add(middleHitbox);
      }
      // 3. Tail
      Rectangle2D.Float tailHitbox = new Rectangle2D.Float(
            hitbox.x + getAdjustedXOffset((nrOfMiddleSegments + 1) * distanceBetweenSegments),
            hitbox.y + getAdjustedYOffset((nrOfMiddleSegments + 1) * distanceBetweenSegments),
            hitbox.width,
            hitbox.height);
      allHitboxes.add(tailHitbox);

      // 4. Reverse, since the head will be drawn last (on top)
      Collections.reverse(allHitboxes);
   }

   private float getAdjustedYOffset(int deltaY) {
      // place segments behind the head along the negative movement direction
      return (float) (-deltaY * normalizedVector.y);
   }

   private float getAdjustedXOffset(int deltaX) {
      // place segments behind the head along the negative movement direction
      return (float) (-deltaX * normalizedVector.x);
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

      // 4. Reverse so that head is drawn last (= on top)
      Collections.reverse(allAnimations);
   }

   private void constructWiggleStuff() {
      hitboxCenters = new ArrayList<>();
      allHitboxes.forEach(hb -> hitboxCenters.add(
            new Point.Float(hb.x + hb.width / 2, hb.y + hb.height / 2)));
   }

   @Override
   protected void moveEnemyDown(float levelYSpeed) {
      allHitboxes.forEach(hb -> {
         hb.y += levelYSpeed;
      });
      hitboxCenters.forEach(p -> {
         p.y += levelYSpeed;
      });
   }

   @Override
   protected void checkOnScreen(float levelYSpeed) {
      if (isDead()) {
         return;
      }
      for (Point.Float p : hitboxCenters) {
         if (onScreenArea.contains(p)) {
            onScreen = true;
            return;
         }
      }
      onScreen = false;
   }

   @Override
   public boolean isOnScreen() {
      return onScreen && attackPhaseActive;
   }

   @Override
   protected void updateCustomBehavior(float levelYSpeed) {
      if (chargeTick >= chargeDone) {
         attackPhaseActive = true;
         updateAttackPhase();
      }
   }

   private void updateAttackPhase() {
      // 1. Move in the direction of the speed vector
      allHitboxes.forEach(hb -> {
         hb.x += speedVector.x;
         hb.y += speedVector.y;
      });
      hitboxCenters.forEach(p -> {
         p.x += speedVector.x;
         p.y += speedVector.y;
      });

      // 2. Wiggle movement
      wiggleRotation = (wiggleRotation + wiggleSpeed) % 360;
      for (int i = 0; i < allHitboxes.size(); i++) {
         int wigglePhase = i * 20;
         double wiggleR = (wiggleRotation + wigglePhase) % 360;
         double rad = Math.toRadians(wiggleR);

         // Oscillate perpendicular to movement direction
         float scalar = (float) Math.cos(rad) * wiggleDistance;
         float perpX = -normalizedVector.y;
         float perpY = normalizedVector.x;
         float xOffset = scalar * perpX;
         float yOffset = scalar * perpY;

         Point.Float center = hitboxCenters.get(i);
         Rectangle2D.Float hb = allHitboxes.get(i);
         hb.x = center.x - hb.width / 2 + xOffset;
         hb.y = center.y - hb.height / 2 + yOffset;
      }
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
      }
      if (damageTick > 0) {
         damageTick--;
         if (damageTick <= 0) {
            setAllAnimationsTo(IDLE);
         }
      }
   }

   private void setAllAnimationsTo(int action) {
      for (int i = 0; i < allAnimations.size(); i++) {
         switch (i) {
            case 0 -> allAnimations.reversed().get(i).setAction(action); // Head
            case (1 + nrOfMiddleSegments) -> allAnimations.reversed().get(i).setAction(action + 2); // Tail
            default -> allAnimations.get(i).setAction(action + 1); // Middle segments
         }
      }
   }

   @Override
   protected void setAnimationsToDamage() {
      setAllAnimationsTo(TAKING_DAMAGE);
   }

   @Override
   protected void resetCustomVars() {
      attackPhaseActive = false;
      constructHitboxes();
      constructWiggleStuff();
   }

   @Override
   protected int amountOfFrames() {
      return 8;
   }

   @Override
   public double getRotation() {
      return angle;
   }

   @Override
   public boolean isSmall() {
      return true;
   }

   @Override
   public boolean canShoot() {
      return false;
   }

}
