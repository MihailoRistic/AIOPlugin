package me.mfaith.aio.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.mfaith.aio.Main;

public class CurseListener implements Listener {

	public CurseListener(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this,plugin);
	}
    @EventHandler
    public void LockInv(InventoryClickEvent e) {
        ItemStack clicked = e.getCurrentItem();
        if(clicked != null && clicked.getType() == Material.BARRIER) {
            e.setCancelled(true);
        }
    }
}