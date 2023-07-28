package Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import Main.Main;
import Vars.Var;

public class PlayerMove implements Listener{
	
	public Main main;

	public PlayerMove(Main main) {
		super();
		this.main = main;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		if(!Var.loggedInPlayers.contains(player.getUniqueId())) {
			
			if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()) {
				player.teleport(event.getFrom());
			}
			
		}
	}

}
