package com.asdfgaems.screens.actors.ui;

import com.asdfgaems.screens.ResourseLoader;
import com.asdfgaems.screens.Vars;
import com.asdfgaems.screens.stages.GuiStage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class InventoryWindow extends Group {
    private GuiStage gui;
    private Inventory inventory;
    private Table leftPanel;
    private ItemInteraction itemInteraction;

    private ItemSlot selectedItem;

    public InventoryWindow(GuiStage gui, Inventory inventory) {
        this.inventory = inventory;
        this.gui = gui;
        inventory.setInventoryWindow(this);
        inventory.setPosition(Vars.slotSize * 1, Vars.slotSize * 1);
        addActor(inventory);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    public void update() {

    }

    public void dropSelectedItem() {
        gui.dropItem(selectedItem);
        inventory.removeItem(selectedItem.getId(), selectedItem.getQuantity());
        selectedItem = null;
        hideItemIteraction();
    }

    public void selectItem(ItemSlot itemSlot) {
        this.selectedItem = itemSlot;
        if(itemSlot.getId() != "null") showItemIteraction();
    }

    public void showItemIteraction() {
        hideItemIteraction();
        itemInteraction = new ItemInteraction(this, selectedItem);
        itemInteraction.setPosition(Vars.slotSize * 7, Vars.slotSize * 6);
        addActor(itemInteraction);
    }

    public void hideItemIteraction() {
        if(itemInteraction != null) itemInteraction.remove();
        itemInteraction = null;
    }

}
