package cn.com.twoke.game.ui;

import cn.com.twoke.game.gamestates.GameState;
import cn.com.twoke.game.gamestates.Playing;
import cn.com.twoke.game.main.Game;
import cn.com.twoke.game.utils.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static cn.com.twoke.game.utils.Constants.UI.PauseButtons.URM_SIZE;

public class LevelCompleteOverlay {

    private Playing playing;
    private UrmButton menu, next;
    private BufferedImage img;

    private int bgX, bgY, bgW, bgH;

    public LevelCompleteOverlay(Playing playing) {
        this.playing = playing;
        initImg();
        initButtons();
    }

    private void initButtons() {
        int menuX = (int)(330 * Game.SCALE);
        int nextX = (int)(445 * Game.SCALE);
        int y = (int)(195 * Game.SCALE);
        next = new UrmButton(nextX, y, URM_SIZE, URM_SIZE, 0);
        menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    private void initImg() {
        img = LoadSave.GetSpriteAtlas(LoadSave.COMPLETED_SPRITE);
        bgW = (int)(img.getWidth() * Game.SCALE);
        bgH = (int)(img.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int)(75 * Game.SCALE);
    }


    public void update() {
        menu.update();
        next.update();
    }


    public void draw(Graphics g) {
        g.setColor(new Color(0,0,0, 200));
        g.fillRect(0,0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(img, bgX, bgY, bgW, bgH, null);
        menu.draw(g);
        next.draw(g);
    }


    public void mouseMoved(MouseEvent e) {
        next.setMouseOver(false);
        menu.setMouseOver(false);

        if (menu.isIn(e)) {
            menu.setMouseOver(true);
        } else if (next.isIn(e)) {
            next.setMouseOver(true);
        }
    }
    public void mouseReleased(MouseEvent e) {
        if (next.isIn(e) && next.isMousePressed()) {
            playing.loadNextLevel();
        }
        else if (menu.isIn(e) && menu.isMousePressed()) {
            playing.setGameState(GameState.MENU);
            playing.resetAll();
        }

        menu.resetBools();
        next.resetBools();
    }
    public void mousePressed(MouseEvent e) {
        if (next.isIn(e)) {
            next.setMousePressed(true);
        }
        else if (menu.isIn(e)) {
            menu.setMousePressed(true);
        }
    }

}
