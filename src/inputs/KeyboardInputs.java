package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gamestates.Gamestate;
import main.Game;
import main.GamePanel;

public class KeyboardInputs implements KeyListener {
    private Game game;
    private KeyEvent oldTypedKey;   // Used to update keybindings
    private KeyEvent newTypedKey;
    private String[] keyBindings = {"W", "S", "D", "A", "Space", "B", "M", "Enter"}; // Indexes are important

    // Default key bindings
    public int up = KeyEvent.VK_W;
    public int down = KeyEvent.VK_S;
    public int right = KeyEvent.VK_D;
    public int left = KeyEvent.VK_A;
    public int interact = KeyEvent.VK_SPACE;
    public int shootBomb = KeyEvent.VK_B;
    public int teleport = KeyEvent.VK_M;
    public int pause = KeyEvent.VK_ENTER;

    public KeyboardInputs(GamePanel gamePanel, Game game) {
        this.game = game;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == up) {
            game.upIsPressed = true;
        }
        if (e.getKeyCode() == down) {
            game.downIsPressed = true;
        }
        if (e.getKeyCode() == right) {
            game.rightIsPressed = true;
        }
        if (e.getKeyCode() == left) {
            game.leftIsPressed = true;
        }
        if (e.getKeyCode() == interact) {
            game.interactIsPressed = true;
        }
        if (e.getKeyCode() == teleport) {
            game.teleportIsPressed = true;
        }
        if (e.getKeyCode() == shootBomb) {
            game.bombIsPressed = true;
        }
        if (e.getKeyCode() == pause) {
            game.pauseIsPressed = true;
        }
        if (Gamestate.state == Gamestate.LEVEL_EDITOR) {
            game.getLevelEditor().handleKeyboardInputs(e);
        }
        oldTypedKey = newTypedKey;  
        newTypedKey = e;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == up) {
            game.upIsPressed = false;
        }
        if (e.getKeyCode() == down) {
            game.downIsPressed = false;
        }
        if (e.getKeyCode() == right) {
            game.rightIsPressed = false;
        }
        if (e.getKeyCode() == left) {
            game.leftIsPressed = false;
        }
        if (e.getKeyCode() == interact) {
            game.interactIsPressed = false;
        }
        if (e.getKeyCode() == teleport) {
            game.teleportIsPressed = false;
        }
        if (e.getKeyCode() == shootBomb) {
            game.bombIsPressed = false;
        }
        if (e.getKeyCode() == pause) {
            game.pauseIsPressed = false;
            }
        }

    /** Should be called when the player is in controls menu, 
     * after they have typed a new key and confirmed with 'interact'. */
    public String updateKeybindings(int index) {
        String newKey = KeyEvent.getKeyText(oldTypedKey.getKeyCode());
        this.keyBindings[index] = newKey;
        updateKeyPressed(index);
        return newKey;
    }

    /** Is used to show the player the latest key they typed */
    public String updateLatestKey(int index) {
        String newKey = KeyEvent.getKeyText(newTypedKey.getKeyCode());
        this.keyBindings[index] = newKey;
        return newKey;
    }

    public String[] getKeyBindings() {
        return keyBindings;
    }

    /** Updates the actual keyBindings for the game */
    private void updateKeyPressed(int index) {
        int keyCode = oldTypedKey.getKeyCode();
        switch (index) {
            case 0 -> up = keyCode;
            case 1 -> down = keyCode;
            case 2 -> right = keyCode;
            case 3 -> left = keyCode;
            case 4 -> interact = keyCode;
            case 5 -> shootBomb = keyCode;
            case 6 -> teleport = keyCode;
            case 7 -> pause = keyCode;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
