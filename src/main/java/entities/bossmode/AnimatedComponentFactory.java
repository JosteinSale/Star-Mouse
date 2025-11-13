package entities.bossmode;

import java.util.HashMap;

import entities.bossmode.rudinger1.AnimatedMouth;
import entities.bossmode.rudinger1.HeatSeekingLazer;
import entities.bossmode.rudinger1.MachineHeart;
import entities.bossmode.rudinger1.ReaperEyes;
import entities.bossmode.rudinger1.RotatingLazer;
import utils.Images;

/**
 * A factory class for producing animated components. Some can be reused, while
 * others are more specific to a certain boss etc.
 */
public class AnimatedComponentFactory {

   public AnimatedComponent getMachineHeartAnimation(int x, int y) {
      HashMap<String, AnimationInfo> aniInfo = new HashMap<>();
      aniInfo.put(MachineHeart.IDLE, new AnimationInfo(0, 2, 2, 0, false));
      aniInfo.put(MachineHeart.DAMAGE, new AnimationInfo(1, 2, 2, 0, false));
      return new AnimatedComponent(
            Images.MACHINE_HEART_SPRITE,
            aniInfo,
            MachineHeart.IDLE,
            40, 40, 2, 2, x, y);
   }

   public AnimatedComponent getHeatSeekingLazerAnimation(int x, int y) {
      HashMap<String, AnimationInfo> aniInfo = new HashMap<>();
      aniInfo.put(HeatSeekingLazer.CHARGING, new AnimationInfo(0, 4, 3, 0, false));
      aniInfo.put(HeatSeekingLazer.VISUAL_WARNING, new AnimationInfo(2, 4, 3, 0, false));
      aniInfo.put(HeatSeekingLazer.SHOOTING, new AnimationInfo(1, 4, 3, 0, false));
      return new AnimatedComponent(
            Images.HEATSEEKING_LAZER_SPRITE,
            aniInfo,
            HeatSeekingLazer.CHARGING,
            30, 220, 3, 4, x, y);
   }

   public AnimatedComponent getRotatingLazerAnimation(int x, int y) {
      HashMap<String, AnimationInfo> aniInfo = new HashMap<>();
      aniInfo.put(RotatingLazer.SHOOTING, new AnimationInfo(0, 3, 3, 0, false));
      aniInfo.put(RotatingLazer.VISUAL_WARNING, new AnimationInfo(1, 3, 3, 0, false));
      return new AnimatedComponent(
            Images.ROTATING_LAZER_SPRITE,
            aniInfo,
            RotatingLazer.VISUAL_WARNING,
            10, 433, 2, 3, x, y);
   }

   public AnimatedComponent getRedChargeAnimation(int x, int y) {
      HashMap<String, AnimationInfo> aniInfo = new HashMap<>();
      aniInfo.put("CHARGE", new AnimationInfo(0, 5, 3, 0, false));
      return new AnimatedComponent(
            Images.LAZER_CHARGE_SPRITE1,
            aniInfo,
            "CHARGE",
            100, 100, 1, 5, x, y);
   }

   public AnimatedComponent getPinkShootAnimation(int x, int y) {
      HashMap<String, AnimationInfo> aniInfo = new HashMap<>();
      aniInfo.put("SHOOT", new AnimationInfo(0, 5, 3, 4, true));
      return new AnimatedComponent(
            Images.LAZER_CHARGE_SPRITE2,
            aniInfo,
            "SHOOT",
            100, 100, 1, 5, x, y);
   }

   public AnimatedComponent getPinkEnergyBall(int x, int y) {
      HashMap<String, AnimationInfo> aniInfo = new HashMap<>();
      aniInfo.put("CHARGE", new AnimationInfo(0, 12, 3, 0, false));
      return new AnimatedComponent(
            Images.ENERGY_BALL_SPRITE,
            aniInfo,
            "CHARGE",
            60, 60, 1, 12, x, y);
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
