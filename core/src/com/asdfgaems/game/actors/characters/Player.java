package com.asdfgaems.game.actors.characters;

import com.asdfgaems.ResourseLoader;
import com.asdfgaems.game.actors.items.Armor;
import com.asdfgaems.game.actors.items.Weapon;
import com.asdfgaems.game.actors.ui.Inventory;
import com.asdfgaems.stages.GameStage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends LiveActor {
    private Inventory inventory;
    private TextureRegion texture;

    private Armor armor;
    private Weapon weapon;

    public Player(GameStage stage, int x, int y) {
        super("player", stage, 2, new CharParameters(100.0f, new DefaultAttacker(5.0f, 0.0f, 0.0f, 1.5f, stage)), x, y, 1, 1);
        getParameters().setCharacter(this);
        texture = ResourseLoader.instance.getTextureRegion("player");
        inventory = new Inventory(24);
        getParameters().allowStarving();
        setVisible(true);
    }

    public void equipWeapon(Weapon weapon) {
        this.weapon = weapon;
        weapon.equip(getParameters());
        weapon.setEquipped(true);
    }

    public void unequipWeapon() {
        weapon.setEquipped(false);
        weapon = null;
        parameters.setAttacker(new DefaultAttacker(5.0f, 0.0f, 0.0f, 1.5f, stage));
    }

    public void equipArmor(Armor armor) {
        this.armor = armor;
        armor.equip(getParameters());
        armor.setEquipped(true);
    }

    public void unequipArmor() {
        armor.unequip(getParameters());
        armor.setEquipped(false);
        armor = null;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public Armor getArmor() {
        return armor;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawTextureRegion(batch, texture);
    }

    public Inventory getInventory() {
        return inventory;
    }
}
