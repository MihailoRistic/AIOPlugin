package me.mfaith.aio.disconnect;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.mfaith.aio.Main;
import me.mfaith.aio.utils.Utils;

public class DisconnectListener implements Listener{
private Main plugin;
	
	public DisconnectListener(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this,plugin);
		this.plugin=plugin;
	}
	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent e) {
		e.setQuitMessage(Utils.chat(plugin.getConfig().getString("disconnect_message").replace("<player>", e.getPlayer().getName())));
	}
}
