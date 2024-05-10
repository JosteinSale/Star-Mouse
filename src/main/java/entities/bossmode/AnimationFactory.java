package entities.bossmode;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import utils.LoadSave;

/** A factory class for producing animations which are used multiple places */
public class AnimationFactory {
   BufferedImage redChargeSprite;
   BufferedImage pinkChargeSprite;
   BufferedImage energyBallSprite;

   public AnimationFactory() {
      this.loadImages();
   }

   private void loadImages() {
      this.redChargeSprite = LoadSave.getBossSprite(LoadSave.LAZER_CHARGE_SPRITE1);
      this.pinkChargeSprite = LoadSave.getBossSprite(LoadSave.LAZER_CHARGE_SPRITE2);
      this.energyBallSprite = LoadSave.getBossSprite(LoadSave.ENERGY_BALL_SPRITE);
   }

   public AnimatedComponent getRedChargeAnimation(int x, int y) {
      ArrayList<AnimationInfo> aniInfo = new ArrayList<>();
      aniInfo.add(new AnimationInfo("CHARGE", 0, 5, 3, 0, false));
      AnimatedComponent chargeAnimation = new AnimatedComponent(
         redChargeSprite,
         100, 100, 1, 5,
         aniInfo,
         x, y);
      return chargeAnimation;
   }

   public AnimatedComponent getPinkShootAnimation(int x, int y) {
      ArrayList<AnimationInfo> aniInfo = new ArrayList<>();
      aniInfo.add(new AnimationInfo("SHOOT", 0, 5, 3, 4, true));
      AnimatedComponent chargeAnimation = new AnimatedComponent(
         pinkChargeSprite,
         100, 100, 1, 5,
         aniInfo,
         x, y);
      return chargeAnimation;
   }

   public AnimatedComponent getPinkEnergyBall(int x, int y) {
      ArrayList<AnimationInfo> aniInfo = new ArrayList<>();
      aniInfo.add(new AnimationInfo("CHARGE", 0, 12, 3, 0, false));
      AnimatedComponent chargeAnimation = new AnimatedComponent(
         energyBallSprite,
         60, 60, 1, 12,
         aniInfo,
         x, y);
      return chargeAnimation;
   }
   
}
