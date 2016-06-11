package com.asdfgaems.utils;

import com.asdfgaems.ResourseLoader;
import com.asdfgaems.Vars;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

public class LightMap extends Actor {
    private int width;
    private int height;

    private boolean visited[][];
    private boolean visible[][];

    private TextureRegion notVisited;
    private TextureRegion notVisible;

    public LightMap(int width, int height) {
        this.height = height;
        this.width = width;
        visible = new boolean[width][height];
        visited = new boolean[width][height];

        notVisible = ResourseLoader.instance.getTextureRegion("not_visible");
        notVisited = ResourseLoader.instance.getTextureRegion("not_visited");
    }

    public void resetLights() {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                visible[x][y] = false;
            }
    }

    public boolean isVisible(int x, int y) {
        return visible[x][y];
    }
    public boolean isVisited(int x, int y) {
        if(x >= width || x < 0 || y >= height || y < 0) return false;
        return visited[x][y];
    }

    public void visit(int x, int y) {
        visible[x][y] = true;
        visited[x][y] = true;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                if(!visited[x][y]) batch.draw(notVisited, x * Vars.tileSize, y * Vars.tileSize, Vars.tileSize, Vars.tileSize);
                else {
                    if(!visible[x][y]) batch.draw(notVisible, x * Vars.tileSize, y * Vars.tileSize, Vars.tileSize, Vars.tileSize);
                }
            }
    }
}
