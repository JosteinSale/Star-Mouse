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
        if (e.getKeyCode() == KeyEvent.VK_W) {
            game.upIsPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            game.downIsPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            game.rightIsPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            game.leftIsPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            game.spaceIsPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_M) {
            game.mIsPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_B) {
            game.bIsPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            game.enterIsPressed = true;
        }


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
        if (e.getKeyCode() == KeyEvent.VK_W) {
            game.upIsPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            game.downIsPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            game.rightIsPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            game.leftIsPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            game.spaceIsPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_M) {
            game.mIsPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_B) {
            game.bIsPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            game.enterIsPressed = false;
        }

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
