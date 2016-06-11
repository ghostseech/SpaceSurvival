package com.asdfgaems.game.actors.items;

import com.asdfgaems.game.actors.characters.Attacker;
import com.asdfgaems.game.actors.characters.LiveActor;
import com.asdfgaems.stages.GameStage;
import com.badlogic.gdx.utils.Array;

public abstract class Weapon extends Item implements Attacker {
    public boolean equipped;

    public Weapon(String id, GameStage stage, int x, int y) {
        super(id, stage, false, 1, x, y);
        equipped = false;
    }

    public void attack(float physDamage, float energyDamage, float radiate, int x, int y, LiveActor damager) {
        stage.getActor(x, y).getParameters().processDamage(physDamage, energyDamage, radiate, damager);
    }

    public float getAttackRange() {
        return 1.5f;
    }

    public void setEquipped(boolean b) {
        equipped = b;
    }
    public boolean isEquipped() {
        return equipped;
    }

    @Override
    public Array<String> getItemActions() {
        Array<String> actions = super.getItemActions();
        if(equipped) actions.add("weapon_unequip_action");
        else actions.add("weapon_equip_action");
        return actions;
    }
    public String getRequiredAmmo() {
        return "null";
    }
}
