package Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import Main.Main;
import Vars.Var;


public class PlayerQuit implements Listener {

    private Main main;

    public PlayerQuit(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerQuitServer(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        event.setQuitMessage(null);
        
        Var.loggedInPlayers.remove(player.getUniqueId());
        if(main.isUUIDInDatabase(player.getUniqueId()))
        	main.setOnlineStatus(player.getUniqueId(), false);
    }
}
