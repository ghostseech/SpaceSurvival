package com.asdfgaems.screens.actors.characters;

import com.asdfgaems.screens.Vars;
import com.asdfgaems.screens.actors.GameActor;
import com.asdfgaems.screens.stages.GameStage;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;

import java.awt.*;

public abstract class LiveActor extends GameActor {
    private int actionPoints;
    private int maxActionPoints;

    protected CharParameters parameters;

    public LiveActor(String name, GameStage stage, int actionPoints, CharParameters parameters, int x, int y, int width, int height) {
        super(name, stage, x, y, width, height);
        this.actionPoints = actionPoints;
        this.maxActionPoints = actionPoints;
        this.parameters = parameters;
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




}
