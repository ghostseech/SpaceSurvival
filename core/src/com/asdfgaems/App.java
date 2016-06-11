package com.asdfgaems;

import com.asdfgaems.screens.GameScreen;
import com.asdfgaems.screens.LevelEditorScreen;
import com.asdfgaems.screens.MenuScreen;
import com.badlogic.gdx.Game;

public class App extends Game {
    public App(boolean desktop) {
        if(desktop) {
            Vars.gameScale = 1;
        }
        else {
            Vars.gameScale = 0.6f;
        }
    }

    @Override
    public void create() {
        ResourseLoader.instance.load();

        setScreen(new MenuScreen(this));
    }

    public void startGame() {
        setScreen(new GameScreen(this));
    }
    public void startLevelEditor() {
        setScreen(new LevelEditorScreen(this));
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
