package me.mfaith.aio.join;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import me.mfaith.aio.Main;
import me.mfaith.aio.utils.Utils;

public class JoinListener implements Listener {
	private Main plugin;

	public JoinListener(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	@EventHandler
    public void onPlayerPrelogin(AsyncPlayerPreLoginEvent event) throws ExecutionException, InterruptedException {
    	final Player player = Bukkit.getPlayer(event.getUniqueId());
        if (player != null) {
        	Bukkit.getScheduler().callSyncMethod(plugin, new Callable<Object>() {
        		public Object call() throws Exception {
        			player.kickPlayer("You logged in from another location");
                    return null;
                }
            }).get();
        }
    }

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		//plugin.raidEvent();
		
		
		Player p = e.getPlayer();
		if (p.hasPlayedBefore()) {
			e.setJoinMessage(
					Utils.chat(plugin.getConfig().getString("join_message").replace("<player>", p.getName())));
		} else {
			e.setJoinMessage(
					Utils.chat(plugin.getConfig().getString("firstJoin_message").replace("<player>", p.getName())));
		}if(p.getClientViewDistance() > Bukkit.getViewDistance()) {
			p.sendMessage("Render distance servera je" + Bukkit.getViewDistance() + 
					", a tvoj je " + p.getClientViewDistance() +
					", smanji ako zelis da smanjis lag");
		}
		plugin.updateSingleCurse(p.getName());
		plugin.saveFactionConfig();
	}
}
