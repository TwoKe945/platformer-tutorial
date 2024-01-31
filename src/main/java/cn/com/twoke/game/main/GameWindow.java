package cn.com.twoke.game.main;

import javax.swing.*;

import static javax.swing.JFrame.EXIT_ON_CLOSE;


public class GameWindow  {
    private JFrame jframe;

    public GameWindow(GamePanel gamePanel) {
        jframe = new JFrame();
//        jframe.setSize(400, 400);
        jframe.setDefaultCloseOperation(EXIT_ON_CLOSE);
        jframe.add(gamePanel);
        // 设置显示位置到屏幕中间
        jframe.setLocationRelativeTo(null);
        jframe.setResizable(false);
        jframe.pack();
        jframe.setVisible(true);
    }

}
