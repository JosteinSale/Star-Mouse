package gamestates.level_select;

import static utils.Constants.UI.LEVEL_ICON_SIZE;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.Gamestate;
import gamestates.State;
import gamestates.Statemethods;
import main_classes.Game;
import misc.ProgressValues;
import utils.LoadSave;
import utils.Constants.Audio;

/** OBS: we should probably wait with implementing more logic until we have more paths. 
 * 
 * Keeps track of unlocked levels, and sets levelLayout based on that. 
 * Handles most of the logic regarding levelInfo, killCount and unlocked levels.
 * Keeps the nescessary images, and passes references to the LevelLayouts.
 */
public class LevelSelect extends State implements Statemethods {
    private ProgressValues progValues;
    private BufferedImage bgImg;
    private ArrayList<ILevelLayout> levelLayouts;
    private BufferedImage[] levelIcons;
    private int selectedLevel = 1;    // This is altered from the current layout.

    private ArrayList<LevelInfo> levelInfo;
    private int currentLayout = 1;
    public final int firstLevelBigThreshold = 130;
    private boolean[] unlockedLevels = {   // For now this is a 1D-array, but we might make it a 2D-array.
        true, false, false, false, false,  // Path 1
             false, false, false, false,   // Path 2
             false, false, false, false    // Path 3
        };
    
    private float bgX = -50;
    private int bgSlideDir = 1;
    private int alphaFade = 255;
    private boolean fadeInActive = true;
    private boolean fadeOutActive = false;

    public LevelSelect(Game game) {
        super(game);
        this.progValues = game.getExploring().getProgressValues();
        this.levelLayouts = new ArrayList<>();
        this.levelIcons = loadLevelIcons();
        this.levelInfo = new ArrayList<>(); 
        loadLevelInfo();
        levelLayouts.add(new LevelLayout1(game, levelIcons, levelInfo));
        bgImg = LoadSave.getExpImageBackground(LoadSave.LEVEL_SELECT_BG);
    }

    private void loadLevelInfo() {
        levelInfo.add(new LevelInfo("Apolis", 130, 100, 99, 99)); // We have a custom method for level1.
        levelInfo.add(new LevelInfo("Vyke", 130, 0, 3, 3));
        // etc
    }

    private BufferedImage[] loadLevelIcons() {
        BufferedImage[] images = new BufferedImage[13];
        BufferedImage img = LoadSave.getExpImageSprite(LoadSave.LEVEL_ICONS);
        for (int i = 0; i < 2; i++) {
            images[i] = img.getSubimage(
                    i * LEVEL_ICON_SIZE, 0, 
                    LEVEL_ICON_SIZE, LEVEL_ICON_SIZE);
            }
        return images;
    }

    @Override
    public void update() {
        moveBackGround();
        if (fadeInActive) {
            updateFadeIn();
        }
        else if (fadeOutActive) {
            updateFadeOut();
        }
        else {
            levelLayouts.get(currentLayout - 1).update();
        }
    }

    /** Can be called from Flying upon exitFinishedLevel(). It does the following:
     * 1. update globalBooleans
     * 2. unlockNextLevel
     * 3. set levelLayout
     * 4. update current levelLayout with new unlocked level, if needed.
     * @param finishedLevel
     * @param killCount
     */
    public void updateUnlockedLevels(int finishedLevel, int killCount) {
        updateGlobalBooleans(finishedLevel, killCount);
        unlockNextLevel(finishedLevel, killCount);
        checkIfNewLayout();
        levelLayouts.get(currentLayout - 1).setUnlocked(finishedLevel);
    }

    private void updateGlobalBooleans(int finishedLevel, int killCount) {
        if (finishedLevel == 1 && killCount == firstLevelBigThreshold) {
            progValues.path3Unlocked = true;  // Note: !firstPlaythrough is also required to get LevelLayout3
        }
        else if (finishedLevel == 5) {progValues.hasEnding1 = true; progValues.firstPlayThrough = false;}
        else if (finishedLevel == 9) {progValues.hasEnding2 = true; progValues.firstPlayThrough = false;}
        else if (finishedLevel == 13) {progValues.hasEnding3 = true; progValues.firstPlayThrough = false;}
    }

    private void unlockNextLevel(int finishedLevel, int killCount) {
        LevelInfo lvl = levelInfo.get(finishedLevel - 1);
        lvl.updateKillCount(killCount);
        int levelToUnlock = 0;

        if (finishedLevel == 1) {
            if (killCount == firstLevelBigThreshold) {
                levelToUnlock = 10;
            } else if (killCount >= lvl.getKillThreshold()) {
                levelToUnlock = 6;
            }
            else {
                levelToUnlock = 2;
            }
        }
        else if (finishedLevel == 5 || finishedLevel == 9 || finishedLevel == 13) {
            levelToUnlock = 1;  // Finishing an ending doesn't open any new levels.
        }
        else {  // level:  2, 3, 4  -  6, 7, 8  -  10, 11, 12
            boolean hasEnoughKills = false;
            if (killCount >= lvl.getKillThreshold()) {
                hasEnoughKills = true;
            };
            levelToUnlock = lvl.getNext(hasEnoughKills);
        }
        unlockedLevels[levelToUnlock - 1] = true; 
    }

    /** Should be called whenever the player unlocks a new level, or finishes an ending.
     * The 'hasEndingX'-booleans should be updated before this method is called.
     * Also check if path 3 has been visited before calling.
     * Then it calculates which level-layout the player should see.
     */
    private void checkIfNewLayout() {
        if ((!progValues.firstPlayThrough) && 
            (progValues.path3Unlocked || progValues.hasEnding3 || progValues.hasEnding2)) {
            currentLayout = 3;
        } else if (progValues.hasEnding1) {
            currentLayout = 2;
        } else {
            currentLayout = 1;
        }
    }

    public boolean canGoToLevel(int level) {
        return unlockedLevels[level -1 ];
    }

    public void goToLevel(int level) {
        this.fadeOutActive = true;
        this.selectedLevel = level;
    }

    private void moveBackGround() {
        this.bgX += 0.05f * bgSlideDir;
        if (this.bgX > 0) {bgSlideDir *= -1;}
        else if (this.bgX < -50) {bgSlideDir *= -1;}
    }

    private void updateFadeIn() {
        this.alphaFade -= 5;
        if (alphaFade < 0) {
            alphaFade = 0;
            fadeInActive = false;
        }
    }

    /** Also handles transition to Exploring */
    private void updateFadeOut() {
        this.alphaFade += 5;
        if (alphaFade > 255) {
            alphaFade = 255;
            this.game.getExploring().loadLevel(selectedLevel);
            this.game.getExploring().update(); 
            Gamestate.state = Gamestate.EXPLORING;
        }
    }

    /** Should be called whenever the player returns to LevelSelect */
    public void reset() {
        this.fadeOutActive = false;
        this.fadeInActive = true;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(bgImg, (int) bgX, 0, Game.GAME_WIDTH + 50, Game.GAME_HEIGHT + 50, null);
        levelLayouts.get(currentLayout - 1).draw(g);

        g.setColor(new Color(0, 0, 0, alphaFade));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
    }
}
