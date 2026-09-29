package entities.animation;

import static entities.animation.Animation.Type.*;

import java.util.HashMap;

import entities.boss_mode.PlayerBoss;
import entities.boss_mode.rudinger1.AnimatedMouth;
import entities.boss_mode.rudinger1.HeatSeekingLazer;
import entities.boss_mode.rudinger1.MachineHeart;
import entities.boss_mode.rudinger1.ReaperEyes;
import entities.boss_mode.rudinger1.RotatingLazer;
import utils.Images;
import utils.Constants.Flying.PlaneAction;

/**
 * A factory class for producing animated components. Some can be reused, while
 * others are more specific to a certain boss etc.
 */
public class ComplexAnimationFactory {

   public static ComplexAnimation GetMachineHeartAnimation(int x, int y) {
      HashMap<String, AnimationInfo> aniInfo = new HashMap<>();
      aniInfo.put(MachineHeart.IDLE, new AnimationInfo(0, 2, 3, LOOP_FORWARDS));
      aniInfo.put(MachineHeart.DAMAGE, new AnimationInfo(1, 2, 3, LOOP_FORWARDS));
      return new ComplexAnimation(
            Images.MACHINE_HEART_SPRITE,
            aniInfo,
            MachineHeart.IDLE,
            40, 40, 2, 2, x, y);
   }

   public static ComplexAnimation GetHeatSeekingLazerAnimation(int x, int y) {
      HashMap<String, AnimationInfo> aniInfo = new HashMap<>();
      aniInfo.put(HeatSeekingLazer.CHARGING, new AnimationInfo(0, 4, 4, LOOP_FORWARDS));
      aniInfo.put(HeatSeekingLazer.VISUAL_WARNING, new AnimationInfo(2, 4, 4, LOOP_FORWARDS));
      aniInfo.put(HeatSeekingLazer.SHOOTING, new AnimationInfo(1, 4, 4, LOOP_FORWARDS));
      return new ComplexAnimation(
            Images.HEATSEEKING_LAZER_SPRITE,
            aniInfo,
            HeatSeekingLazer.CHARGING,
            30, 220, 3, 4, x, y);
   }

   public static ComplexAnimation GetRotatingLazerAnimation(int x, int y) {
      HashMap<String, AnimationInfo> aniInfo = new HashMap<>();
      aniInfo.put(RotatingLazer.SHOOTING, new AnimationInfo(0, 3, 4, LOOP_FORWARDS));
      aniInfo.put(RotatingLazer.VISUAL_WARNING, new AnimationInfo(1, 3, 4, LOOP_FORWARDS));
      return new ComplexAnimation(
            Images.ROTATING_LAZER_SPRITE,
            aniInfo,
            RotatingLazer.VISUAL_WARNING,
            10, 433, 2, 3, x, y);
   }

   public static ComplexAnimation GetRedChargeAnimation(int x, int y) {
      HashMap<String, AnimationInfo> aniInfo = new HashMap<>();
      aniInfo.put("CHARGE", new AnimationInfo(0, 5, 4, LOOP_FORWARDS));
      return new ComplexAnimation(
            Images.LAZER_CHARGE_SPRITE1,
            aniInfo,
            "CHARGE",
            100, 100, 1, 5, x, y);
   }

   public static ComplexAnimation GetPinkShootAnimation(int x, int y) {
      HashMap<String, AnimationInfo> aniInfo = new HashMap<>();
      aniInfo.put("SHOOT", new AnimationInfo(0, 5, 4, LOOP_BACKWARDS));
      return new ComplexAnimation(
            Images.LAZER_CHARGE_SPRITE2,
            aniInfo,
            "SHOOT",
            100, 100, 1, 5, x, y);
   }

   public static ComplexAnimation GetPinkEnergyBall(int x, int y) {
      HashMap<String, AnimationInfo> aniInfo = new HashMap<>();
      aniInfo.put("CHARGE", new AnimationInfo(0, 12, 4, LOOP_FORWARDS));
      return new ComplexAnimation(
            Images.ENERGY_BALL_SPRITE,
            aniInfo,
            "CHARGE",
            60, 60, 1, 12, x, y);
   }

   public static ReaperEyes GetReaperEyes(int x, int y, PlayerBoss player) {
      HashMap<String, AnimationInfo> aniInfo = new HashMap<>();
      aniInfo.putAll(new HashMap<String, AnimationInfo>() {
         {
            put(ReaperEyes.IDLE, new AnimationInfo(0, 2, 3, LOOP_FORWARDS));
            put(ReaperEyes.SHUT_DOWN, new AnimationInfo(1, 5, 10, ONCE_FORWARDS));
            put(ReaperEyes.BOOT_UP, new AnimationInfo(1, 5, 10, ONCE_BACKWARDS));
            put(ReaperEyes.FLASHING, new AnimationInfo(2, 2, 3, LOOP_FORWARDS));
            put(ReaperEyes.SMALL_EYES, new AnimationInfo(3, 2, 3, LOOP_FORWARDS));
         }
      });
      return new ReaperEyes(
            Images.REAPER_EYES, 200, 52, 4, 5,
            aniInfo, x, y, player);
   }

   public static AnimatedMouth GetAnimatedMouth(int x, int y) {
      HashMap<String, AnimationInfo> aniInfo = new HashMap<>();
      aniInfo.putAll(new HashMap<String, AnimationInfo>() {
         {
            put(AnimatedMouth.IDLE, new AnimationInfo(0, 1, 10, LOOP_FORWARDS));
            put(AnimatedMouth.DAMAGE, new AnimationInfo(1, 2, 3, LOOP_FORWARDS));
            put(AnimatedMouth.OPEN_UP, new AnimationInfo(2, 8, 10, ONCE_FORWARDS));
            put(AnimatedMouth.CLOSE, new AnimationInfo(2, 8, 10, ONCE_BACKWARDS));
         }
      });
      return new AnimatedMouth(
            Images.REAPER_MOUTH, 81, 58, 3, 8,
            aniInfo, x, y);
   }

   public static ComplexAnimation GetPlayerFlyAnimation(float x, float y) {
      HashMap<String, AnimationInfo> aniInfo = new HashMap<>();
      aniInfo.putAll(new HashMap<String, AnimationInfo>() {
         {
            put(PlaneAction.IDLE.toString(), new AnimationInfo(0, 1, 3, LOOP_FORWARDS));
            put(PlaneAction.FLYING_LEFT.toString(), new AnimationInfo(1, 3, 3, ONCE_FORWARDS));
            put(PlaneAction.FLYING_RIGHT.toString(), new AnimationInfo(2, 3, 3, ONCE_FORWARDS));
            put(PlaneAction.TELEPORTING_RIGHT.toString(), new AnimationInfo(3, 1, 3, ONCE_FORWARDS));
            put(PlaneAction.TELEPORTING_LEFT.toString(), new AnimationInfo(4, 1, 3, ONCE_FORWARDS));
            put(PlaneAction.TAKING_COLLISION_DAMAGE.toString(), new AnimationInfo(5, 6, 3, ONCE_FORWARDS));
            put(PlaneAction.TAKING_SHOOT_DAMAGE.toString(), new AnimationInfo(6, 4, 3, ONCE_FORWARDS));
         }
      });
      return new ComplexAnimation(
            Images.SHIP_SPRITES, aniInfo, PlaneAction.IDLE.toString(),
            30, 30, 7, 6, x, y);
   }

}
