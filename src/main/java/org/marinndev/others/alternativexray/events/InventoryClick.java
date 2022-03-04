package org.marinndev.others.alternativexray.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.marinndev.others.alternativexray.Main;
import org.marinndev.others.alternativexray.SettingsManager;
import org.marinndev.others.alternativexray.commands.ManagerMenu;
import org.marinndev.others.alternativexray.commands.ModeManagerMenu;
import org.marinndev.others.alternativexray.commands.ModeUpdater;

import java.text.DecimalFormat;

public class InventoryClick implements Listener {
    SettingsManager files = SettingsManager.getFiles();
    Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void inventoryClick(InventoryClickEvent event){
        if(event.getCurrentItem() == null) return;
        Player admin = (Player) event.getWhoClicked();
        if(event.getView().getTitle().equalsIgnoreCase(ChatColor.BLACK + "X-ray manager")){
            event.setCancelled(true);
            if(!admin.hasPermission("axray.admin.manage") && !admin.hasPermission("axray.admin.*")
                    && !admin.isOp()) {
                admin.closeInventory();
                admin.sendMessage(ChatColor.RED + "Insufficient permissions!");
                return;
            }
            ManagerMenu menu = ManagerMenu.getMenus().fromInventory(event.getClickedInventory());
            if(event.getClickedInventory().getType() == InventoryType.PLAYER) return;
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();
            if(clickedItem.getType() == Material.SPRUCE_SIGN){
                menu.openChooseMode(admin);
            }
            if(clickedItem.getType() == Material.COMMAND_BLOCK){
                menu.openSettings(admin);
            }
            if(clickedItem.getType() == Material.CHEST){
                new ModeManagerMenu("Manage a mode:",admin);
            }
        }
        if(event.getView().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Choose a mode:")){
            ManagerMenu menu = ManagerMenu.getMenus().fromInventory(event.getClickedInventory());
            event.setCancelled(true);
            if(!admin.hasPermission("axray.admin.manage") && !admin.hasPermission("axray.admin.*")
                    && !admin.isOp()) {
                admin.closeInventory();
                admin.sendMessage(ChatColor.RED + "Insufficient permissions!");
                return;
            }
            Player player = ((Player) event.getWhoClicked());
            ItemStack clickedItem = event.getCurrentItem();
            if(clickedItem.getType() == Material.GOLD_BLOCK){
                event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis mode is already activated!"));
                player.closeInventory();
            }else if(clickedItem.getType() == Material.CHEST){
                ManagerMenu.getMenus().openExpiryMenu(admin, ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName()));
            }else if(event.getCurrentItem().getType() == Material.LANTERN){
                new ManagerMenu("X-ray manager", admin);
            }
        }
        if(event.getView().getTitle().startsWith(ChatColor.BLACK + "Expire")){
            String mode = ChatColor.stripColor(event.getView().getTitle().replace("Expire (", "")
                    .replace(") after: ",""));
            event.setCancelled(true);
            if(!admin.hasPermission("axray.admin.manage") && !admin.hasPermission("axray.admin.*")
                    && !admin.isOp()) {
                admin.closeInventory();
                admin.sendMessage(ChatColor.RED + "Insufficient permissions!");
                return;
            }
            ItemStack clickedItem = event.getCurrentItem();
            int slot = event.getSlot();
            if(clickedItem.getType() == Material.CARTOGRAPHY_TABLE){
                String[] itemSplitName = ChatColor.stripColor(clickedItem.getItemMeta()
                        .getDisplayName().replaceAll(" ", "")).split(":");
                String unit = itemSplitName[0];
                String amount = itemSplitName[1];
                if(amount.equalsIgnoreCase("infinite")){
                    amount = "0";
                }else if(amount.equalsIgnoreCase("9")){
                    amount = "infinite";
                }else{
                    int amm = Integer.parseInt(amount)+1;
                    ItemMeta meta = clickedItem.getItemMeta();
                    meta.setDisplayName(ChatColor.GOLD + unit + ": " + amm);
                    clickedItem.setItemMeta(meta);
                    return;
                }
                ItemMeta meta = clickedItem.getItemMeta();
                meta.setDisplayName(ChatColor.GOLD + unit + ": " + amount);
                clickedItem.setItemMeta(meta);
            }else if(clickedItem.getType() == Material.FLETCHING_TABLE){
                int zeroCount = 0;
                String time = "";
                String[] itemSplitName = ChatColor.stripColor(event.getClickedInventory().getItem(1)
                        .getItemMeta().getDisplayName().replaceAll(" ", "")).split(":");
                String amount = itemSplitName[1];
                time = time + amount + ":";
                if(amount.equalsIgnoreCase("infinite")){
                    new ModeUpdater(mode,"none",event.getWhoClicked());
                    event.getWhoClicked().closeInventory();
                    return;
                }
                if(Integer.parseInt(amount) == 0){
                    zeroCount+=1;
                }
                itemSplitName = ChatColor.stripColor(event.getClickedInventory().getItem(3)
                        .getItemMeta().getDisplayName().replaceAll(" ", "")).split(":");
                amount = itemSplitName[1];
                time = time + amount + ":";
                if(amount.equalsIgnoreCase("infinite")){
                    new ModeUpdater(mode,"none",event.getWhoClicked());
                    event.getWhoClicked().closeInventory();
                    return;
                }
                if(Integer.parseInt(amount) == 0){
                    zeroCount+=1;
                }
                itemSplitName = ChatColor.stripColor(event.getClickedInventory().getItem(5)
                        .getItemMeta().getDisplayName().replaceAll(" ", "")).split(":");
                amount = itemSplitName[1];
                time = time + amount;
                if(amount.equalsIgnoreCase("infinite")){
                    new ModeUpdater(mode,"none",event.getWhoClicked());
                    event.getWhoClicked().closeInventory();
                    return;
                }
                if(Integer.parseInt(amount) == 0){
                    zeroCount+=1;
                }
                if(zeroCount == 3){
                    new ModeUpdater(mode,"none",event.getWhoClicked());
                    event.getWhoClicked().closeInventory();
                    return;
                }
                new ModeUpdater(mode,time,event.getWhoClicked());
                event.getWhoClicked().closeInventory();
            }
        }
        if(event.getView().getTitle().startsWith(ChatColor.BLACK + "Settings:")){
            event.setCancelled(true);
            if(!admin.hasPermission("axray.admin.manage") && !admin.hasPermission("axray.admin.*")
                    && !admin.isOp()) {
                admin.closeInventory();
                admin.sendMessage(ChatColor.RED + "Insufficient permissions!");
                return;
            }
            if(event.getCurrentItem().getType() == Material.BELL){
                ManagerMenu.getMenus().openToggles(admin);
            }
            if(event.getCurrentItem().getType() == Material.ENDER_CHEST){
                ManagerMenu.getMenus().openMultipliers(admin);
            }
            if(event.getCurrentItem().getType() == Material.GOLD_INGOT){
                ManagerMenu.getMenus().openSizeChances(admin);
            }
            if(event.getCurrentItem().getType() == Material.LANTERN){
                new ManagerMenu("X-ray manager", admin);
            }
        }
        if(event.getView().getTitle().startsWith(ChatColor.BLACK + "Toggles:")){
            event.setCancelled(true);
            if(!admin.hasPermission("axray.admin.manage") && !admin.hasPermission("axray.admin.*")
                    && !admin.isOp()) {
                admin.closeInventory();
                admin.sendMessage(ChatColor.RED + "Insufficient permissions!");
                return;
            }
            if(event.getCurrentItem().getType() == Material.LIME_CONCRETE){
                event.getCurrentItem().setType(Material.RED_CONCRETE);
            }else if(event.getCurrentItem().getType() == Material.RED_CONCRETE){
                event.getCurrentItem().setType(Material.LIME_CONCRETE);
            }else if(event.getCurrentItem().getType() == Material.WRITABLE_BOOK){
                Boolean blockPlaceToggle = (event.getClickedInventory().getItem(1).getType()== Material.LIME_CONCRETE);
                Boolean errorsToggle = (event.getClickedInventory().getItem(3).getType()== Material.LIME_CONCRETE);
                if(blockPlaceToggle){
                    plugin.getConfig().set("block-place-toggle", true);
                }else{
                    plugin.getConfig().set("block-place-toggle", false);
                }
                if(errorsToggle){
                    plugin.getConfig().set("console-errors", true);
                }else{
                    plugin.getConfig().set("console-errors", false);
                }
                admin.closeInventory();
                admin.sendMessage(ChatColor.GREEN + "Toggles updated!");
                plugin.saveConfig();
            }else if(event.getCurrentItem().getType() == Material.LANTERN){
                ManagerMenu.getMenus().openSettings(admin);
            }
        }
        if(event.getView().getTitle().startsWith(ChatColor.BLACK + "Multipliers:")){
            event.setCancelled(true);
            if(!admin.hasPermission("axray.admin.manage") && !admin.hasPermission("axray.admin.*")
                    && !admin.isOp()) {
                admin.closeInventory();
                admin.sendMessage(ChatColor.RED + "Insufficient permissions!");
                return;
            }
            if(event.getCurrentItem().getType() == Material.TOTEM_OF_UNDYING){
                ManagerMenu.getMenus().openGlobalMultiplier(admin);
            }
            if(event.getCurrentItem().getType() == Material.GOLDEN_AXE){
                admin.closeInventory();
                admin.sendMessage(ChatColor.YELLOW + "/axray regions (add/remove) [region] [multiplier]");
            }
            if(event.getCurrentItem().getType() == Material.TNT){
                ManagerMenu.getMenus().openTntMultiplier(admin);
            }
            if(event.getCurrentItem().getType() == Material.LANTERN){
                ManagerMenu.getMenus().openSettings(admin);
            }
        }
        if(event.getView().getTitle().startsWith(ChatColor.BLACK + "Global-multiplier:")){
            event.setCancelled(true);
            if(!admin.hasPermission("axray.admin.manage") && !admin.hasPermission("axray.admin.*")
                    && !admin.isOp()) {
                admin.closeInventory();
                admin.sendMessage(ChatColor.RED + "Insufficient permissions!");
                return;
            }
            if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GOLD + "Tens:")){
                String tensString = event.getCurrentItem().getItemMeta().getDisplayName().replace(ChatColor.GOLD +"Tens: ","");
                tensString = ChatColor.stripColor(tensString);

                int tens = Integer.parseInt(tensString);
                ItemMeta meta = event.getCurrentItem().getItemMeta();
                if(tens<90) {
                    meta.setDisplayName(ChatColor.GOLD + "Tens: " + (tens+10));
                }else{
                    meta.setDisplayName(ChatColor.GOLD + "Tens: " + 0);
                }
                event.getCurrentItem().setItemMeta(meta);
            }
            if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GOLD + "Ones:")){
                String onesString = event.getCurrentItem().getItemMeta().getDisplayName().replace(ChatColor.GOLD +"Ones: ","");
                onesString = ChatColor.stripColor(onesString);

                int ones = Integer.parseInt(onesString);
                ItemMeta meta = event.getCurrentItem().getItemMeta();
                if(ones<9) {
                    meta.setDisplayName(ChatColor.GOLD + "Ones: " + (ones+1));
                }else{
                    meta.setDisplayName(ChatColor.GOLD + "Ones: " + 0);
                }
                event.getCurrentItem().setItemMeta(meta);
            }
            if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GOLD + "Decimal:")){
                String decimalString = event.getCurrentItem().getItemMeta().getDisplayName().replace(ChatColor.GOLD +"Decimal: ","");
                decimalString = ChatColor.stripColor(decimalString);

                double decimal = Double.parseDouble(decimalString);
                ItemMeta meta = event.getCurrentItem().getItemMeta();
                if(decimal<=0.9d) {
                    DecimalFormat df = new DecimalFormat("#.#");
                    String s = df.format(decimal+0.1);
                    meta.setDisplayName(ChatColor.GOLD + "Decimal: " + s);
                }else{
                    meta.setDisplayName(ChatColor.GOLD + "Decimal: " + 0.0d);
                }
                event.getCurrentItem().setItemMeta(meta);
            }
            if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "Confirm")){
               double total  = 0;
                String decimalString = event.getClickedInventory().getItem(1).getItemMeta().getDisplayName().replace(ChatColor.GOLD +"Tens: ","");
                decimalString = ChatColor.stripColor(decimalString);

                total+= Double.parseDouble(decimalString);

                decimalString = event.getClickedInventory().getItem(3).getItemMeta().getDisplayName().replace(ChatColor.GOLD +"Ones: ","");
                decimalString = ChatColor.stripColor(decimalString);
                total+= Double.parseDouble(decimalString);

                decimalString = event.getClickedInventory().getItem(5).getItemMeta().getDisplayName().replace(ChatColor.GOLD +"Decimal: ","");
                decimalString = ChatColor.stripColor(decimalString);
                total+= Double.parseDouble(decimalString);

                plugin.getConfig().set("global-multiplier", total);
                plugin.saveConfig();
                admin.closeInventory();
                admin.sendMessage(ChatColor.GREEN + "Global multiplier set: " + total);
            }
        }
        if(event.getView().getTitle().startsWith(ChatColor.BLACK + "Tnt-multiplier:")){
            event.setCancelled(true);
            if(!admin.hasPermission("axray.admin.manage") && !admin.hasPermission("axray.admin.*")
                    && !admin.isOp()) {
                admin.closeInventory();
                admin.sendMessage(ChatColor.RED + "Insufficient permissions!");
                return;
            }
            if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GOLD + "Tens:")){
                String tensString = event.getCurrentItem().getItemMeta().getDisplayName().replace(ChatColor.GOLD +"Tens: ","");
                tensString = ChatColor.stripColor(tensString);

                int tens = Integer.parseInt(tensString);
                ItemMeta meta = event.getCurrentItem().getItemMeta();
                if(tens<90) {
                    meta.setDisplayName(ChatColor.GOLD + "Tens: " + (tens+10));
                }else{
                    meta.setDisplayName(ChatColor.GOLD + "Tens: " + 0);
                }
                event.getCurrentItem().setItemMeta(meta);
            }
            if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GOLD + "Ones:")){
                String onesString = event.getCurrentItem().getItemMeta().getDisplayName().replace(ChatColor.GOLD +"Ones: ","");
                onesString = ChatColor.stripColor(onesString);

                int ones = Integer.parseInt(onesString);
                ItemMeta meta = event.getCurrentItem().getItemMeta();
                if(ones<9) {
                    meta.setDisplayName(ChatColor.GOLD + "Ones: " + (ones+1));
                }else{
                    meta.setDisplayName(ChatColor.GOLD + "Ones: " + 0);
                }
                event.getCurrentItem().setItemMeta(meta);
            }
            if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GOLD + "Decimal:")){
                String decimalString = event.getCurrentItem().getItemMeta().getDisplayName().replace(ChatColor.GOLD +"Decimal: ","");
                decimalString = ChatColor.stripColor(decimalString);

                double decimal = Double.parseDouble(decimalString);
                ItemMeta meta = event.getCurrentItem().getItemMeta();
                if(decimal<=0.9d) {
                    DecimalFormat df = new DecimalFormat("#.#");
                    String s = df.format(decimal+0.1);
                    meta.setDisplayName(ChatColor.GOLD + "Decimal: " + s);
                }else{
                    meta.setDisplayName(ChatColor.GOLD + "Decimal: " + 0.0d);
                }
                event.getCurrentItem().setItemMeta(meta);
            }
            if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "Confirm")){
                double total  = 0;
                String decimalString = event.getClickedInventory().getItem(1).getItemMeta().getDisplayName().replace(ChatColor.GOLD +"Tens: ","");
                decimalString = ChatColor.stripColor(decimalString);

                total+= Double.parseDouble(decimalString);

                decimalString = event.getClickedInventory().getItem(3).getItemMeta().getDisplayName().replace(ChatColor.GOLD +"Ones: ","");
                decimalString = ChatColor.stripColor(decimalString);
                total+= Double.parseDouble(decimalString);

                decimalString = event.getClickedInventory().getItem(5).getItemMeta().getDisplayName().replace(ChatColor.GOLD +"Decimal: ","");
                decimalString = ChatColor.stripColor(decimalString);
                total+= Double.parseDouble(decimalString);

                plugin.getConfig().set("tnt-multiplier", total);
                plugin.saveConfig();
                admin.closeInventory();
                admin.sendMessage(ChatColor.GREEN + "Tnt multiplier set: " + total);
            }
        }
        if(event.getView().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Ore-size chances:")){
            event.setCancelled(true);
            if(!admin.hasPermission("axray.admin.manage") && !admin.hasPermission("axray.admin.*")
                    && !admin.isOp()) {
                admin.closeInventory();
                admin.sendMessage(ChatColor.RED + "Insufficient permissions!");
                return;
            }
            String itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            if(itemName.equalsIgnoreCase("back")){
                ManagerMenu.getMenus().openSettings(admin);
                return;
            }
            if(itemName.equalsIgnoreCase(" ")) return;
            ManagerMenu.getMenus().openSizeSelector(itemName, event.getCurrentItem().getType(), admin);

        }
        if(event.getView().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Select ore size:")){
            event.setCancelled(true);
            if(!admin.hasPermission("axray.admin.manage") && !admin.hasPermission("axray.admin.*")
                    && !admin.isOp()) {
                admin.sendMessage(ChatColor.RED + "Insufficient permissions!");
                return;
            }
            String itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            if(itemName.equalsIgnoreCase("back")){
                ManagerMenu.getMenus().openSizeChances(admin);
                return;
            }
            if(event.getCurrentItem().getType() == Material.DIAMOND_ORE){
                ManagerMenu.getMenus().openChance(admin, "diamond", Integer.parseInt(itemName));
            }
            if(event.getCurrentItem().getType() == Material.GOLD_ORE){
                ManagerMenu.getMenus().openChance(admin, "gold", Integer.parseInt(itemName));
            }
            if(event.getCurrentItem().getType() == Material.IRON_ORE){
                ManagerMenu.getMenus().openChance(admin, "iron", Integer.parseInt(itemName));
            }
            if(event.getCurrentItem().getType() == Material.COAL_ORE){
                ManagerMenu.getMenus().openChance(admin, "coal", Integer.parseInt(itemName));
            }
            if(event.getCurrentItem().getType() == Material.LAPIS_ORE){
                ManagerMenu.getMenus().openChance(admin, "lapis_lazuli", Integer.parseInt(itemName));
            }
            if(event.getCurrentItem().getType() == Material.EMERALD_ORE){
                ManagerMenu.getMenus().openChance(admin, "emerald", Integer.parseInt(itemName));
            }
            if(event.getCurrentItem().getType() == Material.REDSTONE_ORE){
                ManagerMenu.getMenus().openChance(admin, "redstone", Integer.parseInt(itemName));
            }
            if(event.getCurrentItem().getType() == Material.NETHER_QUARTZ_ORE){
                ManagerMenu.getMenus().openChance(admin, "nether_quartz", Integer.parseInt(itemName));
            }
            if(event.getCurrentItem().getType() == Material.ANCIENT_DEBRIS){
                ManagerMenu.getMenus().openChance(admin, "netherite", Integer.parseInt(itemName));
            }
            if(event.getCurrentItem().getType() == Material.NETHER_GOLD_ORE){
                ManagerMenu.getMenus().openChance(admin, "nether_gold", Integer.parseInt(itemName));
            }
        }
        if(event.getView().getTitle().startsWith(ChatColor.BLACK + "Set chance:")){
            event.setCancelled(true);
            if(!admin.hasPermission("axray.admin.manage") && !admin.hasPermission("axray.admin.*")
                    && !admin.isOp()) {
                admin.closeInventory();
                admin.sendMessage(ChatColor.RED + "Insufficient permissions!");
                return;
            }
            if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GOLD + "Tens:")){
                String tensString = event.getCurrentItem().getItemMeta().getDisplayName().replace(ChatColor.GOLD +"Tens: ","");
                tensString = ChatColor.stripColor(tensString);

                int tens = Integer.parseInt(tensString);
                ItemMeta meta = event.getCurrentItem().getItemMeta();
                if(tens<90) {
                    meta.setDisplayName(ChatColor.GOLD + "Tens: " + (tens+10));
                }else{
                    meta.setDisplayName(ChatColor.GOLD + "Tens: " + 0);
                }
                event.getCurrentItem().setItemMeta(meta);
            }
            if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GOLD + "Ones:")){
                String onesString = event.getCurrentItem().getItemMeta().getDisplayName().replace(ChatColor.GOLD +"Ones: ","");
                onesString = ChatColor.stripColor(onesString);

                int ones = Integer.parseInt(onesString);
                ItemMeta meta = event.getCurrentItem().getItemMeta();
                if(ones<9) {
                    meta.setDisplayName(ChatColor.GOLD + "Ones: " + (ones+1));
                }else{
                    meta.setDisplayName(ChatColor.GOLD + "Ones: " + 0);
                }
                event.getCurrentItem().setItemMeta(meta);
            }
            if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GOLD + "Decimal:")){
                String decimalString = event.getCurrentItem().getItemMeta().getDisplayName().replace(ChatColor.GOLD +"Decimal: ","");
                decimalString = ChatColor.stripColor(decimalString);

                double decimal = Double.parseDouble(decimalString);
                ItemMeta meta = event.getCurrentItem().getItemMeta();
                if(decimal<=0.9d) {
                    DecimalFormat df = new DecimalFormat("#.#");
                    String s = df.format(decimal+0.1);
                    meta.setDisplayName(ChatColor.GOLD + "Decimal: " + s);
                }else{
                    meta.setDisplayName(ChatColor.GOLD + "Decimal: " + 0.0d);
                }
                event.getCurrentItem().setItemMeta(meta);
            }
            if(event.getCurrentItem().getItemMeta().getDisplayName().startsWith(ChatColor.GREEN + "Confirm")){
                double total  = 0;
                String decimalString = event.getClickedInventory().getItem(1).getItemMeta().getDisplayName().replace(ChatColor.GOLD +"Tens: ","");
                decimalString = ChatColor.stripColor(decimalString);

                total+= Double.parseDouble(decimalString);

                decimalString = event.getClickedInventory().getItem(3).getItemMeta().getDisplayName().replace(ChatColor.GOLD +"Ones: ","");
                decimalString = ChatColor.stripColor(decimalString);
                total+= Double.parseDouble(decimalString);

                decimalString = event.getClickedInventory().getItem(5).getItemMeta().getDisplayName().replace(ChatColor.GOLD +"Decimal: ","");
                decimalString = ChatColor.stripColor(decimalString);
                total+= Double.parseDouble(decimalString);

                String ore = ChatColor.stripColor(event.getView().getTitle()).replace("Set chance: (","")
                        .replace(" ", "").replace(")","").split(",")[0];
                int amount = Integer.parseInt(ChatColor.stripColor(event.getView().getTitle()).replace("Set chance: (","")
                        .replace(" ", "").replace(")","").split(",")[1]);
                plugin.getConfig().set("ore-sizes-chances." + ore + "." + amount, total);
                plugin.saveConfig();
                //admin.closeInventory();
                ManagerMenu.getMenus().openSizeSelector(ore,getMaterial(ore),admin);
            }
        }
        ///////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////
        if(event.getView().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Manage a mode:")){
            event.setCancelled(true);
            if(!admin.hasPermission("axray.admin.manage") && !admin.hasPermission("axray.admin.*")
                    && !admin.isOp()) {
                admin.closeInventory();
                admin.sendMessage(ChatColor.RED + "Insufficient permissions!");
                return;
            }
            if(event.getCurrentItem().getType() == Material.CHEST){
                String modeName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                ModeManagerMenu menu = new ModeManagerMenu(ChatColor.BLACK + "Manage a mode: ", (Player) event.getWhoClicked());
                menu.openOptionsMenu(modeName);
            }else if(event.getCurrentItem().getType() == Material.LANTERN){
                new ManagerMenu("X-ray manager",admin);
            }else if(event.getCurrentItem().getType() == Material.BELL){
                admin.sendMessage(ChatColor.YELLOW + "/axray create [modename]");
                admin.closeInventory();
            }
        }
        if(event.getView().getTitle().startsWith(ChatColor.BLACK + "Manage: ")){
            event.setCancelled(true);
            if(!admin.hasPermission("axray.admin.manage") && !admin.hasPermission("axray.admin.*")
                    && !admin.isOp()) {
                admin.closeInventory();
                admin.sendMessage(ChatColor.RED + "Insufficient permissions!");
                return;
            }
            String modeName = ChatColor.stripColor(event.getView().getTitle()).replace("Manage: (","").replace(")","");
            ModeManagerMenu menu = ModeManagerMenu.inventories.get(event.getClickedInventory());
            if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD +"Broadcast-start")){
                admin.closeInventory();
                admin.sendMessage(ChatColor.YELLOW + "/axray mode [mode] broadcast-start [message]");
            }else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD +"Broadcast-end")){
                admin.closeInventory();
                admin.sendMessage(ChatColor.YELLOW + "/axray mode [mode] broadcast-end [message]");
            }else if(event.getCurrentItem().getType() == Material.LANTERN){
                new ModeManagerMenu("Manage a mode:",admin);
            }else if(event.getCurrentItem().getType() == Material.BOOK){
                menu.openBiomesMenu();
            }
        }
        if(event.getView().getTitle().startsWith(ChatColor.BLACK + "Choose biome: (")){
            event.setCancelled(true);
            if(!admin.hasPermission("axray.admin.manage") && !admin.hasPermission("axray.admin.*")
                    && !admin.isOp()) {
                admin.closeInventory();
                admin.sendMessage(ChatColor.RED + "Insufficient permissions!");
                return;
            }
            String modeName = event.getCurrentItem().getItemMeta().getDisplayName();
            ModeManagerMenu menu = ModeManagerMenu.inventories.get(event.getClickedInventory());
            if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD +"Broadcast-start")){
                admin.closeInventory();
                admin.sendMessage(ChatColor.YELLOW + "/axray mode [mode] broadcast-start [message]");
            }else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD +"Broadcast-end")){
                admin.closeInventory();
                admin.sendMessage(ChatColor.YELLOW + "/axray mode [mode] broadcast-end [message]");
            }else if(event.getCurrentItem().getType() == Material.LANTERN){
                menu.openOptionsMenu(menu.mode);
            }else{
                menu.openBiomeOptionsMenu(event.getCurrentItem()
                .getItemMeta().getDisplayName());
            }
        }
        if(event.getView().getTitle().startsWith(ChatColor.BLACK + "Choose option: (")){
            event.setCancelled(true);
            if(!admin.hasPermission("axray.admin.manage") && !admin.hasPermission("axray.admin.*")
                    && !admin.isOp()) {
                admin.closeInventory();
                admin.sendMessage(ChatColor.RED + "Insufficient permissions!");
                return;
            }
            ModeManagerMenu menu = ModeManagerMenu.inventories.get(event.getClickedInventory());
            if(event.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE){
                return;
            }
            if(event.getCurrentItem().getType() == Material.LANTERN){
                menu.openBiomesMenu();
            }else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Ore Layers")){
                menu.openOres("orelayers");
            }else if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Ore Chances")){
                menu.openOres("orechances");
            }
        }
        if(event.getView().getTitle().startsWith(ChatColor.BLACK + "Choose ore: (")){
            event.setCancelled(true);
            if(!admin.hasPermission("axray.admin.manage") && !admin.hasPermission("axray.admin.*")
                    && !admin.isOp()) {
                admin.closeInventory();
                admin.sendMessage(ChatColor.RED + "Insufficient permissions!");
                return;
            }
            ModeManagerMenu menu = ModeManagerMenu.inventories.get(event.getClickedInventory());
            if(event.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE){
                return;
            }
            if(event.getCurrentItem().getType() == Material.LANTERN){
                menu.openBiomeOptionsMenu(menu.biome);
            }else{
                if(menu.option.equalsIgnoreCase("orelayers")) {
                    menu.openLayerHigh(event.getCurrentItem().getType());
                }else if(menu.option.equalsIgnoreCase("orechances")){
                    menu.openChanceHigh(event.getCurrentItem().getType());
                }
            }
        }
        if(event.getView().getTitle().startsWith(ChatColor.BLACK + "HighY Layer: (")){
            event.setCancelled(true);
            if(!admin.hasPermission("axray.admin.manage") && !admin.hasPermission("axray.admin.*")
                    && !admin.isOp()) {
                admin.closeInventory();
                admin.sendMessage(ChatColor.RED + "Insufficient permissions!");
                return;
            }
            ModeManagerMenu menu = ModeManagerMenu.inventories.get(event.getClickedInventory());
            if(event.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE) {
                return;
            }
            String itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            if(event.getCurrentItem().getType() == Material.NETHER_STAR) {
                event.getCurrentItem().setType(Material.END_CRYSTAL);
                admin.updateInventory();
                return;
            }
            int amount = 0;
            int amount2 = 0;
            if(event.getClickedInventory().getItem(4).getType() == Material.NETHER_STAR) {
                if (itemName.startsWith("Add ")) {
                    amount = Integer.parseInt(itemName.replace("Add ", ""));
                }
                if (itemName.startsWith("Remove ")) {
                    amount = -Integer.parseInt(itemName.replace("Remove ", ""));
                }
            }else{
                if (itemName.startsWith("Add ")) {
                    amount2 = Integer.parseInt(itemName.replace("Add ", ""));
                }
                if (itemName.startsWith("Remove ")) {
                    amount2 = -Integer.parseInt(itemName.replace("Remove ", ""));
                }
            }
            String[] data = ChatColor.stripColor(event.getClickedInventory()
                    .getItem(4).getItemMeta().getDisplayName())
                    .replace("HighY: ","").split("-");
            Integer finalint = Integer.parseInt(data[0]) + amount;
            Integer finalint2 = Integer.parseInt(data[1]) + amount2;
            if(finalint< 0){
                finalint = 0;
            }else if(finalint >256){
                finalint = 256;
            }
            if(finalint2< 0){
                finalint2 = 0;
            }else if(finalint2 >256){
                finalint2 = 256;
            }
            String finalname = ChatColor.GOLD + "HighY: " + finalint + "-" + finalint2;
            ItemMeta meta = event.getClickedInventory().getItem(4).getItemMeta();
            meta.setDisplayName(finalname);
            event.getClickedInventory().getItem(4).setItemMeta(meta);

            if(event.getCurrentItem().getType() == Material.END_CRYSTAL) {
                menu.openLayerLow(null);
                //Bukkit.broadcastMessage(menu.mode + "." + menu.biome + ".orelayers." + menu.getOreName(menu.type) + ".highY");
                if(itemName.equalsIgnoreCase("HighY: 0-0") || itemName.equalsIgnoreCase("HighY: 256-256")|| Integer.parseInt(data[0]) >= Integer.parseInt(data[1])){
                    plugin.getConfig().set(menu.mode + "." + menu.biome + ".orelayers." + menu.getOreName(menu.type) + ".highY", null);
                    plugin.saveConfig();
                }else{
                    plugin.getConfig().set(menu.mode + "." + menu.biome + ".orelayers." + menu.getOreName(menu.type) + ".highY",
                            itemName.replace("HighY: ", ""));
                    if(!plugin.getConfig().isSet(menu.mode + "." + menu.biome + ".orechances." + menu.getOreName(menu.type) + ".highY")){
                        plugin.getConfig().set(menu.mode + "." + menu.biome + ".orechances." + menu.getOreName(menu.type) + ".highY", 0);
                    }
                    plugin.saveConfig();
                }
            }
        }
        if(event.getView().getTitle().startsWith(ChatColor.BLACK + "LowY Layer: (")){
            event.setCancelled(true);
            if(!admin.hasPermission("axray.admin.manage") && !admin.hasPermission("axray.admin.*")
                    && !admin.isOp()) {
                admin.closeInventory();
                admin.sendMessage(ChatColor.RED + "Insufficient permissions!");
                return;
            }
            ModeManagerMenu menu = ModeManagerMenu.inventories.get(event.getClickedInventory());
            if(event.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE) {
                return;
            }
            String itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            if(event.getCurrentItem().getType() == Material.NETHER_STAR) {
                event.getCurrentItem().setType(Material.END_CRYSTAL);
                admin.updateInventory();
                return;
            }
            int amount = 0;
            int amount2 = 0;
            if(event.getClickedInventory().getItem(4).getType() == Material.NETHER_STAR) {
                if (itemName.startsWith("Add ")) {
                    amount = Integer.parseInt(itemName.replace("Add ", ""));
                }
                if (itemName.startsWith("Remove ")) {
                    amount = -Integer.parseInt(itemName.replace("Remove ", ""));
                }
            }else{
                if (itemName.startsWith("Add ")) {
                    amount2 = Integer.parseInt(itemName.replace("Add ", ""));
                }
                if (itemName.startsWith("Remove ")) {
                    amount2 = -Integer.parseInt(itemName.replace("Remove ", ""));
                }
            }
            String[] data = ChatColor.stripColor(event.getClickedInventory()
                    .getItem(4).getItemMeta().getDisplayName())
                    .replace("LowY: ","").split("-");
            Integer finalint = Integer.parseInt(data[0]) + amount;
            Integer finalint2 = Integer.parseInt(data[1]) + amount2;
            if(finalint< 0){
                finalint = 0;
            }else if(finalint >256){
                finalint = 256;
            }
            if(finalint2< 0){
                finalint2 = 0;
            }else if(finalint2 >256){
                finalint2 = 256;
            }
            String finalname = ChatColor.GOLD + "LowY: " + finalint + "-" + finalint2;
            ItemMeta meta = event.getClickedInventory().getItem(4).getItemMeta();
            meta.setDisplayName(finalname);
            event.getClickedInventory().getItem(4).setItemMeta(meta);

            if(event.getCurrentItem().getType() == Material.END_CRYSTAL) {
                menu.openLayerLow(null);
                //Bukkit.broadcastMessage(menu.mode + "." + menu.biome + ".orelayers." + menu.getOreName(menu.type) + ".highY");
                if(itemName.equalsIgnoreCase("LowY: 0-0") || itemName.equalsIgnoreCase("LowY: 256-256")|| Integer.parseInt(data[0]) >= Integer.parseInt(data[1])){
                    plugin.getConfig().set(menu.mode + "." + menu.biome + ".orelayers." + menu.getOreName(menu.type) + ".lowY", null);
                    plugin.saveConfig();
                }else{
                    plugin.getConfig().set(menu.mode + "." + menu.biome + ".orelayers." + menu.getOreName(menu.type) + ".lowY",
                            itemName.replace("LowY: ", ""));
                    if(!plugin.getConfig().isSet(menu.mode + "." + menu.biome + ".orechances." + menu.getOreName(menu.type) + ".lowY")){
                        plugin.getConfig().set(menu.mode + "." + menu.biome + ".orechances." + menu.getOreName(menu.type) + ".lowY", 0);
                    }
                    plugin.saveConfig();
                }
                menu.openBiomeOptionsMenu(menu.biome);
            }
        }
        if(event.getView().getTitle().startsWith(ChatColor.BLACK + "HighY Chance: (")){
            event.setCancelled(true);
            if(!admin.hasPermission("axray.admin.manage") && !admin.hasPermission("axray.admin.*")
                    && !admin.isOp()) {
                admin.closeInventory();
                admin.sendMessage(ChatColor.RED + "Insufficient permissions!");
                return;
            }
            ModeManagerMenu menu = ModeManagerMenu.inventories.get(event.getClickedInventory());
            if(event.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE) {
                return;
            }
            String itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            double amount = 0;
            double amount2 = 0;
            if(event.getClickedInventory().getItem(4).getType() == Material.NETHER_STAR) {
                if (itemName.startsWith("Add ")) {
                    amount = Double.parseDouble(itemName.replace("Add ", ""));
                }
                if (itemName.startsWith("Remove ")) {
                    amount = -Double.parseDouble(itemName.replace("Remove ", ""));
                }
            }else{
                if (itemName.startsWith("Add ")) {
                    amount2 = Double.parseDouble(itemName.replace("Add ", ""));
                }
                if (itemName.startsWith("Remove ")) {
                    amount2 = -Double.parseDouble(itemName.replace("Remove ", ""));
                }
            }
            String data = ChatColor.stripColor(event.getClickedInventory()
                    .getItem(4).getItemMeta().getDisplayName())
                    .replace("HighY: ","");
            double finaldouble = Double.parseDouble(data) + amount;
            if(finaldouble<0){
                finaldouble = 0;
            }
            DecimalFormat df = new DecimalFormat("#.####");
            String s = df.format(finaldouble);

            String finalname = ChatColor.GOLD + "HighY: " + s;
            ItemMeta meta = event.getClickedInventory().getItem(4).getItemMeta();
            meta.setDisplayName(finalname);
            event.getClickedInventory().getItem(4).setItemMeta(meta);

            if(event.getCurrentItem().getType() == Material.NETHER_STAR) {
                menu.openChanceLow(menu.type);
                //Bukkit.broadcastMessage(menu.mode + "." + menu.biome + ".orelayers." + menu.getOreName(menu.type) + ".highY");
                plugin.getConfig().set(menu.mode + "." + menu.biome + ".orechances." + menu.getOreName(menu.type) + ".highY",
                       itemName.replace("HighY: ", ""));
                if(!plugin.getConfig().isSet(menu.mode + "." + menu.biome + ".orechances." + menu.getOreName(menu.type) + ".highY")){
                    plugin.getConfig().set(menu.mode + "." + menu.biome + ".orechances." + menu.getOreName(menu.type) + ".highY", 0);
                }
                plugin.saveConfig();
            }
        }
        if(event.getView().getTitle().startsWith(ChatColor.BLACK + "LowY Chance: (")){
            event.setCancelled(true);
            if(!admin.hasPermission("axray.admin.manage") && !admin.hasPermission("axray.admin.*")
                    && !admin.isOp()) {
                admin.closeInventory();
                admin.sendMessage(ChatColor.RED + "Insufficient permissions!");
                return;
            }
            ModeManagerMenu menu = ModeManagerMenu.inventories.get(event.getClickedInventory());
            if(event.getCurrentItem().getType() == Material.BLACK_STAINED_GLASS_PANE) {
                return;
            }
            String itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            double amount = 0;
            double amount2 = 0;
            if(event.getClickedInventory().getItem(4).getType() == Material.NETHER_STAR) {
                if (itemName.startsWith("Add ")) {
                    amount = Double.parseDouble(itemName.replace("Add ", ""));
                }
                if (itemName.startsWith("Remove ")) {
                    amount = -Double.parseDouble(itemName.replace("Remove ", ""));
                }
            }else{
                if (itemName.startsWith("Add ")) {
                    amount2 = Double.parseDouble(itemName.replace("Add ", ""));
                }
                if (itemName.startsWith("Remove ")) {
                    amount2 = -Double.parseDouble(itemName.replace("Remove ", ""));
                }
            }
            String data = ChatColor.stripColor(event.getClickedInventory()
                    .getItem(4).getItemMeta().getDisplayName())
                    .replace("LowY: ","");
            double finaldouble = Double.parseDouble(data) + amount;
            if(finaldouble<0){
                finaldouble = 0;
            }
            DecimalFormat df = new DecimalFormat("#.####");
            String s = df.format(finaldouble);

            String finalname = ChatColor.GOLD + "LowY: " + s;
            ItemMeta meta = event.getClickedInventory().getItem(4).getItemMeta();
            meta.setDisplayName(finalname);
            event.getClickedInventory().getItem(4).setItemMeta(meta);

            if(event.getCurrentItem().getType() == Material.NETHER_STAR) {
                //Bukkit.broadcastMessage(menu.mode + "." + menu.biome + ".orelayers." + menu.getOreName(menu.type) + ".highY");
                plugin.getConfig().set(menu.mode + "." + menu.biome + ".orechances." + menu.getOreName(menu.type) + ".lowY",
                       itemName.replace("LowY: ", ""));
                if(!plugin.getConfig().isSet(menu.mode + "." + menu.biome + ".orechances." + menu.getOreName(menu.type) + ".lowY")){
                    plugin.getConfig().set(menu.mode + "." + menu.biome + ".orechances." + menu.getOreName(menu.type) + ".lowY", 0);
                }
                plugin.saveConfig();
                admin.closeInventory();
                menu.openBiomeOptionsMenu(menu.biome);
            }
        }
    }
    private Material getMaterial(String s){
        if(s.equalsIgnoreCase("netherite")) return Material.ANCIENT_DEBRIS;
        if(s.equalsIgnoreCase("nether_gold")) return Material.NETHER_GOLD_ORE;
        if(s.equalsIgnoreCase("diamond")) return Material.DIAMOND_ORE;
        if(s.equalsIgnoreCase("gold")) return Material.GOLD_ORE;
        if(s.equalsIgnoreCase("iron")) return Material.IRON_ORE;
        if(s.equalsIgnoreCase("coal")) return Material.COAL_ORE;
        if(s.equalsIgnoreCase("lapis_lazuli")) return Material.LAPIS_ORE;
        if(s.equalsIgnoreCase("emerald")) return Material.EMERALD_ORE;
        if(s.equalsIgnoreCase("redstone")) return Material.REDSTONE_ORE;
        if(s.equalsIgnoreCase("nether_quartz")) return Material.NETHER_QUARTZ_ORE;
        if(s.equalsIgnoreCase("copper")) return Material.COPPER_ORE;
        if(s.equalsIgnoreCase("deepslate_iron")) return Material.DEEPSLATE_IRON_ORE;
        if(s.equalsIgnoreCase("deepslate_gold")) return Material.DEEPSLATE_GOLD_ORE;
        if(s.equalsIgnoreCase("deepslate_diamond")) return Material.DEEPSLATE_DIAMOND_ORE;
        if(s.equalsIgnoreCase("deepslate_lapis")) return Material.DEEPSLATE_LAPIS_ORE;
        if(s.equalsIgnoreCase("deepslate_coal")) return Material.DEEPSLATE_COAL_ORE;
        if(s.equalsIgnoreCase("deepslate_emerald")) return Material.DEEPSLATE_EMERALD_ORE;
        if(s.equalsIgnoreCase("nether_copper")) return Material.DEEPSLATE_COPPER_ORE;
        return Material.AIR;
    }
}
