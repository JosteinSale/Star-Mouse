package rendering;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import entities.exploring.Gard;
import entities.exploring.NPC;
import entities.exploring.NpcManager;
import entities.exploring.Oliver;
import main_classes.Game;
import utils.HelpMethods2;
import utils.ResourceLoader;

import static utils.Constants.Exploring.Sprites.STANDARD_SPRITE_HEIGHT;
import static utils.Constants.Exploring.Sprites.STANDARD_SPRITE_WIDTH;

public class RenderNPCs implements SwingRender {
   private Game game;
   private NpcManager npcManager;
   private static BufferedImage[] standardNpcSprites;
   private static BufferedImage gardSprites;
   private static BufferedImage oliverSprites;
   private static BufferedImage[][] gardAnimations;
   private static BufferedImage[][] oliverAnimations;
   static {
      standardNpcSprites = HelpMethods2.GetSimpleAnimationArray(
            ResourceLoader.getExpImageSprite(ResourceLoader.STANDARD_NPC_SPRITES), 16, 90, 60);
      gardSprites = ResourceLoader.getExpImageSprite(ResourceLoader.GARD_SPRITES);
      oliverSprites = ResourceLoader.getExpImageSprite(ResourceLoader.OLIVER_SPRITES);
      gardAnimations = HelpMethods2.GetAnimationArray(gardSprites, 3, 4, STANDARD_SPRITE_WIDTH, STANDARD_SPRITE_HEIGHT);
      oliverAnimations = HelpMethods2.GetAnimationArray(oliverSprites, 5, 4, STANDARD_SPRITE_WIDTH,
            STANDARD_SPRITE_HEIGHT);
   }
   private int drawWidth;
   private int drawHeight;

   public RenderNPCs(Game game, NpcManager npcManager) {
      this.game = game;
      this.npcManager = npcManager;
      this.drawWidth = (int) (STANDARD_SPRITE_WIDTH * 3 * Game.SCALE);
      this.drawHeight = (int) (STANDARD_SPRITE_HEIGHT * 3 * Game.SCALE);
   }

   public void drawFgNpcs(Graphics g, int xLevelOffset, int yLevelOffset) {
      for (NPC npc : npcManager.allNpcs) {
         if (npc.inForeground()) {
            this.drawNpc(g, npc, xLevelOffset, yLevelOffset);
         }
      }
   }

   public void drawBgNpcs(Graphics g, int xLevelOffset, int yLevelOffset) {
      for (NPC npc : npcManager.allNpcs) {
         if (!npc.inForeground()) {
            this.drawNpc(g, npc, xLevelOffset, yLevelOffset);
         }
      }
   }

   private void drawNpc(Graphics g, NPC npc, int xLevelOffset, int yLevelOffset) {
      g.drawImage(
            this.getSprite(npc),
            (int) ((npc.getHitbox().x - npc.getXDrawOffset() - xLevelOffset) * Game.SCALE),
            (int) ((npc.getHitbox().y - npc.getYDrawOffset() - yLevelOffset) * Game.SCALE),
            drawWidth, drawHeight,
            null);
   }

   private BufferedImage getSprite(NPC npc) {
      if (npc instanceof Oliver) {
         return oliverAnimations[npc.getAction()][npc.getAniIndex()];
      } else if (npc instanceof Gard) {
         return gardAnimations[npc.getAction()][npc.getAniIndex()];
      } else {
         return this.getStandardSprite(npc.getName());
      }
   }

   /** Returns the index in the sprite-array for the npc-name */
   private BufferedImage getStandardSprite(String name) {
      int index = switch (name) {
         case "Shady pilot" -> 0;
         case "Lance" -> 1;
         case "Nina" -> 2;
         case "Charlotte" -> 3;
         case "Russel" -> 4;
         case "Emma" -> 5;
         case "Mechanic" -> 6;
         case "Frida" -> 7;
         case "Nathan" -> 8;
         case "Feno" -> 10;
         case "Lt.Red" -> 11;
         case "Skye" -> 12;
         case "Gard" -> 13;
         case "Zack" -> 14;
         case "Acolyte" -> 15;
         default -> throw new IllegalArgumentException(
               "No characterIndex available for '" + name + "'");
      };
      return standardNpcSprites[index];
   }

   public void drawHitboxes(Graphics g, int xLevelOffset, int yLevelOffset) {
      for (NPC npc : npcManager.allNpcs) {
         this.drawHitbox(g, npc, xLevelOffset, yLevelOffset);
      }
   }

   private void drawHitbox(Graphics g, NPC npc, int xLevelOffset, int yLevelOffset) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'drawHitbox'");
   }

   @Override
   public void draw(Graphics g) {

   }

   @Override
   public void dispose() {

   }

}
