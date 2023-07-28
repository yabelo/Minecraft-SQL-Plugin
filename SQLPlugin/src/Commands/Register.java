package Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Main.Main;
import Utils.Utils;
import Vars.Var;

public class Register implements CommandExecutor{
	
	public Main main;

	public Register(Main main) {
		super();
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if(!(sender instanceof Player)) return false;
		Player player = (Player) sender;
		
		String usage = Utils.chat("&cUsage: /register <password>");
		
		if(args.length == 1) {
			if(main.isUUIDInDatabase(player.getUniqueId())) {
				player.sendMessage(Utils.chat("&cYou already registered. \nUsage: /login <password>"));
				return false;
			}
			else {
				try {
					String password = args[0];
					if(password.length() >= 16 || password.length() <= 3) {
						player.sendMessage(Utils.chat("&cTry a different password that reaches this:\n - 3 < password length < 16."));
						return false;
					}
					main.setPasswordForUUID(player.getUniqueId(), password);
					player.sendMessage(Utils.chat("&dYou registered to the server! congrats.\nDon't forget your password for next time: " + password));
					Var.loggedInPlayers.add(player.getUniqueId());

				}
				catch (Exception e) {
					player.sendMessage(Utils.chat("&cCannot register you into server."));
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
