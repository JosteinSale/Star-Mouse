package entities.flying.enemies;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entities.Entity;
import main.Game;
import static utils.Constants.Flying.TypeConstants.DRONE;
import static utils.Constants.Flying.SpriteSizes.DRONE_SPRITE_SIZE;
import static utils.Constants.Flying.DrawOffsetConstants.DRONE_OFFSET_X;
import static utils.Constants.Flying.DrawOffsetConstants.DRONE_OFFSET_Y;

public class Drone extends Entity implements Enemy {
    // Actions
    private static final int IDLE = 1;
    private static final int TAKING_DAMAGE = 0;

    BufferedImage[][] animations;
    private float startY;
    private int maxHP = 60;
    private int HP = maxHP;
    private boolean onScreen = false;   
    private boolean dead = false;

    private int action = IDLE;
    private int aniIndex = 0;
    private int aniTick;
    private int aniTickPerFrame = 3;
    private int damageFrames = 10;
    private int damageTick = 0;

    private int shootTick = 0;
    private int shootInterval;

    public Drone(Rectangle2D.Float hitbox, BufferedImage[][] animations, int shootInterval) {
        super(hitbox);
        startY = hitbox.y;
        this.animations = animations;
        this.shootInterval = shootInterval;
    }

    @Override
    public void update(float levelYSpeed) {
        hitbox.y += levelYSpeed;
        onScreen = (((hitbox.y + hitbox.height) > 0) && (hitbox.y < Game.GAME_DEFAULT_HEIGHT));
        if (onScreen) {
            updateAniTick();
            updateShootTick();
        }
    }

    private void updateAniTick() {
        aniTick++;
        if (aniTick >= aniTickPerFrame) {
            aniTick = 0;
            aniIndex ++;
            if (aniIndex >= getDroneSpriteAmount()) {
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

    /** Later: we might want to customize shootInterval so that each enemy
     * has a specific shootinterval, and the tick starts when enemy is onScreen.
     */
    private void updateShootTick() {
        shootTick ++;
        if (shootTick > shootInterval) {
            shootTick = 0;
        }
    }

    public boolean canShoot() {
        return shootTick == shootInterval;
    }

    @Override
    public Rectangle2D.Float getHitbox() {
        return this.hitbox;
    }

    @Override
    public int getType() {
        return DRONE;
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
    public int getDir() {
        return 0; // No dir
    }

    @Override
    public boolean isOnScreen() {
        return onScreen;
    }

    @Override
    public boolean isSmall() {
      return true;
    }

    public void resetShootTick() {
        this.shootTick = 0;
    }

    @Override
    public void drawHitbox(Graphics g) {
        this.drawHitbox(g, 0, 0);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(
            animations[action][aniIndex], 
            (int) ((hitbox.x - DRONE_OFFSET_X) * Game.SCALE), 
            (int) ((hitbox.y - DRONE_OFFSET_Y)* Game.SCALE), 
            (int) (DRONE_SPRITE_SIZE * 3 * Game.SCALE), 
            (int) (DRONE_SPRITE_SIZE * 3 * Game.SCALE), null);
    }

    private int getDroneSpriteAmount() {
        switch (action) {
            case TAKING_DAMAGE:     
                return 3;     
            case IDLE:
            default:
                return 1;
        }
    }

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
      shootTick = 0;
   }
}
