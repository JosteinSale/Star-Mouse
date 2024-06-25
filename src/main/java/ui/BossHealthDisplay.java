package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import main_classes.Game;
import utils.ResourceLoader;
import static utils.HelpMethods.DrawCenteredString;

public class BossHealthDisplay {
   private Font font;
   private Color HPbgColor = new Color(97, 0, 15, 180);

   private int HPbarMaxW = 800;
   private int HPbarH = 10;
   private int HPbarCurW = HPbarMaxW;
   private int HPbarX = (Game.GAME_DEFAULT_WIDTH - HPbarMaxW) / 2;
   private int HPbarY = 10;
   private Rectangle bossNameRect = new Rectangle(
      (int) (HPbarX * Game.SCALE), 
      (int) ((HPbarY + 20) * Game.SCALE), 
      (int) (HPbarMaxW * Game.SCALE), 
      (int) (HPbarH * Game.SCALE));

   private int maxHP;
   private int HP;
   private String bossName;
   private int blinkTimer = 0;
   private int blinkFrames = 20;

   public BossHealthDisplay(String bossName, int maxHP) {
      this.font = ResourceLoader.getInfoFont();
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

   public void draw(Graphics g) {
      // Healthbar
      g.setColor(HPbgColor);
      g.fillRect(
            (int) (HPbarX * Game.SCALE), (int) (HPbarY * Game.SCALE),
            (int) (HPbarMaxW * Game.SCALE), (int) (HPbarH * Game.SCALE));
      if (blinkTimer % 4 == 0) {
         g.setColor(Color.RED);
      } else {
         g.setColor(Color.WHITE);
      }
      g.fillRect(
            (int) (HPbarX * Game.SCALE), (int) (HPbarY * Game.SCALE),
            (int) (HPbarCurW * Game.SCALE), (int) (HPbarH * Game.SCALE));

      // Text
      g.setFont(font);
      g.setColor(Color.WHITE);
      DrawCenteredString(g, bossName, bossNameRect, font);
      
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
