package cn.com.twoke.game.ui;

import cn.com.twoke.game.gamestates.GameState;
import cn.com.twoke.game.utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static cn.com.twoke.game.utils.Constants.UI.Buttons.*;

public class MenuButton {

    private int xPos, yPos, rowIndex, index;
    private  GameState state;
    private int xOffsetCenter = B_WIDTH / 2;
    private BufferedImage[] imgs;
    private boolean mouseOver, mousePressed;
    private Rectangle bounds;

    public MenuButton(int xPos , int yPos, int rowIndex, GameState state) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state =state;
        loadImgs();
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT);
    }

    private void loadImgs() {
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.BUTTON_ATLAS);
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(B_WIDTH_DEFAULT * i, rowIndex * B_HEIGHT_DEFAULT,
                    B_WIDTH_DEFAULT,
                    B_HEIGHT_DEFAULT
                    );
        }
    }

    public void draw(Graphics g) {
        g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT, null);
    }

    public void update() {
        index = 0;
        if (mouseOver) {
            index = 1;
        }
        if(mousePressed) {
            index = 2;
        }
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public Rectangle getBounds() {
        return bounds;
    }


    public void applyGameState() {
        GameState.currentState = state;
    }

    public GameState getState() {
        return state;
    }

    public void resetBools() {
        mousePressed = false;
        mouseOver = false;
    }

}

