package cn.com.twoke.game.utils;

import cn.com.twoke.game.entities.Crabby;
import cn.com.twoke.game.main.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.com.twoke.game.utils.Constants.EnemyConstants.CRABBY;

public class LoadSave {


    public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
//    public static final String LEVEL_ONE_DATA = "level_one_data.png";
    public static final String LEVEL_ONE_DATA = "level_one_data_long.png";
    public static final String MENU_BACKGROUND_ATLAS = "menu_background.png";
    public static final String BACKGROUND_MENU = "background_menu.png";
    public static final String BUTTON_ATLAS = "button_atlas.png";
    public static final String PAUSE_MENU = "pause_menu.png";
    public static final String SOUND_BUTTON = "sound_button.png";
    public static final String URM_BUTTON = "urm_buttons.png";
    public static final String VOLUME_BUTTON = "volume_buttons.png";


    // 背景
    public static final String PLAYING_BG_IMG = "playing_bg_img.png";
    public static final String SMALL_CLOUDS = "small_clouds.png";
    public static final String BIG_CLOUDS = "big_clouds.png";

    // 敌人
    public static final String CRABBY_SPRITE = "crabby_sprite.png";
    public static final String HEALTH_POWER_BAR = "health_power_bar.png";

    //
    public static final String COMPLETED_SPRITE = "completed_sprite.png";


    // 关卡

    public static BufferedImage[] GetAllLevels() {
        URL url = LoadSave.class.getResource("/lvls");
        File file = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        File[] files = Optional.ofNullable(file).orElseThrow(() -> new RuntimeException("关卡文件夹不能为空！")).listFiles();
        return Arrays.stream(Optional.ofNullable(files).orElseThrow(() -> new RuntimeException("关卡文件不能为空！"))).sorted(LoadSave::compareLevelFiles).map(LoadSave::fileToBufferedImage)
                .toArray(BufferedImage[]::new);
    }

    private static int parseInt(File lvlFile) {
        return Integer.parseInt(lvlFile.getName().replace(".png", "") );
    }

    private static BufferedImage fileToBufferedImage(File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static int compareLevelFiles(File lvlA,File lvlB) {
        return parseInt(lvlA) - parseInt(lvlB);
    }

    public static ArrayList<Crabby> GetCrabs( BufferedImage img) {
        ArrayList<Crabby> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i,j));
                int value = color.getGreen();
                if (value == CRABBY)
                    list.add(new Crabby(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        }
        return list;
    }

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


    public static int[][] GetLevelData(BufferedImage img) {
        int[][] levelData = new int[img.getHeight()][img.getWidth()];
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


    public static Point GetPlayerSpawn(BufferedImage img) {
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i,j));
                int value = color.getGreen();
                if (value == 100)
                    return new Point(i * Game.TILES_SIZE, j * Game.TILES_SIZE);
            }
        }
        return new Point( Game.TILES_SIZE,  Game.TILES_SIZE);
    }



}
