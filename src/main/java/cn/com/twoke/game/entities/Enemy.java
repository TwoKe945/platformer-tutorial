package cn.com.twoke.game.entities;

import cn.com.twoke.game.main.Game;


import java.awt.geom.Rectangle2D;

import static cn.com.twoke.game.utils.Constants.ANI_SPEED;
import static cn.com.twoke.game.utils.Constants.Direction.*;
import static cn.com.twoke.game.utils.Constants.EnemyConstants.*;
import static cn.com.twoke.game.utils.Constants.GRAVITY;
import static cn.com.twoke.game.utils.HelpMethods.*;

public abstract class Enemy extends Entity{

    protected int enemyType;
    protected boolean firstUpdate = true;
    protected  int walkDir = LEFT;
    protected int tileY;
    protected float attackDistance = Game.TILES_SIZE;
    protected boolean active = true;
    protected boolean attackChecked;
    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitBox(width, height);
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
        this.walkSpeed = 0.35f * Game.SCALE;
    }

    public void hurt(int amount) {
        currentHealth -= amount;
        if (currentHealth <= 0) {
            newState(DEAD);
        } else {
            newState(HIT);
        }
    }

    public void resetEnemy() {
        hitBox.x = x;
        hitBox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        airSpeed = 0f;
    }

    protected void checkEnemyHit(Rectangle2D.Float attackBox,Player player) {
        if (attackBox.intersects(player.hitBox)) {
            player.changeHealth(-GetMaxHealth(enemyType));
        }
        attackChecked = true;
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(enemyType, state)) {
                aniIndex = 0;

                if (state == ATTACK || state == HIT) {
                    state = IDLE;
                }else if (state == DEAD) {
                    active = false;
                }
            }
        }
    }

    protected void firstUpdateCheck(int[][] lvlData) {
        if (!IsEntityOnFloor(hitBox, lvlData)) {
            inAir = true;
        }
        firstUpdate = false;
    }

    protected void updateInAir(int[][] lvlData) {
        if(CanMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)) {
            hitBox.y += airSpeed;
            airSpeed += GRAVITY;
        } else {
            inAir =false;
            hitBox.y = GetEntityYPosNextToWall(hitBox, airSpeed);
            tileY = (int)( hitBox.y / Game.TILES_SIZE);
        }
    }

    public boolean isActive() {
        return active;
    }

    protected void move(int[][] lvlData) {
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
    }

    protected void newState(int enemyState) {
        this.state = enemyState;
        aniTick = 0;
        aniIndex = 0;
    }

    protected boolean canSeePlayer(int[][] lvlData, Player player) {
        int playerTileY = (int)(player.getHitBox().y / Game.TILES_SIZE);
        if (playerTileY == tileY) {
            if (isPlayerInRange(player)) {
                return IsSightClear(lvlData, hitBox, player.hitBox, tileY);
            }
        }
        return false;
    }

    protected void turnTowardPlayer(Player player) {
        if (player.hitBox.x > hitBox.x) {
            walkDir = RIGHT;
        } else {
            walkDir = LEFT;
        }
    }

    protected boolean isPlayerInRange(Player player) {
        int absValue = (int)Math.abs(player.hitBox.x - hitBox.x);
        return absValue <= attackDistance * 5;
    }

    protected boolean isPlayerCloseForAttack(Player player) {
        int absValue = (int)Math.abs(player.hitBox.x - hitBox.x);
        return absValue <= attackDistance;
    }





    protected void changeWalkDir() {
        if (walkDir == LEFT) {
            walkDir = RIGHT;
        } else {
            walkDir = LEFT;
        }
    }


}
