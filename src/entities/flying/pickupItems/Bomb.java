package entities.flying.pickupItems;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entities.Entity;
import main.Game;
import utils.LoadSave;

import static utils.Constants.Flying.SpriteSizes.BOMB_SPRITE_SIZE;
import static utils.Constants.Flying.TypeConstants.BOMB;
import static utils.Constants.Flying.DrawOffsetConstants.BOMB_OFFSET_X;
import static utils.Constants.Flying.DrawOffsetConstants.BOMB_OFFSET_Y;

public class Bomb extends Entity implements PickupItem {
    private BufferedImage[] animations;
    private float startY;
    private int aniIndex;
    private int aniTick;
    private int aniTickPerFrame = 3;
    private boolean active = true;

    public Bomb(Rectangle2D.Float hitbox) {
        super(hitbox);
        startY = hitbox.y;
        loadAnimations();
    }

    private void loadAnimations() {
        BufferedImage img = LoadSave.getFlyImageSprite(LoadSave.BOMB_PICKUP_SPRITE);
        this.animations = new BufferedImage[2];
        for (int i = 0; i < animations.length; i++) {
            animations[i] = img.getSubimage(
                i * BOMB_SPRITE_SIZE, 0, BOMB_SPRITE_SIZE, BOMB_SPRITE_SIZE);
        }
    }

    public void update(float yLevelSpeed) {
        this.hitbox.y += yLevelSpeed;
        aniTick++;
        if (aniTick == aniTickPerFrame) {
            aniIndex++;
            aniTick = 0;
            if (aniIndex > 1) {
                aniIndex = 0;
            }
        }
    }

    public void draw(Graphics g) {
        //drawHitbox(g, 0, 0);
        if (active) {
            g.drawImage(
            animations[aniIndex], 
            (int) ((hitbox.x - BOMB_OFFSET_X) * Game.SCALE), 
            (int) ((hitbox.y - BOMB_OFFSET_Y) * Game.SCALE), 
            (int) (BOMB_SPRITE_SIZE * 3 * Game.SCALE),
            (int) (BOMB_SPRITE_SIZE * 3 * Game.SCALE), null);
        }
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Rectangle2D.Float getHitbox() {
        return this.hitbox;
    }

    public int getType() {
        return BOMB;
    }

    @Override
    public void resetTo(float y) {
        this.active = true;
        hitbox.y = startY + y;
    }
}
