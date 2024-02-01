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
    private SoundButton musicButton,sfxButton;
    private UrmButton menuB,replayB,unpauseB;

    private VolumeButton volumeB;

    private Playing playing;

    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        createSoundButton();
        createUrmButton();
        createVolumeButton();
    }

    private void createVolumeButton() {
        int vX = (int) (309 * Game.SCALE);
        int vY = (int) (278 * Game.SCALE);
        volumeB = new VolumeButton(vX, vY, VOLUME_SLIDER_WIDTH, VOLUME_HEIGHT);
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

    private void createSoundButton() {
        int soundX =(int)(450 * Game.SCALE);
        int musicY = (int)(140 * Game.SCALE);
        int sfxY = (int)(186 * Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    private void loadBackground() {
        background = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_MENU);
        bgW = (int)(background.getWidth() * Game.SCALE);
        bgH = (int)(background.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int)( 25 * Game.SCALE );
    }

    public void update() {
        musicButton.update();
        sfxButton.update();

        menuB.update();
        replayB.update();
        unpauseB.update();

        volumeB.update();
    }
    public void draw(Graphics g) {
        g.drawImage(background, bgX, bgY, bgW, bgH, null );

        // Sound Button
        musicButton.draw(g);
        sfxButton.draw(g);

        //
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);

        // volume button
        volumeB.draw(g);
    }

    public void mousePressed(MouseEvent e) {
        if (musicButton.isIn(e)) {
            musicButton.setMousePressed(true);
        }
        else if (sfxButton.isIn(e)) {
            sfxButton.setMousePressed(true);
        } else if (menuB.isIn(e)) {
            menuB.setMousePressed(true);
        } else if (replayB.isIn(e)) {
            replayB.setMousePressed(true);
        } else if (unpauseB.isIn(e)) {
            unpauseB.setMousePressed(true);
        } else if (volumeB.isIn(e)) {
            volumeB.setMousePressed(true);
        }
    }
    public void mouseReleased(MouseEvent e) {
        if (musicButton.isIn(e) && musicButton.isMousePressed()) {
            musicButton.setMuted(!musicButton.isMuted());
        }
        else if (sfxButton.isIn(e) && sfxButton.isMousePressed()) {
            sfxButton.setMuted(!sfxButton.isMuted());
        } else if (menuB.isIn(e) && menuB.isMousePressed()) {
            GameState.currentState = GameState.MENU;
        }else if (replayB.isIn(e) && replayB.isMousePressed()) {
            if (replayB.isMousePressed()) {
                playing.resetAll();
            }
        }else if (unpauseB.isIn(e) && unpauseB.isMousePressed()) {
            playing.unpauseGame();
        }

        resetBools();
    }

    private void resetBools() {
        musicButton.resetBools();
        sfxButton.resetBools();
        menuB.resetBools();
        replayB.resetBools();
        unpauseB.resetBools();
        volumeB.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        cleanMouseOver();

        if (musicButton.isIn(e)) {
            musicButton.setMouseOver(true);
        }
        if (sfxButton.isIn(e)) {
            sfxButton.setMouseOver(true);
        } else if (menuB.isIn(e)) {
            menuB.setMouseOver(true);
        } else if (replayB.isIn(e)) {
            replayB.setMouseOver(true);
        } else if (unpauseB.isIn(e)) {
            unpauseB.setMouseOver(true);
        }  else if (volumeB.isIn(e)) {
            volumeB.setMouseOver(true);
        }
    }

    private void cleanMouseOver() {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
        unpauseB.setMouseOver(false);
        volumeB.setMouseOver(false);
    }

    public void mouseDragged(MouseEvent e) {
        if (volumeB.isMousePressed()) {
            volumeB.changeX(e.getX());
        }
    }

}
