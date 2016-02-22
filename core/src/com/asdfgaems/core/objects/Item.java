package com.asdfgaems.core.objects;

import com.asdfgaems.core.GameObject;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Item {
    public static final int WEAPON = 1;
    public static final int CARD = 2;

    public String name;

    protected int type;
    protected int cost;

    public abstract void draw(SpriteBatch batch, float x, float y, float size);
    public abstract Texture getTexture();

    public int getCost() {
        return cost;
    }

    public int getType() {
        return type;
    }
}
