package gamestates;

import static entities.flying.EntityFactory.TypeConstants.*;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import entities.flying.EntityFactory;
import entities.flying.EntityInfo;
import gamestates.flying.FlyLevelInfo;
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
public class LevelEditor {
   private Game game;
   public Integer level = 4;
   public EntityFactory entityFactory;
   private FlyLevelInfo flyLevelInfo;
   private ArrayList<String> levelData;
   public ArrayList<String> addedEntityNames;
   public ArrayList<Rectangle> hitboxes;
   public ArrayList<Integer> shootTimers;
   public ArrayList<Integer> directions;

   private static final int UP = 1;
   private static final int DOWN = -1;
   public int clImgHeight;
   public int clImgWidth;
   public int clYOffset;
   public float clXOffset;
   public int mapYOffset = 0;
   private int selectedEntity;
   public String selectedName;

   public int curDirection = 1; // 1 = right, -1 = left
   public int shootTimer = 100; // Drones : 100 - 160, Octadrones : 60 - 180
   public int cursorX = 500;
   public int cursorY = 400;
   private int cursorSpeed = 10;

   public LevelEditor(
         Game game, EntityFactory entityFactory) {
      this.game = game;
      this.flyLevelInfo = new FlyLevelInfo();
      this.entityFactory = entityFactory;
      this.hitboxes = new ArrayList<>();
      this.shootTimers = new ArrayList<>();
      this.directions = new ArrayList<>();
      this.levelData = new ArrayList<>();
      this.addedEntityNames = new ArrayList<>();
      this.selectedEntity = 0;
      this.selectedName = entityFactory.getName(selectedEntity);
      loadLevelData(level);
      calcMapValues(level);
   }

   private void calcMapValues(int lvl) {
      this.clImgHeight = flyLevelInfo.getClImgHeight(lvl) * 3;
      this.clImgWidth = 450 * 3;
      this.clYOffset = Game.GAME_DEFAULT_HEIGHT - clImgHeight + 150;
      this.clXOffset = -150;
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

   private void addModifiedEntry(
         String name, int x, int y, int direction, int shootTimer, Rectangle hitbox) {
      // LevelData-string:
      levelData.add(name + ";" + Integer.toString(x) + ";" + Integer.toString(y) +
            ";" + Integer.toString(direction) + ";" + Integer.toString(shootTimer));

      // Other relevant info
      hitboxes.add(hitbox);
      shootTimers.add(shootTimer);
      directions.add(direction);
   }

}