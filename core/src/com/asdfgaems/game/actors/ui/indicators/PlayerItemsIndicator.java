package com.asdfgaems.game.actors.ui.indicators;

import com.asdfgaems.ResourseLoader;
import com.asdfgaems.Vars;
import com.asdfgaems.game.actors.characters.Player;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class PlayerItemsIndicator extends Widget {
    private Player player;
    private TextureRegion weapon;
    private TextureRegion armor;
    private BitmapFont font;
    private int ammo;

    public PlayerItemsIndicator(Player player) {
        this.player = player;
        font = ResourseLoader.instance.getFont("highlighted");

        updateTextureRegions();
        updateAmmoCount();
    }

    public void updateTextureRegions() {
        if(player.getWeapon() != null) weapon = ResourseLoader.instance.getTextureRegion(player.getWeapon().getId());
        else weapon = null;
        if(player.getArmor() != null) armor = ResourseLoader.instance.getTextureRegion(player.getArmor().getId());
        else armor = null;
    }

    public void updateAmmoCount() {
        if(player.getWeapon() != null) ammo = player.getInventory().getItemCount(player.getWeapon().getRequiredAmmo());
        else ammo = 0;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(weapon != null) {
            batch.draw(weapon, getX(), getY(), Vars.slotSize, Vars.slotSize);
            if(ammo > 0) font.draw(batch, String.valueOf(ammo), getX(), getY() + Vars.slotSize * 0.4f);
        }

        if(armor != null) batch.draw(armor, getX() + Vars.slotSize, getY(), Vars.slotSize, Vars.slotSize);
    }
}
