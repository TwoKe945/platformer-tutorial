package cn.com.twoke.game.utils;

import cn.com.twoke.game.main.Game;

public interface Constants {


    boolean DEBUG_HIT_BOX = false;


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
