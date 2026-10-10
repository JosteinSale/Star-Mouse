package utils;

import java.util.HashMap;

import static utils.Constants.Flying.SpriteSizes.BOMBEXPLOSION_SPRITE_HEIGHT;
import static utils.Constants.Flying.SpriteSizes.BOMBEXPLOSION_SPRITE_WIDTH;
import static utils.Constants.Flying.SpriteSizes.EXPLOSION_SPRITE_SIZE;
import static utils.Constants.Flying.SpriteSizes.MINE_EXPLOSION_SPRITE_SIZE;
import static utils.Constants.Flying.SpriteSizes.PRJT_HIT_SPRITE_SIZE;
import static utils.Constants.UI.LEVEL_ICON_SIZE;

/**
 * Simple container class for containing the info of sprite arrays.
 */
public class SpriteInfo {
   public static final HashMap<String, SpriteInfo> spriteInfo = new HashMap<>();
   public final int spriteW;
   public final int spriteH;
   public final int rows;
   public final int cols;

   private SpriteInfo(int spriteW, int spriteH, int rows, int cols) {
      this.spriteW = spriteW;
      this.spriteH = spriteH;
      this.rows = rows;
      this.cols = cols;
   }

   static {
      // Exploring
      register(Images.PLAYER_EXP_SPRITES, 90, 60, 9, 4);
      register(Images.PLAYER_EXP_SPRITES_NAKED, 90, 60, 9, 4);
      register(Images.PLAYER_EXP_SPRITES_SAD, 90, 60, 9, 4);
      register(Images.OLIVER_SPRITES, 90, 60, 9, 4);
      register(Images.GARD_SPRITES, 90, 60, 4, 4);
      register(Images.STANDARD_NPC_SPRITES, 90, 60, 1, 16);

      // Flying
      register(Images.SHIP_SPRITES, 30, 30, 7, 6);
      register(Images.SHIP_FLAME_SPRITES, 15, 15, 1, 2);
      register(Images.SHIP_DEATH_SPRITES, 40, 40, 1, 26);
      register(Images.TARGET_SPRITE, 20, 20, 2, 1);
      register(Images.DRONE_SPRITE, 30, 30, 2, 1);
      register(Images.SMALLSHIP_SPRITE, 30, 30, 2, 1);
      register(Images.OCTADRONE_SPRITE, 30, 30, 2, 1);
      register(Images.TANKDRONE_SPRITE, 30, 30, 2, 4);
      register(Images.BLASTERDRONE_SPRITE, 30, 30, 2, 1);
      register(Images.REAPERDRONE_SPRITE, 210, 80, 2, 1);
      register(Images.FLAMEDRONE_SPRITE, 68, 68, 2, 1);
      register(Images.WASPDRONE_SPRITE, 40, 40, 2, 1);
      register(Images.KAMIKAZEDRONE_SPRITE, 30, 30, 2, 2);
      register(Images.SMALL_ASTEROID_SPRITE, 30, 30, 8, 1);
      register(Images.BIG_ASTEROID_SPRITE, 90, 90, 1, 1);
      register(Images.BURNING_FRAGMENT_SPRITE, 50, 163, 2, 8);
      register(Images.CENTIPEDE_SPRITE, 100, 50, 6, 8);
      register(Images.LURKER_SPRITE, 40, 40, 2, 3);
      register(Images.BAT_DRONE_SPRITE, 40, 40, 2, 7);
      register(Images.MINE_DRONE_SPRITE, 150, 150, 3, 8);
      register(Images.LAZER_DRONE_SPRITE, 60, 60, 2, 2);
      register(Images.DELETE_SPRITE, 28, 30, 1, 1);
      register(Images.POWERUP_SPRITE, 30, 30, 1, 7);
      register(Images.REPAIR_SPRITE, 30, 30, 1, 4);
      register(Images.BOMB_PICKUP_SPRITE, 25, 25, 1, 2);
      register(Images.PROJECTILE_HIT, PRJT_HIT_SPRITE_SIZE, PRJT_HIT_SPRITE_SIZE, 1, 4);
      register(Images.BOMB_EXPLOSION_SPRITE,
            BOMBEXPLOSION_SPRITE_WIDTH, BOMBEXPLOSION_SPRITE_HEIGHT, 1, 11);
      register(Images.EXPLOSION, EXPLOSION_SPRITE_SIZE, EXPLOSION_SPRITE_SIZE, 1, 5);
      register(Images.MINE_EXPLOSION,
            MINE_EXPLOSION_SPRITE_SIZE, MINE_EXPLOSION_SPRITE_SIZE, 1, 6);
      register(Images.FLAME_SHOOT, 132, 100, 1, 6);
      register(Images.ORANGE_GLOW_BIG, 32, 32, 1, 4);
      register(Images.BLUE_GLOW_SMALL, 32, 32, 1, 2);
      register(Images.GREEN_GLOW_SMALL, 32, 32, 1, 2);
      register(Images.REAPER_GLOW, 80, 32, 1, 4);
      register(Images.WHITE_GLOW_BIG, 32, 32, 1, 4);
      register(Images.ORANGE_GLOW_DYNAMIC, 32, 32, 1, 4);

      // UI
      register(Images.LEVEL_ICONS, LEVEL_ICON_SIZE, LEVEL_ICON_SIZE, 1, 13);

      // BossMode
      register(Images.MACHINE_HEART_SPRITE, 40, 40, 2, 2);
      register(Images.HEATSEEKING_LAZER_SPRITE, 30, 220, 3, 4);
      register(Images.ROTATING_LAZER_SPRITE, 10, 433, 2, 3);
      register(Images.REAPER_EYES, 200, 52, 4, 5);
      register(Images.REAPER_MOUTH, 81, 58, 3, 8);
      register(Images.ENERGY_BALL_SPRITE, 60, 60, 1, 12);
      register(Images.RED_CHARGE, 100, 100, 1, 5);
      register(Images.PINK_CHARGE, 100, 100, 1, 5);

      // ObjectMoveEffect
      register(Images.ROW_OF_DRONES, 170, 29, 1, 1);
      register(Images.RUDINGER_SHIP, 50, 50, 1, 2);
      register(Images.RUDINGER1_IDLE, 343, 147, 1, 2);
      register(Images.RUDINGER1_DEATH, 343, 147, 1, 2);
      register(Images.LOOPING_EXPLOSION, 40, 40, 1, 10);
      register(Images.ROW_OF_FLAME_DRONES, 350, 110, 1, 1);
      register(Images.ROW_OF_FLAME_DRONES_SHADOW, 350, 110, 1, 1);
      register(Images.PLAYER_SHIP, 30, 50, 1, 2);
      register(Images.RAZE_SHADOW, 180, 160, 1, 4);
      register(Images.CATHEDRAL, 177, 211, 1, 2);
      register(Images.APO, 340, 333, 1, 1);
      register(Images.WHITE_CHARGE, 100, 100, 1, 5);
   }

   private static void register(String spriteName, int spriteW, int spriteH, int rows, int cols) {
      spriteInfo.put(spriteName, new SpriteInfo(spriteW, spriteH, rows, cols));
   }

   public static SpriteInfo get(String spriteName) {
      if (!spriteInfo.containsKey(spriteName)) {
         throw new IllegalArgumentException("No sprite info registered for " + spriteName);
      }
      return spriteInfo.get(spriteName);
   }
}
