package com.asdfgaems.game.actors.ui.windows;

import com.asdfgaems.ResourseLoader;
import com.asdfgaems.Vars;
import com.asdfgaems.game.actors.characters.Player;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class PlayerStateWindow extends Group {
    private BitmapFont font;
    private Player player;

    private NinePatch background;

    public PlayerStateWindow(Player player) {
        this.player = player;
        setPosition(Vars.slotSize, Vars.slotSize);

        background = new NinePatch(ResourseLoader.instance.getSkin("main").getRegion("default-round"), 3, 3, 3, 3);

        font = ResourseLoader.instance.getFont("default");
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        background.draw(batch, Vars.slotSize * 1, Vars.slotSize * 1, Vars.slotSize * 6, Vars.slotSize * 5);
        font.draw(batch, "Physical Resistance: " + player.getParameters().getPhysResist() * 100 + "%", getX() + Vars.slotSize / 4, getY() + Vars.slotSize * 5 - (Vars.slotSize / 3));
        font.draw(batch, "Energy Resistance: " + player.getParameters().getEnergyResist() * 100 + "%", getX() + Vars.slotSize / 4, getY() + Vars.slotSize * 5 - (Vars.slotSize / 3) * 2);
        font.draw(batch, "Radiation Resistance: " + player.getParameters().getRadiationResist() * 100 + "%", getX() + Vars.slotSize / 4, getY() + Vars.slotSize * 5 - (Vars.slotSize / 3) * 3);
        String abilities = "Effects: ";
        for(int i = 0; i < player.getParameters().getAbilities().size; i++) {
            abilities += player.getParameters().getAbilities().get(i).getId() + ", ";
        }
        font.draw(batch, abilities, getX() + Vars.slotSize / 4, getY() + Vars.slotSize * 5 - (Vars.slotSize / 3) * 4);
    }
}
