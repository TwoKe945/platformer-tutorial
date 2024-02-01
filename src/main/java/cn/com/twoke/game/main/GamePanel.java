package cn.com.twoke.game.main;

import cn.com.twoke.game.inputs.KeyboardInputs;
import cn.com.twoke.game.inputs.MouseInputs;
import javax.swing.*;
import java.awt.*;

import static cn.com.twoke.game.main.Game.GAME_HEIGHT;
import static cn.com.twoke.game.main.Game.GAME_WIDTH;

public class GamePanel extends JPanel {

    private final Game game;

    public GamePanel(Game game) {
        this.game = game;
        MouseInputs mouseInputs = new MouseInputs(this);
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }



    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }

    public Game getGame() {
        return this.game;
    }

}
