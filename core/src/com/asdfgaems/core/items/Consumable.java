package com.asdfgaems.core.items;

import com.asdfgaems.core.objects.Player;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Consumable extends Item {
    public static final int FOOD_RATION_1 = 1;
    public static final int HEALTH_1 = 2;

    public static final int REGEN_MIXTURE = 3;
    public static final int ANTIRAD_MIXTURE = 4;
    public static final int ANTITOXIC_MIXTURE = 5;
    public static final int ATTENTION_MIXTURE = 6;



    public static Texture texture_food_1;
    public static Texture texture_health_1;
    public static Texture texture_regen_mixture;
    public static Texture texture_antirad_mixture;
    public static Texture texture_antitoxic_mixture;
    public static Texture texture_attention_mixture;

    private int type;

    private Texture texture;

    public Consumable(int type) {
        this.type = type;
        if(type == FOOD_RATION_1) {
            texture = texture_food_1;
        }

        else if(type == HEALTH_1) {
            texture = texture_health_1;
        }
        else if(type == REGEN_MIXTURE) {
            texture = texture_regen_mixture;
        }

        else if(type == ANTIRAD_MIXTURE) {
            texture = texture_antirad_mixture;
        }

        else if(type == ANTITOXIC_MIXTURE) {
            texture = texture_antitoxic_mixture;
        }

        else if(type == ATTENTION_MIXTURE) {
            texture = texture_attention_mixture;
        }
        else texture = texture_food_1;
    }
    public void draw(SpriteBatch batch, float x, float y, float size) {

    }

    public Texture getTexture() {
        return  texture;
    }
    public boolean equals(Item item) {
        if(item.getClass() != Consumable.class) return false;
        if(((Consumable)item).type == type) return true;

        return false;
    }
    public String getName() {
        if(type == FOOD_RATION_1) return "Food ration";
        else if(type == HEALTH_1) return "Medkit";
        else if(type == REGEN_MIXTURE) return "Regen mxiture";
        else if(type == ANTIRAD_MIXTURE) return "Antird mixture";
        else if(type == ANTITOXIC_MIXTURE) return "Antitoxic mixture";
        else if(type == ATTENTION_MIXTURE) return "Attention mixture";
        return "Error";
    }
    public String getDescription() {
        if(type == FOOD_RATION_1) return "Simple food ration";
        else if(type == HEALTH_1) return "Simple medkit which ca heal injures";
        else if(type == REGEN_MIXTURE) return "Regen mxiture";
        else if(type == ANTIRAD_MIXTURE) return "Antird mixture";
        else if(type == ANTITOXIC_MIXTURE) return "Antitoxic mixture";
        else if(type == ATTENTION_MIXTURE) return "Attention mixture";
        return "Error";
    }
    public int getType() {
        return type;
    }

}
