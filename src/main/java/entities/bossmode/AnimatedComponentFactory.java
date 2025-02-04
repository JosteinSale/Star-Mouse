package entities.bossmode;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import entities.bossmode.rudinger1.AnimatedMouth;
import entities.bossmode.rudinger1.ReaperEyes;
import utils.ImageContainer;
import utils.ResourceLoader;

/**
 * A factory class for producing animated components. Some can be reused, while
 * others are more specific to a certain boss etc.
 * 
 * NOTE: flush the imageContainer after use.
 */
public class AnimatedComponentFactory {
   public ImageContainer imageContainer;

   public AnimatedComponentFactory(ImageContainer imageContainer) {
      this.imageContainer = new ImageContainer();
   }

   public AnimatedComponent getRedChargeAnimation(int x, int y) {
      ArrayList<AnimationInfo> aniInfo = new ArrayList<>();
      BufferedImage redChargeSprite = ResourceLoader.getBossSprite(ResourceLoader.LAZER_CHARGE_SPRITE1);
      this.imageContainer.addImage(redChargeSprite);

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
      BufferedImage pinkChargeSprite = ResourceLoader.getBossSprite(ResourceLoader.LAZER_CHARGE_SPRITE2);
      aniInfo.add(new AnimationInfo("SHOOT", 0, 5, 3, 4, true));
      this.imageContainer.addImage(pinkChargeSprite);

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
      BufferedImage energyBallSprite = ResourceLoader.getBossSprite(ResourceLoader.ENERGY_BALL_SPRITE);
      this.imageContainer.addImage(energyBallSprite);

      AnimatedComponent chargeAnimation = new AnimatedComponent(
            energyBallSprite,
            60, 60, 1, 12,
            aniInfo,
            x, y);
      return chargeAnimation;
   }

   public ReaperEyes getReaperEyes(int x, int y, PlayerBoss player) {
      BufferedImage eyesImg = ResourceLoader.getBossSprite("boss1_eyes.png");
      this.imageContainer.addImage(eyesImg);

      ArrayList<AnimationInfo> aniInfo = new ArrayList<>(Arrays.asList(
            new AnimationInfo("IDLE", 0, 2, 3, 0, false),
            new AnimationInfo("SHUT_DOWN", 1, 5, 10, 4, false),
            new AnimationInfo("BOOT_UP", 1, 5, 10, 0, true),
            new AnimationInfo("SHOOT", 2, 2, 3, 0, false),
            new AnimationInfo("SMALL", 3, 2, 3, 0, false)));

      return new ReaperEyes(
            eyesImg, 200, 52, 4, 5,
            aniInfo, x, y, player);
   }

   public AnimatedMouth getAnimatedMouth(int x, int y) {
      BufferedImage mouthImg = ResourceLoader.getBossSprite("boss1_mouth.png");
      this.imageContainer.addImage(mouthImg);
      ArrayList<AnimationInfo> aniInfo1 = new ArrayList<>(Arrays.asList(
            new AnimationInfo("IDLE", 0, 1, 10, 0, false),
            new AnimationInfo("DAMAGE", 1, 2, 3, 0, false),
            new AnimationInfo("OPEN_UP", 2, 8, 10, 7, false),
            new AnimationInfo("CLOSE", 2, 8, 10, 0, true)));

      return new AnimatedMouth(
            mouthImg, 81, 58, 3, 8,
            aniInfo1, x, y);
   }

}
