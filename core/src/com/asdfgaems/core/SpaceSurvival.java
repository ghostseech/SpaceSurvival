package com.asdfgaems.core;

import com.asdfgaems.core.screens.GameScreen;
import com.asdfgaems.core.screens.LoadingScreen;
import com.asdfgaems.core.screens.MenuScreen;
import com.asdfgaems.core.screens.SplashScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.Map;

public class SpaceSurvival extends Game {

    public static final int V_WIDTH = 1280;
    public static final int V_HEIGHT = 720;

    public OrthographicCamera camera;

    public AssetManager assets;

    public LoadingScreen loadingScreen;
    public SplashScreen splashScreen;
    public MenuScreen menuScreen;
    public GameScreen gameScreen;



    public SpriteBatch batch;
    public BitmapFont mainFont;
    public Skin skin;

    @Override
    public void create() {
        assets = new AssetManager();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, V_WIDTH, V_HEIGHT);

        loadFonts();

        splashScreen = new SplashScreen(this);
        loadingScreen = new LoadingScreen(this);
        menuScreen = new MenuScreen(this);
        gameScreen = new GameScreen(this);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        setScreen(loadingScreen);
    }

    @Override
    public void dispose() {
        super.dispose();
        assets.dispose();
        splashScreen.dispose();
        menuScreen.dispose();
        loadingScreen.dispose();
    }

    private void loadFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/Arcon.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 24;
        params.color = Color.BLACK;
        mainFont = generator.generateFont(params);
    }

    public GameObject getTocuhed() {
        float mouseX = ((float)Gdx.input.getX() / Gdx.graphics.getWidth() * V_WIDTH + camera.position.x - (float)V_WIDTH/2) / 64.0f;
        float mouseY = ((1.0f - (float)Gdx.input.getY() / (float)Gdx.graphics.getHeight()) * V_HEIGHT + camera.position.y - (float)V_HEIGHT/2) / 64.0f;
        int x = (int)mouseX;
        int y = (int)mouseY;
        for(Map.Entry<String, GameObject> entry : gameScreen.world.gameObjects.entrySet()) {
            if(entry.getValue().x == x && entry.getValue().y == y) return entry.getValue();
        }
        return null;
    }

    public int getTouchedX() {
        float mouseX = ((float)Gdx.input.getX() / Gdx.graphics.getWidth() * V_WIDTH + camera.position.x - (float)V_WIDTH/2) / 64.0f;
        return (int)mouseX;
    }

    public int getToucedY() {
        float mouseY = ((1.0f - (float)Gdx.input.getY() / (float)Gdx.graphics.getHeight()) * V_HEIGHT + camera.position.y - (float)V_HEIGHT/2) / 64.0f;
        return (int)mouseY;
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
}
