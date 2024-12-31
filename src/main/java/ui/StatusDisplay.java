package ui;

public class StatusDisplay {

   private int HP;
   private int maxHP = 100;
   public int bombs;
   public int killedEnemies = 0;
   public int blinkTimer = 0;
   private int blinkFrames = 20;

   public int HPbarMaxW = 300; // Increases as the player upgrades the ship
   public int HPbarCurW = (int) (((float) HP / maxHP) * HPbarMaxW);

   public void update() {
      updateBlinking();
   }

   private void updateBlinking() {
      if (blinkTimer != 0) {
         blinkTimer--;
      }
   }

   public void setHP(int HP) {
      this.HP = HP;
      this.HPbarCurW = (int) (((float) this.HP / maxHP) * HPbarMaxW);
   }

   public void setMaxHP(int maxHP) {
      this.maxHP = maxHP;
      // TODO - Adjust how long the bar is
   }

   public void setBombs(int bombs) {
      this.bombs = bombs;
   }

   public void setKilledEnemies(int killedEnemies) {
      this.killedEnemies = killedEnemies;
   }

   public void setBlinking(boolean active) {
      if (active) {
         this.blinkTimer = blinkFrames;
      } else {
         this.blinkTimer = 0; // reset
      }
   }

}