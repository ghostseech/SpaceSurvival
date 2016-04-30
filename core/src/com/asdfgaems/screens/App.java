package com.asdfgaems.screens;

import com.asdfgaems.screens.screens.GameScreen;
import com.badlogic.gdx.Game;

public class App extends Game {
    public static App app = new App();

    @Override
    public void create() {
        ResourseLoader.instance.load();

        setScreen(new GameScreen());
    }

    @Override
    public void dispose() {
        super.dispose();
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
