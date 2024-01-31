package cn.com.twoke.game.main;

public class Game implements Runnable
{
    private GameWindow gameWindow;
    private GamePanel gamePanel;

    private Thread gameThread;
    private final int FPS_SET = 120;

    public Game() {
        gamePanel = new GamePanel();
        gamePanel.requestFocus();
        gameWindow = new GameWindow(gamePanel);
        startGameLoop();
    }

    @Override
    public void run() {
        double timePerFrame = 1_000_000_000.0 / FPS_SET;
        long lastFrame = System.nanoTime(), lastCheck = System.currentTimeMillis();;
        long now;
        int frames = 0;
        while (true) { // loop
            now = System.nanoTime();
            if(now - lastFrame >= timePerFrame) {
                gamePanel.repaint();
                gamePanel.requestFocus();
                lastFrame = now;
                frames++;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000 ) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
    }

    public void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

}
