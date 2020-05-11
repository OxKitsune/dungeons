package org.gamers.dungeons.util;

import net.md_5.bungee.api.ChatColor;

public class Log {

    // ANSI escape code
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private static void message (String colour, String prefix, String message){
        System.out.println(colour + "[" + prefix + "] " + message);
    }

    /**
     * Log a message to the console
     * @param prefix - the prefix of the message
     * @param message - the message
     */
    public static void info (String prefix, String message){
        message(ANSI_WHITE, prefix, message);
    }

    /**
     * Log a warning to the console
     * @param prefix - the prefix of the message
     * @param message - the message
     */
    public static void warn (String prefix, String message){
        message(ANSI_YELLOW, prefix, message);
    }

    /**
     * Log an error to the console
     * @param prefix - the prefix of the message
     * @param message - the message
     */
    public static void error (String prefix, String message){
        message(ANSI_RED, prefix, message);
    }

}
