package cn.com.twoke.game.entities;

import cn.com.twoke.game.main.Game;
import cn.com.twoke.game.utils.Constants;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {

    protected float x,y;
    protected int width,height;
    protected Rectangle2D.Float hitBox;
    protected int aniTick = 0, aniIndex = 0;
    protected int state;
    protected float airSpeed;
    protected boolean inAir = false;
    protected int maxHealth;
    protected int currentHealth;
    protected Rectangle2D.Float attackBox;
    protected float walkSpeed;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void initHitBox(int width, int height) {
        hitBox = new Rectangle2D.Float(x,  y, (int)(width * Game.SCALE), (int)(height * Game.SCALE));
    }

    public void drawHitBox(Graphics g, int lvlOffset) {
        if (Constants.ENABLE_DEBUG_BOX) {
            g.setColor(Color.PINK);
            g.drawRect((int) hitBox.x - lvlOffset, (int) hitBox.y,(int) hitBox.width, (int)hitBox.height);
        }
    }

    public void drawAttackBox(Graphics g, int xLvlOffset) {
        if (Constants.ENABLE_DEBUG_BOX) {
            g.setColor(Color.RED);
            g.drawRect((int) attackBox.x - xLvlOffset, (int) attackBox.y,(int) attackBox.width, (int)attackBox.height);
        }
    }

    public void updateHisBox() {
        hitBox.x = x;
        hitBox.y = y;
    }

    public Rectangle2D.Float getHitBox() {
        return hitBox;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getState() {
        return state;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }
}
