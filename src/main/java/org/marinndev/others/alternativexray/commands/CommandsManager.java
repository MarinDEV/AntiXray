package org.marinndev.others.alternativexray.commands;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldguard.WorldGuard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.marinndev.others.alternativexray.Main;
import org.marinndev.others.alternativexray.SettingsManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CommandsManager implements CommandExecutor {

    SettingsManager files = SettingsManager.getFiles();
    Main plugin = Main.getPlugin(Main.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(command.getName().equalsIgnoreCase("axray")){
            if(args.length > 0){
                if(args[0].equalsIgnoreCase("reload")){
                    if(sender.isOp() || sender.hasPermission("axray.reload")) {
                        plugin.reloadConfig();
                        sender.sendMessage(ChatColor.GREEN + "Reloaded successfully!");
                    }else{
                        if(sender instanceof Player){
                            ((Player) sender).performCommand("help");
                        }
                    }
                    return true;
                }
            }
            if(!(sender instanceof Player)){// If console sends axray without arguments - cannot open GUI.
                sender.sendMessage("This command can only be used by players.");
                return false;
            }
            Player player = (Player) sender;
            if(args.length == 0){
                if(player.hasPermission("axray.admin.manage") || player.hasPermission("axray.admin.*")
                        || player.isOp()){
                    ManagerMenu managerMenu = new ManagerMenu("X-ray manager", player);
                }else{
                    player.performCommand("help");// Player performs /help if he doesnt have permission for /axray.
                }
                return false;
            }else if(args[0].equalsIgnoreCase("regions")){
                if(!player.hasPermission("axray.admin.regions") && !player.hasPermission("axray.admin.*")
                        && !player.isOp()) {
                    player.performCommand("help");
                    return false;
                }
                if(args.length == 2){
                    if(args[1].equalsIgnoreCase("list")){
                        String list = "";
                        for(String key : plugin.getConfig().getConfigurationSection("worldguard-multipliers").getKeys(false)){
                            list += key + " ";
                        }
                        player.sendMessage(ChatColor.DARK_GREEN + "Regions: " + ChatColor.WHITE + list);
                    }else{
                        sender.sendMessage(" ");
                        sender.sendMessage(ChatColor.YELLOW + "/axray regions (add/remove/list) [region] [multiplier]");
                        sender.sendMessage(" ");
                        return false;
                    }
                    return true;
                }
                if(args.length <= 2){
                    sender.sendMessage(" ");
                    sender.sendMessage(ChatColor.YELLOW + "/axray regions (add/remove/list) [region] [multiplier]");
                    sender.sendMessage(" ");
                    return false;
                }

                String commandWord = args[1];
                String region = args[2];
                boolean regioncheck = false;
                for(World world : Bukkit.getWorlds()) {
                    if(WorldGuard.getInstance().getPlatform().getRegionContainer().get(new BukkitWorld(world)).getRegions().containsKey(region)) {
                        regioncheck = true;
                    }
                }
                if(!regioncheck){
                    sender.sendMessage(ChatColor.RED + "Region doesn't exist!");
                    return false;
                }//TODO: CHECK IF WORLD IS ACTIVATED

                if(commandWord.equalsIgnoreCase("add")){
                    if(args.length <=3 ){
                        sender.sendMessage(" ");
                        sender.sendMessage(ChatColor.YELLOW + "/axray regions (add) [region] [multiplier]");
                        sender.sendMessage(" ");
                        return false;
                    }
                    String multiplier = args[3];
                    double multiplierdouble = 0.0;
                    try{
                        multiplierdouble = Double.parseDouble(multiplier);
                    }catch(NumberFormatException e){
                        sender.sendMessage(ChatColor.RED + "Multiplier has to be a number.");
                        return false;
                    }
                    plugin.getConfig().set("worldguard-multipliers." + region, Double.parseDouble(multiplier));
                    sender.sendMessage(ChatColor.GREEN + "Added " + region + "!");
                }else if(commandWord.equalsIgnoreCase("remove")){
                    if(!plugin.getConfig().isSet("worldguard-multipliers." + region)){
                        sender.sendMessage(ChatColor.RED + "This region is not on the config!");
                        return false;
                    }
                    plugin.getConfig().set("worldguard-multipliers." + region, null);
                    sender.sendMessage(ChatColor.GREEN + "Removed " + region + "!");
                }
                plugin.saveConfig();
            }else if(args[0].equalsIgnoreCase("create")){
                if(!player.hasPermission("axray.admin.createmode") && !player.hasPermission("axray.admin.*")
                        && !player.isOp()) {
                    player.performCommand("help");
                    return false;
                }
                if(args.length <=1){
                    sender.sendMessage(" ");
                    sender.sendMessage(ChatColor.YELLOW + "/axray create [modename]");
                    sender.sendMessage(" ");
                    return false;
                }
                String modename = args[1];
                if(plugin.getConfig().getKeys(false).contains(modename)){
                    sender.sendMessage(ChatColor.RED + "This mode already exists!");
                    return false;
                }
                plugin.getConfig().set(modename, "");
                ModeManagerMenu menu = new ModeManagerMenu("Manage a mode: ", (Player) sender);
                menu.openOptionsMenu(modename);

            }else if(args[0].equalsIgnoreCase("worlds")){
                if(!player.hasPermission("axray.admin.worlds") && !player.hasPermission("axray.admin.*")
                        && !player.isOp()) {
                    player.performCommand("help");
                    return false;
                }
                if(args.length == 1){
                    sender.sendMessage(" ");
                    sender.sendMessage(ChatColor.YELLOW + "/axray worlds (add/remove/list) [world]");
                    sender.sendMessage(" ");
                    return false;
                }
                if(args.length == 2){
                    if(args[1].equalsIgnoreCase("list")){
                        String list = "";
                        for(String key : plugin.getConfig().getStringList("activated-worlds")){
                            list += key + " ";
                        }
                        sender.sendMessage(list);
                    }else{
                        sender.sendMessage(" ");
                        sender.sendMessage(ChatColor.YELLOW + "/axray worlds (add/remove/list) [world]");
                        sender.sendMessage(" ");
                        return false;
                    }
                }else{
                    if(args[1].equalsIgnoreCase("list")) {
                        String list = "";
                        for (String key : plugin.getConfig().getConfigurationSection("activated-worlds").getKeys(false)) {
                            list += key + " ";
                        }
                        sender.sendMessage(list);
                    }
                    if(args[1].equalsIgnoreCase("add")){
                        String world = args[2];
                        if(Bukkit.getWorlds().contains(Bukkit.getWorld(world))){
                            ArrayList<String> current = (ArrayList<String>) plugin.getConfig().getStringList("activated-worlds");
                            if(!current.contains(world)){
                                current.add(world);
                                plugin.getConfig().set("activated-worlds", current);
                                plugin.saveConfig();
                                sender.sendMessage(ChatColor.GREEN + "Added: " + world);
                            }else{
                                sender.sendMessage(ChatColor.RED + "World already exists in the config.yml!");
                            }
                            return true;
                        }else{
                            sender.sendMessage(ChatColor.RED  + world + " doesn't exist!");
                            return false;
                        }
                    }
                    if(args[1].equalsIgnoreCase("remove")){
                        String world = args[2];
                        if(plugin.getConfig().getStringList("activated-worlds").contains(world)){
                            ArrayList<String> current = (ArrayList<String>) plugin.getConfig().getStringList("activated-worlds");
                            current.remove(world);
                            plugin.getConfig().set("activated-worlds", current);
                            plugin.saveConfig();
                            sender.sendMessage(ChatColor.RED + "Removed: " + world);
                            return true;
                        }else{
                            sender.sendMessage(ChatColor.RED  + world + " cannot be found in config.yml!");
                            return false;
                        }
                    }
                }
            }/*else if(args[0].equalsIgnoreCase("tnt-multiplier")){
                if(args.length == 1){
                    player.sendMessage("/axray tnt-multiplier [multiplier] - Sets the tnt multiplier.");
                    return false;
                }else {
                    double multiplier = 1;
                    try{
                        multiplier = Double.parseDouble(args[1]);
                    }catch(NumberFormatException e){
                        player.sendMessage(ChatColor.RED + "The multiplier needs to be a number!");
                        return false;
                    }
                    plugin.getConfig().set("tnt-multiplier", multiplier);
                    plugin.saveConfig();
                    player.sendMessage(ChatColor.GREEN + "tnt-multiplier set to: " + ChatColor.YELLOW + args[1]);
                }
            }*/else if(args[0].equalsIgnoreCase("mode")){
                if(!player.hasPermission("axray.admin.mode") && !player.hasPermission("axray.admin.*")
                        && !player.isOp()) {
                    player.performCommand("help");
                    return false;
                }
                if(args.length == 1){
                    player.sendMessage(" ");
                    player.sendMessage(ChatColor.YELLOW + "/axray mode [mode] broadcast-notice-[start/end] [message]");
                    player.sendMessage(" ");
                    return false;
                }else if(args.length >= 4){
                    String mode = args[1];
                    String commandWord = args[2];
                    String message = "";
                    for(int i = 3; i<args.length; i++){
                        message += args[i] + " ";
                    }
                    if(plugin.getConfig().getKeys(false).contains(mode)
                            && !mode.equalsIgnoreCase("mode") && !mode.equalsIgnoreCase("mode-expire")
                            && !mode.equalsIgnoreCase("global-multiplier") && !mode.equalsIgnoreCase("block-place-toggle")
                            && !mode.equalsIgnoreCase("console-errors") && !mode.equalsIgnoreCase("activated-worlds")
                            && !mode.equalsIgnoreCase("worldguard-multipliers") && !mode.equalsIgnoreCase("ore-size-chances")){

                        if(commandWord.equalsIgnoreCase("broadcast-notice-start")){
                            if(message == "none"){
                                message = " ";
                            }
                             plugin.getConfig().set(mode + "." + commandWord, message);
                             player.sendMessage(ChatColor.GREEN + "Set broadcast-notice-start to: " + ChatColor.YELLOW + message);
                        }else if(commandWord.equalsIgnoreCase("broadcast-notice-end")){
                            if(message == "none"){
                                message = " ";
                            }
                            plugin.getConfig().set(mode + "." + commandWord, message);
                            player.sendMessage(ChatColor.GREEN + "Set broadcast-notice-end to: " + ChatColor.YELLOW + message);
                        }else{
                            player.sendMessage(" ");
                            player.sendMessage(commandWord);
                            player.sendMessage(ChatColor.YELLOW + "/axray mode [mode] " + ChatColor.GOLD + "broadcast-notice-[start/end]" + ChatColor.YELLOW + " [message]");
                            player.sendMessage(" ");
                            return false;
                        }
                        plugin.saveConfig();
                    }else{
                        player.sendMessage(ChatColor.RED + "This mode desn't exist in the config file.");
                        return false;
                    }
                }else{
                    player.sendMessage(" ");
                    player.sendMessage(ChatColor.YELLOW + "/axray mode [mode] broadcast-notice-[start/end] [message]");
                    player.sendMessage(" ");
                    return false;
                }
            }else{
                if(!player.hasPermission("axray.admin.commands") && !player.hasPermission("axray.admin.*")
                        && !player.isOp()) {
                    player.performCommand("help");
                    return false;
                }
                player.sendMessage(ChatColor.BLACK + "=============" + ChatColor.YELLOW
                        + " Anti X-Ray Commands " + ChatColor.BLACK + "=============");
                player.sendMessage("/axray - Open the manage menu.");
                player.sendMessage("/axray create [modename] - Create a new mode.");
                player.sendMessage("/axray regions [add] [region-name] [multiplier] - Add a new region multiplier.");
                player.sendMessage("/axray regions [remove] [region-name] - Removes a region multiplier.");
                player.sendMessage("/axray regions [list] - List of all the region multipliers in the config.");
                player.sendMessage("/axray mode [mode] broadcast-notice-start [message] - Set a message that is broadcast when a mode starts.");
                player.sendMessage("/axray mode [mode] broadcast-notice-end [message] - Set a message that is broadcast when a mode ends.");
                player.sendMessage("/axray worlds [add] [world] - Adds a world that the plugin is activated in.");
                player.sendMessage("/axray worlds [remove] [world] - Removes a world that the plugin is activated in.");
                player.sendMessage("/axray worlds [list] - Lists the worlds that the plugin is activated in.");
            }
        }// End axray command
        return true;
    }
}
