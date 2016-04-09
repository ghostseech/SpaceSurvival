package com.asdfgaems.core.screens;

import com.asdfgaems.core.*;
import com.asdfgaems.core.objects.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
import sun.font.TextLabel;

public class GameScreen extends Stage implements Screen {
    public static Texture bleed_indicator_texture;
    public static Texture toxic_indicator_texture;
    public static Texture radiation_indicator_texture;
    public static Texture hpregen_indicator_texture;

    private SpaceSurvival app;

    private Table menu;
    private float clickTimer;
    private PlayerInfoWindow playerPda;
    private SwapItemsWindow swapItemsWindow;
    private Label playerHp;
    private Label playerSatiety;

    private Table indicators;
    private Image bleed_indicator;
    private Image radiation_indicator;
    private Image hpregen_indicator;
    private Image toxic_indicator;

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

        playerHp.setText("HEALTH:" + app.world.player.getHp());
        playerSatiety.setText("SATIETY:" + app.world.player.getSatiety());
        checkIndicators();

        if(isAllHidden())app.world.activate();
        else app.world.deactivate();

        if(clickTimer > 0.0f) clickTimer -=dt;
        if(Gdx.input.isTouched() && app.world.isActive()) {
            app.camera.position.x -= (Gdx.input.getDeltaX()) * Gdx.graphics.getWidth() / app.V_WIDTH;
            app.camera.position.y += (Gdx.input.getDeltaY()) * Gdx.graphics.getHeight() / app.V_HEIGHT;
        }

        this.act(dt);
    }

    private void checkIndicators() {
        if((indicators.getChildren().indexOf(bleed_indicator, true) == -1)  == app.world.player.isBleed()){
            updateIndicators();
            return;
        }
        if((indicators.getChildren().indexOf(radiation_indicator, true) == -1) == app.world.player.isRadiated()) {
            updateIndicators();
            return;
        }
        if((indicators.getChildren().indexOf(toxic_indicator, true) == -1) == app.world.player.isToxic()) {
            updateIndicators();
            return;
        }
        if((indicators.getChildren().indexOf(hpregen_indicator, true) == -1) == app.world.player.isHpRegen()) {
            updateIndicators();
            return;
        }

    }
    private void updateIndicators() {
        indicators.clearChildren();

        if(app.world.player.isBleed()) {
            indicators.add(bleed_indicator).size(80, 80);
            indicators.row();
        }

        if(app.world.player.isHpRegen()) {
            indicators.add(hpregen_indicator).size(80, 80);
            indicators.row();
        }

        if(app.world.player.isRadiated()) {
            indicators.add(radiation_indicator).size(80, 80);
            indicators.row();
        }

        if(app.world.player.isToxic()) {
            indicators.add(toxic_indicator).size(80, 80);
            indicators.row();
        }
    }

    public void showSwapItemsWindow(Player player, Inventory inventory) {
        swapItemsWindow = new SwapItemsWindow(this, app.skin, player, inventory, 140.0f, 60.0f, 1000.0f, 600.0f);
        swapItemsWindow.show();
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

        playerSatiety = new Label("", app.skin);
        playerSatiety.getStyle().fontColor = new Color(1.0f, 0.3f, 0.3f, 1.0f);
        playerSatiety.setPosition(40.0f, 660.0f);
        playerSatiety.setSize(100.0f, 40.0f);
        this.addActor(playerSatiety);

        playerHp = new Label("", app.skin);
        playerHp.setPosition(40.0f, 620.0f);
        playerHp.setSize(100.0f, 40.0f);
        playerHp.getStyle().fontColor = Color.BLUE;
        this.addActor(playerHp);

        playerPda = new PlayerInfoWindow(this, app.skin, app.world.player, 1000, 600, 140, 60);
        swapItemsWindow = new SwapItemsWindow(this, app.skin, app.world.player, app.world.player.inventory, 140.0f, 60.0f, 1000.0f, 600.0f);

        bleed_indicator = new Image(bleed_indicator_texture);
        bleed_indicator.setSize(64.0f, 64.0f);

        toxic_indicator = new Image(toxic_indicator_texture);
        toxic_indicator.setSize(64.0f, 64.0f);

        radiation_indicator = new Image(radiation_indicator_texture);
        radiation_indicator.setSize(64.0f, 64.0f);

        hpregen_indicator = new Image(hpregen_indicator_texture);
        hpregen_indicator.setSize(64.0f, 64.0f);

        indicators = new Table(app.skin);
        indicators.setPosition(1200, 360);
        indicators.setSize(80, 360);
        indicators.align(Align.top);
        this.addActor(indicators);

        updateIndicators();
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
