package cn.com.twoke.game.gamestates;

import cn.com.twoke.game.main.Game;
import cn.com.twoke.game.ui.MenuButton;
import cn.com.twoke.game.utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Menu extends State implements StateMethods {

    private MenuButton[] buttons = new MenuButton[3];
    private BufferedImage menuBackgroundImg;
    private BufferedImage backgroundImg;
    private int menuX,menuY,menuWidth,menuHeight;

    public Menu(Game game) {
        super(game);
        loadButtons();
        loadBackground();
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_MENU);
        menuBackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_ATLAS);
        menuWidth = (int)(menuBackgroundImg.getWidth() * Game.SCALE);
        menuHeight = (int)(menuBackgroundImg.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int)(45 * Game.SCALE);
    }

    private void loadButtons() {
        buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int) (150 * Game.SCALE), 0, GameState.PLAYING);
        buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int) (220 * Game.SCALE), 1, GameState.OPTIONS);
        buttons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int) (290 * Game.SCALE), 2, GameState.QUIT);
    }

    @Override
    public void update() {
        for (MenuButton button : buttons) {
            button.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0,0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(menuBackgroundImg, menuX, menuY, menuWidth, menuHeight, null);
        for (MenuButton button : buttons) {
            button.draw(g);
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            setGameState(GameState.PLAYING);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton button : buttons) {
            if (isIn(e, button)) {
                button.setMousePressed(true);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton button : buttons) {
            if (isIn(e, button) && button.isMousePressed()) {
                button.applyGameState();
                if (button.getState() == GameState.PLAYING) {
                    game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLvlIndex());
                }
                break;
            }
        }
        resetButtons();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton button : buttons) {
            button.setMouseOver(false);
        }

        for (MenuButton button : buttons) {
            if (isIn(e, button)) {
                button.setMouseOver(true);
                break;
            }
        }

    }

    private void resetButtons() {
        for (MenuButton button : buttons) {
            button.resetBools();
        }
    }
}
