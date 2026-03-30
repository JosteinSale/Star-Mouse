package game_states;

import static entities.flying.EnemyFactory.TypeConstants.*;
import static entities.flying.pickupItems.PickupItemFactory.TypeConstants.*;
import static utils.Constants.Flying.COLLISION_MAP_X_OFFSET;
import static utils.Constants.Flying.COLLISION_MAP_Y_OFFSET;
import static utils.Constants.Flying.COLLISION_MAP_WIDTH;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import entities.flying.EnemyFactory;
import entities.flying.EntityInfo;
import entities.flying.pickupItems.PickupItemFactory;
import game_states.flying.FlyLevelInfo;
import main_classes.Game;
import utils.ResourceLoader;

/**
 * Draws all entities on screen, with their hitbox represented in black.
 * Entity names, -shootTimers, -flipXs and -hitboxes are held in separate
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
   public Integer level;
   public EnemyFactory enemyFactory;
   public PickupItemFactory pickupFactory;
   private ArrayList<String> levelData;
   public ArrayList<Integer> addedEntities;
   public ArrayList<Rectangle> hitboxes;
   public ArrayList<Integer> shootTimers;
   public ArrayList<Integer> flipXs;
   private HashMap<String, Integer> entityNameToTypeMap;

   // Cursor
   public int cursorX = 500;
   public int cursorY = 400;
   private int gridSize = 10;
   private static final int UP = 1;
   private static final int DOWN = 2;
   private static final int RIGHT = 3;
   private static final int LEFT = 4;

   // Enemy stuff
   public int selectedEntity;
   public int enemyFlipX = 1; // 1 for right, -1 for left.
   public int shootTimer = 100;

   // Map
   public int clImgHeight;
   public int clImgWidth;
   public int clImgY;
   public int clImgX;
   public int editorXOffset = 0;
   private int screenNr = 0; // Increases or decreases by 1 every time we change screen vertically.

   public LevelEditor(Game game) {
      super(game);
      this.enemyFactory = new EnemyFactory(null);
      this.pickupFactory = new PickupItemFactory();
      constructEntityNameToTypeMap();
   }

   private void constructEntityNameToTypeMap() {
      this.entityNameToTypeMap = new HashMap<>();
      this.entityNameToTypeMap.putAll(enemyFactory.getNameToTypeMap());
      this.entityNameToTypeMap.putAll(pickupFactory.getNameToTypeMap());
   }

   public void loadLevel(int level) {
      this.level = level;
      hitboxes = new ArrayList<>();
      shootTimers = new ArrayList<>();
      flipXs = new ArrayList<>();
      levelData = new ArrayList<>();
      addedEntities = new ArrayList<>();
      selectedEntity = 0;
      screenNr = 0;
      loadLevelData(level);
      calcMapValues(level);
      game.getView().getRenderLevelEditor().loadLevel(level);
   }

   private void calcMapValues(int lvl) {
      clImgHeight = FlyLevelInfo.getClImgHeight(lvl) * 3;
      clImgWidth = COLLISION_MAP_WIDTH;
      clImgY = setClImgY();
      clImgX = -COLLISION_MAP_X_OFFSET;
   }

   public int getEditorY() {
      return screenNr * Game.GAME_DEFAULT_HEIGHT;
   }

   private int setClImgY() {
      return -clImgHeight + COLLISION_MAP_Y_OFFSET + getEditorY() + Game.GAME_DEFAULT_HEIGHT;
   }

   private void loadLevelData(Integer level) {
      List<String> levelData = ResourceLoader.getFlyLevelData(level);
      for (String line : levelData) {
         String[] lineData = line.split(";");
         String entryName = lineData[0];
         if (enemyFactory.isEnemyRegistered(entryName) ||
               pickupFactory.isPickupItemRegistered(entryName)) {
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
    * MOUSE MOVE - Change cursor position
    * WASD - Move screen
    * Z / C - Change entity
    * UP / DOWN - Change shootTimer
    * F - Change entity direction
    * SPACE - Add / delete entity
    * P - Print levelData in console
    */
   public void handleKeyboardInputs(int key) {
      switch (key) {
         case Input.Keys.W -> moveScreen(UP);
         case Input.Keys.S -> moveScreen(DOWN);
         case Input.Keys.A -> moveScreen(LEFT);
         case Input.Keys.D -> moveScreen(RIGHT);
         case Input.Keys.SPACE -> addEntity();
         case Input.Keys.ENTER -> goToMainMenu();
         case Input.Keys.UP -> shootTimer += 10;
         case Input.Keys.DOWN -> shootTimer -= 10;
         case Input.Keys.C -> toggleSelectedEntity(RIGHT);
         case Input.Keys.Z -> toggleSelectedEntity(LEFT);
         case Input.Keys.F -> enemyFlipX *= -1;
         case Input.Keys.P -> printLevelData();
         default -> { // Do nothing
         }
      }
   }

   public void mouseMoved(int mouseX, int mouseY) {
      // Adjust input x and y to correspond to the currentgame scale
      float scale = Game.GAME_DEFAULT_HEIGHT / (float) Gdx.graphics.getHeight();
      int x = (int) (mouseX * scale);
      int y = (int) (mouseY * scale);

      // We'll snap the cursor to a grid with size of 10,
      // to keep placement consistent
      int xAdjust = scale < 1 ? 150 : 0;
      cursorX = (x / gridSize) * gridSize - xAdjust;
      cursorY = (y / gridSize) * gridSize;
   }

   private void goToMainMenu() {
      game.returnToMainMenu(() -> game.flushImages());
   }

   private void addEntity() {
      int adjustedY = cursorY - getEditorY();
      int adjustedX = cursorX - editorXOffset;
      // Maybe a bit over-engineered, but it works:
      String name = entityNameToTypeMap.entrySet().stream()
            .filter(entry -> entry.getValue() == selectedEntity)
            .map(entry -> entry.getKey())
            .findFirst()
            .orElse(null);
      registerEntityInstance(true, name, adjustedX, adjustedY, enemyFlipX, shootTimer);
   }

   private void toggleSelectedEntity(int direction) {
      int amountOfEntities = entityNameToTypeMap.size();
      if (direction == RIGHT) {
         selectedEntity = (selectedEntity + 1) % amountOfEntities;
      } else if (direction == LEFT) {
         selectedEntity = (selectedEntity - 1 + amountOfEntities)
               % amountOfEntities;
      }
   }

   private void printLevelData() {
      for (String line : levelData) {
         System.out.println(line);
      }
   }

   private void moveScreen(int direction) {
      switch (direction) {
         case UP -> {
            screenNr += 1;
            clImgY = setClImgY();
         }
         case DOWN -> {
            screenNr -= 1;
            clImgY = setClImgY();
         }
         case RIGHT -> {
            clImgX -= 200;
            editorXOffset -= 200;
         }
         case LEFT -> {
            clImgX += 200;
            editorXOffset += 200;
         }
      }

   }

   /**
    * Is used to
    * 1) load the data from levelData-file
    * 2) make new levelData from levelEditor
    * the 'fromEditor'-boolean represents whether this method is called from the
    * levelEditor.
    */
   private void registerEntityInstance(
         boolean calledFromEditor, String name, int x, int y,
         int direction, int shootTimer) {
      int typeConstant = entityNameToTypeMap.get(name);
      EntityInfo info = getEntityInfo(typeConstant);
      if (calledFromEditor) {
         // Adjusting x and y to represent hitbox.x- and -y
         x += info.drawOffsetX;
         y += info.drawOffsetY;
      }
      Rectangle hitbox = new Rectangle(x, y, info.hitboxW, info.hitboxH);

      // Handle delete, and modify + add entries.
      switch (typeConstant) {
         case DELETE:
            deleteOverlappingEntity(hitbox);
            return; // Abort the method.
         case BLASTERDRONE:
            // Blasterdrones have a standard shootTimer of 30
            addModifiedEntry(name, x, y, direction, 30, hitbox);
            break;
         case FLAMEDRONE:
            // Flamedrones have a standard shootTimer of 120
            addModifiedEntry(name, x, y, direction, 120, hitbox);
            break;
         case POWERUP, BOMB, REPAIR, TARGET, SMALLSHIP, TANKDRONE, KAMIKAZEDRONE:
            // Entities without shootTimer will have standard value of 0.
            addModifiedEntry(name, x, y, direction, 0, hitbox);
            break;
         default:
            // All other enemies:
            addModifiedEntry(name, x, y, direction, shootTimer, hitbox);
            break;
      }
      addedEntities.add(typeConstant);
   }

   // Is public to be accessible from RenderLevelEditor
   public EntityInfo getEntityInfo(int typeConstant) {
      if (enemyFactory.enemyInfo.containsKey(typeConstant)) {
         return enemyFactory.enemyInfo.get(typeConstant);
      } else {
         return pickupFactory.pickupInfo.get(typeConstant);
      }
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
      addedEntities.remove(indexToRemove);
      levelData.remove(indexToRemove);
      hitboxes.remove(indexToRemove);
      flipXs.remove(indexToRemove);
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
      flipXs.add(direction);
   }

}