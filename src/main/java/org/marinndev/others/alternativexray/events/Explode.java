package org.marinndev.others.alternativexray.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.marinndev.others.alternativexray.Main;

import java.util.HashMap;

public class Explode implements Listener {

    Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent e){
        if(!plugin.checkWorld(e.getBlock().getWorld().getName())){
            return;
        }
        Block block = e.getBlock();
        Material material = Material.STONE;
        if(block.getWorld().getEnvironment() == World.Environment.NETHER) {
            material = Material.NETHERRACK;
        }
        new BlockBreak().replaceBlocks(block, material,5);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e){
        if(!plugin.checkWorld(e.getLocation().getWorld().getName())){
            return;
        }
        Block block = e.getEntity().getLocation().getBlock();
        Material material = Material.STONE;
        if(block.getWorld().getEnvironment() == World.Environment.NETHER) {
            material = Material.NETHERRACK;
        }
        new BlockBreak().replaceBlocks(block, material,5);
        e.setCancelled(true);
        for(Block brokenBlock : e.blockList()){
            spawnBlocksAround(block,brokenBlock);
        }
        e.setCancelled(false);
    }
    private void spawnBlocksAround(Block explosive, Block block){
        if(!plugin.checkWorld(block.getWorld().getName())){
            return;
        }
        String biomeName = block.getBiome().name();
        biomeName = new BlockBreak().getClosestConfigBiome(new BlockBreak().determineBiome(biomeName));
        Location blockLocation = block.getLocation();
        int blockHeight = block.getY();
        Material replacingMaterial = Material.STONE;
        HashMap<String, Double> oreChances = new BlockBreak().getChanceOfOresInRange(blockHeight,biomeName);
        HashMap<String, Double> multipliedOreChances = new BlockBreak().applyMultipliers(oreChances, null, block.getLocation());
        if(plugin.getConfig().isSet("tnt-multiplier")){
            double tntmultiplier = 1;
            try{
                tntmultiplier = Double.parseDouble(plugin.getConfig().getString("tnt-multiplier"));
            }catch(NumberFormatException e){

            }catch(NullPointerException e){

            }
            if(tntmultiplier == 0){
                multipliedOreChances.put("nothing", 0.00 +Integer.MAX_VALUE);
            }else {
                multipliedOreChances.put("nothing", multipliedOreChances.get("nothing") / tntmultiplier);
            }
        }
        Material newOre = new BlockBreak().getRandomOre(multipliedOreChances,biomeName);
        float dir = (float)Math.toDegrees(Math.atan2(explosive.getLocation().getBlockX() - block.getX(), block.getZ() - explosive.getLocation().getBlockZ()));
        BlockFace face = new BlockBreak().getClosestFace(dir, explosive.getLocation().getPitch());
        Block relativeBlock = block.getRelative(face);
        new BlockBreak().spawnOres(new BlockBreak().generateOreSize(new BlockBreak().fromMaterialToString(newOre)), newOre, relativeBlock, face);
        if(!block.getMetadata("canBeReplaced").isEmpty()){// Checks if the block is one generated by the plugin.
            block.removeMetadata("canBeReplaced",plugin);
        }
    }
}
