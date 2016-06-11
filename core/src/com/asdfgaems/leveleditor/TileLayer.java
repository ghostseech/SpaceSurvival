package com.asdfgaems.leveleditor;

import com.asdfgaems.ResourseLoader;
import com.asdfgaems.Vars;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TileLayer extends Actor {
    private String[][] tiles;
    private TextureRegion[][] textures;

    public TileLayer() {
        tiles = new String[64][64];
        for(int i = 0; i < 64; i++) {
            for(int j = 0; j < 64; j++) {
                tiles[i][j] = "null";
            }
        }
        updateTextures();
    }

    public void setTile(int x, int y, String type) {
        if(x > tiles.length - 1  || y > tiles[0].length -1 || x < 0 || y < 0) return;
        tiles[x][y] = type;
        updateTileTexture(x, y);
    }

    public String getTile(int x, int y) {
        if(x > tiles.length - 1  || y > tiles[0].length -1 || x < 0 || y < 0) return "null";
        return tiles[x][y];
    }

    public void setMapSize(int w, int h) {
        String[][] prev = tiles;
        tiles = new String[w][h];
        for(int i = 0; i < w; i++) {
            for(int j = 0; j < h; j++) {
                tiles[i][j] = "null";
            }
        }

        for(int x = 0; x < prev.length; x ++) {
            for(int y = 0; y < prev[x].length; x++) {
                if(x > w || y > h) continue;
                tiles[x][y] = prev[x][y];
            }
        }
    }

    private void updateTileTexture(int x, int y) {
        if(tiles[x][y] != "null") textures[x][y] = ResourseLoader.instance.getTextureRegion(tiles[x][y]);
        else textures[x][y] = null;
    }

    private void updateTextures() {
        textures = new TextureRegion[tiles.length][tiles[0].length];
        for(int x = 0; x < textures.length; x++) {
            for(int y = 0; y < textures[0].length; y++) {
                updateTileTexture(x, y);
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for(int x = 0; x < textures.length; x++) {
            for(int y = 0; y < textures[0].length; y++) {
                if(textures[x][y] != null) batch.draw(textures[x][y], x * Vars.tileSize, y * Vars.tileSize, Vars.tileSize, Vars.tileSize);
            }
        }
    }

}
