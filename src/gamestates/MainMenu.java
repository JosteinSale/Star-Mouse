package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import main.Game;
import ui.OptionsMenu;
import utils.LoadSave;
import utils.Constants.Audio;

import static utils.Constants.UI.*;

public class MainMenu extends State implements Statemethods {
    private AudioPlayer audioPlayer;
    private BufferedImage bgImg;
    private BufferedImage cursorImg;
    private OptionsMenu optionsMenu;
    private int cursorMinY = 490;
    private int cursorMaxY = 670;
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
        cursorImg = LoadSave.getExpImageSprite(LoadSave.CURSOR_SPRITE_BLACK);
        audioPlayer.startAmbienceLoop(Audio.AMBIENCE_SILENCE);  
        audioPlayer.startSongLoop(Audio.SONG_ACADEMY);
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
        g.drawImage(bgImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
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
