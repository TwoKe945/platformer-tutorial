package cn.com.twoke.game.entities;

import cn.com.twoke.game.gamestates.Playing;
import cn.com.twoke.game.utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static cn.com.twoke.game.utils.Constants.EnemyConstants.*;

public class EnemyManager {

    private Playing playing;
    private BufferedImage[][] crabbyArr;
    private ArrayList<Crabby> crabbies;

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
        addEnemies();
    }

    private void addEnemies() {
        crabbies = LoadSave.GetCrabs();
        System.out.println("size of crabs: " + crabbies.size());
    }


    public void update(int[][] lvlData, Player player) {
        for (Crabby crabby : crabbies) {
            crabby.update(lvlData, player);
        }
    }


    public void draw(Graphics g, int xLvlOffset) {
        drawCrabbies(g, xLvlOffset);
    }

    private void drawCrabbies(Graphics g,  int xLvlOffset) {
        for (Crabby c : crabbies) {
            g.drawImage(crabbyArr[c.getEnemyState()][c.getAniIndex()],(int) c.getHitBox().x - CRABBY_DRAW_OFFSET_X - xLvlOffset,(int) c.getHitBox().y - CRABBY_DRAW_OFFSET_Y,CRABBY_WIDTH,CRABBY_HEIGHT, null);
            c.drawHitBox(g, xLvlOffset);
        }
    }

    private void loadEnemyImgs() {
        crabbyArr = new BufferedImage[5][9];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);

        for (int j = 0; j < crabbyArr.length; j++) {
            for (int i = 0; i < crabbyArr[j].length; i++) {
                crabbyArr[j][i] = temp.getSubimage(i * CRABBY_WIDTH_DEFAULT,
                        j * CRABBY_HEIGHT_DEFAULT,
                         CRABBY_WIDTH_DEFAULT,
                        CRABBY_HEIGHT_DEFAULT);
            }
        }


    }

}
