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

   public RenderRudinger1(Rudinger1 rudinger) {
      this.rudinger = rudinger;
      this.mainBodyImg = ResourceLoader.getBossSprite("boss1_body2.png");
   }

   @Override
   public void draw(Graphics g) {
      if (!rudinger.visible) {
         return;
      } else {
         AnimatedComponent.draw(g, rudinger.eyes);
         drawStaticBossImages(g);
         AnimatedComponent.draw(g, rudinger.mouth);
         // healthDisplay.draw(g);
         // Draw all animations pertaining to individual bossParts and shootPatterns
         rudinger.actionHandler.draw(g, rudinger.currentAction);
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
