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
import ui.OptionsMenu;
import utils.LoadSave;
import utils.Constants.Audio;

public class MainMenu extends State implements Statemethods {
    
    private AudioPlayer audioPlayer;
    private BufferedImage bgImg;
    private BufferedImage titleImg;
    private BufferedImage cursorImg;
    private Font menuFont;
    private OptionsMenu optionsMenu;
    private String[] alternatives = {"New Game", "Level Editor", "Options", "Quit"};
    private ArrayList<Rectangle> menuRectangles;
    private float bgX = -50;
    private int bgSlideDir = 1;
    private int cursorMinY = 480;
    private int cursorMaxY = 630;
    private int cursorX = 280;
    private int cursorY = cursorMinY;
    private int cursorYStep = (cursorMaxY - cursorMinY) / 3;
    private int selectedIndex = 0;
    private int alphaFade = 255;
    private boolean fadeInActive = true;
    private boolean fadeOutActive = false;


    private static final int NEW_GAME = 0;
    private static final int LEVEL_EDITOR = 1;
    private static final int OPTIONS = 2;
    private static final int QUIT = 3;


    public MainMenu(Game game, OptionsMenu optionsMenu) {
        super(game);
        this.optionsMenu = optionsMenu;
        this.audioPlayer = game.getAudioPlayer();
        bgImg = LoadSave.getExpImageBackground(LoadSave.LEVEL_SELECT_BG);
        cursorImg = LoadSave.getExpImageSprite(LoadSave.CURSOR_SPRITE_WHITE);
        titleImg = LoadSave.getExpImageBackground(LoadSave.MAIN_MENU_TITLE);
        menuFont = LoadSave.getNameFont();
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

    /** Is called when player selects 'New Game', after fadeOut is completed */
    private void doTransition() {
        //LEVEL-SELECT - Uncomment to test entire game
        //game.getLevelSelect().reset();
        //Gamestate.state = Gamestate.LEVEL_SELECT;

        // EXPLORING - Uncomment to only test one level in exploring.
        // game.getExploring().loadLevel(2); // Area is currently specified in that method.
        // game.getExploring().update();
        // Gamestate.state = Gamestate.EXPLORING;

        // FLYING - Uncomment to only test one level in flying.
        // game.getFlying().loadLevel(2);
        // game.getFlying().update();
        // Gamestate.state = Gamestate.FLYING;

        // BOSSMODE - Uncomment to only test one boss.
        game.getBossMode().loadNewBoss(1);
        game.getBossMode().update();
        Gamestate.state = Gamestate.BOSS_MODE;
        audioPlayer.startSongLoop(Audio.SONG_BOSS1, 0f); // Temporary
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
            game.interactIsPressed = false;
            if (selectedIndex == NEW_GAME) {
                fadeOutActive = true;
                audioPlayer.stopAllLoops();
                audioPlayer.playSFX(Audio.SFX_STARTGAME);
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
        else {
            handleKeyBoardInputs();
        }
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

    /** Also handles transition to new state */
    private void updateFadeOut() {
        this.alphaFade += 5;
        if (alphaFade > 255) {
            alphaFade = 255;
            doTransition();
        }
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
        
        // Fade
        if (fadeInActive || fadeOutActive) {
            g.setColor(new Color(0, 0, 0, alphaFade));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        }
        // Options
        if (optionsMenu.isActive()) {
            optionsMenu.draw(g);
        }
    }

    private void increaseIndex() {
        selectedIndex = (selectedIndex + 1) % 4;
    }

    private void reduceIndex() {
        selectedIndex -= 1;
        if (selectedIndex < 0) {
            selectedIndex = 3;
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
        this.fadeInActive = true;
        this.fadeOutActive = false;
        this.alphaFade = 255;
        audioPlayer.startSongLoop(Audio.SONG_MAIN_MENU, 0);
    }
}
