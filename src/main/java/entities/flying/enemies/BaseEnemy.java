package entities.flying.enemies;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import entities.Entity;
import entities.AnimationFrame;
import entities.flying.AnimatedGlow;
import entities.flying.EntityInfo;
import main_classes.Game;

/**
 * Base class for most enemy types in the game.
 * This class serves as a common ancestor for various enemy implementations.
 * It defaults to:
 * - having one hitbox
 * - being small
 * - moving downwards only
 * - having only one direction
 * - shooting at set intervals
 * - having 2 states: idle and taking damage
 * All of these can be overridden by subclasses if needed.
 */
public abstract class BaseEnemy extends Entity implements Enemy {
   protected EntityInfo info;
   public AnimatedGlow glow; // Can be null
   protected float startY;
   protected float startX;
   protected int maxHP = 60;
   protected int HP = maxHP;
   protected boolean onScreen = false;
   protected boolean dead = false;
   protected ArrayList<Rectangle2D.Float> allHitboxes; // will contain only one hitbox by default
   protected int damageTick = 0;
   protected int shootTick = 0;
   protected int shootInterval;

   // Actions: correspond to a row in the spritesheet.
   protected int IDLE = 0;
   protected int TAKING_DAMAGE = 1;

   // Animation(s)
   protected AnimationFrame animation;
   protected ArrayList<AnimationFrame> allAnimations; // will contain only one AnimationFrame by default
   protected int aniTick;
   protected int aniTickPerFrame = 4;

   public BaseEnemy(Rectangle2D.Float hitbox, EntityInfo info, int shootInterval, AnimatedGlow glow) {
      super(hitbox);
      allHitboxes = new ArrayList<>();
      allHitboxes.add(hitbox);

      allAnimations = new ArrayList<>();
      animation = new AnimationFrame(IDLE, 0);
      allAnimations.add(animation);

      this.info = info;
      startY = hitbox.y;
      startX = hitbox.x;
      this.shootInterval = shootInterval;
      this.glow = glow;
   }

   public BaseEnemy(Rectangle2D.Float hitbox, EntityInfo info) {
      super(hitbox);
      allHitboxes = new ArrayList<>();
      allHitboxes.add(hitbox);

      allAnimations = new ArrayList<>();
      animation = new AnimationFrame(IDLE, 0);
      allAnimations.add(animation);

      this.info = info;
      startY = hitbox.y;
      startX = hitbox.x;
   }

   @Override
   public void update(float levelYSpeed) {
      hitbox.y += levelYSpeed;

      onScreen = (((hitbox.y + hitbox.height * 1.2) > 0) &&
            (hitbox.y - hitbox.height * 0.2 < Game.GAME_DEFAULT_HEIGHT));

      if (onScreen) {
         updateAniTick();
         updateShootTick();
         updateCustomBehavior(levelYSpeed);
      }
   }

   /**
    * Will be called only when the enemy is on screen.
    * Override with custom behavior if needed
    */
   protected void updateCustomBehavior(float levelYSpeed) {
      // Default behavior: Do nothing
   }

   protected void updateAniTick() {
      aniTick++;
      if (aniTick >= aniTickPerFrame) {
         aniTick = 0;
         animation.nextFrame();
         if (animation.getFrame() >= amountOfFrames()) {
            animation.setFrame(0);
         }
      }
      if (animation.getAction() == TAKING_DAMAGE) {
         damageTick--;
         if (damageTick <= 0) {
            animation.setAction(IDLE);
         }
      }
   }

   protected void updateShootTick() {
      shootTick++;
   }

   public boolean canShoot() {
      return shootTick == shootInterval;
   }

   @Override
   public Rectangle2D.Float getMainHitbox() {
      return this.hitbox;
   }

   @Override
   public ArrayList<Rectangle2D.Float> getAllHitboxes() {
      return this.allHitboxes;
   }

   @Override
   public int getType() {
      return info.typeConstant;
   }

   @Override
   public void takeDamage(int damage) {
      animation.setFrame(0);
      this.HP -= damage;
      animation.setAction(TAKING_DAMAGE);
      damageTick = 4;
      if (HP <= 0) {
         dead = true;
      }
   }

   @Override
   public boolean isDead() {
      return dead;
   }

   @Override
   public int getDir() {
      return Enemy.RIGHT;
   }

   @Override
   public boolean isOnScreen() {
      return onScreen;
   }

   @Override
   public boolean isSmall() {
      return true;
   }

   protected int amountOfFrames() {
      return 1;
   }

   @Override
   public void resetTo(float y) {
      hitbox.y = startY + y;
      hitbox.x = startX;
      if (glow != null)
         glow.reset();
      animation.setAction(IDLE);
      HP = maxHP;
      onScreen = false;
      dead = false;
      aniTick = 0;
      animation.setCol(0);
      shootTick = 0;
   }

   @Override
   public void onCollision(int damage) {
      this.takeDamage(damage);
   }

   @Override
   public EntityInfo getInfo() {
      return this.info;
   }

   @Override
   public ArrayList<AnimationFrame> getAnimationFrames() {
      return allAnimations;
   }

   @Override
   public void onShoot() {
      if (glow != null)
         glow.start();
      this.shootTick = 0;
   }

   @Override
   public boolean hasGlow() {
      return glow != null;
   }

   @Override
   public AnimatedGlow getGlow() {
      return this.glow;
   }

   @Override
   public double getRotation() {
      return 0.0;
   }
}
