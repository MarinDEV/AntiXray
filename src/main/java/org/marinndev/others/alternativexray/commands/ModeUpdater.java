package org.marinndev.others.alternativexray.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.marinndev.others.alternativexray.Main;
import org.marinndev.others.alternativexray.SettingsManager;

import java.util.ArrayList;

public class ModeUpdater {

    private String mode;
    Main plugin = Main.getPlugin(Main.class);

    /**
     * Updates the "mode" variable in Main class
     * @param type
     * @param expiry
     * @param sender
     */
    public ModeUpdater(String type, String expiry, CommandSender sender){
        ArrayList<String> specialKeys = new ArrayList<String>();
        specialKeys.add("mode");
        specialKeys.add("mode-expire");
        specialKeys.add("global-multiplier");
        specialKeys.add("block-place-toggle");
        specialKeys.add("console-errors");
        specialKeys.add("worldguard-multipliers");
        specialKeys.add("ore-sizes-chances");
        mode = plugin.getConfig().getString("mode");

        boolean checkType = false;
        for(String key : plugin.getConfig().getKeys(false)){
            if(key.equalsIgnoreCase(type) && !specialKeys.contains(key)){
                checkType = true;
            }
        }
        if(!checkType){
            if(sender == null) {
                sender.sendMessage(("&c%mode%&e isn't specified in config.yml.").replaceAll("%mode%", type));
                return;
            }
        }
        if(mode.equalsIgnoreCase(type)){
            if(sender != null) {
                sender.sendMessage(("&cThis mode is already activated!").replaceAll("%mode%", type));
                return;
            }
        }
        plugin.getConfig().set("mode", type);
        plugin.saveConfig();
        mode = type;
        if(expiry == null){
            plugin.getConfig().set("mode-expire","none");
            plugin.saveConfig();
        }
        if(expiry != null){
            if(expiry.equalsIgnoreCase("none")){
                plugin.getConfig().set("mode-expire","none");
                plugin.saveConfig();
            }else {
                String[] timeLater = expiry.split(":");
                String expiryTime = plugin.getTime(Integer.parseInt(timeLater[0]), Integer.parseInt(timeLater[1]), Integer.parseInt(timeLater[2]));
                plugin.getConfig().set("mode-expire", expiryTime);
                plugin.saveConfig();
            }
        }
        if(plugin.getConfig().getString(type + ".broadcast-notice-start") != null
                && !plugin.getConfig().getString(type + ".broadcast-notice-start").isEmpty()){
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes
                    ('&', plugin.getConfig().getString(type + ".broadcast-notice-start")));
        }
    }
}
