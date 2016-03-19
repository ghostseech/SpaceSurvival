package com.asdfgaems.core.items;

import com.asdfgaems.core.objects.Player;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Consumable extends Item {
    public static final int FOOD_RATION_1 = 1;
    public static final int HEALTH_1 = 2;

    public static Texture texture_food_1;
    public static Texture texture_health_1;

    private int type;

    public Consumable(int type) {
        this.type = type;
        if(type == FOOD_RATION_1) {

        }
        else if(type == HEALTH_1) {

        }
    }
    public void draw(SpriteBatch batch, float x, float y, float size) {

    }

    public Texture getTexture() {
        if(type == FOOD_RATION_1) {
            return texture_food_1;
        }
        else if(type == HEALTH_1) {
            return texture_health_1;
        }
        return  texture_food_1;
    }
    public boolean equals(Item item) {
        if(item.getClass() != Consumable.class) return false;
        if(((Consumable)item).type == type) return true;

        return false;
    }
    public String getName() {
        if(type == FOOD_RATION_1) return "Food ration";
        else if(type == HEALTH_1) return "Medkit";
        return "Error";
    }
    public String getDescription() {
        if(type == FOOD_RATION_1) return "Simple food ration";
        else if(type == HEALTH_1) return "Simple medkit which ca heal injures";
        return "Error";
    }
    public void consume(Player player) {
        if(type == FOOD_RATION_1) {
            player.addSatiety(40.0f);
        }
        else if(type == HEALTH_1) {
            player.damage(-40.0f);
        }
    }
}
