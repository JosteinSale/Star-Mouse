package rendering.flying;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import entities.flying.PlayerFly;
import main_classes.Game;
import rendering.SwingRender;
import utils.DrawUtils;
import utils.HelpMethods2;
import utils.ResourceLoader;

import static utils.Constants.Flying.SpriteSizes.SHIP_SPRITE_HEIGHT;
import static utils.Constants.Flying.SpriteSizes.SHIP_SPRITE_WIDTH;

import static utils.Constants.Flying.ActionConstants.*;

public class RenderPlayerFly implements SwingRender {
   private Game game;
   private PlayerFly player;
   private BufferedImage[][] animations;
   private BufferedImage[] flameAnimations;
   private BufferedImage tpShadowImg;

   // Status display
   private BufferedImage bombImg;
   private BufferedImage enemyCounterImg;
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
   private Color HPbarBgColor = new Color(97, 0, 15, 180);

   public RenderPlayerFly(Game game, PlayerFly player) {
      this.game = game;
      this.player = player;
      this.loadImages();
   }

   private void loadImages() {
      // Teleport shadow
      this.tpShadowImg = ResourceLoader.getFlyImageSprite(
            ResourceLoader.TELEPORT_SHADOW);

      // Engine flame
      this.flameAnimations = HelpMethods2.GetSimpleAnimationArray(
            ResourceLoader.getFlyImageSprite(ResourceLoader.SHIP_FLAME_SPRITES),
            2, 15, 15);

      // Player Ship
      BufferedImage img = ResourceLoader.getFlyImageSprite(
            ResourceLoader.SHIP_SPRITES);
      this.animations = HelpMethods2.GetAnimationArray(
            img, 7, 6, SHIP_SPRITE_WIDTH, SHIP_SPRITE_HEIGHT);

      // Status display
      this.bombImg = ResourceLoader.getFlyImageSprite(
            ResourceLoader.BOMB_SPRITE);
      this.enemyCounterImg = ResourceLoader.getFlyImageSprite(
            ResourceLoader.ENEMYCOUNTER_SPRITE);
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
         DrawUtils.drawImage(
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

      Color hpBarColor;
      if (player.statusDisplay.blinkTimer % 4 == 0) {
         hpBarColor = Color.RED;
      } else {
         hpBarColor = Color.WHITE;
      }
      DrawUtils.fillRect(
            g, hpBarColor,
            HPbarX - player.statusDisplay.HPbarCurW, HPbarY,
            player.statusDisplay.HPbarCurW, HPbarH);

      // Text
      DrawUtils.drawText(
            g, Color.WHITE, DrawUtils.infoFont,
            "x " + Integer.toString(player.statusDisplay.bombs),
            statusX + 60, bombY + 30);
      DrawUtils.drawText(
            g, Color.WHITE, DrawUtils.infoFont,
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
      DrawUtils.drawImage(
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
