package cn.com.twoke.game.gamestates;

import cn.com.twoke.game.entities.Player;
import cn.com.twoke.game.levels.LevelManager;
import cn.com.twoke.game.main.Game;
import cn.com.twoke.game.ui.PauseOverlay;
import cn.com.twoke.game.utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Playing extends State implements StateMethods {

    private Player player;
    private LevelManager levelManager;
    private boolean paused;

    private PauseOverlay pauseOverlay;

    private int xLvlOffset;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
    private int lvlTilesWide = LoadSave.GetLevelData()[0].length;
    private int maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
    private int maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE;

    public Playing(Game game) {
        super(game);
        initClasses();
    }

    private void initClasses() {
        player = new Player(200, 200, (int)(64 * Game.SCALE),  (int)(40 * Game.SCALE));
        levelManager = new LevelManager(game);
        player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
        pauseOverlay = new PauseOverlay(this);
    }

    public void unpauseGame() {
        this.paused = false;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void windowLostFocus() {
        player.resetDitBoolean();
    }

    @Override
    public void update() {
        if (!paused) {
            levelManager.update();
            player.update();

            checkCloseToBorder();

        } else {
            pauseOverlay.update();
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int)(player.getHitBox().x);
        int diff = playerX - xLvlOffset;

        if (diff > rightBorder) {
            xLvlOffset += diff - rightBorder;
        } else if (diff < leftBorder) {
            xLvlOffset += diff - leftBorder;
        }

        if (xLvlOffset > maxLvlOffsetX) {
            xLvlOffset = maxLvlOffsetX;
        } else if (xLvlOffset < 0) {
            xLvlOffset = 0;
        }
    }

    @Override
    public void draw(Graphics g) {
        levelManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        if (paused) {
            g.setColor(new Color(0,0,0, 200));
            g.fillRect(0,0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            player.setAttack(true);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setRight(true);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(true);
                break;
            case KeyEvent.VK_ESCAPE:
                paused = !paused;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(false);
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (paused) {
            pauseOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseMoved(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseDragged(e);
        }
    }
}