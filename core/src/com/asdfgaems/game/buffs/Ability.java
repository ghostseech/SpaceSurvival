package com.asdfgaems.game.buffs;

import com.asdfgaems.game.actors.characters.CharParameters;

public abstract class Ability {
    protected CharParameters parameters;
    private int duration;
    private int remaining;
    private boolean infinite;

    public Ability(int duration, boolean infinite, CharParameters parameters) {
        this.parameters = parameters;
        this.duration = duration;
        this.remaining = duration;
        this.infinite = infinite;
    }

    public abstract void start();

    public abstract void end();

    public void postAction(){
        if(duration > 0) remaining--;
    }

    public void reset(int duration) {
        this.duration = duration;
        remaining = duration;
    }

    public void setInfinite(boolean infinite) {
        this.infinite = infinite;
    }

    public boolean isinfinite() {
        return infinite;
    }

    public int getDuration() {
        return duration;
    }

    public int getRemaining() {
        return remaining;
    }

    public String getId() {
        return "null";
    }
}
