package entities.flying;

import java.util.HashMap;

import java.awt.geom.Rectangle2D;

import utils.Images;
import entities.flying.enemies.*;
import static entities.flying.EnemyFactory.TypeConstants.*;

/**
 * Factory class for making- and registering new enemies.
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
public class EnemyFactory {
   public HashMap<Integer, EntityInfo> enemyInfo;
   private HashMap<String, Integer> constantToNameMap;
   private PlayerFly player; // Some enemies might need the player for their constructor.

   public static class TypeConstants {
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

   public EnemyFactory(PlayerFly player) {
      this.enemyInfo = new HashMap<>();
      this.constantToNameMap = new HashMap<>();
      this.player = player;
      this.constructConstantToNameMap();
      this.registerAllEntities();
   }

   private void constructConstantToNameMap() {
      this.constantToNameMap.put("target", TARGET);
      this.constantToNameMap.put("drone", DRONE);
      this.constantToNameMap.put("smallShip", SMALLSHIP);
      this.constantToNameMap.put("blasterDrone", BLASTERDRONE);
      this.constantToNameMap.put("tankDrone", TANKDRONE);
      this.constantToNameMap.put("octaDrone", OCTADRONE);
      this.constantToNameMap.put("reaperDrone", REAPERDRONE);
      this.constantToNameMap.put("flameDrone", FLAMEDRONE);
      this.constantToNameMap.put("waspDrone", WASPDRONE);
      this.constantToNameMap.put("kamikazeDrone", KAMIKAZEDRONE);
      this.constantToNameMap.put("smallAsteroid", SMALL_ASTEROID);
      this.constantToNameMap.put("bigAsteroid", BIG_ASTEROID);
      this.constantToNameMap.put("burningFragment", BURNING_FRAGMENT);
   }

   private void registerAllEntities() {
      // TARGET
      enemyInfo.put(TARGET, new EntityInfo(
            TARGET,
            Images.TARGET_SPRITE, 20, 20, 2, 2,
            60, 60, 0, 0,
            1, 0));

      // DRONE
      enemyInfo.put(DRONE, new EntityInfo(
            DRONE,
            Images.DRONE_SPRITE, 30, 30, 2, 2,
            78, 66, 4, 10,
            1, 0));

      // SMALLSHIP
      enemyInfo.put(SMALLSHIP, new EntityInfo(
            SMALLSHIP,
            Images.SMALLSHIP_SPRITE, 30, 30, 2, 2,
            60, 30, 16, 30,
            1, 0));

      // OCTADRONE
      enemyInfo.put(OCTADRONE, new EntityInfo(
            OCTADRONE,
            Images.OCTADRONE_SPRITE, 30, 30, 2, 2,
            80, 80, 5, 5,
            1, 0));

      // TANKDRONE
      enemyInfo.put(TANKDRONE, new EntityInfo(
            TANKDRONE,
            Images.TANKDRONE_SPRITE, 30, 30, 2, 4,
            80, 90, 5, 0,
            1, 0));

      // BLASTERDRONE
      enemyInfo.put(BLASTERDRONE, new EntityInfo(
            BLASTERDRONE,
            Images.BLASTERDRONE_SPRITE, 30, 30, 2, 2,
            60, 90, 15, 0,
            1, 0));

      // REAPERDRONE
      enemyInfo.put(REAPERDRONE, new EntityInfo(
            REAPERDRONE,
            Images.REAPERDRONE_SPRITE, 210, 80, 2, 2,
            510, 150, 60, 45,
            1, 0));

      // FLAMEDRONE
      enemyInfo.put(FLAMEDRONE, new EntityInfo(
            FLAMEDRONE,
            Images.FLAMEDRONE_SPRITE, 132, 128, 3, 6,
            120, 120, 138, 30,
            1, 0));

      // WASPDRONE
      enemyInfo.put(WASPDRONE, new EntityInfo(
            WASPDRONE,
            Images.WASPDRONE_SPRITE, 40, 40, 2, 2,
            90, 90, 15, 15,
            1, 0));

      // KAMIKAZEDRONE
      enemyInfo.put(KAMIKAZEDRONE, new EntityInfo(
            KAMIKAZEDRONE,
            Images.KAMIKAZEDRONE_SPRITE, 30, 30, 2, 2,
            75, 75, 8, 8,
            1, 0));

      // SMALL_ASTEROID
      enemyInfo.put(SMALL_ASTEROID, new EntityInfo(
            SMALL_ASTEROID,
            Images.SMALL_ASTEROID_SPRITE, 30, 30, 8, 2,
            75, 75, 8, 8,
            6, 0));

      // BIG_ASTEROID
      enemyInfo.put(BIG_ASTEROID, new EntityInfo(
            BIG_ASTEROID,
            Images.BIG_ASTEROID_SPRITE, 90, 90, 1, 1,
            220, 220, 25, 25,
            0, 0));

      // BURNING_FRAGMENT
      enemyInfo.put(BURNING_FRAGMENT, new EntityInfo(
            BURNING_FRAGMENT,
            Images.BURNING_FRAGMENT_SPRITE, 50, 100, 2, 8,
            75, 75, 35, 210,
            0, 0));
   }

   /**
    * Returns the amount of all registered entities (including pickupItems and
    * delete)
    */
   public int getAmountOfEntities() {
      return this.constantToNameMap.size();
   }

   /** Call the check if the given string is associated with an enemy */
   public boolean isEnemyRegistered(String name) {
      return this.constantToNameMap.containsKey(name);
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
      EntityInfo info = this.enemyInfo.get(constantToNameMap.get(name));
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

   public HashMap<String, Integer> getEnemyInfoMap() {
      return this.constantToNameMap;
   }
}