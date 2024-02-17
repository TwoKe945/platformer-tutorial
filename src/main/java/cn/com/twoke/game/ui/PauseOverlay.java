package cn.com.twoke.game.ui;

import cn.com.twoke.game.gamestates.GameState;
import cn.com.twoke.game.gamestates.Playing;
import cn.com.twoke.game.main.Game;
import cn.com.twoke.game.utils.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static cn.com.twoke.game.utils.Constants.UI.PauseButtons.*;

public class PauseOverlay {

    private BufferedImage background;
    private int bgX,bgY,bgW,bgH;
    private UrmButton menuB,replayB,unpauseB;
    private AudioOptions audioOptions;
    private Playing playing;

    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        audioOptions = playing.getGame().getAudioOptions();
        createUrmButton();
    }

    private void createUrmButton() {
        int menuX = (int) (313 * Game.SCALE);
        int replayX = (int) (387 * Game.SCALE);
        int unpauseX = (int) (462 * Game.SCALE);
        int urmY = (int)(325 * Game.SCALE);

        menuB = new UrmButton(menuX, urmY, URM_SIZE, URM_SIZE, 2);
        replayB = new UrmButton(replayX, urmY, URM_SIZE, URM_SIZE, 1);
        unpauseB = new UrmButton(unpauseX, urmY, URM_SIZE, URM_SIZE, 0);
    }

    private void loadBackground() {
        background = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_MENU);
        bgW = (int)(background.getWidth() * Game.SCALE);
        bgH = (int)(background.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int)( 25 * Game.SCALE );
    }

    public void update() {

        audioOptions.update();

        menuB.update();
        replayB.update();
        unpauseB.update();

    }
    public void draw(Graphics g) {
        g.setColor(new Color(0 , 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(background, bgX, bgY, bgW, bgH, null );

        audioOptions.draw(g);

        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);
    }

    public void mousePressed(MouseEvent e) {
         if (menuB.isIn(e)) {
            menuB.setMousePressed(true);
        } else if (replayB.isIn(e)) {
            replayB.setMousePressed(true);
        } else if (unpauseB.isIn(e)) {
            unpauseB.setMousePressed(true);
        } else {
             audioOptions.mousePressed(e);}
    }
    public void mouseReleased(MouseEvent e) {
         if (menuB.isIn(e) && menuB.isMousePressed()) {
//            GameState.currentState = GameState.MENU;
             playing.setGameState(GameState.MENU);
             playing.resetAll();
        }else if (replayB.isIn(e) && replayB.isMousePressed()) {
             playing.resetAll();
        }else if (unpauseB.isIn(e) && unpauseB.isMousePressed()) {
            playing.unpauseGame();
        } else {
             audioOptions.mouseReleased(e);
        }
        resetBools();
    }

    private void resetBools() {
        menuB.resetBools();
        replayB.resetBools();
        unpauseB.resetBools();
        audioOptions.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        cleanMouseOver();

        if (menuB.isIn(e)) {
            menuB.setMouseOver(true);
        } else if (replayB.isIn(e)) {
            replayB.setMouseOver(true);
        } else if (unpauseB.isIn(e)) {
            unpauseB.setMouseOver(true);
        } else {
            audioOptions.mouseMoved(e);
        }
    }

    private void cleanMouseOver() {
        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
        unpauseB.setMouseOver(false);
        audioOptions.cleanMouseOver();
    }

    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

}
