package ui;

import main_classes.Game;

public class NumberDisplay {
    private Game game;
    private int[] passCode;
    public int[] currentCode = { 0, 0, 0, 0 };
    public int digitIndex = 0;
    private boolean active = false;

    public NumberDisplay(Game game) {
        this.game = game;
    }

    public void update() {
        handleKeyboardInputs();
    }

    private void handleKeyboardInputs() {
        if (game.rightIsPressed) {
            game.rightIsPressed = false;
            if (digitIndex < 3) {
                digitIndex += 1;
            }
        } else if (game.leftIsPressed) {
            game.leftIsPressed = false;
            if (digitIndex > 0) {
                digitIndex -= 1;
            }
        } else if (game.upIsPressed) {
            game.upIsPressed = false;
            currentCode[digitIndex] = (currentCode[digitIndex] + 1) % 10;
        } else if (game.downIsPressed) {
            game.downIsPressed = false;
            currentCode[digitIndex] -= 1;
            if (currentCode[digitIndex] < 0) {
                currentCode[digitIndex] = 9;
            }
        }
    }

    public void start(int[] passCode) {
        this.passCode = passCode;
        this.active = true;
    }

    public void reset() {
        this.active = false;
        this.digitIndex = 0;
        for (int i = 0; i < 4; i++) {
            currentCode[i] = 0;
        }
    }

    public boolean isCodeCorrect() {
        for (int i = 0; i < 4; i++) {
            if (currentCode[i] != passCode[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean isActive() {
        return this.active;
    }
}
