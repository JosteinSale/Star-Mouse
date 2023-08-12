package utils;

public class Constants {
    public static class UI {
        public static final int CURSOR_WIDTH = 20 * 3;
        public static final int CURSOR_HEIGHT = 11 * 3;
        public static final int INFOBOX_WIDTH = 600;
        public static final int INFOBOX_HEIGHT = 150;
        public static final int DIALOGUEBOX_WIDTH = 269 * 3;
        public static final int DIALOGUEBOX_HEIGHT = 63 * 3;
        public static final int PAUSE_EXPLORING_WIDTH = 800;
        public static final int PAUSE_EXPLORING_HEIGHT = 600;
        public static final int PAUSE_FLYING_WIDTH = 600;
        public static final int PAUSE_FLYING_HEIGHT = 400;
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
        public static final float FONT_SIZE_NAME = 40f;
        public static final float FONT_SIZE_HEADER = 50f;
        public static final int PORTRAIT_SIZE = 55;

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
            switch(playerAction) {
                case STANDING:
                    return 1;
                etc etc
            }
            */
            return 4;
        }
    }
    }

    public static class Flying {
        public static class TypeConstants {
            // Projectile types
            public static final int PLAYER_PROJECTILE = 0;
            public static final int DRONE_PROJECTILE = 1;
            public static final int OCTA_PROJECTILE = 2;
            public static final int BOMB_PROJECTILE = 3;

            // Enemey types
            public static final int TARGET = 0;
            public static final int DRONE = 1;
            public static final int SMALL_SHIP = 2;
            public static final int OCTADRONE = 3;

            // Pickup items
            public static final int POWERUP = 0;
            public static final int REPAIR = 1;
            public static final int BOMB = 2;
        }
        

        public static class Sprites {
            public static final int SHIP_SPRITE_WIDTH = 30;
            public static final int SHIP_SPRITE_HEIGHT = 30;
            public static final int SHIP_FLAMESPRITE_WIDTH = 15;
            public static final int SHIP_FLAMESPRITE_HEIGHT = 15;
            public static final int POWERUP_SPRITE_SIZE = 30;
            public static final int REPAIR_SPRITE_SIZE = 30;
            public static final int BOMB_SPRITE_SIZE = 25;
            public static final int PRJT_HIT_SPRITE_SIZE = 15;
            public static final int PLAYER_PRJT_SPRITE_W = 15;
            public static final int PLAYER_PRJT_SPRITE_H = 25;
            public static final int DRONE_PRJT_SPRITE_SIZE = 37;
            public static final int OCTADRONE_PRJT_SPRITE_SIZE = 28;
            public static final int TARGET_SPRITE_SIZE = 20;
            public static final int DRONE_SPRITE_SIZE = 30;
            public static final int SMALLSHIP_SPRITE_SIZE = 30;
            public static final int OCTADRONE_SPRITE_SIZE = 30;
            public static final int EXPLOSION_SPRITE_SIZE = 40;
            public static final int BOMBEXPLOSION_SPRITE_WIDTH = 300;
            public static final int BOMBEXPLOSION_SPRITE_HEIGHT = 250;
            public static final int ALL_SPRITES_SIZE = 30;
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
                switch(planeAction) {
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
        // SFX
        public static final int LAZER_SAMPLE = 7;
        public static final int BOMB_SHOOT_SAMPLE = 8;
        public static final int TELEPORT_SAMPLE = 9;
        public static final int COLLISION_SAMPLE = 14;
        public static final int SMALL_EXPLOSION_SAMPLE = 18;
        public static final int BIG_EXPLOSION_SAMPLE = 20;

        // Songs
        public static final int TUTORIAL_SONG = 2;

    }
}
