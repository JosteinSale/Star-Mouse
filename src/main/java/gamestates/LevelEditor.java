package gamestates;

import static entities.flying.EntityFactory.TypeConstants.*;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Input;

import entities.flying.EntityFactory;
import entities.flying.EntityInfo;
import gamestates.flying.FlyLevelInfo;
import main_classes.Game;
import utils.ResourceLoader;

/**
 * Draws all entities on screen, with their hitbox represented in black.
 * Entity names, -shootTimers, -directions and -hitboxes are held in separate
 * lists. Thus a specific entity is only identifies by it's common index in the
 * different lists.
 * 
 * Set the level variable to the desired level.
 * When the levelEditor is started, it loads levelData for the specified level.
 * 
 * This gamestate has its own controls (see comments in keyPressed-method).
 * When you're satisfied with the placement of enemies, you can print the data
 * to the console, and copy-paste it into the levelData-file (overwriting the
 * entity entries).
 */
public class LevelEditor extends State {
   public Integer level = 0;
   public EntityFactory entityFactory;
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
      super(game);
      this.entityFactory = entityFactory;
      hitboxes = new ArrayList<>();
      shootTimers = new ArrayList<>();
      directions = new ArrayList<>();
      levelData = new ArrayList<>();
      addedEntityNames = new ArrayList<>();
      selectedEntity = 0;
      selectedName = entityFactory.getName(selectedEntity);
      loadLevelData(level);
      calcMapValues(level);
   }

   private void calcMapValues(int lvl) {
      clImgHeight = FlyLevelInfo.getClImgHeight(lvl) * 3;
      clImgWidth = 450 * 3;
      clYOffset = Game.GAME_DEFAULT_HEIGHT - clImgHeight + 150;
      clXOffset = -150;
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
            registerEntityInstance(false, entryName, xCor, yCor, dir, shootTimer);
         }
      }
   }

   /**
    * Controls:
    * ---------------------------------------------
    * ENTER - Return to Main Menu
    * W / S - Change screen
    * Z / C - Change entity
    * UP / DOWN / LEFT / RIGHT - Change cursor position
    * F - Change entity direction
    * A / D - Change shootTimer
    * SPACE - Add / delete entity
    * P - Print levelData in console
    */
   public void handleKeyboardInputs(int key) {
      switch (key) {
         case Input.Keys.W -> changeScreen(UP);
         case Input.Keys.S -> changeScreen(DOWN);
         case Input.Keys.A -> shootTimer -= 10;
         case Input.Keys.D -> shootTimer += 10;
         case Input.Keys.SPACE -> addEntity();
         case Input.Keys.ENTER -> goToMainMenu();
         case Input.Keys.UP -> cursorY -= cursorSpeed;
         case Input.Keys.DOWN -> cursorY += cursorSpeed;
         case Input.Keys.LEFT -> cursorX -= cursorSpeed;
         case Input.Keys.RIGHT -> cursorX += cursorSpeed;
         case Input.Keys.C -> toggleSelectedEntity("RIGHT");
         case Input.Keys.Z -> toggleSelectedEntity("LEFT");
         case Input.Keys.F -> curDirection *= -1;
         case Input.Keys.P -> printLevelData();
         default -> { // Do nothing
         }
      }
   }

   public void mouseMoved(int screenX, int screenY) {
      // We'll snap the cursor to a grid of 10 px, to keep placement consistent
      cursorX = (screenX / cursorSpeed) * cursorSpeed;
      cursorY = (screenY / cursorSpeed) * cursorSpeed;
   }

   private void goToMainMenu() {
      game.resetMainMenu();
      Gamestate.state = Gamestate.MAIN_MENU;
   }

   private void addEntity() {
      int adjustedY = cursorY + mapYOffset;
      String name = entityFactory.getName(selectedEntity);
      registerEntityInstance(true, name, cursorX, adjustedY, curDirection, shootTimer);
   }

   private void toggleSelectedEntity(String direction) {
      int amountOfEntities = entityFactory.getAmountOfEntities();
      if (direction.equals("RIGHT")) {
         selectedEntity = (selectedEntity + 1) % amountOfEntities;
      } else if (direction.equals("LEFT")) {
         selectedEntity = (selectedEntity - 1 + amountOfEntities)
               % amountOfEntities;
      }
      selectedName = entityFactory.getName(selectedEntity);
   }

   private void printLevelData() {
      for (String line : levelData) {
         System.out.println(line);
      }
   }

   private void changeScreen(int upDown) { // up = 1, down = -1
      clYOffset += Game.GAME_DEFAULT_HEIGHT * upDown;
      mapYOffset -= Game.GAME_DEFAULT_HEIGHT * upDown;
   }

   /**
    * Is used to
    * 1) load the data from levelData-file
    * 2) make new levelData from levelEditor
    * the 'fromEditor'-boolean represents whether this method is called from the
    * levelEditor.
    */
   private void registerEntityInstance(boolean calledFromEditor, String name, int x, int y, int direction,
         int shootTimer) {
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
            addModifiedEntry(info.name, x, y, direction, 30, hitbox);
            break;
         case FLAMEDRONE:
            // Flamedrones have a standard shootTimer of 120
            addModifiedEntry(info.name, x, y, direction, 120, hitbox);
            break;
         case POWERUP, BOMB, REPAIR, TARGET, SMALLSHIP, TANKDRONE, KAMIKAZEDRONE:
            // Entities without shootTimer will have standard value of 0.
            addModifiedEntry(info.name, x, y, direction, 0, hitbox);
            break;
         default:
            // All other enemies:
            addModifiedEntry(name, x, y, direction, shootTimer, hitbox);
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