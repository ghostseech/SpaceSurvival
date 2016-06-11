package com.asdfgaems.game.actors.ui;

import com.asdfgaems.ResourseLoader;
import com.asdfgaems.Vars;
import com.asdfgaems.game.actors.items.Item;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class Inventory extends Table {
    private Array<ItemSlot> items;
    private ItemSelection inventoryWindow;

    public Inventory(int size) {
        this.align(Align.left);
        this.setSize(Vars.slotSize * 6, Vars.slotSize * 6);

        this.items = new Array<ItemSlot>();
        for(int i = 0; i < size; i++) {
            this.items.add(new ItemSlot(this, null, ResourseLoader.instance.getSkin("inventory")));
        }
        update();
    }

    public void update() {
        clear();
        for(int i = 0; i < items.size; i++) {
            add(items.get(i)).size(Vars.slotSize, Vars.slotSize);
            if((i+1) % 6 == 0) row();
        }
    }

    public void selectItem(ItemSlot itemSlot) {
        if(inventoryWindow != null) inventoryWindow.selectItem(items.get(items.indexOf(itemSlot, true)));
    }

    public void setInventoryWindow(ItemSelection inventoryWindow) {
        this.inventoryWindow = inventoryWindow;
    }


    public boolean addItem(Item item) {
        //if items stackable - find slot with item of that type
        if(item.isStackable())
        for(int i = 0; i < items.size; i++) {
            if(items.get(i).compareItems(item)) {
                items.get(i).addItems(item.getQuantity());
                return true;
            }
        }
        //if not, find place for that item
        for(int i = 0; i < items.size; i++) {
            if(items.get(i).getId().equals("null")) {
                items.get(i).setItem(item);
                return true;
            }
        }
        return false;
    }

    public boolean removeItem(String id, int quantity) {
        for(int i = 0; i < items.size; i++) {
            if(items.get(i).getId().equals(id)) {
                if(items.get(i).getQuantity() >= quantity) {
                    items.get(i).subtractItems(quantity);
                    compose();
                    return true;
                }
                else return false;
            }
        }
        return false;
    }
    public boolean haveSpaceForItem(Item item) {
        for(int i = 0; i < items.size; i++) {
            ItemSlot current = items.get(i);
            if(current.getId().equals(item.getId()) && item.isStackable()) return true;
            if(current.getId().equals("null")) return true;
        }
        return false;
    }

    public void compose() {
        Array<Integer> freeSlots = new Array<Integer>();
        for(int i = 0; i < items.size; i++) {
            ItemSlot current = items.get(i);
            if(current.getId().equals("null")) {
                freeSlots.add(i);
            }
            else {
                if(freeSlots.size > 0) {
                    items.get(freeSlots.get(0)).setItem(items.get(i).getItem());
                    items.get(i).removeItem();
                    freeSlots.removeIndex(0);
                    compose();
                    return;
                }
            }
        }
    }

    public void setButtonsColor() {
        for(int i = 0; i < items.size; i++) {
            items.get(i).setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    public int getItemCount(String id) {
        for(ItemSlot i : items) {
            if(i.getId().equals(id)) return i.getQuantity();
        }
        return 0;
    }
}
