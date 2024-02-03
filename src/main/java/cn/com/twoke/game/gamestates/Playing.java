package cn.com.twoke.game.gamestates;

import cn.com.twoke.game.entities.EnemyManager;
import cn.com.twoke.game.entities.Player;
import cn.com.twoke.game.levels.Level;
import cn.com.twoke.game.levels.LevelManager;
import cn.com.twoke.game.main.Game;
import cn.com.twoke.game.objects.ObjectManager;
import cn.com.twoke.game.ui.GameOverOverlay;
import cn.com.twoke.game.ui.LevelCompleteOverlay;
import cn.com.twoke.game.ui.PauseOverlay;
import cn.com.twoke.game.utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static cn.com.twoke.game.utils.Constants.Environment.*;

public class Playing extends State implements StateMethods {

    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    private boolean gameOver = false;
    private boolean paused = false;
    // 是否完成：完成本关所有任务
    private boolean completed = false;

    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompleteOverlay levelCompleteOverlay;

    private int xLvlOffset;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
    private int maxLvlOffsetX;

    private BufferedImage backgroundImg, bigCloud, smallCloud;
    private int[] smallCloudsPos;
    private Random random = new Random();

    private boolean playerDying = false;

    public Playing(Game game) {
        super(game);
        initClasses();
        loadBackground();
        calcLevelOffset();
        loadStartLevel();
    }


    public void  loadNextLevel() {
        resetAll();
        levelManager.loadNextLevel();
        player.setSpawn(levelManager.getCurrentLevel().getLvlSpawn());
        getGame().getAudioPlayer().setLevelSong(levelManager.getLvlIndex());
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelManager.getCurrentLevel());
        objectManager.loadObjects(levelManager.getCurrentLevel());
    }

    private void calcLevelOffset() {
        maxLvlOffsetX = levelManager.getCurrentLevel().getMaxLvlOffsetX();
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
        bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
        smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
        smallCloudsPos = new int[8];
        for (int i = 0; i < smallCloudsPos.length; i++) {
            smallCloudsPos[i] = (int) (90 * Game.SCALE) + random.nextInt((int) (110 * Game.SCALE));
        }
    }

    private void initClasses() {
        player = new Player(200, 200, (int)(64 * Game.SCALE),  (int)(40 * Game.SCALE), this);
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        objectManager = new ObjectManager(this);
        player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawn(levelManager.getCurrentLevel().getLvlSpawn());
        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompleteOverlay = new LevelCompleteOverlay(this);

    }

    public void unpauseGame() {
        this.paused = false;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void windowLostFocus() {
        player.resetDirBoolean();
    }

    @Override
    public void update() {
        if (paused) {
            pauseOverlay.update();
        } else if (completed) {
            levelCompleteOverlay.update();
        } else if (gameOver) {
            gameOverOverlay.update();
        } else if (playerDying) {
            player.update();
        } else if (!gameOver) {
            levelManager.update();
            objectManager.update(this.getLevelManager().getCurrentLevel().getLevelData(), player);
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            checkCloseToBorder();
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

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    public void checkPotionTouched(Rectangle2D.Float hitBox) {
        objectManager.checkObjectTouched(hitBox);
    }

    public void checkObjectHit(Rectangle2D.Float attackBox) {
        objectManager.checkObjectHit(attackBox);
    }

    public void checkSpikeTouched(Player player) {
        objectManager.checkSpikeTouched(player);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

        drawClouds(g);

        levelManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);
        objectManager.draw(g, xLvlOffset);
        if (paused) {
            pauseOverlay.draw(g);
        } else if (gameOver) {
            gameOverOverlay.draw(g);
        } else if (completed) {
            levelCompleteOverlay.draw(g);
        }

    }

    private void drawClouds(Graphics g) {

        // big clouds
        for (int i = 0; i < 3; i++) {
            g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int)(xLvlOffset * 0.3), (int)(204 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
        }
        // small clouds
        for (int i = 0; i < smallCloudsPos.length; i++) {
            g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int)(xLvlOffset * 0.3), smallCloudsPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                player.setAttack(true);
                getGame().getAudioPlayer().playAttackSound();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver) {
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
        if (!gameOver) {
            if (paused) {
                pauseOverlay.mousePressed(e);
            } else if (completed) {
                levelCompleteOverlay.mousePressed(e);
            }
        } else {
            gameOverOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!gameOver) {
            if (paused) {
                pauseOverlay.mouseReleased(e);
            } else if (completed) {
                levelCompleteOverlay.mouseReleased(e);
            }
        }else {
            gameOverOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver) {
            if (paused) {
                pauseOverlay.mouseMoved(e);
            } else if (completed) {
                levelCompleteOverlay.mouseMoved(e);
            }
        }else {
            gameOverOverlay.mouseMoved(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!gameOver) {
            if (paused) {
                pauseOverlay.mouseDragged(e);
            }
        }
    }

    public void resetAll() {
        gameOver = false;
        paused = false;
        completed = false;
        playerDying = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();
    }



    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setMaxLvlOffsetX(int maxLvlOffsetX) {
        this.maxLvlOffsetX = maxLvlOffsetX;
    }

    public void setLevelCompleted(boolean completed) {
        this.completed = completed;
        if (completed) {
            getGame().getAudioPlayer().lvlCompleted();
        }
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }


    public LevelManager getLevelManager() {
        return levelManager;
    }


    public void setPlayerDying(boolean playerDying) {
        this.playerDying = playerDying;
    }
}
