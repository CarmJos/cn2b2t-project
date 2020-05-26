package org.cn2b2t.core.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemStackFactory {

	ItemStack item;

	private ItemStackFactory() {
	}

	public ItemStackFactory(ItemStack is) {
		this.item = is.clone();
	}

	public ItemStackFactory(int type) {
		this(Material.getMaterial(type), 1);
	}

	public ItemStackFactory(Material type) {
		this(type, 1);
	}

	public ItemStackFactory(Material type, int amount) {
		this(type, amount, (short) 0);
	}

	public ItemStackFactory(int type, int amount) {
		this(Material.getMaterial(type), amount, (short) 0);
	}

	public ItemStackFactory(Material type, int amount, short data) {
		this.item = new ItemStack(type, amount, data);
	}

	public ItemStackFactory(int type, int amount, short data) {
		this.item = new ItemStack(type, amount, data);
	}

	public ItemStackFactory(int type, int amount, int data) {
		this(type, amount, (short) data);
	}

	public ItemStackFactory(Material type, int amount, int data) {
		this(type, amount, (short) data);
	}

	public ItemStack toItemStack() {
		return this.item;
	}

	public ItemStackFactory setType(Material type) {
		this.item.setType(type);
		return this;
	}

	public ItemStackFactory setDurability(int i) {
		this.item.setDurability((short) i);
		return this;
	}

	public ItemStackFactory setAmount(int a) {
		this.item.setAmount(a);
		return this;
	}

	public ItemStackFactory setDisplayName(String name) {
		ItemMeta im = this.item.getItemMeta();
		im.setDisplayName(name.replace("&", "§").replace("§§", "&&"));
		this.item.setItemMeta(im);
		return this;
	}

	public ItemStackFactory setLore(List<String> lores) {
		ItemMeta im = this.item.getItemMeta();
		List<String> lores_ = new ArrayList();
		lores.stream().forEach((lore) -> {
			lores_.add(lore.replace("&", "§").replace("§§", "&&"));
		});
		im.setLore(lores_);
		this.item.setItemMeta(im);
		return this;
	}

	public ItemStackFactory addLore(String name) {
		ItemMeta im = this.item.getItemMeta();
		List<String> lores;
		if (im.hasLore()) {
			lores = im.getLore();
		} else {
			lores = new ArrayList<>();
		}
		lores.add(name.replace("&", "§").replace("§§", "&&"));
		im.setLore(lores);
		this.item.setItemMeta(im);
		return this;
	}

	public ItemStackFactory addEnchant(Enchantment ench, int level, boolean ignoreLevelRestriction) {
		ItemMeta im = this.item.getItemMeta();
		im.addEnchant(ench, level, ignoreLevelRestriction);
		this.item.setItemMeta(im);
		return this;
	}

	public ItemStackFactory removeEnchant(Enchantment ench) {
		ItemMeta im = this.item.getItemMeta();
		im.removeEnchant(ench);
		this.item.setItemMeta(im);
		return this;
	}

	public ItemStackFactory addFlag(ItemFlag flag) {
		ItemMeta im = this.item.getItemMeta();
		im.addItemFlags(flag);
		this.item.setItemMeta(im);
		return this;
	}

	public ItemStackFactory removeFlag(ItemFlag flag) {
		ItemMeta im = this.item.getItemMeta();
		im.removeItemFlags(flag);
		this.item.setItemMeta(im);
		return this;
	}

	public ItemStackFactory setSkullOwner(String name) {
		if (this.item.getType() == Material.SKULL_ITEM || this.item.getType() == Material.SKULL) {
			SkullMeta im = (SkullMeta) this.item.getItemMeta();
			im.setOwner(name);
			this.item.setItemMeta(im);
		}
		return this;
	}

}
