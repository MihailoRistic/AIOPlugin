package me.mfaith.aio.interact;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftVillager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import me.mfaith.aio.Main;

public class PlayerInteractListener implements Listener{
	private Main plugin;
	public PlayerInteractListener(Main plugin) {
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
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		if (e.getRightClicked() instanceof CraftVillager) {
			CraftVillager v = (CraftVillager) e.getRightClicked();
			String vf = "factions.villagers."+v.getUniqueId();
			if (plugin.getFactionConfig().contains(vf)) {
				if (plugin.getFactionConfig().getString(vf).matches(playerInFaction(p.getName(), plugin))){
					return;
				}
				if (plugin.getFactionConfig().getString(vf).matches("Default")){
					return;
				}
				e.setCancelled(true);
				p.sendMessage("<"+p.getName() +" u sebi> Mama mi je uvek govorila da ne pricam sa nepoznatim");
			} else {
				e.setCancelled(true);
				p.sendMessage("<"+p.getName() +" u sebi> Mama mi je uvek govorila da ne pricam sa nepoznatim");
			}
		}
	}
}
