package cn.com.twoke.game.ui;

import cn.com.twoke.game.gamestates.GameState;
import cn.com.twoke.game.main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static cn.com.twoke.game.utils.Constants.UI.PauseButtons.*;
import static cn.com.twoke.game.utils.Constants.UI.PauseButtons.SOUND_SIZE;

public class AudioOptions {
    private VolumeButton volumeB;
    private SoundButton musicButton,sfxButton;

    private Game game;

    public AudioOptions(Game game) {
        this.game = game;
        createSoundButton();
        createVolumeButton();
    }

    public void createSoundButton() {
        int soundX =(int)(450 * Game.SCALE);
        int musicY = (int)(140 * Game.SCALE);
        int sfxY = (int)(186 * Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    public void createVolumeButton() {
        int vX = (int) (309 * Game.SCALE);
        int vY = (int) (278 * Game.SCALE);
        volumeB = new VolumeButton(vX, vY, VOLUME_SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    public void update() {
        musicButton.update();
        sfxButton.update();
        volumeB.update();
    }


    public void draw(Graphics g) {
        // Sound Button
        musicButton.draw(g);
        sfxButton.draw(g);

        // volume button
        volumeB.draw(g);
    }


    public void mousePressed(MouseEvent e) {
        if (musicButton.isIn(e)) {
            musicButton.setMousePressed(true);
        }
        else if (sfxButton.isIn(e)) {
            sfxButton.setMousePressed(true);
        } else if (volumeB.isIn(e)) {
            volumeB.setMousePressed(true);
        }
    }
    public void mouseReleased(MouseEvent e) {
        if (musicButton.isIn(e) && musicButton.isMousePressed()) {
            musicButton.setMuted(!musicButton.isMuted());
            game.getAudioPlayer().toggleSongMute();
        }
        else if (sfxButton.isIn(e) && sfxButton.isMousePressed()) {
            sfxButton.setMuted(!sfxButton.isMuted());
            game.getAudioPlayer().toggleEffectMute();
        }
        resetBools();
    }

    public void resetBools() {
        musicButton.resetBools();
        sfxButton.resetBools();
        volumeB.resetBools();
    }

    public void mouseMoved(MouseEvent e) {
        cleanMouseOver();

        if (musicButton.isIn(e)) {
            musicButton.setMouseOver(true);
        }
        if (sfxButton.isIn(e)) {
            sfxButton.setMouseOver(true);
        }  else if (volumeB.isIn(e)) {
            volumeB.setMouseOver(true);
        }
    }

    public void cleanMouseOver() {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        volumeB.setMouseOver(false);
    }

    public void mouseDragged(MouseEvent e) {
        if (volumeB.isMousePressed()) {
            float valueBefore = volumeB.getFloatValue();
            volumeB.changeX(e.getX());
            float valueAfter = volumeB.getFloatValue();
            if (valueAfter != valueBefore) {
                game.getAudioPlayer().setVolume(valueAfter);
            }
        }
    }


}
