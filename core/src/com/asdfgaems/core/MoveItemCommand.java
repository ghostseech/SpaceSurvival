package com.asdfgaems.core;

import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MoveItemCommand implements Command {
    private ItemDrawable item;
    private InventoryDrawable location;
    private InventoryDrawable destination;
    private ButtonGroup buttonGroup;


    public MoveItemCommand(ItemDrawable item, InventoryDrawable location, InventoryDrawable destination, ButtonGroup buttonGroup) {
        this.item = item;
        this.location = location;
        this.destination = destination;
        this.buttonGroup = buttonGroup;
    }

    public void execute() {
        destination.getInventory().addItem(location.getInventory().takeItem(item.getItem()));
        destination.create();
        location.create();
        destination.show();
        location.show();

        buttonGroup.clear();

        for(TextButton b : location.getButtons()) {
            buttonGroup.add(b);
        }

        for(TextButton b : destination.getButtons()) {
            buttonGroup.add(b);
        }

        item.hide();
        item.create();
        item.show();
       if(location.getInventory().checkItem(item.getItem())) item.addCommandButton("MOVE", new MoveItemCommand(item, location, destination, buttonGroup));
        else item.addCommandButton("MOVE", new MoveItemCommand(item, destination, location, buttonGroup));

    }
}
