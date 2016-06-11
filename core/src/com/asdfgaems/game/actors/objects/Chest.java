package com.asdfgaems.game.actors.objects;

import com.asdfgaems.ResourseLoader;
import com.asdfgaems.game.actors.GameActor;
import com.asdfgaems.game.actors.ui.Inventory;
import com.asdfgaems.stages.GameStage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Chest extends GameActor {
    private Inventory inventory;
    private TextureRegion textureClose;
    private TextureRegion textureOpen;
    private boolean closed;
    private int level;

    public Chest(String name, GameStage stage, int level, int x, int y) {
        super(name, stage, x, y, 1, 1);
        inventory = new Inventory(24);
        setLevel(level);
        textureOpen = ResourseLoader.instance.getTextureRegion("chest_opened");
        closed = true;
    }

    public void setLevel(int level) {
        this.level = level;
        if(level == 0) textureClose = ResourseLoader.instance.getTextureRegion("chest_0");
        else if(level == 1) textureClose = ResourseLoader.instance.getTextureRegion("chest_1");
        else if(level == 2) textureClose = ResourseLoader.instance.getTextureRegion("chest_2");
        else if(level == 3) textureClose = ResourseLoader.instance.getTextureRegion("chest_3");
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getLevel() {
        return level;
    }

    public void open() {
        closed = false;
    }

    public void close() {
        closed = true;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(closed)drawTextureRegion(batch, textureClose);
        else drawTextureRegion(batch, textureOpen);
    }
}
