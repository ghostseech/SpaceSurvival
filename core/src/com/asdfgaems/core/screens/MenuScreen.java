package com.asdfgaems.core.screens;

import com.asdfgaems.core.SpaceSurvival;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class MenuScreen implements Screen {

    private final SpaceSurvival app;

    private Stage stage;

    private TextButton startButton;
    private TextButton exitButton;

    public MenuScreen(SpaceSurvival app) {
        this.app = app;
        this.stage = new Stage(new FitViewport(app.V_WIDTH, app.V_HEIGHT));
    }

    @Override
    public void show() {
        System.out.println("MENU");
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        initButtons();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();
    }

    private void initButtons() {
        startButton = new TextButton("Play", app.skin, "default");
        startButton.setPosition(110, 260);
        startButton.setSize(280, 60);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.world.create();
                app.world.activate();
                app.setScreen(app.gameScreen);
            }
        });

        exitButton = new TextButton("Exit", app.skin, "default");
        exitButton.setPosition(110, 190);
        exitButton.setSize(280, 60);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        stage.addActor(startButton);
        stage.addActor(exitButton);
    }

    private void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void resize(int w, int h) {
        stage.getViewport().update(w, h, false);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
