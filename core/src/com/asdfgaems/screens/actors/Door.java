package com.asdfgaems.screens.actors;

import com.asdfgaems.screens.ResourseLoader;
import com.asdfgaems.screens.stages.GameStage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Door extends GameActor {
    private TextureRegion textureClose;
    private TextureRegion textureOpen;

    private int level;

    public Door(String name, GameStage stage, int x, int y) {
        super(name, stage, x, y, 1, 1);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawTextureRegion(batch, textureClose);
    }

    public static Door create(String name, GameStage stage, int level, int x, int y) {
        Door door = new Door(name, stage, x, y);
        door.setLevel(level);
        return door;
    }

    public void setLevel(int level) {
        this.level = level;
        if(level == 0) textureClose = ResourseLoader.instance.getTextureRegion("door_0");
        else if(level == 1) textureClose = ResourseLoader.instance.getTextureRegion("door_1");
        else if(level == 2) textureClose = ResourseLoader.instance.getTextureRegion("door_2");
        else if(level == 3) textureClose = ResourseLoader.instance.getTextureRegion("door_3");
    }

    public int getLevel() {
        return level;
    }
}
