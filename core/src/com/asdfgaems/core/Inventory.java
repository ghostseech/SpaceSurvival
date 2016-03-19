package com.asdfgaems.core;

import com.asdfgaems.core.items.Item;

import java.util.LinkedList;

public class Inventory {
    private LinkedList<Item> items;
    private int size;

    public Inventory(int size) {
        this.items = new LinkedList<Item>();
        this.size = size;
    }

    public void addItem(Item item) {
        if(items.size() < size) items.add(item);
    }

    public void removeItem(int id) {
        items.remove(id);
    }

    public boolean isFull() {
        System.out.println(size + " " + items.size());
        return size <= items.size();
    }

    public Item getItem(int id) {
        if(id >= items.size()) return null;
        return items.get(id);
    }

    public int getSize() {
        return size;
    }

    public int getItemsCount() {
        return items.size();
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
            return result;
        }
    }

    public Item takeItem(int id) {
        if(id == -1) return null;
        else {
            Item result = getItem(id);
            items.remove(result);
            return result;
        }
    }

    public boolean checkItem(Item item) {
        return (items.indexOf(item) != -1);
    }
    public boolean hasItem(Item item) {
        return getItemId(item) != -1;
    }

    public void dispose() {
        items.clear();
    }

}
