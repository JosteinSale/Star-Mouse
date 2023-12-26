package entities.exploring;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;

import entities.Entity;
import main.Game;
import utils.LoadSave;

import static utils.Constants.Exploring.Sprites.*;
import static utils.Constants.Exploring.DirectionConstants.*;

public class Oliver extends Entity implements NPC {
    private String name = "Oliver";
    private Rectangle2D.Float triggerBox;
    private BufferedImage[][] oliverSprites;
    private int spriteWidth;
    private int spriteHeight;
    private int startCutscene = 0;
    private boolean inForeground;

    private int oliverAction = STANDING;
    private boolean poseActive = false;
    private int oliverDirection;
    private float oliverSpeed = 5f;
    
    private int aniTick = 0;
    private int aniTickPerFrame = 8;          // Antall ticks per gang animasjonen oppdateres
    private int aniIndex = 0;


    public Oliver(Float hitbox, int direction, boolean inForeground) {
        super(hitbox);
        this.oliverDirection = direction;
        this.inForeground = inForeground;
        loadSprites();
        adjustImageSizes();
        makeTriggerBox();
    }

    private void makeTriggerBox() {
        this.triggerBox = new Rectangle2D.Float(
            hitbox.x - 8, hitbox.y, hitbox.width + 16, hitbox.height + 8);
    }

    private void loadSprites() {
        BufferedImage img = LoadSave.getExpImageSprite(LoadSave.OLIVER_SPRITES);
        oliverSprites = new BufferedImage[6][4];
        for (int j = 0; j < oliverSprites.length; j++) {
            for (int i = 0; i < oliverSprites[j].length; i++) {
                oliverSprites[j][i] = img.getSubimage(
                    i * STANDARD_SPRITE_WIDTH, 
                    j * STANDARD_SPRITE_HEIGHT, 
                    STANDARD_SPRITE_WIDTH, STANDARD_SPRITE_HEIGHT);
            }
        }
    }

    private void adjustImageSizes() {
        spriteWidth = (int) (STANDARD_SPRITE_WIDTH * Game.SCALE * 3);
        spriteHeight = (int) (STANDARD_SPRITE_HEIGHT * Game.SCALE * 3);
    }

    @Override
    public void update() {
        updateAniTick();
    }

    private void updateAniTick() {
        if (poseActive) {return;}
        aniTick++;
        if (aniTick >= aniTickPerFrame) {
            aniIndex ++;
            aniTick = 0;
        }
        if (aniIndex >= GetSpriteAmount(oliverAction)) {
            aniIndex = 0;
        }
        if (oliverAction == STANDING) {
            aniIndex = oliverDirection;
        }
    }
    
    public void adjustPos(float deltaX, float deltaY) {
        this.hitbox.x += deltaX;
        this.hitbox.y += deltaY;
        this.triggerBox.x += deltaX;
        this.triggerBox.y += deltaY;
    }

    @Override
    public void draw(Graphics g, int xLevelOffset, int yLevelOffset) {
        g.drawImage(
            oliverSprites[oliverAction][aniIndex],
            (int) ((hitbox.x - 80 - xLevelOffset) * Game.SCALE), 
            (int) ((hitbox.y - 30 - yLevelOffset) * Game.SCALE), 
            spriteWidth, spriteHeight, 
            null);
        
        // Triggerbox 
        /* 
        g.setColor(Color.CYAN);
        g.drawRect(
            (int) ((triggerBox.x - xLevelOffset) * Game.SCALE),
            (int) ((triggerBox.y - yLevelOffset) * Game.SCALE),
            (int) (triggerBox.width * Game.SCALE),
            (int) (triggerBox.height * Game.SCALE));
        */
    }


    @Override
    public Rectangle2D.Float getHitbox() {
        return this.hitbox;
    }

    @Override
    public Rectangle2D.Float getTriggerBox() {
        return this.triggerBox;
    }

    public int getStartCutscene() {
        return this.startCutscene;
    }

    @Override
    public void setNewStartingCutscene(int startCutscene) {
        this.startCutscene = startCutscene;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setDir(int dir) {
        this.oliverDirection = dir;
    }

    @Override
    public void setSprite(boolean poseActive, int colIndex, int rowIndex) {
        if (poseActive == true) {
            this.poseActive = true;
            this.oliverAction = rowIndex;
            this.aniIndex = colIndex;
        }
        else {
            this.poseActive = false;
            this.aniIndex = 0;
            this.aniTick = 0;
        }
    }

    @Override
    public boolean inForeground() {
        return this.inForeground;
    }

    public void setAction(int action) {
        this.oliverAction = action;
    }
    
}
