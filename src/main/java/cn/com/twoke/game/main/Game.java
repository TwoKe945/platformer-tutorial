package cn.com.twoke.game.main;

import cn.com.twoke.game.entities.Player;
import cn.com.twoke.game.levels.LevelManager;

import java.awt.*;

public class Game implements Runnable
{
    private GameWindow gameWindow;
    private GamePanel gamePanel;

    private Thread gameThread;
    private Player player;
    private LevelManager levelManager;

    public static final int FPS_SET = 120;
    public static final int UPS_SET = 200;
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
        player = new Player(200, 200, 128, 80);
        levelManager = new LevelManager(this);
        player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
    }

    public void update() {
        player.update();
        levelManager.update();
    }

    public void render(Graphics g) {
        levelManager.draw(g);
        player.render(g);
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

    public Player getPlayer() {
        return this.player;
    }

    public void windowLostFocus() {
        player.resetDitBoolean();
    }
}
