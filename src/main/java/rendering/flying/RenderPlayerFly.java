package rendering.flying;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import entities.flying.PlayerFly;
import main_classes.Game;
import rendering.SwingRender;
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
   private Font statusFont;
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
      this.tpShadowImg = ResourceLoader.getFlyImageSprite(ResourceLoader.TELEPORT_SHADOW);

      // Engine flame
      this.flameAnimations = HelpMethods2.GetSimpleAnimationArray(
            ResourceLoader.getFlyImageSprite(ResourceLoader.SHIP_FLAME_SPRITES),
            2, 15, 15);

      // Player Ship
      BufferedImage img = ResourceLoader.getFlyImageSprite(ResourceLoader.SHIP_SPRITES);
      this.animations = new BufferedImage[7][6];
      for (int j = 0; j < animations.length; j++) {
         for (int i = 0; i < animations[0].length; i++) {
            animations[j][i] = img.getSubimage(
                  i * SHIP_SPRITE_WIDTH,
                  j * SHIP_SPRITE_HEIGHT, SHIP_SPRITE_WIDTH, SHIP_SPRITE_HEIGHT);
         }
      }

      // Status display
      this.bombImg = ResourceLoader.getFlyImageSprite(ResourceLoader.BOMB_SPRITE);
      this.enemyCounterImg = ResourceLoader.getFlyImageSprite(ResourceLoader.ENEMYCOUNTER_SPRITE);
      this.bombImgW = bombImg.getWidth() * 2;
      this.bombImgH = bombImg.getHeight() * 2;
      this.enemyCounterW = enemyCounterImg.getWidth() * 2;
      this.enemyCounterH = enemyCounterImg.getHeight() * 2;
      this.statusFont = ResourceLoader.getInfoFont();
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
         if ((player.teleportBuffer > 5) && (player.planeAction != TAKING_COLLISION_DAMAGE)) {
            actionIndex = TELEPORTING_RIGHT; // Gives us a few extra frames with teleport-animation
         }

         g.drawImage(
               animations[actionIndex][player.aniIndex],
               (int) ((player.hitbox.x - 20) * Game.SCALE),
               (int) ((player.hitbox.y - 20) * Game.SCALE),
               (int) (SHIP_SPRITE_WIDTH * 3 * Game.SCALE),
               (int) (SHIP_SPRITE_HEIGHT * 3 * Game.SCALE), null);

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
      g.drawImage(
            bombImg,
            (int) (statusX * Game.SCALE), (int) (bombY * Game.SCALE),
            (int) (bombImgW * Game.SCALE), (int) (bombImgH * Game.SCALE), null);
      g.drawImage(
            enemyCounterImg,
            (int) (statusX * Game.SCALE), (int) (enemyCounterY * Game.SCALE),
            (int) (enemyCounterW * Game.SCALE), (int) (enemyCounterH * Game.SCALE), null);

      // Healthbar
      g.setColor(HPbarBgColor);
      g.fillRect(
            (int) ((HPbarX - player.statusDisplay.HPbarMaxW) * Game.SCALE), (int) (HPbarY * Game.SCALE),
            (int) (player.statusDisplay.HPbarMaxW * Game.SCALE), (int) (HPbarH * Game.SCALE));
      if (player.statusDisplay.blinkTimer % 4 == 0) {
         g.setColor(Color.RED);
      } else {
         g.setColor(Color.WHITE);
      }
      g.fillRect(
            (int) ((HPbarX - player.statusDisplay.HPbarCurW) * Game.SCALE), (int) (HPbarY * Game.SCALE),
            (int) (player.statusDisplay.HPbarCurW * Game.SCALE), (int) (HPbarH * Game.SCALE));

      // Text
      g.setFont(statusFont);
      g.setColor(Color.WHITE);
      g.drawString(
            "x " + Integer.toString(player.statusDisplay.bombs),
            (int) ((statusX + 60) * Game.SCALE),
            (int) ((bombY + 30) * Game.SCALE));
      g.drawString(
            "x " + Integer.toString(player.statusDisplay.killedEnemies),
            (int) ((statusX + 60) * Game.SCALE),
            (int) ((enemyCounterY + 30) * Game.SCALE));
   }

   private void drawShadow(Graphics g, int teleportDistance) {
      g.drawImage(
            tpShadowImg,
            (int) ((player.hitbox.x - 20 - teleportDistance) * Game.SCALE),
            (int) ((player.hitbox.y - 20) * Game.SCALE),
            (int) (SHIP_SPRITE_WIDTH * 3 * Game.SCALE),
            (int) (SHIP_SPRITE_HEIGHT * 3 * Game.SCALE), null);
   }

   private void drawFlame(Graphics g, float x, float y) {
      g.drawImage(
            flameAnimations[player.flame.aniIndex],
            (int) (x * Game.SCALE),
            (int) (y * Game.SCALE),
            (int) (15 * 3 * Game.SCALE),
            (int) (15 * 3 * Game.SCALE),
            null);
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
