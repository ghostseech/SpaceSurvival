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

    public World world;

    private Table menu;

    private Inventory worldInventory;

    public GameScreen(SpaceSurvival app) {
        super(new StretchViewport(app.V_WIDTH, app.V_HEIGHT));
        this.app = app;
        this.world = new World(app);
        this.worldInventory = null;
    }
    @Override
    public void show() {
        this.clear();
        world.create();
        setupUi();
        Gdx.input.setInputProcessor(this);
        this.addListener(new WorldClickListener(world));
    }

    @Override
    public void render(float v) {
        update(v);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        app.camera.update();
        app.batch.setProjectionMatrix(app.camera.combined);
        app.batch.begin();

        world.draw(app.batch);

        app.batch.end();
        this.draw();
    }

    private void update(float dt) {
        world.update(dt);


        if(Gdx.input.isTouched()) {
            app.camera.position.x -= (Gdx.input.getDeltaX()) * Gdx.graphics.getWidth() / app.V_WIDTH;
            app.camera.position.y += (Gdx.input.getDeltaY()) * Gdx.graphics.getHeight() / app.V_HEIGHT;
        }

        if(Gdx.input.isTouched()) {
            processTouch();
        }

        if(!world.isTurnProcessd) {
            world.isTurnProcessd = true;
            hideWorldInventory();
        }
        this.act(dt);
    }

    private void processTouch() {
        GameObject touched = app.getTocuhed();
        if(touched != null)
            if(touched.getClass() == Chest.class) {
                if(world.player.getDist(touched) <= 1.5f && ((Chest)touched).getLevel() == 0) {
                    showWorldInventory(((Chest)touched).inventory);
                }
            }
    }

    private void hideInventory() {
        world.player.inventory.getUi().addAction(Actions.moveTo(-80.0f, 60.0f, 0.5f));
        hideWorldInventory();
    }
    private void hideWorldInventory() {
        if(worldInventory != null)
            worldInventory.getUi().addAction(Actions.sequence(
                    Actions.moveTo(-80.0f, 60.0f, 0.5f),
                    new Action() {
                        @Override
                        public boolean act(float v) {
                            getActors().removeValue(worldInventory.getUi(), true);
                            worldInventory = null;
                            return true;
                        }
                    }));
    }

    private void showInventory() {
        world.player.inventory.getUi().clearActions();
        world.player.inventory.updateUi();
        world.player.inventory.getUi().addAction(Actions.moveTo(80.0f, 60.0f, 0.5f));
    }
    private void showWorldInventory(Inventory inv) {
        showInventory();
        if(worldInventory != null) this.getActors().removeValue(worldInventory.getUi(), true);
        worldInventory = inv;
        worldInventory.getUi().clearActions();
        worldInventory.getUi().setPosition(-80.0f, 60.0f);
        worldInventory.getUi().addAction(Actions.moveTo(160.0f, 60.0f, 0.5f));
        worldInventory.getUi().getStyle().background = new TextureRegionDrawable(new TextureRegion(app.assets.get("textures/playerui.png", Texture.class)));
        this.addActor(worldInventory.getUi());
        Inventory.connectInventories(worldInventory, world.player.inventory);
    }
    private void setupUi() {
        menu = new Table();
        TextButton openInventoryButton = new TextButton("INV", app.skin, "default");
        openInventoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(world.player.inventory.getUi().getX() == -80.0f) {
                    showInventory();
                }
                else {
                    hideInventory();
                }
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

        world.player.inventory.getUi().setPosition(-80.0f, 60.0f);
        world.player.inventory.getUi().getStyle().background = new TextureRegionDrawable(new TextureRegion(app.assets.get("textures/playerui.png", Texture.class)));
        this.addActor(world.player.inventory.getUi());
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
        world.dispose();
        worldInventory = null;
    }

    @Override
    public void dispose() {
        this.dispose();
    }
    class tmpCilckListener extends ClickListener {
        int n;
        Inventory dest;
        Inventory loc;
        public tmpCilckListener(int n, Inventory loc, Inventory dest) {
            this.n = n;
            this.loc = loc;
            this.dest = dest;
        }
        @Override
        public void clicked(InputEvent event, float x, float y) {
            if(getTapCount() == 1 && dest != null && loc != null) {
                System.out.println(n);
                dest.addItem(loc.takeItem(loc.getItem(n)));
                Inventory tmp = dest;
                dest = loc;
                loc = tmp;
            }
        }
    };
    class WorldClickListener extends ClickListener {
        World world;
        public WorldClickListener(World world) {
            this.world = world;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            world.click();
        }
    }
}
