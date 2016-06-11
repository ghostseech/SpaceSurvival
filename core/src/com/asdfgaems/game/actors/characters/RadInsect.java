package com.asdfgaems.game.actors.characters;

import com.asdfgaems.ResourseLoader;
import com.asdfgaems.stages.GameStage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.util.List;

public class RadInsect extends LiveActor {
    private TextureRegion texture;

    public RadInsect(String name, GameStage stage, int x, int y) {
        super(name, stage, 3, new CharParameters(100.0f, new DefaultAttacker(30.0f, 0.0f, 2.0f, 1.5f, stage)), x, y, 1, 1);
        getParameters().setCharacter(this);
        texture = ResourseLoader.instance.getTextureRegion("radinsect");
    }

    @Override
    public void behavoir() {
        if(stage.isVisibleFrom(tileX, tileY, stage.getPlayer().getTileX(), stage.getPlayer().getTileY())) {
            List<Vector2> path = stage.getPath(tileX, tileY, stage.getPlayer().getTileX(), stage.getPlayer().getTileY());
            if(path.size() == 0) {
                postAction();
                return;
            }
            if(parameters.getAttackRange() < Vector2.dst(stage.getPlayer().getTileX(), stage.getPlayer().getTileY(), getTileX(), getTileY())) moveWithDoorOpening((int)path.get(0).x - tileX, (int)path.get(0).y - tileY);
            else {
                getParameters().attack(stage.getPlayer().getTileX(), stage.getPlayer().getTileY());
                postAction();
            }
        }
        else {
            randomMove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(isVisible()) {
            drawTextureRegion(batch, texture);
            drawHpBar(batch);
        }
    }
}
