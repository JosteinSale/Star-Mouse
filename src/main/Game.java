package main;

import gamestates.Exploring;
import gamestates.Flying;
import gamestates.Gamestate;
import gamestates.LevelEditor;
import gamestates.MainMenu;
import gamestates.StartScreen;
import gamestates.level_select.LevelSelect;
import ui.OptionsMenu;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import audio.AudioPlayer;


public class Game implements Runnable {
    public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int SCREEN_WIDTH = (int) screenSize.getWidth();
    public static final int SCREEN_HEIGHT = (int) screenSize.getHeight();

    public static final int GAME_DEFAULT_WIDTH = 1050;
    public static final int GAME_DEFAULT_HEIGHT = 750;
    public static final float SCALE = 0.8f;
    //public static final float SCALE = SCREEN_HEIGHT / (float) GAME_DEFAULT_HEIGHT;

    public static final int GAME_WIDTH = (int) (GAME_DEFAULT_WIDTH * SCALE);
    public static final int GAME_HEIGHT = (int) (GAME_DEFAULT_HEIGHT * SCALE);

    public static final boolean fullScreen = false;

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;     
    private final int FPS_SET = 60;                          // Påvirker prosessor
    private final int UPS_SET = 60;
    
    private StartScreen startScreen;
    private MainMenu mainMenu;
    private LevelSelect levelSelect;
    private Exploring exploring;
    private Flying flying;
    private LevelEditor levelEditor;
    private OptionsMenu optionsMenu;
    private AudioPlayer audioPlayer;

    public boolean upIsPressed = false;
    public boolean downIsPressed = false;
    public boolean rightIsPressed = false;
    public boolean leftIsPressed = false;
    public boolean interactIsPressed = false;
    public boolean teleportIsPressed = false;
    public boolean bombIsPressed = false;
    public boolean pauseIsPressed = false;

    public Game() {
        initClasses();
        this.gamePanel = new GamePanel(this);
        this.gameWindow = new GameWindow(this.gamePanel, SCREEN_WIDTH, SCREEN_HEIGHT);
        gamePanel.requestFocus(true);
        optionsMenu.setKeyboardInputs(gamePanel.getKeyboardInputs());
        startGameLoop();
    }

    private void initClasses() {
        this.audioPlayer = new AudioPlayer();
        this.optionsMenu = new OptionsMenu(this, audioPlayer);
        this.startScreen = new StartScreen(this);
        this.mainMenu = new MainMenu(this, optionsMenu);
        this.exploring = new Exploring(this);
        this.levelSelect = new LevelSelect(this);
        this.flying = new Flying(this);
        this.levelEditor = new LevelEditor(this);
    }

    private void startGameLoop() {
        this.gameThread = new Thread(this);    // Game passes inn som argument i tråden
        this.gameThread.start();
    }


    private void update() {
        audioPlayer.update();
        switch(Gamestate.state) {
            case START_SCREEN:
                startScreen.update();
                break;
            case MAIN_MENU:
                mainMenu.update();
                break;
            case LEVEL_SELECT:
                levelSelect.update();
                break;
            case EXPLORING:
                exploring.update();
                break;
            case FLYING:
                flying.update();
                break;
            case LEVEL_EDITOR:
                levelEditor.update();
                break;
            case QUIT:
            default:
                System.exit(0);
                break;
        }
    }

    public void render(Graphics g) {
        switch(Gamestate.state) {
            case START_SCREEN:
                startScreen.draw(g);
                break;
            case MAIN_MENU:
                mainMenu.draw(g);
                break;
            case LEVEL_SELECT:
                levelSelect.draw(g);
                break;
            case EXPLORING:
                exploring.draw(g);
                break;
            case FLYING:
                flying.draw(g);
                break;
            case LEVEL_EDITOR:
                levelEditor.draw(g);
                break;
            default:
                break;
        }
    }

    public void windowLostFocus() {
        if (Gamestate.state.equals(Gamestate.EXPLORING)) {
            exploring.resetDirBooleans();
        }
    }


    // @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;  // 1 milliard nanosekunder / FPS_SET
        double timePerUpdate = 1000000000.0 / UPS_SET;  // 1 milliard nanosekunder / UPS_SET
        long previousTime = System.nanoTime();
        double deltaU = 0;     // delta UPS
        double deltaF = 0;     // delta FPS


        // Kaller repaint og update hvis riktig tidsintervall har passert
        while (true) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                deltaU--;
                update(); 
            }

            if (deltaF >= 1) {
                deltaF--;
                gamePanel.repaint();
            }

        }
    }

    public StartScreen getStartScreen() {
        return this.startScreen;
    }

    public MainMenu getMainMenu() {
        return this.mainMenu;
    }

    public LevelSelect getLevelSelect() {
        return this.levelSelect;
    }

    public Exploring getExploring() {
        return this.exploring;
    }

    public Flying getFlying() {
        return this.flying;
    }

    public LevelEditor getLevelEditor() {
        return this.levelEditor;
    }

    public AudioPlayer getAudioPlayer() {
        return this.audioPlayer;
    }

    public OptionsMenu getOptionsMenu() {
        return this.optionsMenu;
    }

    public void sleep(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }

    public void resetMainMenu() {
        this.mainMenu.reset();
    }

/* 
    public AudioOptions getAudioOptions() {
        return audioOptions;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    */
}
