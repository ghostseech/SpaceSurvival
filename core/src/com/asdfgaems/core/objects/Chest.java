package com.asdfgaems.core.objects;

import com.asdfgaems.core.GameObject;
import com.asdfgaems.core.TileMap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Chest extends GameObject {
    public static Texture texture_lvl_1;
    public static Texture texture_lvl_2;
    public static Texture texture_lvl_3;

    private int level;

    public Chest(int x, int y, int level) {
        this.x = x;
        this.y = y;
        this.level = level;
        this.collidable = true;
    }

    @Override
    public void processTurn() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void draw(SpriteBatch batch) {
        Texture texture = texture_lvl_1;
        if(level == 2) texture = texture_lvl_2;
        else if(level == 3) texture = texture_lvl_3;

        batch.draw(texture, x * TileMap.TILE_SIZE, y * TileMap.TILE_SIZE, TileMap.TILE_SIZE, TileMap.TILE_SIZE);
    }
}
