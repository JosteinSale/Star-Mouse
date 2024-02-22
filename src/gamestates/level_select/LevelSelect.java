package gamestates.level_select;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.State;
import gamestates.Statemethods;
import main.Game;
import misc.ProgressValues;
import utils.LoadSave;

public class LevelSelect extends State implements Statemethods {
    private ProgressValues progValues;
    private BufferedImage bgImg;
    private ArrayList<Statemethods> levelLayouts;
    private ArrayList<BufferedImage> levelIcons;
    private ArrayList<LevelInfo> levels;
    private int currentLayout = 1;
    // See loadLevelInfo();

    private boolean[] unlockedLevels = {   // For now this is a 1D-array, but we might make it a 2D-array.
        true, false, false, false, false,  // Path 1
             false, false, false, false,   // Path 2
             false, false, false, false};  // Path 3
    

    public LevelSelect(Game game) {
        super(game);
        this.progValues = game.getExploring().getProgressValues();
        this.levelLayouts = new ArrayList<>();
        this.levelIcons = loadLevelIcons();
        this.levels = new ArrayList<>(); 
        loadLevelInfo();
        levelLayouts.add(new LevelLayout1(game, levelIcons));
        bgImg = LoadSave.getExpImageBackground(LoadSave.LEVEL_SELECT_BG);
    }

    private void loadLevelInfo() {
        levels.add(new LevelInfo("Apolis", 130, 100));
        levels.add(new LevelInfo("Vyke", 130, 100));
        // etc
    }

    private ArrayList<BufferedImage> loadLevelIcons() {
        return new ArrayList<>();
    }

    @Override
    public void update() {
        levelLayouts.get(currentLayout - 1).update();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(bgImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        levelLayouts.get(currentLayout - 1).draw(g);
    }

    /** Should be called whenever the player unlocks a new level, or finishes an ending.
     * The 'hasEndingX'-booleans should be updated before this method is called.
     * It first checks if path 3 has been visited.
     * Then it calculates which level layout the player should see.
     */
    public void updateLayout() {
        checkPath3Visited();
        if ((!progValues.firstPlayThrough) && 
            (progValues.path3Visited || progValues.hasEnding3 || progValues.hasEnding2)) {
            currentLayout = 3;
        } else if (progValues.hasEnding1) {
            currentLayout = 2;
        } else {
            currentLayout = 1;
        }
    }

    /** Should be called whenever the player finishes a level. */
    public void checkPath3Visited() {
        for (int i = 9; i <= 12; i++) {
            if (unlockedLevels[i] == true) {
                progValues.path3Visited = true;
            }
        }
    }
}
