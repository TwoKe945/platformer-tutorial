package cn.com.twoke.game.utils;

import cn.com.twoke.game.main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LoadSave {


    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    public static final String LEVEL_ONE_ATLAS = "level_one_data.png";
    public static final String MENU_BACKGROUND_ATLAS = "menu_background.png";
    public static final String BUTTON_ATLAS = "button_atlas.png";
    public static final String PAUSE_MENU = "pause_menu.png";
    public static final String SOUND_BUTTON = "sound_button.png";
    public static final String URM_BUTTON = "urm_buttons.png";
    public static final String VOLUME_BUTTON = "volume_buttons.png";

    public static BufferedImage GetSpriteAtlas(String spriteFileName) {
        BufferedImage image = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + spriteFileName);
        try {
            image = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return image;
    }


    public static int[][] GetLevelData() {
        int[][] levelData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
        BufferedImage img = GetSpriteAtlas(LEVEL_ONE_ATLAS);
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i,j));
                int value = color.getRed();
                if (value >= 48)
                    value = 0;
                levelData[j][i] =value;
            }
        }
        return levelData;
    }

}
