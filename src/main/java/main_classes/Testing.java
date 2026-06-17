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
   public static final Gamestate testState = Gamestate.EXPLORING;
   public static final boolean drawHitboxes = false;
   public static final int unlockedLevels = 13;
   public static final boolean playMusic = true;
   public static final boolean playSFX = true;

   // Exploring
   public static final int exploringLevel = 1;
   public static final int exploringArea = 1;
   public static final boolean drawCollissionMap = false;

   // Flying
   public static final int flyingStartY = 0; // 13000
   public static final int flyingLevel = 1;
   public static final boolean printLevelY = false;
   public static final int maxHP = 100;

   // Boss Mode
   public static final int bossNr = 1;
}
