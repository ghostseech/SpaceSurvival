package com.asdfgaems.screens.utils;

import com.asdfgaems.screens.ResourseLoader;
import com.asdfgaems.screens.Vars;
import com.asdfgaems.screens.actors.TileMap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TileDrawer {
    private TextureRegion wall;

    public TileDrawer() {
        wall = ResourseLoader.instance.getTextureRegion("wall_1");
    }

    public void drawFloor(Batch batch, TileMap.Tile tile, int x, int y) {
        batch.draw(tile.floor, x * Vars.tileSize, y * Vars.tileSize, Vars.tileSize, Vars.tileSize);
    }

    public void drawTile(Batch batch, TileMap.Tile tile, int x, int y) {
        switch (tile.tile) {
            case wall:
                batch.draw(wall, x * Vars.tileSize, (y - 1) * Vars.tileSize, Vars.tileSize * 2, Vars.tileSize * 2);
                break;
        }
    }

}
