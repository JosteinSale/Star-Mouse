package rendering.boss_mode;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import entities.bossmode.AnimatedComponent;
import entities.bossmode.rudinger1.Rudinger1;
import main_classes.Game;
import utils.ResourceLoader;

public class RenderRudinger1 implements IRenderBoss {
   private Rudinger1 rudinger;
   private BufferedImage mainBodyImg;
   private RenderBossHealth rBossHealth;

   public RenderRudinger1(Rudinger1 rudinger, RenderBossHealth rBossHealth) {
      this.rudinger = rudinger;
      this.mainBodyImg = ResourceLoader.getBossSprite("boss1_body2.png");
      this.rBossHealth = rBossHealth;
      rBossHealth.setNew(rudinger.getHealthDisplay());
   }

   @Override
   public void draw(Graphics g) {
      if (!rudinger.visible) {
         return;
      } else {
         AnimatedComponent.draw(g, rudinger.eyes);
         drawStaticBossImages(g);
         drawRudingerMouth(g);
         rudinger.actionHandler.draw(g, rudinger.currentAction);
         rBossHealth.draw(g);
      }
   }

   private void drawRudingerMouth(Graphics g) {
      AnimatedComponent.draw(g, rudinger.mouth);
      if (rudinger.mouth.shouldDrawBlink) {
         rudinger.mouth.drawBlinking(g);
      }
   }

   private void drawStaticBossImages(Graphics g) {
      g.drawImage(
            mainBodyImg,
            (int) (rudinger.mainBodyXPos * Game.SCALE),
            (int) (rudinger.mainBodyYPos * Game.SCALE),
            (int) (mainBodyImg.getWidth() * 3 * Game.SCALE),
            (int) (mainBodyImg.getHeight() * 3 * Game.SCALE), null);
   }

   @Override
   public void flush() {
      this.mainBodyImg = null;
   }
}
