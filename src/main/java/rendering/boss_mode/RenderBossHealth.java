package rendering.boss_mode;

import ui.BossHealthDisplay;
import utils.DrawUtils;

import java.awt.Graphics;
import java.awt.Rectangle;
import rendering.MyColor;

import main_classes.Game;

/**
 * Renders the boss health.
 * OBS: call the 'setNew'-method whenever you load a new boss.
 */
public class RenderBossHealth {
   private BossHealthDisplay hdp;
   private MyColor HPbgColor = new MyColor(97, 0, 15, 180);
   private int HPbarH = 10;
   private int HPbarY = 10;
   private Rectangle bossNameRect;

   /** Sets the health display for the new boss */
   public void setNew(BossHealthDisplay healthDisplay) {
      this.hdp = healthDisplay;
      this.bossNameRect = new Rectangle(
            (int) (healthDisplay.HPbarX * Game.SCALE),
            (int) ((HPbarY + 20) * Game.SCALE),
            (int) (healthDisplay.HPbarMaxW * Game.SCALE),
            (int) (HPbarH * Game.SCALE));
   }

   public void draw(Graphics g) {
      // Healthbar
      DrawUtils.fillRect(
            g, HPbgColor,
            hdp.HPbarX, HPbarY,
            hdp.HPbarMaxW, HPbarH);

      MyColor hpColor;
      if (hdp.blinkTimer % 4 == 0) {
         hpColor = MyColor.RED;
      } else {
         hpColor = MyColor.WHITE;
      }
      DrawUtils.fillRect(
            g, hpColor,
            hdp.HPbarX, HPbarY,
            hdp.HPbarCurW, HPbarH);

      // Text
      DrawUtils.DrawCenteredString(
            g, hdp.bossName, bossNameRect,
            DrawUtils.infoFont, MyColor.WHITE);
   }
}
