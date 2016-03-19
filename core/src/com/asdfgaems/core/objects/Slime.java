package com.asdfgaems.core.objects;

import com.asdfgaems.core.GameObject;
import com.asdfgaems.core.World;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;

public class Slime extends Enemy {
    protected LinkedList<Vector2> path;

    public Slime(World world) {
        super(world);
    }

    @Override
    public float processTurn() {
        if(path.size() > 0) {
            x += path.getFirst().x;
            y += path.getFirst().y;
            path.removeFirst();
        }
        return 0.5f;
    }

    @Override
    public void draw(SpriteBatch batch) {

    }

    @Override
    public void update(float dt) {

    }
}
