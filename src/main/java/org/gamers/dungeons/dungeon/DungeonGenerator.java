package org.gamers.dungeons.dungeon;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.gamers.dungeons.util.Log;

import java.io.IOException;

public class DungeonGenerator {

    private final Plugin plugin;
    private static DungeonGenerator instance;


    /**
     * Construct a new dungeon room registry.
     *
     * @param plugin - the plugin that's constructing this instance
     */
    public DungeonGenerator(Plugin plugin) {
        this.plugin = plugin;
    }

    public void generateDungeon (Location location){

        // Paste the base room
        DungeonRoomRegistry.getInstance().getDungeonRoom("base_room").ifPresent(dungeonRoom -> {
            dungeonRoom.paste(location);
        });

    }

    /**
     * Initialise the {@link DungeonRoomRegistry}.
     *
     * @param plugin - the plugin to register the dungeon room registry for.
     */
    public static void init (JavaPlugin plugin){

        if(instance != null) throw new IllegalStateException("DungeonGenerator has already been initialised!");


        instance = new DungeonGenerator(plugin);
    }

    /**
     * Get the {@link DungeonRoomRegistry} instance.
     *
     * @return - the dungeon room registry instance
     */
    public static DungeonGenerator getInstance() {
        if(instance == null) throw new IllegalStateException("DungeonGenerator hasn't been initialised yet! Make sure to call DungeonRoomRegistry.init(Plugin)");

        return instance;

    }
}
