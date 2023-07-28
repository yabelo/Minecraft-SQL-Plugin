package Main;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import Commands.Login;
import Commands.Register;
import Listener.PlayerJoin;
import Listener.PlayerMove;
import Listener.PlayerQuit;

public class Main extends JavaPlugin {
	public Connection connection;
	private String databaseName = "Users.db";

	@Override
	public void onEnable() {
		createDatabaseFile();
		connectToDatabase();
		createTable();
		
		getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
		getServer().getPluginManager().registerEvents(new PlayerQuit(this), this);
		getServer().getPluginManager().registerEvents(new PlayerMove(this), this);
		
		getCommand("register").setExecutor(new Register(this));
		getCommand("login").setExecutor(new Login(this));
	}

	@Override
	public void onDisable() {
		disconnectFromDatabase();

	}

	private void createDatabaseFile() {
		File dataFolder = getDataFolder();
		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
		}
	}

	private void connectToDatabase() {
		try {

			Class.forName("org.sqlite.JDBC");

			String url = "jdbc:sqlite:" + getDataFolder().getAbsolutePath() + File.separator + databaseName;
			connection = DriverManager.getConnection(url);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	private void disconnectFromDatabase() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void createTable() {
	    try {
	        String query = "CREATE TABLE IF NOT EXISTS players ("
	                + "uuid TEXT PRIMARY KEY,"
	                + "password TEXT,"
	                + "online INTEGER CHECK (online IN (0, 1)) DEFAULT 0)";
	        try (Statement statement = connection.createStatement()) {
	            statement.execute(query);
	        }
	    } catch (SQLException e) {
	        getLogger().severe("Error creating table: " + e.getMessage());
	        e.printStackTrace();
	    }
	}


	public void executeQuery(String query) {
		try (Statement statement = connection.createStatement()) {
			statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isUUIDInDatabase(UUID uuid) {
		String query = "SELECT * FROM players WHERE uuid=?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, uuid.toString());
			ResultSet resultSet = statement.executeQuery();
			return resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void setPasswordForUUID(UUID uuid, String password) {
	    String query = "INSERT OR REPLACE INTO players (uuid, password, online) VALUES (?, ?, ?)";
	    try (PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setString(1, uuid.toString());
	        statement.setString(2, password);
	        statement.setInt(3, 1);
	        statement.executeUpdate();
	    } catch (SQLException e) {
	        getLogger().severe("Error while setting password in the database: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	
	public String getPasswordForUUID(UUID uuid) {
        String query = "SELECT password FROM players WHERE uuid=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password");
            }
        } catch (SQLException e) {
            getLogger().severe("Error while getting password from the database: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
	
	public void setOnlineStatus(UUID uuid, boolean isOnline) {
	    String query = "UPDATE players SET online = ? WHERE uuid = ?";
	    try (PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setInt(1, isOnline ? 1 : 0);
	        statement.setString(2, uuid.toString());
	        statement.executeUpdate();
	    } catch (SQLException e) {
	        getLogger().severe("Error while updating online status in the database: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

}
