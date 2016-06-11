package com.asdfgaems.game.actors.characters;

import com.asdfgaems.ResourseLoader;
import com.asdfgaems.Vars;
import com.asdfgaems.game.actors.GameActor;
import com.asdfgaems.game.actors.objects.Door;
import com.asdfgaems.stages.GameStage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public abstract class LiveActor extends GameActor {
    private int actionPoints;
    private int maxActionPoints;

    private boolean visible;

    protected CharParameters parameters;
    private TextureRegion hpbar;

    public LiveActor(String name, GameStage stage, int actionPoints, CharParameters parameters, int x, int y, int width, int height) {
        super(name, stage, x, y, width, height);
        this.actionPoints = actionPoints;
        this.maxActionPoints = actionPoints;
        this.parameters = parameters;
        visible = false;
        hpbar = ResourseLoader.instance.getTextureRegion("health");
    }

    public void postTurn() {
        actionPoints = maxActionPoints;
    }

    public int getActionsPoints() {
        return actionPoints;
    }

    public void postAction() {
        actionPoints--;
        parameters.postAction();
    }

    public void move(int dirX, int dirY) {
        postAction();
        addAction(Actions.moveBy(dirX * Vars.tileSize, dirY * Vars.tileSize, Vars.turnAnimaionTime));
        tileX += dirX;
        tileY += dirY;
    }

    public void behavoir() {

    }

    public boolean isVisible() {
        return visible;
    }
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public CharParameters getParameters() {
        return parameters;
    }

    public void randomMove() {
        int dirx = 0;
        int diry = 0;

        for(int i = 0; i < 10; i ++) {
            dirx = MathUtils.random(-1, 1);
            diry = MathUtils.random(-1, 1);

            if(!stage.isCollidable(tileX + dirx, tileY + diry, false, false, false, false) && (dirx != 0 || diry != 0)) {
                moveWithDoorOpening(dirx, diry);
                return;
            }
        }
        postAction();
    }

    public void moveWithDoorOpening(int dirx, int diry) {
        GameActor a = stage.getObject(tileX + dirx, tileY + diry);
        if(a instanceof Door && ((Door) a).getLevel() == 0 && ((Door) a).isClosed()) {
            ((Door) a).open();
            postAction();
        }
        else {
            move(dirx, diry);
        }
    }

    public void drawHpBar(Batch batch) {
        batch.draw(hpbar, getX(), getY() + Vars.tileSize, Vars.tileSize * getTileWidth() * (parameters.getHealth() / parameters.getMaxHelath()), Vars.tileSize / 5);
    }
}
