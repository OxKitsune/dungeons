package org.gamers.dungeons.dungeon.parser;

import org.gamers.dungeons.dungeon.DungeonRoom;
import org.gamers.dungeons.util.Log;

import java.util.Arrays;

public abstract class SignParser {

    /* The id this sign parser is going to parse */
    private final String id;

    /**
     * Construct a new sign parser with the specified id.
     *
     * @param id - the id
     */
    public SignParser(String id) {
        this.id = id;
    }

    /**
     * Parse the sign data
     * @param lines - the lines of the sign
     * @param dungeonRoom - the dungeon room to parse this for
     */
    public boolean parseSign (String[] lines, DungeonRoom dungeonRoom){

        Arrays.stream(lines).forEach(line -> {
            Log.info(getClass().getName(), line);
        });

        if(lines[0] == null || !lines[0].equals("[DUNGEON]")) return false;

        Log.info(getClass().getName(), "Parsing sign data");

        // Parse the sign if it is possible to parse
        if(lines[1] != null && lines[1].equals(id)) return parseData(dungeonRoom, lines[2], lines[3]);

        // return false
        return false;
    }

    /**
     * Parse the sign data for the specified dungeon room.
     *
     * @param data - an array that contains the last two signs of the sign that's being parsed
     * @param dungeonRoom - the dungeon room this is being parsed for
     */
    public abstract boolean parseData(DungeonRoom dungeonRoom, String... data);

    /**
     * Get the id of this sign parser.
     *
     *  @return - the id of this sign parser
     */
    public String getId() {
        return id;
    }
}
