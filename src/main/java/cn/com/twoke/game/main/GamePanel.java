package cn.com.twoke.game.main;

import cn.com.twoke.game.inputs.KeyboardInputs;
import cn.com.twoke.game.inputs.MouseInputs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private float xDelta = 100, yDelta = 100;
    private BufferedImage bufferedImage, subImage;

    public GamePanel() {
        mouseInputs = new MouseInputs(this);
        importImage();
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void importImage() {
        InputStream is = getClass().getResourceAsStream("/img.png");
        try {
            bufferedImage = ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setPanelSize() {
        Dimension size = new Dimension(1280, 800);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    public void changeXDelta(int value) {
        this.xDelta += value;
    }
    public void changeYDelta(int value) {
        this.yDelta += value;
    }

    public void setRectPos(int x, int y) {
        this.xDelta = x;
        this.yDelta = y;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        subImage = bufferedImage.getSubimage(1 * 64,8 * 40, 64, 40);
//        g.drawImage(bufferedImage.getSubimage(0,0, 64, 40), 0 , 0, null);
        g.drawImage(subImage, (int) xDelta , (int) yDelta, 128, 80, null);
    }

}
