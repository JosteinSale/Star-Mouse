package rendering.flying;

import java.awt.Graphics;

import entities.flying.PlayerFly;
import main_classes.Game;
import rendering.MyColor;
import rendering.MyImage;
import rendering.MySubImage;
import rendering.SwingRender;
import utils.DrawUtils;
import utils.HelpMethods2;
import utils.Images;

import static utils.Constants.Flying.SpriteSizes.SHIP_SPRITE_HEIGHT;
import static utils.Constants.Flying.SpriteSizes.SHIP_SPRITE_WIDTH;

import static utils.Constants.Flying.ActionConstants.*;

public class RenderPlayerFly implements SwingRender {
   private Game game;
   private PlayerFly player;
   private MySubImage[][] animations;
   private MySubImage[] flameAnimations;
   private MyImage tpShadowImg;

   // Status display
   private MyImage bombImg;
   private MyImage enemyCounterImg;
   private int statusX = Game.GAME_DEFAULT_WIDTH - 150;
   private int bombY = Game.GAME_DEFAULT_HEIGHT - 120;
   private int bombImgW;
   private int bombImgH;
   private int enemyCounterY = Game.GAME_DEFAULT_HEIGHT - 70;
   private int enemyCounterW;
   private int enemyCounterH;
   private int HPbarX = Game.GAME_DEFAULT_WIDTH - 180;
   private int HPbarY = Game.GAME_DEFAULT_HEIGHT - 50;
   private int HPbarH = 10;
   private MyColor HPbarBgColor = new MyColor(97, 0, 15, 180);

   public RenderPlayerFly(Game game, PlayerFly player) {
      this.game = game;
      this.player = player;
      this.loadImages(game.getImages());
   }

   private void loadImages(Images images) {
      // Teleport shadow
      this.tpShadowImg = images.getFlyImageSprite(
            Images.TELEPORT_SHADOW, true);

      // Engine flame
      this.flameAnimations = HelpMethods2.GetUnscaled1DAnimationArray(
            images.getFlyImageSprite(Images.SHIP_FLAME_SPRITES, true),
            2, 15, 15);

      // Player Ship
      MyImage shipImg = images.getFlyImageSprite(
            Images.SHIP_SPRITES, true);
      this.animations = HelpMethods2.GetUnscaled2DAnimationArray(
            shipImg, 7, 6, SHIP_SPRITE_WIDTH, SHIP_SPRITE_HEIGHT);

      // Status display
      this.bombImg = images.getFlyImageSprite(
            Images.BOMB_SPRITE, true);
      this.enemyCounterImg = images.getFlyImageSprite(
            Images.ENEMYCOUNTER_SPRITE, true);
      this.bombImgW = bombImg.getWidth() * 2;
      this.bombImgH = bombImg.getHeight() * 2;
      this.enemyCounterW = enemyCounterImg.getWidth() * 2;
      this.enemyCounterH = enemyCounterImg.getHeight() * 2;
   }

   @Override
   public void draw(Graphics g) {
      if (player.visible) {
         // Teleport shadows
         if (game.teleportIsPressed) {
            drawShadow(g, player.teleportDistance);
            drawShadow(g, -player.teleportDistance);
         }

         // Flame
         if (!game.downIsPressed) {
            drawFlame(g, player.hitbox.x + 3f, player.hitbox.y + player.hitbox.height);
         }

         // Player
         int actionIndex = player.planeAction;
         if ((player.teleportBuffer > 5) &&
               (player.planeAction != TAKING_COLLISION_DAMAGE)) {
            actionIndex = TELEPORTING_RIGHT;
            // Gives us a few extra frames with teleport-animation
         }
         DrawUtils.drawSubImage(
               g, animations[actionIndex][player.aniIndex],
               (int) (player.hitbox.x - 20), (int) (player.hitbox.y - 20),
               SHIP_SPRITE_WIDTH * 3, SHIP_SPRITE_HEIGHT * 3);

         drawStatusDisplay(g);
         // g.setColor(Color.RED);
         // this.drawHitbox(g, 0, 0);

         // Teleport hitbox
         /*
          * g.setColor(Color.RED);
          * g.drawRect(
          * (int) (teleportHitbox.x * Game.SCALE),
          * (int) (teleportHitbox.y * Game.SCALE),
          * (int) (teleportHitbox.width * Game.SCALE),
          * (int) (teleportHitbox.height * Game.SCALE));
          */
      }
   }

   private void drawStatusDisplay(Graphics g) {
      // Images
      DrawUtils.drawImage(
            g, bombImg,
            statusX, bombY,
            bombImgW, bombImgH);
      DrawUtils.drawImage(
            g, enemyCounterImg,
            statusX, enemyCounterY,
            enemyCounterW, enemyCounterH);

      // Healthbar
      DrawUtils.fillRect(
            g, HPbarBgColor,
            HPbarX - player.statusDisplay.HPbarMaxW, HPbarY,
            player.statusDisplay.HPbarMaxW, HPbarH);

      MyColor hpBarColor;
      if (player.statusDisplay.blinkTimer % 4 == 0) {
         hpBarColor = MyColor.RED;
      } else {
         hpBarColor = MyColor.WHITE;
      }
      DrawUtils.fillRect(
            g, hpBarColor,
            HPbarX - player.statusDisplay.HPbarCurW, HPbarY,
            player.statusDisplay.HPbarCurW, HPbarH);

      // Text
      DrawUtils.drawText(
            g, MyColor.WHITE, DrawUtils.infoFont,
            "x " + Integer.toString(player.statusDisplay.bombs),
            statusX + 60, bombY + 30);
      DrawUtils.drawText(
            g, MyColor.WHITE, DrawUtils.infoFont,
            "x " + Integer.toString(player.statusDisplay.killedEnemies),
            statusX + 60, enemyCounterY + 30);
   }

   private void drawShadow(Graphics g, int teleportDistance) {
      DrawUtils.drawImage(
            g, tpShadowImg,
            (int) (player.hitbox.x - 20 - teleportDistance),
            (int) (player.hitbox.y - 20),
            SHIP_SPRITE_WIDTH * 3,
            SHIP_SPRITE_HEIGHT * 3);
   }

   private void drawFlame(Graphics g, float x, float y) {
      DrawUtils.drawSubImage(
            g, flameAnimations[player.flame.aniIndex],
            (int) x, (int) y,
            45, 45);
   }

   @Override
   public void dispose() {

   }

   /**
    * Flying and BossMode uses different player-objects in their model,
    * but they have the same render. Use this method to set the correct model.
    * 
    * @param player
    */
   public void setPlayer(PlayerFly player) {
      this.player = player;
   }

}
