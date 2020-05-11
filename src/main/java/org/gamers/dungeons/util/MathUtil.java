package org.gamers.dungeons.util;

public class MathUtil {

    /**
     * Get a random value between two integers.
     *
     * @param min - the minimum value
     * @param max - the maximum value (exclusive)
     *
     * @return - a random value between these two values
     */
    public static int randomInt (int min, int max){
        return (int) Math.floor(Math.random() * (max-min)) + min;
    }

}
