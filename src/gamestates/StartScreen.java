package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import main.Game;
import utils.LoadSave;
import utils.Constants.Audio;

public class StartScreen extends State implements Statemethods {
    private BufferedImage bgImg;
    private AudioPlayer audioPlayer;

    public StartScreen(Game game) {
        super(game);
        this.audioPlayer = game.getAudioPlayer();
        bgImg = LoadSave.getExpImageBackground(LoadSave.START_SCREEN_BG);
    }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void update() {
        handleKeyBoardInputs();
    }

    private void handleKeyBoardInputs() {
        if (game.spaceIsPressed) {
            game.spaceIsPressed = false;
            audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
            Gamestate.state = Gamestate.MAIN_MENU;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(bgImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
    }

    
    
}
