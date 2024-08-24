package data_storage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class is a container for values pertaining to the players' progress.
 * It contains the data that will represent a single save file.
 */
public class ProgressValues {
   /*
    * See resetToDefault-method for default values.
    * OBS: Whenever you include a new value, make sure to initiate it in that
    * method!
    */

   public boolean saveStarted;
   public String lastUsed;

   private int maxHP;
   private int lazerDmg;
   private int credits;
   private int bombs;

   public boolean hasEnding1;
   public boolean hasEnding2;
   public boolean hasEnding3;
   public boolean firstPlayThrough;
   public boolean path3Unlocked;

   public int levelLayout;
   public boolean[] unlockedLevels;

   public ProgressValues() {
      this.resetToDefault();
      this.setTime();
   }

   // Setters
   public void setMaxHP(int maxHP) {
      this.maxHP = maxHP;
   }

   public void setLazerDmg(int lazerDmg) {
      this.lazerDmg = lazerDmg;
   }

   public void setCredits(int credits) {
      this.credits = credits;
   }

   public void setBombs(int bombs) {
      this.bombs = bombs;
   }

   /** Should be called whenever the progressValues are saved. */
   public void setTime() {
      LocalDateTime currentDateTime = LocalDateTime.now();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
      this.lastUsed = currentDateTime.format(formatter);
   }

   // Getters
   public int getMaxHP() {
      return maxHP;
   }

   public int getLazerDmg() {
      return lazerDmg;
   }

   public int getCredits() {
      return credits;
   }

   public int getBombs() {
      return bombs;
   }

   /** Replaces all variables with default values. */
   public void resetToDefault() {
      maxHP = 100;
      lazerDmg = 7;
      credits = 0;
      bombs = 0;

      hasEnding1 = false;
      hasEnding2 = false;
      hasEnding3 = false;
      firstPlayThrough = true;
      path3Unlocked = false;

      levelLayout = 1;
      unlockedLevels = getNewUnlockedLevels();
   }

   private boolean[] getNewUnlockedLevels() {
      boolean[] array = new boolean[13];
      for (int i = 0; i < array.length; i++) {
         array[i] = false;
      }
      array[0] = true; // Unlock first level.
      return array;
   }
}
