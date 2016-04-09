package com.asdfgaems.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {
    protected World world;
    private String name;

    public int x;
    public int y;

    public boolean collidable;
    public boolean needUpdate;

    public GameObject(World world, String name) {
        this.world = world;
        this.needUpdate = false;
        this.name = name;
        needUpdate = false;
    }

    public abstract float processTurn();
    public abstract void draw(SpriteBatch batch);
    public abstract void update(float dt);
    public float getDist(GameObject object) {
        return (float) Vector2.dst((float)x, (float)y, (float)object.x, (float)object.y);
    }
    public String getIdName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
