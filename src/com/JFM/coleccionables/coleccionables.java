package com.JFM.coleccionables;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
public class coleccionables extends JavaPlugin implements Listener {

    private static Boolean coleccionablesBoolean = true;
    private PlayerData playerData;
    public static void coleccionablesSwitch() {
        coleccionablesBoolean = !coleccionablesBoolean;
    }

    @Override
    public void onEnable(){
        ColeccionablesCommands commands = new ColeccionablesCommands();
        getCommand("coleccionables").setExecutor(commands);
        getServer().getPluginManager().registerEvents(this, this);
        playerData = new PlayerData(this);
        getServer().getConsoleSender().sendMessage("[coleccionables]: Plugin is enabled!");
    }
    @Override
    public void onDisable(){
        playerData.save();
        getServer().getConsoleSender().sendMessage("[coleccionables]: Plugin is disabled!");
    }
    @EventHandler
    public void onPlayerGetVHS(InventoryClickEvent event){
        event.setCancelled(true);
        //getServer().getConsoleSender().sendMessage("inventory");
        ItemStack item = event.getCurrentItem();
        if (item == null) { event.setCancelled(false);return;}
        ItemMeta m = item.getItemMeta();
        if(m==null) { event.setCancelled(false);return;}
        getServer().getConsoleSender().sendMessage(m.getDisplayName().toString());
        if (m != null && m.hasLore() && m.getDisplayName().contains("VHS") && coleccionablesBoolean){
            getServer().getConsoleSender().sendMessage("inventory VHS");
            Player p = (Player) event.getWhoClicked();
            p.closeInventory();
            new Thread(()->{playerGetVHS(p, item);}).start();
        }else event.setCancelled(false);
    }

    private synchronized void playerGetVHS(Player p, ItemStack i){
        i.toString();
        Boolean newVHS = playerData.checkVHS(p, i);
        if (newVHS) {
            //p.sendTitle(ChatColor.DARK_PURPLE + "Has recogido un VHS", ChatColor.LIGHT_PURPLE + "Tienes "+playerData.getVHS(p)+"/10");
            p.sendTitle(ChatColor.DARK_PURPLE + "Has recogido un VHS", null);
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation(),60,0.9,0.9,0.9,0.6);
            p.playSound(p.getLocation(),Sound.ENTITY_FIREWORK_ROCKET_BLAST, 0.6F,1);
            try {
                Thread.sleep(1000);
                p.playSound(p.getLocation(),Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED,1,1);
                Thread.sleep(250);
                p.sendTitle(null, ChatColor.LIGHT_PURPLE + "Tienes "+playerData.getVHS(p)+"/10",0,40,15);
            }catch(InterruptedException e){}
        }else{
            p.sendTitle(ChatColor.DARK_RED + "Ya tienes este VHS", ChatColor.LIGHT_PURPLE + "Tienes "+playerData.getVHS(p)+"/10",0,30,15);
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        }
    }
}

class ColeccionablesCommands implements CommandExecutor {
    @Override
    public boolean onCommand (CommandSender sender, Command cmd, String label, String[] args){
        if (sender instanceof Player) {
            if (!((Player) sender).isOp()) return true;
        }
        Player p = (Player) sender;
        coleccionables.coleccionablesSwitch();
        p.sendMessage("coleccionables modificado");
        return true;
    }
}