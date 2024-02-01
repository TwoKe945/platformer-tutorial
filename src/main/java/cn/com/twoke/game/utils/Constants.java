package cn.com.twoke.game.utils;

import cn.com.twoke.game.main.Game;

public interface Constants {


    boolean DEBUG_HIT_BOX = false;


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
        public static final int GROUND = 4;
        public static final int HIT = 5;
        public static final int ATTACK_1 = 6;
        public static final int ATTACK_JUMP_1 = 7;
        public static final int ATTACK_JUMP_2 = 8;

        public static int getSpriteAmount(int playAction) {
            switch (playAction) {
                case RUNNING:
                    return 6;
                case IDLE:
                    return 5;
                case HIT:
                    return 4;
                case JUMP:
                case ATTACK_1:
                case ATTACK_JUMP_1:
                case ATTACK_JUMP_2:
                    return 3;
                case GROUND:
                    return 2;
                case FALLING:
                default:
                    return 1;
            }
        }

    }

}
