package com.asdfgaems.game.actors.items;

import com.asdfgaems.game.buffs.Ability;
import com.asdfgaems.game.actors.characters.CharParameters;
import com.asdfgaems.game.actors.characters.Player;
import com.asdfgaems.stages.GameStage;
import com.badlogic.gdx.utils.Array;

import java.util.LinkedList;

public class Consumable extends Item {
    private float damage;
    private float satiety;
    private LinkedList<Ability> buffs;

    public Consumable(String id, GameStage stage, int quantity, int x, int y) {
        super(id, stage, true, quantity, x, y);
    }

    public void consume(Player player) {
        CharParameters parameters = player.getParameters();
        if(getId().equals("medkit_1")) {
            parameters.heal(40.0f);
            parameters.removeBuff("bleeding");
        }
        else if(getId().equals("food_ration_1")) {
            parameters.addSatiety(40.0f);
        }
    }

    @Override
    public Array<String> getItemActions() {
        Array<String> array = super.getItemActions();
        array.add("consume_action");
        return array;
    }
}
