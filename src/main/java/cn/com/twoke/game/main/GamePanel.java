package cn.com.twoke.game.main;

import cn.com.twoke.game.inputs.KeyboardInputs;
import cn.com.twoke.game.inputs.MouseInputs;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private float xDelta = 100, yDelta = 100;
    private float xDir = 1f, yDir = 1f;
    private Color color = Color.BLUE;
    private Random random;

    private ArrayList<MyRect> rects = new ArrayList<>();

    public GamePanel() {
        random = new Random();
        mouseInputs = new MouseInputs(this);

        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
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

    public void spawnRect(int x, int y) {
        rects.add(new MyRect(x, y));
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (MyRect rect: rects) {
            rect.updateRect();
            rect.draw(g);
        }

        updateRectangle();
        g.setColor(color);
        g.fillRect((int)xDelta, (int)yDelta, 200, 50);

//        repaint();
    }

    private void updateRectangle() {
        this.xDelta += xDir;
        if ((xDelta + 200) > 400 || xDelta < 0){
            xDir*=-1;
            color = getRndColor();
        }

        this.yDelta += yDir;
        if ((yDelta + 50) > 400 || yDelta < 0){
            yDir *= -1;
            color = getRndColor();
        }
    }

    private Color getRndColor() {
        int r = random.nextInt(255);
        int b = random.nextInt(255);
        int g = random.nextInt(255);
        return new Color(r, b, g);
    }




    public class MyRect {
        int x, y, w, h;
        int xDir = 1, yDir = 1;
        Color color;

        public MyRect(int x, int y) {
            this.x = x;
            this.y = y;
            w = random.nextInt(50);
            h = w;
            color = newColor();
        }

        public void updateRect() {
            this.x += this.xDir;
            this.y += this.yDir;

            if ((x + w) > 400 || x < 0){
                this.xDir *=-1;
                color = newColor();
            }

            if ((y + h) > 400 || y < 0){
                this.yDir *= -1;
                color = newColor();
            }
        }

        private Color newColor() {
            int r = random.nextInt(255);
            int b = random.nextInt(255);
            int g = random.nextInt(255);
            return new Color(r, b, g);
        }

        public void draw(Graphics g) {
            g.setColor(color);
            g.fillRect(x,y,w,h);
        }

    }
}
