package com.asdfgaems.screens.actors;

import com.asdfgaems.screens.ResourseLoader;
import com.asdfgaems.screens.Vars;
import com.asdfgaems.screens.utils.TileDrawer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TileMap extends Actor {
    private Tile tiles[][];
    private TileDrawer tileDrawer;
    private int width;
    private int height;

    public TileMap() {
        create(32, 32);
    }

    public void create(int width, int height) {
        this.width = width;
        this.height = height;

        tiles = new Tile[width][height];

        for(int x = 0; x < width; x++)
            for(int y = 0; y < height; y++) {
                tiles[x][y] = new Tile(ResourseLoader.instance.getTextureRegion("floor_1"), null, false);
            }
        tileDrawer = new TileDrawer();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        for(int x = 0; x < width; x++)
            for(int y = 0; y < height; y++) {
                tileDrawer.drawFloor(batch, tiles[x][y], x, y);
            }

        for(int y = width - 1; y >= 0; y--)
            for(int x = 0; x < height; x++) {
                tileDrawer.drawTile(batch, tiles[x][y], x, y);
            }
    }

    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    public void load(String levelId) {
        TiledMap tiledMap;
        int width;
        int height;

        tiledMap = new TmxMapLoader().load(levelId);

        //FLOOR INITIALIZATION
        TiledMapTileLayer floorLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
        width = floorLayer.getWidth();
        height = floorLayer.getHeight();
        create(width, height);

        TiledMapTileLayer wallLayer = (TiledMapTileLayer)tiledMap.getLayers().get(1);

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                String floorType = floorLayer.getCell(x, y).getTile().getProperties().get("type", String.class);
                String wallType;
                boolean collidable = false;

                if(wallLayer.getCell(x, y) == null) {
                    wallType = null;
                    collidable = false;
                }
                else {
                    wallType = wallLayer.getCell(x, y).getTile().getProperties().get("type", String.class);
                    collidable = true;
                }

                getTile(x, y).floor = ResourseLoader.instance.getTextureRegion(floorType);
                getTile(x, y).tile = getTileType(wallType);
                getTile(x, y).collidable = collidable;
            }

    }

    private TileType getTileType(String tile) {
        if(tile == null) return TileType.nothing;
        if(tile.equals("wall_1")) return TileType.wall;
        return TileType.nothing;
    }

    public enum TileType {
        nothing,
        wall;
    }

    public class Tile {
        public boolean collidable;
        public TextureRegion floor;
        public TileType tile;

        public Tile(TextureRegion floor, TileType tile, boolean collidable) {
            this.collidable = collidable;
            this.floor = floor;
            this.tile = tile;
        }
    }
}
