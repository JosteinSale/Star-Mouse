package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import main.Game;
import utils.LoadSave;
import utils.Constants.Audio;

import static utils.Constants.Exploring.Sprites.STANDARD_SPRITE_HEIGHT;
import static utils.Constants.Exploring.Sprites.STANDARD_SPRITE_WIDTH;

public class StartScreen extends State implements Statemethods {
    private BufferedImage mouseImg;
    private int mouseImgW;
    private int mouseImgH;
    private AudioPlayer audioPlayer;
    private Font font;

    public StartScreen(Game game) {
        super(game);
        this.audioPlayer = game.getAudioPlayer();
        mouseImg = LoadSave.getExpImageSprite(LoadSave.BASIC_MOUSE);
        mouseImgW = 100 * 3;
        mouseImgH = 100 * 3;
        font = LoadSave.getInfoFont();
    }

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
        // Background
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        // MouseImg
        g.drawImage(mouseImg, 
            (int) ((Game.GAME_DEFAULT_WIDTH / 2 - mouseImgW / 2) * Game.SCALE),
            (int) ((Game.GAME_DEFAULT_HEIGHT / 2 - mouseImgH / 2) * Game.SCALE), 
            (int) (mouseImgW * Game.SCALE), 
            (int) (mouseImgH * Game.SCALE), null);
        
        // Text
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(
            "Press SPACE", 
            (int) (430 * Game.SCALE), (int) (600 * Game.SCALE));
    }
 
}
