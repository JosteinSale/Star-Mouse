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
   public static final int unlockedLevels = 12;
   public static final boolean playMusic = false;
   public static final boolean playSFX = true;

   // Exploring
   public static final int exploringLevel = 4;
   public static final int exploringArea = 1;
   public static final boolean drawCollissionMap = true;

   // Flying
   public static final int flyingStartY = 9000; // 9000
   public static final int flyingLevel = 4;
   public static final boolean printLevelY = false;
   public static final int maxHP = 100;

   // Boss Mode
   public static final int bossNr = 1;
}
