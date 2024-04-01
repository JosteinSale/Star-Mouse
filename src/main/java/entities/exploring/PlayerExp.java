package entities.exploring;

import static utils.Constants.Exploring.DirectionConstants.*;
import static utils.Constants.Exploring.Sprites.STANDARD_SPRITE_HEIGHT;
import static utils.Constants.Exploring.Sprites.STANDARD_SPRITE_WIDTH;
import static utils.HelpMethods.CollidesWithMap;
import static utils.HelpMethods.CollidesWithNpc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Entity;
import main_classes.Game;
import utils.LoadSave;

public class PlayerExp extends Entity {
    Game game;
    ArrayList<BufferedImage[][]> playerSprites;
    BufferedImage collisionImg;
    private Color shadowColor = new Color(0, 0, 0, 50);
    private int playerSpriteWidth;
    private int playerSpriteHeight;

    private boolean poseActive = false;
    private int playerAction;
    private int playerDirection = LEFT;
    private int spriteSheet = 0;
    private float playerSpeed = 5f;
    private boolean visible = true;
    
    private int aniTick = 0;
    private int aniTickPerFrame = 8;          // Antall ticks per gang animasjonen oppdateres
    private int aniIndex = 0;

    public PlayerExp(Game game, Float hitbox, int direction, BufferedImage collisionImg) {
        super(hitbox);
        this.game = game;
        this.playerDirection = direction;
        playerAction = STANDING;
        this.collisionImg = collisionImg;
        this.playerSprites = new ArrayList<>();
        loadSprites();
        adjustImageSizes();
    }

    private void loadSprites() {
        BufferedImage image = LoadSave.getExpImageSprite(LoadSave.PLAYER_EXP_SPRITES);
        BufferedImage[][] normalSprites = new BufferedImage[6][4];
        for (int j = 0; j < normalSprites.length; j++) {
            for (int i = 0; i < normalSprites[j].length; i++) {
                normalSprites[j][i] = image.getSubimage(
                    i * STANDARD_SPRITE_WIDTH, 
                    j * STANDARD_SPRITE_HEIGHT, 
                    STANDARD_SPRITE_WIDTH, STANDARD_SPRITE_HEIGHT);
            }
        }
        playerSprites.add(normalSprites);
        
        BufferedImage image2 = LoadSave.getExpImageSprite(LoadSave.PLAYER_EXP_SPRITES_NAKED);
        BufferedImage[][] nakedSprites = new BufferedImage[5][4];
        for (int j = 0; j < nakedSprites.length; j++) {
            for (int i = 0; i < nakedSprites[j].length; i++) {
                nakedSprites[j][i] = image2.getSubimage(
                    i * STANDARD_SPRITE_WIDTH, 
                    j * STANDARD_SPRITE_HEIGHT, 
                    STANDARD_SPRITE_WIDTH, STANDARD_SPRITE_HEIGHT);
            }
        }
        playerSprites.add(nakedSprites);
    }

    private void adjustImageSizes() {
        playerSpriteWidth = (int) (STANDARD_SPRITE_WIDTH * Game.SCALE * 3);
        playerSpriteHeight = (int) (STANDARD_SPRITE_HEIGHT * Game.SCALE * 3);
    }
    
    public void keyPressed(KeyEvent e) {}

    public void KeyReleased(KeyEvent e) {}

    public void update(ArrayList<Rectangle2D.Float> npcHitboxes, boolean cutsceneActive) {
        if (!cutsceneActive) {
            handleKeyboardInputs(npcHitboxes);
            updatePlayerAction();
        }
        updateAniTick();
    }

    private void handleKeyboardInputs(ArrayList<Rectangle2D.Float> npcHitboxes) {
        if (game.leftIsPressed) {
            playerAction = WALKING_LEFT;
            playerDirection = LEFT;
            hitbox.x -= playerSpeed;
            if (CollidesWithMap(hitbox, collisionImg) || CollidesWithNpc(hitbox, npcHitboxes)) {
                hitbox.x += playerSpeed;
            }
        }
        if (game.upIsPressed && !(game.leftIsPressed && game.rightIsPressed)) {
            playerAction = WALKING_UP;
            playerDirection = UP;
            hitbox.y -= playerSpeed;
            if (CollidesWithMap(hitbox, collisionImg) || CollidesWithNpc(hitbox, npcHitboxes)) {
                hitbox.y += playerSpeed;
            }
        }
        if (game.rightIsPressed) {
            playerAction = WALKING_RIGHT;
            playerDirection = RIGHT;
            hitbox.x += playerSpeed;
            if (CollidesWithMap(hitbox, collisionImg) || CollidesWithNpc(hitbox, npcHitboxes)) {
                hitbox.x -= playerSpeed;
            }
        }
        if (game.downIsPressed && !(game.leftIsPressed && game.rightIsPressed)) {
            playerAction = WALKING_DOWN;
            playerDirection = DOWN;
            hitbox.y += playerSpeed;
            if (CollidesWithMap(hitbox, collisionImg) || CollidesWithNpc(hitbox, npcHitboxes)) {
                hitbox.y -= playerSpeed;
            }
        }
    }

    private void updatePlayerAction() {
        if (!game.upIsPressed && !game.downIsPressed && !game.leftIsPressed && !game.rightIsPressed) {
            playerAction = STANDING;
            return;
        }
        if ((game.upIsPressed && game.downIsPressed) || (game.leftIsPressed && game.rightIsPressed)) {
            playerAction = STANDING;
        }
    }

    private void updateAniTick() {
        if (poseActive) {return;}
        aniTick++;
        if (aniTick >= aniTickPerFrame) {
            aniIndex ++;
            aniTick = 0;
        }
        if (aniIndex >= GetSpriteAmount(playerAction)) {
            aniIndex = 0;
        }
        if (playerAction == STANDING) {
            aniIndex = playerDirection;
        }
    }

    public void resetAll() {
        game.downIsPressed = false;
        game.rightIsPressed = false;
        game.leftIsPressed = false;
        game.upIsPressed = false;
        this.playerAction = STANDING;
        this.aniTick = 0;
        this.aniIndex = 0;
    }

    public void draw(Graphics g, int xLevelOffset, int yLevelOffset) {
        if (visible) {
            //drawShadow(g, xLevelOffset, yLevelOffset);
            g.drawImage(
            playerSprites.get(spriteSheet)[playerAction][aniIndex],
            (int) ((hitbox.x - 113 - xLevelOffset) * Game.SCALE), 
            (int) ((hitbox.y - 135 - yLevelOffset) * Game.SCALE), 
            playerSpriteWidth, playerSpriteHeight, 
            null);
        }
    }

    private void drawShadow(Graphics g, int xLevelOffset, int yLevelOffset) {
        g.setColor(shadowColor);
        g.fillOval(
            (int) ((hitbox.x - xLevelOffset) * Game.SCALE), 
            (int) ((hitbox.y  - yLevelOffset) * Game.SCALE), 
            (int) (hitbox.width * Game.SCALE), (int) (hitbox.height * Game.SCALE));
    }

    public Rectangle2D.Float getHitbox() {
        return this.hitbox;
    }

    public void setDirection(int dir) {
        this.playerDirection = dir;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setSpriteSheet(int sheetIndex) {
        this.spriteSheet = sheetIndex;
    }

    public void adjustPos(float walkSpeedX, float walkSpeedY) {
        hitbox.x += walkSpeedX;
        hitbox.y += walkSpeedY;
    }

    public void setAction(int action) {
        this.playerAction = action;
    }

    public void setSprite(boolean pose, int colIndex, int rowIndex) {
        if (pose == true) {
            this.poseActive = true;
            this.playerAction = rowIndex;
            this.aniIndex = colIndex;
        }
        else {  // Stop posing
            this.poseActive = false;
            this.playerAction = STANDING;
            this.aniIndex = 0;
        }
    }
}
