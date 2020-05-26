package org.cn2b2t.core.modules.gui;

import org.cn2b2t.core.Main;
import org.cn2b2t.core.modules.users.User;
import org.cn2b2t.core.managers.utils.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUI {

	GUIType type;
	String name;
	public GUIItem[] items;
	public Inventory inv;

	boolean setCancelledIfClickOnTarget = true;
	boolean setCancelledIfClickOnSelf = true;
	boolean setCancelledIfClickOnOuter = true;

	Map<String,Object> flags;

	Listener listener;

	public GUI(GUIType type, String name) {
		this.type = type;
		this.name = name;
		switch (type) {
			case ONEBYNINE:
				this.items = new GUIItem[9];
				break;
			case TWOBYNINE:
				this.items = new GUIItem[18];
				break;
			case THREEBYNINE:
				this.items = new GUIItem[27];
				break;
			case FOURBYNINE:
				this.items = new GUIItem[36];
				break;
			case FIVEBYNINE:
				this.items = new GUIItem[45];
				break;
			default:
			case SIXBYNINE:
				this.items = new GUIItem[54];
				break;

			case HOPPER:
				this.items = new GUIItem[InventoryType.HOPPER.getDefaultSize()];
				break;
			case BEACON:
				this.items = new GUIItem[InventoryType.BEACON.getDefaultSize()];
				break;
			case DISPENSER:
				this.items = new GUIItem[InventoryType.DISPENSER.getDefaultSize()];
				break;
			case DROPPER:
				this.items = new GUIItem[InventoryType.DROPPER.getDefaultSize()];
				break;
			case FURNACE:
				this.items = new GUIItem[InventoryType.FURNACE.getDefaultSize()];
				break;
			case WORKBENCH:
				this.items = new GUIItem[InventoryType.WORKBENCH.getDefaultSize()];
				break;
			case CRAFTING:
				this.items = new GUIItem[InventoryType.CRAFTING.getDefaultSize()];
				break;
			case ENCHANTING:
				this.items = new GUIItem[InventoryType.ENCHANTING.getDefaultSize()];
				break;
			case BREWING:
				this.items = new GUIItem[InventoryType.BREWING.getDefaultSize()];
				break;
			case PLAYER:
				this.items = new GUIItem[InventoryType.PLAYER.getDefaultSize()];
				break;
			case MERCHANT:
				this.items = new GUIItem[InventoryType.MERCHANT.getDefaultSize()];
				break;
			case ENDER_CHEST:
				this.items = new GUIItem[InventoryType.ENDER_CHEST.getDefaultSize()];
				break;

			case CREATIVE:
				this.items = new GUIItem[InventoryType.CREATIVE.getDefaultSize()];
				break;
			case CANCEL:
				this.items = null;
		}
	}

	public final void setItem(int index, GUIItem item) {
		if (item == null) {
			this.items[index] = new GUIItem(new ItemStack(0));
		} else {
			this.items[index] = item;
		}
	}

	/**
	 * 批量添加GUI Item
	 *
	 * @param item
	 * @param index
	 */
	public void setItem(GUIItem item, int... index) {
		for (int i : index) {
			setItem(i, item);
		}
	}

	public GUIItem getItem(int index) {
		return this.items[index];
	}

	public void updateView(){
		if(this.inv != null){
			List<HumanEntity> viewers = this.inv.getViewers();
			for (int index = 0; index < this.items.length; index++) {
				if (items[index] == null) {
					inv.setItem(index, new ItemStack(Material.AIR));
				} else {
					inv.setItem(index, items[index].display);
				}
			}
			for(HumanEntity p : viewers){
				((Player)p).updateInventory();
			}
		}
	}

	public void setCancelledIfClickOnTarget(boolean b){
		this.setCancelledIfClickOnTarget = b;
	}

	public void setCancelledIfClickOnSelf(boolean b){
		this.setCancelledIfClickOnSelf = b;
	}

	public void setCancelledIfClickOnOuter(boolean b){
		this.setCancelledIfClickOnOuter = b;
	}

	public void addFlag(String flag,Object obj){
		if(this.flags == null) this.flags = new HashMap<>();
		this.flags.put(flag,obj);
	}

	public Object getFlag(String flag){
		if(this.flags == null) return null;
		else
			return this.flags.get(flag);
	}

	public void setFlag(String flag, Object obj){
		if(this.flags == null) this.flags = new HashMap<>();
		this.flags.replace(flag,obj);
	}

	public void removeFlag(String flag){
		if(this.flags == null) this.flags = new HashMap<>();
		this.flags.remove(flag);
	}

	public void rawClickListener(InventoryClickEvent event){}

	public void openGUI(User user) {
		Inventory inv;
		if (this.type == GUIType.CANCEL) {
			throw new NullPointerException("被取消或不存在的GUI");
		}
		switch (type) {
			default:
			case ONEBYNINE:
			case TWOBYNINE:
			case THREEBYNINE:
			case FOURBYNINE:
			case FIVEBYNINE:
			case SIXBYNINE:
				inv = Bukkit.createInventory(null, this.items.length, this.name);
				break;
			case HOPPER:
				inv = Bukkit.createInventory(null, InventoryType.HOPPER, name);
				break;
			case BEACON:
				inv = Bukkit.createInventory(null, InventoryType.BEACON, name);
				break;
			case DISPENSER:
				inv = Bukkit.createInventory(null, InventoryType.DISPENSER, name);
				break;
			case DROPPER:
				inv = Bukkit.createInventory(null, InventoryType.DROPPER, name);
				break;
			case FURNACE:
				inv = Bukkit.createInventory(null, InventoryType.FURNACE, name);
				break;
			case WORKBENCH:
				inv = Bukkit.createInventory(null, InventoryType.WORKBENCH, name);
				break;
			case CRAFTING:
				inv = Bukkit.createInventory(null, InventoryType.CRAFTING, name);
				break;
			case ENCHANTING:
				inv = Bukkit.createInventory(null, InventoryType.ENCHANTING, name);
				break;
			case BREWING:
				inv = Bukkit.createInventory(null, InventoryType.BREWING, name);
				break;
			case PLAYER:
				inv = Bukkit.createInventory(null, InventoryType.PLAYER, name);
				break;
			case CREATIVE:
				inv = Bukkit.createInventory(null, InventoryType.CREATIVE, name);
				break;
			case MERCHANT:
				inv = Bukkit.createInventory(null, InventoryType.MERCHANT, name);
				break;
			case ENDER_CHEST:
				inv = Bukkit.createInventory(null, InventoryType.ENDER_CHEST, name);
				break;
		}
		
		for (int index = 0; index < this.items.length; index++) {
			if (items[index] == null) {
				inv.setItem(index, new ItemStack(Material.AIR));
			} else {
				inv.setItem(index, items[index].display);
			}
		}
		user.openedGUI = this;
		this.inv = inv;
//		user.getPlayer().closeInventory();
		user.getPlayer().openInventory(inv);

		if(listener == null)
		Bukkit.getPluginManager().registerEvents(listener = new Listener() {
			@EventHandler
			public void onInventoryClickEvent(InventoryClickEvent event) {
				rawClickListener(event);
				if (!(event.getWhoClicked() instanceof Player)) {
					return;
				}
				Player p = (Player) event.getWhoClicked();
				User user = UserManager.getUser(p);
				if (event.getSlot() != -999){
					try {
						if (user.openedGUI == GUI.this && event.getClickedInventory() != null && event.getClickedInventory().equals(GUI.this.inv) && GUI.this.items[event.getSlot()] != null)
							GUI.this.items[event.getSlot()].realRawClickAction(event);
					}catch (ArrayIndexOutOfBoundsException e){
						e.printStackTrace();
						System.err.print("err cause by GUI("+GUI.this.toString()+"), name="+ name);
						return;
					}
				}else {
					if(setCancelledIfClickOnOuter)event.setCancelled(true);
				}
				if (user.openedGUI != null && /*user.openedGUI.inv.equals(event.getClickedInventory())*/ user.openedGUI == GUI.this && event.getClickedInventory() != null) {
					if(event.getClickedInventory().equals(GUI.this.inv)) {
						if(setCancelledIfClickOnTarget)event.setCancelled(true);

						if (event.getSlot() != -999 && GUI.this.items[event.getSlot()] != null) {
							if (GUI.this.items[event.getSlot()].isActionActive()) {
								GUI.this.items[event.getSlot()].onClick(event.getClick());
								GUI.this.items[event.getSlot()].ClickAction(event.getClick(), user);
								GUI.this.items[event.getSlot()].rawClickAction(event);
								if (!GUI.this.items[event.getSlot()].actions.isEmpty()) {
									for (GUIItem.GUIClickAction action : GUI.this.items[event.getSlot()].actions) {
										action.run(event.getClick(), user);
									}
								}
							}
							if (!GUI.this.items[event.getSlot()].actionsIngoreActive.isEmpty()) {
								for (GUIItem.GUIClickAction action : GUI.this.items[event.getSlot()].actionsIngoreActive) {
									action.run(event.getClick(), user);
								}
							}
						}
					}else if(event.getClickedInventory().equals(p.getInventory())){
						if(setCancelledIfClickOnSelf)event.setCancelled(true);
					}
				}
			}

			@EventHandler
			public void onDrag(InventoryDragEvent e){
				if (e.getWhoClicked() instanceof Player) {
					Player p = (Player) e.getWhoClicked();
					if(e.getInventory().equals(inv) || e.getInventory().equals(p.getInventory())){
						GUI.this.onDrag(e);
					}
				}
			}

			@EventHandler
			public void onInventoryCloseEvent(InventoryCloseEvent event) {
				if (event.getPlayer() instanceof Player && event.getInventory().equals(inv)) {
					Player p = (Player) event.getPlayer();
					User u = UserManager.getUser(p);
					if (event.getInventory().equals(inv)) {
						HandlerList.unregisterAll(this);
						listener = null;
						onClose();
					}
				}
			}
		}, Main.getInstance());

	}

	public void onDrag(InventoryDragEvent e){
	}

	public void onClose(){
	}
	
}
