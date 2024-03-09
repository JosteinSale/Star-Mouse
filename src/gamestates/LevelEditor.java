package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import main.Game;
import utils.LoadSave;

import static utils.Constants.Flying.SpriteSizes.FLAMEDRONE_SPRITE_HEIGHT;
import static utils.Constants.Flying.SpriteSizes.FLAMEDRONE_SPRITE_WIDTH;
import static utils.Constants.Flying.SpriteSizes.KAMIKAZEDRONE_SPRITE_SIZE;
import static utils.Constants.Flying.SpriteSizes.REAPERDRONE_SPRITE_HEIGHT;
import static utils.Constants.Flying.SpriteSizes.REAPERDRONE_SPRITE_WIDTH;
import static utils.Constants.Flying.SpriteSizes.SMALL_SPRITES_SIZE;
import static utils.Constants.Flying.SpriteSizes.WASPDRONE_SPRITE_SIZE;
import static utils.Constants.Flying.HitboxConstants.*;
import static utils.Constants.Flying.TypeConstants.*;
import static utils.HelpMethods2.GetEntityDrawOffsets;
import static utils.HelpMethods2.GetEntitySpriteSizes;


/**
 * Draws all entities on screen, with their hitbox represented in black.
 * Images, coordinates and hitboxes are held in separate lists. 
 * Thus a specific entity is only identifies by it's common index in the different lists.
 * 
 * When the levelEditor is started, it loads levelData from the specified level.
 * 
 * See comments in keyPressed-method for controls.
 * When you're satisfied with the placement of enemies, you can print the data to
 * the console, and copy-paste it into the levelData-file (overwriting the entity entries).
 * 
 * OBS: for almost all list-indexing in this object, we use Constants.Flying.TypeConstants.
 */
public class LevelEditor implements Statemethods {
    private Game game;
    private Integer level = 1;
    private BufferedImage clImg;
    private BufferedImage[] entityImgs;                  
    private ArrayList<Rectangle> hitboxes;
    private ArrayList<int[]> entityCor;
    private int[][] entitySpriteSizes;   // For each inner list: 0 = spritewidth, 1 = spriteheight.
    private int[][] entityDrawOffsets;
    private ArrayList<String> levelData;
    private ArrayList<Integer> entityDirections;             // For entities with directions
    private ArrayList<Integer> entityTypes;
    private ArrayList<Integer> shootTimers;                  // For entities with shootTimers

    private static final int UP = 1;
    private static final int DOWN = -1;
    private int clImgHeight;
    private int clImgWidth;
    private int clYOffset;
    private float clXOffset;
    private int mapYOffset = 0;
    private int selectedEntity = 0;
    
    private int curDirection = 1;      // 1 = right, -1 = left
    private int shootTimer = 100;   // Drones : 100 - 160, Octadrones : 60 - 180
    private Font font = LoadSave.getInfoFont();
    private int cursorX = 500;
    private int cursorY = 400;
    private int cursorSpeed = 10;

    public LevelEditor(Game game) {
        this.game = game;
        loadMapImages(level);
        loadEntityImages();
        this.entityCor = new ArrayList<>();
        this.hitboxes = new ArrayList<>();
        this.levelData = new ArrayList<>();
        this.entityDirections = new ArrayList<>();
        this.entityTypes = new ArrayList<>();   
        this.shootTimers = new ArrayList<>();
        entitySpriteSizes = GetEntitySpriteSizes();
        entityDrawOffsets = GetEntityDrawOffsets();
        loadLevelData(level);
    }

    

    private void loadMapImages(int lvl) {
        this.clImg = LoadSave.getFlyImageCollision("level" + Integer.toString(lvl) + "_cl.png");
        this.clImgHeight = clImg.getHeight() * 3;
        this.clImgWidth = clImg.getWidth() * 3;
        this.clYOffset = Game.GAME_DEFAULT_HEIGHT - clImgHeight + 150;
        this.clXOffset = 150;
    }

    private void loadEntityImages() {
        this.entityImgs = new BufferedImage[14];
        BufferedImage img = LoadSave.getFlyImageSprite(LoadSave.SMALL_ENTITY_SPRITES);
        for (int i = 0; i < 10; i++) {
            entityImgs[i] = img.getSubimage(
                i * SMALL_SPRITES_SIZE, 0, SMALL_SPRITES_SIZE, SMALL_SPRITES_SIZE);
        }
        entityImgs[REAPERDRONE] = 
            LoadSave.getFlyImageSprite(LoadSave.REAPERDRONE_SPRITE).getSubimage(
                0, REAPERDRONE_SPRITE_HEIGHT, REAPERDRONE_SPRITE_WIDTH, REAPERDRONE_SPRITE_HEIGHT);

        entityImgs[FLAMEDRONE] = 
            LoadSave.getFlyImageSprite(LoadSave.FLAMEDRONE_SPRITE).getSubimage(
                0, FLAMEDRONE_SPRITE_HEIGHT, FLAMEDRONE_SPRITE_WIDTH, FLAMEDRONE_SPRITE_HEIGHT);
            
        entityImgs[WASPDRONE] = 
            LoadSave.getFlyImageSprite(LoadSave.WASPDRONE_SPRITE).getSubimage(
                0, WASPDRONE_SPRITE_SIZE, WASPDRONE_SPRITE_SIZE, WASPDRONE_SPRITE_SIZE);
            
        entityImgs[KAMIKAZEDRONE] = 
            LoadSave.getFlyImageSprite(LoadSave.KAMIKAZEDRONE_SPRITE).getSubimage(
                0, KAMIKAZEDRONE_SPRITE_SIZE, KAMIKAZEDRONE_SPRITE_SIZE, KAMIKAZEDRONE_SPRITE_SIZE);
        
    }

    private void loadLevelData(Integer level) {
        List<String> levelData = LoadSave.getFlyLevelData(level);
        for (String line : levelData) {
            String[] lineData = line.split(";");
                int entity = switch(lineData[0]) {   // index in entityImgs-array
                    case "powerup" -> POWERUP;
                    case "repair" -> REPAIR;
                    case "bomb" -> BOMB;
                    case "target" -> TARGET;
                    case "drone" -> DRONE;
                    case "smallShip" -> SMALLSHIP;
                    case "octaDrone" -> OCTADRONE;
                    case "tankDrone" -> TANKDRONE;
                    case "blasterDrone" -> BLASTERDRONE;
                    case "reaperDrone" -> REAPERDRONE;
                    case "flameDrone" -> FLAMEDRONE;
                    case "waspDrone" -> WASPDRONE;
                    case "kamikazeDrone" -> KAMIKAZEDRONE;
                    default -> 99;    // For entities currently not handled in addEntityToList()
                };
                
            if (entity < 99) {
                int xCor = Integer.parseInt(lineData[1]);
                int yCor = Integer.parseInt(lineData[2]);
                int dir = Integer.parseInt(lineData[3]);
                int shootTimer = Integer.parseInt(lineData[4]);
                addEntityToList(entity, xCor, yCor, dir, shootTimer);
            }
        }
    }

    @Override
    public void update() {}

    /**
     * Controls:
     * ---------------------------------------------
     * w / s                         Change screen 
     * x                             Change entity
     * UP / DOWN / LEFT / RIGHT      Change cursor position
     * f                             Change entity direction
     * a / d                         Change shootTimer
     * SPACE                         Add / delete entity
     * p                             Print levelData in console
     */
    public void handleKeyboardInputs(KeyEvent e) {
        if (game.upIsPressed) {
            game.upIsPressed = false;
            this.changeScreen(UP);
        }
        else if (game.downIsPressed) {
            game.downIsPressed = false;
            this.changeScreen(DOWN);
        }
        else if (game.leftIsPressed) {
            game.leftIsPressed = false;
            this.shootTimer -= 10;
        }
        else if (game.rightIsPressed) {
            game.rightIsPressed = false;
            this.shootTimer += 10;
        }
        else if (game.interactIsPressed) {
            game.interactIsPressed = false;
            int adjustedY = cursorY + mapYOffset;
            addEntityToList(selectedEntity, cursorX, adjustedY, curDirection, this.shootTimer);
        }
        // Here we use som KeyEvents, but if it leads to trouble we can add booleans in game-object.
        else if (e.getKeyCode() == KeyEvent.VK_UP) {
            this.cursorY -= cursorSpeed;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            this.cursorY += cursorSpeed;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            this.cursorX -= cursorSpeed;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.cursorX += cursorSpeed;
        }
        else if (e.getKeyCode() == KeyEvent.VK_X) {
            selectedEntity = (selectedEntity + 1) % entityImgs.length;
        }
        else if (e.getKeyCode() == KeyEvent.VK_F) {
            game.bombIsPressed = false;
            curDirection *= -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_P) {
            printLevelData();
        }
    }

    private void printLevelData() {
        for (String line : levelData) {
            System.out.println(line);
        }
    }

    private void changeScreen(int upDown) {   // up = 1, down = -1
        this.clYOffset += Game.GAME_DEFAULT_HEIGHT * upDown;
        this.mapYOffset -= Game.GAME_DEFAULT_HEIGHT * upDown;
    }

    /** Is used to 
     *      1) load the data from levelData-file
     *      2) make new levelData from levelEditor
    */
    private void addEntityToList(int entity, int x, int y, int direction, int shootTimer) {
        int width = 150;    // Hitbox-size if no entities match
        int height = 150;
        x += entityDrawOffsets[entity][0];
        y += entityDrawOffsets[entity][1];

        if (entity == DELETE) {    
            Rectangle hitbox = new Rectangle(x, y, 90, 90);
            int indexToRemove = -1;
            for (int i = 0; i < hitboxes.size(); i++) {
                if (hitbox.intersects(hitboxes.get(i))) {
                    indexToRemove = i;
                    break;
                }
            }
            if (indexToRemove > -1) {
                hitboxes.remove(indexToRemove);
                entityCor.remove(indexToRemove);
                levelData.remove(indexToRemove);
                entityTypes.remove(indexToRemove);
                entityDirections.remove(indexToRemove);
                shootTimers.remove(indexToRemove);
            }
            return;
        }
        else if (entity == POWERUP) {
            levelData.add("powerup;" + Integer.toString(x) + ";" + 
                Integer.toString(y) + ";" + Integer.toString(direction) + ";0");
            width = POWERUP_HITBOX_W;
            height = POWERUP_HITBOX_H;
        }
        else if (entity == REPAIR) {
            levelData.add("repair;" + Integer.toString(x) + ";" 
                + Integer.toString(y) + ";" + Integer.toString(direction) + ";0");
            width = REPAIR_HITBOX_SIZE;
            height = REPAIR_HITBOX_SIZE;
        }
        else if (entity == BOMB) {
            levelData.add("bomb;" + Integer.toString(x) + ";" 
                + Integer.toString(y)+ ";" + Integer.toString(direction) + ";0");
            width = BOMB_HITBOX_SIZE;
            height = BOMB_HITBOX_SIZE;
        }
        else if (entity == TARGET) {
            levelData.add("target;" + Integer.toString(x) + ";" 
                + Integer.toString(y) + ";" + Integer.toString(direction) + ";0");
            width = TARGET_HITBOX_SIZE;
            height = TARGET_HITBOX_SIZE;
        }
        else if (entity == DRONE) {
            levelData.add("drone;" + Integer.toString(x) + ";" + Integer.toString(y) 
                + ";" + Integer.toString(direction) + ";" + Integer.toString(shootTimer));
            width = DRONE_HITBOX_W;
            height = DRONE_HITBOX_H;
        }
        else if (entity == SMALLSHIP) {   
            levelData.add("smallShip;" + Integer.toString(x) + ";"
                 + Integer.toString(y) + ";" + Integer.toString(direction) + ";0");
            width = SMALLSHIP_HITBOX_W;   
            height = SMALLSHIP_HITBOX_H;
        }
        else if (entity == OCTADRONE) {   
            levelData.add("octaDrone;" + Integer.toString(x) + ";" + Integer.toString(y) + ";" 
                + Integer.toString(direction) + ";" + Integer.toString(shootTimer));
            width = OCTADRONE_HITBOX_SIZE;  
            height = OCTADRONE_HITBOX_SIZE;
        }
        else if (entity == TANKDRONE) {
            levelData.add("tankDrone;" + Integer.toString(x) + ";"  
                 + Integer.toString(y)+ ";" + Integer.toString(direction) + ";0");
            width = TANKDRONE_HITBOX_W;
            height = TANKDRONE_HITBOX_H;
        }
        else if (entity == BLASTERDRONE) {
            levelData.add("blasterDrone;" + Integer.toString(x) + ";"
                 + Integer.toString(y) + ";" + Integer.toString(direction) + ";30");
            width = BLASTERDRONE_HITBOX_W;   
            height = BLASTERDRONE_HITBOX_H;
        }
        else if (entity == REAPERDRONE) {
            levelData.add("reaperDrone;" + Integer.toString(x) + ";"
                 + Integer.toString(y) + ";1;"  + Integer.toString(shootTimer));
            width = REAPERDRONE_HITBOX_W;   
            height = REAPERDRONE_HITBOX_H;
        }
        else if (entity == FLAMEDRONE) {
            levelData.add("flameDrone;" + Integer.toString(x) + ";"
                 + Integer.toString(y) + ";1;120");
            width = FLAMEDRONE_HITBOX_SIZE;   
            height = FLAMEDRONE_HITBOX_SIZE;
        }
        else if (entity == WASPDRONE) {
            levelData.add("waspDrone;" + Integer.toString(x) + ";" + Integer.toString(y) 
                + ";" + Integer.toString(direction) + ";" + Integer.toString(shootTimer));
            width = WASPDRONE_HITBOX_SIZE;   
            height = WASPDRONE_HITBOX_SIZE;
        }
        else if (entity == KAMIKAZEDRONE) {
            levelData.add("kamikazeDrone;" + Integer.toString(x) + ";" + Integer.toString(y) + ";1;0");
            width = KAMIKAZEDRONE_HITBOX_SIZE;   
            height = KAMIKAZEDRONE_HITBOX_SIZE;
        }

        entityTypes.add(entity);
        entityDirections.add(direction);
        if ((entity == DRONE) || (entity == OCTADRONE) || 
            (entity == WASPDRONE) || (entity == REAPERDRONE)) {
            shootTimers.add(shootTimer);
        } else {
            shootTimers.add(0);
        }
        int[] cor = {x, y};
        entityCor.add(cor);
        Rectangle hitbox = new Rectangle(cor[0], cor[1], width, height);
        hitboxes.add(hitbox);
    }

    @Override
    public void draw(Graphics g) {
        drawMaps(g);
    }

    private void drawMaps(Graphics g) {
        g.drawImage(
            clImg, 
            (int) (-clXOffset * Game.SCALE), 
            (int) (clYOffset * Game.SCALE), 
            (int) (clImgWidth * Game.SCALE), 
            (int) (clImgHeight * Game.SCALE), null);
        
        // Top text
        g.setColor(Color.BLACK);
        g.setFont(font);
        g.drawString("direction : " + Integer.toString(curDirection), 20, 20);
        g.drawString("shootTimer : " + Integer.toString(shootTimer), 20, 50);
        g.drawString("y :" + Integer.toString(mapYOffset), 700, 20);
        
        // Outplaced images
        for (int i = 0; i < entityTypes.size(); i++) {
            int entityType = entityTypes.get(i);
            // Text
            g.drawString(
                Integer.toString(shootTimers.get(i)), 
                (int)(hitboxes.get(i).x * Game.SCALE),
                (int)((hitboxes.get(i).y - mapYOffset - 20) * Game.SCALE));
            
            // Hitbox
            g.fillRect(
                (int)(hitboxes.get(i).x * Game.SCALE),
                (int)((hitboxes.get(i).y - mapYOffset) * Game.SCALE),
                (int)(hitboxes.get(i).width * Game.SCALE),
                (int)(hitboxes.get(i).height * Game.SCALE)
            );
            // Image
            int dir = entityDirections.get(i);
            int x = entityCor.get(i)[0];
            int y = entityCor.get(i)[1];
            int xOffset = entityDrawOffsets[entityType][0];
            int yOffset = entityDrawOffsets[entityType][1];
            int spriteW = entitySpriteSizes[entityType][0];
            int spriteH = entitySpriteSizes[entityType][1];
            if (dir == -1) {
                xOffset -= 3 * spriteW;
            }
            g.drawImage(
                entityImgs[entityType],
                (int) ((x - xOffset) * Game.SCALE),
                (int) ((y - yOffset - mapYOffset) * Game.SCALE),
                (int) (spriteW * 3 * dir * Game.SCALE),
                (int) (spriteH * 3 * Game.SCALE), null);   
        }
        // Cursor
        g.drawImage(
            entityImgs[selectedEntity], 
            (int) (cursorX * Game.SCALE), 
            (int) (cursorY * Game.SCALE), 
            (int) (entitySpriteSizes[selectedEntity][0] * 3 * Game.SCALE), 
            (int) (entitySpriteSizes[selectedEntity][1] * 3 * Game.SCALE), null);
    }
}
