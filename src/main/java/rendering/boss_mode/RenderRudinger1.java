package rendering.boss_mode;

import java.awt.Graphics;

import entities.bossmode.rudinger1.Rudinger1;
import rendering.MyImage;
import rendering.misc.RenderAnimatedComponent;
import utils.DrawUtils;
import utils.Images;

public class RenderRudinger1 implements IRenderBoss {
   private Rudinger1 rudinger;
   private RenderActionHandler rActionHandler;
   private MyImage mainBodyImg;
   private int mainBodyW;
   private int mainBodyH;
   private RenderBossHealth rBossHealth;
   private RenderAnimatedComponent rEyes;
   private RenderAnimatedComponent rMouth;
   private RenderAnimatedComponent rRotatingLazerCharge;

   public RenderRudinger1(Rudinger1 rudinger, RenderBossHealth rBossHealth, Images images) {
      this.rudinger = rudinger;
      this.mainBodyImg = images.getBossSprite("boss1_body2.png");
      this.mainBodyW = mainBodyImg.getWidth();
      this.mainBodyH = mainBodyImg.getHeight();
      this.rBossHealth = rBossHealth;
      rBossHealth.setNew(rudinger.getHealthDisplay());
      this.rActionHandler = new RenderActionHandler(rudinger.actionHandler, images);
      this.rEyes = new RenderAnimatedComponent(rudinger.eyes, images);
      this.rMouth = new RenderAnimatedComponent(rudinger.mouth, images);
      this.rRotatingLazerCharge = new RenderAnimatedComponent(
            rudinger.verticalLazer.chargeAnimation, images);
   }

   @Override
   public void draw(Graphics g) {
      if (!rudinger.visible) {
         return;
      } else {
         rEyes.draw(g);
         drawStaticBossImages(g);
         drawRudingerMouth(g);
         rActionHandler.draw(g, rudinger.currentAction);
         drawRotatingLazerCharge(g);
         rBossHealth.draw(g);
      }
   }

   private void drawRudingerMouth(Graphics g) {
      rMouth.draw(g);
      if (rudinger.mouth.shouldDrawBlink) {
         rMouth.drawSubImage(g, 1, 0);
      }
   }

   private void drawRotatingLazerCharge(Graphics g) {
      if (rudinger.verticalLazer.isCharging()) {
         rRotatingLazerCharge.draw(g);
      }

   }

   private void drawStaticBossImages(Graphics g) {
      DrawUtils.drawImage(
            g, mainBodyImg,
            (int) rudinger.mainBodyXPos, (int) rudinger.mainBodyYPos,
            mainBodyW * 3, mainBodyH * 3);
   }

   @Override
   public void flush() {
      this.mainBodyImg = null;
   }
}
