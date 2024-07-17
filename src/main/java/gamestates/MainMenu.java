package gamestates;

import static utils.Constants.UI.*;
import static utils.HelpMethods.DrawCenteredString;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import audio.AudioPlayer;
import main_classes.Game;
import ui.LoadSaveMenu;
import ui.OptionsMenu;
import utils.ResourceLoader;
import utils.Constants.Audio;

/** The MainMenu is the gateway into the different game states:
 * 
 * - Testing: Can test whatever state. This doesn't affect loading and saving.
 * - New Game: let's the player start a new game in a given save file.
 * - Load Save: let's player load a previous save, or start a new save if the 
 *              selected save is empty.
 * - Options: let's the player customize the controls and sound volume
 * - Level Editor: developer tool for editing flying levels
 * - Quit: quits the game.
 */
 public class MainMenu extends State implements Statemethods {
    
    private AudioPlayer audioPlayer;
    private BufferedImage bgImg;
    private BufferedImage titleImg;
    private BufferedImage cursorImg;
    private Font menuFont;
    private LoadSaveMenu loadSaveMenu;
    private OptionsMenu optionsMenu;
    private String[] alternatives = {"Testing", "New Game", "Load Save", "Level Editor", "Options", "Quit"};
    private ArrayList<Rectangle> menuRectangles;
    private float bgX = -50;
    private int bgSlideDir = 1;
    private int cursorMinY = 480;
    private int cursorMaxY = 700;
    private int cursorX = 280;
    private int cursorY = cursorMinY;
    private int cursorYStep = (cursorMaxY - cursorMinY) / 5;
    private int selectedIndex = 0;
    private int alphaFade = 255;
    private boolean fadeInActive = true;
    private boolean fadeOutActive = false;

    private static final int TESTING = 0;
    private static final int NEW_GAME = 1;
    private static final int LOAD_SAVE = 2;
    private static final int LEVEL_EDITOR = 3;
    private static final int OPTIONS = 4;
    private static final int QUIT = 5;


    public MainMenu(Game game, OptionsMenu optionsMenu) {
        super(game);
        this.optionsMenu = optionsMenu;
        this.audioPlayer = game.getAudioPlayer();
        this.loadSaveMenu = new LoadSaveMenu(game);
        bgImg = ResourceLoader.getExpImageBackground(ResourceLoader.LEVEL_SELECT_BG);
        cursorImg = ResourceLoader.getExpImageSprite(ResourceLoader.CURSOR_SPRITE_WHITE);
        titleImg = ResourceLoader.getExpImageBackground(ResourceLoader.MAIN_MENU_TITLE);
        menuFont = ResourceLoader.getNameFont();
        makeMenuRectangles();
    }

    private void makeMenuRectangles() {
        this.menuRectangles = new ArrayList<>();
        for (int i = 0; i < alternatives.length; i++) {
            Rectangle rect = new Rectangle(
                (int) (425 * Game.SCALE), (int) ((450 + i * cursorYStep) * Game.SCALE),
                (int) (200 * Game.SCALE), (int) (50 * Game.SCALE));
            menuRectangles.add(rect);
        }
    }

    private void handleKeyBoardInputs() {
        if (game.upIsPressed) {
            game.upIsPressed = false;
            audioPlayer.playSFX(Audio.SFX_CURSOR);
            moveCursorUp();
            reduceIndex();
        }
        else if (game.downIsPressed) {
            game.downIsPressed = false;
            audioPlayer.playSFX(Audio.SFX_CURSOR);
            moveCursorDown();
            increaseIndex();
        }
        else if (game.interactIsPressed) {
            this.handleInteractPressed();
        }
    }
    
    private void handleInteractPressed() {
        game.interactIsPressed = false;
        if (selectedIndex == TESTING) {
            audioPlayer.stopAllLoops();
            audioPlayer.playSFX(Audio.SFX_STARTGAME);
            this.enterTestingMode();
        } 
        else if (selectedIndex == NEW_GAME) {
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
            this.loadSaveMenu.activate(LoadSaveMenu.NEW_GAME);
        } 
        else if (selectedIndex == LOAD_SAVE) {
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
            this.loadSaveMenu.activate(LoadSaveMenu.LOAD_SAVE);
        } 
        else if (selectedIndex == LEVEL_EDITOR) {
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
            audioPlayer.stopAllLoops();
            Gamestate.state = Gamestate.LEVEL_EDITOR;
        } 
        else if (selectedIndex == OPTIONS) {
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
            optionsMenu.setActive(true);
        } 
        else {
            Gamestate.state = Gamestate.QUIT;
        }
    }

    /** In testing mode, the game will not load any data, and will
     * not save any data. */
    private void enterTestingMode() {
        // Activate testing mode
        game.testingMode = true;

        // UNLOCK LEVELS
        game.getLevelSelect().unlockAllLevelsUpTo(4);

        // LEVEL SELECT
        game.getLevelSelect().reset();
        Gamestate.state = Gamestate.LEVEL_SELECT;

        // EXPLORING - Uncomment to only test one level in exploring.
        // game.getExploring().loadLevel(1); // Area is currently specified in that method.
        // game.getExploring().update();
        // Gamestate.state = Gamestate.EXPLORING;

        // FLYING - Uncomment to only test one level in flying.
        // game.getFlying().loadLevel(4);
        // game.getFlying().update();
        // Gamestate.state = Gamestate.FLYING;

        // BOSSMODE - Uncomment to only test one boss.
        // game.getBossMode().loadNewBoss(1);
        // game.getBossMode().update();
        // Gamestate.state = Gamestate.BOSS_MODE;
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
        else if (optionsMenu.isActive()) {
            optionsMenu.update();
        }
        else if (loadSaveMenu.isActive()) {
            loadSaveMenu.update();
        }
        else {
            handleKeyBoardInputs();
        }
    }

    private void moveBackGround() {
        this.bgX += 0.05f * bgSlideDir;
        if (this.bgX > 0) {bgSlideDir *= -1;}
        else if (this.bgX < -50) {bgSlideDir *= -1;}
    }

    public void startTransitionToGame() {
        this.fadeOutActive = true;
        audioPlayer.stopAllLoops();
        audioPlayer.playSFX(Audio.SFX_STARTGAME);
    }

    private void updateFadeIn() {
        this.alphaFade -= 5;
        if (alphaFade < 0) {
            alphaFade = 0;
            fadeInActive = false;
        }
    }

    /** Also handles transition to new state */
    private void updateFadeOut() {
        this.alphaFade += 5;
        if (alphaFade > 255) {
            this.loadSaveMenu.deActivate();
            alphaFade = 255;
            startGame();
        }
    }

    /** Is called when after fadeOut is completed.
     * For now, it takes you to Level Select. In the future, we might
     * make it so that if it's a new game, it goes straight to Exploring in lvl 1.
     */
    private void startGame() {
        game.getLevelSelect().reset();
        game.getLevelSelect().transferDataFromSave();
        Gamestate.state = Gamestate.LEVEL_SELECT;
    }


    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Background
        g.drawImage(bgImg, 
            (int) bgX, 0, Game.GAME_WIDTH + 50, Game.GAME_HEIGHT + 50, null);

        // Text
        g.setColor(Color.WHITE);
        g.setFont(menuFont);
        for (int i = 0; i < alternatives.length; i++) {
            DrawCenteredString(g2, alternatives[i], menuRectangles.get(i), menuFont);
        }
        g.drawImage(
            titleImg, 
            (int) (280 * Game.SCALE), 
            (int) (100 * Game.SCALE), 
            (int) (500 * Game.SCALE), 
            (int) (200 * Game.SCALE),
            null);

        // Cursor
        g.drawImage(
            cursorImg, 
            (int) (cursorX * Game.SCALE), 
            (int) ((cursorY - CURSOR_HEIGHT/2) * Game.SCALE), 
            (int) (CURSOR_WIDTH * Game.SCALE), 
            (int) (CURSOR_HEIGHT * Game.SCALE),
            null);
        
        // Options
        if (optionsMenu.isActive()) {
            optionsMenu.draw(g);
        }
        // LoadSave Menu
        else if (loadSaveMenu.isActive()) {
            loadSaveMenu.draw(g);
        }

        // Fade
        if (fadeInActive || fadeOutActive) {
            g.setColor(new Color(0, 0, 0, alphaFade));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        }
    }

    private void increaseIndex() {
        selectedIndex = (selectedIndex + 1) % alternatives.length;
    }

    private void reduceIndex() {
        selectedIndex -= 1;
        if (selectedIndex < 0) {
            selectedIndex = alternatives.length - 1;
        }
    }

    private void moveCursorUp() {
        cursorY -= cursorYStep;
        if (cursorY < cursorMinY) {
            cursorY = cursorMaxY;
        }
    }

    private void moveCursorDown() {
        cursorY += cursorYStep;
        if (cursorY > cursorMaxY) {
            cursorY = cursorMinY;
        }
    }

    /** Should be called whenever the player returns to the main menu */
    public void reset() {
        game.testingMode = false;
        this.fadeInActive = true;
        this.fadeOutActive = false;
        this.alphaFade = 255;
        audioPlayer.startSong(Audio.SONG_MAIN_MENU, 0, true);
    }
}
