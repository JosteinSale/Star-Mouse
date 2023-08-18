package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import main.Game;
import utils.LoadSave;
import static utils.Constants.Flying.Sprites.ALL_SPRITES_SIZE;

public class LevelEditor implements Statemethods {
    private Game game;
    private Integer level = 0;
    private BufferedImage clImg;
    private BufferedImage[] entityImgs;                    // Standardiserte bilder
    private ArrayList<BufferedImage> outPlacedImgs;
    private ArrayList<Rectangle> hitboxes;
    private ArrayList<int[]> entityCor;
    private ArrayList<String> levelData;
    private static final int UP = 1;
    private static final int DOWN = -1;
    private int clImgHeight;
    private int clImgWidth;
    private float clYOffset;
    private float clXOffset;
    private int entityYOffset = 0;
    private int selectedIndex = 0;
    private String curDirection = "right";

    private int cursorX = 500;
    private int cursorY = 400;
    private int cursorSpeed = 10;

    public LevelEditor(Game game) {
        this.game = game;
        loadMapImages();
        loadEntityImages();
        this.outPlacedImgs = new ArrayList<>();
        this.entityCor = new ArrayList<>();
        this.hitboxes = new ArrayList<>();
        this.levelData = new ArrayList<>();
        loadLevelData(level);
    }

    private void loadMapImages() {
        this.clImg = LoadSave.getFlyImageCollision("BorderTest2.png");
        this.clImgHeight = clImg.getHeight() * 3;
        this.clImgWidth = clImg.getWidth() * 3;
        this.clYOffset = Game.GAME_DEFAULT_HEIGHT - clImgHeight + 150;
        this.clXOffset = 150;
    }

    private void loadEntityImages() {
        this.entityImgs = new BufferedImage[8];
        BufferedImage img = LoadSave.getFlyImageSprite(LoadSave.ALL_ENTITY_SPRITES);
        for (int i = 0; i < entityImgs.length; i++) {
            entityImgs[i] = img.getSubimage(
                i * ALL_SPRITES_SIZE, 0, ALL_SPRITES_SIZE, ALL_SPRITES_SIZE);
        }
    }

    private void loadLevelData(Integer level) {
        List<String> levelData = LoadSave.getFlyLevelData(level);
        for (String line : levelData) {
            String[] lineData = line.split(";");
                int entity = switch(lineData[0]) {
                    case "powerup" -> 0;
                    case "repair" -> 1;
                    case "bomb" -> 2;
                    case "target" -> 3;
                    default -> 99;
                };
                if (entity < 99) {
                    addEntityToList(Integer.parseInt(lineData[1]), Integer.parseInt(lineData[2]), entity);
                }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            this.changeScreen(UP);
        }
        else if (e.getKeyCode() == KeyEvent.VK_S) {
            this.changeScreen(DOWN);
        }
        else if (e.getKeyCode() == KeyEvent.VK_X) {
            this.selectedIndex = (selectedIndex + 1) % entityImgs.length;
        }
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
        else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            int adjustedY = cursorY + entityYOffset;
            addEntityToList(cursorX, adjustedY, selectedIndex);
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

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void changeScreen(int upDown) {   // up = 1, down = -1
        this.clYOffset += Game.GAME_DEFAULT_HEIGHT * upDown;
        this.entityYOffset -= Game.GAME_DEFAULT_HEIGHT * upDown;
    }

    private void addEntityToList(int x, int y, int entity) {
        int width = 150;    // Hitbox-width
        int height = 150;   // Hitbox-height
        int xOffset = 0;
        int yOffset = 0;
        if (entity == 0) {
            levelData.add("powerup;" + Integer.toString(cursorX) + ";" + Integer.toString(y));
            width = 30;
            height = 50;
            xOffset = 30;
            yOffset = 20;
        }
        else if (entity == 1) {
            levelData.add("repair;" + Integer.toString(cursorX) + ";" + Integer.toString(y));
            width = 60;
            height = 60;
            xOffset = 15;
            yOffset = 15;
        }
        else if (entity == 2) {
            levelData.add("bomb;" + Integer.toString(cursorX) + ";" + Integer.toString(y));
            width = 45;
            height = 45;
            xOffset = 15;
            yOffset = 18;
        }
        else if (entity == 3) {
            levelData.add("target;" + Integer.toString(cursorX) + ";" + Integer.toString(y));
            width = 60;
            height = 60;
            xOffset = 0;
            yOffset = 0;
        }
        else if (entity == 7) {       // delete
            Rectangle hitbox = new Rectangle(cursorX, y, 90, 90);
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
            }
        }
        if (entity < 7) {        
            int[] cor = {x, y};
            entityCor.add(cor);
            outPlacedImgs.add(entityImgs[entity]);
            Rectangle hitbox = new Rectangle(cor[0] + xOffset, cor[1] + yOffset, width, height);
            hitboxes.add(hitbox);
        }
    }

    @Override
    public void update() {
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
        
        // Outplaced images
        for (int i = 0; i < outPlacedImgs.size(); i++) {
            g.setColor(Color.BLACK);
            g.fillRect(
                (int)(hitboxes.get(i).x * Game.SCALE),
                (int)((hitboxes.get(i).y - entityYOffset) * Game.SCALE),
                (int)(hitboxes.get(i).width * Game.SCALE),
                (int)(hitboxes.get(i).height * Game.SCALE)
            );
            g.drawImage(
            outPlacedImgs.get(i), 
            (int) (entityCor.get(i)[0] * Game.SCALE), 
            (int) ((entityCor.get(i)[1] - entityYOffset) * Game.SCALE), 
            (int) (ALL_SPRITES_SIZE * 3 * Game.SCALE), 
            (int) (ALL_SPRITES_SIZE * 3 * Game.SCALE), null);
        }

        // Cursor
        g.drawImage(
            entityImgs[selectedIndex], 
            (int) (cursorX * Game.SCALE), 
            (int) (cursorY * Game.SCALE), 
            (int) (ALL_SPRITES_SIZE * 3 * Game.SCALE), 
            (int) (ALL_SPRITES_SIZE * 3 * Game.SCALE), null);
    }
}
