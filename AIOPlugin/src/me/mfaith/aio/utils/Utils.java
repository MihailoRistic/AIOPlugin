package me.mfaith.aio.utils;

import java.util.Random;

import org.bukkit.ChatColor;

public class Utils {

	public static String chat(String s, String ...args) {
		int i=s.indexOf("<{",0);
		int x=0;
		while (i!=-1 && x<args.length){
			j=s.indexOf("}>",i);
			s.replace(s.subSequence(i,j), args[x]);
			x++;
			i=s.indexOf("<{",i);
		}
		if (x=args.length){
			return ChatColor.translateAlternateColorCodes('&',s);
		} else {
			return (ChatColor.translateAlternateColorCodes('&',s) + "Invalid argument amount, inputed "+args.length+", needed x");
		}
	}
	
	public static String line(String ...s) {
		Random string = new Random();
		return s[string.nextInt(s.length)];
	}
}
