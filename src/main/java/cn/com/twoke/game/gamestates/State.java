package cn.com.twoke.game.gamestates;

import cn.com.twoke.game.audio.AudioPlayer;
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

    public void setGameState(GameState gameState) {
        switch (gameState) {
            case MENU:
                game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
                break;
            case PLAYING: game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLvlIndex());
        }
        GameState.currentState = gameState;
    }

}
