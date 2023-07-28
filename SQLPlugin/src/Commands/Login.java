package Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Main.Main;
import Utils.Utils;
import Vars.Var;

public class Login implements CommandExecutor{
	
	public Main main;

	public Login(Main main) {
		super();
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if(!(sender instanceof Player)) return false;
		Player player = (Player) sender;
		
		String usage = Utils.chat("&cUsage: /login <password>");
		
		if(Var.loggedInPlayers.contains(player.getUniqueId())) {
			player.sendMessage(Utils.chat("&dAlready logged in."));
			return false;
		}
		
		if(args.length == 1) {
			if(!main.isUUIDInDatabase(player.getUniqueId())) {
				player.sendMessage(Utils.chat("&cYou need to register first. \nUseage: /register <password>"));
				return false;
			}
			else {
				try {
					if(main.getPasswordForUUID(player.getUniqueId()).equals(args[0])) {
						player.sendMessage(Utils.chat("&dYou logined successfully."));
						Var.loggedInPlayers.add(player.getUniqueId());
					}
					else {
						player.sendMessage(Utils.chat("&cTry a different password."));
						return false;
					}
				}
				catch (Exception e) {
					player.sendMessage(Utils.chat("&cCannot login you into server."));
					Bukkit.getServer().getConsoleSender().sendMessage("Cannot register " + player.getName() + " : " + e.getMessage());
					return false;
				}
			}
		}
		else {
			player.sendMessage(usage);
			return false;
		}
		
		return true;
	}

}
