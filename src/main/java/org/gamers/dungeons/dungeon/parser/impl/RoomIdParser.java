package org.gamers.dungeons.dungeon.parser.impl;

import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.gamers.dungeons.dungeon.DungeonRoom;
import org.gamers.dungeons.dungeon.parser.SignParseError;
import org.gamers.dungeons.dungeon.parser.SignParser;

import java.util.Arrays;

public class RoomIdParser extends SignParser {
    /**
     * Construct a new sign parser with the specified id.
     */
    public RoomIdParser() {
        super("id");
    }



    @Override
    public boolean parseData(DungeonRoom dungeonRoom, String... data) {

        if(data[0] == null) throw new SignParseError("Invalid room id for " + dungeonRoom.getId());

        Arrays.stream(data).forEach(line -> {
            Log.info(getClass().getName() + "data: " + line);
        });

        dungeonRoom.setId(data[0]);

        Log.info("RoomIdParser", "Room Id: " + data[0]);
        return true;
    }
}
