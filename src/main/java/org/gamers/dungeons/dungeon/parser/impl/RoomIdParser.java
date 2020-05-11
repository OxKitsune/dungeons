package org.gamers.dungeons.dungeon.parser.impl;

import org.gamers.dungeons.dungeon.DungeonRoom;
import org.gamers.dungeons.dungeon.parser.SignParseError;
import org.gamers.dungeons.dungeon.parser.SignParser;

public class RoomIdParser extends SignParser {
    /**
     * Construct a new sign parser with the specified id.
     */
    public RoomIdParser() {
        super("id");
    }



    @Override
    public boolean parseData(String[] data, DungeonRoom dungeonRoom) {

        if(data[0] == null) throw new SignParseError("Invalid room id for " + dungeonRoom.getId());

        dungeonRoom.setId(data[0]);
        return true;
    }
}