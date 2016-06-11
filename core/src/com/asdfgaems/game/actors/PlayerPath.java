package com.asdfgaems.game.actors;

import com.asdfgaems.ResourseLoader;
import com.asdfgaems.Vars;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;


import java.util.LinkedList;
import java.util.List;

public class PlayerPath extends Actor {
    private List<Vector2> path;
    private TextureRegion texture;

    public PlayerPath() {
        texture = ResourseLoader.instance.getTextureRegion("player_target");
        path = new LinkedList<Vector2>();
    }

    public void setPath(List<Vector2> path) {
        this.path = path;
    }

    public void removePath() {
        path = new LinkedList<Vector2>();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for(Vector2 v : path) batch.draw(texture, v.x * Vars.tileSize, v.y * Vars.tileSize, Vars.tileSize, Vars.tileSize);
    }
}
//TODO: 1. PlayerPath   2. MENU    3. LEVEL EDITOR    4. LIGHTS
