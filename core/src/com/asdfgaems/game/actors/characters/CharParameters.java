package com.asdfgaems.game.actors.characters;

import com.asdfgaems.game.buffs.Bleeding;
import com.asdfgaems.game.buffs.Ability;
import com.asdfgaems.game.buffs.Radiation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class CharParameters {
    private Array<Ability> abilities;
    private Array<String> immunities;

    private LiveActor character;

    private final float maxSatiety = 100.0f;
    private float maxHelath;

    private float health;
    private float satiety;
    private float radiation;

    private float starvingPerAction = 0.5f;
    private float hpregen;

    private Attacker attacker;

    private float physDamage;
    private float energyDamage;
    private float radiateValue;
    private float attackRange;

    private float physResist;
    private float energyResist;
    private float radiationResist;

    private float visionRange;

    public CharParameters(float health, Attacker attacker) {
        immunities = new Array<String>();
        abilities = new Array<Ability>();

        this.maxHelath = health;
        this.health = health;


        hpregen = 0.5f;
        satiety = -1;
        radiation = 0;
        physDamage = 0;
        energyDamage = 0;
        radiateValue = 0;

        physResist = 0;
        energyResist = 0;
        radiationResist = 0;
        attackRange = 1.5f;

        visionRange = 10;
        setAttacker(attacker);
    }

    public void setCharacter(LiveActor character) {
        this.character = character;
    }

    public void setDamage(float phys, float energy, float radiation) {
        physDamage = phys;
        energyDamage = energy;
        radiateValue = radiation;
    }

    public void setAttckRange(float range) {
        attackRange = range;
    }

    public float getAttackRange() {
        return attackRange;
    }

    public void setAttacker(Attacker attacker) {
        this.attacker = attacker;
        attacker.equip(this);
    }

    public void setResists(float phys, float energy, float radiation) {
        physResist = phys;
        energyResist = energy;
        radiationResist = radiation;
    }

    public void addResists(float phys, float energy, float radiation) {
        physResist += phys;
        energyResist += energy;
        radiationResist += radiation;
    }

    public void multiplyResists(float phys, float energy, float radiation) {
        physResist *= phys;
        energyResist *= energy;
        radiationResist *= radiation;
    }

    public void addHpRegen(float regen) {
        hpregen += regen;
    }

    public void addDamage(float phys, float energy, float radiation) {
        physDamage += phys;
        energyDamage += energy;
        radiateValue += radiation;
    }

    public void multiplyDamage(float phys, float energy, float radiation) {
        physDamage *= phys;
        energyDamage *= energy;
        radiateValue *= radiation;
    }

    public void processDamage(float phys, float energy, float radiation, LiveActor damager) {
        health -= phys * (1 - physResist) * MathUtils.random(0.9f, 1.1f);
        health -= energy * (1 - energyResist) * MathUtils.random(0.8f, 1.2f);
        this.radiation += radiation * (1 - radiationResist)* MathUtils.random(0.6f, 1.4f);
    }

    public void attack(int x, int y) {
        attacker.attack(physDamage, energyDamage, radiateValue, x, y, character);
    }

    public void postAction() {
        if(satiety != -1) {
            if(satiety > 0) {
                satiety -= starvingPerAction;
            }
        }

        for(Ability b : abilities) {
            b.postAction();
            if(!b.isinfinite() && b.getRemaining() <= 0) {
                b.end();
                abilities.removeValue(b, true);
            }
        }
    }


    public void addBuff(String id, int duration) {
        if(immunities.contains(id, false)) {
            return;
        }

        for(Ability b : abilities) {
            if(b.getId().equals(id)) {
                if(duration == -1) b.setInfinite(true);
                else {
                    if(b.getDuration() < duration) b.reset(duration);
                }
                return;
            }
        }

        Ability ability = null;
        boolean infinite = (duration == -1);

        if(id.equals("bleeding")) {
            ability = new Bleeding(this, duration, infinite);

        }
        else if(id.equals("radiation")) {
            ability = new Radiation(this, duration, infinite);
        }

        ability.start();
        abilities.add(ability);
    }

    public void addImmunity(String id) {
        if(!immunities.contains(id, false)) {
            immunities.add(id);
        }
    }

    public void removeImmunity(String id) {
        immunities.removeValue(id, false);
    }

    public void removeBuff(String id) {
        for(Ability b : abilities) {
            if(b.getId().equals(id)) {
                abilities.removeValue(b, true);
            }
        }
    }

    public void allowStarving() {
        satiety = 100.0f;
    }

    public void addSatiety(float val) {
        satiety += val;
        if(satiety >= maxSatiety) satiety = maxSatiety;
    }


    public void damage(float value) {
        health -= value;
        if(health < 0.0f) health = 0.0f;
    }

    public void heal(float value) {
        health += value;
        if(health > maxHelath) health = maxHelath;
    }

    public float getVisionRange() {
        return visionRange;
    }

    public Array<Ability> getAbilities() {
        return abilities;
    }


    public float getPhysResist() {
        return physResist;
    }

    public float getEnergyResist() {
        return energyResist;
    }

    public float getRadiationResist() {
        return radiationResist;
    }

    public float getHealth() {
        return health;
    }
    public float getMaxHelath() {
        return maxHelath;
    }

    public float getSatiety() {
        return satiety;
    }
    public float getMaxSatiety() {
        return maxSatiety;
    }

}
