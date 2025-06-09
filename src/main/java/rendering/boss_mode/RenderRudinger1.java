package rendering.boss_mode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
   public void draw(SpriteBatch sb) {
      if (!rudinger.visible) {
         return;
      } else {
         rEyes.draw(sb);
         drawStaticBossImages(sb);
         drawRudingerMouth(sb);
         rActionHandler.draw(sb, rudinger.currentAction);
         drawRotatingLazerCharge(sb);
         rBossHealth.draw(sb);
      }
   }

   private void drawRudingerMouth(SpriteBatch sb) {
      rMouth.draw(sb);
      if (rudinger.mouth.shouldDrawBlink) {
         rMouth.drawSubImage(sb, 1, 0);
      }
   }

   private void drawRotatingLazerCharge(SpriteBatch sb) {
      if (rudinger.verticalLazer.isCharging()) {
         rRotatingLazerCharge.draw(sb);
      }

   }

   private void drawStaticBossImages(SpriteBatch sb) {
      DrawUtils.drawImage(
            sb, mainBodyImg,
            (int) rudinger.mainBodyXPos, (int) rudinger.mainBodyYPos,
            mainBodyW * 3, mainBodyH * 3);
   }
}
