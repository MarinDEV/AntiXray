package org.marinndev.others.alternativexray.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.marinndev.others.alternativexray.Main;

import java.util.ArrayList;

public class BlockPush implements Listener {

    @EventHandler
    public void onPush(BlockPistonExtendEvent e){
        for(Block block : e.getBlocks()){
            if(block.getType() == Material.STONE
                    || block.getType() == Material.NETHERRACK) {
                block.setMetadata("canBeReplaced", new FixedMetadataValue(Main.getProvidingPlugin(Main.class), false));
            }
        }
    }
    @EventHandler
    public void onRetract(BlockPistonRetractEvent e){
        for(Block block : e.getBlocks()){
            if(block.getType() == Material.STONE
                    || block.getType() == Material.NETHERRACK) {
                block.setMetadata("canBeReplaced", new FixedMetadataValue(Main.getProvidingPlugin(Main.class), false));
            }
        }
    }
}
