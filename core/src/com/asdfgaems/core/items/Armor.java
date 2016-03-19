package com.asdfgaems.core.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Armor extends Item {
    public static final int ARMOR_BASE = 1;

    public static Texture texture_base;

    private int type;
    private float physResist;
    private float energyResist;

    public Armor(int type) {
        this.type = type;
        if(type == ARMOR_BASE) {
            physResist = 0.05f;
            energyResist = 0.05f;
        }
    }
    public void draw(SpriteBatch batch, float x, float y, float size) {

    }

    public Texture getTexture() {
        if(type == ARMOR_BASE) return texture_base;

        return texture_base;
    }
    public boolean equals(Item item) {
        if(item.getClass() != Armor.class) return false;

        if(((Armor)item).type == type) return true;
        else return false;
    }
    public String getName() {
        if(type == ARMOR_BASE) return "Worker cloth";
        return "Error";
    }
    public String getDescription() {
        if(type == ARMOR_BASE) return "Standard space station workers cloth  with low resistance";
        return "Error";
    }
    public float getPhysResist() {
        return physResist;
    }
    public float getEnergyResist() {
        return energyResist;
    }
}
