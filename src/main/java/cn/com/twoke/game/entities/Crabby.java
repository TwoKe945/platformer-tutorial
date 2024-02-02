package cn.com.twoke.game.entities;

import cn.com.twoke.game.main.Game;
import cn.com.twoke.game.utils.Constants;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static cn.com.twoke.game.utils.Constants.Direction.RIGHT;
import static cn.com.twoke.game.utils.Constants.EnemyConstants.*;

public class Crabby extends Enemy{
    // AttackBox

    private int attackBoxOffsetX;


    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitBox(22, 19);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x,y,(int)(Game.SCALE * 82), (int)(Game.SCALE * 19));
        attackBoxOffsetX = (int)(Game.SCALE * 30);
    }

    private void updateBehavior(int[][] lvlData, Player player) {
        if (firstUpdate)
            firstUpdateCheck(lvlData);
        if (inAir) {
            updateInAir(lvlData);
        } else {
            switch (state) {
                case IDLE:
                    newState(RUNNING);
                    break;
                case RUNNING:
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardPlayer(player);
                        if (isPlayerCloseForAttack(player))
                            newState(ATTACK);
                    }
                    move(lvlData);
                    break;
                case HIT:
                    break;
                case ATTACK:
                    if (aniIndex == 0)
                        attackChecked = false;
                    if (aniIndex == 3 && !attackChecked) {
                        checkEnemyHit(attackBox, player);
                    }
                    break;
                case DEAD:
                    break;
            }
        }
    }

    public void update(int[][] lvlData, Player player) {
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x = hitBox.x - attackBoxOffsetX;
        attackBox.y = hitBox.y;
    }



    public int flipX() {
        if (walkDir == RIGHT) {
            return width;
        }
        return 0;
    }

    public int flipW() {
        if (walkDir == RIGHT) {
            return -1;
        }
        return 1;
    }



}
