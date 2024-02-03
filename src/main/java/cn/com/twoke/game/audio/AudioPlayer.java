package cn.com.twoke.game.audio;

import cn.com.twoke.game.main.Game;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;

public class AudioPlayer {

    public static int MENU_1 = 0;
    public static int LEVEL_1 = 1;
    public static int LEVEL_2 = 2;

    public static int DIE = 0;
    public static int JUMP = 1;
    public static int GAMEOVER = 2;
    public static int LVL_COMPLETED = 3;
    public static int ATTACK_ONE = 4;
    public static int ATTACK_TWO = 5;
    public static int ATTACK_THREE = 6;

    private Clip[] songs, effects;
    private int currentSongId;
    private float volume = 0.5f;
    private boolean songMute, effectMute;
    private Random rand = new Random();

    public AudioPlayer() {
        loadSongs();
        loadEffects();
        playSong(MENU_1);
    }


    private void loadSongs() {
        String[] names = {"menu", "level1", "level2"};
        songs = new Clip[names.length];
        for (int i = 0; i < songs.length; i++) {
            songs[i] = getClip(names[i]);
        }

    }


    private void loadEffects() {
        String[] names = {"die", "jump", "gameover", "lvlcompleted", "attack1", "attack2", "attack3"};
        effects = new Clip[names.length];
        for (int i = 0; i < effects.length; i++) {
            effects[i] = getClip(names[i]);
        }
        updateEffectsVolume();
    }


    private Clip getClip(String name) {
        URL url = getClass().getResource("/audio/"+name+".wav");
        AudioInputStream audio = null;
        Clip c = null;
        try {
            audio = AudioSystem.getAudioInputStream(url);
            c = AudioSystem.getClip();
            c.open(audio);
            return c;
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        return null;
    }


    private void updateSongVolume() {
        FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }

    private void updateEffectsVolume() {
        for (Clip effect : effects) {
            FloatControl gainControl = (FloatControl) effect.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }

    public void toggleSongMute() {
        this.songMute = !songMute;
        for (Clip song : songs) {
            BooleanControl booleanControl = (BooleanControl) song.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute);
        }
    }

    public void toggleEffectMute() {
        effectMute = !effectMute;
        for (Clip effect : effects) {
            BooleanControl booleanControl = (BooleanControl) effect.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(effectMute);
        }
        if (!effectMute)
            playEffect(JUMP);
    }

    public void playEffect(int effectId) {
        effects[effectId].setMicrosecondPosition(0);
        effects[effectId].start();
    }

    public void playAttackSound() {
        int start = 4;
        start += rand.nextInt(3);
        playEffect(start);
    }

    public void setVolume(float volume) {
        this.volume = volume;
        if (this.volume < 0) {
            this.volume = 0;
        } else if (this.volume > 1) {
            this.volume = 1;
        }
        updateSongVolume();
        updateEffectsVolume();
    }

    public void stopSong() {
        if (songs[currentSongId].isActive()) {
            songs[currentSongId].stop();
        }
    }

    public void setLevelSong(int lvlIndex) {
        if (lvlIndex % 2 == 0) {
            playSong(LEVEL_1);
        } else {
            playSong(LEVEL_2);
        }
    }

    public void lvlCompleted() {
        stopSong();
        playEffect(LVL_COMPLETED);
    }

    public void playSong(int songId) {
        stopSong();
        currentSongId = songId;
        updateSongVolume();
        songs[currentSongId].setMicrosecondPosition(0);
        songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY); // 循环播放
    }

}
