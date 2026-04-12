package main_classes;

import game_states.Gamestate;

/**
 * Simple container class, which holds variables that can be manipulated for
 * testing purposes.
 */
public final class Testing {
   // Is changed from MainMenu - Don't modify.
   public static boolean testingMode = false;

   // General stuff
   public static final Gamestate testState = Gamestate.FLYING;
   public static final boolean drawHitboxes = true;
   public static final int unlockedLevels = 13;
   public static final boolean playMusic = false;
   public static final boolean playSFX = true;

   // Exploring
   public static final int exploringLevel = 1;
   public static final int exploringArea = 2;

   // Flying
   public static final int flyingStartY = 0;
   public static final int flyingLevel = 2;

   // Boss Mode
   public static final int bossNr = 1;
}
