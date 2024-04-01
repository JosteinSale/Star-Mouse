package entities.flying.enemies;

import static utils.Constants.Flying.SpriteSizes.TARGET_SPRITE_SIZE;
import static utils.Constants.Flying.TypeConstants.TARGET;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;

import entities.Entity;
import main_classes.Game;


public class Target extends Entity implements Enemy {
    // Actions
    private static final int IDLE = 1;
    private static final int TAKING_DAMAGE = 0;

    BufferedImage[][] animations;
    private float startY;
    private int maxHP = 25;
    private int HP = maxHP;
    private boolean onScreen = false;   
    private boolean dead = false;

    private int action = IDLE;
    private int aniIndex;
    private int aniTick;
    private int aniTickPerFrame = 3;
    private int damageFrames = 10;
    private int damageTick = 0;

    public Target(Rectangle2D.Float hitbox, BufferedImage[][] animations) {
        super(hitbox);
        startY = hitbox.y;
        this.animations = animations;
    }

    @Override
    public void update(float levelYSpeed) {
        hitbox.y += levelYSpeed;
        onScreen = (((hitbox.y + hitbox.height) > 0) && (hitbox.y < Game.GAME_DEFAULT_HEIGHT));
        if (onScreen) {
            updateAniTick();
        }
    }

    private void updateAniTick() {
        aniTick++;
        if (aniTick >= aniTickPerFrame) {
            aniTick = 0;
            aniIndex ++;
            if (aniIndex >= getTargetSpriteAmount()) {
                aniIndex = 0;
            }
        }
        if (action == TAKING_DAMAGE) {
            damageTick --;
            if (damageTick <= 0) {
                action = IDLE;
            }
        }
        
    }

    @Override
    public Float getHitbox() {
        return this.hitbox;
    }

    @Override
    public int getType() {
        return TARGET;
    }

    @Override
    public void takeDamage(int damage) {
        this.HP -= damage;
        this.action = TAKING_DAMAGE;
        this.damageTick = damageFrames;
        if (HP <= 0) {
            dead = true;
        }
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    @Override
    public boolean isOnScreen() {
        return onScreen;
    }

    @Override
    public void drawHitbox(Graphics g) {
        this.drawHitbox(g, 0, 0);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(
            animations[action][aniIndex], 
            (int) (hitbox.x * Game.SCALE), 
            (int) (hitbox.y * Game.SCALE), 
            (int) (TARGET_SPRITE_SIZE * 3 * Game.SCALE), 
            (int) (TARGET_SPRITE_SIZE * 3 * Game.SCALE), null);
    }

    private int getTargetSpriteAmount() {
        switch (action) {
            case TAKING_DAMAGE:          
                return 4;
            case IDLE:
            default:
                return 1;
        }
    }

    @Override
    public boolean canShoot() {
        return false;
    }

    @Override
    public boolean isSmall() {
      return true;
    }

    @Override
    public int getDir() {
        return 0; // No dir
    }

    @Override
    public void resetShootTick() {}

    @Override
   public void resetTo(float y) {
      hitbox.y = startY + y;
      action = IDLE;
      HP = maxHP;
      onScreen = false;
      dead = false;
      aniTick = 0;
      aniIndex = 0;
      damageTick = 0;
   }
}
