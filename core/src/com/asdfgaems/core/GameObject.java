package com.asdfgaems.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameObject {
    public int x;
    public int y;

    public boolean collidable;

    public abstract void processTurn();
    public abstract void draw(SpriteBatch batch);
    public abstract void update(float dt);
}
