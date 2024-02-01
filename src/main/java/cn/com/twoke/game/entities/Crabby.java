package cn.com.twoke.game.entities;

import cn.com.twoke.game.main.Game;

import java.awt.*;
import java.util.Random;

import static cn.com.twoke.game.utils.Constants.Direction.LEFT;
import static cn.com.twoke.game.utils.Constants.Direction.RIGHT;
import static cn.com.twoke.game.utils.Constants.EnemyConstants.*;
import static cn.com.twoke.game.utils.HelpMethods.*;

public class Crabby extends Enemy{
    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitBox(x, y, (int)(22 * Game.SCALE), (int)(19 * Game.SCALE));
    }

    private void updateMove(int[][] lvlData, Player player) {
        if (firstUpdate)
            firstUpdateCheck(lvlData);
        if (inAir) {
            updateInAir(lvlData);
        } else {
            switch (enemyState) {
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if (canSeePlayer(lvlData, player))
                        turnTowardPlayer(player);
                    if (isPlayerCloseForAttack(player))
                        newState(ATTACK);
                    move(lvlData);
                    break;
                case HIT:
                    break;
                case ATTACK:
                    break;
                case DEAD:
                    break;
            }
        }
    }

    public void update(int[][] lvlData, Player player) {
        updateMove(lvlData, player);
        updateAnimationTick();
    }
}
