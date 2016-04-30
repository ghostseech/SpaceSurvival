package com.asdfgaems.screens.actors.ui;

import com.asdfgaems.screens.ResourseLoader;
import com.asdfgaems.screens.Vars;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class ItemInteraction extends Table {
    private InventoryWindow inventoryWindow;
    private Image icon;
    private ItemSlot item;
    private Label nameLabel;
    private Label infoLabel;

    public ItemInteraction(final InventoryWindow inventoryWindow, ItemSlot item) {
        ResourseLoader loader = ResourseLoader.instance;
        background(new TextureRegionDrawable(loader.getTextureRegion("ui_background")));
        align(Align.topLeft);
        this.inventoryWindow = inventoryWindow;
        this.item = item;

        icon = new Image(ResourseLoader.instance.getTextureRegion(item.getId()));

        nameLabel = new Label(loader.getText(item.getId() + "_name") + "(" + String.valueOf(item.getQuantity()) + ")", loader.getSkin("main"));
        infoLabel = new Label(loader.getText(item.getId() + "_description"), loader.getSkin("main"));

        add(icon).size(Vars.slotSize, Vars.slotSize);
        add(nameLabel);
        row();
        add(infoLabel);

        Array<String> actions = new Array<String>();
        actions.add("drop_action");

        for(int i = 0; i < actions.size; i++) {
            TextButton button = new TextButton(loader.getText(actions.get(i)), loader.getSkin("main"));
            add(button).size(Vars.slotSize, Vars.slotSize / 2);
            row();
            if(actions.get(i).equals("drop_action")) {
                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        inventoryWindow.dropSelectedItem();
                    }
                });
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
