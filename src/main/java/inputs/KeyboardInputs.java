package inputs;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input;

import gamestates.Gamestate;
import main_classes.Game;

public class KeyboardInputs implements InputProcessor {
    private Game game;
    private int newTypedKey; // The most recently typed key
    private int oldTypedKey; // The second most recently typed key

    // Default key bindings
    public int up = Input.Keys.W;
    public int down = Input.Keys.S;
    public int right = Input.Keys.D;
    public int left = Input.Keys.A;
    public int interact = Input.Keys.SPACE;
    public int shootBomb = Input.Keys.B;
    public int teleport = Input.Keys.M;
    public int pause = Input.Keys.ENTER;

    /*
     * An array of the string representation of the keybindings,
     * used for display in the controls menu.
     * OBS: the indexes are important.
     */
    private String[] keyBindingNames = {
            Input.Keys.toString(up),
            Input.Keys.toString(down),
            Input.Keys.toString(right),
            Input.Keys.toString(left),
            Input.Keys.toString(interact),
            Input.Keys.toString(shootBomb),
            Input.Keys.toString(teleport),
            Input.Keys.toString(pause) };

    public KeyboardInputs(Game game) {
        this.game = game;
    }

    /**
     * Should be called when the player is in controls menu,
     * after they have typed a new key and confirmed with 'interact'.
     */
    public String updateKeybindings(int index) {
        // Since 'interact' was the newest typed key, we use the 'oldTypedKey'
        // to get the actual key the player wanted to set.
        String newKey = Input.Keys.toString(oldTypedKey);
        this.keyBindingNames[index] = newKey;
        remapKeyPressed(index);
        return newKey;
    }

    /** Is used to show the player the latest key they typed */
    public String updateLatestKey(int index) {
        String newKey = Input.Keys.toString(newTypedKey);
        this.keyBindingNames[index] = newKey;
        return newKey;
    }

    public String[] getKeyBindingNames() {
        return keyBindingNames;
    }

    /** Updates the actual keyBindings for the game */
    private void remapKeyPressed(int index) {
        int keyCode = oldTypedKey;
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
    public boolean keyDown(int keycode) {
        if (keycode == up) {
            game.upIsPressed = true;
        }
        if (keycode == down) {
            game.downIsPressed = true;
        }
        if (keycode == right) {
            game.rightIsPressed = true;
        }
        if (keycode == left) {
            game.leftIsPressed = true;
        }
        if (keycode == interact) {
            game.interactIsPressed = true;
        }
        if (keycode == teleport) {
            game.teleportIsPressed = true;
        }
        if (keycode == shootBomb) {
            game.bombIsPressed = true;
        }
        if (keycode == pause) {
            game.pauseIsPressed = true;
        }
        if (Gamestate.state == Gamestate.LEVEL_EDITOR) {
            // game.getLevelEditor().handleKeyboardInputs(keycode); // TODO
        }
        oldTypedKey = newTypedKey;
        newTypedKey = keycode;

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == up) {
            game.upIsPressed = false;
        }
        if (keycode == down) {
            game.downIsPressed = false;
        }
        if (keycode == right) {
            game.rightIsPressed = false;
        }
        if (keycode == left) {
            game.leftIsPressed = false;
        }
        if (keycode == interact) {
            game.interactIsPressed = false;
        }
        if (keycode == teleport) {
            game.teleportIsPressed = false;
        }
        if (keycode == shootBomb) {
            game.bombIsPressed = false;
        }
        if (keycode == pause) {
            game.pauseIsPressed = false;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
