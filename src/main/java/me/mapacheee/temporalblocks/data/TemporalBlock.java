package me.mapacheee.temporalblocks.data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public class TemporalBlock {

    private final Location location;
    private final Material originalMaterial;
    private final BlockData originalBlockData;
    private final String playerName;
    private final long expireTime;

    public TemporalBlock(Location location, Material originalMaterial, 
                        BlockData originalBlockData, String playerName, long expireTime) {
        this.location = location;
        this.originalMaterial = originalMaterial;
        this.originalBlockData = originalBlockData;
        this.playerName = playerName;
        this.expireTime = expireTime;
    }

    public Location getLocation() {
        return location;
    }

    public Material getOriginalMaterial() {
        return originalMaterial;
    }

    public BlockData getOriginalBlockData() {
        return originalBlockData;
    }

    public String getPlayerName() {
        return playerName;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() >= expireTime;
    }
}
