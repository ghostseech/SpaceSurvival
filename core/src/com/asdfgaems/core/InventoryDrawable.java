package com.asdfgaems.core;


import com.asdfgaems.core.objects.Item;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.LinkedList;

public class InventoryDrawable {
    private Stage stage;
    private Skin skin;

    private Inventory inventory;

    private ScrollPane scrollPane;
    private Table table;
    private LinkedList<TextButton> buttons;


    private final float x;
    private final float y;
    private final float width;
    private final float height;

    public InventoryDrawable(Inventory inventory, Stage stage, Skin skin, float x, float y, float width, float height) {
        this.inventory = inventory;
        this.stage = stage;
        this.skin = skin;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.buttons = new LinkedList<TextButton>();
        create();
    }

    public ScrollPane getWidget() {
        return scrollPane;
    }

    public void show() {
        stage.addActor(scrollPane);
    }

    public void hide() {
        stage.getActors().removeValue(scrollPane, true);
    }

    public void addItem(Item item) {
    }

    public void removeItem(int id) {

    }

    public void create() {
        buttons.clear();
        hide();

        this.table = new Table();

        this.table.setSize(width, height / 8 * inventory.getSize());
        this.table.align(Align.top);

        this.scrollPane = new ScrollPane(table);
        this.scrollPane.setScrollingDisabled(true, false);
        this.scrollPane.setSize(width, height);
        this.scrollPane.setPosition(x, y);

        for (int i = 0; i < inventory.getItemsCount(); i++) {
            TextButton button = new TextButton(inventory.getItem(i).getName(), skin);
            button.setSize(width, height / 8);
            button.setUserObject(inventory.getItem(i));

            this.buttons.add(button);
            table.add(button).size(width, height / 8);
            table.row();
        }
    }

    public void setButtonsColor(Color color) {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setColor(color);
        }
    }

    public Inventory getInventory() {
        return inventory;
    }
    public LinkedList<TextButton> getButtons() {
        return buttons;
    }
}
