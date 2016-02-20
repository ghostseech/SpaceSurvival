package com.asdfgaems.core.screens;

import com.asdfgaems.core.SpaceSurvival;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class SplashScreen implements Screen {

    private final SpaceSurvival app;
    private Stage stage;
    private Image splashImage;

    public SplashScreen(SpaceSurvival app) {
        this.app = app;
        this.stage = new Stage(new FitViewport(app.V_WIDTH, app.V_HEIGHT, app.camera));
    }

    @Override
    public void show() {
        System.out.println("SPLASH");
        Gdx.input.setInputProcessor(stage);

        Runnable screenChange = new Runnable() {
            @Override
            public void run() {
                app.setScreen(app.menuScreen);
            }
        };

        Texture splashTexture = app.assets.get("splash.png", Texture.class);
        splashImage = new Image(splashTexture);
        splashImage.setScale(2.0f);
        splashImage.setOrigin(splashImage.getWidth() / 2, splashImage.getHeight() / 2);
        splashImage.setPosition(stage.getWidth() / 2 - 200, stage.getHeight() / 2 + 50);

        stage.addActor(splashImage);

        splashImage.addAction(Actions.sequence(
                Actions.alpha(0.0f),
                Actions.fadeIn(0.5f, Interpolation.pow3),
                Actions.delay(1.0f),
                Actions.fadeOut(0.5f, Interpolation.pow5),
                Actions.run(screenChange)));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);
        stage.draw();
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
