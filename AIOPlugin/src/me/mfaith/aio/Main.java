/*	TODO
 * Napraviti tree cutter sistem
 * pokusati popraviti bad package 27
 * Enchantovane strele + infinity
 * Uveti afk sistem (da mogu ostali da spavaju ako je taj neko afk)
 * Saznati kako uraditi curse zbog offline playera
*/
package me.mfaith.aio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.mfaith.aio.bed.BedEnterListener;
import me.mfaith.aio.breed.BreedListener;
import me.mfaith.aio.chat.ChatListener;
import me.mfaith.aio.chat.TCommand;
import me.mfaith.aio.death.DeathListener;
import me.mfaith.aio.death.EntityDeathListener;
import me.mfaith.aio.disconnect.DisconnectListener;
import me.mfaith.aio.interact.PlayerInteractListener;
import me.mfaith.aio.inventory.CurseListener;
import me.mfaith.aio.join.JoinListener;
import me.mfaith.aio.join.RespawnListener;
import me.mfaith.aio.move.PlayerMoveListener;
import me.mfaith.aio.takedamage.DamagedListener;
import me.mfaith.aio.utils.Utils;

public class Main extends JavaPlugin {
	private File FactionFile, RaidFile;
	private FileConfiguration fconfig, raidconfig;
	boolean raidb= false;
	List<String> zgroup= new ArrayList<String>();
	HashMap<String,Location> afklocs = new HashMap<String,Location>();
	
	@Override
	public void onEnable() {
		new TCommand(this);
		createFactionFiles();
		createRaidFiles();
		saveDefaultConfig();
		new JoinListener(this);
		new BedEnterListener(this);
		new DeathListener(this);
		new DamagedListener(this);
		new RespawnListener(this);
		new CurseListener(this);
		new BreedListener(this);
		new EntityDeathListener(this);
		new DisconnectListener(this);
		//new BlockBreakListener(this);
		new PlayerMoveListener(this);
		new PlayerInteractListener(this);
		new ChatListener(this);
		
		//RENEW TIMER
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				Bukkit.broadcastMessage(Utils.chat("&7[Server Notification]: Osvezite me!"));
			}
	    }, 216000L, 216000L);
		
		
		//AFK TIMER
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (afklocs.get(p.getName()) != null) {
						if (afklocs.get(p.getName())==p.getLocation()) {
							p.setSleepingIgnored(true);
						}p.setSleepingIgnored(false);
					}
					afklocs.put(p.getName(),p.getLocation());
				}
			}
	    }, 1200L, 1200L);
	}
	
	public void updateCurse() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			String pName = p.getName();
			this.getFactionConfig().set("players."+pName+".curse", 18-(this.getFactionConfig().getInt("factions.Default.NoV")/2));
			for(int i=0;18>i;i++) {
				if (this.getFactionConfig().getInt("players."+pName+".curse")>i){
					ItemStack item = p.getInventory().getItem(i+9);
					if (item.getType() != Material.BARRIER) {
						p.getInventory().setItem(i+9, new ItemStack(Material.BARRIER));
						p.updateInventory();
						p.getWorld().dropItem(p.getLocation(), item);
					}
				} else
					if (p.getInventory().getItem(i+9).getType() == Material.BARRIER) {
						p.getInventory().setItem(i+9, null);
						p.updateInventory();
					}
			}
		}
		this.saveFactionConfig();
	}
	
	public void updateSingleCurse(String pName) {
		Player p = Bukkit.getServer().getPlayer(pName);
		this.getFactionConfig().set("players."+pName+".curse", 18-(this.getFactionConfig().getInt("factions.Default.NoV")/2));
		for(int i=0;18>i;i++) {
			if (this.getFactionConfig().getInt("players."+pName+".curse")>i){
				ItemStack item = p.getInventory().getItem(i+9);
				if (item.getType() != Material.BARRIER) {
					p.getInventory().setItem(i+9, new ItemStack(Material.BARRIER));
					p.updateInventory();
					p.getWorld().dropItem(p.getLocation(), item);
				}
			}else if (p.getInventory().getItem(i+9).getType() == Material.BARRIER) {
				p.getInventory().setItem(i+9, null);
				p.updateInventory();
			}
		}
		this.saveFactionConfig();
	}
	
	public FileConfiguration getFactionConfig() {
        return this.fconfig;
    }
	
	public FileConfiguration getRaidConfig() {
        return this.raidconfig;
    }
	
	public void saveFactionConfig() {
		if (fconfig == null || FactionFile == null) {
	        return;
	    }
	    try {
	    	getFactionConfig().save(FactionFile);
	    } catch (IOException ex) {
	        getLogger().log(Level.SEVERE, "Could not save config to " + FactionFile, ex);
	    }
	}	
	
	public void saveRaidConfig() {
		if (raidconfig == null || RaidFile == null) {
	        return;
	    }
	    try {
	    	getRaidConfig().save(RaidFile);
	    } catch (IOException ex) {
	        getLogger().log(Level.SEVERE, "Could not save config to " + FactionFile, ex);
	    }
	}	
	
	public void createRaidFiles() {
		RaidFile = new File(getDataFolder(), "raid.yml");
	    if (!RaidFile.exists()) {
	    	RaidFile.getParentFile().mkdirs();
	    	saveResource("raid.yml", false);
	    }

	    raidconfig= new YamlConfiguration();
	    try {
	    	raidconfig.load(RaidFile);
	    } catch (IOException | InvalidConfigurationException e) {
	        e.printStackTrace();
	    }
	}
	
	public void createFactionFiles() {
		FactionFile = new File(getDataFolder(), "f.yml");
	    if (!FactionFile.exists()) {
	    	FactionFile.getParentFile().mkdirs();
	    	saveResource("f.yml", false);
	    }

	    fconfig= new YamlConfiguration();
	    try {
	    	fconfig.load(FactionFile);
	    } catch (IOException | InvalidConfigurationException e) {
	        e.printStackTrace();
	    }
	}
	private int getChunkHighestBlock(int x, int z) {
		int max = 0;
		x *= 16;
		z *= 16;
		for (int y = 255; y > 1; y--) {
			if (Bukkit.getWorld("world").getBlockAt(x + 8, y, z + 8).getBlockData().getMaterial().isSolid())
				max = y;
		}
		for (int y = 255; y > 1; y--) {
			if (Bukkit.getWorld("world").getBlockAt(x + 9, y, z + 8).getBlockData().getMaterial().isSolid())
				if (max < y)
					max = y;
		}
		for (int y = 255; y > 1; y--) {
			if (Bukkit.getWorld("world").getBlockAt(x + 8, y, z + 9).getBlockData().getMaterial().isSolid())
				if (max < y)
					max = y;
		}
		for (int y = 255; y > 1; y--) {
			if (Bukkit.getWorld("world").getBlockAt(x + 9, y, z + 9).getBlockData().getMaterial().isSolid())
				if (max < y)
					max = y;
		}
		return max + 1;
	}
	

	public List<String> getZombies() {
		return zgroup;
	}
	
	public void addZombie(String zombie) {
		this.zgroup.add(zombie);
	}
	
	public void removeZombie(String zombie) {
		this.zgroup.remove(zombie);
	}
	
	public boolean getRaiding() {
		return raidb;
	}
	
	public void setRaiding(boolean bool) {
		this.raidb=bool;
	}
	
	private String victim;
	private int now = 0, raid = 0;

	private void addNoW() {
		now++;
	}

	public int getNoW() {
		return now;
	}

	private String getVictim() {
		return victim;
	}

	private int getRaid() {
		return raid;
	}

	private void setRaid(int raid) {
		this.raid = raid;
	}

	private void setVictim(String victim) {
		this.victim = victim;
	}

	private void ChooseRandom() {
		int RP = new Random().nextInt(Bukkit.getOnlinePlayers().size());
		int i = 0;
		for (Player p : getServer().getOnlinePlayers()) {
			if (RP == i) {
				if (getFactionConfig().contains("players." + p.getName() + ".faction")) {
					setVictim(getFactionConfig().getString("players." + p.getName() + ".faction"));
					break;
				} else {
					ChooseRandom();
					break;
				}
			}
			i++;
		}
	}
	

	public void raidEvent() {
		if (Bukkit.getOnlinePlayers().size()>=1) {
			if(!getRaidConfig().contains("lastRaid") || getRaidConfig().getLong("lastRaid")+86400000<=new Date().getTime()) {
				getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
					@Override
		            public void run() {
		                if (Bukkit.getOnlinePlayers().size()>=1) {
		            		if(!getRaidConfig().contains("lastRaid") || getRaidConfig().getLong("lastRaid")+86400000<=new Date().getTime()) {
		        				getRaidConfig().set("lastRaid", new Date().getTime());
		            			if(!(getFactionConfig().contains("factions.Default.raided") && getFactionConfig().getBoolean("factions.Default.raided")!=true)) {
		            				setVictim("Default");
	            					Bukkit.broadcastMessage("<Villager iz glavnog sela> Boze pomozi, sta sam bogu zgresio pa da nas napadnu ovako!");
	                    		} else {
	                    			ChooseRandom();
	                    			for(String pName : getFactionConfig().getStringList("factions."+getVictim()+".players")) {
	                    				Player p = getServer().getPlayer(pName);
	                    				p.sendMessage("<Villager iz tvog sela> Sta je ovo, boze pomozi!");
	                    			}
	                    		}
		            			now=0;
		            			setRaiding(true);
	            				setRaid(getServer().getScheduler().scheduleSyncRepeatingTask(Main.this, new Runnable() {
	            	            	@Override
	            	            	public void run() {
	            	            		if(getNoW()<6) {
	            	            			if (getZombies().size()==0) {
		        		            			Bukkit.broadcastMessage("<Villager> Evo ih!");
				            	            	addNoW();
				            	            	
				            	           		for(String chstring : getRaidConfig().getKeys(true)) {
				            	           			if (chstring.length()>(getVictim()+".chunks.").length()) {
				    	                       			String[] test = chstring.split("\\_");
				    		                       		String[] test1 = test[0].split("\\.");
				    		                       		for (int i=0; getNoW()>i; i++) { 
					    		                       		Zombie zombie = (Zombie) getServer().getWorld("world").spawnEntity(new Location(getServer().getWorld("world"), Integer.parseInt(test1[2])*16+8.5, getChunkHighestBlock(Integer.parseInt(test1[2]), Integer.parseInt(test[1])), Integer.parseInt(test[1])*16+8.5), EntityType.ZOMBIE);
					    		                       		zombie.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
					    		                       		zombie.getEquipment().setHelmetDropChance(0);
					    		                       		zombie.setRemoveWhenFarAway(false);
					    		                       		addZombie(""+zombie.getUniqueId());
				    		                       		}
				    	                       		}
				                       			}
			            	           		}
		            	            	}else if (getRaid()!=0){
		            	            		Bukkit.broadcastMessage("<Villager> Pobegli su!");
		            	            		
		            	            		setRaiding(false);
		            	            		getRaidConfig().set("lastRaid", new Date().getTime());
		            	    				saveRaidConfig();
		            	            		getServer().getScheduler().cancelTask(getRaid());
		            	            	}
	            	            	}
	            	        	}, 240L, 120L));
	                		}
	                	}
	            	}
	        	}, 12000L, 12000L);
			}
		}
	}
}
