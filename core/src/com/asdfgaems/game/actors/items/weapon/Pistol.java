package com.asdfgaems.game.actors.items.weapon;

import com.asdfgaems.game.actors.characters.CharParameters;
import com.asdfgaems.game.actors.characters.LiveActor;
import com.asdfgaems.game.actors.items.Weapon;
import com.asdfgaems.stages.GameStage;

public class Pistol extends Weapon {
    public Pistol(GameStage stage, int x, int y) {
        super("pistol", stage, x, y);
    }

    @Override
    public void attack(float physDamage, float energyDamage, float radiate, int x, int y, LiveActor damager) {
        super.attack(physDamage, energyDamage, radiate, x, y, damager);
    }

    @Override
    public void equip(CharParameters parameters) {
        parameters.setDamage(20.0f, 0.0f, 0.0f);
        parameters.setAttckRange(10.0f);
    }

    @Override
    public String getRequiredAmmo() {
        return "acess_card_1";
    }
    //TODO; анимация, класс Human, отображение PlayerTarget, враги-люди объединены в группы и при атаке одного из них остальные идут на помощь
}
