package gamestates;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import main.Game;
import utils.LoadSave;

public class LevelSelect extends State implements Statemethods {
    // TODO - change background and behavior based on which playthrough we're at.
    private int playThrough = 1;
    private BufferedImage bgImg;
    private AudioPlayer audioPlayer;
    

    public LevelSelect(Game game) {
        super(game);
        this.audioPlayer = game.getAudioPlayer();
        bgImg = LoadSave.getExpImageBackground(LoadSave.LEVEL_SELECT_BG);

    }

    @Override
    public void update() {
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(bgImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
    }
}
