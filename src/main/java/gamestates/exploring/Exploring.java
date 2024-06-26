package gamestates.exploring;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import audio.AudioPlayer;
import data_storage.ProgressValues;
import gamestates.State;
import gamestates.Statemethods;
import main_classes.Game;
import ui.InventoryItem;
import ui.MechanicOverlay;
import ui.PauseExploring;
import utils.ResourceLoader;

public class Exploring extends State implements Statemethods {
    private AudioPlayer audioPlayer;
    private int currentLevel;
    private int currentArea;    // Is set in the loadLevel-method. Can be altered for testing purposes.
    private ArrayList<Area> areas;
    private PauseExploring pauseOverlay;
    private ProgressValues progValues;
    private MechanicOverlay mechanicOverlay;
    private boolean mechanicActive = false;

    public Exploring(Game game) {
        super(game);
        this.audioPlayer = game.getAudioPlayer();
        initProxyProgValues();
        areas = new ArrayList<>();
        pauseOverlay = new PauseExploring(game, progValues, audioPlayer, game.getOptionsMenu());
        mechanicOverlay = new MechanicOverlay(game, progValues);
    }

    /** Initiates a new rogressValues-object that represents a new game.
     * In the case of testing, it will serve as a proxy.
     * If the player loads a previous save, this object will replaced with the
     * one loaded on disc.
     */
    private void initProxyProgValues() {
        this.progValues = new ProgressValues();
    }

    /** Loads all areas for the given level. 
     * Is normally only called from LevelSelect, but can also be called from the MainMenu if needed.*/
    public void loadLevel(int level) {
        System.out.println("Loading level " + level + ":");
        this.currentLevel = level;
        this.currentArea = 1;
        this.areas = new ArrayList<>();
        ArrayList<List<String>> levelData = ResourceLoader.getExpLevelData(level);
        ArrayList<List<String>> cutsceneData = ResourceLoader.getExpCutsceneData(level);
        for (int i = 0; i < levelData.size(); i++) {
            Area area = new Area(game, this, audioPlayer, level, i + 1, levelData.get(i), cutsceneData.get(i));
            areas.add(area);
            System.out.println("Area " + (i + 1) + " succesfully loaded.");
        }
    }

    private void checkPause() {
        if (game.pauseIsPressed) {
            game.pauseIsPressed = false;
            this.pauseOverlay.flipActive();
        }
    }

    @Override
    public void update() {
        if (pauseOverlay.isActive()) {
            checkPause();
            pauseOverlay.update();
        } else if (mechanicActive) {
            mechanicOverlay.update();
        } else {
            checkPause();
            areas.get(currentArea - 1).update();
        }
    }

    public void goToArea(int area) {
        this.currentArea = area;
        areas.get(currentArea - 1).update();
        // The new area needs to be updated before we can draw it.
    }

    @Override
    public void draw(Graphics g) {
        areas.get(currentArea - 1).draw(g);
        if (pauseOverlay.isActive()) {
            pauseOverlay.draw(g);
        } else if (mechanicActive) {
            mechanicOverlay.draw(g);
        }
    }

    public void resetDirBooleans() {
        areas.get(currentArea - 1).getPlayer().resetAll();
    }

    public void addToInventory(InventoryItem item) {
        this.pauseOverlay.addItem(item);
    }

    public boolean playerHasEnoughCredits(int amount) {
        if (amount <= this.progValues.getCredits()) {
            return true;
        }
        return false;
    }

    public ProgressValues getProgressValues() {
        return this.progValues;
    }

    /** Can be used to load a 'save' from disc */
    public void setProgressValues(ProgressValues progValues) {
        this.progValues = progValues;
    }

    public void updatePauseInventory() {
        this.pauseOverlay.updateProgressValues();
    }

    public void setMechanicActive(boolean active) {
        this.mechanicActive = active;
    }

    public int getSongForArea(int newArea) {
        return this.areas.get(newArea - 1).getSong();
    }

    public Area getArea(int area) {
        return areas.get(area - 1);
    }

    public void goToFlying() {
        areas.get(currentArea - 1).goToFlying(currentLevel);
    }
}
