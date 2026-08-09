package utils;

public class Constants {

   public static enum Direction {
      LEFT,
      RIGHT,
      UP,
      DOWN
   }

   public static class UI {
      public static final int CURSOR_WIDTH = 20 * 3;
      public static final int CURSOR_HEIGHT = 11 * 3;
      public static final int SLIDER_WIDTH = 8 * 3;
      public static final int SLIDER_HEIGHT = 15 * 3;
      public static final int INFOBOX_WIDTH = 600;
      public static final int INFOBOX_HEIGHT = 150;
      public static final int DIALOGUEBOX_WIDTH = 269 * 3;
      public static final int DIALOGUEBOX_HEIGHT = 63 * 3;
      public static final int OPTIONS_WIDTH = 800;
      public static final int OPTIONS_HEIGHT = 600;
      public static final int PAUSE_EXPLORING_WIDTH = 800;
      public static final int PAUSE_EXPLORING_HEIGHT = 600;
      public static final int MECHANIC_DISPLAY_WIDTH = 265 * 3;
      public static final int MECHANIC_DISPLAY_HEIGHT = 177 * 3;
      public static final int NUMBER_DISPLAY_WIDTH = 164 * 3;
      public static final int NUMBER_DISPLAY_HEIGHT = 130 * 3;
      public static final int NUMBER_SELECT_WIDTH = 11 * 3;
      public static final int NUMBER_SELECT_HEIGHT = 81 * 3;
      public static final int ITEM_BOX_WIDTH = 130 * 3;
      public static final int ITEM_BOX_HEIGHT = 66 * 3;
      public static final int INFOBOX_MAX_LETTERS = 30;
      public static final int DIALOGUE_MAX_LETTERS = 32;
      public static final int ITEM_MAX_LETTERS = 24;
      public static final float FONT_SIZE_ITEM = 20f;
      public static final float FONT_SIZE_INFO = 28f;
      public static final float FONT_SIZE_MENU = 37f;
      public static final float FONT_SIZE_NAME = 40f;
      public static final float FONT_SIZE_HEADER = 50f;
      public static final int PORTRAIT_SIZE = 55;
      public static final int LEVEL_ICON_SIZE = 50;
      public static final int LEVEL_ICON_DRAW_SIZE = 100;
      public static final int LEVEL_SELECT_BOX_SIZE = 130;

   }

   public static class Exploring {

      public enum CharacterAction {
         STANDING,
         WALKING,
         POSING
      }

      public static class Sprites {
         public static final int STANDARD_SPRITE_WIDTH = 90;
         public static final int STANDARD_SPRITE_HEIGHT = 60;
      }

      public static class Cutscenes {
         public static final String FADE_TO = "to";
         public static final String FADE_FROM = "from";

         public static final String OBJECT = "object";
         public static final String DOOR = "door";
         public static final String PORTAL = "portal";
         public static final String NPC = "npc";
         public static final String AUTOMATIC_TRIGGER = "automaticTrigger";
         public static final String BOSS = "boss";
         public static final String PLAYER = "player";
         public static final String OLIVER = "oliver";
         public static final String GARD = "gard";
         public static final String SONG = "song";
         public static final String AMBIENCE = "ambience";
      }
   }

   public static class Flying {
      public static final int REPAIR_HEALTH = 50;
      public static final int COLLISION_MAP_Y_OFFSET = 150;
      public static final int COLLISION_MAP_X_OFFSET = 150;
      public static final int COLLISION_MAP_WIDTH = 450 * 3;
      public static final float DEFAULT_FG_SPEED = 2f;
      public static final float SLOWED_DOWN_FG_SPEED = 0.7f;
      public static final float SHIP_HITBOX_WIDTH = 50f;
      public static final float SHIP_HITBOX_HEIGHT = 50f;

      public static class SpriteSizes {
         public static final int SHIP_SPRITE_WIDTH = 30;
         public static final int SHIP_SPRITE_HEIGHT = 30;
         public static final int PRJT_HIT_SPRITE_SIZE = 15;
         public static final int PLAYER_PRJT_SPRITE_W = 15;
         public static final int PLAYER_PRJT_SPRITE_H = 25;
         public static final int BOMB_PRJT_SPRITE_SIZE = 25;
         public static final int REAPERDRONE_PRJT_SPRITE_W = 114;
         public static final int REAPERDRONE_PRJT_SPRITE_H = 17;
         public static final int FLAME_PRJT_SPRITE_W = 150;
         public static final int FLAME_PRJT_SPRITE_H = 80;
         public static final int EXPLOSION_SPRITE_SIZE = 40;
         public static final int BOMBEXPLOSION_SPRITE_WIDTH = 300;
         public static final int BOMBEXPLOSION_SPRITE_HEIGHT = 250;
      }

      public static class PlaneAction {
         public static final int IDLE = 0;
         public static final int FLYING_LEFT = 1;
         public static final int FLYING_RIGHT = 2;
         public static final int TELEPORTING_RIGHT = 3;
         public static final int TELEPORTING_LEFT = 4;
         public static final int TAKING_COLLISION_DAMAGE = 5;
         public static final int TAKING_SHOOT_DAMAGE = 6;

         public static int GetPlayerSpriteAmount(int planeAction) {
            switch (planeAction) {
               case FLYING_RIGHT:
                  return 3;
               case FLYING_LEFT:
                  return 3;
               case TELEPORTING_LEFT:
               case TELEPORTING_RIGHT:
               case IDLE:
                  return 1;
               case TAKING_COLLISION_DAMAGE:
                  return 6;
               case TAKING_SHOOT_DAMAGE:
                  return 4;
            }
            return 4;
         }
      }
   }

   public static class Audio {
      // SFX IDs - Misc
      public static final String CATHEDRAL_SHOT = "cathedral_shot";

      // SFX IDs - Flying
      public static final String SFX_LAZER = "lazer";
      public static final String SFX_BOMBSHOOT = "bomb_shoot";
      public static final String SFX_TELEPORT = "teleport";
      public static final String SFX_COLLISION = "collision";
      public static final String SFX_SMALL_EXPLOSION = "small_explosion";
      public static final String SFX_BIG_EXPLOSION = "big_explosion";
      public static final String SFX_BOMB_PICKUP = "bomb_pickup";
      public static final String SFX_REPAIR = "repair";
      public static final String SFX_POWERUP = "powerup";
      public static final String SFX_HURT = "hurt";
      public static final String SFX_DEATH = "death";

      // SFX IDs - Exploring
      public static final String SFX_CURSOR = "cursor";
      public static final String SFX_CURSOR_SELECT = "cursor_select";
      public static final String SFX_STARTGAME = "start_game";
      public static final String SFX_INVENTORY_PICKUP = "pickup";
      public static final String SFX_SUCCESS = "success";
      public static final String SFX_INFOBOX = "infobox";
      public static final String MISSILE_STRIKE = "missile_strike";

      // SFX IDs - BossMode
      public static final String SFX_METALLIC_SOUND = "metallic_sound";
      public static final String SFX_RUDINGER1_DEATH = "rudinger_death";

      // Song IDs - Misc
      public static final String NONE = "none";
      public static final String SONG_MAIN_MENU = "song_main_menu";
      public static final String SONG_RUDINGER_THEME = "song_rudinger_theme";
      public static final String SONG_APO_EXPLODES = "song_apo_explodes";
      public static final String SONG_BURNING_PLANET = "song_burning_planet";

      // Song IDs - Flying
      public static final String SONG_FLY_LEVEL0 = "song_fly_level0";
      public static final String SONG_FLY_LEVEL1 = "song_fly_level1";
      public static final String SONG_FLY_LEVEL2 = "song_fly_level2";
      public static final String SONG_FLY_LEVEL3 = "song_fly_level3";
      public static final String SONG_FLY_LEVEL4 = "song_fly_level4";
      public static final String SONG_FLY_LEVEL5 = "song_fly_level5";

      // Song IDs - Exploring
      public static final String SONG_EXPLORING_ACADEMY = "song_exploring_academy";
      public static final String SONG_EXPLORING_VYKE = "song_exploring_vyke";
      public static final String SONG_EXPLORING_CATHEDRAL = "song_exploring_cathedral";

      // Song IDs - Bosses
      public static final String SONG_BOSS1 = "song_boss1";

      // Ambience IDs
      public static final String AMBIENCE_ROCKET_ENGINE = "ambience_rocket_engine";
      public static final String AMBIENCE_WIND = "ambience_wind";
      public static final String AMBIENCE_HANGAR = "ambience_hangar";
      public static final String AMBIENCE_ALARM = "ambience_alarm";
      public static final String AMBIENCE_CAVE = "ambience_cave";

      public static String GetFlyLevelSong(int level) {
         switch (level) {
            case 0:
               return SONG_FLY_LEVEL0;
            case 1:
               return SONG_FLY_LEVEL1;
            case 2:
               return SONG_FLY_LEVEL2;
            case 3:
               return SONG_FLY_LEVEL3;
            case 4:
               return SONG_FLY_LEVEL4;
            case 5:
               return SONG_FLY_LEVEL5;
            default:
               return SONG_FLY_LEVEL0;
         }
      }
   }
}
