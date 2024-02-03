package cn.com.twoke.game.gamestates;

import cn.com.twoke.game.main.Game;
import cn.com.twoke.game.ui.AudioOptions;
import cn.com.twoke.game.ui.MenuButton;
import cn.com.twoke.game.ui.UrmButton;
import cn.com.twoke.game.utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static cn.com.twoke.game.utils.Constants.UI.PauseButtons.URM_SIZE;

public class GameOptions extends State implements StateMethods {

    private AudioOptions audioOptions;
    private BufferedImage backgroundImg, optionsBackgroundImg;
    private int optionX,optionY,optionW,optionH;
    private UrmButton menuB;

    public GameOptions(Game game) {
        super(game);
        this.game = game;
        loadImgs();
        loadButton();
        this.audioOptions = game.getAudioOptions();
    }

    private void loadImgs() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_MENU);
        optionsBackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_BACKGROUND);
        optionW = (int)(optionsBackgroundImg.getWidth() * Game.SCALE);
        optionH = (int)(optionsBackgroundImg.getHeight() * Game.SCALE);
        optionX = Game.GAME_WIDTH / 2 - optionW / 2;
        optionY = (int)(33 * Game.SCALE);
    }

    private void loadButton() {
        int menuX = (int)(387 * Game.SCALE);
        int menuY = (int)(325 * Game.SCALE);
        this.menuB = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
    }

    public void update() {
        menuB.update();
        audioOptions.update();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            GameState.currentState = GameState.MENU;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0,0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(optionsBackgroundImg, optionX, optionY, optionW, optionH, null);
        menuB.draw(g);
        audioOptions.draw(g);
    }


    @Override
    public void mousePressed(MouseEvent e) {
        if (menuB.isIn(e)) {
            menuB.setMousePressed(true);
        } else {
            audioOptions.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (menuB.isIn(e) && menuB.isMousePressed()) {
            GameState.currentState = GameState.MENU;
        }   else {
            audioOptions.mouseReleased(e);
        }
        menuB.resetBools();
        audioOptions.resetBools();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);
        audioOptions.cleanMouseOver();

        if (menuB.isIn(e)) {
            menuB.setMouseOver(true);
        } else {
            audioOptions.mouseMoved(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }
}
