package cn.com.twoke.game.gamestates;

import cn.com.twoke.game.main.Game;

public class State {

    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
