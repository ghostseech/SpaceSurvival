package com.asdfgaems.core.screens;

import com.asdfgaems.core.*;
import com.asdfgaems.core.items.Armor;
import com.asdfgaems.core.items.Consumable;
import com.asdfgaems.core.items.Item;
import com.asdfgaems.core.items.Weapon;
import com.asdfgaems.core.objects.Player;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class PlayerInfoWindow {
    public static Texture backgroundTexture;
    public static final int STATE_INVENTORY = 1;

    private final float width;
    private final float height;

    private final float x;
    private final float y;

    private Player player;
    private Stage stage;
    private Image background;
    private TextButton inventoryButton;
    private TextButton exitButton;

    private Skin skin;

    private float menuButtonWidth;
    private float menuButtonHeight;
    private float menuSpace;

    private boolean hidden;
    private int state;

    private InventoryDrawable inventoryDrawable;
    private ItemDrawable itemDrawable;
    private ButtonGroup buttonGroup;

    public PlayerInfoWindow(Stage stage, Skin skin, Player player, float width, float height, float x, float y) {
        this.stage = stage;
        this.skin = skin;
        this.player = player;

        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;

        state = STATE_INVENTORY;
        hidden = true;
        menuButtonHeight = height / 8;
        menuButtonWidth = width / 6;
        menuSpace = 40.0f;

        background = new Image(backgroundTexture);
        background.setSize(width, height);
        background.setPosition(x, y);

        inventoryButton = new TextButton("ITEMS", skin);

        inventoryButton.background(new TextureRegionDrawable(new TextureRegion(backgroundTexture)));
        inventoryButton.setSize(menuButtonWidth, menuButtonHeight);
        inventoryButton.setPosition(x + menuSpace, y + height - menuSpace - menuButtonHeight * 0 - menuButtonHeight);

        exitButton = new TextButton("BACK", skin);

        exitButton.background(new TextureRegionDrawable(new TextureRegion(backgroundTexture)));
        exitButton.setSize(menuButtonWidth, menuButtonHeight);
        exitButton.setPosition(x + menuSpace, y + height - menuSpace - menuButtonHeight * 1 - menuButtonHeight);



        inventoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                show();
                state = STATE_INVENTORY;
                inventoryDrawable.create();
                buttonGroup.clear();
                for(int i = 0; i < inventoryDrawable.getButtons().size(); i++) {
                    buttonGroup.add(inventoryDrawable.getButtons().get(i));
                }

                inventoryDrawable.show();
                for(int i = 0; i < inventoryDrawable.getButtons().size(); i++) {
                    ((TextButton)inventoryDrawable.getButtons().get(i)).addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            updateItemDrawable();
                        }
                    });
                }
                itemDrawable.show();
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });

        inventoryDrawable = new InventoryDrawable(player.inventory, stage, skin, x + menuButtonWidth + menuSpace + menuSpace, y + menuSpace, menuButtonWidth, height - menuSpace * 2);
        buttonGroup = new ButtonGroup();

        for(int i = 0; i < inventoryDrawable.getButtons().size(); i++) {
            buttonGroup.add(inventoryDrawable.getButtons().get(i));
        }

        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setUncheckLast(true);

        itemDrawable = new ItemDrawable(stage, skin, (Item)buttonGroup.getChecked().getUserObject(), x + menuButtonWidth * 2 + menuSpace *3, y + height - menuButtonHeight - menuSpace * 2, menuButtonWidth * 2, menuButtonHeight, menuButtonHeight, menuButtonHeight);
        updateItemDrawable();
        itemDrawable.hide();
    }

    public void show() {
        hide();
        hidden = false;
        stage.addActor(background);
        stage.addActor(inventoryButton);
        stage.addActor(exitButton);
    }

    public void hide() {
        hidden = true;
        stage.getActors().removeValue(background, true);
        stage.getActors().removeValue(inventoryButton, true);
        stage.getActors().removeValue(exitButton, true);
        inventoryDrawable.hide();
        itemDrawable.hide();
    }

    public void update(float dt) {
        if(state == STATE_INVENTORY) {
            inventoryDrawable.setButtonsColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
            if(buttonGroup.getChecked() != null) {
                buttonGroup.getChecked().setColor(0.7f, 1.0f, 0.7f, 1.0f);
                for(int i = 0; i < buttonGroup.getButtons().size; i++) {
                    if(((TextButton)buttonGroup.getButtons().get(i)).getUserObject() == player.getArmor()) ((TextButton)buttonGroup.getButtons().get(i)).setColor(1.0f, 0.7f, 0.7f, 1.0f);
                    if(((TextButton)buttonGroup.getButtons().get(i)).getUserObject() == player.getWeapon()) ((TextButton)buttonGroup.getButtons().get(i)).setColor(1.0f, 0.7f, 0.7f, 1.0f);
                }
            }
            else {
                itemDrawable.hide();
            }
        }
    }

    private void updateItemDrawable() {
        itemDrawable.hide();
        itemDrawable = new ItemDrawable(stage, skin, (Item)buttonGroup.getChecked().getUserObject(), x + menuButtonWidth * 2 + menuSpace *3, y + height - menuButtonHeight - menuSpace * 2, menuButtonWidth * 2, menuButtonHeight, menuButtonHeight, menuButtonHeight);

        if(itemDrawable.getItem().getClass() == Armor.class) {
            if(player.getArmor() == itemDrawable.getItem()) {
                addCommandButton("Unequip");
                itemDrawable.getButtons().getLast().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        player.equipArmor(null);
                    }
                });
            }
            else {
                addCommandButton("Equip");
                itemDrawable.getButtons().getLast().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        player.equipArmor((Armor)itemDrawable.getItem());
                    }
                });
            }
        }
        else if(itemDrawable.getItem().getClass() == Weapon.class) {
            if(player.getWeapon() == itemDrawable.getItem()) {
                addCommandButton("Unequip");
                itemDrawable.getButtons().getLast().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        player.equipWeapon(null);
                    }
                });
            }
            else {
                addCommandButton("Equip");
                itemDrawable.getButtons().getLast().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        player.equipWeapon((Weapon) itemDrawable.getItem());
                    }
                });
            }
        }
        else if(itemDrawable.getItem().getClass() == Consumable.class) {
            addCommandButton("Use");
            itemDrawable.getButtons().getLast().addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    player.consume((Consumable) itemDrawable.getItem());
                    inventoryDrawable.getInventory().removeItem(itemDrawable.getItem());
                    inventoryDrawable.create();
                    buttonGroup.clear();
                    for(TextButton b : inventoryDrawable.getButtons()) {
                        buttonGroup.add(b);
                    }
                    for(int i = 0; i < inventoryDrawable.getButtons().size(); i++) {
                        ((TextButton)inventoryDrawable.getButtons().get(i)).addListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                updateItemDrawable();
                            }
                        });
                    }
                    inventoryDrawable.show();
                    updateItemDrawable();
                }
            });
        }
        itemDrawable.show();
    }

    private void addCommandButton(String text) {
        itemDrawable.addCommandButton(text);
        itemDrawable.getButtons().getLast().addListener(new UpdateItemListener());
    }

    public boolean isHidden() {
        return hidden;
    }

    private class UpdateItemListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            updateItemDrawable();
        }
    }
}
