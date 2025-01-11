package gamestates;

import static entities.flying.EntityFactory.TypeConstants.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import entities.flying.EntityFactory;
import entities.flying.EntityInfo;
import main_classes.Game;
import utils.ResourceLoader;

/**
 * Draws all entities on screen, with their hitbox represented in black.
 * Entity names, -shootTimers, -directions and -hitboxes are held in separate
 * lists.
 * Thus a specific entity is only identifies by it's common index in the
 * different lists.
 * 
 * Set the level variable to the desired level.
 * When the levelEditor is started, it loads levelData from the specified level.
 * 
 * See comments in keyPressed-method for controls.
 * When you're satisfied with the placement of enemies, you can print the data
 * to the console, and copy-paste it into the levelData-file (overwriting the
 * entity entries). See the 'handleKeyboardInputs'-method for controls.
 */
public class LevelEditor implements Statemethods {
   private Game game;
   private Integer level = 4;
   private BufferedImage clImg;
   private ArrayList<String> levelData;
   private EntityFactory entityFactory;
   private ArrayList<String> addedEntityNames;
   private ArrayList<Rectangle> hitboxes;
   private ArrayList<Integer> shootTimers;
   private ArrayList<Integer> directions;

   private static final int UP = 1;
   private static final int DOWN = -1;
   private int clImgHeight;
   private int clImgWidth;
   private int clYOffset;
   private float clXOffset;
   private int mapYOffset = 0;
   private int selectedEntity;
   private String selectedName;

   private int curDirection = 1; // 1 = right, -1 = left
   private int shootTimer = 100; // Drones : 100 - 160, Octadrones : 60 - 180
   private Font font = ResourceLoader.getInfoFont();
   private int cursorX = 500;
   private int cursorY = 400;
   private int cursorSpeed = 10;

   public LevelEditor(Game game, EntityFactory entityFactory) {
      this.game = game;
      loadMapImages(level);
      this.entityFactory = entityFactory;
      this.hitboxes = new ArrayList<>();
      this.shootTimers = new ArrayList<>();
      this.directions = new ArrayList<>();
      this.levelData = new ArrayList<>();
      this.addedEntityNames = new ArrayList<>();
      this.selectedEntity = 0;
      this.selectedName = entityFactory.getName(selectedEntity);
      loadLevelData(level);
   }

   private void loadMapImages(int lvl) {
      this.clImg = ResourceLoader.getFlyImageCollision("level" + Integer.toString(lvl) + "_cl.png");
      this.clImgHeight = clImg.getHeight() * 3;
      this.clImgWidth = clImg.getWidth() * 3;
      this.clYOffset = Game.GAME_DEFAULT_HEIGHT - clImgHeight + 150;
      this.clXOffset = 150;
   }

   private void loadLevelData(Integer level) {
      List<String> levelData = ResourceLoader.getFlyLevelData(level);
      for (String line : levelData) {
         String[] lineData = line.split(";");
         String entryName = lineData[0];
         if (entityFactory.isEnemyRegistered(entryName) ||
               entityFactory.isPickupItemRegistered(entryName)) {
            // Entity successfully identified.
            int xCor = Integer.parseInt(lineData[1]);
            int yCor = Integer.parseInt(lineData[2]);
            int dir = Integer.parseInt(lineData[3]);
            int shootTimer = Integer.parseInt(lineData[4]);
            addEntityToList(false, entryName, xCor, yCor, dir, shootTimer);
         }
      }
   }

   @Override
   public void update() {
   }

   /**
    * Controls:
    * ---------------------------------------------
    * ENTER Return to Main Menu
    * w / s Change screen
    * x Change entity
    * UP / DOWN / LEFT / RIGHT Change cursor position
    * f Change entity direction
    * a / d Change shootTimer
    * SPACE Add / delete entity
    * p Print levelData in console
    */
   public void handleKeyboardInputs(KeyEvent e) {
      if (game.upIsPressed) {
         game.upIsPressed = false;
         this.changeScreen(UP);
      } else if (game.downIsPressed) {
         game.downIsPressed = false;
         this.changeScreen(DOWN);
      } else if (game.leftIsPressed) {
         game.leftIsPressed = false;
         this.shootTimer -= 10;
      } else if (game.rightIsPressed) {
         game.rightIsPressed = false;
         this.shootTimer += 10;
      } else if (game.interactIsPressed) {
         game.interactIsPressed = false;
         int adjustedY = cursorY + mapYOffset;
         String name = entityFactory.getName(selectedEntity);
         addEntityToList(true, name, cursorX, adjustedY, curDirection, this.shootTimer);
      } else if (game.pauseIsPressed) {
         game.resetMainMenu();
         Gamestate.state = Gamestate.MAIN_MENU;
      }
      // Here we use som KeyEvents, but if it leads to trouble we can add booleans in
      // game-object.
      else if (e.getKeyCode() == KeyEvent.VK_UP) {
         this.cursorY -= cursorSpeed;
      } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
         this.cursorY += cursorSpeed;
      } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
         this.cursorX -= cursorSpeed;
      } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
         this.cursorX += cursorSpeed;
      } else if (e.getKeyCode() == KeyEvent.VK_X) {
         selectedEntity = (selectedEntity + 1) % entityFactory.getAmountOfEntities();
         selectedName = entityFactory.getName(selectedEntity);
      } else if (e.getKeyCode() == KeyEvent.VK_F) {
         game.bombIsPressed = false;
         curDirection *= -1;
      } else if (e.getKeyCode() == KeyEvent.VK_P) {
         printLevelData();
      }
   }

   private void printLevelData() {
      for (String line : levelData) {
         System.out.println(line);
      }
   }

   private void changeScreen(int upDown) { // up = 1, down = -1
      this.clYOffset += Game.GAME_DEFAULT_HEIGHT * upDown;
      this.mapYOffset -= Game.GAME_DEFAULT_HEIGHT * upDown;
   }

   /**
    * Is used to
    * 1) load the data from levelData-file
    * 2) make new levelData from levelEditor
    * the 'fromEditor'-boolean represents whether this method is called from the
    * levelEditor.
    */
   private void addEntityToList(boolean calledFromEditor, String name, int x, int y, int direction, int shootTimer) {
      EntityInfo info = entityFactory.getEntityInfo(name);
      if (calledFromEditor) {
         // Adjusting x and y to represent hitbox.x- and -y
         x += info.drawOffsetX;
         y += info.drawOffsetY;
      }
      Rectangle hitbox = new Rectangle(x, y, info.hitboxW, info.hitboxH);
      int typeConstant = info.typeConstant;

      // Handle delete, and modify + add entries.
      switch (typeConstant) {
         case DELETE:
            deleteOverlappingEntity(hitbox);
            return; // Abort the method.
         case BLASTERDRONE:
            // Blasterdrones have a standard shootTimer of 30
            this.addModifiedEntry(info.name, x, y, direction, 30, hitbox);
            break;
         case FLAMEDRONE:
            // Flamedrones have a standard shootTimer of 120
            this.addModifiedEntry(info.name, x, y, direction, 120, hitbox);
            break;
         case POWERUP, BOMB, REPAIR, TARGET, SMALLSHIP, TANKDRONE, KAMIKAZEDRONE:
            // Entities without shootTimer will have standard value of 0.
            this.addModifiedEntry(info.name, x, y, direction, 0, hitbox);
            break;
         default:
            // All other enemies:
            this.addModifiedEntry(name, x, y, direction, shootTimer, hitbox);
            break;
      }
      addedEntityNames.add(name);
   }

   private void deleteOverlappingEntity(Rectangle deleteHitbox) {
      int indexToRemove = -1;
      for (int i = 0; i < hitboxes.size(); i++) {
         if (deleteHitbox.intersects(hitboxes.get(i))) {
            indexToRemove = i;
            break;
         }
      }
      if (indexToRemove > -1) {
         removeEntry(indexToRemove);
      }
   }

   private void removeEntry(int indexToRemove) {
      addedEntityNames.remove(indexToRemove);
      levelData.remove(indexToRemove);
      hitboxes.remove(indexToRemove);
      directions.remove(indexToRemove);
      shootTimers.remove(indexToRemove);
   }

   private void addModifiedEntry(String name, int x, int y, int direction, int shootTimer, Rectangle hitbox) {
      // LevelData-string:
      levelData.add(name + ";" + Integer.toString(x) + ";" + Integer.toString(y) +
            ";" + Integer.toString(direction) + ";" + Integer.toString(shootTimer));

      // Other relevant info
      hitboxes.add(hitbox);
      shootTimers.add(shootTimer);
      directions.add(direction);
   }

   @Override
   public void draw(Graphics g) {
      drawMapAndText(g);
      drawEntities(g);
      drawCursor(g);
   }

   private void drawMapAndText(Graphics g) {
      // Map
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
   }

   private void drawEntities(Graphics g) {
      for (int i = 0; i < addedEntityNames.size(); i++) {
         EntityInfo info = entityFactory.getEntityInfo(addedEntityNames.get(i));
         Rectangle2D hitbox = hitboxes.get(i);
         // Text
         g.drawString(
               Integer.toString(shootTimers.get(i)),
               (int) (hitbox.getX() * Game.SCALE),
               (int) ((hitbox.getY() - mapYOffset - 20) * Game.SCALE));

         // Hitbox
         g.fillRect(
               (int) (hitbox.getX() * Game.SCALE),
               (int) ((hitbox.getY() - mapYOffset) * Game.SCALE),
               (int) (hitbox.getWidth() * Game.SCALE),
               (int) (hitbox.getHeight() * Game.SCALE));
         // Image
         int dir = directions.get(i);
         int xOffset = info.drawOffsetX;
         int yOffset = info.drawOffsetY;
         int spriteW = info.spriteW;
         int spriteH = info.spriteH;
         if (dir == -1) {
            xOffset -= 3 * spriteW;
         }
         g.drawImage(
               info.getEditorImage(),
               (int) ((hitbox.getX() - xOffset) * Game.SCALE),
               (int) ((hitbox.getY() - yOffset - mapYOffset) * Game.SCALE),
               (int) (spriteW * 3 * dir * Game.SCALE),
               (int) (spriteH * 3 * Game.SCALE), null);
      }
   }

   private void drawCursor(Graphics g) {
      EntityInfo cursorInfo = entityFactory.getEntityInfo(selectedName);
      g.drawImage(
            cursorInfo.getEditorImage(),
            (int) (cursorX * Game.SCALE),
            (int) (cursorY * Game.SCALE),
            (int) (cursorInfo.spriteW * 3 * Game.SCALE),
            (int) (cursorInfo.spriteH * 3 * Game.SCALE), null);
   }
}