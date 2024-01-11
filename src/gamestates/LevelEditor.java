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
import static utils.Constants.Flying.Sprites.ALL_SPRITES_SIZE;
import static utils.Constants.Flying.TypeConstants.SMALL_SHIP;


/**
 * Draws all entities on screen, with their hitbox represented in black.
 * Images, coordinates and hitboxes are held in separate lists. 
 * Thus a specific entity is only identifies by it's common index in the different lists.
 * 
 * When the levelEditor is started, it loads levelData from the specified level.
 * 
 * See comments in keyPressed-method for controls.
 * When you're satisfied with the placement of enemies, you can print the data to
 * the console, and copy-paste it into the levelData-file (overwriting the enemy entries).
 * 
 * NOTE: In this object, enemyNr's are independent from the ones defined in the Constant-class.
 * They're only determined by the index in the allSprites-image.
 */
public class LevelEditor implements Statemethods {
    private Game game;
    private Integer level = 1;
    private BufferedImage clImg;
    private BufferedImage[] entityImgs;                    // Standardiserte bilder
    private ArrayList<BufferedImage> outPlacedImgs;
    private ArrayList<Rectangle> hitboxes;
    private ArrayList<int[]> entityCor;
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
    private int entityYOffset = 0;
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
        this.outPlacedImgs = new ArrayList<>();
        this.entityCor = new ArrayList<>();
        this.hitboxes = new ArrayList<>();
        this.levelData = new ArrayList<>();
        this.entityDirections = new ArrayList<>();
        this.entityTypes = new ArrayList<>();   
        this.shootTimers = new ArrayList<>(); 
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
        this.entityImgs = new BufferedImage[10];
        BufferedImage img = LoadSave.getFlyImageSprite(LoadSave.ALL_ENTITY_SPRITES);
        for (int i = 0; i < entityImgs.length; i++) {
            entityImgs[i] = img.getSubimage(
                i * ALL_SPRITES_SIZE, 0, ALL_SPRITES_SIZE, ALL_SPRITES_SIZE);
        } 
        // We can have bigger sprites, maybe in a separate file. Add them to the same array indepentendly.
    }

    private void loadLevelData(Integer level) {
        List<String> levelData = LoadSave.getFlyLevelData(level);
        for (String line : levelData) {
            String[] lineData = line.split(";");
                int entity = switch(lineData[0]) {   // index in entityImgs-array
                    case "powerup" -> 1;
                    case "repair" -> 2;
                    case "bomb" -> 3;
                    case "target" -> 4;
                    case "drone" -> 5;
                    case "smallShip" -> 6;
                    case "octaDrone" -> 7;
                    case "tankDrone" -> 8;
                    case "blasterDrone" -> 9;
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
     * W / S                         Change screen 
     * X                             Change entity
     * UP / DOWN / LEFT / RIGHT      Change cursor position
     * F                             Change direction
     * A / D                         Change shootTimer
     * SPACE                         Add / delete entity
     * P                             Print levelData in console
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
            int adjustedY = cursorY + entityYOffset;
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
        this.entityYOffset -= Game.GAME_DEFAULT_HEIGHT * upDown;
    }

    /** Is used to 
     *      1) load the data from levelData-file
     *      2) make new levelData from levelEditor
    */
    private void addEntityToList(int entity, int x, int y, int direction, int shootTimer) {
        int width = 150;    // Hitbox-size if no entities match
        int height = 150;   
        int xOffset = 0;
        int yOffset = 0;

        if (entity == 0) {      // Delete
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
                outPlacedImgs.remove(indexToRemove);
                entityCor.remove(indexToRemove);
                levelData.remove(indexToRemove);
                entityTypes.remove(indexToRemove);
                entityDirections.remove(indexToRemove);
                shootTimers.remove(indexToRemove);
            }
            return;
        }
        else if (entity == 1) {
            levelData.add("powerup;" + Integer.toString(x) + ";" + 
                Integer.toString(y) + ";" + Integer.toString(direction) + ";0");
            width = 30;
            height = 50;
            xOffset = 28;
            yOffset = 20;
        }
        else if (entity == 2) {
            levelData.add("repair;" + Integer.toString(x) + ";" 
                + Integer.toString(y) + ";" + Integer.toString(direction) + ";0");
            width = 60;
            height = 60;
            xOffset = 15;
            yOffset = 15;
        }
        else if (entity == 3) {
            levelData.add("bomb;" + Integer.toString(x) + ";" 
                + Integer.toString(y)+ ";" + Integer.toString(direction) + ";0");
            width = 45;
            height = 45;
            xOffset = 15;
            yOffset = 18;
        }
        else if (entity == 4) {
            levelData.add("target;" + Integer.toString(x) + ";" 
                + Integer.toString(y) + ";" + Integer.toString(direction) + ";0");
            width = 60;
            height = 60;
            xOffset = 0;
            yOffset = 0;
        }
        else if (entity == 5) {
            levelData.add("drone;" + Integer.toString(x) + ";" + Integer.toString(y) 
                + ";" + Integer.toString(direction) + ";" + Integer.toString(shootTimer));
            width = 78;
            height = 66;
            xOffset = 4;
            yOffset = 10;
        }
        else if (entity == 6) {   
            levelData.add("smallShip;" + Integer.toString(x) + ";"
                 + Integer.toString(y) + ";" + Integer.toString(direction) + ";0");
            width = 60;   
            height = 30;
            xOffset = 16;
            yOffset = 30;
        }
        else if (entity == 7) {   
            levelData.add("octaDrone;" + Integer.toString(x) + ";" + Integer.toString(y) + ";" 
                + Integer.toString(direction) + ";" + Integer.toString(shootTimer));
            width = 80;  
            height = 80;
            xOffset = 5;
            yOffset = 5;
        }
        else if (entity == 8) {
            levelData.add("tankDrone;" + Integer.toString(x) + ";"  
                 + Integer.toString(y)+ ";" + Integer.toString(direction) + ";0");
            width = 80;
            height = 90;
            xOffset = 5;
            yOffset = 0;
        }
        else if (entity == 9) {
            levelData.add("blasterDrone;" + Integer.toString(x) + ";"
                 + Integer.toString(y) + ";" + Integer.toString(direction) + ";30");
            width = 60;   
            height = 90;
            xOffset = 15;
            yOffset = 0;
        }

        entityTypes.add(entity);
        entityDirections.add(direction);
        if ((entity == 5) || (entity == 7)) {
            shootTimers.add(shootTimer);
        } else {
            shootTimers.add(0);
        }
        int[] cor = {x, y};
        entityCor.add(cor);
        outPlacedImgs.add(entityImgs[entity]);
        Rectangle hitbox = new Rectangle(cor[0] + xOffset, cor[1] + yOffset, width, height);
        hitboxes.add(hitbox);
    }

    @Override
    public void draw(Graphics g) {
        drawMaps(g);
    }

    private void drawMaps(Graphics g) {
        g.drawImage(
            clImg, 
            (int) (-150 * Game.SCALE), 
            (int) (clYOffset * Game.SCALE), 
            (int) (clImgWidth * Game.SCALE), 
            (int) (clImgHeight * Game.SCALE), null);
        
        // Text
        g.setColor(Color.BLACK);
        g.setFont(font);
        g.drawString("direction : " + Integer.toString(curDirection), 20, 20);
        g.drawString("shootTimer : " + Integer.toString(shootTimer), 20, 50);
        g.drawString("y :" + Integer.toString(entityYOffset), 700, 20);
        
        // Outplaced images
        for (int i = 0; i < outPlacedImgs.size(); i++) {
            g.drawString(
                Integer.toString(shootTimers.get(i)), 
                (int)(hitboxes.get(i).x * Game.SCALE),
                (int)((hitboxes.get(i).y - entityYOffset - 20) * Game.SCALE));
            g.fillRect(
                (int)(hitboxes.get(i).x * Game.SCALE),
                (int)((hitboxes.get(i).y - entityYOffset) * Game.SCALE),
                (int)(hitboxes.get(i).width * Game.SCALE),
                (int)(hitboxes.get(i).height * Game.SCALE)
            );
            if (entityTypes.get(i) == 6) {    // smallShip
                int dir = entityDirections.get(i);
                int xOffset = 0;
                if (dir == -1) {
                    xOffset = 88;
                }
                g.drawImage(
                    outPlacedImgs.get(i),
                    (int) ((entityCor.get(i)[0] + xOffset) * Game.SCALE),
                    (int) ((entityCor.get(i)[1] - entityYOffset) * Game.SCALE),
                    (int) (ALL_SPRITES_SIZE * 3 * dir * Game.SCALE),
                    (int) (ALL_SPRITES_SIZE * 3 * Game.SCALE), null);
                }
            else {
                g.drawImage(
                    outPlacedImgs.get(i),
                    (int) (entityCor.get(i)[0] * Game.SCALE),
                    (int) ((entityCor.get(i)[1] - entityYOffset) * Game.SCALE),
                    (int) (ALL_SPRITES_SIZE * 3 * Game.SCALE),
                    (int) (ALL_SPRITES_SIZE * 3 * Game.SCALE), null);
                }
        }

        // Cursor
        g.drawImage(
            entityImgs[selectedEntity], 
            (int) (cursorX * Game.SCALE), 
            (int) (cursorY * Game.SCALE), 
            (int) (ALL_SPRITES_SIZE * 3 * Game.SCALE), 
            (int) (ALL_SPRITES_SIZE * 3 * Game.SCALE), null);
    }
}
