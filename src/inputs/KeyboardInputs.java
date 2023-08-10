package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gamestates.Gamestate;
import main.Game;
import main.GamePanel;

public class KeyboardInputs implements KeyListener {
    Game game;

    public KeyboardInputs(GamePanel gamePanel, Game game) {
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(Gamestate.state) {
            case START_SCREEN:
                game.getStartScreen().keyPressed(e);
                break;
            case MAIN_MENU:
                game.getMainMenu().keyPressed(e);
                break;
            case LEVEL_SELECT:
                game.getLevelSelect().keyPressed(e);
                break;
            case EXPLORING:
                game.getExploring().keyPressed(e);
                break;
            case FLYING:
                game.getFlying().keyPressed(e);
                break;
            case LEVEL_EDITOR:
                game.getLevelEditor().keyPressed(e);
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(Gamestate.state) {
            case START_SCREEN:
                game.getStartScreen().keyReleased(e);
                break;
            case MAIN_MENU:
                game.getMainMenu().keyReleased(e);
                break;
            case LEVEL_SELECT:
                game.getLevelSelect().keyReleased(e);
                break;
            case EXPLORING:
                game.getExploring().keyReleased(e);
                break;
            case FLYING:
                game.getFlying().keyReleased(e);
                break;
            case LEVEL_EDITOR:
                game.getLevelEditor().keyReleased(e);
                break;
            default:
                break;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
}
