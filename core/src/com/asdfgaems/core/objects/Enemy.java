package com.asdfgaems.core.objects;

import com.asdfgaems.core.GameObject;
import com.asdfgaems.core.World;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Enemy extends GameObject implements LiveObject {
    protected float health;
    protected float maxHealth;

    public Enemy(World world) {
        super(world);
    }

    public abstract float processTurn();
    public abstract void draw(SpriteBatch batch);
    public abstract void update(float dt);

    @Override
    public void damage(float damage) {
        health -= damage;
        if(health < 0) health = 0.0f;
        if(health > maxHealth) health = maxHealth;
    }

    @Override
    public void setHp(float hp) {
        health = hp;
    }

    @Override
    public float getHp() {
        return health;
    }
}
