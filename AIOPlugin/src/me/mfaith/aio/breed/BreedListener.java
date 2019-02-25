package me.mfaith.aio.breed;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftVillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;

import me.mfaith.aio.Main;

public class BreedListener implements Listener{
	private Main plugin;
	public BreedListener(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this,plugin);
		this.plugin=plugin;
	}
	
	private String VillagerInFaction(CraftVillager v, Main plugin) {
		if (plugin.getFactionConfig().contains("factions.villagers."+v.getUniqueId()))
			return plugin.getFactionConfig().getString("factions.villagers."+v.getUniqueId());
		else return null;
	}

	@EventHandler
	public void OnBreed(EntityBreedEvent e) {
		if (e.getEntity() instanceof CraftVillager) {
			CraftVillager v = (CraftVillager) e.getEntity();
			CraftVillager m = (CraftVillager) e.getMother();
			CraftVillager f = (CraftVillager) e.getFather();
			String vf = VillagerInFaction(m, plugin);
			if (vf==null) {
				vf=VillagerInFaction(f, plugin);
				if (vf!=null) {
					plugin.getFactionConfig().set("factions."+vf+".NoV", (plugin.getFactionConfig().getInt("factions."+vf+".NoV")+2));
					plugin.getFactionConfig().set("factions.villagers."+v.getUniqueId(), vf);
					plugin.getFactionConfig().set("factions.villagers."+m.getUniqueId(), vf);
				}
			}else {
				if (VillagerInFaction(f, plugin)!=null) {
					plugin.getFactionConfig().set("factions."+vf+".NoV", (plugin.getFactionConfig().getInt("factions."+vf+".NoV")+1));
					plugin.getFactionConfig().set("factions.villagers."+v.getUniqueId(), vf);
				} else {
					plugin.getFactionConfig().set("factions."+vf+".NoV", (plugin.getFactionConfig().getInt("factions."+vf+".NoV")+2));
					plugin.getFactionConfig().set("factions.villagers."+v.getUniqueId(), vf);
					plugin.getFactionConfig().set("factions.villagers."+f.getUniqueId(), vf);
				}
			}
			plugin.updateCurse();
			plugin.saveFactionConfig();
		}
		plugin.saveFactionConfig();
	}
}
