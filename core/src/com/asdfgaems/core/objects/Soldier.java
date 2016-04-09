package com.asdfgaems.core.objects;

import com.asdfgaems.core.TileMap;
import com.asdfgaems.core.World;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class Soldier extends Enemy {
//   public static final int

    public static Texture texture_type_1;
    private static final int RANDOM_WALKING = 1;
    private static final int PLAYER_CHASING = 2;
    protected List<Vector2> path;

    private int lastPlayerX;
    private int lastPlayerY;

    private Texture texture;
    private int state;

    private int chaseTime;

    private int type;

    public Soldier(World world, String name, int type, int x, int y) {
        super(world, name, x ,y);
        texture = texture_type_1;
        state = RANDOM_WALKING;
        collidable = true;
        maxHealth = 200.0f;
        health = 200.0f;
        lastPlayerX = 0;
        lastPlayerY = 0;
        chaseTime = 0;
    }

    @Override
    public float processTurn() {
        needUpdate = false;
        if(state == RANDOM_WALKING) {
            randomWalking();
        }
        else if(state == PLAYER_CHASING) {
            playerChasing();
        }

        return 0.2f;
    }

    private void randomWalking() {
        if(world.player.getDist(this) <= 4 && world.isVisibleFrom(x, y, world.player.x, world.player.y)) {
            state = PLAYER_CHASING;
            playerChasing();
            return;
        }


        int dirx = MathUtils.random(-1, 1);
        int diry = MathUtils.random(-1, 1);
        if(!world.isCollidable(x + dirx, y + diry)) {
            x += dirx;
            y += diry;
        }
    }

    private void playerChasing() {
        world.cancelPlayerMoveing();
        if(world.player.getDist(this) > 12) {
            state = RANDOM_WALKING;
            randomWalking();
            lastPlayerX = 0;
            lastPlayerY = 0;
            chaseTime = 0;
            return;
        }
        if(chaseTime <= 4) {
            lastPlayerX = world.player.x;
            lastPlayerY = world.player.y;
        }
        if(lastPlayerX != 0 && lastPlayerY !=0 && !world.isVisibleFrom(x, y, world.player.x, world.player.y) && chaseTime > 4) {
            if(x == lastPlayerX && y == lastPlayerY) {
                state = RANDOM_WALKING;
                randomWalking();
                lastPlayerX = 0;
                lastPlayerY = 0;
                chaseTime = 0;
                return;
            }
        }
        if(world.player.getDist(this) <= 1.5f) {
            world.player.damage(40.0f, this);
        }
        else {
            if(world.isVisibleFrom(x, y, world.player.x, world.player.y)) {
                path = world.getPath(x, y, world.player.x, world.player.y);
                lastPlayerX = world.player.x;
                lastPlayerY = world.player.y;
                if(path.size() > 0) {
                    x = (int)path.get(0).x;
                    y = (int)path.get(0).y;
                }
            }
            else {
                path = world.getPath(x, y, lastPlayerX, lastPlayerY);
                if(path.size() > 0) {
                    x = (int)path.get(0).x;
                    y = (int)path.get(0).y;
                }
            }
        }
        chaseTime ++;
    }


    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture, x * TileMap.TILE_SIZE, y * TileMap.TILE_SIZE, TileMap.TILE_SIZE, TileMap.TILE_SIZE);
        drawHp(batch);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void damage(float damage, LiveObject object) {
        super.damage(damage, object);
        state = PLAYER_CHASING;
        chaseTime = 0;
        lastPlayerX = world.player.x;
        lastPlayerY = world.player.y;
    }

    @Override
    public void death() {

    }


}
