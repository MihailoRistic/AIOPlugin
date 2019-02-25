package me.mfaith.aio.chat;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.mfaith.aio.Main;

public class ChatListener implements Listener{
	private Main plugin;
	public ChatListener(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	private String playerInFaction(String p, Main plugin) {
		if(plugin.getFactionConfig().contains("players."+p+".faction")) {
			return plugin.getFactionConfig().getString("players."+p+".faction");
		}
		return null;
	}
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		e.setMessage("["+playerInFaction(e.getPlayer().getName(),plugin)+"] "+e.getPlayer().getName()+":"+e.getMessage());
	}
}
