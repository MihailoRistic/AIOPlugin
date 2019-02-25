package me.mfaith.aio.takedamage;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.mfaith.aio.Main;
import me.mfaith.aio.utils.Utils;

public class DamagedListener implements Listener{
private Main plugin;
	
	public DamagedListener(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this,plugin);
		this.plugin=plugin;
	}
	@EventHandler
	public void onPlayerDamaged(final EntityDamageEvent event) {
		Entity e=event.getEntity();
		if(e instanceof Player) {
			Player p=(Player)e;
			if (event.getCause() == DamageCause.FALL) {
				if ((p.getHealth()-event.getFinalDamage() <= 4) && (p.getHealth()-event.getFinalDamage() > 0)) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 10, false, false));
					p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 129, true, true),true);
					Bukkit.broadcastMessage(Utils.chat(plugin.getConfig().getString("fall_message").replace("<player>", p.getDisplayName())));
				}
			}
			if (p.getHealth()-event.getFinalDamage()<=0) {
				String pName = p.getName();
				for(int i=0;18>i;i++) {
					if (plugin.getFactionConfig().getInt("players."+pName+".curse")>i){
						p.getInventory().setItem(i+9, null);
						p.updateInventory();
					}
				}
			}
		}
	}
}
