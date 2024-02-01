package cn.com.twoke.game.entities;

import cn.com.twoke.game.main.Game;
import cn.com.twoke.game.utils.Constants;


import static cn.com.twoke.game.utils.Constants.EnemyConstants.*;

public abstract class Enemy extends Entity{

    protected int aniIndex, enemyState, enemyType;
    private int aniTick, aniSpeed = (int)( 25 * Game.SCALE);

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitBox(x, y, width, height);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
                aniIndex = 0;
            }
        }
    }

    public void update() {
        updateAnimationTick();
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getEnemyState() {
        return enemyState;
    }

}
