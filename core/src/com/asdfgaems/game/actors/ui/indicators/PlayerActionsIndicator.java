package com.asdfgaems.game.actors.ui.indicators;

import com.asdfgaems.ResourseLoader;
import com.asdfgaems.Vars;
import com.asdfgaems.game.actors.characters.Player;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class PlayerActionsIndicator extends Widget {
    private Player player;
    private TextureRegion texture;
    public PlayerActionsIndicator(Player player) {
        this.player = player;
        this.texture = ResourseLoader.instance.getTextureRegion("player_actions_indicator");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for(int i = 0; i < player.getActionsPoints(); i++) {
            batch.draw(texture, getX() + Vars.tileSize * i, getY(), Vars.tileSize, Vars.tileSize);
        }
    }
}
