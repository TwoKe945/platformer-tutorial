package cn.com.twoke.game.entities;

import cn.com.twoke.game.main.Game;
import cn.com.twoke.game.utils.HelpMethods;
import cn.com.twoke.game.utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static cn.com.twoke.game.utils.Constants.PlayerConstants.*;

public class Player extends Entity {

    private BufferedImage[][] animations;
    private int aniTick = 0, aniIndex = 0, aniSpeed = 15;
    private int playerAction = IDLE;
    private boolean moving = false, attacking = false;
    private boolean left, up, right, down, jump;
    private float  playerSpeed = 1.0f * Game.SCALE;
    private int[][] levelData;
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;

    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        initHitBox(x, y, (int)(20 * Game.SCALE), (int) (27 * Game.SCALE));
    }


    public void update() {
        updatePos();
        updateAnimationTick();
        setAnimation();
    }

    public void render(Graphics g, int lvlOffset) {
        g.drawImage(animations[playerAction][aniIndex], (int) (hitBox.x - xDrawOffset) - lvlOffset , (int) (hitBox.y - yDrawOffset), width, height, null);
        drawHitBox(g, lvlOffset);
    }

    public void setAnimation() {
        int startAni = playerAction;

        if (moving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }

        if (inAir) {
            if (airSpeed < 0) {
                playerAction = JUMP;
            } else {
                playerAction = FALLING;
            }
        }

        if (attacking)
            playerAction = ATTACK_1;

        if (startAni != playerAction) {
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
        animations = new BufferedImage[9][6];
        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i]  = image.getSubimage(i * 64, j * 40 ,64, 40);
            }
        }
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

        if (left)
            xSpeed -= playerSpeed;

        if (right)
            xSpeed += playerSpeed;

        if (!inAir)
            if (!HelpMethods.IsEntityOnFloor(hitBox, levelData))
                inAir = true;
        if (inAir) {
            if (HelpMethods.CanMoveHere(hitBox.x, hitBox.y+airSpeed, hitBox.width, hitBox.height, levelData)) {
                hitBox.y += airSpeed;
                airSpeed += gravity;
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
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount(playerAction)){
                aniIndex = 0;
                attacking = false;
            }
        }
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isDown() {
        return down;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void resetDitBoolean() {
        left = false;
        right = false;
        up = false;
        down = false;
    }
    public void setAttack(boolean attacking) {
        this.attacking = attacking;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }
}
