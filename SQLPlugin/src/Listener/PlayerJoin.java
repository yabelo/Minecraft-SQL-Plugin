package Listener;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import Main.Main;
import Utils.Utils;

public class PlayerJoin implements Listener {

    private Main main;

    public PlayerJoin(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoinServer(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        FileConfiguration config = main.getConfig();
        
        event.setJoinMessage(null);
        player.sendMessage(Utils.chat(config.getString("welcome-message").replace("%pn%", player.getName())));

        if(!main.isUUIDInDatabase(player.getUniqueId())) {
        	
        	for(String s : config.getStringList("need-to-register-message")) {
                player.sendMessage(Utils.chat(s));
        	}
        }
        else {
        	for(String s : config.getStringList("need-to-login-message")) {
                player.sendMessage(Utils.chat(s));
        	}
        }
        
        main.setOnlineStatus(player.getUniqueId(), true);
    }
}
