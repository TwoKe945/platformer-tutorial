package cn.com.twoke.game.utils;

import cn.com.twoke.game.main.Game;

public interface Constants {

    float GRAVITY = 0.04f * Game.SCALE;
    float ANI_SPEED = (int)( 25 * Game.SCALE);

    boolean ENABLE_DEBUG_BOX = true;


    interface Environment {
        int BIG_CLOUD_WIDTH_DEFAULT = 448;
        int BIG_CLOUD_HEIGHT_DEFAULT = 101;
        int BIG_CLOUD_WIDTH = (int) (Game.SCALE * BIG_CLOUD_WIDTH_DEFAULT);
        int BIG_CLOUD_HEIGHT = (int) (Game.SCALE * BIG_CLOUD_HEIGHT_DEFAULT);
        int SMALL_CLOUD_WIDTH_DEFAULT = 74;
        int SMALL_CLOUD_HEIGHT_DEFAULT = 24;
        int SMALL_CLOUD_WIDTH = (int) (Game.SCALE * SMALL_CLOUD_WIDTH_DEFAULT);
        int SMALL_CLOUD_HEIGHT = (int) (Game.SCALE * SMALL_CLOUD_HEIGHT_DEFAULT);
    }

    interface UI {

        interface Buttons {
            int B_WIDTH_DEFAULT = 140;
            int B_HEIGHT_DEFAULT = 56;
            int B_WIDTH = (int)(B_WIDTH_DEFAULT * Game.SCALE);
            int B_HEIGHT = (int)(B_HEIGHT_DEFAULT * Game.SCALE);
        }

        interface PauseButtons {

            int SOUND_SIZE_DEFAULT = 42;
            int SOUND_SIZE = (int)(SOUND_SIZE_DEFAULT * Game.SCALE);


            int URM_SIZE_DEFAULT = 56;
            int URM_SIZE = (int)(URM_SIZE_DEFAULT * Game.SCALE);


            int VOLUME_DEFAULT_WIDTH = 28;
            int VOLUME_DEFAULT_HEIGHT = 44;
            int VOLUME_SLIDER_DEFAULT_WIDTH = 215;

            int VOLUME_HEIGHT = (int) (Game.SCALE * VOLUME_DEFAULT_HEIGHT);
            int VOLUME_WIDTH  =  (int) (Game.SCALE * VOLUME_DEFAULT_WIDTH);
            int VOLUME_SLIDER_WIDTH =  (int) (Game.SCALE * VOLUME_SLIDER_DEFAULT_WIDTH);
        }

    }
    interface Direction {
        int LEFT = 0;
        int UP = 1;
        int RIGHT = 2;
        int DOWN = 3;
    }

    class PlayerConstants {
       public static final int RUNNING = 1;
        public static final int IDLE = 0;
        public static final int JUMP = 2;
        public static final int FALLING = 3;
        public static final int ATTACK = 4;
        public static final int HIT = 5;
        public static final int DEAD = 6;



        public static int getSpriteAmount(int playAction) {
            switch (playAction) {
                case DEAD:
                    return 8;
                case RUNNING:
                    return 6;
                case IDLE:
                    return 5;
                case HIT:
                    return 4;
                case JUMP:
                case ATTACK:
                    return 3;
                case FALLING:
                default:
                    return 1;
            }
        }

    }

    class EnemyConstants {
        public static final int CRABBY = 0;

        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK= 2;
        public static final int HIT= 3;
        public static final int DEAD= 4;

        public static final int CRABBY_WIDTH_DEFAULT = 72;
        public static final int CRABBY_HEIGHT_DEFAULT = 32;

        public static final int CRABBY_WIDTH = (int)(Game.SCALE * CRABBY_WIDTH_DEFAULT);
        public static final int CRABBY_HEIGHT =(int)(Game.SCALE * CRABBY_HEIGHT_DEFAULT);

        public static final int CRABBY_DRAW_OFFSET_X = (int)(Game.SCALE * 26);
        public static final int CRABBY_DRAW_OFFSET_Y =  (int)(Game.SCALE * 9);

        public static int GetSpriteAmount(int enemyType, int enemyState) {
            switch (enemyType) {
                case CRABBY:
                    switch (enemyState) {
                        case IDLE:
                            return 9;
                        case RUNNING:
                            return 6;
                        case ATTACK:
                            return 7;
                        case HIT:
                            return 4;
                        case DEAD:
                            return 5;
                    }
                    break;
            }
            return 0;
        }

        public static int GetMaxHealth(int enemyType) {
            switch (enemyType) {
                case CRABBY:
                    return 10;
                default:
                    return 1;
            }
        }
        public static int GetEnemyDmg(int enemyType) {
            switch (enemyType) {
                case CRABBY:
                    return 15;
                default:
                    return 0;
            }
        }
    }
}
