package me.mfaith.aio.chat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftVillager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import me.mfaith.aio.Main;

public class TCommand implements CommandExecutor {
	private Main plugin;
	public TCommand(Main plugin) {
		this.plugin=plugin;
		plugin.getCommand("t").setExecutor(this);
	}
	
	private static int NoV=0;
	private static int getNoV() {
		return NoV;
	}	
	private static void setNoV() {
		NoV++;
	}
	private static void resetNoV() {
		NoV=0;
	}
		
	private static void getEntitiesAroundPoint(Location location, int radius, List<String> entities, List<Entity> ents) {
	    for (Entity entity : ents) {
	        if ((entity instanceof CraftVillager) && (entity.getLocation().distanceSquared(location) <= radius * radius) && (!entities.contains(entity.getUniqueId()+""))) {
	        	setNoV();
			    entities.add(entity.getUniqueId()+"");
		        getEntitiesAroundPoint(entity.getLocation(), 65, entities, ents);
	        }
	    }
	}
	
	private static void getChunksAroundEntites(Location location, int radius, List<Entity> entities, List<Chunk> chunks, List<Entity> ents) {
	    for (Entity entity : ents) {
	    	if ((entity instanceof CraftVillager) && (entity.getLocation().distanceSquared(location) <= radius * radius)) {
	        	int x=entity.getLocation().getChunk().getX(),z=entity.getLocation().getChunk().getZ();
	        	for (int i=-1;i<2;i++) {
	        		for (int j=-1;j<2;j++) {
	        			Chunk currentChunk = entity.getWorld().getChunkAt(x+i, z+j);
	        			if (!chunks.contains(currentChunk)) {
	        				chunks.add(currentChunk);
	        			}
	        		}
	        	}
	        	if (!entities.contains(entity)) {
			        entities.add(entity);
		        	getChunksAroundEntites(entity.getLocation(), 65,entities, chunks, ents);
	        	}
	        }
	    }
	}
	
	private static List<String> getRSpawnChunks(List<Chunk> chunks, Main plugin){
		if (chunks==null) return null;
		else {
			List<String> schunks = new ArrayList<String>();
			for (Chunk chunk : chunks) {
				int cx=chunk.getX(), cz=chunk.getZ();
				FileConfiguration fc=plugin.getFactionConfig();
				if (!fc.contains("factions.chunks."+(cx+1)+"_"+cz) && !schunks.contains((cx+1)+"_"+cz)) {
					schunks.add((cx+1)+"_"+cz);
					}
				if (!fc.contains("factions.chunks."+(cx-1)+"_"+cz) && !schunks.contains((cx-1)+"_"+cz)) {
					schunks.add((cx-1)+"_"+cz);
					}
				if (!fc.contains("factions.chunks."+cx+"_"+(cz+1)) && !schunks.contains((cx+"_"+(cz+1)))) {
					schunks.add((cx+"_"+(cz+1)));
					}
				if (!fc.contains("factions.chunks."+cx+"_"+(cz-1)) && !schunks.contains((cx+"_"+(cz-1)))) {
					schunks.add((cx+"_"+(cz-1)));
					}
				}
			return schunks;
			}
	}

	private String playerInFaction(String p, Main plugin) {
		if(plugin.getFactionConfig().contains("players."+p+".faction")) {
			return plugin.getFactionConfig().getString("players."+p+".faction");
		}
		return null;
	}
	
	@EventHandler
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player)sender;
			if (args.length==0) {
				if (playerInFaction(p.getName(), plugin) != null) {
					p.sendMessage("["+playerInFaction(p.getName(), plugin)+"]:");
					p.sendMessage("     /t claim");
					p.sendMessage("     /t invite");
					p.sendMessage("     /t notifications");
					p.sendMessage("     /t leave");
				}
				else {
					p.sendMessage("[T]: /t create");
					p.sendMessage("     /t join");
					p.sendMessage("     /t notifications");
				}
				
				
			} else if (args[0].equalsIgnoreCase("create")) {
				if (args.length>1) {
					String s="";
					for (int i=1;i<args.length;i++) {
						if (i==1) s=args[1];
						else
						s= s + " " + args[i];
					}
					if (playerInFaction(p.getName(), plugin) != null){
						p.sendMessage("[T]: Clan si kraljevstva: "+playerInFaction(p.getName(), plugin));
					}
					else if (plugin.getFactionConfig().contains("factions."+s)) {
						p.sendMessage("[T]: Vec postoji " + s);
					}
					else {
						plugin.getFactionConfig().set(("players."+p.getName()+".faction"), s);
						plugin.getFactionConfig().set("factions." + s + ".members", p.getName());
						p.sendMessage("[T]: Uspesno napravljeno kraljevstvo: " + s + "");
					}
				}
				else {
					if (playerInFaction(p.getName(), plugin) == null) {
						p.sendMessage("[T]: /t create <Name>");
					}
					else p.sendMessage("[T]: Clan si kraljevstva: "+playerInFaction(p.getName(), plugin));
				}
				
				
			} else if (args[0].equalsIgnoreCase("claim")) {
				
				if (playerInFaction(p.getName(),plugin)!=null) {
					resetNoV();
					List<String> villagers = new ArrayList<String>();
					getEntitiesAroundPoint(p.getLocation(), 65, villagers, p.getWorld().getEntities());
					for(String villager : villagers) {
						if ((plugin.getFactionConfig().contains("factions.villagers."+villager))&&(!plugin.getFactionConfig().getString("factions.villagers."+villager).equals(playerInFaction(p.getName(), plugin)))) {
							p.sendMessage("Teritorija je claimovana");
							return true;
						}
					}
					for(String villager : villagers) {
						plugin.getFactionConfig().set("factions.villagers."+villager, playerInFaction(p.getName(), plugin));
					}
					plugin.getFactionConfig().set("factions."+playerInFaction(p.getName(), plugin)+".NoV", getNoV());
					List<Chunk> chunks = new ArrayList<Chunk>();
					getChunksAroundEntites(p.getLocation(), 65, new ArrayList<Entity>(), chunks, p.getWorld().getEntities());
					for(Chunk chunk : chunks) {
						plugin.getFactionConfig().set("factions.chunks."+chunk.getX()+"_"+chunk.getZ(), playerInFaction(p.getName(), plugin));
					}
					List<String> rspawnchunks=getRSpawnChunks(chunks, plugin);
					for(String chunk : rspawnchunks)
						plugin.getRaidConfig().set(playerInFaction(p.getName(), plugin)+".chunks."+chunk, true);
					
					p.sendMessage("["+playerInFaction(p.getName(), plugin)+"]: Uspesno osvajanje teritorije!");
					
				} else p.sendMessage("[T]: Moras biti clan kraljevstva da bih to uradio");
				
				
			} else if (args[0].equalsIgnoreCase("invite")) {
				if(playerInFaction(p.getName(), plugin)!=null) {
					if(args.length>1) {
						if (plugin.getFactionConfig().contains("players."+args[1])) {
							plugin.getFactionConfig().set("players."+args[1]+".invites", playerInFaction(p.getName(), plugin));
							p.sendMessage("Uspesno pozvan "+args[1]);
						}else p.sendMessage("Igrac ne postoji");
					}else p.sendMessage("["+playerInFaction(p.getName(), plugin)+"]: /t invite <Player Name>");
				}else p.sendMessage("[T]: Moras biti clan kraljevstva da bih to uradio");
				
			}else if (args[0].equalsIgnoreCase("join")) {
				if(playerInFaction(p.getName(), plugin)==null) {
					if(args.length>1) {
						String s="";
						for (int i=1;i<args.length;i++) {
							if (i==1) s=args[1];
							else
							s= s + " " + args[i];
						}
						if (plugin.getFactionConfig().contains("players."+p.getName()+".invites") && plugin.getFactionConfig().getString("players."+p.getName()+".invites").matches(s)) {
							plugin.getFactionConfig().set(("players."+p.getName()+".faction"), s);
							plugin.getFactionConfig().set("factions." + s + ".members", p.getName());
							p.sendMessage("Uspesno si usao u \""+s+"\"");
						}else {
							p.sendMessage("Nisi invitovan od strane \""+ s+"\", ili ne postoji");
						}
					}else p.sendMessage("["+playerInFaction(p.getName(), plugin)+"]: /t join <Faction Name>");
				}else p.sendMessage("[T]: Clan si kraljevstva: "+playerInFaction(p.getName(), plugin));
			}
			
			else if (args[0].equalsIgnoreCase("notifications")){
				if (args.length > 1) {
					if (args[1].equalsIgnoreCase("borders")) {
						if( (args.length>2) && (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false")) ) {
							plugin.getFactionConfig().set("players."+p.getName()+".notifications.borders", args[2]);
							if (args[2].equalsIgnoreCase("true")) p.sendMessage("Uspesno ukljucene border notifikacije");
							else p.sendMessage("Uspesno iskljucene border notifikacije");
						}else {
							p.sendMessage("[T]: /t notifications borders true");
							p.sendMessage("     /t notifications borders false");
						}
					}
				}else{
					p.sendMessage("[T]: /t notifications borders");
				}
				
			}else if (args[0].equalsIgnoreCase("leave")) {
				if(playerInFaction(p.getName(), plugin)!=null) {
					plugin.getFactionConfig().set(("players."+p.getName()+".faction"), null);
				}else p.sendMessage("[T]: Clan si kraljevstva: "+playerInFaction(p.getName(), plugin));
			}
			else return false;
			plugin.saveFactionConfig();
			plugin.saveRaidConfig();
		}
		else sender.sendMessage("Only players can use /t");
		return true;
	}
}
