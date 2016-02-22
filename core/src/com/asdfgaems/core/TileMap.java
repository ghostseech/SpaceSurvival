package com.asdfgaems.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TileMap {
    public static float TILE_SIZE = 64.0f;

    public static final int EMPTY =1;
    public static final int WALL = 2;

    public class Tile {
        public Texture background;
        public Texture texture;
        public boolean collidable;
    };

    private Tile[][] tiles;

    public final int width;
    public final int height;

    public TileMap(int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new Tile[width][height];
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                Tile tmp = new Tile();
                tmp.collidable = false;
                tmp.texture = null;
                tiles[i][j] = tmp;
            }
        }
    }

    public Tile getTile(int w, int h) {
        if(w > width || h > height) return null;
        return tiles[w][h];
    }

    public void draw(SpriteBatch batch) {
        for(int x = 0; x < width; x++)
            for(int y = 0; y < height; y++) {
                Tile current = getTile(x, y);
                if(current.background != null) batch.draw(current.background, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                if(current.texture != null) batch.draw(current.texture, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
    }
}
