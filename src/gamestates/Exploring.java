package gamestates;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import audio.AudioPlayer;
import main.Game;
import misc.Area;
import misc.ProgressValues;
import ui.InventoryItem;
import ui.MechanicOverlay;
import ui.PauseExploring;
import utils.LoadSave;

public class Exploring extends State implements Statemethods {
    private AudioPlayer audioPlayer;
    private int currentLevel = 1;
    private int currentArea = 1;
    private ArrayList<Area> areas;
    private PauseExploring pauseOverlay;
    private ProgressValues progValues;
    private MechanicOverlay mechanicOverlay;
    private boolean mechanicActive = false;

    public Exploring(Game game) {
        super(game);
        this.audioPlayer = game.getAudioPlayer();
        areas = new ArrayList<>();
        loadLevel(currentLevel);
        progValues = new ProgressValues();
        pauseOverlay = new PauseExploring(game, progValues, audioPlayer, game.getOptionsMenu());
        mechanicOverlay = new MechanicOverlay(game, progValues);
    }

    // Laster inn alle areas for denne levelen
    // Kan kalles fra andre deler av programmet senere
    public void loadLevel(int level) {
        ArrayList<List<String>> levelData = LoadSave.getExpLevelData(level);
        ArrayList<List<String>> cutsceneData = LoadSave.getExpCutsceneData(level);
        for (int i = 0; i < levelData.size(); i++) {
            Area area = new Area(game, this, audioPlayer, level, i + 1, levelData.get(i), cutsceneData.get(i));
            areas.add(area);
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
        // Den nye arean må få oppdatert seg før vi tegner den.
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
}
