package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import cutscenes.Cutscene;
import cutscenes.CutsceneManager2;
import entities.exploring.AutomaticTrigger;
import entities.flying.EnemyManager;
import entities.flying.PickupItem;
import entities.flying.PlayerFly;
import entities.flying.Powerup;
import entities.flying.Repair;
import game_events.*;
import main.Game;
import projectiles.ProjectileHandler;
import ui.TextboxManager2;
import utils.LoadSave;
import static utils.HelpMethods.GetAutomaticTrigger;
import static utils.HelpMethods2.GetPickupItem;
import static utils.HelpMethods.GetCutscenes;

import static utils.Constants.Flying.TypeConstants.POWERUP;
import static utils.Constants.Flying.TypeConstants.REPAIR;
import static utils.Constants.Flying.TypeConstants.BOMB;

public class Flying implements Statemethods {
    private Game game;
    private Integer level = 0;
    private EnemyManager enemyManager;
    private ProjectileHandler projectileHandler;
    private PlayerFly player;
    private CutsceneManager2 cutsceneManager;
    private EventHandler eventHandler;
    private ArrayList<AutomaticTrigger> automaticTriggers;
    private ArrayList<PickupItem> pickupItems;
    private int repairHealth = 100;
    private boolean pause = false;
    private boolean gamePlayActive = true;

    private BufferedImage clImg;
    private BufferedImage bgImg;
    private int clImgHeight;
    private int clImgWidth;
    private int bgImgHeight;
    private float clYOffset;
    private float clXOffset;
    private float bgYOffset;
    private float fgNormalSpeed = 2f;
    private float bgNormalSpeed = 0.7f;
    private float fgCurSpeed = fgNormalSpeed;
    private float bgCurSpeed = bgNormalSpeed;

    private int chartingY = 0;

    public Flying(Game game) {
        this.game = game;
        this.automaticTriggers = new ArrayList<>();
        this.pickupItems = new ArrayList<>();
        loadMapImages();
        initClasses();
        loadEventReactions();
        loadLevelData(level);
        loadCutscenes(level);
    }

    private void loadMapImages() {
        this.clImg = LoadSave.getFlyImageCollision("BorderTest1.png");
        this.clImgHeight = clImg.getHeight() * 3;
        this.clImgWidth = clImg.getWidth() * 3;
        this.clYOffset = Game.GAME_DEFAULT_HEIGHT - clImgHeight + 150;
        this.clXOffset = 150;
        this.bgImg = LoadSave.getFlyImageBackground("BigFile_test2.png");
        this.bgImgHeight = bgImg.getHeight();
        this.bgYOffset = Game.GAME_DEFAULT_HEIGHT - bgImgHeight;
    }

    private void initClasses() {
        Rectangle2D.Float playerHitbox = new Rectangle2D.Float(500f, 600f, 50f, 50f);
        this.player = new PlayerFly(playerHitbox, clImg);
        this.enemyManager = new EnemyManager(player);
        this.projectileHandler = new ProjectileHandler(player, enemyManager, clImg);
        this.eventHandler = new EventHandler();
        TextboxManager2 textboxManager = new TextboxManager2();
        this.cutsceneManager = new CutsceneManager2(eventHandler, textboxManager);
    }

    private void loadLevelData(Integer level) {
        List<String> levelData = LoadSave.getFlyLevelData(level);
        for (String line : levelData) {
            String[] lineData = line.split(";");
            if (lineData[0].equals("automaticTrigger")) {
                automaticTriggers.add(GetAutomaticTrigger(lineData));
            }
            else if (lineData[0].equals("powerup")) {
                pickupItems.add(GetPickupItem(lineData, POWERUP));
            }
            else if (lineData[0].equals("repair")) {
                pickupItems.add(GetPickupItem(lineData, REPAIR));
            }
            else if (lineData[0].equals("bomb")) {
                pickupItems.add(GetPickupItem(lineData, BOMB));
            }
        }
    }

    private void loadCutscenes(Integer level) {
        List<String> cutsceneData = LoadSave.getFlyCutsceneData(level);
        ArrayList<ArrayList<Cutscene>> cutscenes = GetCutscenes(cutsceneData);
        for (ArrayList<Cutscene> cutscenesForTrigger : cutscenes) {
            cutsceneManager.addCutscene(cutscenesForTrigger);
        }
    }

    private void loadEventReactions() {
        this.eventHandler.addEventListener(this::doReaction);
    }

    public void doReaction(GeneralEvent event) {
        if (event instanceof DialogueEvent evt) {
            this.cutsceneManager.startDialogue(evt.name(), evt.speed(), evt.text(), evt.portraitIndex());
        }
        else if (event instanceof FadeInEvent evt) {
            this.cutsceneManager.startFadeIn(evt.speed());
        }
        else if (event instanceof SetVisibleEvent evt) {
            this.player.setVisible(evt.visible());
        }
        else if (event instanceof WaitEvent evt) {
            this.cutsceneManager.waitFrames(evt.waitFrames());
        }
        else if (event instanceof SetOverlayImageEvent evt) {
            this.cutsceneManager.setOverlayImage(evt.fileName());
        }
        else if (event instanceof SetOverlayActiveEvent evt) {
            this.cutsceneManager.setOverlayActive(evt.active());
        }
        else if (event instanceof BlackScreenEvent evt) {
            this.cutsceneManager.setBlackScreen(evt.active());
        }
        else if (event instanceof SetGameplayEvent evt) {
            this.gamePlayActive = evt.active();
        }
        else if (event instanceof FadeHeaderEvent evt) {
            cutsceneManager.fadeHeader(evt.inOut(), evt.yPos(), evt.fadeSpeed(), evt.header());
        }
        /* 
        else if (event instanceof SetStartingCutsceneEvent2 evt) {
            this.setNewStartingCutscene(evt.triggerObject(), evt.cutsceneIndex());
        }
        */
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            this.pause = !pause;
        }
        else {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            projectileHandler.setSpacePressed(true);
        }
        else if (e.getKeyCode() == KeyEvent.VK_B) {
            projectileHandler.setBPressed(true);
        }
        this.player.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            projectileHandler.setSpacePressed(false);
            projectileHandler.resetShootTick();
        }
        else if (e.getKeyCode() == KeyEvent.VK_B) {
            projectileHandler.setBPressed(false);
            projectileHandler.resetShootTick();
        }
        this.player.KeyReleased(e);
    }

    @Override
    public void update() {
        if (pause) {
            //pauseOverlay.update()
        }
        else {
            if (gamePlayActive) {
                if (!cutsceneManager.isActive()) {
                    checkCutsceneTriggers();
                }
                //updateChartingY();
                moveMaps();
                moveCutscenes();
                player.update(clYOffset, clXOffset);
                updatePickupItems();
                enemyManager.update(fgCurSpeed);
                projectileHandler.update(clYOffset, clXOffset, fgCurSpeed);
            }
            cutsceneManager.update();
        }
    }

    private void updatePickupItems() {
        for (PickupItem p : pickupItems) {
            p.update(fgCurSpeed);
            if ((p.getHitbox().intersects(player.getHitbox())) && p.isActive()) {
                p.setActive(false);
                if (p.getType() == POWERUP) {
                    projectileHandler.setPowerup(true);
                }
                else if (p.getType() == REPAIR) {
                    player.increaseHealth(repairHealth);
                }
                else if (p.getType() == BOMB) {
                    projectileHandler.addBombToInventory();
                }
            }
        }
    }

    private void updateChartingY() {
        chartingY -= fgCurSpeed;
        System.out.println(chartingY);
    }

    private void moveCutscenes() {
        for (AutomaticTrigger trigger : automaticTriggers) {
            trigger.getHitbox().y += fgCurSpeed;
        }
    }

    private void checkCutsceneTriggers() {
        int index = 0;
        for (AutomaticTrigger trigger : automaticTriggers) {
            if ((trigger.getHitbox().y > 0) && (trigger.getHitbox().y < 10)) {
                if (!trigger.hasPlayed()) {
                    this.cutsceneManager.startCutscene(index, automaticTriggers.get(index).getStartCutscene());
                    trigger.setPlayed();
                }
            }
            index += 1;
        }   
    }

    private void moveMaps() {
        if (player.takesCollisionDmg()) {
            fgCurSpeed = fgNormalSpeed / 2;
            bgCurSpeed = bgNormalSpeed / 2;
        }
        else {
            fgCurSpeed = fgNormalSpeed;
            bgCurSpeed = bgNormalSpeed;
        }
        clYOffset += fgCurSpeed;
        bgYOffset += bgCurSpeed;
    }

    @Override
    public void draw(Graphics g) {
        drawMaps(g);
        drawPickupItems(g);
        this.player.draw(g);
        this.enemyManager.draw(g);
        this.projectileHandler.draw(g);
        this.cutsceneManager.draw(g);
    }

    private void drawMaps(Graphics g) {
        g.drawImage(
            bgImg, 
            0, (int) (bgYOffset * Game.SCALE), 
            Game.GAME_WIDTH, (int) (bgImgHeight * Game.SCALE), null);
        g.drawImage(
            clImg, 
            (int) (-150 * Game.SCALE), 
            (int) (clYOffset * Game.SCALE), 
            (int) (clImgWidth * Game.SCALE), 
            (int) (clImgHeight * Game.SCALE), null);
    }

    private void drawPickupItems(Graphics g) {
        for (PickupItem p : pickupItems) {
            p.draw(g);
        }
    }

    /* 
    private void setNewStartingCutscene(int triggerIndex, int cutsceneIndex) {
        this.automaticTriggers.get(triggerIndex).setStartCutscene(cutsceneIndex);
    }
    */
}
