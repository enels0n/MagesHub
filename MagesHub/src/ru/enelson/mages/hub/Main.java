package ru.enelson.mages.hub;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.ChatColor;

public class Main extends org.bukkit.plugin.java.JavaPlugin implements Listener {
	
	public static FileConfiguration ConfigMain;
	File FileConfig;
	static Plugin plugin;
	
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		FileConfig = new File(getDataFolder(), "config.yml");
		if(!FileConfig.exists()) saveResource("config.yml", true);
		ConfigMain = YamlConfiguration.loadConfiguration(FileConfig);
		plugin = this;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("lobby") && args.length > 0 && args[0].equalsIgnoreCase("reload") && (sender.hasPermission("mageshub.reload") || sender.isOp())) {
			ConfigMain = YamlConfiguration.loadConfiguration(FileConfig);
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&',ConfigMain.getString("messages.reload")));
		}
		else if (command.getName().equalsIgnoreCase("lobby")) {
			Player p = (Player)sender;
			toHub(p);
		}
		return false;
	}
	
	public static void toHub(Player p) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(ConfigMain.getString("lobbyserver"));
		p.sendPluginMessage((org.bukkit.plugin.Plugin) Main.plugin, "BungeeCord", out.toByteArray());
	}
}
