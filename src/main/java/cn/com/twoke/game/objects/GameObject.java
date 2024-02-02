package cn.com.twoke.game.objects;

import cn.com.twoke.game.main.Game;
import cn.com.twoke.game.utils.Constants;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static cn.com.twoke.game.utils.Constants.ANI_SPEED;
import static cn.com.twoke.game.utils.Constants.ObjectConstants.*;

/**
 * 游戏对象实体
 */
public class GameObject {

    protected int x,y, objType;
    /**
     * 碰撞盒子
     */
    protected Rectangle2D.Float hitBox;
    protected boolean doAnimation, active = true;
    protected int aniIndex, aniTick;
    protected int xDrawOffset, yDrawOffset;


    public GameObject(int x, int y, int objType) {
        this.x = x;
        this.y = y;
        this.objType = objType;
    }

    public void initHitBox(int width, int height) {
        hitBox = new Rectangle2D.Float(x, y, (int)( width * Game.SCALE), (int)(height * Game.SCALE));
    }

    public void drawHitBox(Graphics g, int lvlOffset) {
        if (Constants.ENABLE_DEBUG_BOX) {
            g.setColor(Color.PINK);
            g.drawRect((int) hitBox.x - lvlOffset, (int) hitBox.y,(int) hitBox.width, (int)hitBox.height);
        }
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(objType)) {
                aniIndex = 0;

                if (objType == BARREL || objType == BOX) {
                    doAnimation = false;
                    active = false;
                }
            }
        }
    }

    public void reset() {
        aniTick = 0;
        aniIndex = 0;
        active = true;
        // todo: add if here
        if (objType == BARREL || objType == BOX) {
            doAnimation = false;
        } else {
            doAnimation = true;
        }
    }

    public int getObjType() {
        return objType;
    }

    public Rectangle2D.Float getHitBox() {
        return hitBox;
    }

    public boolean isActive() {
        return active;
    }

    public int getXDrawOffset() {
        return xDrawOffset;
    }

    public int getYDrawOffset() {
        return yDrawOffset;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getAniIndex() {
        return aniIndex;
    }
}
