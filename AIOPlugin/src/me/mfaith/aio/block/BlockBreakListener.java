package me.mfaith.aio.block;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.mfaith.aio.Main;

public class BlockBreakListener implements Listener{
	
	public BlockBreakListener(Main plugin) {
		Bukkit.getPluginManager().registerEvents(this,plugin);
	}
	
	private int getDropCount(int base, int fortuneLevel){
		Random random = new Random();
	    if ((fortuneLevel > 0))
	    {
	        int drops = random.nextInt(fortuneLevel + 2) - 1;
	        if (drops < 0)
	        {
	            drops = 0;
	        }
	        return base * (drops + 1);
	    }
	    return base;
	}
	
	private int getFortuneLvl(Player p) {
		return p.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
	}
	
	
	@EventHandler
	public void onBreakBlock(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (p.getGameMode()==GameMode.SURVIVAL) {
			Block b = e.getBlock();
			ItemStack hand = p.getInventory().getItemInMainHand();
			boolean ironTool = (hand.getType().equals(Material.DIAMOND_PICKAXE) || hand.getType().equals(Material.IRON_PICKAXE) || hand.getType().equals(Material.GOLDEN_PICKAXE));
			boolean stoneTool = (ironTool || hand.getType().equals(Material.STONE_PICKAXE));
			boolean woodenTool = (stoneTool || hand.getType().equals(Material.WOODEN_PICKAXE));
			if ((b.getBlockData().getMaterial().equals(Material.COAL_ORE)) && woodenTool) {
				e.setDropItems(false);
				e.setExpToDrop(e.getExpToDrop()-1);
				Bukkit.getWorld(b.getWorld().getName()).dropItem(b.getLocation(), new ItemStack(Material.COAL_ORE, getDropCount(1,getFortuneLvl(p))));
			} else
			if ((b.getDrops().contains(new ItemStack(Material.IRON_ORE))) && stoneTool) {
				e.setDropItems(false);
				e.setExpToDrop(e.getExpToDrop()-1);
				Bukkit.getWorld(b.getWorld().getName()).dropItem(b.getLocation(), new ItemStack(Material.IRON_ORE, getDropCount(1,getFortuneLvl(p))));
			} else
			if ((b.getDrops().contains(new ItemStack(Material.REDSTONE))) && ironTool) {
				e.setDropItems(false);
				e.setExpToDrop(e.getExpToDrop()-1);
				Bukkit.getWorld(b.getWorld().getName()).dropItem(b.getLocation(), new ItemStack(Material.REDSTONE_ORE, getDropCount(1 ,getFortuneLvl(p))+3));
			} else
			if ((b.getDrops().contains(new ItemStack(Material.LAPIS_LAZULI))) && stoneTool) {
				e.setDropItems(false);
				Random r = new Random();
				e.setExpToDrop(e.getExpToDrop()-1);
				Bukkit.getWorld(b.getWorld().getName()).dropItem(b.getLocation(), new ItemStack(Material.LAPIS_ORE, getDropCount(4+r.nextInt(5) ,getFortuneLvl(p))));
			} else
			if ((b.getDrops().contains(new ItemStack(Material.GOLD_ORE))) && ironTool) {
				e.setDropItems(false);
				e.setExpToDrop(e.getExpToDrop()-1);
				Bukkit.getWorld(b.getWorld().getName()).dropItem(b.getLocation(), new ItemStack(Material.GOLD_ORE, getDropCount(1,getFortuneLvl(p))));
			} else
			if ((b.getDrops().contains(new ItemStack(Material.EMERALD))) && ironTool) {
					e.setDropItems(false);
					e.setExpToDrop(e.getExpToDrop()-1);
					Bukkit.getWorld(b.getWorld().getName()).dropItem(b.getLocation(), new ItemStack(Material.EMERALD_ORE, getDropCount(1,getFortuneLvl(p))));
			} else
			if ((b.getDrops().contains(new ItemStack(Material.QUARTZ))) && woodenTool) {
				e.setDropItems(false);
				e.setExpToDrop(e.getExpToDrop()-1);
				Bukkit.getWorld(b.getWorld().getName()).dropItem(b.getLocation(), new ItemStack(Material.NETHER_QUARTZ_ORE, getDropCount(1,getFortuneLvl(p))));
			}else
			if ((b.getDrops().contains(new ItemStack(Material.DIAMOND))) && ironTool) {
				e.setDropItems(false);
				e.setExpToDrop(e.getExpToDrop()-1);
				Bukkit.getWorld(b.getWorld().getName()).dropItem(b.getLocation(), new ItemStack(Material.DIAMOND_ORE, getDropCount(1,getFortuneLvl(p))));
			}
		}
	}
}
