package org.marinndev.others.alternativexray;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.marinndev.others.alternativexray.commands.CommandsManager;
import org.marinndev.others.alternativexray.commands.ModeUpdater;
import org.marinndev.others.alternativexray.events.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class Main extends JavaPlugin {

    private String mode;
    SettingsManager files = SettingsManager.getFiles();

    @Override
    public void onEnable() {
        files.setup(this);
        setModeFromConfig();
        expiryCheck.runTaskTimer(this,20,20);
        Bukkit.getPluginCommand("axray").setExecutor(new CommandsManager());
        Bukkit.getPluginManager().registerEvents(new BlockBreak(),this);
        Bukkit.getPluginManager().registerEvents(new BlockPush(),this);
        Bukkit.getPluginManager().registerEvents(new BlockPlace(),this);
        Bukkit.getPluginManager().registerEvents(new Explode(),this);
        Bukkit.getPluginManager().registerEvents(new InventoryClick(),this);
        addMetaData();
    }

    /**
     * Gets the current config mode <br />
     * By default, this would be "Default" but in case of an event it could be e.g.,"DiamondEvent".
     */
    public String getMode(){
        return mode;
    }

    /**
     * Sets the value of mode to ("Mode" from config.yml)
     */
    private void setModeFromConfig(){
        try {
            mode = this.getConfig().getString("mode");
        }catch(NullPointerException e){
            this.getConfig().set("mode","default");
            mode = "default";
        }
    }

    /**
     * Runnable that manages the mode expiration in config
     */
    BukkitRunnable expiryCheck = new BukkitRunnable() {
        @Override
        public void run() {
            int i = compareDates();
            setModeFromConfig();
            if(mode.equalsIgnoreCase("default")) return;
            if(i <= 0){
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString(mode + ".broadcast-notice-end")));
                new ModeUpdater("default", null, null);
            }
        }
    };

    /**
     * Compares the mode-expire DateTime value in config with the current DateTime value
     * @return The difference between the two values (<0 , 0 , >0)
     */
    public int compareDates(){
        String configDate = this.getConfig().getString("mode-expire");
        if(configDate.equalsIgnoreCase("none")) return 1;
        String now = getTime(0,0,0);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh-mm", Locale.ENGLISH);
        Date expireDateTime;
        Date currentDateTime;
        try {
            expireDateTime = format.parse(configDate);
            currentDateTime = format.parse(now);
            int i = expireDateTime.compareTo(currentDateTime);
            return i;
        } catch (ParseException e) {
            return 1;
        }
    }

    /**
     * Adds the metadata to the claimed blocks to protect them from being replaced
     */
    private void addMetaData(){
        for(org.bukkit.World world : Bukkit.getWorlds()){
            new BlockPlace().protectBlocks(world);
        }
    }

    /**
     * Checks if the plugin is activated in a world
     * @param world
     * @return true/false
     */
    public boolean checkWorld(String world){
        if(this.getConfig().getStringList("activated-worlds").contains(world)){
            return true;
        }
        return false;
    }

    /**
     * Get a datetime value for the specified params
     * @param days
     * @param hours
     * @param minutes
     * @return DateFormat (yyyy-MM-dd hh-mm)
     */
    public String getTime(int days, int hours, int minutes){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, days);
        cal.add(Calendar.HOUR_OF_DAY, hours);
        cal.add(Calendar.MINUTE, minutes);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh-mm", Locale.ENGLISH);
        format.format(cal.getTime());
        return format.format(cal.getTime());
    }
}
