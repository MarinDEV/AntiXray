package org.marinndev.others.alternativexray.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.marinndev.others.alternativexray.Main;
import org.marinndev.others.alternativexray.SettingsManager;

import java.util.ArrayList;
import java.util.HashMap;

public class ModeManagerMenu{

    Main plugin = Main.getPlugin(Main.class);
    Inventory managerInventory;
    public String biome = "";
    public String mode = "";
    Player admin = null;
    public String option = ""; // Ore Layers or Ore Chances
    public Material type;

    public static HashMap<Inventory, ModeManagerMenu> inventories = new HashMap<Inventory, ModeManagerMenu>();
    static ModeManagerMenu menus = new ModeManagerMenu();
    public static ModeManagerMenu getMenus(){
        return menus;
    }
    public ModeManagerMenu(){}

    public ModeManagerMenu(String title, Player admin){
        inventories.put(managerInventory, this);
        this.admin = admin;
        ArrayList<String> modes = ManagerMenu.getMenus().getModes();
        int modeCount = modes.size();
        if(modeCount<=1){
            admin.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&cThere are no other methods to choose from."));
        }
        int size = ((modeCount+2)/9 + 1)*9;
        Inventory inventory = Bukkit.createInventory(null,size,ChatColor.BLACK + title);
        for(int i=0;i<=size-1;i++){
            if(modes.size()<=i){
                addOption(inventory, Material.BLACK_STAINED_GLASS_PANE, " ", i);
                continue;
            }else if(!(modes.get(i) == null)){
                addOption(inventory, Material.CHEST, ChatColor.GOLD + modes.get(i), i);
            }
        }
        addOption(inventory, Material.BELL, ChatColor.GOLD + "Add a new mode", modes.size());
        addOption(inventory, Material.LANTERN, ChatColor.GOLD + "Back", size-1);
        admin.openInventory(inventory);
    }
    public void addOption(Inventory inventory, Material type, String name, int slot){
        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        inventory.setItem(slot, item);
    }

    public void openOptionsMenu(String mode){
        this.mode = mode;
        managerInventory = Bukkit.createInventory(null,9,ChatColor.BLACK + "Manage: " + this.mode);
        for(int i = 0;i<9;i++){
            addOption(managerInventory, Material.BLACK_STAINED_GLASS_PANE, " ", i);
        }
        addOption(managerInventory, Material.PAPER, ChatColor.GOLD + "Broadcast-start", 3);
        addOption(managerInventory, Material.PAPER, ChatColor.GOLD + "Broadcast-end", 5);
        addOption(managerInventory, Material.BOOK, ChatColor.GOLD + "Biomes", 1);
        addOption(managerInventory, Material.LANTERN, ChatColor.GOLD + "Back", 8);
        admin.openInventory(managerInventory);
        inventories.put(managerInventory, this);
    }
    public void openBiomesMenu(){
        managerInventory = Bukkit.createInventory(null,18,ChatColor.BLACK + "Choose biome: ("+this.mode+")");
        for(int i = 0;i<9;i++){
            addOption(managerInventory, Material.BLACK_STAINED_GLASS_PANE, " ", i);
        }
        addOption(managerInventory, Material.BOOK,"default",0);
        addOption(managerInventory, Material.BOOK,"nether",1);
        addOption(managerInventory, Material.BOOK,"ocean",2);
        addOption(managerInventory, Material.BOOK,"forest",3);
        addOption(managerInventory, Material.BOOK,"forest_hills",4);
        addOption(managerInventory, Material.BOOK,"jungle",5);
        addOption(managerInventory, Material.BOOK,"jungle_hills",6);
        addOption(managerInventory, Material.BOOK,"taiga",7);
        addOption(managerInventory, Material.BOOK,"taiga_hills",8);
        addOption(managerInventory, Material.BOOK,"swamp",9);
        addOption(managerInventory, Material.BOOK,"swamp_hills",10);
        addOption(managerInventory, Material.BOOK,"plains",11);
        addOption(managerInventory, Material.BOOK,"savanna",12);
        addOption(managerInventory, Material.BOOK,"savanna_plateau",13);
        addOption(managerInventory, Material.BOOK,"desert",14);
        addOption(managerInventory, Material.BOOK,"desert_hills",15);
        addOption(managerInventory, Material.BOOK,"mountains",16);
        addOption(managerInventory, Material.LANTERN,"Back",17);
        admin.openInventory(managerInventory);
        inventories.put(managerInventory, this);
    }
    public void openBiomeOptionsMenu(String biome){
        this.biome = biome;
        managerInventory = Bukkit.createInventory(null,9,ChatColor.BLACK + "Choose option: ( ... , "+this.biome+")");
        for(int i = 0;i<9;i++){
            addOption(managerInventory, Material.BLACK_STAINED_GLASS_PANE, " ", i);
        }
        addOption(managerInventory, Material.WRITABLE_BOOK,"Ore layers",1);
        addOption(managerInventory, Material.WRITABLE_BOOK,"Ore Chances",3);
        addOption(managerInventory, Material.LANTERN,"Back",8);
        admin.openInventory(managerInventory);
        inventories.put(managerInventory, this);
    }
    public void openOres(String option){ // Either orelayers || orechances
        this.option =option;
        managerInventory = Bukkit.createInventory(null,27,ChatColor.BLACK + "Choose ore: ( ... , "+option+")");
        for(int i = 0;i<27;i++){
            addOption(managerInventory, Material.BLACK_STAINED_GLASS_PANE, " ", i);
        }
        addOption(managerInventory, Material.EMERALD_ORE,ChatColor.GOLD + "Emerald",0);
        addOption(managerInventory, Material.DIAMOND_ORE,ChatColor.GOLD + "Diamond",1);
        addOption(managerInventory, Material.GOLD_ORE,ChatColor.GOLD + "Gold",2);
        addOption(managerInventory, Material.IRON_ORE,ChatColor.GOLD + "Iron",3);
        addOption(managerInventory, Material.COAL_ORE,ChatColor.GOLD + "Coal",4);
        addOption(managerInventory, Material.LAPIS_ORE,ChatColor.GOLD + "Lapis_lazuli",5);
        addOption(managerInventory, Material.REDSTONE_ORE,ChatColor.GOLD + "Redstone",6);
        addOption(managerInventory, Material.NETHER_QUARTZ_ORE,ChatColor.GOLD + "Quartz",7);
        addOption(managerInventory, Material.ANCIENT_DEBRIS,ChatColor.GOLD + "Netherite",8);
        addOption(managerInventory, Material.NETHER_GOLD_ORE,ChatColor.GOLD + "Nether Gold",9);
        addOption(managerInventory, Material.COPPER_ORE, ChatColor.GOLD + "Copper", 10);
        addOption(managerInventory, Material.DEEPSLATE_IRON_ORE, ChatColor.GOLD + "Deepslate Iron", 11);
        addOption(managerInventory, Material.DEEPSLATE_GOLD_ORE, ChatColor.GOLD + "Deepslate Gold", 12);
        addOption(managerInventory, Material.DEEPSLATE_DIAMOND_ORE, ChatColor.GOLD + "Deepslate Diamond", 13);
        addOption(managerInventory, Material.DEEPSLATE_LAPIS_ORE, ChatColor.GOLD + "Deepslate Lapis", 14);
        addOption(managerInventory, Material.DEEPSLATE_COAL_ORE, ChatColor.GOLD + "Deepslate Coal", 15);
        addOption(managerInventory, Material.DEEPSLATE_EMERALD_ORE, ChatColor.GOLD + "Deepslate Emerald", 16);
        addOption(managerInventory, Material.DEEPSLATE_COPPER_ORE, ChatColor.GOLD + "Deepslate Iron", 17);
        addOption(managerInventory, Material.LANTERN,ChatColor.GOLD + "Back",26);
        admin.openInventory(managerInventory);
        inventories.put(managerInventory, this);
    }
    public void openLayerHigh(Material type){ // Either orelayers || orechances
        this.type = type;
        managerInventory = Bukkit.createInventory(null,9,ChatColor.BLACK + "HighY Layer: ( ... , "+getOreName(this.type)+")");
        for(int i = 0;i<9;i++){
            addOption(managerInventory, Material.BLACK_STAINED_GLASS_PANE, " ", i);
        }
        String highY;
        if(plugin.getConfig().isSet(this.mode + "." + this.biome + ".orelayers." + getOreName(this.type) + ".highY")){
            highY = plugin.getConfig().getString(this.mode + "." + this.biome + ".orelayers." + getOreName(this.type) + ".highY");
        }else{
            highY = "0-0";
        }
        addOption(managerInventory, Material.RED_STAINED_GLASS,ChatColor.GOLD + "Remove 1",3);
        addOption(managerInventory, Material.RED_STAINED_GLASS,ChatColor.GOLD + "Remove 10",2);
        addOption(managerInventory, Material.RED_STAINED_GLASS,ChatColor.GOLD + "Remove 100",1);
        addOption(managerInventory, Material.NETHER_STAR,ChatColor.GOLD + "HighY: " + highY,4);
        addOption(managerInventory, Material.GREEN_STAINED_GLASS,ChatColor.GOLD + "Add 1",5);
        addOption(managerInventory, Material.GREEN_STAINED_GLASS,ChatColor.GOLD + "Add 10",6);
        addOption(managerInventory, Material.GREEN_STAINED_GLASS,ChatColor.GOLD + "Add 100",7);
        admin.openInventory(managerInventory);
        inventories.put(managerInventory, this);
    }
    public void openLayerLow(Material type){ // Either orelayers || orechances
        managerInventory = Bukkit.createInventory(null,9,ChatColor.BLACK + "LowY Layer: ( ... , "+getOreName(this.type)+")");
        for(int i = 0;i<9;i++){
            addOption(managerInventory, Material.BLACK_STAINED_GLASS_PANE, " ", i);
        }
        String lowY;
        if(plugin.getConfig().isSet(this.mode + "." + this.biome + ".orelayers." + getOreName(this.type) + ".lowY")){
            lowY = plugin.getConfig().getString(this.mode + "." + this.biome + ".orelayers." + getOreName(this.type) + ".lowY");
        }else {
            lowY = "0-0";
        }
        addOption(managerInventory, Material.RED_STAINED_GLASS,ChatColor.GOLD + "Remove 1",3);
        addOption(managerInventory, Material.RED_STAINED_GLASS,ChatColor.GOLD + "Remove 10",2);
        addOption(managerInventory, Material.RED_STAINED_GLASS,ChatColor.GOLD + "Remove 100",1);
        addOption(managerInventory, Material.NETHER_STAR,ChatColor.GOLD + "LowY: " + lowY,4);
        addOption(managerInventory, Material.GREEN_STAINED_GLASS,ChatColor.GOLD + "Add 1",5);
        addOption(managerInventory, Material.GREEN_STAINED_GLASS,ChatColor.GOLD + "Add 10",6);
        addOption(managerInventory, Material.GREEN_STAINED_GLASS,ChatColor.GOLD + "Add 100",7);
        admin.openInventory(managerInventory);
        inventories.put(managerInventory, this);
    }
    public void openChanceHigh(Material type){ // Either orelayers || orechances
        this.type = type;
        managerInventory = Bukkit.createInventory(null,9,ChatColor.BLACK + "HighY Chance: ( ... , "+getOreName(this.type)+")");
        for(int i = 0;i<9;i++){
            addOption(managerInventory, Material.BLACK_STAINED_GLASS_PANE, " ", i);
        }
        String highY;
        highY = plugin.getConfig().getString(this.mode + "." + this.biome + ".orechances." + getOreName(this.type) + ".highY");

        if(highY == null) highY = "0";
        addOption(managerInventory, Material.RED_STAINED_GLASS,ChatColor.GOLD + "Remove 0.1",3);
        addOption(managerInventory, Material.RED_STAINED_GLASS,ChatColor.GOLD + "Remove 0.01",2);
        addOption(managerInventory, Material.RED_STAINED_GLASS,ChatColor.GOLD + "Remove 0.001",1);
        addOption(managerInventory, Material.RED_STAINED_GLASS,ChatColor.GOLD + "Remove 0.0001",0);
        addOption(managerInventory, Material.NETHER_STAR,ChatColor.GOLD + "HighY: " + highY,4);
        addOption(managerInventory, Material.GREEN_STAINED_GLASS,ChatColor.GOLD + "Add 0.1",5);
        addOption(managerInventory, Material.GREEN_STAINED_GLASS,ChatColor.GOLD + "Add 0.01",6);
        addOption(managerInventory, Material.GREEN_STAINED_GLASS,ChatColor.GOLD + "Add 0.001",7);
        addOption(managerInventory, Material.GREEN_STAINED_GLASS,ChatColor.GOLD + "Add 0.0001",8);
        admin.openInventory(managerInventory);
        inventories.put(managerInventory, this);
    }
    public void openChanceLow(Material type){ // Either orelayers || orechances
        this.type = type;
        managerInventory = Bukkit.createInventory(null,9,ChatColor.BLACK + "LowY Chance: ( ... , "+getOreName(this.type)+")");
        for(int i = 0;i<9;i++){
            addOption(managerInventory, Material.BLACK_STAINED_GLASS_PANE, " ", i);
        }
        String lowY;
        lowY = plugin.getConfig().getString(this.mode + "." + this.biome + ".orechances." + getOreName(this.type) + ".lowY");

        if(lowY == null) lowY = "0";
        addOption(managerInventory, Material.RED_STAINED_GLASS,ChatColor.GOLD + "Remove 0.1",3);
        addOption(managerInventory, Material.RED_STAINED_GLASS,ChatColor.GOLD + "Remove 0.01",2);
        addOption(managerInventory, Material.RED_STAINED_GLASS,ChatColor.GOLD + "Remove 0.001",1);
        addOption(managerInventory, Material.RED_STAINED_GLASS,ChatColor.GOLD + "Remove 0.0001",0);
        addOption(managerInventory, Material.NETHER_STAR,ChatColor.GOLD + "LowY: " + lowY,4);
        addOption(managerInventory, Material.GREEN_STAINED_GLASS,ChatColor.GOLD + "Add 0.1",5);
        addOption(managerInventory, Material.GREEN_STAINED_GLASS,ChatColor.GOLD + "Add 0.01",6);
        addOption(managerInventory, Material.GREEN_STAINED_GLASS,ChatColor.GOLD + "Add 0.001",7);
        addOption(managerInventory, Material.GREEN_STAINED_GLASS,ChatColor.GOLD + "Add 0.0001",8);
        admin.openInventory(managerInventory);
        inventories.put(managerInventory, this);
    }
    public String getOreName(Material material){
        if(material == Material.DIAMOND_ORE) return "diamond";
        if(material == Material.EMERALD_ORE) return "emerald";
        if(material == Material.GOLD_ORE) return "gold";
        if(material == Material.IRON_ORE) return "iron";
        if(material == Material.COAL_ORE) return "coal";
        if(material == Material.LAPIS_ORE) return "lapis_lazuli";
        if(material == Material.REDSTONE_ORE) return "redstone";
        if(material == Material.NETHER_QUARTZ_ORE) return "nether_quartz";
        if(material == Material.ANCIENT_DEBRIS) return "netherite";
        if(material == Material.NETHER_GOLD_ORE) return "nether_gold";
        if(material == Material.COPPER_ORE) return "copper";
        if(material == Material.DEEPSLATE_IRON_ORE) return "deepslate_iron";
        if(material == Material.DEEPSLATE_GOLD_ORE) return "deepslate_gold";
        if(material == Material.DEEPSLATE_DIAMOND_ORE) return "deepslate_diamond";
        if(material == Material.DEEPSLATE_LAPIS_ORE) return "deepslate_lapis";
        if(material == Material.DEEPSLATE_COAL_ORE) return "deepslate_coal";
        if(material == Material.DEEPSLATE_EMERALD_ORE) return "deepslate_emerald";
        if(material == Material.DEEPSLATE_COPPER_ORE) return "deepslate_copper";

        return "null";
    }
}
