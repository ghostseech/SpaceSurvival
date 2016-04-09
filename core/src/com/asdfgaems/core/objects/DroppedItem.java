package com.asdfgaems.core.objects;

import com.asdfgaems.core.GameObject;
import com.asdfgaems.core.TileMap;
import com.asdfgaems.core.World;
import com.asdfgaems.core.items.Item;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DroppedItem extends GameObject {
    private Item item;

    public DroppedItem(World world, String name, Item item, int x, int y) {
        super(world, name);
        this.item = item;
        this.collidable = false;
        this.x = x;
        this.y = y;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public float processTurn() {
        return 0.0f;
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(item.getTexture(), x * TileMap.TILE_SIZE, y * TileMap.TILE_SIZE, TileMap.TILE_SIZE, TileMap.TILE_SIZE);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public float getDist(GameObject object) {
        return super.getDist(object);
    }


}
