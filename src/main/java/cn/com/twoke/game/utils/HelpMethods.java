package cn.com.twoke.game.utils;

import cn.com.twoke.game.main.Game;

public final class HelpMethods {


    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] levelData) {
        if (!IsSolid(x, y, levelData))
            if (!IsSolid(x+width, y+ height, levelData))
                if(!IsSolid(x+width,y,levelData))
                    return !IsSolid(x, y + height, levelData);
        return false;
    }


    public static boolean IsSolid(float x, float y, int[][] levelData) {
        if (x < 0 || x > Game.GAME_WIDTH)
            return true;
        if (y < 0 || y > Game.GAME_HEIGHT)
            return  true;

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;
        if (levelData.length <= yIndex || levelData[levelData.length-1].length <= xIndex) return true;

        int value = levelData[(int) yIndex][(int) xIndex];
        return value >= 48 || value < 0 || value != 11;
    }


}
