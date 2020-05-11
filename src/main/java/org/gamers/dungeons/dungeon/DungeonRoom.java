package org.gamers.dungeons.dungeon;

import org.bukkit.Location;
import org.gamers.dungeons.schematic.Schematic;

public class DungeonRoom {

    /* The id of this dungeon room */
    private String id;

    /* The schematic of this dungeon room */
    private final Schematic schematic;

    public DungeonRoom(Schematic schematic) {
        this.schematic = schematic;
    }

    // Paste the dungeon at the target location
    public void paste (Location location){
        schematic.paste(location, true);
    }

    /**
     * Set the id of this room.
     *
     * @param id - the id of this room
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the id of this room
     * @return - the id of this room
     */
    public String getId() {
        return id;
    }

    /**
     * Get the schematic of this room
     *
     * @return - the schematic of this room
     */
    public Schematic getSchematic() {
        return schematic;
    }
}
