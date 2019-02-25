package me.mfaith.aio.death;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftVillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import me.mfaith.aio.Main;

public class EntityDeathListener implements Listener{
	private Main plugin;
		
	public EntityDeathListener(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this,plugin);
		this.plugin=plugin;
	}
	
	

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		if (e.getEntity() instanceof CraftVillager) {
			CraftVillager v= (CraftVillager) e.getEntity();
			if (plugin.getFactionConfig().contains("factions.villagers."+v.getUniqueId())) {
				if (plugin.getFactionConfig().getString("factions.villagers."+v.getUniqueId()).matches("Default")) {
					Bukkit.broadcastMessage("Umro villager iz Glavnog sela!");
				}else
					Bukkit.broadcastMessage("Umro villager iz \""+ plugin.getFactionConfig().getString("factions.villagers."+v.getUniqueId()) +"\" teritorije!");
				String f= plugin.getFactionConfig().getString("factions.villagers."+v.getUniqueId());
				plugin.getFactionConfig().set("factions."+f+".NoV",plugin.getFactionConfig().getInt("factions."+f+".NoV")-1);
				plugin.getFactionConfig().set("factions.villagers."+v.getUniqueId(), null);
				plugin.updateCurse();
				plugin.saveFactionConfig();
			}
		}/* else
		if (plugin.getRaiding()) {
			if (plugin.getZombies().contains(""+e.getEntity().getUniqueId())) {
				plugin.removeZombie(""+e.getEntity().getUniqueId());
			}
		}*/
	}
}
