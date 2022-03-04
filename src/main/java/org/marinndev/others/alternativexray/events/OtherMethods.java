package org.marinndev.others.alternativexray.events;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class OtherMethods {

    public boolean checkRegionClaim(Location location){ // Gets the applicable WorldGuard regions.
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(new BukkitWorld(location.getWorld()));
        ApplicableRegionSet regionsSet = regions.getApplicableRegions(BlockVector3.at(location.getX(),location.getY(),location.getZ()));
        for (Iterator<ProtectedRegion> it = regionsSet.iterator(); it.hasNext(); ) {
            ProtectedRegion region = it.next();
            if (region.getId().startsWith("ps-"))
                return true;
        }
        return false;
    }
}
