package cn.com.twoke.game.objects;

import cn.com.twoke.game.gamestates.Playing;
import cn.com.twoke.game.utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static cn.com.twoke.game.utils.Constants.ObjectConstants.*;

/**
 * 游戏对象管理器
 */
public class ObjectManager {

    private Playing playing;
    private BufferedImage[][] potionImgs,containerImgs;

    private List<Potion> potions;
    private List<GameContainer> containers;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImgs();

        potions = new ArrayList<>();
        containers = new ArrayList<>();

        potions.add(new Potion(300, 300, RED_POTION));
        potions.add(new Potion(400, 300, BLUE_POTION));
        containers.add(new GameContainer(500, 300, BARREL));
        containers.add(new GameContainer(600, 300, BOX));
    }

    private void loadImgs() {
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTIONS_SPRITES);
        potionImgs = new BufferedImage[2][7];

        for (int j = 0; j < potionImgs.length; j++) {
            for (int i = 0; i < potionImgs[j].length; i++) {
                potionImgs[j][i] = potionSprite.getSubimage(POTION_WIDTH_DEFAULT * i,
                        POTION_HEIGHT_DEFAULT * j, POTION_WIDTH_DEFAULT, POTION_HEIGHT_DEFAULT);
            }
        }

        BufferedImage objectsSprite = LoadSave.GetSpriteAtlas(LoadSave.OBJECTS_SPRITES);
        containerImgs = new BufferedImage[2][8];

        for (int j = 0; j < containerImgs.length; j++) {
            for (int i = 0; i < containerImgs[j].length; i++) {
                containerImgs[j][i] = objectsSprite.getSubimage(CONTAINER_WIDTH_DEFAULT * i,
                        CONTAINER_HEIGHT_DEFAULT * j, CONTAINER_WIDTH_DEFAULT, CONTAINER_HEIGHT_DEFAULT);
            }
        }
    }


    public void update() {
        for (Potion potion : potions) {
            if (potion.isActive())
                potion.update();
        }

        for (GameContainer container : containers) {
            if(container.isActive())
                container.update();
        }


    }


    public void draw(Graphics g, int xLvlOffset) {
        drawPotions(g,xLvlOffset);
        drawContainers(g,xLvlOffset);
    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        for (GameContainer container : containers) {
            if(container.isActive()) {
                int type = 0;
                if (container.getObjType() == BARREL) {
                    type = 1;
                }
                g.drawImage(containerImgs[type][container.getAniIndex()],
                        (int)(container.getHitBox().x - container.getXDrawOffset() - xLvlOffset),
                        (int)(container.getHitBox().y - container.getYDrawOffset()),
                        CONTAINER_WIDTH,
                        CONTAINER_HEIGHT,
                        null
                );
                container.drawHitBox(g, xLvlOffset);
            }
        }
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for (Potion potion : potions) {
            if (potion.isActive()) {
                int type = 0;
                if (potion.getObjType() == RED_POTION) {
                    type = 1;
                }
                g.drawImage(potionImgs[type][potion.getAniIndex()],
                        (int)(potion.getHitBox().x - potion.getXDrawOffset() - xLvlOffset),
                        (int)(potion.getHitBox().y - potion.getYDrawOffset()),
                        POTION_WIDTH,
                        POTION_HEIGHT,
                        null
                );
                potion.drawHitBox(g, xLvlOffset);
            }
        }
    }

}
