package misc;

/** This class is a container for values pertaining to the players' progress. 
 * It contains the data that will represent a single save file.
*/
public class ProgressValues {
   private int maxHP = 100;
   private int lazerDmg = 7;
   private int credits = 0;
   private int bombs = 10;

   public boolean hasEnding1 = false;
   public boolean hasEnding2 = false;
   public boolean hasEnding3 = false;          
   public boolean firstPlayThrough = true;
   public boolean path3Unlocked = false;

   public int levelLayout = 1;
   public boolean[] unlockedLevels = {   // For now this is a 1D-array, but we might make it a 2D-array.
      true, false, false, false, false,  // Path 1
           false, false, false, false,   // Path 2
           false, false, false, false    // Path 3
      };

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

   
   
}
