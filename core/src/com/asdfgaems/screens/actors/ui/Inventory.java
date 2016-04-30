package com.asdfgaems.screens.actors.ui;

import com.asdfgaems.screens.ResourseLoader;
import com.asdfgaems.screens.Vars;
import com.asdfgaems.screens.stages.GuiStage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class Inventory extends Table {
    private Array<ItemSlot> items;
    private InventoryWindow inventoryWindow;

    public Inventory(int size) {
        this.align(Align.left);
        this.setSize(Vars.slotSize * 6, Vars.slotSize * 6);

        this.items = new Array<ItemSlot>();
        for(int i = 0; i < size; i++) {
            this.items.add(new ItemSlot(this, "null", 0, ResourseLoader.instance.getSkin("inventory")));
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

    public void setInventoryWindow(InventoryWindow inventoryWindow) {
        this.inventoryWindow = inventoryWindow;
    }


    public boolean addItem(String id, int quantity, boolean stackable) {
        //if items stackable - find slot with item of that type
        if(stackable)
        for(int i = 0; i < items.size; i++) {
            if(items.get(i).getId().equals(id)) {
                items.get(i).addItems(quantity);
                return true;
            }
        }
        //if not, find place for that item
        for(int i = 0; i < items.size; i++) {
            if(items.get(i).getId().equals("null")) {
                items.get(i).setItem(id, quantity);
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
                    return true;
                }
                else return false;
            }
        }
        return false;
    }
}
