package entities.exploring;

import static utils.Constants.Exploring.DirectionConstants.*;
import static utils.Constants.Exploring.Sprites.STANDARD_SPRITE_HEIGHT;
import static utils.Constants.Exploring.Sprites.STANDARD_SPRITE_WIDTH;
import static utils.HelpMethods.CollidesWithMap;
import static utils.HelpMethods.CollidesWithNpc;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Entity;
import main_classes.Game;
import utils.HelpMethods2;
import utils.ResourceLoader;

public class PlayerExp extends Entity {
    private Game game;
    private BufferedImage collisionImg;
    private int playerSpriteWidth;
    private int playerSpriteHeight;

    // Sprite sheets
    private ArrayList<BufferedImage[][]> playerSprites;
    private final int NORMAL_SPRITE = 0;
    private final int NAKED_SPRITE = 1;
    private final int SAD_SPRITE = 2;


    private boolean poseActive = false;
    private int playerAction;
    private int playerDirection = LEFT;
    private static int CURRENT_SPRITE_SHEET = 0;
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

    /** Add sprites according to the indexes given at the top */
    private void loadSprites() {
        BufferedImage[][] normalSprites = HelpMethods2.GetAnimationArray(
            ResourceLoader.getExpImageSprite(ResourceLoader.PLAYER_EXP_SPRITES), 
            6, 4, STANDARD_SPRITE_WIDTH, STANDARD_SPRITE_HEIGHT);
        BufferedImage[][] nakedSprites = HelpMethods2.GetAnimationArray(
            ResourceLoader.getExpImageSprite(ResourceLoader.PLAYER_EXP_SPRITES_NAKED), 
            5, 4, STANDARD_SPRITE_WIDTH, STANDARD_SPRITE_HEIGHT);
        BufferedImage[][] sadSprites = HelpMethods2.GetAnimationArray(
            ResourceLoader.getExpImageSprite(ResourceLoader.PLAYER_EXP_SPRITES_SAD), 
            5, 4, STANDARD_SPRITE_WIDTH, STANDARD_SPRITE_HEIGHT);
        playerSprites.add(normalSprites); 
        playerSprites.add(nakedSprites);
        playerSprites.add(sadSprites);
    }

    private void adjustImageSizes() {
        playerSpriteWidth = (int) (STANDARD_SPRITE_WIDTH * Game.SCALE * 3);
        playerSpriteHeight = (int) (STANDARD_SPRITE_HEIGHT * Game.SCALE * 3);
    }
    
    public void keyPressed(KeyEvent e) {}

    public void KeyReleased(KeyEvent e) {}

    public void update(ArrayList<Rectangle2D.Float> npcHitboxes, boolean cutsceneActive, boolean standardFadeActive) {
        if (!cutsceneActive && !standardFadeActive) {
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
            playerSprites.get(CURRENT_SPRITE_SHEET)[playerAction][aniIndex],
            (int) ((hitbox.x - 113 - xLevelOffset) * Game.SCALE), 
            (int) ((hitbox.y - 135 - yLevelOffset) * Game.SCALE), 
            playerSpriteWidth, playerSpriteHeight, 
            null);
        }
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

    public void setCURRENT_SPRITE_SHEET(int sheetIndex) {
        PlayerExp.CURRENT_SPRITE_SHEET = sheetIndex;
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
