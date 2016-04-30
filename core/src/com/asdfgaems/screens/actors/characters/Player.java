package com.asdfgaems.screens.actors.characters;

import com.asdfgaems.screens.ResourseLoader;
import com.asdfgaems.screens.actors.Door;
import com.asdfgaems.screens.actors.GameActor;
import com.asdfgaems.screens.actors.ui.Inventory;
import com.asdfgaems.screens.stages.GameStage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends LiveActor {
    private Inventory inventory;
    private TextureRegion texture;

    public Player(GameStage stage, int x, int y) {
        super("player", stage, 2, new CharParameters(100.0f), x, y, 1, 1);
        texture = ResourseLoader.instance.getTextureRegion("player");
        inventory = new Inventory(24);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawTextureRegion(batch, texture);
    }

    public Inventory getInventory() {
        return inventory;
    }
}
