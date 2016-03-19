package com.asdfgaems.core.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Item {
    public static final int WEAPON = 1;
    public static final int CARD = 2;

    protected int type;
    protected int cost;

    public abstract void draw(SpriteBatch batch, float x, float y, float size);
    public abstract Texture getTexture();
    public abstract boolean equals(Item item);
    public abstract String getName();
    public abstract String getDescription();

    public int getCost() {
        return cost;
    }

    public int getType() {
        return type;
    }
}
