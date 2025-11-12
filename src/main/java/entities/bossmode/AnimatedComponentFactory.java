package entities.bossmode;

import java.util.HashMap;

import entities.bossmode.rudinger1.AnimatedMouth;
import entities.bossmode.rudinger1.ReaperEyes;
import utils.Images;

/**
 * A factory class for producing animated components. Some can be reused, while
 * others are more specific to a certain boss etc.
 */
public class AnimatedComponentFactory {

   public AnimatedComponent getRedChargeAnimation(int x, int y) {
      HashMap<String, AnimationInfo> aniInfo = new HashMap<>();
      aniInfo.put("CHARGE", new AnimationInfo(0, 5, 3, 0, false));
      AnimatedComponent chargeAnimation = new AnimatedComponent(
            Images.LAZER_CHARGE_SPRITE1,
            aniInfo,
            "CHARGE",
            100, 100, 1, 5, x, y);
      return chargeAnimation;
   }

   public AnimatedComponent getPinkShootAnimation(int x, int y) {
      HashMap<String, AnimationInfo> aniInfo = new HashMap<>();
      aniInfo.put("SHOOT", new AnimationInfo(0, 5, 3, 4, true));
      AnimatedComponent chargeAnimation = new AnimatedComponent(
            Images.LAZER_CHARGE_SPRITE2,
            aniInfo,
            "SHOOT",
            100, 100, 1, 5, x, y);
      return chargeAnimation;
   }

   public AnimatedComponent getPinkEnergyBall(int x, int y) {
      HashMap<String, AnimationInfo> aniInfo = new HashMap<>();
      aniInfo.put("CHARGE", new AnimationInfo(0, 12, 3, 0, false));
      AnimatedComponent chargeAnimation = new AnimatedComponent(
            Images.ENERGY_BALL_SPRITE,
            aniInfo,
            "CHARGE",
            60, 60, 1, 12, x, y);
      return chargeAnimation;
   }

   public ReaperEyes getReaperEyes(int x, int y, PlayerBoss player) {
      HashMap<String, AnimationInfo> aniInfo = new HashMap<>();
      aniInfo.putAll(new HashMap<String, AnimationInfo>() {
         {
            put(ReaperEyes.IDLE, new AnimationInfo(0, 2, 3, 0, false));
            put(ReaperEyes.SHUT_DOWN, new AnimationInfo(1, 5, 10, 4, false));
            put(ReaperEyes.BOOT_UP, new AnimationInfo(1, 5, 10, 0, true));
            put(ReaperEyes.FLASHING, new AnimationInfo(2, 2, 3, 0, false));
            put(ReaperEyes.SMALL_EYES, new AnimationInfo(3, 2, 3, 0, false));
         }
      });

      return new ReaperEyes(
            "boss1_eyes.png", 200, 52, 4, 5,
            aniInfo, x, y, player);
   }

   public AnimatedMouth getAnimatedMouth(int x, int y) {
      HashMap<String, AnimationInfo> aniInfo = new HashMap<>();
      aniInfo.putAll(new HashMap<String, AnimationInfo>() {
         {
            put(AnimatedMouth.IDLE, new AnimationInfo(0, 1, 10, 0, false));
            put(AnimatedMouth.DAMAGE, new AnimationInfo(1, 2, 3, 0, false));
            put(AnimatedMouth.OPEN_UP, new AnimationInfo(2, 8, 10, 7, false));
            put(AnimatedMouth.CLOSE, new AnimationInfo(2, 8, 10, 0, true));
         }
      });

      return new AnimatedMouth(
            "boss1_mouth.png", 81, 58, 3, 8,
            aniInfo, x, y);
   }

}
