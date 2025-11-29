package entities.bossmode.rudinger1;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;

import entities.bossmode.AnimatedComponent;
import entities.bossmode.AnimatedComponentFactory;
import entities.bossmode.BossActionHandler;
import entities.bossmode.DefaultBossPart;
import entities.bossmode.IBoss;
import entities.bossmode.IBossPart;
import entities.bossmode.PlayerBoss;
import entities.bossmode.VulnerableComponent;
import entities.flying.EntityFactory;
import entities.flying.pickupItems.PickupItem;
import main_classes.Game;
import projectiles.ProjectileHandler2;
import projectiles.shootPatterns.FanPattern;
import projectiles.shootPatterns.HeatSeekingPattern;
import static entities.flying.EntityFactory.TypeConstants.REPAIR;
import ui.BossHealthDisplay;

public class Rudinger1 implements IBoss {
   public BossActionHandler actionHandler;
   private AnimatedComponentFactory animationFactory;
   private EntityFactory entityFactory;
   private BossHealthDisplay healthDisplay;
   public boolean visible;
   private ArrayList<PickupItem> pickupItems;

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
   private final String IDLE1 = "IDLE1";
   private final String SHOOT_ATTACK1 = "SHOOT_ATTACK1";
   private final String IDLE2 = "IDLE2";
   private final String ROTATING_LAZER = "ROTATING_LAZER";
   private final String IDLE3 = "IDLE3";
   private final String HEATSEEKING_LAZER = "HEATSEEKING_LAZER";
   private final String SHOOT_ATTACK2 = "SHOOT_ATTACK2";
   private final String IDLE4 = "IDLE4";
   private final String MACHINE_HEART = "MACHINE_HEART";

   // Damage
   private int maxHP = 100;
   private int HP = maxHP;

   public Rudinger1(Game game, PlayerBoss player, ProjectileHandler2 projectileHandler,
         AnimatedComponentFactory animationFactory, ArrayList<PickupItem> pickupItems,
         EntityFactory entityFactory) {
      this.animationFactory = animationFactory;
      this.entityFactory = entityFactory;
      this.pickupItems = pickupItems;
      this.actionHandler = new BossActionHandler();
      this.healthDisplay = new BossHealthDisplay("Grand Reaper", maxHP);
      this.constructMainBody();
      this.constructAnimatedComponents(player);
      this.constructBossParts(game, player);
      this.constructShootPatterns(projectileHandler, player);
      this.registerActions();
      this.actionHandler.startAction(IDLE1);
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

   private void constructBossParts(Game game, PlayerBoss player) {
      // Two rotating lazers. One vertical and one horizontal.

      int width1 = 30;
      int height1 = 1300;
      Rectangle2D.Float hitbox1 = new Rectangle2D.Float(
            (float) mainGunPoint.getX() - width1 / 2,
            (float) mainGunPoint.getY() - height1 / 2,
            width1, height1);
      AnimatedComponent redChargeAnimation = animationFactory.getRedChargeAnimation(
            Game.GAME_DEFAULT_WIDTH / 2 - 150,
            Game.GAME_DEFAULT_HEIGHT / 2 - 170);
      this.verticalLazer = new RotatingLazer(
            hitbox1, animationFactory, 0.0, redChargeAnimation);
      this.horizontalLazer = new RotatingLazer(
            hitbox1, animationFactory, Math.PI / 2, null);

      // A heatseeking lazer.

      int width2 = 90;
      int height2 = 660;
      Rectangle2D.Float hitbox2 = new Rectangle2D.Float(
            (float) mainGunPoint.getX() - width2 / 2,
            (float) mainGunPoint.getY(),
            width2, height2);
      this.heatSeekingLazer = new HeatSeekingLazer(
            hitbox2, animationFactory, player, mainGunPoint);

      // The machine heart.

      int width3 = 100;
      int height3 = 100;
      Rectangle2D.Float hitbox3 = new Rectangle2D.Float(
            (float) heartDockingPoint.getX() - width3 / 2,
            (float) heartDockingPoint.getY() - height3 / 2,
            width3, height3);
      this.machineHeart = new MachineHeart(
            hitbox3, animationFactory, player, heartDockingPoint);

      // The vulnerable area (is not part of an attack, doesn't have animations)
      // It's placed just below the machine heart.

      int width4 = 120;
      int height4 = 40;
      Rectangle2D.Float hitbox4 = new Rectangle2D.Float(
            (float) heartDockingPoint.getX() - width4 / 2,
            (float) heartDockingPoint.getY() + 40,
            width4, height4);
      this.vulnerableComponent = new VulnerableComponent(
            game.getProgressValues().getLazerDmg(), hitbox4, this);

   }

   private void registerActions() {
      // The boss will loop through these actions sequentially.
      // When at the end -> it goes back to start.

      actionHandler.registerAction(
            IDLE1,
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
            IDLE2,
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
            IDLE3,
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
            IDLE4,
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
      actionHandler.update();
      updateAnimatedComponents();
      addRepairAtEndOfCycle();
      healthDisplay.update();
   }

   private void addRepairAtEndOfCycle() {
      // If the boss has just ended its last action in the cycle...
      if (actionHandler.getNameOfCurrentAction().equals(MACHINE_HEART)
            && actionHandler.shouldAbort()) {
         // ...then add a repair.
         String name = entityFactory.getName(REPAIR);
         PickupItem repair = entityFactory.getNewPickupItem(
               name, mainGunPoint.x - 30, mainGunPoint.y + 100);
         pickupItems.add(repair);
      }
   }

   private void updateAnimatedComponents() {
      setEyeAnimations();
      setMouthAnimations();

      eyes.update();
      mouth.updateAnimations();
   }

   private void setMouthAnimations() {
      String action = actionHandler.getNameOfCurrentAction();
      if (action.equals(MACHINE_HEART)) {
         if (actionHandler.isActionCharging()) {
            mouth.setAnimation(AnimatedMouth.OPEN_UP);
         } else if (actionHandler.isActionCoolingDown()) {
            mouth.setAnimation(AnimatedMouth.CLOSE);
         }
      } else {
         mouth.setAnimation(AnimatedMouth.IDLE);
      }
   }

   private void setEyeAnimations() {
      String action = actionHandler.getNameOfCurrentAction();
      if (action.equals(MACHINE_HEART)) {
         if (actionHandler.isActionCharging()) {
            eyes.setAnimation(ReaperEyes.SHUT_DOWN);
         } else if (actionHandler.isActionCoolingDown()) {
            eyes.setAnimation(ReaperEyes.BOOT_UP);
         }
      } else if (action.equals(SHOOT_ATTACK1)) {
         if (!actionHandler.isActionCharging()) {
            eyes.setAnimation(ReaperEyes.FLASHING);
         }
      } else if (action.equals(HEATSEEKING_LAZER)) {
         eyes.setAnimation(ReaperEyes.SMALL_EYES);
      } else {
         eyes.setAnimation(ReaperEyes.IDLE);
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
      String action = actionHandler.getNameOfCurrentAction();
      if (action.equals(MACHINE_HEART) && !overrideInvincibility) {
         // Don't take damage during mouth opening / closing
         if (actionHandler.isActionCharging() || actionHandler.isActionCoolingDown()) {
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
      this.actionHandler.reset();
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
