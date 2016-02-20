package com.asdfgaems.core.screens;

import com.asdfgaems.core.SpaceSurvival;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class LoadingScreen implements Screen {

    private final SpaceSurvival app;

    public LoadingScreen(SpaceSurvival app) {
        this.app = app;
    }

    @Override
    public void show() {
        System.out.println("LOADING");
        queueAssets();
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(app.assets.update()) {
            app.setScreen(app.splashScreen);
        }
    }

    private void queueAssets() {
        app.assets.load("splash.png", Texture.class);
        app.assets.load("ui/uiskin.atlas", TextureAtlas.class);
        app.assets.load("textures/player1.png", Texture.class);
        app.assets.load("textures/chest_lvl_1.png", Texture.class);
        app.assets.load("textures/chest_lvl_2.png", Texture.class);
        app.assets.load("textures/chest_lvl_3.png", Texture.class);
        app.assets.load("textures/door_lvl_1.png", Texture.class);
        app.assets.load("textures/door_lvl_2.png", Texture.class);
        app.assets.load("textures/door_lvl_3.png", Texture.class);
        app.assets.load("textures/enemy_1_test.png", Texture.class);
        app.assets.load("textures/enemy_2_test.png", Texture.class);
        app.assets.load("textures/floor_1.png", Texture.class);
        app.assets.load("textures/floor_2.png", Texture.class);
        app.assets.load("textures/wall_1.png", Texture.class);
    }

    @Override
    public void resize(int i, int i1) {

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

    }
}
