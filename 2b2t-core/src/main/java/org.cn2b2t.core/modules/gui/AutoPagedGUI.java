package org.cn2b2t.core.modules.gui;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.cn2b2t.core.modules.users.User;
import org.cn2b2t.core.utils.ItemStackFactory;

public class AutoPagedGUI extends CommonPagedGUI {

    ItemStack lastPageUI;
    ItemStack nextPageUI;
    ItemStack firstPageUI;
    ItemStack endPageUI;
    int lastPageSlot = -1;
    int nextPageSlot = -1;

    public AutoPagedGUI(GUIType type, String name, int[] range) {
        super(type, name, range);
    }

    public AutoPagedGUI(GUIType type, String name, int a, int b) {
        super(type, name, a, b);
    }

    public void setLastPageUI(ItemStack lastPageUI) {
        this.lastPageUI = lastPageUI;
    }

    public void setNextPageUI(ItemStack nextPageUI) {
        this.nextPageUI = nextPageUI;
    }

    public void setFirstPageUI(ItemStack firstPageUI) {
        this.firstPageUI = firstPageUI;
    }

    public void setEndPageUI(ItemStack endPageUI) {
        this.endPageUI = endPageUI;
    }

    public void setLastPageSlot(int slot) {
        this.lastPageSlot = slot;
    }

    public void setNextPageSlot(int slot) {
        this.nextPageSlot = slot;
    }

    @Override
    public void openGUI(User user) {
        if (lastPageSlot >= 0)
            if (hasLastPage()) {
                setItem(lastPageSlot, new GUIItem(lastPageUI == null ? new ItemStackFactory(Material.ARROW)
                        .setDisplayName("上一页")
                        .toItemStack() : lastPageUI) {
                    @Override
                    public void ClickAction(ClickType type, User u) {
                        lastPage();
                        openGUI(u);
                        u.getPlayer().playSound(u.getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG, 0.5f, 1);
                    }
                });
            } else {
                setItem(lastPageSlot, new GUIItem(firstPageUI == null ? null : firstPageUI));
            }

        if (lastPageSlot >= 0)
            if (hasNextPage()) {
                setItem(nextPageSlot, new GUIItem(nextPageUI == null ? new ItemStackFactory(Material.ARROW)
                        .setDisplayName("下一页")
                        .toItemStack() : nextPageUI) {
                    @Override
                    public void ClickAction(ClickType type, User u) {
                        nextPage();
                        openGUI(u);
                        u.getPlayer().playSound(u.getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG, 0.5f, 1);
                    }
                });
            } else {
                setItem(nextPageSlot, new GUIItem(endPageUI == null ? null : endPageUI));
            }

        super.openGUI(user);
    }

}
