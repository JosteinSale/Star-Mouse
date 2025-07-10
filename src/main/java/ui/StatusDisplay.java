package ui;

public class StatusDisplay {

   private int HP;
   private int maxHP;
   public int bombs;
   public int killedEnemies = 0;
   public int blinkTimer = 0;
   private int blinkFrames = 20;

   public int HPbarMaxW; // Increases as the player upgrades the ship
   public int HPbarCurW;

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
      this.HPbarMaxW = (int) (maxHP * 2.5f);
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