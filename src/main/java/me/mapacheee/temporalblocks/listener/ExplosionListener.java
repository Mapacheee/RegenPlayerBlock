package me.mapacheee.temporalblocks.listener;

import com.thewinterframework.paper.listener.ListenerComponent;
import me.mapacheee.temporalblocks.service.ExplosionService;
import com.google.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

@ListenerComponent
public class ExplosionListener implements Listener {

    private final ExplosionService explosionService;

    @Inject
    public ExplosionListener(ExplosionService explosionService) {
        this.explosionService = explosionService;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        explosionService.registerExplosion(event.getLocation(), event.blockList());
    }
}
