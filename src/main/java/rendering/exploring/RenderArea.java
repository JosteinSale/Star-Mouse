package rendering.exploring;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.exploring.PlayerExp;
import gamestates.exploring.Area;
import main_classes.Game;
import rendering.SwingRender;
import utils.DrawUtils;
import utils.HelpMethods2;
import utils.ResourceLoader;

import static utils.Constants.Exploring.Sprites.STANDARD_SPRITE_HEIGHT;
import static utils.Constants.Exploring.Sprites.STANDARD_SPRITE_WIDTH;

public class RenderArea implements SwingRender {
   private Game game;
   private Area area;
   private RenderMap1 rMap;
   private RenderNPCs rNPCs;

   // Player
   private static int playerSpriteWidth;
   private static int playerSpriteHeight;
   private static ArrayList<BufferedImage[][]> playerSprites;
   private final int NORMAL_SPRITE = 0;
   private final int NAKED_SPRITE = 1;
   private final int SAD_SPRITE = 2;

   /** Add player sprites according to the indexes given at the top */
   static {
      playerSprites = new ArrayList<>();
      BufferedImage[][] normalSprites = HelpMethods2.GetUnscaled2DAnimationArray(
            ResourceLoader.getExpImageSprite(ResourceLoader.PLAYER_EXP_SPRITES),
            6, 4, STANDARD_SPRITE_WIDTH, STANDARD_SPRITE_HEIGHT);
      BufferedImage[][] nakedSprites = HelpMethods2.GetUnscaled2DAnimationArray(
            ResourceLoader.getExpImageSprite(ResourceLoader.PLAYER_EXP_SPRITES_NAKED),
            5, 4, STANDARD_SPRITE_WIDTH, STANDARD_SPRITE_HEIGHT);
      BufferedImage[][] sadSprites = HelpMethods2.GetUnscaled2DAnimationArray(
            ResourceLoader.getExpImageSprite(ResourceLoader.PLAYER_EXP_SPRITES_SAD),
            5, 4, STANDARD_SPRITE_WIDTH, STANDARD_SPRITE_HEIGHT);
      playerSprites.add(normalSprites);
      playerSprites.add(nakedSprites);
      playerSprites.add(sadSprites);

      adjustPlayerImgSizes();
   }

   public RenderArea(Game game, Area area, int levelIndex, int areaIndex) {
      this.game = game;
      this.area = area;
      this.rMap = new RenderMap1(area.getMapManager(), levelIndex, areaIndex);
      this.rNPCs = new RenderNPCs(game, area.getNpcManager());
   }

   private static void adjustPlayerImgSizes() {
      playerSpriteWidth = STANDARD_SPRITE_WIDTH * 3;
      playerSpriteHeight = STANDARD_SPRITE_HEIGHT * 3;
   }

   @Override
   public void draw(Graphics g) {
      int xLevelOffset = area.getMapManager().xLevelOffset;
      int yLevelOffset = area.getMapManager().yLevelOffset;

      // Maps
      rMap.drawLandscape(g);
      rMap.drawBackground(g);

      // Entities
      rNPCs.drawBgNpcs(g, xLevelOffset, yLevelOffset);
      this.drawPlayer(g, xLevelOffset, yLevelOffset);

      // Foreground
      rNPCs.drawFgNpcs(g, xLevelOffset, yLevelOffset);
      rMap.drawForeground(g);

      // Hitboxes
      // drawHitboxes(g, mapManager.xLevelOffset, mapManager.yLevelOffset);
   }

   public void drawPlayer(Graphics g, int xLevelOffset, int yLevelOffset) {
      PlayerExp player = area.getPlayer();
      if (player.visible) {
         // drawShadow(g, xLevelOffset, yLevelOffset);
         DrawUtils.drawImage(
               g, playerSprites.get(PlayerExp.CURRENT_SPRITE_SHEET)[player.playerAction][player.aniIndex],
               (int) (player.hitbox.x - 113 - xLevelOffset),
               (int) (player.hitbox.y - 135 - yLevelOffset),
               playerSpriteWidth, playerSpriteHeight);
      }
   }

   private void drawHitboxes(Graphics g, int xLevelOffset, int yLevelOffset) {
      // g.setColor(Color.RED);
      // player.drawHitbox(g, xLevelOffset, yLevelOffset);

      // for (InteractableObject ob : interactableObject) {
      // ob.drawHitbox(g, xLevelOffset, yLevelOffset);
      // }
      // for (Door door : doors) {
      // door.drawHitbox(g, xLevelOffset, yLevelOffset);
      // }
      // for (Portal portal : portals) {
      // portal.drawHitbox(g, xLevelOffset, yLevelOffset);
      // }
      // npcManager.drawHitboxes(g, xLevelOffset, yLevelOffset);

      // for (AutomaticTrigger trigger : automaticTriggers) {
      // trigger.drawHitbox(g, xLevelOffset, yLevelOffset);
      // }
   }

   @Override
   public void dispose() {
      this.rMap.dispose();
   }

}
