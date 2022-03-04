package org.marinndev.others.alternativexray.events;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import dev.espi.protectionstones.*;
import dev.espi.protectionstones.event.PSCreateEvent;
import dev.espi.protectionstones.event.PSRemoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.marinndev.others.alternativexray.Main;
import org.marinndev.others.alternativexray.SettingsManager;

import java.util.ArrayList;

public class BlockPlace implements Listener {

    Main plugin = Main.getPlugin(Main.class);
    BlockFace[] blockFaces = new BlockFace[] {BlockFace.UP, BlockFace.DOWN, BlockFace.EAST,
            BlockFace.WEST, BlockFace.SOUTH, BlockFace.NORTH};
    SettingsManager files = SettingsManager.getFiles();

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        if(!plugin.checkWorld(e.getBlock().getWorld().getName())){
            return;
        }
        if(!plugin.getConfig().getBoolean("block-place-toggle")) return;
        ArrayList<Block> oresAgainst = new ArrayList<Block>();
        Block block = e.getBlock();
        if(!block.getType().isOccluding()) return;
        block.setMetadata("canBeReplaced",new FixedMetadataValue(plugin,false));
        for(BlockFace face : blockFaces){
            Block relativeBlock = block.getRelative(face);
            if(!isOre(relativeBlock)) continue;
            oresAgainst.add(relativeBlock);
        }
        for(Block ore : oresAgainst){
            if(isCompletelyBlocked(ore)) {
                Permission perm = new Permission("axray.block-ores", PermissionDefault.FALSE);
                if(e.getPlayer().hasPermission(perm)){
                    return;
                }
                e.setCancelled(true);
                e.getPlayer().sendMessage("You cannot completely block off ores!");
                return;
            }
        }
    }
    /*@EventHandler
    public void onForm(EntityChangeBlockEvent e){
        if(!plugin.checkWorld(e.getBlock().getWorld().getName())){
            return;
        }
        Block block = e.getBlock();
        if(!plugin.getConfig().getBoolean("block-place-toggle")) return;
        ArrayList<Block> oresAgainst = new ArrayList<Block>();
        for(BlockFace face : blockFaces){
            Block relativeBlock = block.getRelative(face);
            if(!isOre(relativeBlock)) continue;
            oresAgainst.add(relativeBlock);
        }
        for(Block ore : oresAgainst){
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    if(isCompletelyBlocked(ore)) {
                        e.getBlock().breakNaturally();
                        //e.getPlayer().sendMessage("You cannot completely block off ores!");
                        return;
                    }
                }
            },1);
        }
    }*/
    @EventHandler
    public void onPSCreate(PSCreateEvent e){
        e.getRegion().getProtectBlock().setMetadata("isProtected", new FixedMetadataValue(plugin,true));
    }
    @EventHandler
    public void onPSRemove(PSRemoveEvent e){
        /*
        Location location = e.getRegion().getProtectBlock().getLocation();
        String locationString = location.getWorld().getName() + ":" + location.getBlockX() + ":"
                + location.getBlockY() + ":" + location.getBlockZ();
        ArrayList<String> blockLocations = (ArrayList<String>) files.getProtectedBlocks().getStringList("protected-blocks");
        if(blockLocations.contains(locationString)){
            blockLocations.remove(locationString);
            files.getProtectedBlocks().set("protected-blocks", blockLocations);
            files.saveProtectedBlocks();
        }*/
    }
    public void protectBlocks(World world){
        if(!plugin.checkWorld(world.getName())){
            return;
        }
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(new BukkitWorld(world));
        for(String regionName : regions.getRegions().keySet()){
            ProtectedRegion region = regions.getRegions().get(regionName);
            if(region.getFlags().containsKey(FlagHandler.PS_MERGED_REGIONS)) {
                Object s = region.getFlag(FlagHandler.PS_MERGED_REGIONS);
                String mergedRegions = s.toString();
                mergedRegions = mergedRegions.replace("[","");
                mergedRegions = mergedRegions.replace("ps","");
                mergedRegions = mergedRegions.replace("]","");
                mergedRegions = mergedRegions.replaceAll("x",":");
                mergedRegions = mergedRegions.replaceAll("y",":");
                mergedRegions = mergedRegions.replaceAll("z",":");
                String[] blockArray = mergedRegions.split(",");
                actOnPSBlock(blockArray,world);
            }else if(region.getId().startsWith("ps") && region.getId().toLowerCase().contains("x")
                            && region.getId().toLowerCase().contains("y")
                            && region.getId().toLowerCase().contains("z")){
                String name = region.getId();
                name = name.replace("ps","");
                name = name.replaceAll("x",":");
                name = name.replaceAll("y",":");
                name = name.replaceAll("z",":");
                String[] blockArray = new String[]{name};
                actOnPSBlock(blockArray, world);
            }
        }
    }
    private boolean isOre(Block block){
        Material type = block.getType();
        if(type.toString().contains("ORE")) return true;
        else if(type.toString().equalsIgnoreCase("ANCIENT_DEBRIS")) return true;
        return false;
    }
    private boolean isCompletelyBlocked(Block block){
        for(BlockFace face : blockFaces){
            Block relativeBlock = block.getRelative(face);
            if(!relativeBlock.getType().isOccluding()) return false;
        }
        return true;
    }
    private void actOnPSBlock(String[] blockArray, World world){
        for(int i = 0; i<=blockArray.length-1; i++){
            String[] splitCoordinates = blockArray[i].split(":");
            int x = Integer.parseInt(splitCoordinates[0].replace(":", "").replace(" ", ""));
            int y = Integer.parseInt(splitCoordinates[1].replace(":", ""));
            int z = Integer.parseInt(splitCoordinates[2].replace(":", ""));
            Location blockLocation = new Location(world,x,y,z);
            blockLocation.getBlock().setMetadata("isProtected", new FixedMetadataValue(plugin,true));
        }
    }
}
