package cn.com.twoke.game.gamestates;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface StateMethods {
    default void update() {};
    default void test() {};
    default void draw(Graphics g) {};
    default void mouseClicked(MouseEvent e) {};
    default void mousePressed(MouseEvent e) {};
    default void mouseReleased(MouseEvent e) {};
    default void mouseMoved(MouseEvent e) {};
    default  void mouseDragged(MouseEvent e) {};
    default void keyPressed(KeyEvent e) {};
    default void keyReleased(KeyEvent e) {};
}
