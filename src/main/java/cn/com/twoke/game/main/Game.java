package cn.com.twoke.game.main;

public class Game implements Runnable
{
    private GameWindow gameWindow;
    private GamePanel gamePanel;

    private Thread gameThread;
    private static final int FPS_SET = 120;
    private static final int UPS_SET = 200;

    public Game() {
        gamePanel = new GamePanel();
        gamePanel.requestFocus();
        gameWindow = new GameWindow(gamePanel);
        startGameLoop();
    }

    private void update() {
        gamePanel.updateGame();
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

}
