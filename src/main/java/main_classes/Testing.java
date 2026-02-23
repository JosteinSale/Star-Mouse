package main_classes;

import gamestates.Gamestate;

/**
 * Simple container class, which holds variables that can be manipulated for
 * testing purposes.
 */
public final class Testing {
   // Is changed from MainMenu - Don't modify.
   public static boolean testingMode = false;

   // General stuff
   public static final Gamestate testState = Gamestate.BOSS_MODE;
   public static final boolean drawHitboxes = false;
   public static final int unlockedLevels = 13;
   public static final boolean playMusic = true;
   public static final boolean playSFX = true;

   // Exploring
   public static final int exploringLevel = 1;
   public static final int exploringArea = 2;

   // Flying
   public static final int flyingStartY = 22000;
   public static final int flyingLevel = 1;

   // Boss Mode
   public static final int bossNr = 1;

   // Level Editor
   public static final int levelEditorLvl = 1;
}
