package org.cn2b2t.core.modules.gui;

import org.cn2b2t.core.modules.users.User;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class GUIItem {

	ItemStack display;
	boolean actionActive = true;

	public Set<GUIClickAction> actions = new HashSet<>();
	public Set<GUIClickAction> actionsIngoreActive = new HashSet<>();

	public GUIItem(ItemStack display) {
		this.display = display;
	}

	public final ItemStack getDisplay() {
		return this.display;
	}

	public final void setDisplay(ItemStack display) {
		this.display = display;
	}

	public final boolean isActionActive(){
		return this.actionActive;
	}

	public final void setActionActive(boolean b){
		actionActive = b;
	}

	@Deprecated
	public void ClickAction(ClickType type, User u) {

	}

	public void onClick(ClickType type){

	}

	public void addClickAction(GUIClickAction action){
		actions.add(action);
	}

	public void addActionIgnoreActive(GUIClickAction action){
		actionsIngoreActive.add(action);
	}

	public void customAction() {

	}

	public void rawClickAction(InventoryClickEvent event) {

	}

	public void realRawClickAction(InventoryClickEvent event) {

	}

	public void customAction(Object obj) {

	}

	public void customAction(User user) {

	}

	public abstract static class GUIClickAction{
		public abstract void run(ClickType type, User u);
	}

}
