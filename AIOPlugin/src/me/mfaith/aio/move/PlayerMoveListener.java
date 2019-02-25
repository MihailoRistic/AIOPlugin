package me.mfaith.aio.move;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World.Environment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.mfaith.aio.Main;

public class PlayerMoveListener implements Listener{
	private Main plugin;
	public PlayerMoveListener(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Chunk f = e.getFrom().getChunk();
		Chunk t = e.getTo().getChunk();
		String fs = "factions.chunks."+f.getX()+"_"+f.getZ();
		String ts = "factions.chunks."+t.getX()+"_"+t.getZ();
		if (plugin.getFactionConfig().contains("players."+e.getPlayer().getName()+".notifications.borders") && plugin.getFactionConfig().getString("players."+e.getPlayer().getName()+".notifications.borders").equalsIgnoreCase("true"))
			if(e.getPlayer().getWorld().getEnvironment() == Environment.NORMAL)
				if (f != t) {
					if (plugin.getFactionConfig().contains(fs)) {
						if (!plugin.getFactionConfig().contains(ts)) { //izlazi iz teritorije
							if (plugin.getFactionConfig().getString(fs).matches("Default")) {
								e.getPlayer().sendMessage("Izasao si iz glavnog sela");
							}else
							e.getPlayer().sendMessage("Izlazis iz teritorije \"" + plugin.getFactionConfig().getString(fs) + "\"");
						}
					} else {
						if (plugin.getFactionConfig().contains(ts)) { //ulazi u teritoriju
							if (plugin.getFactionConfig().getString(ts).matches("Default")) {
								e.getPlayer().sendMessage("Dosao si do glavnog sela");
							}else
							e.getPlayer().sendMessage("Usao si u terituriju \"" + plugin.getFactionConfig().getString(ts) + "\"");
						}
					}
				}
	}
}
