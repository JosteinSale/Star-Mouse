package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import main.Game;
import misc.Area;
import ui.InventoryItem;
import ui.PauseExploring;
import utils.LoadSave;

public class Exploring extends State implements Statemethods {
    private int currentLevel = 1;
    private int currentArea = 3;
    private ArrayList<Area> areas;
    private PauseExploring pauseOverlay;

    public Exploring(Game game) {
        super(game);
        areas = new ArrayList<>();
        loadLevel(currentLevel);
        pauseOverlay = new PauseExploring();
    }

    // Laster inn alle areas for denne levelen
    // Kan kalles fra andre deler av programmet senere
    public void loadLevel(int level) {
        ArrayList<List<String>> levelData = LoadSave.getExpLevelData(level);
        ArrayList<List<String>> cutsceneData = LoadSave.getExpCutsceneData(level);
        for (int i = 0; i < levelData.size(); i++) {
            Area area = new Area(this, level, i + 1, levelData.get(i), cutsceneData.get(i));
            areas.add(area);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            this.pauseOverlay.flipActive();
        }
        else if (pauseOverlay.isActive()) {
            pauseOverlay.keyPressed(e);
        }
        else {
            areas.get(currentArea - 1).keyPressed(e);  
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
        areas.get(currentArea - 1).keyReleased(e);  
    }


    @Override
    public void update() {
        if (pauseOverlay.isActive()) {
            pauseOverlay.update();
        }
        else {
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
        }
    }

    public void resetDirBooleans() {
        areas.get(currentArea - 1).getPlayer().resetAll();
    }

    public void addToInventory(InventoryItem item) {
        this.pauseOverlay.addItem(item);
    }

    public void updateInventory(String type, int amount) {
        this.pauseOverlay.updateValues(type, amount);
    }

    public boolean playerHasEnoughCredits(int amount) {
        if (amount <= this.pauseOverlay.getCredits()) {
            return true;
        }
        return false;
    }

    /** Returns the number of credits in the player's inventory */
    public int getCredits() {
        return this.pauseOverlay.getCredits();
    }
}
