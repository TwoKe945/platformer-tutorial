package cn.com.twoke.game.entities;

import cn.com.twoke.game.audio.AudioPlayer;
import cn.com.twoke.game.gamestates.Playing;
import cn.com.twoke.game.main.Game;
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


    private int powerBarWidth = (int)(104 * Game.SCALE);
    private int powerBarHeight = (int)(2 * Game.SCALE);
    private int powerBarXStart = (int)(44 * Game.SCALE);
    private int powerBarYStart = (int)(34 * Game.SCALE);
    private int powerWidth = powerBarWidth;
    private int powerMaxValue = 200;
    private int powerValue = powerMaxValue;

    private int flipX = 0;
    private int flipW = 1;

    protected boolean attackChecked;
    private Playing playing;

    private int tileY;

    private boolean powerAttackActive;
    private int powerAttackTick;
    private int powerGrowSpeed = 15;
    private int powerGrowTick = 15;

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
        updatePowerBar();
        updateHealthBar();
        if (currentHealth <= 0) {
             if (state != DEAD) {
                 state = DEAD;
                 aniIndex = 0;
                 aniTick = 0;
                 playing.setPlayerDying(true);
                 playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
             } else if (aniIndex == GetSpriteAmount(DEAD) - 1 && aniTick >= ANI_SPEED - 1) {
                playing.setGameOver(true);
                 playing.getGame().getAudioPlayer().stopSong();
                 playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
             } else {
                 updateAnimationTick();
             }
            return;
        }
        updateAttackBox();

        updatePos();
        if (moving) {
            checkPotionTouched();
            checkSpikeTouched();
            tileY = (int)(hitBox.y / Game.TILES_SIZE);
            if (powerAttackActive) {
                powerAttackTick++;
                if (powerAttackTick >= 35) {
                    powerAttackTick = 0;
                    powerAttackActive = false;
                }
            }
        }
        if (attacking || powerAttackActive)
            checkAttack();
        updateAnimationTick();
        setAnimation();
    }

    private void checkSpikeTouched() {
        playing.checkSpikeTouched(this);
    }

    private void checkPotionTouched() {

        playing.checkPotionTouched(hitBox);

    }

    private void checkAttack() {
        if (attackChecked || aniIndex != 1)
            return;
        attackChecked = true;
        if (powerAttackActive) {
            attackChecked = false;
        }
        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
        playing.getGame().getAudioPlayer().playAttackSound();
    }

    private void updateAttackBox() {
        if (right || (powerAttackActive && flipW == 1)) {
            attackBox.x = hitBox.x + hitBox.width + (int)(Game.SCALE * 10);
        } else if (left || (powerAttackActive && flipW == -1)) {
            attackBox.x =  hitBox.x - hitBox.width - (int)(Game.SCALE * 10);
        }
        attackBox.y = hitBox.y + (Game.SCALE * 10);
    }

    private void updateHealthBar() {
        healthWidth = (int)((currentHealth / (float) maxHealth) * healthBarWidth);
    }

    private void updatePowerBar() {
        powerWidth = (int)((powerValue / (float) powerMaxValue) * powerBarWidth);
        System.out.println("power: " + powerWidth);
        powerGrowTick++;
        if (powerGrowTick >= powerGrowSpeed) {
            powerGrowTick = 0;
            changePower(1);
        }
    }

    public void render(Graphics g, int lvlOffset) {
        g.drawImage(animations[state][aniIndex], (int) (hitBox.x - xDrawOffset) - lvlOffset + flipX, (int) (hitBox.y - yDrawOffset), width * flipW, height, null);
        drawHitBox(g, lvlOffset);
        drawAttackBox(g, lvlOffset);
        drawUI(g);
    }


    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        // Health Bar
        g.setColor(Color.RED);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);


        // Power Bar
        g.setColor(Color.YELLOW);
        g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);

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

    public void changePower(int value) {
        powerValue += value;
        if (powerValue >= powerMaxValue) {
            powerValue = powerMaxValue;
        } else if (powerValue <= 0) {
            powerValue = 0;
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

        if (powerAttackActive) {
            state = ATTACK;
            aniIndex = 1;
            aniTick = 0;
            return;
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

        if (!inAir)
            if (!powerAttackActive)
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

        if (powerAttackActive) {
            if (!left && !right) {
                if (flipW == -1) {
                    xSpeed = -walkSpeed;
                } else {
                    xSpeed = walkSpeed;
                }
            }
            xSpeed *= 3;
        }

        if (!inAir)
            if (!HelpMethods.IsEntityOnFloor(hitBox, levelData))
                inAir = true;
        if (inAir && !powerAttackActive) {
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
        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
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
            if (powerAttackActive) {
                powerAttackActive = false;
                powerAttackTick = 0;
            }
        }
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(state)){
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
        powerValue = powerMaxValue;

        hitBox.x = x;
        hitBox.y = y;
        if (right) {
            attackBox.x = hitBox.x + hitBox.width + (int)(Game.SCALE * 10);
        } else if (left) {
            attackBox.x =  hitBox.x - hitBox.width - (int)(Game.SCALE * 10);
        } else {
            attackBox.x = x;
        }
        attackBox.y = hitBox.y + (Game.SCALE * 10);

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

    public void kill() {
        currentHealth = 0;
    }

    public int getTileY() {
        return tileY;
    }

    public void powerAttack() {
        if (powerAttackActive)  return;
        if (powerValue >= 60) {
            powerAttackActive = true;
            changePower(-60);
        }
    }
}
