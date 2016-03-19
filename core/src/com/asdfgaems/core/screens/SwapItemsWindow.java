package com.asdfgaems.core.screens;

import com.asdfgaems.core.Inventory;
import com.asdfgaems.core.InventoryDrawable;
import com.asdfgaems.core.ItemDrawable;
import com.asdfgaems.core.MoveItemCommand;
import com.asdfgaems.core.items.Item;
import com.asdfgaems.core.objects.Player;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class SwapItemsWindow {
    public static Texture backgroundTexture;
    private Stage stage;
    private Skin skin;

    private Image background;
    private InventoryDrawable playerInventoryDrawable;
    private InventoryDrawable inventoryDrawable;
    private ItemDrawable itemDrawable;

    private Player player;
    private Inventory inventory;
    private Button exitButton;

    private Label playerInventoryLabel;
    private Label inventoryLabel;

    private ButtonGroup buttonGroup;


    private boolean hidden;

    private final float x;
    private final float y;
    private final float width;
    private final float height;

    private final float menuSpace = 40.0f;

    private final float buttonWidth;
    private final float buttonHeight;

    public SwapItemsWindow(Stage stage, Skin skin, Player player, Inventory inventory, float x, float y, float width, float height) {
        this.stage = stage;
        this.skin = skin;
        this.player = player;
        this.inventory = inventory;

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.buttonWidth = width/4;
        this.buttonHeight = height/8;


        hidden = true;

        playerInventoryLabel = new Label("YOUR ITEMS", skin);
        playerInventoryLabel.setAlignment(Align.center);
        playerInventoryLabel.setSize(buttonWidth, buttonHeight);
        playerInventoryLabel.setPosition(x + menuSpace, y + height - menuSpace - buttonHeight);

        inventoryLabel = new Label("STASH ITEMS", skin);
        inventoryLabel.setAlignment(Align.center);
        inventoryLabel.setSize(buttonWidth, buttonHeight);
        inventoryLabel.setPosition(x + menuSpace * 2 + buttonWidth, y + height - menuSpace - buttonHeight);

        this.playerInventoryDrawable = new InventoryDrawable(player.inventory, stage, skin, x + menuSpace, y + menuSpace, buttonWidth, height - menuSpace * 2 - buttonHeight);
        this.inventoryDrawable = new InventoryDrawable(inventory, stage, skin, x + buttonWidth + menuSpace * 2, y + menuSpace, buttonWidth, height - menuSpace * 2 - buttonHeight);

        buttonGroup = new ButtonGroup();
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setUncheckLast(true);

        for(TextButton b : playerInventoryDrawable.getButtons()) {
            buttonGroup.add(b);
            b.addListener(new UpdateItemListener());
        }

        for(TextButton b : inventoryDrawable.getButtons()) {
            buttonGroup.add(b);
            b.addListener(new UpdateItemListener());
        }

        itemDrawable = new ItemDrawable(stage, skin, (Item)buttonGroup.getChecked().getUserObject(), x + buttonWidth * 2 + menuSpace *3, y + height - buttonHeight - menuSpace * 2, buttonWidth * 2 - menuSpace * 4, buttonHeight, buttonHeight, buttonHeight);
        updateItemDrawable();
        itemDrawable.hide();
        background = new Image(backgroundTexture);
        background.setPosition(x, y);
        background.setSize(width, height);

        exitButton = new TextButton("BACK", skin);

        exitButton.setSize(buttonWidth, buttonHeight);
        exitButton.setPosition(x + width - buttonWidth - menuSpace, y + menuSpace);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });
    }

    public void update(float dt) {
        playerInventoryDrawable.setButtonsColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
        inventoryDrawable.setButtonsColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));

        if(buttonGroup.getChecked() != null) {
            inventoryDrawable.setButtonsColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
            buttonGroup.getChecked().setColor(0.7f, 1.0f, 0.7f, 1.0f);
        }
        else {
            itemDrawable.hide();
        }
    }

    private void updateItemDrawable() {
        itemDrawable.hide();
        itemDrawable = new ItemDrawable(stage, skin, (Item)buttonGroup.getChecked().getUserObject(), x + buttonWidth * 2 + menuSpace *3, y + height - buttonHeight - menuSpace * 2, buttonWidth * 2 - menuSpace * 4, buttonHeight, buttonHeight, buttonHeight);
        if(player.inventory.checkItem(itemDrawable.getItem())) itemDrawable.addCommandButton("MOVE", new MoveItemCommand(itemDrawable, playerInventoryDrawable, inventoryDrawable, buttonGroup));
        else itemDrawable.addCommandButton("MOVE", new MoveItemCommand(itemDrawable, inventoryDrawable, playerInventoryDrawable, buttonGroup));
        itemDrawable.getButtons().getLast().addListener(new UpdateItemListener());
        itemDrawable.getButtons().getLast().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                updateButtonGroup();
            }
        });
        itemDrawable.show();
    }
    private void updateButtonGroup() {
        for(int i = 0; i < buttonGroup.getButtons().size; i++) {
            ((TextButton)buttonGroup.getButtons().get(i)).addListener(new UpdateItemListener());
        }
    }
    public void show() {
        hidden = false;
        stage.addActor(background);
        stage.addActor(exitButton);
        stage.addActor(playerInventoryLabel);
        stage.addActor(inventoryLabel);
        playerInventoryDrawable.show();
        inventoryDrawable.show();
        itemDrawable.show();
    }

    public void hide() {
        hidden = true;
        playerInventoryDrawable.hide();
        inventoryDrawable.hide();
        itemDrawable.hide();
        stage.getActors().removeValue(exitButton, true);
        stage.getActors().removeValue(background, true);
        stage.getActors().removeValue(playerInventoryLabel, true);
        stage.getActors().removeValue(inventoryLabel, true);
    }
    public boolean isHidden() {
        return hidden;
    }

    public class UpdateItemListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            updateItemDrawable();
        }
    }

}
