package entities.bossmode.rudinger1;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import entities.bossmode.AnimatedComponent;
import entities.bossmode.AnimationInfo;
import entities.bossmode.BossActionHandler;
import entities.bossmode.IBoss;
import entities.bossmode.IBossPart;
import entities.bossmode.PlayerBoss;
import main_classes.Game;
import utils.LoadSave;

public class Rudinger1 implements IBoss {
   private int HP = 2000;
   private float mainBodyXPos;
   private float mainBodyYPos;
   private BufferedImage mainBodyImg;
   private ReaperEyes eyes;
   private AnimatedComponent mouth;
   private final Point mainGunPoint = new Point(Game.GAME_DEFAULT_WIDTH/2, 350);
   private final Point heartDockingPoint = new Point(Game.GAME_DEFAULT_WIDTH/2 - 1, 240);
   private IBossPart horizontalLazer;
   private IBossPart verticalLazer;
   private IBossPart heatSeekingLazer;
   private IBossPart machineHeart;
   private BossActionHandler actionHandler;

   // Actions
   private final int IDLE = 0;
   private final int ATTACK1 = 1;
   private final int ATTACK2 = 2;
   private final int ATTACK3 = 3;
   private final int ATTACK4 = 4;

   // Global timer
   private int tick = 0;
   private int currentAction = 0;


   public Rudinger1(PlayerBoss player) {
      this.constructMainBody();
      this.constructAnimatedComponents(player);
      this.constructVulnerableArea();
      this.constructBossParts(player);
      //this.loadProjectilePatterns();
      actionHandler = new BossActionHandler();
      this.registerActions();
      this.actionHandler.startAction(currentAction);
   }

   private void constructAnimatedComponents(PlayerBoss player) {
      // Reaper eyes
      BufferedImage eyesImg = LoadSave.getBossSprite("boss1_eyes.png");
      float xPos = 230;
      float yPos = 100;
      ArrayList<AnimationInfo> aniInfo = new ArrayList<>(Arrays.asList(
         new AnimationInfo("IDLE", 0, 2, 3, 0, false),
         new AnimationInfo("SHUT_DOWN", 1, 5, 10, 4, false),
         new AnimationInfo("BOOT_UP", 1, 5, 10, 0, true)));
      this.eyes = new ReaperEyes(
         eyesImg, 200, 52, 2, 5, 
         aniInfo, xPos, yPos, player);
      
      // Reaper mouth
      BufferedImage mouthImg = LoadSave.getBossSprite("boss1_mouth.png");
      float xPos1 = 402;
      float yPos1 = 145;
      ArrayList<AnimationInfo> aniInfo1 = new ArrayList<>(Arrays.asList(
         new AnimationInfo("IDLE", 0, 1, 10, 0, false),
         new AnimationInfo("DAMAGE", 1, 2, 3, 0, false),
         new AnimationInfo("OPEN_UP", 2, 8, 10, 7, false),
         new AnimationInfo("CLOSE", 2, 8, 10, 0, true)));
      this.mouth = new AnimatedComponent(
         mouthImg, 81, 58, 3, 8, 
         aniInfo1, xPos1, yPos1);
   }

   private void constructMainBody() {
      this.mainBodyImg = LoadSave.getBossSprite("boss1_body.png");
      this.mainBodyXPos = 0;
      this.mainBodyYPos = - 50;
   }

   // The vulnerable part is the boss's mouth. 
   // This part also houses the machine heart.
   // It has several different states.
   private void constructVulnerableArea() {
      

   }

   private void constructBossParts(PlayerBoss player) {
      // ATTACK1: Two rotating lazers. One vertical and one horizontal.
      BufferedImage lazerImg = LoadSave.getBossSprite(LoadSave.ROTATING_LAZER_SPRITE);
      int width1 = 30;
      int height1 = 1300;
      Rectangle2D.Float hitbox1 = new Rectangle2D.Float(
         (float) mainGunPoint.getX() - width1/2,
         (float) mainGunPoint.getY() - height1/2, 
         width1, height1);
      this.verticalLazer = new RotatingLazer(
         hitbox1, lazerImg, 2, 3, 10, 433, 0.0, true);
      this.horizontalLazer = new RotatingLazer(
         hitbox1, lazerImg, 2, 3, 10, 433, Math.PI/2, false);
      
      // ATTACK2: A heatseeking lazer.
      BufferedImage lazerImg2 = LoadSave.getBossSprite(LoadSave.HEATSEEKING_LAZER_SPRITE);
      int width2 = 90;
      int height2 = 660;
      Rectangle2D.Float hitbox2 = new Rectangle2D.Float(
         (float) mainGunPoint.getX() - width2/2, 
         (float) mainGunPoint.getY(), 
         width2, height2);
      this.heatSeekingLazer = new HeatSeekingLazer(
         hitbox2, lazerImg2, 3, 4, 30, 220, player, mainGunPoint);
      
      // ATTACK3: The machine heart.
      BufferedImage heartImg = LoadSave.getBossSprite(LoadSave.MACHINE_HEART_SPRITE);
      int width3 = 100;
      int height3 = 100;
      Rectangle2D.Float hitbox3 = new Rectangle2D.Float(
         (float) heartDockingPoint.getX() - width3/2, 
         (float) heartDockingPoint.getY() - height3/2, 
         width3, height3);
      this.machineHeart = new MachineHeart(
         hitbox3, heartImg, 2, 2, 40, 40, player, heartDockingPoint);

   }

   private void registerActions() {
      // The boss will loop through these actions sequentially.
      // When at the end -> it goes back to start.

      actionHandler.registerAction(
         IDLE, 
         100, 
         new ArrayList<>());

      // actionHandler.registerAction(
      //    ATTACK1, 
      //    500, 
      //    new ArrayList<IBossPart>(Arrays.asList(
      //       horizontalLazer, verticalLazer)));

      // actionHandler.registerAction(
      //    IDLE, 
      //    40, 
      //    new ArrayList<>());

      // actionHandler.registerAction(
      //    ATTACK2, 
      //    540, 
      //    new ArrayList<IBossPart>(Arrays.asList(
      //       heatSeekingLazer)));

      actionHandler.registerAction(
         ATTACK3, 
         new ArrayList<IBossPart>(Arrays.asList(
            machineHeart)));
      
   }

   @Override
   public void update() {
      checkIfAbortAction();
      updateGlobalCycle();
      updateCurrentAction();
      updateAnimatedComponents();
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

   private void updateAnimatedComponents() {
      this.eyes.update();
      this.mouth.updateAnimations();
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
   // In such case, the globalTick is forwarded to the end of the action.
   private void checkIfAbortAction() {
      if (this.actionHandler.shouldAbort(currentAction)) {
         this.goToNextAction();
      }
   }

   @Override
   public ArrayList<IBossPart> getBossParts() {
      ArrayList<IBossPart> bossParts = new ArrayList<>();
      bossParts.add(verticalLazer);
      bossParts.add(horizontalLazer);
      bossParts.add(heatSeekingLazer);
      bossParts.add(machineHeart);
      return bossParts;
   }

   @Override
   public void draw(Graphics g) {
      drawEyeAnimations(g);
      drawStaticBossImages(g);
      drawMouthAnimations(g);
      // Draw all animations pertaining to individual bossParts
      this.actionHandler.draw(g);
   }

   private void drawMouthAnimations(Graphics g) {
      int action = this.actionHandler.getName(currentAction);
      if (action == ATTACK3) {
         if (actionHandler.isActionCharging(currentAction)) {
            mouth.setAnimation(2);
         }
         else if (actionHandler.isActionCoolingDown(currentAction)) {
            mouth.setAnimation(3);
         }
      }
      else {
         mouth.setAnimation(0);
      }
      this.mouth.draw(g);
   }

   private void drawEyeAnimations(Graphics g) {
      int action = this.actionHandler.getName(currentAction);
      if (action == ATTACK3) {
         if (actionHandler.isActionCharging(currentAction)) {
            eyes.setAnimation(1);
         }
         else if (actionHandler.isActionCoolingDown(currentAction)) {
            eyes.setAnimation(2);
         }
      }
      else {
         eyes.setAnimation(0);
      }
      this.eyes.draw(g);
   }

   private void drawStaticBossImages(Graphics g) {
      g.drawImage(
         mainBodyImg, 
         (int) (mainBodyXPos * Game.SCALE),
         (int) (mainBodyYPos * Game.SCALE),
         (int) (mainBodyImg.getWidth() * 3 * Game.SCALE),
         (int) (mainBodyImg.getHeight() * 3 * Game.SCALE), null);
   }

   @Override
   public int getXPos() {
      return (int) this.mainBodyXPos;
   }

   @Override
   public int getYPos() {
      return (int) this.mainBodyYPos;
   }

   
}
