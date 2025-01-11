package rendering.boss_mode;

import ui.BossHealthDisplay;

import utils.ResourceLoader;
import static utils.HelpMethods.DrawCenteredString;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import main_classes.Game;

/**
 * Renders the boss health.
 * OBS: call the 'setNew'-method whenever you load a new boss.
 */
public class RenderBossHealth {
   private BossHealthDisplay hdp;
   private Font font;
   private Color HPbgColor = new Color(97, 0, 15, 180);
   private int HPbarH = 10;
   private int HPbarY = 10;
   private Rectangle bossNameRect;

   public RenderBossHealth() {
      this.font = ResourceLoader.getInfoFont();
   }

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
      g.setColor(HPbgColor);
      g.fillRect(
            (int) (hdp.HPbarX * Game.SCALE), (int) (HPbarY * Game.SCALE),
            (int) (hdp.HPbarMaxW * Game.SCALE), (int) (HPbarH * Game.SCALE));
      if (hdp.blinkTimer % 4 == 0) {
         g.setColor(Color.RED);
      } else {
         g.setColor(Color.WHITE);
      }
      g.fillRect(
            (int) (hdp.HPbarX * Game.SCALE), (int) (HPbarY * Game.SCALE),
            (int) (hdp.HPbarCurW * Game.SCALE), (int) (HPbarH * Game.SCALE));

      // Text
      g.setFont(font);
      g.setColor(Color.WHITE);
      DrawCenteredString(g, hdp.bossName, bossNameRect, font);
   }
}
