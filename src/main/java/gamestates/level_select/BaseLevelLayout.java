package gamestates.level_select;

import java.util.ArrayList;

import audio.AudioPlayer;
import main_classes.Game;
import utils.Singleton;

public abstract class BaseLevelLayout extends Singleton {
   protected Game game;
   protected AudioPlayer audioPlayer;
   public ArrayList<LevelSlot> levelSlots;
   public int selectedIndex = 0;
   public int X = 150;
   public int Y;
   public int W = 777;
   public int H;

   public BaseLevelLayout(Game game) {
      this.game = game;
      this.audioPlayer = game.getAudioPlayer();
      this.levelSlots = new ArrayList<>();
   }

   /** Override with custom behavior */
   public void update() {
   }

   public void setUnlocked(int level, LevelInfo levelInfo) {
      this.levelSlots.get(level - 1).setAssociatedLevel(levelInfo);
   }

   public void clearAll() {
      selectedIndex = 0;
      for (LevelSlot slot : levelSlots) {
         slot.clearAssociatedLevel();
      }
   }

}
