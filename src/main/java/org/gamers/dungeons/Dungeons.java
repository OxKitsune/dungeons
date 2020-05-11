package org.gamers.dungeons;

import org.bukkit.plugin.java.JavaPlugin;

public class Dungeons extends JavaPlugin {

    private static Dungeons instance;


    @Override
    public void onEnable() {

        // Set the plugin instance
        instance = this;

    }


    /**
     * Get the plugin instance.
     *
     * @return - the plugin instance
     */
    public static Dungeons getInstance() {

        // Make sure plugin has been initialised.
        if(instance ==  null) throw new IllegalStateException("Plugin hasn't been initialised yet!");

        return instance;
    }
}
