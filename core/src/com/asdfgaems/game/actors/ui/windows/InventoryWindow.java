package com.asdfgaems.game.actors.ui.windows;

import com.asdfgaems.Vars;
import com.asdfgaems.game.actors.ui.*;
import com.asdfgaems.stages.GuiStage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

public class InventoryWindow extends Group implements ItemActionListener, ItemSelection {
    private GuiStage gui;
    private Inventory inventory;
    private ItemInteraction itemInteraction;

    private ItemSlot selectedItem;

    public InventoryWindow(GuiStage gui, Inventory inventory) {
        this.inventory = inventory;
        this.gui = gui;
        this.itemInteraction = null;
        inventory.setInventoryWindow(this);
        inventory.setPosition(Vars.slotSize * 1, Vars.slotSize * 1);
        addActor(inventory);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
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
        itemInteraction.setPosition(Vars.slotSize * 7, Vars.slotSize * 1);

        Array<String> actions = selectedItem.getItem().getItemActions();

        for(int i = 0; i < actions.size; i++) {
            itemInteraction.addActionButton(actions.get(i));
        }

        if(gui.isChestWindowReady())
            if(gui.isChestHaveSlot(selectedItem))
                itemInteraction.addActionButton("move_action");

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
            if(gui.getPlayerWeapon() == selectedItem.getItem()) {
                gui.unequipWeapon();
            }
            if(gui.getPlayerArmor() == selectedItem.getItem()) {
                gui.unequipArmor();
            }
            selectedItem.removeItem();
            selectedItem.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            selectedItem = null;
            hideItemIteraction();
        }
        else if(id.equals("move_action")) {
            gui.addItemToChestInventory(selectedItem);
            if(gui.getPlayerWeapon() == selectedItem.getItem()) {
                gui.unequipWeapon();
            }
            if(gui.getPlayerArmor() == selectedItem.getItem()) {
                gui.unequipArmor();
            }
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
        else if(id.equals("weapon_equip_action")) {
            gui.equipWeapon(selectedItem);
            hideItemIteraction();
            showItemIteraction();
        }

        else if(id.equals("weapon_unequip_action")) {
            gui.unequipWeapon();
            hideItemIteraction();
            showItemIteraction();
        }
        else if(id.equals("armor_equip_action")) {
            gui.equipArmor(selectedItem);
            hideItemIteraction();
            showItemIteraction();
        }
        else if(id.equals("armor_unequip_action")) {
            gui.unequipArmor();
            hideItemIteraction();
            showItemIteraction();
        }
        inventory.compose();
    }

    public Inventory getInventory() {
        return inventory;
    }
}
