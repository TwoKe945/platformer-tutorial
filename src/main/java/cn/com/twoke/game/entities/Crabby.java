package cn.com.twoke.game.entities;

import cn.com.twoke.game.main.Game;

import java.awt.*;

import static cn.com.twoke.game.utils.Constants.EnemyConstants.*;

public class Crabby extends Enemy{
    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitBox(x, y, (int)(22 * Game.SCALE), (int)(19 * Game.SCALE));
    }



}
