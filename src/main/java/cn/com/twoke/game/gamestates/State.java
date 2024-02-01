package cn.com.twoke.game.gamestates;

import cn.com.twoke.game.main.Game;
import cn.com.twoke.game.ui.MenuButton;
import javafx.scene.input.MouseButton;

import java.awt.event.MouseEvent;

public class State {

    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public boolean isIn(MouseEvent e, MenuButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }

    public Game getGame() {
        return game;
    }
}
