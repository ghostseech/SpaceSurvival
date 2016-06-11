package com.asdfgaems.game.actors.ui.windows;

import com.asdfgaems.Vars;
import com.asdfgaems.game.actors.ui.*;
import com.asdfgaems.stages.GuiStage;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

public class ChestWindow extends Group implements ItemActionListener, ItemSelection {
    private GuiStage gui;
    private Inventory inventory;
    private ItemInteraction itemInteraction;
    private ItemSlot selectedItem;

    public ChestWindow(GuiStage gui, Inventory inventory) {
        this.gui = gui;
        this.inventory = inventory;
        this.itemInteraction = null;
        inventory.setInventoryWindow(this);
        inventory.setPosition(Vars.slotSize * 1, Vars.slotSize * 1);
        addActor(inventory);
    }

    @Override
    public void selectItem(ItemSlot itemSlot) {
        if(selectedItem != null) selectedItem.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        hideItemIteraction();
        this.selectedItem = itemSlot;
        if(!itemSlot.getId().equals("null")) {
            showItemIteraction();
            selectedItem.setColor(0.0f, 1.0f, 0.0f, 1.0f);
        }
    }

    public void showItemIteraction() {
        hideItemIteraction();
        itemInteraction = new ItemInteraction(this, selectedItem);
        itemInteraction.setPosition(Vars.slotSize * 8, Vars.slotSize * 6);
        Array<String> actions = new Array<String>();
        actions.add("move_action");
        actions.add("drop_action");

        for(int i = 0; i < actions.size; i++) {
            itemInteraction.addActionButton(actions.get(i));
        }

        if(gui.isPlayerHaveSlot(selectedItem)) itemInteraction.addActionButton("move_action");
        addActor(itemInteraction);
    }

    public void hideItemIteraction() {
        if(itemInteraction != null) itemInteraction.remove();
        itemInteraction = null;
    }

    @Override
    public void doItemAction(String id) {
        if(id.equals("drop_action")) {
            gui.dropItem(selectedItem);
            selectedItem.removeItem();
            selectedItem.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            selectedItem = null;
            hideItemIteraction();
        }
        else if(id.equals("move_action")) {
            gui.addItemToPlayerInventory(selectedItem);
            selectedItem.removeItem();
            selectedItem.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            selectedItem = null;
            hideItemIteraction();
        }
        else if(id.equals("consume_action")) {
            gui.consumeItem(selectedItem);
            selectedItem.subtractItems(1);
            if(selectedItem.getItem() == null) {
                selectedItem.setColor(1.0f, 1.0f, 1.0f, 1.0f);
                selectedItem = null;
                hideItemIteraction();
            }
        }
        inventory.compose();
    }
    public Inventory getInventory() {
        return inventory;
    }


}
