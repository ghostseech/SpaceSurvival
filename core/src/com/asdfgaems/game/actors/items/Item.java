package com.asdfgaems.game.actors.items;

import com.asdfgaems.ResourseLoader;
import com.asdfgaems.game.actors.GameActor;
import com.asdfgaems.stages.GameStage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Item extends GameActor {
    private TextureRegion textureRegion;
    private int quantity;
    private boolean stackable;

    private String id;

    public Item(String id, GameStage stage, boolean stackable,  int quantity, int x, int y) {
        super("item", stage, x, y, 1, 1);
        this.textureRegion = ResourseLoader.instance.getTextureRegion(id);
        this.id = id;
        this.quantity = quantity;
        this.stackable = stackable;
        if(!stackable) quantity = 1;
    }

    public Array<String> getItemActions() {
        Array actions = new Array<String>();
        actions.add("drop_action");
        return actions;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawTextureRegion(batch, textureRegion);
    }

    public String getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isStackable() {
        return stackable;
    }

    public void addQuantity(int quantity) {
        if(stackable) {
            this.quantity += quantity;
        }
    }

    public boolean compareItemId(Item item) {
        if(item == null) return false;
        return id.equals(item.getId());
    }
}
