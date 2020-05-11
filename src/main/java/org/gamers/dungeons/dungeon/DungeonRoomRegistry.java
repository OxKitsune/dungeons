package org.gamers.dungeons.dungeon;

import org.bukkit.block.Sign;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.gamers.dungeons.dungeon.parser.SignParser;
import org.gamers.dungeons.dungeon.parser.impl.RoomIdParser;
import org.gamers.dungeons.schematic.Schematic;
import org.gamers.dungeons.schematic.blockentity.BlockEntity;
import org.gamers.dungeons.schematic.blockentity.impl.BlockEntitySign;
import org.gamers.dungeons.util.Log;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DungeonRoomRegistry {

    /* The plugin that instantiated this class */
    private final Plugin plugin;

    /* The singleton instance */
    private static DungeonRoomRegistry instance;

    /* A map of registered dungeon rooms */
    private Map<String, DungeonRoom> dungeonRooms;

    /* A list of signs parsers */
    private List<SignParser> signParsers;

    /**
     * Construct a new dungeon room registry.
     *
     * @param plugin - the plugin that's constructing this instance
     */
    public DungeonRoomRegistry(Plugin plugin) {
        this.plugin = plugin;

        this.dungeonRooms = new HashMap<>();

        this.signParsers = new ArrayList<>();
    }


    /**
     * Load the dungeon room types.
     *
     * @throws IOException = thrown whenever something goes wrong during the process
     */
    public void loadRooms () throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        // Check if the schematics directory exists
        File schematicsDirectory = new File(plugin.getDataFolder(), "schematics" + File.pathSeparator);

        // Make sure the schematic directory exists!
        if(!schematicsDirectory.exists() || !schematicsDirectory.isDirectory() || schematicsDirectory.listFiles().length < 1){
            schematicsDirectory.mkdir();
            Log.error("DungeonRoomRegistry", "Schematics directory is empty! Make sure that it contains schematics");
            return;
        }

        // Loop through all files in schematics directory
        for(File file : schematicsDirectory.listFiles()){

            // Make sure the file is a schematic
            if(!file.getName().endsWith(".schem")){
                Log.warn("DungeonRoomRegistry", file.getName() + " is not a schematic, but it's in the schematics directory!");
                continue;
            }

            // Load the schematic
            Schematic roomSchematic = new Schematic(file.getAbsolutePath());
            roomSchematic.parse();

            // Create DungeonRoom
            DungeonRoom room = new DungeonRoom(roomSchematic);

            // Parse the signs in the schematic
            List<Integer> blockEntitiesToRemove = new ArrayList<>();

            // Loop through schematic block entities to parse signs
            roomSchematic.getBlockEntityMap().forEach((index, blockEntity) -> {

                // Make sure block entity is block entity sign
                if(blockEntity instanceof BlockEntitySign){

                    // Parse the sign data
                    signParsers.forEach(signParser -> {
                        if(signParser.parseData(((BlockEntitySign)blockEntity).getLines(), room)) blockEntitiesToRemove.add(index);
                    });

                }
            });

            // Remove parsed signs from schematic
            blockEntitiesToRemove.forEach(index -> {
                roomSchematic.getBlockEntityMap().remove(index);
            });

            // Register the dungeon room
            dungeonRooms.put(room.getId(), room);
        }
    }

    /**
     * Register a new sign parser.
     *
     * @param newSignParser - the sign parser to add
     */
    public void  registerSignParser (SignParser newSignParser){

        // Make sure the sign parser is not registered already
        for(SignParser signParser : signParsers){
            if(newSignParser.getId().equals(signParser.getId())) throw new IllegalArgumentException("Sign parser with id \"" + newSignParser.getId() + "\" is already registered!");
        }

        // Add a new sign parser
        signParsers.add(newSignParser);
    }

    /**
     * Attempt to get a {@link DungeonRoom} for the specified id.
     *
     * @param id - the id of the dungeon room
     *
     * @return - an {@link Optional} describing the result of said attempt
     */
    public Optional<DungeonRoom> getDungeonRoom (String id){

        if(dungeonRooms.containsKey(id)) return Optional.of(dungeonRooms.get(id));

        return Optional.empty();

    }

    /**
     * Initialise the {@link DungeonRoomRegistry}.
     *
     * @param plugin - the plugin to register the dungeon room registry for.
     */
    public static void init (JavaPlugin plugin){

        if(instance != null) throw new IllegalStateException("DungeonRoomRegistry has already been initialised!");


        instance = new DungeonRoomRegistry(plugin);

        // Register sign parsers
        instance.registerSignParser(new RoomIdParser());

        try {
            instance.loadRooms();
        } catch (IOException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            Log.error("DungeonRoomRegistry", "Failed to load dungeon rooms: " + e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * Get the {@link DungeonRoomRegistry} instance.
     *
     * @return - the dungeon room registry instance
     */
    public static DungeonRoomRegistry getInstance() {
        if(instance == null) throw new IllegalStateException("DungeonRoomRegistry hasn't been initialised yet! Make sure to call DungeonRoomRegistry.init(Plugin)");

        return instance;

    }
}
