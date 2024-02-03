package cn.com.twoke.game.levels;

import cn.com.twoke.game.entities.Crabby;
import cn.com.twoke.game.main.Game;
import cn.com.twoke.game.objects.Cannon;
import cn.com.twoke.game.objects.GameContainer;
import cn.com.twoke.game.objects.Potion;
import cn.com.twoke.game.objects.Spike;
import cn.com.twoke.game.utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static cn.com.twoke.game.utils.LoadSave.*;

public class Level {

    private BufferedImage img;
    private int[][] levelData;
    private List<Crabby> crabbies;
    private List<Potion> potions;
    private List<GameContainer> containers;
    private List<Spike> spikes;
    private List<Cannon> cannons;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;

    private Point lvlSpawn;

    public Level(BufferedImage img) {
        this.img = img;
        createLevelData();
        createEnemies();
        createContainers();
        createPotions();
        createSpikes();
        createCanons();
        createLvlOffsetDaTa();
        calcPlayerSpawn();
    }

    private void createCanons() {
        cannons = GetCanons(img);
    }

    private void createSpikes() {
        spikes = GetSpikes(img);
    }

    private void createPotions() {
        potions = GetPotions(img);
    }

    private void createContainers() {
        containers = GetContainers(img);
    }

    public List<Potion> getPotions() {
        return potions;
    }

    public List<GameContainer> getContainers() {
        return containers;
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

    public List<Spike> getSpikes() {
        return spikes;
    }

    public List<Cannon> getCannons() {
        return cannons;
    }
}
