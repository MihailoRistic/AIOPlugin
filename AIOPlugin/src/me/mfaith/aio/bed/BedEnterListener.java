package me.mfaith.aio.bed;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

import me.mfaith.aio.Main;

public class BedEnterListener implements Listener{
private Main plugin;
	
	public BedEnterListener(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this,plugin);
		this.plugin=plugin;
	}
	@EventHandler
	public void onPlayerEnterBed(PlayerBedEnterEvent e) {
		if (plugin.getRaiding()) {
			e.setCancelled(true);
			return;
		}
		Player p = e.getPlayer();
		Chunk bedC = e.getBed().getChunk();
		String path = "factions.chunks."+bedC.getX()+"_"+bedC.getZ();
		if (!plugin.getFactionConfig().contains(path)) {
			p.sendMessage("Ne mozes spavati u divljini!");
			e.setCancelled(true);
		} else if (!plugin.getFactionConfig().getString(path).equals(plugin.getFactionConfig().getString("players."+p.getName()+".faction"))) {
			if (!plugin.getFactionConfig().getString(path).equals("Default")) {
				p.sendMessage("Ne mozes spavati u tudjoj teritoriji!");
				e.setCancelled(true);
			}
		}/*else {
			boolean b = true;
			for (Player ps : Bukkit.getOnlinePlayers()) {
				if (!p.isSleepingIgnored() && p.isSleeping()) {
					b = false;
					break;
				}
			}
			if b Bukkit.getServer().
		}*/
	}
}
