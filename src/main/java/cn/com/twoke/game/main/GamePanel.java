package cn.com.twoke.game.main;

import cn.com.twoke.game.inputs.KeyboardInputs;
import cn.com.twoke.game.inputs.MouseInputs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private int xDelta = 100, yDelta = 100;
    public GamePanel() {
        mouseInputs = new MouseInputs(this);

        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    public void changeXDelta(int value) {
        this.xDelta += value;
        repaint();
    }
    public void changeYDelta(int value) {
        this.yDelta += value;
        repaint();
    }

    public void setRectPos(int x, int y) {
        this.xDelta = x;
        this.yDelta = y;
        repaint();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

//        g.drawRect(100, 100, 200, 50);
        g.fillRect(xDelta, yDelta, 200, 50);
    }
}
