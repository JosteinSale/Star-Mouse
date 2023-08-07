package projectiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import utils.LoadSave;

import static utils.Constants.Flying.Sprites.BOMBEXPLOSION_SPRITE_WIDTH;
import static utils.Constants.Flying.Sprites.BOMBEXPLOSION_SPRITE_HEIGHT;

public class BombExplosion {
    BufferedImage[] animation;
    private int imgW;
    private int imgH;
    private int x;
    private int y;
    private int aniTick = 0;
    private int aniIndex = 0;
    private int aniTickPerFrame = 5;
    boolean done = false;        // Is true when animation is over
    boolean explode = false;     // Is true when enemies should take damage 

    public BombExplosion(BufferedImage[] animation, int x, int y) {
        this.x = x;
        this.y = y;
        this.animation = animation;
        this.imgW = BOMBEXPLOSION_SPRITE_WIDTH * 3;
        this.imgH = BOMBEXPLOSION_SPRITE_HEIGHT * 3;
    }

    public void update(float fgCurSpeed) {
        this.y += fgCurSpeed;
        this.aniTick++;
        if (aniTick > aniTickPerFrame) {
            aniIndex ++;
            aniTick = 0;
            if (aniIndex == 6) {explode = true;}
            else if (aniIndex > 10) {
                done = true;
                aniIndex = 10;
            }
        }
    }

    public void draw(Graphics g) {
        g.drawImage(
            animation[aniIndex], 
            (int) ((x - imgW/2) * Game.SCALE),
            (int) ((y - imgH/2) * Game.SCALE),
            (int) (imgW * Game.SCALE), 
            (int) (imgH * Game.SCALE), null);
    }

    public boolean isDone() {
        return this.done;
    }

    public boolean explosionHappens() {
        return this.explode;
    }
}
