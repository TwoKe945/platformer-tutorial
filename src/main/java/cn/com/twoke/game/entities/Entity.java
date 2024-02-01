package cn.com.twoke.game.entities;

import cn.com.twoke.game.utils.Constants;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {

    protected float x,y;
    protected int width,height;
    protected Rectangle2D.Float hitBox;


    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void initHitBox(float x, float y, float width, float height) {
        hitBox = new Rectangle2D.Float(x,  y, width, height);
    }

    public void drawHitBox(Graphics g) {
        if (Constants.DEBUG_HIT_BOX) {
            g.setColor(Color.PINK);
            g.drawRect((int) hitBox.x, (int) hitBox.y,(int) hitBox.width, (int)hitBox.height);
        }
    }

    public void render(Graphics g) {
        doRender(g);
        drawHitBox(g);
    }

    protected abstract void doRender(Graphics g);

    public void updateHisBox() {
        hitBox.x = x;
        hitBox.y = y;
    }

    public Rectangle2D.Float getHitBox() {
        return hitBox;
    }
}
