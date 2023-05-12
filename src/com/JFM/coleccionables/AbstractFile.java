package com.JFM.coleccionables;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public class AbstractFile {

    protected coleccionables main;
    private File file;
    protected FileConfiguration config;

    public AbstractFile(coleccionables main, String fileName){
        this.main = main;
        this.file = new File(main.getDataFolder(), fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            }catch (IOException e){}
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void save(){
        try{
            config.save(file);
        }catch (IOException e){}
    }

}

class PlayerData extends AbstractFile{

    public PlayerData(coleccionables main){
        super(main, "playerdata.yml");
    }
    public void newPlayer(Player player){
        if(!config.isSet("players."+ player.getName())){
            config.set("players."+ player.getName(),"");
            config.set("players."+ player.getName()+".Numero",0);
        }
    }

    public int getVHS(Player p) {
        return config.getInt("players."+p.getName()+".Numero");
    }
    public Boolean checkVHS(Player p, ItemStack i) {
        if(!config.isSet("players."+p.getName()+"."+i.getItemMeta().getDisplayName())){
            config.set("players."+p.getName()+"."+i.getItemMeta().getDisplayName(),true);
            config.set("players."+p.getName()+".Numero",config.getInt("players."+p.getName()+".Numero")+1);
            return true;
        }else{
            return false;
        }
    }
}
