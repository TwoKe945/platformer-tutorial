package cn.com.twoke.game.entities;

import java.awt.*;

import static cn.com.twoke.game.utils.Constants.EnemyConstants.*;

public class Crabby extends Enemy{
    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
    }


    @Override
    public void update() {
        super.update();

    }

    @Override
    public void render(Graphics g) {
        drawHitBox(g);
    }
}
