package me.mapacheee.temporalblocks.data;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.List;

public class ExplosionData {

    private final Location explosionLocation;
    private final List<Block> destroyedBlocks;
    private final long timestamp;

    public ExplosionData(Location explosionLocation, List<Block> destroyedBlocks) {
        this.explosionLocation = explosionLocation;
        this.destroyedBlocks = destroyedBlocks;
        this.timestamp = System.currentTimeMillis();
    }

    public Location getExplosionLocation() {
        return explosionLocation;
    }

    public List<Block> getDestroyedBlocks() {
        return destroyedBlocks;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
