package com.asdfgaems.screens.actors.characters;

public class CharParameters {
    private float health;
    private float maxHelath;

    public CharParameters(float health) {
        this.maxHelath = health;
        this.health = health;
    }

    public void postAction() {

    }

    public void damage(float value) {
        health -= value;
        if(health < 0.0f) health = 0.0f;
    }

    public void heal(float value) {
        health += value;
        if(health > maxHelath) health = maxHelath;
    }
}
