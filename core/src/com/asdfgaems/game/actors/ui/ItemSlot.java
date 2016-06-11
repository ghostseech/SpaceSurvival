package com.asdfgaems.game.actors.ui;

import com.asdfgaems.ResourseLoader;
import com.asdfgaems.Vars;
import com.asdfgaems.game.actors.items.Item;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ItemSlot extends ImageButton {
    private Inventory inventory;
    private Item item;
    private TextureRegion itemIcon;



    public ItemSlot(final Inventory inventory, Item item, Skin skin) {
        super(skin);
        this.inventory = inventory;

        this.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                inventory.update();
                select();
            }
        });

        setItem(item);
    }

    public void removeItem() {
        item = null;
        itemIcon = null;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(itemIcon != null)batch.draw(itemIcon, getX(), getY(), Vars.slotSize, Vars.slotSize);
        if(item != null)
            if(item.getQuantity() >= 2)ResourseLoader.instance.getFont("default").draw(batch, String.valueOf(item.getQuantity()), getX() + Vars.slotSize * 0.1f, getY() + Vars.slotSize * 0.9f );
    }

    public void setItem(Item item) {
        this.item = item;
        if(item == null) itemIcon = null;
        else itemIcon = ResourseLoader.instance.getTextureRegion(item.getId());
    }

    public void addItems(int quantity) {
        if(item == null) return;
        item.addQuantity(quantity);
    }

    public void subtractItems(int quantity) {
        if(item == null) return;
        item.addQuantity(-quantity);
        if(item.getQuantity() <= 0) {
            removeItem();
        }
    }

    public String getId() {
        if(item == null) return "null";
        return item.getId();
    }

    public int getQuantity() {
        if(item == null) return 0;
        return item.getQuantity();
    }

    public boolean compareItems(Item item) {
        if(this.item == null) return false;
        return this.item.compareItemId(item);
    }

    public Item getItem() {
        return item;
    }

    private void select() {
        inventory.selectItem(this);
    }

}
