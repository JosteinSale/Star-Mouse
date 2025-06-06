package entities.bossmode.rudinger1;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;

import entities.bossmode.AnimatedComponentFactory;
import entities.bossmode.BossActionHandler;
import entities.bossmode.DefaultBossPart;
import entities.bossmode.IBoss;
import entities.bossmode.IBossPart;
import entities.bossmode.PlayerBoss;
import entities.bossmode.VulnerableComponent;
import main_classes.Game;
import projectiles.ProjectileHandler2;
import projectiles.shootPatterns.FanPattern;
import projectiles.shootPatterns.HeatSeekingPattern;
import ui.BossHealthDisplay;
import utils.Images;

public class Rudinger1 implements IBoss {
   public BossActionHandler actionHandler;
   private AnimatedComponentFactory animationFactory;
   private BossHealthDisplay healthDisplay;
   public boolean visible;

   // Coordinates
   public float mainBodyXPos;
   public float mainBodyYPos;
   private final Point mainGunPoint = new Point(Game.GAME_DEFAULT_WIDTH / 2, 350);
   private final Point heartDockingPoint = new Point(Game.GAME_DEFAULT_WIDTH / 2 - 1, 240);

   // Animations
   public ReaperEyes eyes;
   public AnimatedMouth mouth;

   // IBossParts
   public RotatingLazer verticalLazer; // Will also draw charge-animation
   private RotatingLazer horizontalLazer;
   private HeatSeekingLazer heatSeekingLazer;
   private MachineHeart machineHeart;
   private VulnerableComponent vulnerableComponent;

   // ShootPatterns
   private FanPattern fanPattern1;
   private FanPattern fanPattern2;
   private HeatSeekingPattern heatSeekingPattern1;
   private HeatSeekingPattern heatSeekingPattern2;

   // Actions
   private final int IDLE = 0;
   private final int SHOOT_ATTACK1 = 1;
   private final int ROTATING_LAZER = 2;
   private final int HEATSEEKING_LAZER = 3;
   private final int SHOOT_ATTACK2 = 4;
   private final int MACHINE_HEART = 5;

   // Global timer
   private int tick = 0;
   public int currentAction = 0;

   // Damage
   private int maxHP = 3000;
   private int HP = maxHP;

   public Rudinger1(PlayerBoss player, ProjectileHandler2 projectileHandler,
         AnimatedComponentFactory animationFactory) {
      this.animationFactory = animationFactory;
      this.actionHandler = new BossActionHandler();
      this.healthDisplay = new BossHealthDisplay("Grand Reaper", maxHP);
      this.constructMainBody();
      this.constructAnimatedComponents(player);
      this.constructBossParts(player);
      this.constructShootPatterns(projectileHandler, player);
      this.registerActions();
      this.actionHandler.startAction(currentAction);
   }

   private void constructShootPatterns(ProjectileHandler2 projectileHandler, PlayerBoss player) {
      Point leftGunPoint = new Point(215, 285);
      Point rightGunPoint = new Point(825, 285);

      // Two fanPatterns, one for each wing cannon.
      this.fanPattern1 = new FanPattern(
            projectileHandler, leftGunPoint, animationFactory,
            120, 0, 200);

      this.fanPattern2 = new FanPattern(
            projectileHandler, rightGunPoint, animationFactory,
            120, 100, 200);

      // Two heatSeekingPatterns, one for each wing cannon.
      this.heatSeekingPattern1 = new HeatSeekingPattern(
            projectileHandler, leftGunPoint, animationFactory, player,
            60, 0, 60);

      this.heatSeekingPattern2 = new HeatSeekingPattern(
            projectileHandler, rightGunPoint, animationFactory, player,
            60, 30, 60);
   }

   private void constructAnimatedComponents(PlayerBoss player) {
      // Reaper eyes
      this.eyes = animationFactory.getReaperEyes(230, 100, player);

      // Reaper mouth
      this.mouth = animationFactory.getAnimatedMouth(403, 145);
   }

   private void constructMainBody() {
      this.mainBodyXPos = 0;
      this.mainBodyYPos = -50;
   }

   private void constructBossParts(PlayerBoss player) {
      // Two rotating lazers. One vertical and one horizontal.

      int width1 = 30;
      int height1 = 1300;
      Rectangle2D.Float hitbox1 = new Rectangle2D.Float(
            (float) mainGunPoint.getX() - width1 / 2,
            (float) mainGunPoint.getY() - height1 / 2,
            width1, height1);
      this.verticalLazer = new RotatingLazer(
            hitbox1, Images.ROTATING_LAZER_SPRITE,
            2, 3, 10, 433, 0.0, true);
      this.horizontalLazer = new RotatingLazer(
            hitbox1, Images.ROTATING_LAZER_SPRITE,
            2, 3, 10, 433, Math.PI / 2, false);

      // A heatseeking lazer.

      int width2 = 90;
      int height2 = 660;
      Rectangle2D.Float hitbox2 = new Rectangle2D.Float(
            (float) mainGunPoint.getX() - width2 / 2,
            (float) mainGunPoint.getY(),
            width2, height2);
      this.heatSeekingLazer = new HeatSeekingLazer(
            hitbox2, Images.HEATSEEKING_LAZER_SPRITE,
            3, 4, 30, 220, player, mainGunPoint);

      // The machine heart.

      int width3 = 100;
      int height3 = 100;
      Rectangle2D.Float hitbox3 = new Rectangle2D.Float(
            (float) heartDockingPoint.getX() - width3 / 2,
            (float) heartDockingPoint.getY() - height3 / 2,
            width3, height3);
      this.machineHeart = new MachineHeart(
            hitbox3, Images.MACHINE_HEART_SPRITE,
            2, 2, 40, 40, player, heartDockingPoint);

      // The vulnerable area (is not part of an attack, doesn't have animations)
      // It's placed just below the machine heart.

      int width4 = 120;
      int height4 = 40;
      Rectangle2D.Float hitbox4 = new Rectangle2D.Float(
            (float) heartDockingPoint.getX() - width4 / 2,
            (float) heartDockingPoint.getY() + 40,
            width4, height4);
      this.vulnerableComponent = new VulnerableComponent(
            hitbox4, Images.EMPTY_IMAGE,
            0, 0, 0, 0, this);

   }

   private void registerActions() {
      // The boss will loop through these actions sequentially.
      // When at the end -> it goes back to start.

      actionHandler.registerAction(
            IDLE,
            160,
            new ArrayList<>(),
            new ArrayList<>());

      actionHandler.registerAction(
            SHOOT_ATTACK1,
            700,
            new ArrayList<>(),
            new ArrayList<>(Arrays.asList(
                  fanPattern1, fanPattern2)));

      actionHandler.registerAction(
            IDLE,
            100,
            new ArrayList<>(),
            new ArrayList<>());

      actionHandler.registerAction(
            ROTATING_LAZER,
            600,
            new ArrayList<DefaultBossPart>(Arrays.asList(
                  horizontalLazer, verticalLazer)),
            new ArrayList<>());

      actionHandler.registerAction(
            IDLE,
            100,
            new ArrayList<>(),
            new ArrayList<>());

      actionHandler.registerAction(
            HEATSEEKING_LAZER,
            720,
            new ArrayList<DefaultBossPart>(Arrays.asList(
                  heatSeekingLazer)),
            new ArrayList<>());

      actionHandler.registerAction(
            SHOOT_ATTACK2,
            500,
            new ArrayList<>(),
            new ArrayList<>(Arrays.asList(
                  heatSeekingPattern1, heatSeekingPattern2)));

      actionHandler.registerAction(
            IDLE,
            100,
            new ArrayList<>(),
            new ArrayList<>());

      actionHandler.registerAction(
            MACHINE_HEART,
            new ArrayList<DefaultBossPart>(Arrays.asList(
                  machineHeart)),
            new ArrayList<>());

   }

   @Override
   public void update() {
      checkIfAbortAction();
      updateGlobalCycle();
      updateCurrentAction();
      updateAnimatedComponents();
      healthDisplay.update();
   }

   private void updateGlobalCycle() {
      if (!actionHandler.hasDuration(currentAction)) {
         return;
      }
      this.tick++;
      if (this.tick >= this.actionHandler.getDuration(currentAction)) {
         this.tick = 0;
         goToNextAction();
      }
   }

   private void goToNextAction() {
      this.actionHandler.finishAction(currentAction);
      this.currentAction = (currentAction + 1) % this.actionHandler.amountOfActions();
      this.actionHandler.startAction(currentAction);
   }

   private void updateCurrentAction() {
      this.actionHandler.updateAction(currentAction);
   }

   // An active action can choose to abort its attack for whatever reason.
   // In such case, we go to the next action.
   private void checkIfAbortAction() {
      if (this.actionHandler.shouldAbort(currentAction)) {
         this.goToNextAction();
      }
   }

   private void updateAnimatedComponents() {
      setEyeAnimations();
      setMouthAnimations();

      eyes.update();
      mouth.updateAnimations();
   }

   private void setMouthAnimations() {
      int action = this.actionHandler.getName(currentAction);
      if (action == MACHINE_HEART) {
         if (actionHandler.isActionCharging(currentAction)) {
            mouth.setAnimation(2);
         } else if (actionHandler.isActionCoolingDown(currentAction)) {
            mouth.setAnimation(3);
         }
      } else {
         mouth.setAnimation(0);
      }
   }

   private void setEyeAnimations() {
      int action = this.actionHandler.getName(currentAction);
      if (action == MACHINE_HEART) {
         if (actionHandler.isActionCharging(currentAction)) {
            eyes.setAnimation(1);
         } else if (actionHandler.isActionCoolingDown(currentAction)) {
            eyes.setAnimation(2);
         }
      } else if (action == SHOOT_ATTACK1) {
         if (!actionHandler.isActionCharging(currentAction)) {
            eyes.setAnimation(3);
         }
      } else if (action == HEATSEEKING_LAZER) {
         eyes.setAnimation(4);
      } else {
         eyes.setAnimation(0);
      }
   }

   @Override
   public ArrayList<IBossPart> getBossParts() {
      ArrayList<IBossPart> bossParts = new ArrayList<>();
      bossParts.add(verticalLazer);
      bossParts.add(horizontalLazer);
      bossParts.add(heatSeekingLazer);
      bossParts.add(machineHeart);
      bossParts.add(vulnerableComponent);
      return bossParts;
   }

   @Override
   public int getXPos() {
      return (int) this.mainBodyXPos;
   }

   @Override
   public int getYPos() {
      return (int) this.mainBodyYPos;
   }

   @Override
   public void takeDamage(int damage, boolean overrideInvincibility) {
      int action = actionHandler.getName(currentAction);
      if (action == MACHINE_HEART && !overrideInvincibility) {
         // Don't take damage during docking
         if (actionHandler.isActionCharging(currentAction) ||
               actionHandler.isActionCoolingDown(currentAction)) {
            return;
         }
      }
      this.HP -= damage;
      this.mouth.damageAnimation();
      this.healthDisplay.setHP(HP);
      this.healthDisplay.setBlinking(true);
   }

   @Override
   public void reset() {
      this.HP = maxHP;
      this.tick = 0;
      this.actionHandler.finishAction(currentAction);
      this.currentAction = 0;
      this.healthDisplay.setHP(HP);
      this.healthDisplay.setBlinking(false);
   }

   @Override
   public void setVisible(boolean visible) {
      this.visible = visible;
   }

   @Override
   public boolean isDead() {
      return this.HP <= 0;
   }

   @Override
   public void flush() {

   }

   public BossHealthDisplay getHealthDisplay() {
      return this.healthDisplay;
   }

}
