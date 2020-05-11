package org.gamers.dungeons.schematic;

import net.minecraft.server.v1_15_R1.NBTBase;
import net.minecraft.server.v1_15_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.NBTTagList;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.gamers.dungeons.schematic.blockentity.BlockEntity;
import org.gamers.dungeons.schematic.blockentity.BlockEntityType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Class used to parse schematics
 */
public class Schematic {

    /* The file extension of the schematic, since spigot 1.13 use .schem */
    private static final String FORMAT = ".schem";

    /* The absolute path to the schematic */
    private final String absolutePath;

    /* The dimensions of the schematic */
    private short width, height, length;

    /* The block data palette of the schematic */
    private final Map<Integer, BlockData> blockPalette;

    /* The block data array of the schematic */
    private byte[] blockDataArray;

    /* The block entity map of the schematic */
    private final Map<Integer, BlockEntity> blockEntityMap;

    /**
     * Construct a new Schematic.
     * <p>
     *     Note: This does not parse the schematic yet, to parse the schematic use {@link Schematic#parse}.
     * </p>
     *
     * @param absolutePath the absolute path to the schematic
     */
    public Schematic (String absolutePath) {

        // Make sure the path ends with the file format!
        if(!absolutePath.endsWith(FORMAT)) throw new UnsupportedOperationException("Schematic format not supported! Please make sure the file is a schematic!");

        this.absolutePath = absolutePath;
        this.blockPalette = new HashMap<>();
        this.blockEntityMap = new HashMap<>();
    }

    /**
     * Parse the schematic.
     *
     * @throws IOException thrown if the schematic file doesn't exist or if it's not in the proper nbt format!
     */
    public void parse () throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        // Create input stream using absolute path
        InputStream inputStream = new FileInputStream(new File(absolutePath));

        // Parse the nbt data using minecraft's NBT library
        NBTTagCompound nbtData = NBTCompressedStreamTools.a(inputStream);

        // Get the dimensions of the schematic
        width = nbtData.getShort("Width");
        height = nbtData.getShort("Height");
        length = nbtData.getShort("Length");

        // Get the block data as byte array
        blockDataArray = nbtData.getByteArray("BlockData");

        // Get the block palette, this contains all the possible block states in this schematic
        // These block states are stored as a string
        // Example: minecraft:oak_fence[east=false,north=true,south=false,waterlogged=false,west=false]
        NBTTagCompound palette = nbtData.getCompound("Palette");

        // Get all the tile entities in this schematic
        NBTTagList blockEntities = (NBTTagList) nbtData.get("BlockEntities");

        // Make sure the schematic contains block entities
        if(blockEntities != null){

            // Loop through all block entities of the schematic
            for (NBTBase nbtBase : blockEntities) {

                // Get the block entity nbt data
                NBTTagCompound blockEntityNBTData = (NBTTagCompound) nbtBase;

                // Get the block entity type of the block entity
                BlockEntityType blockEntityType = BlockEntityType.fromId(blockEntityNBTData.getString("Id"));

                // Make sure the block entity type is implemented
                if (blockEntityType == null) throw new NotImplementedException(blockEntityNBTData.getString("Id") + " is not an implemented block entity type!");

                // Get the block entity constructor and create a new block entity
                Constructor<? extends BlockEntity> constructor = blockEntityType.getType().getConstructor(NBTTagCompound.class);
                BlockEntity blockEntity = constructor.newInstance(blockEntityNBTData);

                // The index of the block at X, Y, Z is (Y * length + Z) * width + X.
                int index = (blockEntity.getPosition().getBlockY() * length + blockEntity.getPosition().getBlockZ()) * width + blockEntity.getPosition().getBlockX();

                // Add the block entity to the block entity list
                blockEntityMap.put(index, blockEntity);
            }
        }

        // Loop through all the block states in the palette
        palette.getKeys().forEach(state -> {

            // Get the index of the block state
            int index = palette.getInt(state);

            // Construct the block data based of the block state and add it to the block data map
            BlockData blockData = Bukkit.createBlockData(state);
            blockPalette.put(index, blockData);
        });

        // Close the input stream
        inputStream.close();
    }

    /**
     * Paste the schematic at the target location.
     *
     * @param targetLocation - the target location
     * @param ignoreAirBlocks - whether to ignore air blocks or not
     */
    public void paste (Location targetLocation, boolean ignoreAirBlocks){

        // Coordinates in schematics range from (0, 0, 0) to (Width - 1, Height -1, Length - 1)
        for(int x = 0; x < width; ++x){
            for(int y = 0; y < height; ++y){
                for(int z = 0; z < length; ++z){

                    // The index of the block at X, Y, Z is (Y * length + Z) * width + X.
                    int index = (y * length + z) * width + x;

                    // Get the block data from the palette
                    BlockData blockData = blockPalette.get((int) blockDataArray[index]);

                    // Make sure block data isn't null
                    if(blockData == null){
                        Log.error("Schematic", "BlockData is null for index: " + index);
                        continue;
                    }

                    // Ignore air blocks
                    if(ignoreAirBlocks && blockData.getMaterial() == Material.AIR) continue;

                    // Set the block data using the block's state
                    BlockState blockState = targetLocation.clone().add(x, y, z).getBlock().getState();
                    blockState.setBlockData(blockData);

                    // Force the update but don't update physics to make sure the blocks get pasted properly.
                    // If applyPhysics is set to true, portal blocks won't get placed and sand blocks will fall etc.
                    blockState.update(true, false);

                    // Apply block entity data
                    if(blockEntityMap.get(index) != null){
                        blockEntityMap.get(index).applyData(targetLocation);
                    }
                }
            }
        }
    }

    /**
     * Get the width of this schematic.
     *
     * @return - the width of this schematic
     */
    public short getWidth() {
        return width;
    }

    /**
     * Get the height of this schematic.
     *
     * @return - the height of this schematic
     */
    public short getHeight() {
        return height;
    }
    /**
     * Get the length of this schematic.
     *
     * @return - the length of this schematic
     */
    public short getLength() {
        return length;
    }

    /**
     * Get the size of the the schematic.
     * <p>
     *     Note: Size = width * height * length
     * </p>
     *
     * @return - the size of this schematic
     */
    public int getSize () {
        return width * height * length;
    }

    /**
     * Get the block palette for this schematic.
     *
     * @return - the block palette
     */
    public Map<Integer, BlockData> getBlockPalette() {
        return blockPalette;
    }

    /**
     * Get the block data array for this schematic.
     *
     * @return - the block data array
     */
    public byte[] getBlockDataArray() {
        return blockDataArray;
    }

    /**
     * Get the block entity map for this schematic.
     *
     * @return - the block entity map
     */
    public Map<Integer, BlockEntity> getBlockEntityMap() {
        return blockEntityMap;
    }
}