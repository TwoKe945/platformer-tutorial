package cn.com.twoke.game.entities;

import cn.com.twoke.game.main.Game;
import cn.com.twoke.game.utils.Constants;


import static cn.com.twoke.game.utils.Constants.Direction.*;
import static cn.com.twoke.game.utils.Constants.EnemyConstants.*;
import static cn.com.twoke.game.utils.HelpMethods.*;

public abstract class Enemy extends Entity{

    protected int aniIndex, enemyState, enemyType;
    private int aniTick, aniSpeed = (int)( 25 * Game.SCALE);
    private boolean firstUpdate = true;
    private boolean inAir = false;
    private float fallSpeed;
    private float gravity = 0.04f * Game.SCALE;
    private  int walkDir = LEFT;
    private float walkSpeed = 0.35f * Game.SCALE;

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

    public void update(int[][] lvlData) {
        updateMove(lvlData);
        updateAnimationTick();
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getEnemyState() {
        return enemyState;
    }

    private void updateMove(int[][] lvlData) {
        if (firstUpdate) {
            if (!IsEntityOnFloor(hitBox, lvlData)) {
                inAir = true;
            }
            firstUpdate = false;
        }

        if (inAir) {
            if(CanMoveHere(hitBox.x, hitBox.y + fallSpeed, hitBox.width, hitBox.height, lvlData)) {
                hitBox.y += fallSpeed;
                fallSpeed += gravity;
            } else {
                inAir =false;
                hitBox.y = GetEntityYPosNextToWall(hitBox, fallSpeed);
            }
        } else {
            switch (enemyState) {
                case IDLE:
                    enemyState = RUNNING;
                    break;
                case RUNNING:
                    float xSpeed = 0;
                    if (walkDir == LEFT) {
                        xSpeed -= walkSpeed;
                    } else {
                        xSpeed += walkSpeed;
                    }

                    if (CanMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData)) {
                        if (IsFloor(hitBox, xSpeed, lvlData)) {
                            hitBox.x += xSpeed;
                            return;
                        }
                    }
                    changeWalkDir();

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

    private void changeWalkDir() {
        if (walkDir == LEFT) {
            walkDir = RIGHT;
        } else {
            walkDir = LEFT;
        }

    }

}
