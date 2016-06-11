package com.asdfgaems.game.actors.objects;

import com.asdfgaems.ResourseLoader;
import com.asdfgaems.game.actors.GameActor;
import com.asdfgaems.stages.GameStage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Door extends GameActor {
    private TextureRegion textureClose;
    private TextureRegion textureOpen;

    private int timeToAutoClose;
    private int level;
    private boolean closed;

    public Door(String name, GameStage stage, int level, int x, int y) {
        super(name, stage, x, y, 1, 1);
        setLevel(level);
        closed = true;
        textureOpen = ResourseLoader.instance.getTextureRegion("door_opened");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(closed)drawTextureRegion(batch, textureClose);
        else drawTextureRegion(batch, textureOpen);
    }

    public void setLevel(int level) {
        this.level = level;
        if(level == 0) textureClose = ResourseLoader.instance.getTextureRegion("door_0");
        else if(level == 1) textureClose = ResourseLoader.instance.getTextureRegion("door_1");
        else if(level == 2) textureClose = ResourseLoader.instance.getTextureRegion("door_2");
        else if(level == 3) textureClose = ResourseLoader.instance.getTextureRegion("door_3");
    }

    public void open() {
        closed = false;
        timeToAutoClose = 2;
    }

    public void close() {
        closed = true;
        timeToAutoClose = 0;
    }

    public boolean isClosed() {
    return closed;
    }

    public int getLevel() {
        return level;
    }

    public boolean autoClose() {
        if(closed) return false;
        if(timeToAutoClose == 0) {
            close();
            return true;
        }
        else {
            timeToAutoClose --;
            return false;
        }
    }
}
