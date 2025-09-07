package me.mapacheee.temporalblocks;

import com.thewinterframework.paper.PaperWinterPlugin;
import com.thewinterframework.winter.annotation.WinterBootPlugin;


@WinterBootPlugin
public class TemporalBlocksPlugin extends PaperWinterPlugin {

    public void onEnable() {
        super.onEnable();
        getLogger().info("TemporalBlocks - Zazaland started!")
    }
}
