package com.asdfgaems.core.objects;

import com.asdfgaems.core.GameObject;
import com.asdfgaems.core.TileMap;
import com.asdfgaems.core.World;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Door extends GameObject {
    public static Texture texture_unlocked;
    public static Texture texture_lvl_1;
    public static Texture texture_lvl_2;
    public static Texture texture_lvl_3;

    private int level;
    private boolean locked;

    public Door(World world, int x, int y, int level) {
        super(world);
        this.level = level;
        this.x = x;
        this. y = y;
        if(level == 0) {
            this.locked = false;
            collidable = false;
        }
        else {
            this.locked = true;
            collidable = true;
        }
    }

    @Override
    public float processTurn() {
        needUpdate = false;
        return 0.0f;
    }

    @Override
    public void draw(SpriteBatch batch) {
        Texture texture = texture_unlocked;
        if(level == 1) texture = texture_lvl_1;
        else if(level == 2) texture = texture_lvl_2;
        else if(level == 3) texture = texture_lvl_3;

        batch.draw(texture, x * TileMap.TILE_SIZE, y * TileMap.TILE_SIZE, TileMap.TILE_SIZE, TileMap.TILE_SIZE);
    }

    @Override
    public void update(float dt) {

    }

    public boolean isLocked() {
        return  locked;
    }

    public void unlock() {
        locked = false;
    }

    public void action(GameObject actor) {
        if(actor.getClass() == Player.class) {
            Player player = (Player)actor;
            if(level == 1 && player.inventory.takeItem(new AcessCard(1)) != null) locked = false;
            else if(level == 2 && player.inventory.takeItem(new AcessCard(2)) != null) locked = false;
            else if(level == 3 && player.inventory.takeItem(new AcessCard(3)) != null) locked = false;
            open();
        }
    }

    public void lock() {
        locked = true;
    }

    public void open() {
        if(!locked)collidable = false;
    }

    public void close() {
        collidable = true;
    }

    public int getLevel() {
        return level;
    }
}
