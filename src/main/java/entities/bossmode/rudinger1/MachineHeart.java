package entities.bossmode.rudinger1;

import static utils.Constants.Flying.ActionConstants.IDLE;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;

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
   private Point midwayPoint;       // Midway point for start-docking
   private double dockingSpeed = 3.0;
   private double followSpeed = 5.0;

   private final int IDLE_ANIM = 0;
   private final int DAMAGE_ANIM = 1;

   private boolean startDocking;
   private boolean endDocking;
   private boolean abortAttack;     // Becomes true if endDocking is finished.
   private int nrOfCollisions = 0;  // Collisions with player
   private int behaviorTick = 0;
   private int followDuration = 500;

   private int collisionTick = 0;
   private int collisionWaitDuration = 30;
   private int damageTick = 0;
   private int damageDuration = 20;

   private int aniTick = 0;
   private int aniSpeed = 2;

   public MachineHeart(
         Float hitbox, BufferedImage img, int aniRows, int aniCols, int spriteW, int spriteH, 
         PlayerBoss player, Point startPoint) { 
      super(hitbox, img, aniRows, aniCols, spriteW, spriteH);
      this.player = player;
      this.dockingPoint = startPoint;
      this.midwayPoint = new Point(
         (int)this.dockingPoint.getX(), (int)this.dockingPoint.getY() + 200);
   }

   @Override
   public void startAttack() {
      this.animAction = IDLE_ANIM;
      this.startDocking = true;
      this.rotatedImgVisible = true;
   }

   @Override
   public void updateBehavior() {
      updateAnimations();
      updateDamageTick();

      if (startDocking) {
         updateStartDocking();
      }
      else if (endDocking) {
         updateEndDocking();
      }
      else {
         updateFollowBehavior();
      }
   }

   private void updateStartDocking() {
      behaviorTick++;
      // Start by standing still for 120 frames while boss-animation plays
      if (behaviorTick < 120) {
         return; 
      }
      // Then move to midway point and enable collision
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
         this.moveTowardsPoint(  // Move towards player
            (int)player.getHitbox().getCenterX(), 
            (int)player.getHitbox().getCenterY(),
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
         // Then abort the attack
         if (this.behaviorTick >= 120) {
            this.endDocking = false;
            this.abortAttack = true; 
         }
      }
   }

   private boolean isNotAtDockingPoint() {
      return (
         Math.abs(this.getNonRotatedHitbox().getCenterY() - this.dockingPoint.y) > 4) ||
         (Math.abs(this.getNonRotatedHitbox().getCenterX() - this.dockingPoint.x) > 4
         );
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

   @Override
   public void updateAnimations() {
      aniTick++;
      if (aniTick > aniSpeed) {
         aniTick = 0;
         aniIndex ++;
         if (aniIndex > 1) {
            aniIndex = 0;
         }
      }
   }

   private void updateDamageTick() {
      // If has taking damage
      if (damageTick > 0) {  
         damageTick--;
         if (damageTick == 0) {
            this.animAction = IDLE;
         }
      }
   }

   @Override
   public void onPlayerCollision() {
      if (endDocking || startDocking) {
         return;  // No behavior
      }
      this.collisionTick = this.collisionWaitDuration;
      this.nrOfCollisions++;
      checkIf4Collisions();
   }

   @Override
   public void onTeleportHit() {
      if (endDocking || startDocking) {
         return;  // No Behavior
      }
      this.damageTick = damageDuration;
      this.animAction = DAMAGE_ANIM;
      this.nrOfCollisions++;
      checkIf4Collisions();
   }

   private void checkIf4Collisions() {
      if (nrOfCollisions >= 4)  {
         this.endDocking = true;
         this.behaviorTick = 0;
      }
   }


   @Override
   public boolean isCharging() {
      return (startDocking || endDocking);
   }

   @Override
   public void finishAttack() {
      this.endDocking = false;
      this.collisionEnabled = false;
      this.rotatedImgVisible = false;
      this.behaviorTick = 0;
      this.nrOfCollisions = 0;
      this.abortAttack = false;
   }

   @Override
   public boolean shouldAbort() {
      return this.abortAttack;
   }

   @Override
   public void draw(Graphics g) {
      super.draw(g);
   }
   
}
