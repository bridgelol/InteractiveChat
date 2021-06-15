package com.loohp.interactivechat.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class RarityUtils {
	
	private static Class<?> craftItemStackClass;
	private static Class<?> nmsItemStackClass;
	private static Class<?> nmsEnumItemRarityClass;
	private static Class<?> nmsEnumChatFormatClass;
	private static Method asNMSCopyMethod;
	private static Method getItemRarityMethod;
	private static Field getItemRarityColorField;
	
	public static void setupRarity() {
		try {
			craftItemStackClass = NMSUtils.getNMSClass("org.bukkit.craftbukkit.%s.inventory.CraftItemStack");
			nmsItemStackClass = NMSUtils.getNMSClass("net.minecraft.server.%s.ItemStack", "net.minecraft.world.item.ItemStack");
			nmsEnumItemRarityClass = NMSUtils.getNMSClass("net.minecraft.server.%s.EnumItemRarity", "net.minecraft.world.item.EnumItemRarity");
			nmsEnumChatFormatClass = NMSUtils.getNMSClass("net.minecraft.server.%s.EnumChatFormat", "net.minecraft.EnumChatFormat");
			asNMSCopyMethod = craftItemStackClass.getMethod("asNMSCopy", ItemStack.class);
			getItemRarityMethod = Stream.of(nmsItemStackClass.getMethods()).filter(each -> each.getReturnType().equals(nmsEnumItemRarityClass)).findFirst().orElse(null);
			getItemRarityColorField = Stream.of(nmsEnumItemRarityClass.getFields()).filter(each -> each.getType().equals(nmsEnumChatFormatClass)).findFirst().orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public static ChatColor getRarityColor(ItemStack item) {
		ChatColor color = ChatColor.WHITE;
		if (!item.getType().equals(Material.AIR)) {
			if (item.hasItemMeta() && item.getItemMeta().hasEnchants()) {
				color = ChatColor.AQUA;
			}
			try {
				Object nmsItemStackObject = asNMSCopyMethod.invoke(null, item);
				Object nmsEnumItemRarityObject = getItemRarityMethod.invoke(nmsItemStackObject);
				Object nmsEnumChatFormatObject = getItemRarityColorField.get(nmsEnumItemRarityObject);
				String str = nmsEnumChatFormatObject.toString();
				color = ChatColor.getByChar(str.charAt(str.length() - 1));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		return color;
	}
	
	public static ChatColor getRarityColor(Material material) {
		ChatColor color = ChatColor.WHITE;
		if (!material.equals(Material.AIR)) {
			ItemStack item = new ItemStack(material);
			try {
				Object nmsItemStackObject = asNMSCopyMethod.invoke(null, item);
				Object nmsEnumItemRarityObject = getItemRarityMethod.invoke(nmsItemStackObject);
				Object nmsEnumChatFormatObject = getItemRarityColorField.get(nmsEnumItemRarityObject);
				String str = nmsEnumChatFormatObject.toString();
				color = ChatColor.getByChar(str.charAt(str.length() - 1));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return color;
	}
}
