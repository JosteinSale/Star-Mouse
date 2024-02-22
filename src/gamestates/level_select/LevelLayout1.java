package gamestates.level_select;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import audio.AudioPlayer;
import gamestates.State;
import gamestates.Statemethods;
import main.Game;

public class LevelLayout1 extends State implements Statemethods {
    private ArrayList<BufferedImage> levelIcons;
    private AudioPlayer audioPlayer;
    private int selectedLevel = 0;
    private int cursorX;
    private int cursorY;
    private int layoutX;
    private int layoutY;

    public LevelLayout1(Game game, ArrayList<BufferedImage> levelIcons) {
        super(game);
        this.audioPlayer = game.getAudioPlayer();
        this.levelIcons = levelIcons;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics g) {

    }
    
}
