package org.gamers.dungeons.dungeon.parser;

import org.gamers.dungeons.dungeon.DungeonRoom;

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
        if(lines[0] != null || !lines[0].equals("[DUNGEON]")) return false;

        // Copy over the sign data.
        String[] signData = new String[2];
        System.arraycopy(lines, 2, lines, 0, 2);

        // Parse the sign if it is possible to parse
        if(lines[1] != null || lines[1].equals(id)) return parseData(signData, dungeonRoom);

        // return false
        return false;
    }

    /**
     * Parse the sign data for the specified dungeon room.
     *
     * @param data - an array that contains the last two signs of the sign that's being parsed
     * @param dungeonRoom - the dungeon room this is being parsed for
     */
    public abstract boolean parseData (String[] data, DungeonRoom dungeonRoom);

    /**
     * Get the id of this sign parser.
     *
     *  @return - the id of this sign parser
     */
    public String getId() {
        return id;
    }
}
