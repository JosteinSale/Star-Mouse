package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import main.Game;
import utils.LoadSave;
import utils.Constants.Audio;

import static utils.Constants.UI.*;

public class MainMenu extends State implements Statemethods {
    private AudioPlayer audioPlayer;
    private BufferedImage bgImg;
    private BufferedImage cursorImg;
    private int cursorMinY = 490;
    private int cursorMaxY = 670;
    private int cursorX = 280;
    private int cursorY = cursorMinY;
    private int cursorYStep = (cursorMaxY - cursorMinY) / 3;
    private int selectedIndex = 0;

    public MainMenu(Game game) {
        super(game);
        this.audioPlayer = game.getAudioPlayer();
        bgImg = LoadSave.getExpImageBackground(LoadSave.MAIN_MENU_BG);
        cursorImg = LoadSave.getExpImageSprite(LoadSave.CURSOR_SPRITE_BLACK);
        audioPlayer.startAmbienceLoop(Audio.AMBIENCE_SILENCE);   // Nescessary because stupid
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            audioPlayer.playSFX(Audio.SFX_CURSOR);
            moveCursorUp();
            reduceIndex();
        }
        else if (e.getKeyCode() == KeyEvent.VK_S) {
            audioPlayer.playSFX(Audio.SFX_CURSOR);
            moveCursorDown();
            increaseIndex();
        }
        else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (selectedIndex == 0) {
                audioPlayer.stopAllLoops();
                audioPlayer.playSFX(Audio.SFX_STARTGAME);
                //this.game.getExploring().update();  // uncomment
                Gamestate.state = Gamestate.EXPLORING;
            }
            else if (selectedIndex == 1) {
                audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
                Gamestate.state = Gamestate.LEVEL_EDITOR;
            }
            else if (selectedIndex == 2) {
                audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
                //TODO
            }
            else {
                Gamestate.state = Gamestate.QUIT;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    
    @Override
    public void update() {}

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
