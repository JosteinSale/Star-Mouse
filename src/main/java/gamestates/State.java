package gamestates;

import main_classes.Game;
import utils.Singleton;

public class State extends Singleton {
    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return this.game;
    }

}
