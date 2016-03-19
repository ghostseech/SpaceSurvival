package com.asdfgaems.core.objects;

import com.asdfgaems.core.GameObject;
import com.asdfgaems.core.Inventory;
import com.asdfgaems.core.TileMap;
import com.asdfgaems.core.World;
import com.asdfgaems.core.items.AcessCard;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Chest extends GameObject {
    public static Texture texture_lvl_1;
    public static Texture texture_lvl_2;
    public static Texture texture_lvl_3;

    public Inventory inventory;


    private int level;
    public Chest(World world, int x, int y, int level) {
        super(world);
        this.x = x;
        this.y = y;
        this.level = level;
        this.collidable = true;
        this.inventory = new Inventory(10);
    }

    @Override
    public float processTurn() {
        needUpdate = false;
        return 0.0f;
    }

    @Override
    public void update(float dt) {

    }
    public void unlock(GameObject actor) {
        if(actor.getClass() == Player.class) {
            Player player = (Player)actor;
            if(level == 1 && player.inventory.takeItem(new AcessCard(1)) != null) level = 0;
            else if(level == 2 && player.inventory.takeItem(new AcessCard(2)) != null) level = 0;
            else if(level == 3 && player.inventory.takeItem(new AcessCard(3)) != null) level = 0;
        }
    }
    public int getLevel() {
        return level;
    }

    @Override
    public void draw(SpriteBatch batch) {
        Texture texture = texture_lvl_1;
        if(level == 2) texture = texture_lvl_2;
        else if(level == 3) texture = texture_lvl_3;
        else if(level == 0) texture = texture_lvl_1;

        batch.draw(texture, x * TileMap.TILE_SIZE, y * TileMap.TILE_SIZE, TileMap.TILE_SIZE, TileMap.TILE_SIZE);
    }
}
