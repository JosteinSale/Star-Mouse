package gamestates.level_select;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import audio.AudioPlayer;
import main_classes.Game;
import utils.ResourceLoader;

public abstract class DefaultLevelLayout implements ILevelLayout {
   protected Game game;
   protected AudioPlayer audioPlayer;
   protected Font font;
   protected ArrayList<LevelSlot> levelSlots;
   protected BufferedImage layoutImg;
   protected BufferedImage cursorBox;
   protected int selectedIndex = 0;
   protected int layoutX = 150;
   protected int layoutY;
   protected int layoutW = 777;
   protected int layoutH;

   public DefaultLevelLayout(Game game) {
      this.game = game;
      this.audioPlayer = game.getAudioPlayer();
      this.cursorBox = ResourceLoader.getExpImageSprite(ResourceLoader.LEVEL_SELECT_BOX);
      this.font = ResourceLoader.getMenuFont();
      this.levelSlots = new ArrayList<>();
   }

   @Override
   public void draw(Graphics g) {
      // Layout
      g.drawImage(
         layoutImg, 
         (int) (layoutX * Game.SCALE), (int) (layoutY * Game.SCALE), 
         (int) (layoutW * Game.SCALE), (int) (layoutH * Game.SCALE), null);
      
      // Levels
      for (int i = 0; i < levelSlots.size(); i++) {
         LevelSlot slot = levelSlots.get(i);
         if (!slot.isEmpty) {
            slot.drawIcon(g);
            if (i == selectedIndex) {
               slot.drawText(g);
            }
         }
         if (i == selectedIndex) {
            slot.drawSelected(g);
         }
      }
   }

   @Override
   public void update() {
      /*  Override this method with custom behavior */
   }

   @Override
   public void setUnlocked(int level, LevelInfo levelInfo, BufferedImage levelIcon) {
      this.levelSlots.get(level - 1).setAssociatedLevel(levelInfo, levelIcon);
   }
   
}
