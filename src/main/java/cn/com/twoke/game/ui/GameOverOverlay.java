package cn.com.twoke.game.ui;

import cn.com.twoke.game.gamestates.GameState;
import cn.com.twoke.game.gamestates.Playing;
import cn.com.twoke.game.main.Game;
import cn.com.twoke.game.utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static cn.com.twoke.game.utils.Constants.UI.PauseButtons.URM_SIZE;

public class GameOverOverlay {

    private Playing playing;
    private BufferedImage screen;
    private int screenX,screenY,screenW,screenH;
    private UrmButton menu, replay;

    public GameOverOverlay(Playing playing) {
        this.playing = playing;
        createScreenImg();
        createButtons();
    }

    private void createButtons() {
        int menuX = (int)(335 * Game.SCALE);
        int replayX = (int)(440 * Game.SCALE);
        int y = (int)(195 * Game.SCALE);
        replay = new UrmButton(replayX, y, URM_SIZE, URM_SIZE, 1);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    private void createScreenImg() {
        screen = LoadSave.GetSpriteAtlas(LoadSave.DEATH_SCREEN);
        screenW = (int)(Game.SCALE * screen.getWidth());
        screenH = (int)(Game.SCALE * screen.getHeight());
        screenX = Game.GAME_WIDTH / 2 - screenW / 2;
        screenY = (int)(100 * Game.SCALE);
    }


    public void draw(Graphics g) {
        g.setColor(new Color(0 , 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(screen, screenX, screenY, screenW, screenH, null);
        menu.draw(g);
        replay.draw(g);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            playing.resetAll();
            GameState.currentState = GameState.MENU;
        }
    }


    public void mouseMoved(MouseEvent e) {
        replay.setMouseOver(false);
        menu.setMouseOver(false);

        if (menu.isIn(e)) {
            menu.setMouseOver(true);
        } else if (replay.isIn(e)) {
            replay.setMouseOver(true);
        }
    }
    public void mouseReleased(MouseEvent e) {
        if (replay.isIn(e) && replay.isMousePressed()) {
            playing.resetAll();
            playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLvlIndex());
        }
        else if (menu.isIn(e) && menu.isMousePressed()) {
            playing.resetAll();
            playing.setGameState(GameState.MENU);
        }

        menu.resetBools();
        replay.resetBools();
    }
    public void mousePressed(MouseEvent e) {
        if (replay.isIn(e)) {
            replay.setMousePressed(true);
        }
        else if (menu.isIn(e)) {
            menu.setMousePressed(true);
        }
    }

    public void update() {
        menu.update();
        replay.update();
    }
}
