package com.asdfgaems.game.actors.characters;

public interface Attacker {
    public void attack(float physDamage, float energyDamage, float radiate, int x, int y, LiveActor damager);
    public void equip(CharParameters parameters);
}
