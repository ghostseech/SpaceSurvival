package com.asdfgaems.core.objects;

import com.asdfgaems.core.GameObject;
import com.asdfgaems.core.Inventory;
import com.asdfgaems.core.TileMap;
import com.asdfgaems.core.World;
import com.asdfgaems.core.items.Armor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;

import java.util.LinkedList;
import java.util.List;

public class Player extends GameObject implements LiveObject{
    public static Texture texture;

    public Inventory inventory;
    private Vector2 dir;
    private List<Vector2> path;

    private float satiety;
    private float health;

    private float timer;

    private Armor armor;

    public Player(World world, int x, int y) {
        super(world);
        this.x = x;
        this.y = y;
        this.dir = new Vector2();
        path = new LinkedList<Vector2>();

        health = 100.0f;
        satiety = 100.0f;
    }

    @Override
    public float processTurn() {
        needUpdate = false;
        if(path.size() > 0) {
            needUpdate = true;
            Vector2 cur = path.get(0);
            int tmpx = (int)cur.x - x;
            int tmpy = (int)cur.y - y;
            path.remove(0);
            dir.x = cur.x - x;
            dir.y = cur.y - y;
        }

        x += dir.x;
        y += dir.y;

        dir.set(0.0f, 0.0f);

        return 0.5f;
    }

    public void move(int dirX, int dirY) {
        dir.x = dirX;
        dir.y = dirY;
    }

    public void move(List<Vector2> path) {
        this.path = path;
        world.requestTurnProcess();
    }

    public void action(GameObject object, String objectName, float dist) {
        if(dist >= 1.5f) return;;
        if(object.getClass() == Door.class) {
            Door door = (Door)object;
            if(door.isLocked()) {
                door.action(this);
            }
        }
        else if(object.getClass() == Chest.class) {
            Chest chest = (Chest)object;
            if(chest.getLevel() != 0) {
                chest.unlock(this);
            }
        }
    }
    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture, x * TileMap.TILE_SIZE, y * TileMap.TILE_SIZE, TileMap.TILE_SIZE, TileMap.TILE_SIZE);
    }

    @Override
    public void update(float dt) {

    }

    public void equipArmor(Armor armor) {
        this.armor = armor;
    }
    public Armor getArmor() {
        return armor;
    }

    public void addSatiety(float val) {
        satiety += val;
        if(satiety > 100.0f) satiety = 100.0f;
    }
    public float getSatiety() {
        return satiety;
    }
    public void damage(float damage) {
        health -= damage;
        if(armor != null) health += damage * armor.getPhysResist();

        if(health < 0.0f) health = 0.0f;
        if(health > 100.0f) health = 100.0f;
    }

    public void setHp(float hp) {
        health = hp;

    }
    public float getHp() {
        return health;
    }

    public void dispose() {
        inventory.dispose();
    }
}
