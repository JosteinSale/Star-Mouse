package utils;

public class Constants {
    public static final String ABSOLUTE_PATH = "src/main/resources/";

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
        public static final int PAUSE_FLYING_WIDTH = 600;
        public static final int PAUSE_FLYING_HEIGHT = 400;
        public static final int MECHANIC_DISPLAY_WIDTH = 265 * 3;
        public static final int MECHANIC_DISPLAY_HEIGHT = 177 * 3;
        public static final int NUMBER_DISPLAY_WIDTH = 164 * 3;
        public static final int NUMBER_DISPLAY_HEIGHT = 130 * 3;
        public static final int NUMBER_SELECT_WIDTH = 11 * 3;
        public static final int NUMBER_SELECT_HEIGHT = 81 * 3;
        public static final int ITEM_BOX_WIDTH = 130 * 3;
        public static final int ITEM_BOX_HEIGHT = 66 * 3;
        public static final int INFOBOX_MAX_LETTERS = 27;
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

        public static class Sprites {
            public static final int STANDARD_SPRITE_WIDTH = 90;
            public static final int STANDARD_SPRITE_HEIGHT = 60;
        }

        public static class Cutscenes {
            // Hvilket element som har trigget en cutscene
            public static final int OBJECT = 0;
            public static final int DOOR = 1;
            public static final int NPC = 2;
            public static final int AUTOMATIC = 3;
            public static final int CHOICE = 4;

            public static final String FADE_OUT = "out";
            public static final String FADE_IN = "in";
        }

        // Dette er standard-verdier.
        public static class DirectionConstants {
            // Index for raden en animasjon har i spritesheet.
            public static final int STANDING = 0;
            public static final int WALKING_RIGHT = 1;
            public static final int WALKING_LEFT = 2;
            public static final int WALKING_DOWN = 3;
            public static final int WALKING_UP = 4;

            // Retningen spilleren peker mot
            public static final int RIGHT = 0;
            public static final int LEFT = 1;
            public static final int DOWN = 2;
            public static final int UP = 3;

            // Kan ha første bokstaven i navnet stor for å signalisere at metoden er statisk
            public static int GetSpriteAmount(int playerAction) {
                /*
                 * switch(playerAction) {
                 * case STANDING:
                 * return 1;
                 * etc etc
                 * }
                 */
                return 4;
            }
        }
    }

    public static class Flying {
        public static final int REPAIR_HEALTH = 50;

        public static class TypeConstants {
            // Projectile types
            public static final int PLAYER_PROJECTILE = 0;
            public static final int DRONE_PROJECTILE = 1;
            public static final int OCTA_PROJECTILE = 2;
            public static final int BOMB_PROJECTILE = 3;
            public static final int REAPER_PROJECTILE = 4;
            public static final int FLAME_PROJECTILE = 5;
            public static final int BOSS_PROJECTILE1 = 6;
        }

        public static class SpriteSizes {
            public static final int SHIP_SPRITE_WIDTH = 30;
            public static final int SHIP_SPRITE_HEIGHT = 30;
            public static final int SHIP_FLAMESPRITE_WIDTH = 15;
            public static final int SHIP_FLAMESPRITE_HEIGHT = 15;
            public static final int PRJT_HIT_SPRITE_SIZE = 15;
            public static final int PLAYER_PRJT_SPRITE_W = 15;
            public static final int PLAYER_PRJT_SPRITE_H = 25;
            public static final int BOMB_PRJT_SPRITE_SIZE = 25;
            public static final int DRONE_PRJT_SPRITE_SIZE = 37;
            public static final int OCTADRONE_PRJT_SPRITE_SIZE = 28;
            public static final int REAPERDRONE_PRJT_SPRITE_W = 114;
            public static final int REAPERDRONE_PRJT_SPRITE_H = 17;
            public static final int FLAME_PRJT_SPRITE_W = 150;
            public static final int FLAME_PRJT_SPRITE_H = 80;
            public static final int EXPLOSION_SPRITE_SIZE = 40;
            public static final int BOMBEXPLOSION_SPRITE_WIDTH = 300;
            public static final int BOMBEXPLOSION_SPRITE_HEIGHT = 250;
            public static final int SMALL_SPRITES_SIZE = 30;
        }

        // Dette er standard-verdier.
        public static class ActionConstants {
            // Index for raden en animasjon har i spritesheet.
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
        // SFX - Flying
        public static final int SFX_LAZER = 0;
        public static final int SFX_BOMBSHOOT = 1;
        public static final int SFX_TELEPORT = 2;
        public static final int SFX_COLLISION = 3;
        public static final int SFX_SMALL_EXPLOSION = 4;
        public static final int SFX_BIG_EXPLOSION = 5;
        public static final int SFX_BOMB_PICKUP = 6;
        public static final int SFX_REPAIR = 7;
        public static final int SFX_POWERUP = 8;
        public static final int SFX_HURT = 16;
        public static final int SFX_DEATH = 17;

        // SFX - Exploring
        public static final int SFX_CURSOR = 9;
        public static final int SFX_CURSOR_SELECT = 11;
        public static final int SFX_STARTGAME = 10;
        public static final int SFX_INVENTORY_PICKUP = 12;
        public static final int SFX_SUCCESS = 13;
        public static final int SFX_INFOBOX = 14;

        // SFX - BossMode
        public static final int SFX_METALLIC_SOUND = 18;
        public static final int SFX_RUDINGER1_DEATH = 19;

        // Songs (don't change these indexes!)
        public static final int SONG_LEVEL0 = 0;
        public static final int SONG_ACADEMY = 1;
        public static final int SONG_LEVEL1 = 2;
        public static final int SONG_MAIN_MENU = 3;
        public static final int SONG_VYKE = 4;
        public static final int SONG_LEVEL2 = 5;
        public static final int SONG_BOSS1 = 6;
        public static final int RUDINGER_THEME = 7;
        public static final int SONG_ASTEROID_ESCAPE = 8;
        public static final int SONG_BACK_TO_APO = 10;

        // Ambience (don't change these indexes!)
        public static final int AMBIENCE_SILENCE = 0;
        public static final int AMBIENCE_ROCKET_ENGINE = 1;
        public static final int AMBIENCE_WIND = 2;

        public static int GetFlyLevelSong(int level) {
            switch (level) {
                case 0:
                    return SONG_LEVEL0;
                case 1:
                    return SONG_LEVEL1;
                case 2:
                    return SONG_LEVEL2;
                case 3:
                    return SONG_ASTEROID_ESCAPE;
                case 4:
                    return SONG_BACK_TO_APO;
                default:
                    return SONG_LEVEL0;
            }
        }
    }
}
