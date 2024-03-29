package entities.flying.enemies;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entities.Entity;
import main.Game;
import static utils.Constants.Flying.TypeConstants.SMALLSHIP;
import static utils.Constants.Flying.SpriteSizes.SMALLSHIP_SPRITE_SIZE;
import static utils.Constants.Flying.DrawOffsetConstants.SMALLSHIP_OFFSET_X;
import static utils.Constants.Flying.DrawOffsetConstants.SMALLSHIP_OFFSET_Y;

public class SmallShip extends Entity implements Enemy{
    // Actions
    private static final int IDLE = 1;
    private static final int TAKING_DAMAGE = 0;

    private int direction;            // 1 = right, -1 = left
    private int xSpeed = 2;

    BufferedImage[][] animations;
    private float startY;
    private float startX;
    private int maxHP = 20;
    private int HP = maxHP;
    private boolean onScreen = false;   
    private boolean dead = false;

    private int action = IDLE;
    private int aniIndex = 0;
    private int aniTick;
    private int aniTickPerFrame = 3;
    private int damageFrames = 10;
    private int damageTick = 0;


    public SmallShip(Rectangle2D.Float hitbox, BufferedImage[][] animations, int direction) {
        super(hitbox);
        startX = hitbox.x;
        startY = hitbox.y;
        this.animations = animations;
        this.direction = direction;
    }

    @Override
    public void update(float levelYSpeed) {
        hitbox.y += levelYSpeed;
        onScreen = (((hitbox.y + hitbox.height) > 0) && (hitbox.y < Game.GAME_DEFAULT_HEIGHT));
        if (onScreen) {
            updateAniTick();
            this.hitbox.x += xSpeed * direction;
        }
    }

    private void updateAniTick() {
        aniTick++;
        if (aniTick >= aniTickPerFrame) {
            aniTick = 0;
            aniIndex ++;
            if (aniIndex >= getSmallShipSpriteAmount()) {
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

    public boolean canShoot() {
        return false;
    }

    @Override
    public Rectangle2D.Float getHitbox() {
        return this.hitbox;
    }

    @Override
    public int getType() {
        return SMALLSHIP;
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
    public int getDir() {
        return this.direction;
    }

    @Override
    public boolean isSmall() {
      return true;
    }

    @Override
    public void drawHitbox(Graphics g) {
        this.drawHitbox(g, 0, 0);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(
            animations[action][aniIndex], 
            (int) ((hitbox.x - SMALLSHIP_OFFSET_X + getFlipX()) * Game.SCALE), 
            (int) ((hitbox.y - SMALLSHIP_OFFSET_Y)* Game.SCALE), 
            (int) (SMALLSHIP_SPRITE_SIZE * 3 * direction * Game.SCALE), 
            (int) (SMALLSHIP_SPRITE_SIZE * 3 * Game.SCALE), null);
    }

    private float getFlipX() {
        if (direction == 1) {
            return 0;
        }
        else {
            return (hitbox.width + 28);
        }
    }

    private int getSmallShipSpriteAmount() {
        switch (action) {
            case TAKING_DAMAGE:     
                return 4;     
            case IDLE:
            default:
                return 1;
        }
    }

    @Override
    public void resetShootTick() {}

    @Override
   public void resetTo(float y) {
      hitbox.y = startY + y;
      hitbox.x = startX;
      action = IDLE;
      HP = maxHP;
      onScreen = false;
      dead = false;
      aniTick = 0;
      aniIndex = 0;
      damageTick = 0;
   }
    
}
