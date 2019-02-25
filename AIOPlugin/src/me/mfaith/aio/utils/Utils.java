package me.mfaith.aio.utils;

import java.util.Random;

import org.bukkit.ChatColor;

public class Utils {

	public static String chat(String s) {
		return ChatColor.translateAlternateColorCodes('&',s);
	}
	
	public static String line(String ...s) {
		Random string = new Random();
		return s[string.nextInt(s.length)];
	}
}
