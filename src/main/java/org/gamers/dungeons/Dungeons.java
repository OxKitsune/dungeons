package org.gamers.dungeons;

import org.bukkit.plugin.java.JavaPlugin;
import org.gamers.dungeons.command.GenerateCommand;
import org.gamers.dungeons.dungeon.DungeonGenerator;
import org.gamers.dungeons.dungeon.DungeonRoomRegistry;

public class Dungeons extends JavaPlugin {

    private static Dungeons instance;


    @Override
    public void onEnable() {

        // Set the plugin instance
        instance = this;

        // Initialise singletons
        DungeonRoomRegistry.init(this);
        DungeonGenerator.init(this);

        // Register commands
        getCommand("generate").setExecutor(new GenerateCommand());
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
