package com.asdfgaems.game.actors.ui.indicators;

import com.asdfgaems.ResourseLoader;
import com.asdfgaems.Vars;
import com.asdfgaems.game.actors.characters.Player;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class PlayerStateIndicator extends Widget {
    private Player player;
    private BitmapFont font;

    public PlayerStateIndicator(Player player) {
        this.player = player;

        font =  ResourseLoader.instance.getFont("highlighted");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        font.draw(batch, ResourseLoader.instance.getText("health") + " : " + String.valueOf(player.getParameters().getHealth()), getX(), getY() + Vars.tileSize);
        font.draw(batch, ResourseLoader.instance.getText("satiety") + " : " + String.valueOf(player.getParameters().getSatiety()), getX(), getY()+ Vars.tileSize/2);
       }
}
