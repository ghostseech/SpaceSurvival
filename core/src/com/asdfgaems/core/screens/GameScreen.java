package com.asdfgaems.core.screens;

import com.asdfgaems.core.*;
import com.asdfgaems.core.objects.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class GameScreen extends Stage implements Screen {
    private SpaceSurvival app;

    private Table menu;
    private float clickTimer;
    private PlayerInfoWindow playerPda;
    private SwapItemsWindow swapItemsWindow;

    public GameScreen(SpaceSurvival app) {
        super(new StretchViewport(app.V_WIDTH, app.V_HEIGHT));
        this.app = app;
    }
    @Override
    public void show() {
        this.clear();
        Gdx.input.setInputProcessor(this);
        this.addListener(new WorldClickListener(app.world));

        setupUi();
        clickTimer = 0.0f;
    }

    @Override
    public void render(float v) {
        update(v);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        app.camera.update();
        app.batch.setProjectionMatrix(app.camera.combined);
        app.batch.begin();

        app.world.draw(app.batch);

        app.batch.end();
        this.draw();
    }

    private void update(float dt) {
        app.world.update(dt);
        playerPda.update(dt);
        swapItemsWindow.update(dt);

        if(isAllHidden())app.world.activate();

        if(clickTimer > 0.0f) clickTimer -=dt;
        if(Gdx.input.isTouched() && app.world.isActive()) {
            app.camera.position.x -= (Gdx.input.getDeltaX()) * Gdx.graphics.getWidth() / app.V_WIDTH;
            app.camera.position.y += (Gdx.input.getDeltaY()) * Gdx.graphics.getHeight() / app.V_HEIGHT;
        }

        if(Gdx.input.isTouched()) {
            processTouch();
        }
        this.act(dt);
    }

    private void processTouch() {
        if(!app.world.isActive()) return;

        GameObject touched = app.getTocuhed();
        if(touched != null)
            if(touched.getClass() == Chest.class) {
                if(app.world.player.getDist(touched) <= 1.5f && ((Chest)touched).getLevel() == 0) {
                    swapItemsWindow = new SwapItemsWindow(this, app.skin, app.world.player, ((Chest)touched).inventory, 140.0f, 60.0f, 1000.0f, 600.0f);
                    swapItemsWindow.show();
                    app.world.deactivate();
                }
            }
    }

    private void setupUi() {
        menu = new Table();
        TextButton openInventoryButton = new TextButton("INV", app.skin, "default");
        openInventoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hideAll();
                playerPda.show();
                app.world.deactivate();
            }
        });

        TextButton backToMenuButton = new TextButton("MENU", app.skin, "default");
        backToMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.menuScreen);
            }
        });

        menu.setSize(80.0f, 400.0f);
        menu.setPosition(0.0f, 160.0f);
        menu.align(Align.top);
        menu.padTop(20.0f);
        menu.background(new TextureRegionDrawable(new TextureRegion(app.assets.get("textures/playerui.png", Texture.class))));
        menu.add(openInventoryButton).size(64.0f, 64.0f);
        menu.row();
        menu.add(backToMenuButton);

        this.addActor(menu);

        playerPda = new PlayerInfoWindow(this, app.skin, app.world.player, 1000, 600, 140, 60);
        swapItemsWindow = new SwapItemsWindow(this, app.skin, app.world.player, app.world.player.inventory, 140.0f, 60.0f, 1000.0f, 600.0f);
    }

    private void hideAll() {
        playerPda.hide();
        swapItemsWindow.hide();
    }

    private boolean isAllHidden() {
        return playerPda.isHidden() && swapItemsWindow.isHidden();
    }

    @Override
    public void resize(int w, int h) {
        this.getViewport().update(w, h, false);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        this.clear();
        app.world.dispose();
    }

    @Override
    public void dispose() {
        this.dispose();
    }

    private class WorldClickListener extends ClickListener {
        private World world;
        public WorldClickListener(World world) {
            this.world = world;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            world.click();
        }
    }
}
