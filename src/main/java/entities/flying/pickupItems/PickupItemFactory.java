package entities.flying.pickupItems;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;

import entities.flying.EntityInfo;
import utils.Images;

import static entities.flying.pickupItems.PickupItemFactory.TypeConstants.*;

public class PickupItemFactory {
   public HashMap<Integer, EntityInfo> pickupInfo;
   private HashMap<String, Integer> constantToNameMap;

   public static class TypeConstants {
      public static final int DELETE = 0;
      public static final int POWERUP = 1;
      public static final int REPAIR = 2;
      public static final int BOMB = 3;
   }

   public PickupItemFactory() {
      this.pickupInfo = new HashMap<>();
      this.constantToNameMap = new HashMap<>();
      this.constructConstantToNameMap();
      this.registerAllEntities();
   }

   private void constructConstantToNameMap() {
      this.constantToNameMap.put("delete", DELETE);
      this.constantToNameMap.put("powerup", POWERUP);
      this.constantToNameMap.put("repair", REPAIR);
      this.constantToNameMap.put("bomb", BOMB);
   }

   private void registerAllEntities() {
      // DELETE
      pickupInfo.put(DELETE, new EntityInfo(
            DELETE,
            Images.DELETE_SPRITE, 28, 30, 1, 1,
            90, 90, 0, 0,
            0, 0));

      // POWERUP
      pickupInfo.put(POWERUP, new EntityInfo(
            POWERUP,
            Images.POWERUP_SPRITE, 30, 30, 1, 7,
            30, 50, 28, 20,
            0, 0));

      // REPAIR
      pickupInfo.put(REPAIR, new EntityInfo(
            REPAIR,
            Images.REPAIR_SPRITE, 30, 30, 1, 4,
            60, 60, 15, 15,
            0, 0));

      // BOMB
      pickupInfo.put(BOMB, new EntityInfo(
            BOMB,
            Images.BOMB_PICKUP_SPRITE, 25, 25, 1, 2,
            45, 45, 15, 18,
            0, 0));
   }

   /** Call the check if the given string is associated with a pickupItem */
   public boolean isPickupItemRegistered(String name) {
      return this.constantToNameMap.containsKey(name);
   }

   public PickupItem getNewPickupItem(int typeConstant, float x, float y) {
      // Extracting necessary info
      EntityInfo info = this.pickupInfo.get(typeConstant);
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
      int typeConstant = this.constantToNameMap.get(name);
      return this.getNewPickupItem(typeConstant, x, y);
   }

   public HashMap<String, Integer> getPickupInfoMap() {
      return this.constantToNameMap;
   }

}
