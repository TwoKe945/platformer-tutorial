package cn.com.twoke.game.levels;

import cn.com.twoke.game.entities.Crabby;
import cn.com.twoke.game.main.Game;
import cn.com.twoke.game.utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static cn.com.twoke.game.utils.LoadSave.*;

public class Level {

    private BufferedImage img;
    private int[][] levelData;
    private List<Crabby> crabbies;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;

    private Point lvlSpawn;

    public Level(BufferedImage img) {
        this.img = img;
        createLevelData();
        createEnemies();
        createLvlOffsetDaTa();
        calcPlayerSpawn();
    }

    private void calcPlayerSpawn() {
        lvlSpawn = GetPlayerSpawn(img);
    }

    private void createEnemies() {
        crabbies = GetCrabs(img);
    }

    private void createLvlOffsetDaTa() {
       lvlTilesWide = levelData[0].length;
       maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
       maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE;
    }

    public Point getLvlSpawn() {
        return lvlSpawn;
    }

    private void createLevelData() {
        levelData = GetLevelData(img);
    }

    public int getSpriteIndex(int x, int y) {
        return levelData[y][x];
    }

    public int[][] getLevelData() {
        return levelData;
    }

    public int getLvlTilesWide() {
        return lvlTilesWide;
    }

    public int getMaxTilesOffset() {
        return maxTilesOffset;
    }

    public int getMaxLvlOffsetX() {
        return maxLvlOffsetX;
    }

    public List<Crabby> getCrabbies() {
        return crabbies;
    }

}
