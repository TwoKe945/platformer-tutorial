package cn.com.twoke.game.levels;

import cn.com.twoke.game.gamestates.GameState;
import cn.com.twoke.game.main.Game;
import cn.com.twoke.game.utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LevelManager {

    private Game game;
    private BufferedImage[] levelSprite;
    private List<Level> levels;
    private int lvlIndex = 0;

    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprites();
        buildLevels();
    }

    private void buildLevels() {
        levels = Arrays.stream(LoadSave.GetAllLevels()).map(img -> new Level(img)).collect(Collectors.toList());
        lvlIndex = 0;
    }

    private void importOutsideSprites() {
        BufferedImage tempImage = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[48];
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 12; i++) {
                int index = j * 12+ i;
                levelSprite[index] = tempImage.getSubimage(i * 32, j * 32, 32, 32);
            }
        }

    }

    public void draw(Graphics g, int lvlOffset) {
        for (int j = 0; j < Game.TILES_IN_HEIGHT; j++) {
            for (int i = 0; i < levels.get(lvlIndex).getLevelData()[0].length; i++) {
                int index = levels.get(lvlIndex).getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], Game.TILES_SIZE * i - lvlOffset,Game.TILES_SIZE * j, Game.TILES_SIZE,Game.TILES_SIZE, null);
            }
        }
    }

    public void update() {

    }


    public Level getCurrentLevel() {
        return levels.get(lvlIndex);
    }

    public int getAmountOfLevels() {
        return levels.size();
    }

    public void loadNextLevel() {
        lvlIndex++;
        if (lvlIndex >= levels.size()) {
            lvlIndex = 0;
            System.out.println("No More levels! Game Completed!");
            GameState.currentState = GameState.MENU;
        }
        Level newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLevelData(newLevel.getLevelData());
        game.getPlaying().setMaxLvlOffsetX(newLevel.getMaxLvlOffsetX());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
    }
}
