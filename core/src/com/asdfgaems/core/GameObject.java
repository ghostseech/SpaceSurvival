package com.asdfgaems.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {
    public int x;
    public int y;

    public boolean collidable;

    public abstract void processTurn();
    public abstract void draw(SpriteBatch batch);
    public abstract void update(float dt);
    public float getDist(GameObject object) {
        return (float) Vector2.dst((float)x, (float)y, (float)object.x, (float)object.y);
    }
}
