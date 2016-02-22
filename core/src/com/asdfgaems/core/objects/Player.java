package com.asdfgaems.core.objects;

import com.asdfgaems.core.GameObject;
import com.asdfgaems.core.Inventory;
import com.asdfgaems.core.TileMap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class Player extends GameObject {
    public static Texture texture;

    public Inventory inventory;
    private Vector2 dir;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.dir = new Vector2();
    }

    @Override
    public void processTurn() {
        x += dir.x;
        y += dir.y;

        dir.set(0.0f, 0.0f);
    }

    public void move(int dirX, int dirY) {
        dir.x = dirX;
        dir.y = dirY;
    }

    public void action(GameObject object, String objectName, double dist) {
        if(dist >= 1.5) return;;
        if(object.getClass() == Door.class) {
            Door door = (Door)object;
            if(door.isLocked()) {
                if(door.getLevel() == 1) {
                    int itemId = inventory.getItemId("CARD_1");
                    if(itemId != -1) {
                        inventory.removeItem(itemId);
                        door.unlock();
                    }
                }
                else if(door.getLevel() == 2) {
                    int itemId = inventory.getItemId("CARD_2");
                    if(itemId != -1) {
                        inventory.removeItem(itemId);
                        door.unlock();
                    }
                }
                else if(door.getLevel() == 3) {
                    int itemId = inventory.getItemId("CARD_3");
                    if(itemId != -1) {
                        inventory.removeItem(itemId);
                        door.unlock();
                    }
                }

            }
            door.unlock();
            door.open();
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture, x * TileMap.TILE_SIZE, y * TileMap.TILE_SIZE, TileMap.TILE_SIZE, TileMap.TILE_SIZE);
    }

    @Override
    public void update(float dt) {

    }
}
