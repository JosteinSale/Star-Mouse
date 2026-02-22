package entities.flying.enemies;

import java.awt.geom.Rectangle2D;

import entities.flying.EntityInfo;
import entities.flying.AnimatedGlow;

public interface Enemy {

   public static final int RIGHT = 1;
   public static final int LEFT = -1;

   /**
    * If the enemy is small, the player can teleport through it and kill it.
    * Big enemies can not be teleported through, and if the player attempts it,
    * he will collide just like he would with the map.
    * 
    * @return
    */
   public boolean isSmall();

   /**
    * Updates y-pos with levelYSpeed.
    * Also if the enemy is onScreen and !isDead: updates individual movement,
    * aniTick and shootTick.
    */
   public void update(float levelYSpeed);

   public Rectangle2D.Float getHitbox();

   public int getType();

   /**
    * Will be called in projecileHandler when the enemy is hit by a projectile.
    * Reduces the enemy's HP by the specified amount. Also sets the enemies
    * status to TAKING_DAMAGE. If HP falls below 0, it sets 'dead' to true.
    */
   public void takeDamage(int damage);

   /**
    * Will be called by the enemymanager if the player collides with the enemy.
    * Is useful if there is distinct behavior for the enemy upon collision.
    * If not, you can simply do a call to takeShootDamage in the implementation.
    */
   public void onCollision(int damage);

   /**
    * Returns true if the enemy can shoot at the current frame. Enemies can only
    * shoot at specific intervals
    */
   public boolean canShoot();

   public boolean isDead();

   /** Is used for drawing and to update individual behavior */
   public boolean isOnScreen();

   /** Resets the enemy position, active-status, aniIndex, HP and shootTimer */
   public void resetTo(float y);

   /**
    * Needed for projectileHandler and drawing. 1 = facing right, -1 = facing left.
    */
   public int getDir();

   /** Is used for drawing */
   public EntityInfo getInfo();

   /** Returns the enemy's current action (IDLE, TAKING_DAMAGE...) */
   public int getAction();

   /** Returns the current animation index in the spritesheet of the enemy */
   public int getAniIndex();

   /**
    * Called when the enemy shoots a projectile. Can activate enemy-specific
    * effects, like shoot glow or sound effects.
    */
   public void onShoot();

   public boolean hasGlow();

   public AnimatedGlow getGlow();
}
