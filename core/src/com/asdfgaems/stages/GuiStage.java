package com.asdfgaems.stages;

import com.asdfgaems.ResourseLoader;
import com.asdfgaems.Vars;
import com.asdfgaems.game.actors.characters.Player;
import com.asdfgaems.game.actors.items.Armor;
import com.asdfgaems.game.actors.items.Consumable;
import com.asdfgaems.game.actors.items.Item;
import com.asdfgaems.game.actors.items.Weapon;
import com.asdfgaems.game.actors.ui.*;
import com.asdfgaems.game.actors.ui.indicators.PlayerActionsIndicator;
import com.asdfgaems.game.actors.ui.indicators.PlayerItemsIndicator;
import com.asdfgaems.game.actors.ui.indicators.PlayerStateIndicator;
import com.asdfgaems.game.actors.ui.windows.ChestWindow;
import com.asdfgaems.game.actors.ui.windows.InventoryWindow;
import com.asdfgaems.game.actors.ui.windows.PlayerStateWindow;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class GuiStage extends Stage {
    private GameStage stage;
    private InventoryWindow inventoryWindow;
    private ChestWindow chestWindow;
    private PlayerStateWindow playerStateWindow;

    private PlayerActionsIndicator playerActionsIndicator;
    private PlayerStateIndicator playerStateIndicator;
    private PlayerItemsIndicator playerItemsIndicator;

    private Table leftPanel;
    private boolean hidden;

    public GuiStage(GameStage stage) {
        this.stage = stage;

        chestWindow = null;
        inventoryWindow = null;
        hidden = true;
        setupIngameLeftPanel();
        addActor(leftPanel);
    }

    public void setPlayerInventory(Inventory inventory) {
        inventoryWindow = new InventoryWindow(this, inventory);
    }

    public void setPlayer(Player player) {
        playerStateWindow = new PlayerStateWindow(player);

        playerActionsIndicator = new PlayerActionsIndicator(player);
        playerActionsIndicator.setPosition(0.0f, Vars.tileSize * 10);
        addActor(playerActionsIndicator);

        playerStateIndicator = new PlayerStateIndicator(player);
        playerStateIndicator.setPosition(Vars.tileSize * 16, Vars.tileSize * 10);
        addActor(playerStateIndicator);

        playerItemsIndicator = new PlayerItemsIndicator(player);
        playerItemsIndicator.setPosition(Vars.tileSize * 16, Vars.tileSize * 8.5f);
        addActor(playerItemsIndicator);
    }

    public void openChest(Inventory inventory) {
        chestWindow = new ChestWindow(this, inventory);
        openWindow(chestWindow);
    }

    public void openPlayerInventory() {
        openWindow(inventoryWindow);
    }

    public void openPlayerStats() {
        openWindow(playerStateWindow);
    }

    public void setupIngameLeftPanel() {
        leftPanel = new Table();
        leftPanel.align(Align.top);
        leftPanel.setSize(Vars.slotSize, Vars.slotSize * 3);
        leftPanel.setPosition(0, Vars.slotSize * 3);

        addWindowButton(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                openPlayerInventory();
            }
        },inventoryWindow, "inventory_button");

        addWindowButton(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                openPlayerStats();
            }
        }, playerStateWindow, "playerstate_button");
    }

    public void setupInuiLeftPanel() {
        leftPanel = new Table();
        leftPanel.align(Align.top);
        leftPanel.setSize(Vars.slotSize, Vars.slotSize * 3);
        leftPanel.setPosition(0, Vars.slotSize * 3);

        addWindowButton(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                openPlayerInventory();
                            }
        },inventoryWindow, "inventory_button");

        addWindowButton(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                openPlayerStats();
            }
        }, playerStateWindow, "playerstate_button");

        if(chestWindow != null)
            addWindowButton(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    openChest(chestWindow.getInventory());
                }
            }, chestWindow, "chest_opened");

        addCloseWindowButton();
    }

    private void addWindowButton(ClickListener listener,  Actor window, String id) {
        ImageButton inventoryButton = new ImageButton((ResourseLoader.instance.getSkin("main")));
        Image inventoryIcon = new Image(ResourseLoader.instance.getTextureRegion(id));

        inventoryButton.add(inventoryIcon).size(Vars.slotSize);
        if(getActors().indexOf(window, true) != -1) inventoryButton.setColor(0.0f, 1.0f, 0.0f, 1.0f);

        inventoryButton.addListener(listener);

        leftPanel.add(inventoryButton).size(Vars.slotSize, Vars.slotSize);
        leftPanel.row();
    }

    private void openWindow(Actor actor) {
        ChestWindow c = chestWindow;
        clearAll();
        chestWindow = c;
        addActor(actor);
        setupInuiLeftPanel();
        addActor(leftPanel);
        addActor(playerActionsIndicator);
        addActor(playerStateIndicator);
        addActor(playerItemsIndicator);
        hidden = false;
    }

    public void clearAll() {
        hidden = true;

        inventoryWindow.hideItemIteraction();
        inventoryWindow.getInventory().setButtonsColor();
        if(chestWindow != null) {
            chestWindow.hideItemIteraction();
            chestWindow.getInventory().setButtonsColor();
            chestWindow = null;
        }
        clear();
    }

    private void addCloseWindowButton() {
        ImageButton closeWindowButton = new ImageButton((ResourseLoader.instance.getSkin("main")));
        Image closeWindowIcon = new Image(ResourseLoader.instance.getTextureRegion("close_window_button"));
        closeWindowButton.add(closeWindowIcon).size(Vars.slotSize);
        closeWindowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearAll();
                setupIngameLeftPanel();
                addActor(leftPanel);
                addActor(playerActionsIndicator);
                addActor(playerStateIndicator);
                addActor(playerItemsIndicator);
            }
        });
        leftPanel.add(closeWindowButton).size(Vars.slotSize, Vars.slotSize);
        leftPanel.row();
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(stage.isPlayerReady()) return super.touchDown(screenX, screenY, pointer, button);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(stage.isPlayerReady()) return super.touchDragged(screenX, screenY, pointer);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(stage.isPlayerReady()) return super.touchUp(screenX, screenY, pointer, button);
        return false;
    }

    @Override
    public boolean keyDown(int keyCode) {
        return super.keyDown(keyCode);
    }

    public void dropItem(ItemSlot item) {
        stage.dropItem(item.getItem());
    }

    public boolean isHidden() {
        return hidden;
    }

    public void addItemToPlayerInventory(ItemSlot item) {
        if(!inventoryWindow.getInventory().addItem(item.getItem())) {
            addItemToChestInventory(item);
        }
    }

    public void addItemToChestInventory(ItemSlot item) {
        if(!chestWindow.getInventory().addItem(item.getItem())) {
            addItemToPlayerInventory(item);
        }
    }

    public void onAttack() {
        playerItemsIndicator.updateAmmoCount();
    }

    public void consumeItem(ItemSlot item) {
        ((Consumable)item.getItem()).consume(stage.getPlayer());
    }

    public boolean isChestWindowReady() {
        return chestWindow != null;
    }

    public boolean isChestHaveSlot(ItemSlot i) {
        if(chestWindow != null) return chestWindow.getInventory().haveSpaceForItem(i.getItem());
        return false;
    }

    public void equipWeapon(ItemSlot item) {
        stage.getPlayer().equipWeapon((Weapon)item.getItem());
        playerItemsIndicator.updateTextureRegions();
        playerItemsIndicator.updateAmmoCount();
    }
    public void equipArmor(ItemSlot item) {
        stage.getPlayer().equipArmor((Armor) item.getItem());
        playerItemsIndicator.updateTextureRegions();
    }

    public void unequipWeapon() {
        stage.getPlayer().unequipWeapon();
        playerItemsIndicator.updateTextureRegions();
        playerItemsIndicator.updateAmmoCount();
    }

    public void unequipArmor() {
        stage.getPlayer().unequipArmor();
        playerItemsIndicator.updateTextureRegions();
    }

    public Item getPlayerWeapon() {
        return stage.getPlayer().getWeapon();

    }
    public Item getPlayerArmor() {
        return stage.getPlayer().getArmor();
    }

    public boolean isPlayerHaveSlot(ItemSlot i) {
        return inventoryWindow.getInventory().haveSpaceForItem(i.getItem());
    }

}
