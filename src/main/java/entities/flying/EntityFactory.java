package entities.flying;

import java.util.HashMap;

import java.awt.geom.Rectangle2D;

import utils.Images;
import entities.flying.enemies.*;
import entities.flying.pickupItems.*;
import static entities.flying.EntityFactory.TypeConstants.*;

/**
 * Factory class for making- and registering new entities.
 * Contains a list of info associated with each entity.
 * When creating a new enemy:
 * .Make new static final typeConstant-variable.
 * .Add an entry in the constructConstantToNameMap-method.
 * .Add sprite name in ResourceLoader
 * .Register new entity in registerAllEntities-method.
 * .Make an new enemy constructor in the getEntity-method.
 * .Update LevelData :: addEntityToList() if modification of levelData is
 * needed.
 */
public class EntityFactory {
   public HashMap<String, EntityInfo> pickupInfo;
   public HashMap<String, EntityInfo> enemyInfo;
   private HashMap<Integer, String> constantToNameMap;
   private PlayerFly player; // Some enemies might need the player for their constructor.

   // Entity constants. Should be numbered from 0 and upwards.
   public static class TypeConstants {
      public static final int DELETE = 0;
      public static final int POWERUP = 1;
      public static final int REPAIR = 2;
      public static final int BOMB = 3;
      public static final int TARGET = 4;
      public static final int DRONE = 5;
      public static final int SMALLSHIP = 6;
      public static final int OCTADRONE = 7;
      public static final int TANKDRONE = 8;
      public static final int BLASTERDRONE = 9;
      public static final int REAPERDRONE = 10;
      public static final int FLAMEDRONE = 11;
      public static final int WASPDRONE = 12;
      public static final int KAMIKAZEDRONE = 13;
      public static final int SMALL_ASTEROID = 14;
      public static final int BIG_ASTEROID = 15;
      public static final int BURNING_FRAGMENT = 16;
   }

   public EntityFactory(PlayerFly player) {
      this.enemyInfo = new HashMap<>();
      this.pickupInfo = new HashMap<>();
      this.constantToNameMap = new HashMap<>();
      this.player = player;
      this.constructConstantToNameMap();
      this.registerAllEntities();
   }

   private void constructConstantToNameMap() {
      this.constantToNameMap.put(DELETE, "delete");
      this.constantToNameMap.put(POWERUP, "powerup");
      this.constantToNameMap.put(REPAIR, "repair");
      this.constantToNameMap.put(BOMB, "bomb");
      this.constantToNameMap.put(TARGET, "target");
      this.constantToNameMap.put(DRONE, "drone");
      this.constantToNameMap.put(SMALLSHIP, "smallShip");
      this.constantToNameMap.put(BLASTERDRONE, "blasterDrone");
      this.constantToNameMap.put(TANKDRONE, "tankDrone");
      this.constantToNameMap.put(OCTADRONE, "octaDrone");
      this.constantToNameMap.put(REAPERDRONE, "reaperDrone");
      this.constantToNameMap.put(FLAMEDRONE, "flameDrone");
      this.constantToNameMap.put(WASPDRONE, "waspDrone");
      this.constantToNameMap.put(KAMIKAZEDRONE, "kamikazeDrone");
      this.constantToNameMap.put(SMALL_ASTEROID, "smallAsteroid");
      this.constantToNameMap.put(BIG_ASTEROID, "bigAsteroid");
      this.constantToNameMap.put(BURNING_FRAGMENT, "burningFragment");
   }

   private void registerAllEntities() {
      this.registerEntity(
            DELETE,
            Images.DELETE_SPRITE, 28, 30, 1, 1,
            getName(DELETE), 90, 90, 0, 0,
            0, 0);
      this.registerEntity(
            POWERUP,
            Images.POWERUP_SPRITE, 30, 30, 1, 7,
            getName(POWERUP), 30, 50, 28, 20,
            0, 0);
      this.registerEntity(
            REPAIR,
            Images.REPAIR_SPRITE, 30, 30, 1, 4,
            getName(REPAIR), 60, 60, 15, 15,
            0, 0);
      this.registerEntity(
            BOMB,
            Images.BOMB_PICKUP_SPRITE, 25, 25, 1, 2,
            getName(BOMB), 45, 45, 15, 18,
            0, 0);
      this.registerEntity(
            TARGET,
            Images.TARGET_SPRITE, 20, 20, 2, 2,
            getName(TARGET), 60, 60, 0, 0,
            1, 0);
      this.registerEntity(
            DRONE,
            Images.DRONE_SPRITE, 30, 30, 2, 2,
            getName(DRONE), 78, 66, 4, 10,
            1, 0);
      this.registerEntity(
            SMALLSHIP,
            Images.SMALLSHIP_SPRITE, 30, 30, 2, 2,
            getName(SMALLSHIP), 60, 30, 16, 30,
            1, 0);
      this.registerEntity(
            OCTADRONE,
            Images.OCTADRONE_SPRITE, 30, 30, 2, 2,
            getName(OCTADRONE), 80, 80, 5, 5,
            1, 0);
      this.registerEntity(
            TANKDRONE,
            Images.TANKDRONE_SPRITE, 30, 30, 2, 4,
            getName(TANKDRONE), 80, 90, 5, 0,
            1, 0);
      this.registerEntity(
            BLASTERDRONE,
            Images.BLASTERDRONE_SPRITE, 30, 30, 2, 2,
            getName(BLASTERDRONE), 60, 90, 15, 0,
            1, 0);
      this.registerEntity(
            REAPERDRONE,
            Images.REAPERDRONE_SPRITE, 210, 80, 2, 4,
            getName(REAPERDRONE), 510, 150, 60, 45,
            1, 0);
      this.registerEntity(
            FLAMEDRONE,
            Images.FLAMEDRONE_SPRITE, 132, 128, 3, 6,
            getName(FLAMEDRONE), 120, 120, 138, 30,
            1, 0);
      this.registerEntity(
            WASPDRONE,
            Images.WASPDRONE_SPRITE, 40, 40, 2, 2,
            getName(WASPDRONE), 90, 90, 15, 15,
            1, 0);
      this.registerEntity(
            KAMIKAZEDRONE,
            Images.KAMIKAZEDRONE_SPRITE, 30, 30, 2, 2,
            getName(KAMIKAZEDRONE), 75, 75, 8, 8,
            1, 0);
      this.registerEntity(
            SMALL_ASTEROID,
            Images.SMALL_ASTEROID_SPRITE, 30, 30, 8, 2,
            getName(SMALL_ASTEROID), 75, 75, 8, 8,
            6, 0);
      this.registerEntity(
            BIG_ASTEROID,
            Images.BIG_ASTEROID_SPRITE, 90, 90, 1, 1,
            getName(BIG_ASTEROID), 220, 220, 25, 25,
            0, 0);
      this.registerEntity(
            BURNING_FRAGMENT,
            Images.BURNING_FRAGMENT_SPRITE, 50, 100, 2, 8,
            getName(BURNING_FRAGMENT), 75, 75, 35, 210,
            0, 0);
   }

   /**
    * Registers pickup-items into the pickup-hashMap, and enemies into
    * the enemies-hashMap.
    */
   private void registerEntity(
         int typeConstant,
         String spriteSheet, int spriteW, int spriteH, int rows, int cols,
         String name, int hitboxW, int hitboxH, int drawOffsetX, int drawOffsetY,
         int editorImgRow, int editorImgCol) {

      HashMap<String, EntityInfo> map = switch (typeConstant) {
         case DELETE, REPAIR, BOMB, POWERUP -> this.pickupInfo;
         default -> this.enemyInfo;
      };
      map.put(name,
            new EntityInfo(
                  typeConstant,
                  spriteSheet, spriteW, spriteH, rows, cols,
                  name, hitboxW, hitboxH, drawOffsetX, drawOffsetY,
                  editorImgRow, editorImgCol));
   }

   /**
    * Can be used to get the name of the enemy associated with a given constant.
    */
   public String getName(int constant) {
      return this.constantToNameMap.get(constant);
   }

   /**
    * Returns the amount of all registered entities (including pickupItems and
    * delete)
    */
   public int getAmountOfEntities() {
      return this.constantToNameMap.size();
   }

   /**
    * Can be used by the levelEditor, and the enemies themselves to extract
    * necessary info, like drawOffsets and editorImage.
    * If the name isn't registered, it throws an error.
    */
   public EntityInfo getEntityInfo(String name) {
      if (this.isEnemyRegistered(name)) {
         return this.enemyInfo.get(name);
      } else if (this.isPickupItemRegistered(name)) {
         return this.pickupInfo.get(name);
      } else {
         throw new IllegalArgumentException("No info available for " + name);
      }
   }

   /** Call the check if the given string is associated with an enemy */
   public boolean isEnemyRegistered(String name) {
      return this.enemyInfo.containsKey(name);
   }

   /** Call the check if the given string is associated with a pickupItem */
   public boolean isPickupItemRegistered(String name) {
      return this.pickupInfo.containsKey(name);
   }

   /**
    * Call to get an enemy, based on the given lineData.
    * OBS: call the isEnemyRegistered-method first, so that you know that the
    * lineData
    * is actually for an enemy, and not something else (like a cutscene).
    */
   public Enemy GetNewEnemy(String[] lineData) {
      // Parsing the data
      String name = lineData[0];
      float x = Float.parseFloat(lineData[1]);
      float y = Float.parseFloat(lineData[2]);
      int dir = Integer.parseInt(lineData[3]);
      int shootTimer = Integer.parseInt(lineData[4]);

      // Extracting necessary info
      EntityInfo info = this.enemyInfo.get(name);
      int typeConstant = info.typeConstant;
      int hitboxW = info.hitboxW;
      int hitboxH = info.hitboxH;

      // Construct the enemy
      Rectangle2D.Float hitbox = new Rectangle2D.Float(x, y, hitboxW, hitboxH);
      switch (typeConstant) {
         case TARGET:
            return new Target(hitbox, info);
         case DRONE:
            return new Drone(hitbox, info, shootTimer);
         case SMALLSHIP:
            return new SmallShip(hitbox, info, dir);
         case OCTADRONE:
            return new OctaDrone(hitbox, info, shootTimer);
         case TANKDRONE:
            return new TankDrone(hitbox, info);
         case BLASTERDRONE:
            return new BlasterDrone(hitbox, info);
         case REAPERDRONE:
            return new ReaperDrone(hitbox, info, shootTimer);
         case FLAMEDRONE:
            return new FlameDrone(hitbox, info);
         case WASPDRONE:
            return new WaspDrone(hitbox, info, dir, shootTimer);
         case KAMIKAZEDRONE:
            KamikazeDrone kamikazeDrone = new KamikazeDrone(hitbox, info);
            kamikazeDrone.setPlayer(player);
            return kamikazeDrone;
         case SMALL_ASTEROID:
            return new SmallAsteroid(hitbox, info, shootTimer, dir);
         case BIG_ASTEROID:
            return new BigAsteroid(hitbox, info, shootTimer, dir);
         case BURNING_FRAGMENT:
            return new BurningFragment(hitbox, info, shootTimer);
         default:
            throw new IllegalArgumentException("No enemy constructor for type " + typeConstant);
      }
   }

   public PickupItem getNewPickupItem(String name, float x, float y) {
      // Extracting necessary info
      EntityInfo info = this.pickupInfo.get(name);
      int typeConstant = info.typeConstant;
      int hitboxW = info.hitboxW;
      int hitboxH = info.hitboxH;

      // Construct the pickupItem
      Rectangle2D.Float hitbox = new Rectangle2D.Float(x, y, hitboxW, hitboxH);
      switch (typeConstant) {
         case BOMB:
            return new Bomb(hitbox, info);
         case REPAIR:
            return new Repair(hitbox, info);
         case POWERUP:
            return new Powerup(hitbox, info);
         default:
            throw new IllegalArgumentException("No enemy constructor for type " + typeConstant);
      }
   }

   public PickupItem getPickupItemFromLineData(String[] lineData) {
      // Parsing the data
      String name = lineData[0];
      float x = Float.parseFloat(lineData[1]);
      float y = Float.parseFloat(lineData[2]);
      return this.getNewPickupItem(name, x, y);
   }

}