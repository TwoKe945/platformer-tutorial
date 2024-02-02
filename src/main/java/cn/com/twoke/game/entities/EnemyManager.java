package cn.com.twoke.game.entities;

import cn.com.twoke.game.gamestates.Playing;
import cn.com.twoke.game.levels.Level;
import cn.com.twoke.game.utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static cn.com.twoke.game.utils.Constants.EnemyConstants.*;

public class EnemyManager {

    private Playing playing;
    private BufferedImage[][] crabbyArr;
    private List<Crabby> crabbies;

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
    }

    public void loadEnemies(Level level) {
        crabbies = level.getCrabbies();
        System.out.println("size of crabs: " + crabbies.size());
    }


    public void update(int[][] lvlData, Player player) {
        boolean isAnyActive  = false;
        for (Crabby crabby : crabbies) {
            if (crabby.isActive()) {
                crabby.update(lvlData, player);
                isAnyActive = true;
            }
        }
        if (!isAnyActive)
            playing.setLevelCompleted(true);
    }


    public void draw(Graphics g, int xLvlOffset) {
        drawCrabbies(g, xLvlOffset);
    }

    private void drawCrabbies(Graphics g,  int xLvlOffset) {
        for (Crabby c : crabbies) {
            if (c.isActive()) {
                g.drawImage(crabbyArr[c.getEnemyState()][c.getAniIndex()],(int) c.getHitBox().x - CRABBY_DRAW_OFFSET_X - xLvlOffset + c.flipX(),(int) c.getHitBox().y - CRABBY_DRAW_OFFSET_Y,CRABBY_WIDTH * c.flipW(),CRABBY_HEIGHT, null);
                c.drawHitBox(g, xLvlOffset);
                c.drawAttackBox(g, xLvlOffset);
            }
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (Crabby crabby : crabbies) {
            if (crabby.isActive()) {
                if (attackBox.intersects(crabby.getHitBox())) {
                    crabby.hurt(10);
                    return;
                }
            }
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

    public void resetAllEnemies() {
        for (Crabby crabby : crabbies) {
            crabby.resetEnemy();
        }

    }
}
