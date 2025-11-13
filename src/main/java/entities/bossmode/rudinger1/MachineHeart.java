package entities.bossmode.rudinger1;

import java.awt.Point;
import java.awt.geom.Rectangle2D.Float;

import entities.bossmode.AnimatedComponentFactory;
import entities.bossmode.DefaultBossPart;
import entities.bossmode.PlayerBoss;

/**
 * The MachineHeart starts by exiting its docking-point.
 * Then it goes into a state where it follows player.
 * It will do so for A) a specific duration, or B) as long as
 * less than 4 player-collisions are registered.
 * Once it exits this state, it returns to its docking-point, and lastly
 * activates the 'abortAttack'-boolean.
 * 
 * OBS: this attack does not have a globally controlled duration.
 */
public class MachineHeart extends DefaultBossPart {
   private PlayerBoss player;
   private Point dockingPoint;
   private Point midwayPoint; // Midway point for start-docking
   private double dockingSpeed = 3.0;
   private double followSpeed = 5.0;

   // Animation states
   public static final String IDLE = "IDLE";
   public static final String DAMAGE = "DAMAGE";

   private boolean startDocking;
   private boolean endDocking;
   private boolean abortAttack; // Becomes true if endDocking is finished.
   private int nrOfCollisions = 0; // Collisions with player
   private int behaviorTick = 0;
   private int followDuration = 500;

   private int collisionTick = 0;
   private int collisionWaitDuration = 30;
   private int damageTick = 0;
   private int damageDuration = 20;

   public MachineHeart(
         Float hitbox, AnimatedComponentFactory animationFactory,
         PlayerBoss player, Point startPoint) {
      super(hitbox, animationFactory.getMachineHeartAnimation((int) hitbox.x, (int) hitbox.y));
      this.player = player;
      this.dockingPoint = startPoint;
      this.midwayPoint = new Point(
            (int) this.dockingPoint.getX(), (int) this.dockingPoint.getY() + 200);
   }

   @Override
   public void startAttack() {
      animation.setAnimation(IDLE);
      this.startDocking = true;
   }

   @Override
   public void updateBehavior() {
      updateAnimations();
      updateDamageTick();

      if (startDocking) {
         updateStartDocking();
      } else if (endDocking) {
         updateEndDocking();
      } else {
         updateFollowBehavior();
      }
   }

   private void updateStartDocking() {
      behaviorTick++;
      // Start by standing still for 120 frames while boss-animation plays
      if (behaviorTick < 120) {
         if (behaviorTick > 70) {
            this.isVisible = true;
         } // Syncronizing with mouth animation
         return;
      }
      // Then move towards midway docking-point
      collisionEnabled = true;
      updatePosition(0, (int) dockingSpeed, 0.0);
      if (getNonRotatedHitbox().getCenterY() >= midwayPoint.y) {
         startDocking = false;
         behaviorTick = 0;
      }
   }

   private void updateFollowBehavior() {
      // If has collided
      if (collisionTick > 0) {
         collisionTick--;
      }
      // If not taking damage and hasn't collided
      if (this.collisionTick == 0 && this.damageTick == 0) {
         this.moveTowardsPoint( // Move towards player
               (int) player.getHitbox().getCenterX(),
               (int) player.getHitbox().getCenterY(),
               followSpeed);
      }
      // Check if the follow phase is over
      this.behaviorTick++;
      if ((this.behaviorTick > followDuration) || nrOfCollisions >= 4) {
         this.endDocking = true;
         this.behaviorTick = 0;
      }
   }

   private void updateEndDocking() {
      // First move to endpoint
      if (isNotAtDockingPoint()) {
         moveTowardsPoint(
               (int) dockingPoint.getX(),
               (int) dockingPoint.getY(),
               dockingSpeed);
      }
      // Then stand still for 120 frames while boss-animation plays
      else {
         this.behaviorTick++;
         this.collisionEnabled = false;
         if (behaviorTick > 50) {
            this.isVisible = false;
         } // Syncronizing with mouth animation
           // Then abort the attack
         if (this.behaviorTick >= 120) {
            this.endDocking = false;
            this.abortAttack = true;
         }
      }
   }

   private boolean isNotAtDockingPoint() {
      return (Math.abs(this.getNonRotatedHitbox().getCenterY() - this.dockingPoint.y) > 4) ||
            (Math.abs(this.getNonRotatedHitbox().getCenterX() - this.dockingPoint.x) > 4);
   }

   private void moveTowardsPoint(int x, int y, double speed) {
      // Calculate the direction vector of the line
      double dx = x - this.nonRotatedHitbox.getCenterX();
      double dy = y - this.nonRotatedHitbox.getCenterY();

      // Calculate the length of the line
      double lineLength = Math.sqrt(dx * dx + dy * dy);

      // Normalize the direction vector and adjust for speed.
      dx = (dx / lineLength) * speed;
      dy = (dy / lineLength) * speed;

      this.updatePosition((int) dx, (int) dy, 0.0);
   }

   private void updateDamageTick() {
      // If has taking damage
      if (damageTick > 0) {
         damageTick--;
         if (damageTick == 0) {
            animation.setAnimation(IDLE);
         }
      }
   }

   @Override
   public void onPlayerCollision() {
      if (endDocking || startDocking) {
         return; // No behavior
      } else {
         this.collisionTick = this.collisionWaitDuration;
         this.nrOfCollisions++;
         checkIf4Collisions();
      }
   }

   @Override
   public void onTeleportHit() {
      if (endDocking || startDocking) {
         return; // No Behavior
      } else {
         this.damageTick = damageDuration;
         animation.setAnimation(DAMAGE);
         this.nrOfCollisions++;
         checkIf4Collisions();
      }
   }

   private void checkIf4Collisions() {
      if (nrOfCollisions >= 4) {
         this.endDocking = true;
         this.behaviorTick = 0;
      }
   }

   @Override
   public boolean isCharging() {
      return startDocking;
   }

   @Override
   public boolean isCoolingDown() {
      return endDocking && behaviorTick > 40; // Syncronized with animations
   }

   @Override
   public void finishAttack() {
      this.setPosition((int) dockingPoint.getX(), (int) dockingPoint.getY(), 0.0);
      this.endDocking = false;
      this.collisionEnabled = false;
      this.isVisible = false;
      this.behaviorTick = 0;
      this.nrOfCollisions = 0;
      this.abortAttack = false;
   }

   @Override
   public boolean shouldAbort() {
      return this.abortAttack;
   }

   @Override
   public boolean stopsProjectiles() {
      return true;
   }

}
