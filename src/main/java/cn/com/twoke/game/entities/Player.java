package cn.com.twoke.game.entities;

import cn.com.twoke.game.gamestates.Playing;
import cn.com.twoke.game.main.Game;
import cn.com.twoke.game.utils.Constants;
import cn.com.twoke.game.utils.HelpMethods;
import cn.com.twoke.game.utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static cn.com.twoke.game.utils.Constants.ANI_SPEED;
import static cn.com.twoke.game.utils.Constants.GRAVITY;
import static cn.com.twoke.game.utils.Constants.PlayerConstants.*;

public class Player extends Entity {

    private BufferedImage[][] animations;
    private boolean moving = false, attacking = false;
    private boolean left, right, jump;
    private int[][] levelData;
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;

    // Jumping / Gravity
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;

    // StatusBarUI
    private  BufferedImage statusBarImg;

    private int statusBarWidth = (int)(192 * Game.SCALE);
    private int statusBarHeight = (int)(58 * Game.SCALE);
    private int statusBarX = (int)(10 * Game.SCALE);
    private int statusBarY = (int)(10 * Game.SCALE);

    private int healthBarWidth = (int)(150 * Game.SCALE);
    private int healthBarHeight = (int)(4 * Game.SCALE);
    private int healthBarXStart = (int)(34 * Game.SCALE);
    private int healthBarYStart = (int)(14 * Game.SCALE);
    private int healthWidth = healthBarWidth;

    private int flipX = 0;
    private int flipW = 1;

    protected boolean attackChecked;
    private Playing playing;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        this.state =IDLE;
        this.maxHealth = 100;
        this.currentHealth = this.maxHealth;
        this.walkSpeed = 1.0f * Game.SCALE;
        loadAnimations();
        initHitBox(20, 27);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int)(20 * Game.SCALE), (int)(20 * Game.SCALE));
    }

    public void update() {
        updateHealthBar();
        if (currentHealth <= 0) {
            playing.setGameOver(true);
            return;
        }
        updateAttackBox();
        updatePos();
        if (attacking)
            checkAttack();
        updateAnimationTick();
        setAnimation();
    }

    private void checkAttack() {
        if (attackChecked || aniIndex != 1)
            return;
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
    }

    private void updateAttackBox() {
        if (right) {
            attackBox.x = hitBox.x + hitBox.width + (int)(Game.SCALE * 10);
        } else if (left) {
            attackBox.x =  hitBox.x - hitBox.width - (int)(Game.SCALE * 10);
        }
        attackBox.y = hitBox.y + (Game.SCALE * 10);
    }

    private void updateHealthBar() {
        healthWidth = (int)((currentHealth / (float) maxHealth) * healthBarWidth);
    }

    public void render(Graphics g, int lvlOffset) {
        g.drawImage(animations[state][aniIndex], (int) (hitBox.x - xDrawOffset) - lvlOffset + flipX, (int) (hitBox.y - yDrawOffset), width * flipW, height, null);
        drawHitBox(g, lvlOffset);
        drawAttackBox(g, lvlOffset);
        drawUI(g);
    }


    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.RED);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
    }

    public void changeHealth(int value) {
        currentHealth += value;
        if (currentHealth <= 0) {
            currentHealth = 0;
            // gameOver();
        } else if (currentHealth >= maxHealth){
            currentHealth = maxHealth;
        }
    }

    public void setAnimation() {
        int startAni = state;

        if (moving) {
            state = RUNNING;
        } else {
            state = IDLE;
        }

        if (inAir) {
            if (airSpeed < 0) {
                state = JUMP;
            } else {
                state = FALLING;
            }
        }

        if (attacking) {
            state = ATTACK;
            if (startAni != ATTACK) {
                aniIndex = 1;
                aniTick = 0;
                return;
            }
        }

        if (startAni != state) {
            resetAniTick();
        }
    }


    public void  loadLevelData(int[][] levelData) {
        this.levelData = levelData;
        if (!HelpMethods.IsEntityOnFloor(hitBox, levelData)) {
            this.inAir = true;
        }
    }
    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void loadAnimations() {
        BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animations = new BufferedImage[7][8];
        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i]  = image.getSubimage(i * 64, j * 40 ,64, 40);
            }
        }
        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.HEALTH_POWER_BAR);
    }

    private void updatePos() {
        moving = false;
        if (jump)
            jump();
//        if (!left && !right && !inAir)
//            return;
        if (!inAir)
            if (!right && !left || (left && right))
                return;

        float xSpeed = 0;

        if (left) {
            xSpeed -= walkSpeed;
            flipX = width;
            flipW = -1;
        }

        if (right) {
            xSpeed += walkSpeed;
            flipX = 0;
            flipW = 1;
        }

        if (!inAir)
            if (!HelpMethods.IsEntityOnFloor(hitBox, levelData))
                inAir = true;
        if (inAir) {
            if (HelpMethods.CanMoveHere(hitBox.x, hitBox.y+airSpeed, hitBox.width, hitBox.height, levelData)) {
                hitBox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            } else {
                hitBox.y = HelpMethods.GetEntityYPosNextToWall(hitBox, airSpeed);
                if (airSpeed > 0) {
                    resetInAir();
                } else {
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPos(xSpeed);
            }
        } else {
            updateXPos(xSpeed);
        }

        moving = true;

    }

    private void jump() {
        if (inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if (HelpMethods.CanMoveHere(hitBox.x+xSpeed, hitBox.y, hitBox.width, hitBox.height, levelData)) {
            hitBox.x += xSpeed;
        } else {
            hitBox.x = HelpMethods.GetEntityXPosNextToWall(hitBox, xSpeed);
        }
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount(state)){
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
            }
        }
    }

    public boolean isLeft() {
        return left;
    }


    public boolean isRight() {
        return right;
    }


    public void setLeft(boolean left) {
        this.left = left;
    }


    public void setRight(boolean right) {
        this.right = right;
    }

    public void resetDirBoolean() {
        left = false;
        right = false;
    }
    public void setAttack(boolean attacking) {
        this.attacking = attacking;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void resetAll() {
        resetDirBoolean();
        inAir = false;
        attacking = false;
        moving = false;
        state = IDLE;
        currentHealth = maxHealth;

        hitBox.x = x;
        hitBox.y = y;

        if (!HelpMethods.IsEntityOnFloor(hitBox, levelData)) {
            this.inAir = true;
        }
    }


    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitBox.x = x;
        hitBox.y = y;
    }

}
