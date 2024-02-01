package cn.com.twoke.game.ui;

import cn.com.twoke.game.main.Game;
import cn.com.twoke.game.utils.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static cn.com.twoke.game.utils.Constants.UI.PauseButtons.SOUND_SIZE;

public class PauseOverlay {

    private BufferedImage background;
    private int bgX,bgY,bgW,bgH;
    private SoundButton musicButton,sfxButton;

    public PauseOverlay() {
        loadBackground();
        createSoundButton();
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
    }
    public void draw(Graphics g) {
        g.drawImage(background, bgX, bgY, bgW, bgH, null );

        // Sound Button
        musicButton.draw(g);
        sfxButton.draw(g);
    }

    public void mousePressed(MouseEvent e) {
        if (musicButton.isIn(e)) {
            musicButton.setMousePressed(true);
        }
        else if (sfxButton.isIn(e)) {
            sfxButton.setMousePressed(true);
        }
    }
    public void mouseReleased(MouseEvent e) {
        if (musicButton.isIn(e) && musicButton.isMousePressed()) {
            musicButton.setMuted(!musicButton.isMuted());
        }
        else if (sfxButton.isIn(e) && sfxButton.isMousePressed()) {
            sfxButton.setMuted(!sfxButton.isMuted());
        }

        musicButton.resetBools();
        sfxButton.resetBools();
    }
    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);


        if (musicButton.isIn(e)) {
            musicButton.setMouseOver(true);
        }
        if (sfxButton.isIn(e)) {
            sfxButton.setMouseOver(true);
        }
    }
    public void mouseDragged(MouseEvent e) {

    }

}
