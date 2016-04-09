package com.asdfgaems.core.objects;

import com.asdfgaems.core.GameObject;
import com.asdfgaems.core.TileMap;
import com.asdfgaems.core.World;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Enemy extends GameObject implements LiveObject {
    public static Texture health_texture;
    protected float health;
    protected float maxHealth;

    public Enemy(World world, String name, int x, int y) {
        super(world, name);
        this.x = x;
        this.y = y;
    }

    public abstract float processTurn();
    public abstract void draw(SpriteBatch batch);
    public abstract void update(float dt);

    protected void drawHp(SpriteBatch batch) {
        batch.draw(health_texture, x * TileMap.TILE_SIZE, (y + 1) * TileMap.TILE_SIZE, TileMap.TILE_SIZE * health/maxHealth, 20.0f);
    }

    @Override
    public void damage(float damage, LiveObject object) {
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
