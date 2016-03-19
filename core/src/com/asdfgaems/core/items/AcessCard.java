package com.asdfgaems.core.items;

import com.asdfgaems.core.TileMap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AcessCard extends Item {
    public static Texture texture_lvl_1;
    public static Texture texture_lvl_2;
    public static Texture texture_lvl_3;

    private int level;

    public AcessCard(int level) {
        this.type = CARD;
        this.level = level;
        this.cost = 300;
    }

    public boolean equals(Item item){
        if(item.getClass() == AcessCard.class) {
            return this.level == ((AcessCard)item).getLevel();
        }
        else return false;
    }

    public int getLevel() {
        return level;
    }

    public Texture getTexture() {
        if(level == 1) return texture_lvl_1;
        else if(level == 2) return texture_lvl_2;
        else if(level == 3) return texture_lvl_3;
        return null;
    }

    @Override
    public void draw(SpriteBatch batch, float x, float y, float size) {
        Texture texture = texture_lvl_1;
        if(level == 2) texture = texture_lvl_2;
        else if(level == 3) texture = texture_lvl_3;

        batch.draw(texture, x * TileMap.TILE_SIZE, y * TileMap.TILE_SIZE, TileMap.TILE_SIZE, TileMap.TILE_SIZE);
    }

    public String getName() {
        return "Acess card - " + String.valueOf(level);
    }
    public String getDescription() {
        return "Level:" + String.valueOf(level) + "Acess card need to unlock doors, chests, terminals";
    }
}
