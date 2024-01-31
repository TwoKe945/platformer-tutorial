package cn.com.twoke.game.main;

import cn.com.twoke.game.inputs.KeyboardInputs;
import cn.com.twoke.game.inputs.MouseInputs;
import cn.com.twoke.game.utils.Constants;
import com.sun.xml.internal.bind.v2.model.core.ID;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static cn.com.twoke.game.utils.Constants.PlayerConstants.*;
import static cn.com.twoke.game.utils.Constants.Direction.*;

public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private float xDelta = 0, yDelta = 0;
    private BufferedImage bufferedImage;
    private BufferedImage[][] animations;
    private int aniTick = 0, aniIndex = 0, aniSpeed = 15;
    private int playerAction = IDLE;
    private int playerDir = -1;
    private boolean moving = false;

    public GamePanel() {
        mouseInputs = new MouseInputs(this);
        importImage();
        loadAnimations();

        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void loadAnimations() {
        animations = new BufferedImage[9][6];

        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i]  = bufferedImage.getSubimage(i * 64, j * 40 ,64, 40);
            }
        }
    }

    private void importImage() {
        InputStream is = getClass().getResourceAsStream("/img.png");
        try {
            bufferedImage = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setPanelSize() {
        Dimension size = new Dimension(1280, 800);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    public void setDirection(int direction) {
        this.playerDir = direction;
        this.moving = true;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void setAnimation() {
        if (moving) {
            playerAction = RUNNING;
        } else {
            playerAction = IDLE;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        updateAnimationTick();
        setAnimation();
        updatePos();
        g.drawImage(animations[playerAction][aniIndex], (int) xDelta , (int) yDelta, 256, 160, null);
    }

    private void updatePos() {
        if (moving) {
            switch (playerDir) {
                case LEFT:
                    xDelta-=5;
                    break;
                case UP:
                    yDelta-=5;
                    break;
                case RIGHT:
                    xDelta+=5;
                    break;
                case DOWN:
                    yDelta+=5;
                    break;
            }
        }

    }

    private void updateAnimationTick() {
        aniTick++;

        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= getSpriteAmount(playerAction))
                aniIndex = 0;
        }

    }

}
