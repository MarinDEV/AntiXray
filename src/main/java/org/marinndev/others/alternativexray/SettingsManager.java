package org.marinndev.others.alternativexray;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingsManager {

    Plugin plugin;
    static SettingsManager files = new SettingsManager();

    public static SettingsManager getFiles(){
        return files;
    }

    FileConfiguration errorLogger;
    FileConfiguration protectedblocks;
    File lfile;
    File blockfile;

    //Sets up the files
    public void setup(Plugin plugin){
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        plugin.saveResource("config.yml",false);
        lfile = new File(plugin.getDataFolder(), "errors.yml");
        plugin.saveResource("errors.yml",false);
        errorLogger = YamlConfiguration.loadConfiguration(lfile);
    }// End setup

    public void logError(String errorMessage){
        errorLogger.set(getCurrentTime(),errorMessage);
        saveErrors();
    }// End logError

    private String getCurrentTime(){
        SimpleDateFormat formatter= new SimpleDateFormat("dd/MM/yyyy 'at' hh:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }// End getCurrentTime

    public FileConfiguration getProtectedBlocks(){
        return protectedblocks;
    }// End getProtectedBlocks

    public void saveProtectedBlocks() {
        try {
            protectedblocks.save(blockfile);
        }
        catch (IOException e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save protectedblocks.yml!");
        }
    }// End saveProtectedBlocks

    public void saveErrors(){
        try {
            errorLogger.save(lfile);
        }
        catch (IOException e) {
            Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save errors.yml!");
        }
    }// End saveErrors
}
