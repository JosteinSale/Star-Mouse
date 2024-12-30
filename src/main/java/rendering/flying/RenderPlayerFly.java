package rendering.flying;

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
   }

   @Override
   public void draw(Graphics g) {
      if (player.visible) {
         // Teleport shadows
         if (game.teleportIsPressed) {
            // g.setColor(Color.LIGHT_GRAY);
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

}
