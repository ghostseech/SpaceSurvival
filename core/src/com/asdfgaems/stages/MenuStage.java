package com.asdfgaems.stages;

import com.asdfgaems.App;
import com.asdfgaems.ResourseLoader;
import com.asdfgaems.Vars;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MenuStage extends Stage {
    private App app;
    private TextButton startGameButton;
    private TextButton startLevelEditorButton;
    private TextButton exitButton;
    private Skin skin;

    public MenuStage(final App app) {
        this.app = app;

        skin = ResourseLoader.instance.getSkin("main");

        startGameButton = new TextButton("Start game", skin);
        startGameButton.setSize(Vars.WIDTH / 4, Vars.HEIGHT / 10);
        startGameButton.setPosition(-Vars.WIDTH / 4,  Vars.HEIGHT / 3);
        startGameButton.addAction(Actions.moveTo(Vars.WIDTH / 8, Vars.HEIGHT / 3, 1.0f));
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.startGame();
            }
        });

        startLevelEditorButton = new TextButton("Level editor", skin);
        startLevelEditorButton.setSize(Vars.WIDTH / 4, Vars.HEIGHT / 10);
        startLevelEditorButton.setPosition(-Vars.WIDTH / 4,  Vars.HEIGHT / 3 - Vars.HEIGHT / 10 * 1.2f);
        startLevelEditorButton.addAction(Actions.moveTo(Vars.WIDTH / 8, Vars.HEIGHT / 3 - Vars.HEIGHT / 10 * 1.2f, 1.0f));
        startLevelEditorButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.startLevelEditor();
            }
        });

        addActor(startGameButton);
        addActor(startLevelEditorButton);
    }
}
