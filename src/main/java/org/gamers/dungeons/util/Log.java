package org.gamers.dungeons.util;

import net.md_5.bungee.api.ChatColor;

public class Log {

    private static void message (ChatColor colour, String prefix, String message){
        System.out.println(colour + "[" + prefix + "] " + message);
    }

    /**
     * Log a message to the console
     * @param prefix - the prefix of the message
     * @param message - the message
     */
    public static void info (String prefix, String message){
        message(ChatColor.WHITE, prefix, message);
    }

    /**
     * Log a warning to the console
     * @param prefix - the prefix of the message
     * @param message - the message
     */
    public static void warn (String prefix, String message){
        message(ChatColor.YELLOW, prefix, message);
    }

    /**
     * Log an error to the console
     * @param prefix - the prefix of the message
     * @param message - the message
     */
    public static void error (String prefix, String message){
        message(ChatColor.RED, prefix, message);
    }

}
