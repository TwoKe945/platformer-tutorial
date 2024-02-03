package cn.com.twoke.game.main;

import cn.com.twoke.game.entities.Player;
import cn.com.twoke.game.gamestates.GameOptions;
import cn.com.twoke.game.gamestates.GameState;
import cn.com.twoke.game.gamestates.Menu;
import cn.com.twoke.game.gamestates.Playing;
import cn.com.twoke.game.levels.LevelManager;
import cn.com.twoke.game.ui.AudioOptions;
import cn.com.twoke.game.utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Game implements Runnable
{
    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    public static final int FPS_SET = 120;
    public static final int UPS_SET = 200;

    private Playing playing;
    private Menu menu;
    private AudioOptions audioOptions;
    private GameOptions gameOptions;
    public final static  int TILES_DEFAULT_SIZE = 32;
    public final static  float SCALE = 2f;
    public final static  int TILES_IN_WIDTH = 26;
    public final static  int TILES_IN_HEIGHT = 14;
    public final static  int TILES_SIZE = (int)(TILES_DEFAULT_SIZE * SCALE);
    public final static  int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static  int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;


    public Game() {
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void initClasses() {
        this.audioOptions = new AudioOptions();
        this.menu = new Menu(this);
        this.playing = new Playing(this);
        this.gameOptions = new GameOptions(this);
    }

    public AudioOptions getAudioOptions() {
        return audioOptions;
    }

    public GameOptions getGameOptions() {
        return gameOptions;
    }

    public void update() {
        switch (GameState.currentState) {
            case MENU:
                this.menu.update();
                break;
            case PLAYING:
                this.playing.update();
                break;
            case OPTIONS:
                this.gameOptions.update();
                break;
            case QUIT:
                System.exit(0);
                break;
        }
    }

    public void render(Graphics g) {
        switch (GameState.currentState) {
            case MENU:
                this.menu.draw(g);
                break;
            case OPTIONS:
                this.gameOptions.draw(g);
                break;
            case PLAYING:
                this.playing.draw(g);
                break;
        }
    }

    @Override
    public void run() {
        double timePerFrame = 1_000_000_000.0 / FPS_SET;
        double timePerUpdate = 1_000_000_000.0 / UPS_SET;
        long lastCheck = System.currentTimeMillis();;
        long previousTime = System.nanoTime();
        int frames = 0;
        int updates = 0;

        double deltaU = 0;
        double deltaF = 0;
        while (true) { // loop
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                gamePanel.requestFocus();
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000 ) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    public void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void windowLostFocus() {
        if (GameState.currentState == GameState.PLAYING) {
            playing.windowLostFocus();
        }
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }
}
