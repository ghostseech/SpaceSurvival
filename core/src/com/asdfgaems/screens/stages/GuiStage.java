package com.asdfgaems.screens.stages;

import com.asdfgaems.screens.ResourseLoader;
import com.asdfgaems.screens.Vars;
import com.asdfgaems.screens.actors.ui.InventoryWindow;
import com.asdfgaems.screens.actors.ui.ItemSlot;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class GuiStage extends Stage{
    private GameStage stage;
    private InventoryWindow inventoryWindow;
    private Table leftPanel;
    private boolean hidden;

    public GuiStage(GameStage stage) {
        this.stage = stage;
        this.inventoryWindow = new InventoryWindow(this, stage.getPlayer().getInventory());
        this.hidden = true;
        setupLeftPanel();
        addLeftPanel();
    }

    public void showInventory() {
        if(!isHidden()) return;
        hidden = false;
        addActor(inventoryWindow);
    }

    public void hideAllWindows() {
        hidden = true;
        inventoryWindow.hideItemIteraction();
        clear();
        addLeftPanel();
    }

    private void addLeftPanel() {
        addActor(leftPanel);
    }

    private void setupLeftPanel() {
        this.leftPanel = new Table();
        leftPanel.align(Align.top);
        leftPanel.setSize(Vars.slotSize, Vars.slotSize * 3);
        leftPanel.setPosition(0, Vars.slotSize * 3);

        ImageButton inventoryButton = new ImageButton((ResourseLoader.instance.getSkin("main")));
        Image inventoryIcon = new Image(ResourseLoader.instance.getTextureRegion("inventory_button"));
        inventoryButton.add(inventoryIcon).size(Vars.slotSize);

        inventoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(getActors().indexOf(inventoryWindow, true) == -1) {
                    if(isHidden()) addCloseWindowButton();
                    hideAllWindows();
                    showInventory();
                }
            }
        });

        leftPanel.add(inventoryButton).size(Vars.slotSize, Vars.slotSize);
        leftPanel.row();
    }

    private void addCloseWindowButton() {
        ImageButton closeWindowButton = new ImageButton((ResourseLoader.instance.getSkin("main")));
        Image closeWindowIcon = new Image(ResourseLoader.instance.getTextureRegion("close_window_button"));
        closeWindowButton.add(closeWindowIcon).size(Vars.slotSize);
        closeWindowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hideAllWindows();
                leftPanel.remove();
                setupLeftPanel();
                addLeftPanel();
            }
        });
        leftPanel.add(closeWindowButton).size(Vars.slotSize, Vars.slotSize);
    }

    @Override
    public boolean keyDown(int keyCode) {
        return super.keyDown(keyCode);
    }

    public boolean isHidden() {
        return hidden;
    }

    public void dropItem(ItemSlot item) {
        stage.dropItem(item.getId(), item.getQuantity());
    }

}
