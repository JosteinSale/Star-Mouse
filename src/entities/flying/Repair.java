package entities.flying;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;

import entities.Entity;
import main.Game;
import utils.LoadSave;

import static utils.Constants.Flying.Sprites.REPAIR_SPRITE_SIZE;
import static utils.Constants.Flying.TypeConstants.REPAIR;

public class Repair extends Entity implements PickupItem {
    private BufferedImage[] animations;
    private float startY;
    private int aniIndex;
    private int aniTick;
    private int aniTickPerFrame = 7;
    private boolean active = true;
    private int healthIncrease = 100;

    public Repair(Float hitbox) {
        super(hitbox);
        startY = hitbox.y;
        loadAnimations();
    }

    private void loadAnimations() {
        BufferedImage img = LoadSave.getFlyImageSprite(LoadSave.REPAIR_SPRITE);
        this.animations = new BufferedImage[4];
        for (int i = 0; i < animations.length; i++) {
            animations[i] = img.getSubimage(
                i * REPAIR_SPRITE_SIZE, 0, REPAIR_SPRITE_SIZE, REPAIR_SPRITE_SIZE);
        }
    }

    public void update(float yLevelSpeed) {
        this.hitbox.y += yLevelSpeed;
        aniTick++;
        if (aniTick == aniTickPerFrame) {
            aniIndex++;
            aniTick = 0;
            if (aniIndex > 3) {
                aniIndex = 0;
            }
        }
    }

    public void draw(Graphics g) {
        //drawHitbox(g, 0, 0);
        if (active) {
            g.drawImage(
            animations[aniIndex], 
            (int) ((hitbox.x - 15) * Game.SCALE), 
            (int) ((hitbox.y - 15) * Game.SCALE), 
            (int) (REPAIR_SPRITE_SIZE * 3 * Game.SCALE),
            (int) (REPAIR_SPRITE_SIZE * 3 * Game.SCALE), null);
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

    public int getHealth() {
        return this.healthIncrease;
    }

    public int getType() {
        return REPAIR;
    }

    @Override
    public void resetTo(float y) {
        this.active = true;
        hitbox.y = startY + y;
    }
    
}
