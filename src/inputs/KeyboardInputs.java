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
        if (Gamestate.state == Gamestate.LEVEL_EDITOR) {
            game.getLevelEditor().handleKeyboardInputs(e);
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
        }
    
    @Override
    public void keyTyped(KeyEvent e) {}   // Could use this later to set custom keybindings :)
}
