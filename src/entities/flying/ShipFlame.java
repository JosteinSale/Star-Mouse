package entities.flying;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import utils.LoadSave;
import static utils.Constants.Flying.Sprites.SHIP_FLAMESPRITE_WIDTH;
import static utils.Constants.Flying.Sprites.SHIP_FLAMESPRITE_HEIGHT;

public class ShipFlame {
    private BufferedImage[] animations;
    private int aniIndex = 0;
    private int aniTick = 0;
    private int aniTickPerFrame = 2;

    public ShipFlame() {
        loadAnimations();
    }

    private void loadAnimations() {
        BufferedImage img = LoadSave.getFlyImageSprite(LoadSave.SHIP_FLAME_SPRITES);
        this.animations = new BufferedImage[2];
        for (int i = 0; i < animations.length; i++) {
            animations[i] = img.getSubimage(
                i * SHIP_FLAMESPRITE_WIDTH, 0, SHIP_FLAMESPRITE_WIDTH, SHIP_FLAMESPRITE_HEIGHT);
        }
    }

    public void update() {
        aniTick ++;
        if (aniTick > aniTickPerFrame) {
            aniTick = 0;
            aniIndex = (aniIndex + 1) % animations.length;
        }
    }

    public void draw(Graphics g, float xPos, float yPos) {
        g.drawImage(
            animations[aniIndex], 
            (int) (xPos * Game.SCALE), 
            (int) (yPos * Game.SCALE),
            (int) (SHIP_FLAMESPRITE_WIDTH * 3 * Game.SCALE),
            (int) (SHIP_FLAMESPRITE_HEIGHT * 3 * Game.SCALE),
            null);
    }

}
