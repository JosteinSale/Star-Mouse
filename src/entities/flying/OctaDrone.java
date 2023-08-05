package entities.flying;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entities.Entity;
import main.Game;
import static utils.Constants.Flying.Sprites.OCTADRONE_SPRITE_SIZE;
import static utils.Constants.Flying.TypeConstants.OCTADRONE;

public class OctaDrone extends Entity implements Enemy {
    // Actions
    private static final int IDLE = 1;
    private static final int TAKING_DAMAGE = 0;

    BufferedImage[][] animations;
    private int HP = 80;
    private boolean onScreen = false;   
    private boolean dead = false;

    private int action = IDLE;
    private int aniIndex = 0;
    private int aniTick;
    private int aniTickPerFrame = 3;
    private int damageFrames = 10;
    private int damageTick = 0;

    private int shootTick = 0;
    private int shootTimer;

    public OctaDrone(Rectangle2D.Float hitbox, BufferedImage[][] animations, int shootTimer) {
        super(hitbox);
        this.animations = animations;
        this.shootTimer = shootTimer;
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

    /** 
     * This enemy can only shoot once in its lifetime
     */
    private void updateShootTick() {
        shootTick ++;
    }

    /** 
     * This enemy can only shoot once in its lifetime
     */
    public boolean canShoot() {
        return shootTick == shootTimer;
    }

    @Override
    public Rectangle2D.Float getHitbox() {
        return this.hitbox;
    }

    @Override
    public int getType() {
        return OCTADRONE;
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

    public void resetShootTick() {}

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
            (int) (OCTADRONE_SPRITE_SIZE * 3 * Game.SCALE), 
            (int) (OCTADRONE_SPRITE_SIZE * 3 * Game.SCALE), null);
    }

    private int getDroneSpriteAmount() {
        switch (action) {
            case TAKING_DAMAGE:     
                return 4;     
            case IDLE:
            default:
                return 1;
        }
    }
}
