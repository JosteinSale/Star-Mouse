package gamestates.exploring;

import java.util.ArrayList;
import java.util.List;

import audio.AudioPlayer;
import cutscenes.cutsceneManagers.DefaultCutsceneManager;
import data_storage.ProgressValues;
import gamestates.State;
import main_classes.Game;
import ui.InventoryItem;
import ui.MechanicOverlay;
import ui.PauseExploring;
import utils.ResourceLoader;

public class Exploring extends State {
    private AudioPlayer audioPlayer;
    private int currentLevel;
    public int currentArea; // Is set in the loadLevel-method. Can be altered for testing purposes.
    private ArrayList<Area> areas;
    private PauseExploring pauseOverlay;
    private ProgressValues progValues; // Is the only instance in the code base (except LevelSelect)
    private MechanicOverlay mechanicOverlay;

    public Exploring(Game game) {
        super(game);
        this.audioPlayer = game.getAudioPlayer();
        initProxyProgValues();
        areas = new ArrayList<>();
        pauseOverlay = new PauseExploring(game, audioPlayer, game.getOptionsMenu());
        mechanicOverlay = new MechanicOverlay(
                game,
                game.getTextboxManager().getInfoBox(),
                game.getTextboxManager().getInfoChoice());
    }

    /**
     * Initiates a new rogressValues-object.
     * In the case of testing, it will serve as a proxy.
     * If the player loads a previous save, this object will replaced with the
     * one loaded on disc.
     */
    private void initProxyProgValues() {
        this.progValues = ProgressValues.getNewSave();
    }

    /** Can be used to load a 'save' from disc */
    public void setProgressValues(ProgressValues progValues) {
        this.progValues = progValues;
        game.getLevelSelect().transferDataFromSave();
    }

    /**
     * Loads all areas for the given level.
     * Is normally only called from LevelSelect, but can also be called from the
     * MainMenu if needed.
     */
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
        game.getView().getRenderExploring().loadLevel(level);
        DefaultCutsceneManager cm = areas.get(currentArea - 1).getCutsceneManager();
        game.getView().getRenderCutscene().setCutsceneManager(cm);
    }

    private void checkPause() {
        if (game.pauseIsPressed) {
            game.pauseIsPressed = false;
            this.pauseOverlay.flipActive();
        }
    }

    public void update() {
        if (pauseOverlay.isActive()) {
            checkPause();
            pauseOverlay.update();
        } else if (mechanicOverlay.isActive()) {
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

    public void updatePauseInventory() {
        this.pauseOverlay.updateProgressValues();
    }

    public void setMechanicActive(boolean active) {
        this.mechanicOverlay.setActive(active);
        this.mechanicOverlay.onOpen();
    }

    public int getSongForArea(int newArea) {
        return this.areas.get(newArea - 1).getSong();
    }

    public int getAmbienceForArea(int newArea) {
        return this.areas.get(newArea - 1).getAmbience();
    }

    public Area getArea(int area) {
        return areas.get(area - 1);
    }

    public void goToFlying() {
        areas.get(currentArea - 1).goToFlying(currentLevel);
    }

    public void skipLevel() {
        areas.get(currentArea - 1).skipLevel();
    }

    public void flushAreas() {
        for (Area area : areas) {
            area.flushImages();
        }
        areas.clear();
    }

    public PauseExploring getPauseOverlay() {
        return this.pauseOverlay;
    }

    public ArrayList<Area> getAreas() {
        return this.areas;
    }

    public boolean isMechanicActive() {
        return mechanicOverlay.isActive();
    }

    public MechanicOverlay getMechanicOverlay() {
        return this.mechanicOverlay;
    }

    public DefaultCutsceneManager getCurrentCutsceneManager() {
        return areas.get(currentArea - 1).getCutsceneManager();
    }
}
