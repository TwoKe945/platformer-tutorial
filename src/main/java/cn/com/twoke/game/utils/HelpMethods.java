package cn.com.twoke.game.utils;

import cn.com.twoke.game.main.Game;

import java.awt.geom.Rectangle2D;

public final class HelpMethods {


    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] levelData) {
        if (!IsSolid(x, y, levelData))
            if (!IsSolid(x+width, y+ height, levelData))
                if(!IsSolid(x+width,y,levelData))
                    return !IsSolid(x, y + height, levelData);
        return false;
    }


    public static boolean IsSolid(float x, float y, int[][] levelData) {
        int maxWidth = levelData[0].length * Game.TILES_SIZE;
        if (x < 0 || x >= maxWidth)
            return true;
        if (y < 0 || y >= Game.GAME_HEIGHT)
            return  true;

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;
        if (levelData.length <= yIndex || levelData[levelData.length-1].length <= xIndex) return true;

        int value = levelData[(int) yIndex][(int) xIndex];
        return value >= 48 || value < 0 || value != 11;
    }


    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitBox, float xSpeed) {
        int currentTile = (int) (hitBox.x / Game.TILES_SIZE);
        if (xSpeed > 0) {
            // Right
            int tileXPos = currentTile * Game.TILES_SIZE;
            int xOffset = (int) (Game.TILES_SIZE - hitBox.width);
            return tileXPos + xOffset - 1;
        } else {
            // Left
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static float GetEntityYPosNextToWall(Rectangle2D.Float hitBox, float airSpeed) {
        int currentTile = (int) (hitBox.y / Game.TILES_SIZE);
        if (airSpeed > 0) {
            // Falling
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = (int) (Game.TILES_SIZE - hitBox.height);
            return tileYPos + yOffset - 1;
        } else {
            // Jumping
            return currentTile * Game.TILES_SIZE;
        }

    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitBox, int[][] levelData) {
        if (!IsSolid(hitBox.x , hitBox.y + hitBox.height + 1, levelData ))
            if (!IsSolid(hitBox.x + hitBox.width , hitBox.y + hitBox.height + 1, levelData ))
                return false;
        return true;
    }
}