package me.mfaith.aio.death;

import java.util.Date;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.mfaith.aio.Main;
import me.mfaith.aio.utils.Utils;

public class DeathListener implements Listener{
	private Main plugin;
	
	public DeathListener(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this,plugin);
		this.plugin=plugin;
	}
	
	@SuppressWarnings("deprecation")
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		Date now = new Date();
		now.setTime(now.getTime()+300000);
		e.setDeathMessage(Utils.chat(plugin.getConfig().getString("death_message").replace("<player>", p.getDisplayName())));
		if (!((plugin.getFactionConfig().contains("players."+p.getName()+".lastBan"))&&(plugin.getFactionConfig().getLong("players."+p.getName()+".lastBan")+600000>=now.getTime()))) {
			p.sendMessage("<&7&oPatrijarh Pavle&f> Imao si srece ovoga puta");
			plugin.getFactionConfig().set("players."+p.getName()+".lastBan", now.getTime());
		} else {
			p.sendMessage(Utils.chat("<&7&oPatrijarh Pavle&f> Odmori do " + now.getHours()+":"+now.getMinutes()+":"+now.getSeconds()));
			plugin.getFactionConfig().set("players."+p.getName()+".lastBan", now.getTime());
			Bukkit.getBanList(BanList.Type.NAME).addBan(p.getDisplayName(), Utils.chat("&fOnesvestio si se&f"), now, "AIO");
		}
		plugin.saveFactionConfig();
	}
}
