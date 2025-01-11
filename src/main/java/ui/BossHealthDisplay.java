package ui;

import main_classes.Game;

public class BossHealthDisplay {
   private int maxHP;
   private int HP;
   public String bossName;
   public int HPbarMaxW = 800;
   public int HPbarCurW = HPbarMaxW;
   public int HPbarX = (Game.GAME_DEFAULT_WIDTH - HPbarMaxW) / 2;
   public int blinkTimer = 0;
   private int blinkFrames = 20;

   public BossHealthDisplay(String bossName, int maxHP) {
      this.bossName = bossName;
      this.maxHP = maxHP;
      this.HP = maxHP;
   }

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

   public void setBlinking(boolean active) {
      if (active) {
         this.blinkTimer = blinkFrames;
      } else {
         this.blinkTimer = 0; // reset
      }
   }
}
