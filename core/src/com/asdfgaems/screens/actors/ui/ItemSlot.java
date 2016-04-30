package com.asdfgaems.screens.actors.ui;

import com.asdfgaems.screens.ResourseLoader;
import com.asdfgaems.screens.Vars;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class ItemSlot extends ImageButton {
    private Inventory inventory;
    private String id;
    private TextureRegion itemIcon;

    private int quantity;

    public ItemSlot(final Inventory inventory, String id, int quantity, Skin skin) {
        super(skin);
        this.inventory = inventory;

        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                inventory.update();
                select();
            }
        });

        setItem(id, quantity);
    }

    public void removeItem() {
        id = "null";
        itemIcon = null;
        quantity = 0;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(itemIcon != null)batch.draw(itemIcon, getX(), getY(), Vars.slotSize, Vars.slotSize);
        if(quantity >= 2)ResourseLoader.instance.getFont("default").draw(batch, String.valueOf(quantity), getX() + Vars.slotSize * 0.1f, getY() + Vars.slotSize * 0.9f );
    }

    public void setItem(String id, int quantity) {
        this.id = id;
        this.quantity = quantity;
        if(id.equals("null")) itemIcon = null;
        else itemIcon = ResourseLoader.instance.getTextureRegion(id);
    }

    public void addItems(int quantity) {
        this.quantity += quantity;
    }

    public void subtractItems(int quantity) {
        this.quantity -= quantity;
        if(this.quantity <= 0) {
            removeItem();
        }
    }

    public String getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    private void select() {
        inventory.selectItem(this);
    }

}
