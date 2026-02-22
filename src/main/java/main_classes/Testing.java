package main_classes;

import gamestates.Gamestate;

/**
 * Simple container class, which holds variables that can be manipulated for
 * testing purposes.
 */
public final class Testing {
   // Is changed from MainMenu. Don't modify.
   public static boolean testingMode = false;

   // Testing variables. Modify as needed.
   public static final Gamestate testState = Gamestate.FLYING;
   public static final boolean drawHitboxes = true;
   public static final int testLevel = 1;
   public static final int testArea = 2;
   public static final int tstUnlockedLevels = 13;
   public static final int levelEditorLvl = 1;
   public static final boolean playMusic = false;
   public static final boolean playSFX = true;
}
