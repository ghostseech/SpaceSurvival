package com.asdfgaems.game.actors.items.armor;

import com.asdfgaems.game.actors.characters.CharParameters;
import com.asdfgaems.game.actors.items.Armor;
import com.asdfgaems.game.actors.items.Item;
import com.asdfgaems.stages.GameStage;

public class ClothArmor extends Armor {
    public ClothArmor(GameStage stage, int x, int y) {
        super("cloth_armor", stage, x, y);
    }

    @Override
    public void equip(CharParameters parameters) {
        parameters.addResists(0.05f, 0.0f, 0.0f);
    }

    @Override
    public void unequip(CharParameters parameters) {
        parameters.addResists(-0.05f, 0.0f, 0.0f);
    }
}
