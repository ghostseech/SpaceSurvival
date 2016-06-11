package com.asdfgaems.game.buffs;

import com.asdfgaems.game.actors.characters.CharParameters;

public class Bleeding extends Ability {
    public Bleeding(CharParameters parameters, int duration, boolean infinite) {
        super(duration, infinite, parameters);
    }

    @Override
    public void start() {

    }

    @Override
    public void end() {

    }

    @Override
    public void postAction() {
        super.postAction();
        parameters.damage(1.0f);
    }

    @Override
    public String getId() {
        return "bleeding";
    }
}
