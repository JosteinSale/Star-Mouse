package entities.flying;

import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

import utils.Images;
import entities.Dimensions;
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
   public HashMap<Integer, FlyEntityInfo> enemyInfo;
   private HashMap<String, Integer> nameToTypeMap;
   private PlayerFly player;
   private EnemyManager enemyManager;

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
      public static final int CENTIPEDE = 17;
      public static final int LURKER = 18;
      public static final int BAT_DRONE = 19;
      public static final int MINE_DRONE = 20;
      public static final int LAZER_DRONE = 21;
   }

   public EnemyFactory(EnemyManager enemyManager, PlayerFly player) {
      this.enemyInfo = new HashMap<>();
      this.nameToTypeMap = new HashMap<>();
      this.enemyManager = enemyManager;
      this.player = player;
      this.constructNameToConstantMap();
      this.registerAllEntities();
   }

   private void constructNameToConstantMap() {
      this.nameToTypeMap.put("target", TARGET);
      this.nameToTypeMap.put("drone", DRONE);
      this.nameToTypeMap.put("smallShip", SMALLSHIP);
      this.nameToTypeMap.put("blasterDrone", BLASTERDRONE);
      this.nameToTypeMap.put("tankDrone", TANKDRONE);
      this.nameToTypeMap.put("octaDrone", OCTADRONE);
      this.nameToTypeMap.put("reaperDrone", REAPERDRONE);
      this.nameToTypeMap.put("flameDrone", FLAMEDRONE);
      this.nameToTypeMap.put("waspDrone", WASPDRONE);
      this.nameToTypeMap.put("kamikazeDrone", KAMIKAZEDRONE);
      this.nameToTypeMap.put("smallAsteroid", SMALL_ASTEROID);
      this.nameToTypeMap.put("bigAsteroid", BIG_ASTEROID);
      this.nameToTypeMap.put("burningFragment", BURNING_FRAGMENT);
      this.nameToTypeMap.put("centipede", CENTIPEDE);
      this.nameToTypeMap.put("lurker", LURKER);
      this.nameToTypeMap.put("batDrone", BAT_DRONE);
      this.nameToTypeMap.put("mineDrone", MINE_DRONE);
      this.nameToTypeMap.put("lazerDrone", LAZER_DRONE);
   }

   private void registerAllEntities() {
      // TARGET
      enemyInfo.put(TARGET, new FlyEntityInfo(
            TARGET,
            Images.TARGET_SPRITE, 20, 20, 2, 1,
            60, 60, 0, 0));

      // DRONE
      enemyInfo.put(DRONE, new FlyEntityInfo(
            DRONE,
            Images.DRONE_SPRITE, 30, 30, 2, 1,
            78, 66, 0, 0));

      // SMALLSHIP
      enemyInfo.put(SMALLSHIP, new FlyEntityInfo(
            SMALLSHIP,
            Images.SMALLSHIP_SPRITE, 30, 30, 2, 1,
            60, 30, 0, 0));

      // OCTADRONE
      enemyInfo.put(OCTADRONE, new FlyEntityInfo(
            OCTADRONE,
            Images.OCTADRONE_SPRITE, 30, 30, 2, 1,
            80, 80, 0, 0));

      // TANKDRONE
      enemyInfo.put(TANKDRONE, new FlyEntityInfo(
            TANKDRONE,
            Images.TANKDRONE_SPRITE, 30, 30, 2, 4,
            80, 90, 0, 0));

      // BLASTERDRONE
      enemyInfo.put(BLASTERDRONE, new FlyEntityInfo(
            BLASTERDRONE,
            Images.BLASTERDRONE_SPRITE, 30, 30, 2, 1,
            60, 90, 0, 0));

      // REAPERDRONE
      enemyInfo.put(REAPERDRONE, new FlyEntityInfo(
            REAPERDRONE,
            Images.REAPERDRONE_SPRITE, 210, 80, 2, 1,
            510, 150, 0, 0));

      // FLAMEDRONE
      enemyInfo.put(FLAMEDRONE, new FlyEntityInfo(
            FLAMEDRONE,
            Images.FLAMEDRONE_SPRITE, 68, 68, 2, 1,
            120, 120, 0, 0));

      // WASPDRONE
      enemyInfo.put(WASPDRONE, new FlyEntityInfo(
            WASPDRONE,
            Images.WASPDRONE_SPRITE, 40, 40, 2, 1,
            90, 90, 0, 0));

      // KAMIKAZEDRONE
      enemyInfo.put(KAMIKAZEDRONE, new FlyEntityInfo(
            KAMIKAZEDRONE,
            Images.KAMIKAZEDRONE_SPRITE, 30, 30, 2, 2,
            75, 75, 0, 0));

      // SMALL_ASTEROID
      enemyInfo.put(SMALL_ASTEROID, new FlyEntityInfo(
            SMALL_ASTEROID,
            Images.SMALL_ASTEROID_SPRITE, 30, 30, 8, 1,
            75, 75, 0, 0));

      // BIG_ASTEROID
      enemyInfo.put(BIG_ASTEROID, new FlyEntityInfo(
            BIG_ASTEROID,
            Images.BIG_ASTEROID_SPRITE, 90, 90, 1, 1,
            220, 220, 0, 0));

      // BURNING_FRAGMENT
      enemyInfo.put(BURNING_FRAGMENT, new FlyEntityInfo(
            BURNING_FRAGMENT,
            Images.BURNING_FRAGMENT_SPRITE, 50, 163, 2, 8,
            75, 75, 0, 0));

      // CENTIPEDE
      enemyInfo.put(CENTIPEDE, new FlyEntityInfo(
            CENTIPEDE,
            Images.CENTIPEDE_SPRITE, 100, 50, 6, 8,
            81, 66, 0, 0));

      // LURKER
      enemyInfo.put(LURKER, new FlyEntityInfo(
            LURKER,
            Images.LURKER_SPRITE, 40, 40, 2, 3,
            90, 80, 0, 0));

      // BAT DRONE
      enemyInfo.put(BAT_DRONE, new FlyEntityInfo(
            BAT_DRONE,
            Images.BAT_DRONE_SPRITE, 40, 40, 2, 7,
            60, 60, 0, 6));

      // MINE DRONE
      enemyInfo.put(MINE_DRONE, new FlyEntityInfo(
            MINE_DRONE,
            Images.MINE_DRONE_SPRITE, 150, 150, 3, 8,
            84, 84, 0, 0));

      // LAZER DRONE
      enemyInfo.put(LAZER_DRONE, new FlyEntityInfo(
            LAZER_DRONE,
            Images.LAZER_DRONE_SPRITE, 60, 60, 2, 2,
            66, 66, 0, 0));
   }

   /**
    * Returns the amount of all registered entities (including pickupItems and
    * delete)
    */
   public int getAmountOfEntities() {
      return this.nameToTypeMap.size();
   }

   /** Call the check if the given string is associated with an enemy */
   public boolean isEnemyRegistered(String name) {
      return this.nameToTypeMap.containsKey(name);
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
      int chargeTimer = Integer.parseInt(lineData[4]);

      // Extracting necessary info
      FlyEntityInfo info = this.enemyInfo.get(nameToTypeMap.get(name));
      int typeConstant = info.typeConstant;
      int hitboxW = info.hitboxW;
      int hitboxH = info.hitboxH;

      // Construct the enemy
      Dimensions hitbox = new Dimensions(x, y, hitboxW, hitboxH);
      switch (typeConstant) {
         case TARGET:
            return new Target(hitbox, info);
         case DRONE:
            return new Drone(hitbox, info, chargeTimer);
         case SMALLSHIP:
            return new SmallShip(hitbox, info, dir);
         case OCTADRONE:
            return new OctaDrone(hitbox, info, chargeTimer);
         case TANKDRONE:
            return new TankDrone(hitbox, info);
         case BLASTERDRONE:
            return new BlasterDrone(hitbox, info);
         case REAPERDRONE:
            return new ReaperDrone(hitbox, info, chargeTimer);
         case FLAMEDRONE:
            return new FlameDrone(hitbox, info);
         case WASPDRONE:
            return new WaspDrone(hitbox, info, dir, chargeTimer);
         case KAMIKAZEDRONE:
            return new KamikazeDrone(hitbox, info, player);
         case SMALL_ASTEROID:
            return new SmallAsteroid(hitbox, info, chargeTimer, dir);
         case BIG_ASTEROID:
            return new BigAsteroid(hitbox, info, chargeTimer, dir);
         case BURNING_FRAGMENT:
            return new BurningFragment(hitbox, info, chargeTimer);
         case CENTIPEDE:
            int centipedeVectorX = Integer.parseInt(lineData[5]);
            int centipedeVectorY = Integer.parseInt(lineData[6]);
            return new Centipede(hitbox, info, chargeTimer, new Vector2(centipedeVectorX, centipedeVectorY));
         case LURKER:
            return new Lurker(hitbox, info, chargeTimer, player);
         case BAT_DRONE:
            return new BatDrone(hitbox, info, chargeTimer, dir);
         case MINE_DRONE:
            return new MineDrone(hitbox, info, enemyManager, player);
         case LAZER_DRONE:
            int lazerVectorX = Integer.parseInt(lineData[5]);
            int lazerVectorY = Integer.parseInt(lineData[6]);
            return new LazerDrone(hitbox, info, new Vector2(lazerVectorX, lazerVectorY));
         default:
            throw new IllegalArgumentException("No enemy constructor for type " + typeConstant);
      }
   }

   public HashMap<String, Integer> getNameToTypeMap() {
      return this.nameToTypeMap;
   }
}