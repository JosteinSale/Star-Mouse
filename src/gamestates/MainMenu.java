package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import audio.AudioPlayer;
import main.Game;
import ui.OptionsMenu;
import utils.LoadSave;
import utils.Constants.Audio;

import static utils.Constants.UI.*;
import static utils.HelpMethods.DrawCenteredString;

public class MainMenu extends State implements Statemethods {
    private AudioPlayer audioPlayer;
    private BufferedImage bgImg;
    private BufferedImage titleImg;
    private BufferedImage cursorImg;
    private Font menuFont;
    private OptionsMenu optionsMenu;
    private String[] alternatives = {"New Game", "Level Editor", "Options", "Quit"};
    private ArrayList<Rectangle> menuRectangles;
    private int cursorMinY = 480;
    private int cursorMaxY = 630;
    private int cursorX = 280;
    private int cursorY = cursorMinY;
    private int cursorYStep = (cursorMaxY - cursorMinY) / 3;
    private int selectedIndex = 0;

    private static final int NEW_GAME = 0;
    private static final int LEVEL_EDITOR = 1;
    private static final int OPTIONS = 2;
    private static final int QUIT = 3;


    public MainMenu(Game game, OptionsMenu optionsMenu) {
        super(game);
        this.optionsMenu = optionsMenu;
        this.audioPlayer = game.getAudioPlayer();
        bgImg = LoadSave.getExpImageBackground(LoadSave.MAIN_MENU_BG);
        cursorImg = LoadSave.getExpImageSprite(LoadSave.CURSOR_SPRITE_WHITE);
        titleImg = LoadSave.getExpImageBackground(LoadSave.MAIN_MENU_TITLE);
        menuFont = LoadSave.getNameFont();
        audioPlayer.startAmbienceLoop(Audio.AMBIENCE_SILENCE);  
        audioPlayer.startSongLoop(Audio.SONG_ACADEMY);
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
        else if (game.spaceIsPressed) {
            game.spaceIsPressed = false;
            if (selectedIndex == NEW_GAME) {
                audioPlayer.stopAllLoops();
                audioPlayer.playSFX(Audio.SFX_STARTGAME);
                this.game.getExploring().update(); 
                Gamestate.state = Gamestate.EXPLORING;
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
        if (optionsMenu.isActive()) {
            optionsMenu.update();
        } 
        else {
            handleKeyBoardInputs();
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Background
        //g.setColor(Color.BLACK);
        //g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        g.drawImage(bgImg, 
            0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

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
}
