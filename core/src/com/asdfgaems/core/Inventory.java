package com.asdfgaems.core;

import com.asdfgaems.core.objects.Item;

import java.util.Vector;

public class Inventory {
    private Vector<Item> items;
    private int size;

    public Inventory(int size) {
        this.items = new Vector<Item>();
        this.size = size;
    }

    public void addItem(Item item) {
        if(items.size() < size) items.add(item);
    }

    public void removeItem(int id) {
        items.remove(id);
    }

    public Item getItem(int id) {
        if(id >= items.size()) return null;
        return items.get(id);
    }

    public int getSize() {
        return size;
    }

    public int getItemId(String name) {
        for(int i = 0; i < items.size(); i++) {
            if(items.get(i).name.equals(name)) return i;
        }
        return -1;
    }
}
