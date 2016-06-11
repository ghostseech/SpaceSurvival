package com.asdfgaems.screens;

import com.asdfgaems.App;
import com.asdfgaems.stages.LevelEditorGuiStage;
import com.asdfgaems.stages.LevelEditorStage;
import com.asdfgaems.stages.MenuStage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;

public class LevelEditorScreen implements Screen {
    private App app;
    private LevelEditorStage stage;
    private LevelEditorGuiStage gui;

    public LevelEditorScreen(App app) {
        this.app = app;
        stage = new LevelEditorStage(app);
        gui = new LevelEditorGuiStage(app);
        stage.setGui(gui);
    }

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(gui);
        multiplexer.addProcessor(new GestureDetector(stage));
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        stage.act(delta);
        gui.act(delta);
        stage.draw();
        gui.draw();
    }

    @Override
    public void resize(int w, int h) {
        stage.getViewport().update(w, h);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
