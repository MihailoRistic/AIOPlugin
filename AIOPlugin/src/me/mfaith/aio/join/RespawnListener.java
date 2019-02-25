package me.mfaith.aio.join;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import me.mfaith.aio.Main;

public class RespawnListener implements Listener{
private Main plugin;
	
	public RespawnListener(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this,plugin);
		this.plugin=plugin;
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		if(p.isBanned()) p.kickPlayer("Onesvestio si se");
		
		String pName = p.getName();
		for(int i=0;18>i;i++) {
			if (plugin.getFactionConfig().getInt("players."+pName+".curse")>i){
				p.getInventory().setItem(i+9, new ItemStack(Material.BARRIER));
				p.updateInventory();
			}
		}
		
	}
}
