package cn.com.twoke.game.levels;

public class Level {

    private int[][] levelData;

    public Level(int[][] levelData) {
        this.levelData = levelData;
    }

    public int getSpriteIndex(int x, int y) {
        return levelData[y][x];
    }

}
