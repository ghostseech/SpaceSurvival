package com.asdfgaems.screens.actors;

import com.asdfgaems.screens.Vars;
import com.asdfgaems.screens.stages.GameStage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameActor extends Actor {
    protected GameStage stage;

    protected int tileX;
    protected int tileY;

    private int tileWidth;
    private int tileHeight;

    protected String id;

    public GameActor(String name, GameStage stage, int x, int y, int width, int height) {
        setName(name);
        setTilePosition(x, y);
        this.tileWidth = width;
        this.tileHeight = height;
        this.stage = stage;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    protected void drawTextureRegion(Batch batch, TextureRegion texture) {
        batch.draw(texture, getX(), getY(), Vars.tileSize, Vars.tileSize);
    }

    public void setTilePosition(int x, int y) {
        setPosition(x * Vars.tileSize, y * Vars.tileSize);
        tileX = x;
        tileY = y;
    }

    public int getTileX() {
        return tileX;
    }
    public int getTileY() {
        return tileY;
    }

    public int getTileWidth() {
        return tileWidth;
    }
    public int getTileHeight() {
        return tileHeight;
    }

}
