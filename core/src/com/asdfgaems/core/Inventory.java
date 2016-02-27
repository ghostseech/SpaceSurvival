package com.asdfgaems.core;

import com.asdfgaems.core.objects.Item;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.Vector;

public class Inventory {
    private Vector<Item> items;
    private int size;

    private ScrollPane ui;
    private Inventory connected;

    public Inventory(int size) {
        this.items = new Vector<Item>();
        this.size = size;

        Table table = new Table();
        table.setSize(80.0f, size * 70.0f);
        table.padTop(30.0f);
        Vector<Image> inventoryButtons = new Vector<Image>();
        for(int i = 0; i < size; i++) {

            Image img = new Image();

            inventoryButtons.add(img);
            table.add(img).size(64.0f, 64.0f);
            table.row();
        }
        this.ui = new ScrollPane(table);

        ui.setSize(80.0f, 600.0f);
        ui.setScrollingDisabled(true, false);
    }

    public void addItem(Item item) {
        if(items.size() < size) items.add(item);
        updateUi();
    }

    public void removeItem(int id) {
        items.remove(id);
        updateUi();
    }

    public Item getItem(int id) {
        if(id >= items.size()) return null;
        return items.get(id);
    }

    public int getSize() {
        return size;
    }

    public int getItemId(Item item) {
        for(int i = 0; i < items.size(); i++) {
            if(items.get(i).equals(item)) return i;
        }
        return -1;
    }

    public Item takeItem(Item item) {
        int id = getItemId(item);
        if(id == -1) return null;
        else {
            Item result = getItem(id);
            items.remove(result);
            updateUi();
            return result;
        }
    }

    public boolean hasItem(Item item) {
        return getItemId(item) != -1;
    }

    public void dispose() {
        items.clear();
        ui.clear();
    }

    public ScrollPane getUi() {
        return ui;
    }

    public static void connectInventories(Inventory inv1, Inventory inv2) {
        inv1.connected = inv2;
        inv2.connected = inv1;
        inv1.updateUi();
        inv2.updateUi();
    }
    public void disconnect() {
        connected.connected = null;
        connected = null;
    }

    public void updateUi() {
        for(int i = 0; i < size; i++) {
            Image img = (Image)((Table)ui.getWidget()).getChildren().get(i);

            img.clearListeners();
            Item cur = getItem(i);
            if(cur != null) {
                TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(cur.getTexture()));
                img.setDrawable(drawable);
                if(connected != null)img.addListener(new inventoryClickListener(i, this, connected));
            }
            else img.setDrawable(null);
        }
    }

    class inventoryClickListener extends ClickListener {
        int id;
        Inventory dest;
        Inventory loc;
        public inventoryClickListener(int id, Inventory loc, Inventory dest) {
            this.id = id;
            this.loc = loc;
            this.dest = dest;
        }
        @Override
        public void clicked(InputEvent event, float x, float y) {
            if(getTapCount() == 1 && dest != null && loc != null) {
                System.out.println(id);
                dest.addItem(loc.takeItem(loc.getItem(id)));
                Inventory tmp = dest;
                dest = loc;
                loc = tmp;
                dest.updateUi();
                loc.updateUi();
            }
        }
    };
}
