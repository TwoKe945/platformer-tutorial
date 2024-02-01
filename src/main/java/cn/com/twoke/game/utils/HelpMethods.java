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
       return IsTileSolid((int)xIndex, (int) yIndex, levelData);
    }

    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {
        int value = lvlData[(int) yTile][(int) xTile];
        return value >= 48 || value < 0 || value != 11;
    }

    public static boolean IsFloor(Rectangle2D.Float hitBox, float xSpeed, int[][] lvlData) {
        return IsSolid(hitBox.x +xSpeed, hitBox.y + hitBox.height + 1, lvlData);
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


    public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++) {
            if (IsTileSolid(xStart + i, y, lvlData )) {
                return false;
            }
            // fixme 修复在悬崖时，敌人会停留不转身巡逻的问题
            if (!IsTileSolid(xStart + i, y + 1, lvlData )) {
                return false;
            }
        }
        return true;
    }


    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float firstHitBox, Rectangle2D.Float secondHitBox, int yTile) {
        int firstXTile = (int)(firstHitBox.x / Game.TILES_SIZE);
        int secondXTile = (int)(secondHitBox.x / Game.TILES_SIZE);
        if (firstXTile > secondXTile)
            return IsAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
        else
            return IsAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);
    }
}
