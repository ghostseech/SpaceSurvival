package com.asdfgaems.game.actors.characters;

import com.asdfgaems.stages.GameStage;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class DefaultAttacker implements Attacker {
    private GameStage stage;
    private float phys;
    private float energy;
    private float radiation;
    private float range;

    public DefaultAttacker(float phys, float energy, float radiation, float range, GameStage stage) {
        this.stage = stage;
        this.phys = phys;
        this.energy = energy;
        this.radiation = radiation;
        this.range = range;

    }

    @Override
    public void attack(float physDamage, float energyDamage, float radiate, int x, int y, LiveActor damager) {
        stage.getActor(x, y).getParameters().processDamage(physDamage, energyDamage, radiate, damager);
    }

    @Override
    public void equip(CharParameters parameters) {
        parameters.setDamage(phys, energy, radiation);
        parameters.setAttckRange(range);
    }
}
