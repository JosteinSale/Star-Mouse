package gamestates.level_select;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import audio.AudioPlayer;
import main.Game;

/** 
 * In LevelLayout1, the unlocked levels are displayed in a single line, and the player
 * can only enter the last one at any given time.
 * The cursor can move back and forth, to display info about a given level.
 */
public class LevelLayout1 implements ILevelLayout {
    private AudioPlayer audioPlayer;
    private ArrayList<BufferedImage> allLevelIcons;
    private ArrayList<LevelInfo> allLevelInfo;
    private boolean[] allUnlockedLevels;

    private ArrayList<Integer> levelsInCurrentPath;
    private BufferedImage layoutImg;

    private int selectedIndex = 0;
    private int cursorX;
    private int cursorY;
    private int layoutX;
    private int layoutY;

    public LevelLayout1(
        Game game, ArrayList<BufferedImage> levelIcons, 
        ArrayList<LevelInfo> levelInfo, boolean[] unlockedLevels) {
        
        this.audioPlayer = game.getAudioPlayer();
        this.allLevelIcons = levelIcons;
        this.allLevelInfo = levelInfo;
        this.allUnlockedLevels = unlockedLevels;
        this.levelsInCurrentPath = new ArrayList<>();
        levelsInCurrentPath.add(1);
    }

    @Override
    public void update() {
        // If game.spaceIsPressed()
        //    { if (selectedIndex == unlockedLevels.size(-1)) {
        //            levelSelect.goToLevel(unlockedLevels.get(selectedIndex))} }
        //     else { audioPlayer.playSFX(CantDoThat!))}

    }

    @Override
    public void draw(Graphics g) {

    }

    @Override
    public void setUnlocked(int level) {
         
    }
    
}
