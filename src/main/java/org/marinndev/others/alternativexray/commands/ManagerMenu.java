package org.marinndev.others.alternativexray.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.marinndev.others.alternativexray.Main;
import org.marinndev.others.alternativexray.SettingsManager;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ManagerMenu {

    private static HashMap<Inventory, ManagerMenu> inventories = new HashMap<Inventory,ManagerMenu>();
    SettingsManager files = SettingsManager.getFiles();
    Main plugin = Main.getPlugin(Main.class);
    Inventory managerInventory;
    String title;
    static ManagerMenu menus = new ManagerMenu();
    public static ManagerMenu getMenus(){
        return menus;
    }
    public ManagerMenu(){}

    public ManagerMenu(String title, Player admin){
        managerInventory = Bukkit.createInventory(null, 9, ChatColor.BLACK + title);
        addOption(managerInventory, Material.BLACK_STAINED_GLASS_PANE, " ", 0);
        addOption(managerInventory, Material.BLACK_STAINED_GLASS_PANE, " ", 1);
        addOption(managerInventory, Material.SPRUCE_SIGN, ChatColor.GOLD + "Change mode", 2);
        addOption(managerInventory, Material.BLACK_STAINED_GLASS_PANE, " ", 3);
        addOption(managerInventory, Material.COMMAND_BLOCK, ChatColor.GOLD + "General Settings", 4);
        addOption(managerInventory, Material.BLACK_STAINED_GLASS_PANE, " ", 5);
        addOption(managerInventory, Material.CHEST, ChatColor.GOLD + "Manage/Add a mode", 6);
        addOption(managerInventory, Material.BLACK_STAINED_GLASS_PANE, " ", 7);
        addOption(managerInventory, Material.BLACK_STAINED_GLASS_PANE, " ", 8);
        admin.openInventory(this.managerInventory);
        inventories.put(managerInventory,this);
    }
    public ManagerMenu fromInventory(Inventory inventory){
        return inventories.get(inventory);
    }

    public void addOption(Inventory inventory, Material type, String name, int slot){
        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        inventory.setItem(slot, item);
    }

    public void openChooseMode(Player admin){
        ArrayList<String> modes = getModes();
        int modeCount = modes.size();
        if(modeCount<=1){
            admin.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cThere are no other methods to choose from."));
        }
        int size = (modeCount/9+1)*9;
        Inventory inventory = Bukkit.createInventory(null,size,ChatColor.BLACK + "Choose a mode:");
        for(int i=0;i<=size-1;i++){
            if(modes.size()<=i){
                addOption(inventory, Material.BLACK_STAINED_GLASS_PANE, " ", i);
                continue;
            }else if(!(modes.get(i) == null)){
                if(plugin.getConfig().getString("mode").replaceAll(" ", "").equalsIgnoreCase(modes.get(i))){
                    addOption(inventory, Material.GOLD_BLOCK, ChatColor.GOLD + modes.get(i), i);
                }else{
                    addOption(inventory, Material.CHEST, ChatColor.DARK_AQUA + modes.get(i), i);
                }
            }
        }
        addOption(inventory, Material.LANTERN, "Back", size-1);
        admin.openInventory(inventory);
    }
    public void openExpiryMenu(Player admin, String mode){
        Inventory expiryWindow = Bukkit.createInventory(admin, 9,ChatColor.BLACK + "Expire (" + mode +") after: ");
        for(int i = 0; i<=8; i++){
            addOption(expiryWindow, Material.BLACK_STAINED_GLASS_PANE, " ", i);
        }
        addOption(expiryWindow, Material.CARTOGRAPHY_TABLE, ChatColor.GOLD + "Days: 0", 1);
        addOption(expiryWindow, Material.CARTOGRAPHY_TABLE, ChatColor.GOLD + "Hours: 0", 3);
        addOption(expiryWindow, Material.CARTOGRAPHY_TABLE, ChatColor.GOLD + "Minutes: 0", 5);
        addOption(expiryWindow, Material.FLETCHING_TABLE, ChatColor.GREEN + "Confirm", 7);
        admin.openInventory(expiryWindow);
    }
    public void openConfirmMenu(Player admin, String message){
        Inventory confirmMenu = Bukkit.createInventory(null, 9, message);
        for(int i = 0; i<=8; i++){
            addOption(confirmMenu, Material.BLACK_STAINED_GLASS_PANE, " ", i);
        }
        addOption(confirmMenu, Material.GREEN_STAINED_GLASS_PANE, ChatColor.GREEN + "CONFIRM", 5);
        addOption(confirmMenu, Material.RED_STAINED_GLASS_PANE, ChatColor.RED + "DENY", 3);
        admin.openInventory(confirmMenu);
    }

    public ArrayList<String> getModes(){
        ArrayList<String> modes = new ArrayList<String>();
        ArrayList<String> specialKeys = new ArrayList<String>();
        specialKeys.add("mode");
        specialKeys.add("mode-expire");
        specialKeys.add("global-multiplier");
        specialKeys.add("block-place-toggle");
        specialKeys.add("console-errors");
        specialKeys.add("worldguard-multipliers");
        specialKeys.add("ore-sizes-chances");
        specialKeys.add("activated-worlds");
        specialKeys.add("tnt-multiplier");
        for(String mode : plugin.getConfig().getKeys(false)){
            if(specialKeys.contains(mode)) continue;
            modes.add(mode);
        }
        return modes;
    }

    public void openSettings(Player admin){
        Inventory inventory = Bukkit.createInventory(null,9,ChatColor.BLACK + "Settings:");
        for(int i = 0;i<9;i++){
            addOption(inventory, Material.BLACK_STAINED_GLASS_PANE, " ", i);
        }
        addOption(inventory, Material.BELL, ChatColor.GOLD + "Toggles", 1);
        addOption(inventory, Material.ENDER_CHEST, ChatColor.GOLD + "Multipliers", 3);
        addOption(inventory, Material.GOLD_INGOT, ChatColor.GOLD + "Ore-size chances", 5);
        addOption(inventory, Material.LANTERN, ChatColor.GOLD + "Back", 8);
        admin.openInventory(inventory);
    }

    public void openToggles(Player admin){
        Inventory inventory = Bukkit.createInventory(null,9,ChatColor.BLACK + "Toggles:");
        for(int i = 0;i<9;i++){
            addOption(inventory, Material.BLACK_STAINED_GLASS_PANE, " ", i);
        }
        if(!plugin.getConfig().getBoolean("block-place-toggle") || !plugin.getConfig().contains("block-place-toggle")){
            addOption(inventory, Material.RED_CONCRETE, ChatColor.GOLD + "Block-place blocking", 1);
        }else {
            addOption(inventory, Material.LIME_CONCRETE, ChatColor.GOLD + "Block-place blocking", 1);
        }
        if(!plugin.getConfig().getBoolean("console-errors") || !plugin.getConfig().contains("console-errors")){
            addOption(inventory, Material.RED_CONCRETE, ChatColor.GOLD + "Console-errors", 3);
        }else {
            addOption(inventory, Material.LIME_CONCRETE, ChatColor.GOLD + "Console-errors", 3);
        }
        addOption(inventory, Material.WRITABLE_BOOK, ChatColor.GREEN + "Confirm Changes", 5);
        addOption(inventory, Material.LANTERN, ChatColor.YELLOW + "Back", 8);
        admin.openInventory(inventory);
    }
    public void openMultipliers(Player admin){
        Inventory inventory = Bukkit.createInventory(null,9,ChatColor.BLACK + "Multipliers:");
        for(int i = 0;i<9;i++){
            addOption(inventory, Material.BLACK_STAINED_GLASS_PANE, " ", i);
        }
        addOption(inventory, Material.TOTEM_OF_UNDYING, ChatColor.GOLD + "Global multiplier",1);
        addOption(inventory, Material.GOLDEN_AXE, ChatColor.GOLD + "Region multipliers",3);
        addOption(inventory, Material.TNT, ChatColor.GOLD + "TNT multiplier",5);
        addOption(inventory, Material.LANTERN, ChatColor.GOLD + "Back",8);
        admin.openInventory(inventory);
    }
    public void openGlobalMultiplier(Player admin){
        Inventory inventory = Bukkit.createInventory(null,9,ChatColor.BLACK + "Global-multiplier:");
        for(int i = 0;i<9;i++){
            addOption(inventory, Material.BLACK_STAINED_GLASS_PANE, " ", i);
        }
        double globalMultiplier = plugin.getConfig().getDouble("global-multiplier");
        double decimal = globalMultiplier%1;
        DecimalFormat df = new DecimalFormat("#.#");
        String decimalString = df.format(decimal);
        int ones = ((int) (globalMultiplier - decimal))%10;
        int tens = (((int) globalMultiplier)-ones)%100;

        addOption(inventory, Material.CARTOGRAPHY_TABLE, ChatColor.GOLD + "Tens: " + tens, 1);
        addOption(inventory, Material.CARTOGRAPHY_TABLE, ChatColor.GOLD + "Ones: " + ones, 3);
        addOption(inventory, Material.CARTOGRAPHY_TABLE, ChatColor.GOLD + "Decimal: " + decimalString, 5);
        addOption(inventory, Material.FLETCHING_TABLE, ChatColor.GREEN + "Confirm", 7);
        admin.openInventory(inventory);
    }
    public void openTntMultiplier(Player admin){
        Inventory inventory = Bukkit.createInventory(null,9,ChatColor.BLACK + "Tnt-multiplier:");
        for(int i = 0;i<9;i++){
            addOption(inventory, Material.BLACK_STAINED_GLASS_PANE, " ", i);
        }
        double globalMultiplier = plugin.getConfig().getDouble("tnt-multiplier");
        double decimal = globalMultiplier%1;
        DecimalFormat df = new DecimalFormat("#.#");
        String decimalString = df.format(decimal);
        int ones = ((int) (globalMultiplier - decimal))%10;
        int tens = (((int) globalMultiplier)-ones)%100;

        addOption(inventory, Material.CARTOGRAPHY_TABLE, ChatColor.GOLD + "Tens: " + tens, 1);
        addOption(inventory, Material.CARTOGRAPHY_TABLE, ChatColor.GOLD + "Ones: " + ones, 3);
        addOption(inventory, Material.CARTOGRAPHY_TABLE, ChatColor.GOLD + "Decimal: " + decimalString, 5);
        addOption(inventory, Material.FLETCHING_TABLE, ChatColor.GREEN + "Confirm", 7);
        admin.openInventory(inventory);
    }
    public void openSizeChances(Player admin){
        Inventory inventory = Bukkit.createInventory(null,27,ChatColor.BLACK + "Ore-size chances:");
        for(int i = 0;i<27;i++){
            addOption(inventory, Material.BLACK_STAINED_GLASS_PANE, " ", i);
        }
        addOption(inventory, Material.EMERALD_ORE, ChatColor.GOLD + "Emerald", 0);
        addOption(inventory, Material.DIAMOND_ORE, ChatColor.GOLD + "Diamond", 1);
        addOption(inventory, Material.GOLD_ORE, ChatColor.GOLD + "Gold", 2);
        addOption(inventory, Material.IRON_ORE, ChatColor.GOLD + "Iron", 3);
        addOption(inventory, Material.COAL_ORE, ChatColor.GOLD + "Coal", 4);
        addOption(inventory, Material.LAPIS_ORE, ChatColor.GOLD + "Lapis", 5);
        addOption(inventory, Material.REDSTONE_ORE, ChatColor.GOLD + "Redstone", 6);
        addOption(inventory, Material.NETHER_QUARTZ_ORE, ChatColor.GOLD + "Quartz", 7);
        addOption(inventory, Material.ANCIENT_DEBRIS, ChatColor.GOLD + "Netherite", 8);
        addOption(inventory, Material.NETHER_GOLD_ORE, ChatColor.GOLD + "Nether Gold", 9);
        addOption(inventory, Material.COPPER_ORE, ChatColor.GOLD + "Copper", 10);
        addOption(inventory, Material.DEEPSLATE_IRON_ORE, ChatColor.GOLD + "Deepslate Iron", 11);
        addOption(inventory, Material.DEEPSLATE_GOLD_ORE, ChatColor.GOLD + "Deepslate Gold", 12);
        addOption(inventory, Material.DEEPSLATE_DIAMOND_ORE, ChatColor.GOLD + "Deepslate Diamond", 13);
        addOption(inventory, Material.DEEPSLATE_LAPIS_ORE, ChatColor.GOLD + "Deepslate Lapis", 14);
        addOption(inventory, Material.DEEPSLATE_COAL_ORE, ChatColor.GOLD + "Deepslate Coal", 15);
        addOption(inventory, Material.DEEPSLATE_EMERALD_ORE, ChatColor.GOLD + "Deepslate Emerald", 16);
        addOption(inventory, Material.DEEPSLATE_COPPER_ORE, ChatColor.GOLD + "Deepslate Iron", 17);

        addOption(inventory, Material.LANTERN, ChatColor.GOLD + "Back", 26);
        admin.openInventory(inventory);
    }
    public void openSizeSelector(String ore, Material type, Player admin){
        Inventory inventory = Bukkit.createInventory(null,9,ChatColor.BLACK + "Select ore size:");
        for(int i = 0;i<9;i++){
            addOption(inventory, Material.BLACK_STAINED_GLASS_PANE, " ", i);
        }
        addOption(inventory, type, ChatColor.GOLD + "1", 0);
        addOption(inventory, type, ChatColor.GOLD + "2", 2);
        addOption(inventory, type, ChatColor.GOLD + "4", 4);
        addOption(inventory, type, ChatColor.GOLD + "8", 6);
        addOption(inventory, Material.LANTERN, ChatColor.GOLD + "Back", 8);
        admin.openInventory(inventory);
    }
    public void openChance(Player admin, String ore, int amount){
        Inventory inventory = Bukkit.createInventory(null,9,ChatColor.BLACK + "Set chance: (" + ore + ", " + amount + ")");
        for(int i = 0;i<9;i++){
            addOption(inventory, Material.BLACK_STAINED_GLASS_PANE, " ", i);
        }
        DecimalFormat df = new DecimalFormat("#.#");
        double decimal = plugin.getConfig().getDouble("ore-sizes-chances." + ore + "." + amount)%1;
        String decimalString = df.format(decimal);
        int ones = plugin.getConfig().getInt("ore-sizes-chances." + ore + "." + amount)%10;
        int tens = (plugin.getConfig().getInt("ore-sizes-chances." + ore + "." + amount)-ones)%100;
        addOption(inventory, Material.CARTOGRAPHY_TABLE, ChatColor.GOLD + "Tens: " + tens, 1);
        addOption(inventory, Material.CARTOGRAPHY_TABLE, ChatColor.GOLD + "Ones: " + ones, 3);
        addOption(inventory, Material.CARTOGRAPHY_TABLE, ChatColor.GOLD + "Decimal: "+decimalString, 5);
        addOption(inventory, Material.FLETCHING_TABLE, ChatColor.GREEN + "Confirm", 7);
        admin.openInventory(inventory);
    }
}
