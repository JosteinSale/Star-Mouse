package gamestates.level_select;

import static utils.Constants.UI.LEVEL_ICON_DRAW_SIZE;
import static utils.Constants.UI.LEVEL_SELECT_BOX_SIZE;
import static utils.HelpMethods.DrawCenteredString;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import audio.AudioPlayer;
import main_classes.Game;
import utils.LoadSave;
import utils.Constants.Audio;

/** 
 * In LevelLayout1, the unlocked levels are displayed in a single line, and the player
 * can only enter the last one at any given time.
 * The cursor can move back and forth, to display info about a given level.
 */
public class LevelLayout1 implements ILevelLayout {
    private Game game;
    private AudioPlayer audioPlayer;
    private Font font;
    private BufferedImage[] allLevelIcons;
    private ArrayList<LevelInfo> allLevelInfo;

    private ArrayList<Integer> levelsInCurrentPath;
    private BufferedImage layoutImg;
    private BufferedImage cursorBox;

    private Rectangle lvlNameRect;
    private Rectangle killCountRect;
    private int selectedIndex = 0;
    private int cursorX = 110;
    private int cursorY = 315;
    private int imageDist = 182;

    private int layoutX = 150;
    private int layoutY = 360;
    private int layoutW = 777;
    private int layoutH = 45;

    public LevelLayout1(Game game, BufferedImage[] levelIcons, ArrayList<LevelInfo> levelInfo) {
        this.game = game;
        this.audioPlayer = game.getAudioPlayer();
        this.allLevelIcons = levelIcons;
        this.allLevelInfo = levelInfo;
        this.levelsInCurrentPath = new ArrayList<>();
        levelsInCurrentPath.add(1);
        // Add more levels to test.

        this.layoutImg = LoadSave.getExpImageBackground(LoadSave.LEVEL_SELECT_LAYOUT1);
        this.cursorBox = LoadSave.getExpImageSprite(LoadSave.LEVEL_SELECT_BOX);
        this.font = LoadSave.getMenuFont();
        this.lvlNameRect = new Rectangle(
            (int) ((cursorX + 16) * Game.SCALE), 
            (int) ((cursorY - 60) * Game.SCALE), 
            (int) (LEVEL_ICON_DRAW_SIZE * Game.SCALE), 
            (int) (50 * Game.SCALE));
        this.killCountRect = new Rectangle(lvlNameRect);
        this.killCountRect.y += (int) (195 * Game.SCALE);
    }

    @Override
    public void update() {
        handleKeyboardInputs();
    }

    private void handleKeyboardInputs() {
        if (game.interactIsPressed) {
            game.interactIsPressed = false;
            if (selectedIndex != levelsInCurrentPath.size() - 1) {
                audioPlayer.playSFX(Audio.SFX_HURT);
            }
            else {
                audioPlayer.playSFX(Audio.SFX_CURSOR_SELECT);
                int lvl = levelsInCurrentPath.get(selectedIndex);
                this.game.getLevelSelect().goToLevel(lvl);
            }
        }
        else if (game.rightIsPressed) {
            game.rightIsPressed = false;
            audioPlayer.playSFX(Audio.SFX_CURSOR);
            if (selectedIndex < 4) {
                selectedIndex ++;
                cursorX += imageDist;
                lvlNameRect.x += (int) (imageDist * Game.SCALE);
                killCountRect.x += (int) (imageDist * Game.SCALE);
            }
        }
        else if (game.leftIsPressed) {
            game.leftIsPressed = false;
            audioPlayer.playSFX(Audio.SFX_CURSOR);
            if (selectedIndex > 0) {
                selectedIndex --;
                cursorX -= imageDist;
                lvlNameRect.x -= (int) (imageDist * Game.SCALE);
                killCountRect.x -= (int) (imageDist * Game.SCALE);
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        // Layout
        g.drawImage(
            layoutImg, 
            (int) (layoutX * Game.SCALE), (int) (layoutY * Game.SCALE), 
            (int) (layoutW * Game.SCALE), (int) (layoutH * Game.SCALE), null);
        
        // Level icons
        for (int i = 0; i < levelsInCurrentPath.size(); i++) {
            int imageIndex = levelsInCurrentPath.get(i) - 1;
            g.drawImage(
                allLevelIcons[imageIndex], 
                (int) ((125 + i * imageDist) * Game.SCALE), 
                (int) (330 * Game.SCALE), 
                (int) (LEVEL_ICON_DRAW_SIZE * Game.SCALE), 
                (int) (LEVEL_ICON_DRAW_SIZE * Game.SCALE), null);
        }

        // CursorBox + levelInfo
        g.drawImage(cursorBox, 
            (int) (cursorX * Game.SCALE), (int) (cursorY * Game.SCALE), 
            (int) (LEVEL_SELECT_BOX_SIZE * Game.SCALE), 
            (int) (LEVEL_SELECT_BOX_SIZE * Game.SCALE), null);
        
        if (selectedIndex < levelsInCurrentPath.size()) {
            int level = levelsInCurrentPath.get(selectedIndex) - 1;
            LevelInfo lvl = allLevelInfo.get(level);
            String name = lvl.getName();
            Integer killCount = lvl.getKillCount();
            Integer totalEnemies = lvl.getTotalEnemies();
            g.setFont(font);
            g.setColor(Color.WHITE);
            DrawCenteredString(g, name, lvlNameRect, font);
            DrawCenteredString(g, killCount.toString() + "/" + totalEnemies.toString(), killCountRect, font);
        }
        
    }

    @Override
    public void setUnlocked(int level) {
        // levelsInCurrentPath.add(level);  // Uncomment when we have all the levels ready :P
        // For now, just add them manually in the constructor
        levelsInCurrentPath.add(2);  // Simulates going from level 1 to 2.
    }
    
}
