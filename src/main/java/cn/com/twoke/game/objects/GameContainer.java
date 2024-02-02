package cn.com.twoke.game.objects;

import cn.com.twoke.game.main.Game;

import static cn.com.twoke.game.utils.Constants.ObjectConstants.*;

public class GameContainer extends GameObject{

    public GameContainer(int x, int y, int objType) {
        super(x, y, objType);
        createHitBox();
    }

    private void createHitBox() {
        if (objType == BOX) {
            initHitBox(25, 18);
            xDrawOffset = (int)(Game.SCALE * 7);
            yDrawOffset = (int)(Game.SCALE * 12);
        } else if (objType == BARREL) {
            initHitBox(23, 25);
            xDrawOffset = (int)(Game.SCALE * 8);
            yDrawOffset = (int)(Game.SCALE * 5);
        }
    }


    public void update() {
        if (doAnimation)
            updateAnimationTick();
    }

}
