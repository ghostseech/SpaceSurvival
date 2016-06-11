package com.asdfgaems.game.actors.items;

import com.asdfgaems.game.actors.characters.CharParameters;
import com.asdfgaems.stages.GameStage;
import com.badlogic.gdx.utils.Array;

public abstract class Armor extends Item {
    private boolean equipped;

    public Armor(String id, GameStage stage, int x, int y) {
        super(id, stage, false, 1, x, y);
    }

    public abstract void equip(CharParameters parameters);
    public abstract void unequip(CharParameters parameters);

    public void setEquipped(boolean b) {
        equipped = b;
    }
    public boolean isEquipped() {
        return equipped;
    }

    @Override
    public Array<String> getItemActions() {
        Array<String> actions = super.getItemActions();
        if(equipped) actions.add("armor_unequip_action");
        else actions.add("armor_equip_action");
        return actions;
    }

}
