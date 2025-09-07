package me.mapacheee.temporalblocks.listener;

import com.thewinterframework.paper.listener.ListenerComponent;
import me.mapacheee.temporalblocks.service.TemporalBlockService;
import com.google.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

@ListenerComponent
public class BlockListener implements Listener {

    private final TemporalBlockService temporalBlockService;

    @Inject
    public BlockListener(TemporalBlockService temporalBlockService) {
        this.temporalBlockService = temporalBlockService;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        temporalBlockService.addTemporalBlock(event.getBlock(), event.getPlayer());
    }
}
