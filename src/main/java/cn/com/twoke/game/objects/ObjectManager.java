package cn.com.twoke.game.objects;

import cn.com.twoke.game.entities.Player;
import cn.com.twoke.game.gamestates.Playing;
import cn.com.twoke.game.levels.Level;
import cn.com.twoke.game.main.Game;
import cn.com.twoke.game.utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static cn.com.twoke.game.utils.Constants.ObjectConstants.*;
import static cn.com.twoke.game.utils.Constants.Projectiles.CANNON_BALL_HEIGHT;
import static cn.com.twoke.game.utils.Constants.Projectiles.CANNON_BALL_WIDTH;
import static cn.com.twoke.game.utils.HelpMethods.CanCannonSeePlayer;
import static cn.com.twoke.game.utils.HelpMethods.IsProjectileHittingLevel;
import static cn.com.twoke.game.utils.LoadSave.*;

/**
 * 游戏对象管理器
 */
public class ObjectManager {

    private Playing playing;
    private BufferedImage[][] potionImgs,containerImgs;
    private BufferedImage spikeImg, cannonBallImg;

    private BufferedImage[] canonImgs;

    private List<Potion> potions;
    private List<GameContainer> containers;
    private List<Spike> spikes;
    private List<Cannon> cannons;
    private List<Projectile> projectiles = new ArrayList<>();

    private boolean resetting = false;

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImgs();

    }

    public void checkObjectTouched(Rectangle2D.Float hitbox) {
        for (Potion potion : potions) {
            if (potion.isActive()) {
                if (hitbox.intersects(potion.getHitBox())) {
                    potion.setActive(false);
                    applyEffectToPlayer(potion);
                }
            }
        }
    }

    public void checkSpikeTouched(Player player) {
        for (Spike spike : spikes) {
            if (player.getHitBox().intersects(spike.getHitBox())) {
               player.kill();
            }
        }
    }

    public void applyEffectToPlayer(Potion potion) {
        if (potion.getObjType() == RED_POTION) {
          playing.getPlayer().changeHealth(RED_POTION_VALUE);
        } else {
           playing.getPlayer().changePower(BLUE_POTION_VALUE);
        }
    }

    public void checkObjectHit(Rectangle2D.Float attackBox) {
        for (GameContainer container : containers) {
            if (container.isActive() && !container.doAnimation) {
                if (container.getHitBox().intersects(attackBox)) {
                    container.setAnimation(true);
                    int  type = 0;
                    if (container.getObjType() == BARREL) {
                        type = 1;
                    }
                    potions.add(new Potion((int)(container.getHitBox().x + container.getHitBox().width / 4),
                            (int)(container.getHitBox().y - container.getHitBox().height / 5), type));
                    return;
                }
            }
        }
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
        spikeImg = LoadSave.GetSpriteAtlas(TRAP_ATLAS);
        canonImgs = new BufferedImage[7];
        BufferedImage temp = LoadSave.GetSpriteAtlas(CANON_ATLAS);
        for (int i = 0; i < canonImgs.length; i++) {
            canonImgs[i] = temp.getSubimage((i * CANON_WIDTH_DEFAULT), 0, CANON_WIDTH_DEFAULT, CANON_HEIGHT_DEFAULT);
        }
        cannonBallImg = LoadSave.GetSpriteAtlas(BALL);
    }


    public void update(int[][] levelData, Player player) {
        for (Potion potion : potions) {
            if (potion.isActive())
                potion.update();
        }

        for (GameContainer container : containers) {
            if(container.isActive())
                container.update();
        }

        updateCannons(levelData, player);
        updateProjectiles(levelData, player);

    }

    private void updateProjectiles(int[][] levelData, Player player) {
        for (Projectile projectile : projectiles) {
            if (projectile.isActive()) {
                projectile.updatePos();
                if (projectile.getHitBox().intersects(player.getHitBox())) {
                    player.changeHealth(-25);
                    projectile.setActive(false);
                } else if (IsProjectileHittingLevel(projectile, levelData)) {
                    projectile.setActive(false);
                }
            }
        }
    }

    private void updateCannons(int[][] levelData, Player player) {
        for (Cannon cannon : cannons) {
            if (!cannon.doAnimation) {
                if (cannon.getTileY() == player.getTileY()) {
                    if (isPlayerInRange(cannon, player)) {
                        if (isPlayerInFrontOfCannon(cannon, player)) {
                            if (CanCannonSeePlayer(levelData, player.getHitBox(), cannon.getHitBox(), cannon.getTileY())) {
                                cannon.setAnimation(true);
                            }
                        }
                    }
                }
            }
            cannon.update();
            if (cannon.getAniIndex() == 4 && cannon.getAniTick() == 0) {
                shootCannon(cannon);
            }
        }
    }

    private void shootCannon(Cannon cannon) {
        int dir = 1;
        if (cannon.getObjType() == CANON_LEFT) {
            dir = -1;
        }
        projectiles.add(new Projectile((int)cannon.getHitBox().x, (int)cannon.getHitBox().y, dir));
    }

    private boolean isPlayerInFrontOfCannon(Cannon cannon, Player player) {
        if (cannon.getObjType() == CANON_LEFT) {
            return cannon.getHitBox().x > player.getHitBox().x;
        } else {
            return cannon.getHitBox().x < player.getHitBox().x;
        }
    }

    private boolean isPlayerInRange(Cannon cannon, Player player) {
        int absValue = (int)(Math.abs(player.getHitBox().x - cannon.getHitBox().x));
        return absValue <= Game.TILES_SIZE * 5;
    }


    public void draw(Graphics g, int xLvlOffset) {
        drawPotions(g,xLvlOffset);
        drawContainers(g,xLvlOffset);
        drawTraps(g, xLvlOffset);
        drawCannons(g, xLvlOffset);
        drawProjectiles(g, xLvlOffset);
    }

    private void drawProjectiles(Graphics g, int xLvlOffset) {
        for (Projectile projectile : projectiles) {
            if (projectile.isActive()) {
                g.drawImage(cannonBallImg,
                        (int)(projectile.getHitBox().x - xLvlOffset),
                        (int)(projectile.getHitBox().y ),
                        CANNON_BALL_WIDTH,
                        CANNON_BALL_HEIGHT,
                        null
                );
            }
        }

    }

    private void drawCannons(Graphics g, int xLvlOffset) {
        for (Cannon c : cannons) {
            int x = (int)(c.getHitBox().x  - xLvlOffset);
            int width = CANON_WIDTH;
            if (c.getObjType() == CANON_RIGHT) {
                x += width;
                width *= -1;
            }
            g.drawImage(canonImgs[c.getAniIndex()], x,
                    (int)(c.getHitBox().y ), width, CANON_HEIGHT, null);
            c.drawHitBox(g, xLvlOffset);
        }
    }

    private void drawTraps(Graphics g, int xLvlOffset) {
        for (Spike spike : spikes) {
            g.drawImage(spikeImg, (int)(spike.getHitBox().x - spike.getXDrawOffset() - xLvlOffset ),
                    (int)( spike.getHitBox().y -spike.getYDrawOffset()), SPIKE_WIDTH, SPIKE_HEIGHT, null);
            spike.drawHitBox(g, xLvlOffset);
        }
    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        if (resetting) return;
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
        if (resetting) return;
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

    public void loadObjects(Level level) {
        this.potions = new ArrayList<>(level.getPotions());
        this.containers = new ArrayList<>(level.getContainers());
        spikes = level.getSpikes();
        cannons = level.getCannons();
        projectiles.clear();
    }

    public void resetAllObjects() {
        System.out.println("Size of arrays: " + potions.size() + " - " + containers.size());
        resetting = true;
        loadObjects(playing.getLevelManager().getCurrentLevel());

        for (Potion potion : potions) {
            potion.reset();
        }
        for (GameContainer container : containers) {
            container.reset();
        }
        for (Cannon cannon : cannons) {
            cannon.reset();
        }
        resetting = false;
        System.out.println("Size of arrays after: " + potions.size() + " - " + containers.size());
    }
}
