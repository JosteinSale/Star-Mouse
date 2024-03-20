package gamestates;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import audio.AudioPlayer;
import cutscenes.Cutscene;
import cutscenes.CutsceneManager2;
import entities.exploring.AutomaticTrigger;
import entities.flying.PlayerFly;
import entities.flying.enemies.Enemy;
import entities.flying.enemies.EnemyManager;
import entities.flying.pickupItems.PickupItem;
import game_events.*;
import main.Game;
import misc.ProgressValues;
import projectiles.ProjectileHandler;
import ui.GameoverOverlay;
import ui.LevelFinishedOverlay;
import ui.OptionsMenu;
import ui.PauseFlying;
import ui.TextboxManager2;
import utils.LoadSave;
import static utils.HelpMethods.GetAutomaticTrigger;
import static utils.HelpMethods2.GetPickupItem;
import static utils.HelpMethods.GetCutscenes;

import static utils.Constants.Flying.TypeConstants.POWERUP;
import static utils.Constants.Flying.TypeConstants.REPAIR;
import static utils.Constants.Flying.TypeConstants.BOMB;
import static utils.Constants.Flying.TypeConstants.DRONE;
import static utils.Constants.Audio;

public class Flying extends State implements Statemethods {

    public AudioPlayer audioPlayer;
    private PauseFlying pauseOverlay;
    private LevelFinishedOverlay levelFinishedOverlay;
    private GameoverOverlay gameoverOverlay;
    private Integer level;
    private EnemyManager enemyManager;
    private ProjectileHandler projectileHandler;
    private PlayerFly player;
    private CutsceneManager2 cutsceneManager;
    private EventHandler eventHandler;
    private ArrayList<AutomaticTrigger> automaticTriggers;
    private ArrayList<PickupItem> pickupItems;
    private int repairHealth = 100;
    private int song;
    private boolean pause = false;
    private boolean gamePlayActive = true;
    private boolean levelFinished = false;
    private boolean gameOver = false;
    private float resetYPos;
    private float skipYPos;

    private int[] bgImgHeights = {7600, 10740, 6000};
    private float[] resetPoints = {20f, 1300f, 1000f};
    private float[] endLevelPoints = {17000f, 27500f, 0f};
    private BufferedImage clImg;
    private Image scaledClImg;
    private Image scaledBgImg;
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
        super(game);
        this.audioPlayer = game.getAudioPlayer();
        this.automaticTriggers = new ArrayList<>();
        this.pickupItems = new ArrayList<>();
        initClasses(game.getOptionsMenu(), game.getExploring().getProgressValues());
        loadEventReactions();
        projectileHandler.setBombs(game.getExploring().getProgressValues().getBombs());
    }

    public void loadLevel(int level) {
        this.level = level;
        this.song = Audio.GetFlyLevelSong(level);
        this.resetYPos = resetPoints[level];
        this.skipYPos = endLevelPoints[level];
        loadMapAndOffsets(level);
        player.setClImg(this.clImg);
        projectileHandler.setClImg(this.clImg);
        projectileHandler.setBombs(game.getExploring().getProgressValues().getBombs());
        enemyManager.loadEnemiesForLvl(level);
        loadPickupItems(level);
        loadCutscenes(level);  
        player.setKilledEnemies(0);
        //startAt(-13000);     // For testing purposes
        
    }

    private void initClasses(OptionsMenu optionsMenu, ProgressValues progValues) {
        Rectangle2D.Float playerHitbox = new Rectangle2D.Float(500f, 400f, 50f, 50f);
        this.player = new PlayerFly(game, playerHitbox);
        this.enemyManager = new EnemyManager(player, audioPlayer);
        this.projectileHandler = new ProjectileHandler(game, audioPlayer, player, enemyManager);
        this.eventHandler = new EventHandler();
        TextboxManager2 textboxManager = new TextboxManager2();
        this.cutsceneManager = new CutsceneManager2(eventHandler, textboxManager);
        this.pauseOverlay = new PauseFlying(this, optionsMenu);
        this.levelFinishedOverlay = new LevelFinishedOverlay(this, progValues);
        this.gameoverOverlay = new GameoverOverlay(this);
    }

    private void loadMapAndOffsets(int lvl) {
        this.clImg = LoadSave.getFlyImageCollision("level" + Integer.toString(lvl) + "_cl.png");
        this.clImgHeight = clImg.getHeight() * 3;
        this.clImgWidth = clImg.getWidth() * 3;
        this.clYOffset = Game.GAME_DEFAULT_HEIGHT - clImgHeight + 150;
        this.clXOffset = 150;
        BufferedImage bgImg = LoadSave.getFlyImageBackground("level" + Integer.toString(lvl) + "_bg.png");
        this.bgImgHeight = bgImgHeights[lvl];
        this.bgYOffset = Game.GAME_DEFAULT_HEIGHT - bgImgHeight;
        scaledClImg =  clImg.getScaledInstance(
            (int) (clImgWidth * Game.SCALE), 
            (int) (clImgHeight * Game.SCALE), Image.SCALE_SMOOTH);
        scaledBgImg =  bgImg.getScaledInstance(
            (Game.GAME_WIDTH), 
            (int) (bgImgHeight * Game.SCALE), Image.SCALE_SMOOTH);
    }

    private void loadPickupItems(Integer level) {
        pickupItems.clear();
        automaticTriggers.clear();
        List<String> levelData = LoadSave.getFlyLevelData(level);
        for (String line : levelData) {
            String[] lineData = line.split(";");
            if (lineData[0].equals("automaticTrigger")) {
                automaticTriggers.add(GetAutomaticTrigger(lineData));
            }
            else if (lineData[0].equals("powerup")) {
                int width = 30;
                int height = 50;
                pickupItems.add(GetPickupItem(lineData, width, height, POWERUP));
            }
            else if (lineData[0].equals("repair")) {
                int width = 60;
                int height = 60;
                pickupItems.add(GetPickupItem(lineData, width, height, REPAIR));
            }
            else if (lineData[0].equals("bomb")) {
                int width = 45;
                int height = 45;
                pickupItems.add(GetPickupItem(lineData, width, height, BOMB));
            }
        }
    }

    private void loadCutscenes(Integer level) {
        this.cutsceneManager.clear();
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
        else if (event instanceof FadeOutEvent evt) {
            this.cutsceneManager.startFadeOut(evt.speed());
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
        else if (event instanceof LevelFinishedEvent evt) {
            this.levelFinished = true;
            this.levelFinishedOverlay.setLevelStats(enemyManager.getKilledEnemies());
        }
        else if (event instanceof StartSongEvent evt) {
            this.game.getAudioPlayer().startSongLoop(evt.index());
        }
        else if (event instanceof StartAmbienceEvent evt) {
            audioPlayer.startAmbienceLoop(evt.index());
        }
        else if (event instanceof FadeOutLoopEvent evt) {
            audioPlayer.fadeOutAllLoops();
        }
        else if (event instanceof FellowShipEvent evt) {
            cutsceneManager.startFellowShips(evt.xPos(), evt.yPos(), evt.takeOffTimer());
        }
        /* 
        else if (event instanceof SetStartingCutsceneEvent2 evt) {
            this.setNewStartingCutscene(evt.triggerObject(), evt.cutsceneIndex());
        }
        */
    }

    /** Flips the pause boolean */
    public void flipPause() {
        this.pause = !pause;
    }

    @Override
    public void update() {
        if (gameOver) {
            gameoverOverlay.update();
        }
        else if (pause) {
            checkPause();
            pauseOverlay.update();
        }
        else if (levelFinished) {
            levelFinishedOverlay.update();
        }
        else if (gamePlayActive) {   
            if (!cutsceneManager.isActive()) {
                checkCutsceneTriggers();
            }
            updateChartingY();
            checkPause();
            moveMaps();
            moveCutscenes();
            player.update(clYOffset, clXOffset);
            updatePickupItems();
            enemyManager.update(fgCurSpeed);
            projectileHandler.update(clYOffset, clXOffset, fgCurSpeed);
        }
        if (!pause) {cutsceneManager.update();}
    }

    private void updateChartingY() {
        chartingY += fgCurSpeed;
        //System.out.println(chartingY);
    }

    private void checkPause() {
        if (game.pauseIsPressed) {
            game.pauseIsPressed = false;
            this.flipPause();
            audioPlayer.flipSongActive();
        }
    }

    private void updatePickupItems() {
        for (PickupItem p : pickupItems) {
            p.update(fgCurSpeed);
            if (p.isActive() && (p.getHitbox().intersects(player.getHitbox()))) {
                p.setActive(false);
                if (p.getType() == POWERUP) {
                    projectileHandler.setPowerup(true);
                    audioPlayer.playSFX(Audio.SFX_POWERUP);
                }
                else if (p.getType() == REPAIR) {
                    player.increaseHealth(repairHealth);
                    audioPlayer.playSFX(Audio.SFX_REPAIR);
                }
                else if (p.getType() == BOMB) {
                    projectileHandler.addBombToInventory();
                    audioPlayer.playSFX(Audio.SFX_BOMB_PICKUP);
                }
            }
        }
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
                    trigger.setPlayed(true);
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
        if (!levelFinished) {
            drawMaps(g);
            drawPickupItems(g);
            this.player.draw(g);
            this.enemyManager.draw(g);
            this.projectileHandler.draw(g);
        }
        if (!gameOver) {
            this.cutsceneManager.draw(g);
        }
        if (gameOver) {
            gameoverOverlay.draw(g);
        }
        else if (pause) {
            pauseOverlay.draw(g);
        }
        else if (levelFinished) {
            levelFinishedOverlay.draw(g);
        }
    }

    private void drawMaps(Graphics g) {
        g.drawImage(
            scaledBgImg, 
            0, (int) (bgYOffset * Game.SCALE), null);
        g.drawImage(
            scaledClImg, 
            (int) (-150 * Game.SCALE), 
            (int) (clYOffset * Game.SCALE), null);
            
    }

    private void drawPickupItems(Graphics g) {
        for (PickupItem p : pickupItems) {
            p.draw(g);
        }
    }

    public void exitFinishedLevel() {
        // Credits are updated in LevelFinishedOverlay.
        game.getExploring().getProgressValues().setBombs(projectileHandler.getBombsAtEndOfLevel());
        game.getExploring().updatePauseInventory();
        this.resetFlying();
        if (this.level == 0) {
            Gamestate.state = Gamestate.EXPLORING;
        }
        else {
            game.getLevelSelect().reset();
            game.getLevelSelect().updateUnlockedLevels(level, enemyManager.getKilledEnemies().size());
            Gamestate.state = Gamestate.LEVEL_SELECT;
        }
    }

    /** Used only for testing porposes */
    private void startAt(int y) {
        this.clYOffset -= y;
        this.bgYOffset -= y * (bgCurSpeed / fgCurSpeed);
        for (PickupItem p : pickupItems) {
            p.getHitbox().y -= y;
        }
        for (AutomaticTrigger trigger : automaticTriggers) {
            trigger.getHitbox().y -= y;
        }
        enemyManager.moveAllEnemies(y);
    }

    /** Resets all non-level-specific values in flying-mode.
     * Should be called both when loading a new level, and when resetting the current level.
     * (Level-specific values are set in the 'loadLevel'-method and 'resetLevel'-method) */
    public void resetFlying() {
        pause = false;
        gamePlayActive = true;
        levelFinished = false;
        gameOver = false;
        fgCurSpeed = fgNormalSpeed;
        bgCurSpeed = bgNormalSpeed;
        player.reset();
        gameoverOverlay.reset();
        projectileHandler.reset();
        cutsceneManager.reset();
    }

    public void killPlayer() {
        gameOver = true;
        gamePlayActive = false;
        player.setVisible(false);
        gameoverOverlay.setPlayerPos(player.getHitbox().x, player.getHitbox().y);
        projectileHandler.setBombs(game.getExploring().getProgressValues().getBombs());
        audioPlayer.stopAllLoops();
        audioPlayer.startAmbienceLoop(Audio.AMBIENCE_SILENCE);
        audioPlayer.playSFX(Audio.SFX_DEATH);
    }

    /** Not to be confused with the resetFlying()-method. This method should be called
     * when the player has died, and resets the level. 
     * It should reset (not reload) all level-specific variables, 
     * like enemies, pickup-items and map-offsets.
     */
    public void resetLevel() {
        this.clYOffset = Game.GAME_DEFAULT_HEIGHT - clImgHeight + 150 + resetYPos;
        this.bgYOffset = Game.GAME_DEFAULT_HEIGHT - bgImgHeight + (resetYPos / 3);
        for (PickupItem p : pickupItems) {
            p.resetTo(resetYPos);
        }
        for (AutomaticTrigger trigger : automaticTriggers) {
            trigger.resetTo(resetYPos);
        }
        enemyManager.resetEnemiesTo(resetYPos);
        audioPlayer.startSongLoop(song);
        audioPlayer.startAmbienceLoop(Audio.AMBIENCE_ROCKET_ENGINE);
    }


    /** Changes the levelOffset to the end of the level, and moves the automatic triggers accorgingly. 
     * Doesn't move enemies or pickup-items. */
    public void skipLevel() {
    this.cutsceneManager.skipCutscene();
    this.clYOffset = Game.GAME_DEFAULT_HEIGHT - clImgHeight + 150 + skipYPos;
    this.bgYOffset = Game.GAME_DEFAULT_HEIGHT - bgImgHeight + (skipYPos * (bgCurSpeed / fgCurSpeed));
    for (AutomaticTrigger trigger : automaticTriggers) {
        trigger.resetTo(skipYPos);
        }
    }

    /** Can be used to artificially increase the number of killed enemies by 10.
     * Use for testing purposes.
     */
    public void plus10KilledEnemies() {
        for (int i = 0; i < 10; i++) {
            this.enemyManager.increaseKilledEnemies(DRONE); 
        }
    } 

    /** Can be used to artificially decrease the number of killed enemies by 10.
     * Use for testing purposes.
     */
    public void minus10KilledEnemies() {
        for (int i = 0; i < 10; i++) {
            this.enemyManager.decreaseKilledEnemies(DRONE); 
        }
    }

    /** Is called from Player. Is needed to check teleport collision with big enemies. */
    public ArrayList<Enemy> getBigEnemies() {
        return this.enemyManager.getBigEnemies();
    }

    /** Is called from Player. Is needed for big enemies */
    public void checkIfDead(Enemy enemy) {
        this.enemyManager.checkIfDead(enemy);
    }
}
