package com.asdfgaems.core.screens;

import com.asdfgaems.core.GameObject;
import com.asdfgaems.core.SpaceSurvival;
import com.asdfgaems.core.items.AcessCard;
import com.asdfgaems.core.items.Armor;
import com.asdfgaems.core.items.Consumable;
import com.asdfgaems.core.items.Weapon;
import com.asdfgaems.core.objects.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

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
            app.skin = new Skin();
            app.skin.addRegions(app.assets.get("ui/uiskin.atlas", TextureAtlas.class));
            app.skin.add("default-font", app.mainFont);
            app.skin.load(Gdx.files.internal("ui/uiskin.json"));

            setupGameResources();

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
        app.assets.load("textures/door1.png", Texture.class);
        app.assets.load("textures/door_lvl_1.png", Texture.class);
        app.assets.load("textures/door_lvl_2.png", Texture.class);
        app.assets.load("textures/door_lvl_3.png", Texture.class);

        app.assets.load("textures/card_lvl_1.png", Texture.class);
        app.assets.load("textures/card_lvl_2.png", Texture.class);
        app.assets.load("textures/card_lvl_3.png", Texture.class);

        app.assets.load("textures/enemy_1_test.png", Texture.class);
        app.assets.load("textures/enemy_2_test.png", Texture.class);
        app.assets.load("textures/radinsect_1.png", Texture.class);

        app.assets.load("textures/floor_1.png", Texture.class);
        app.assets.load("textures/floor_2.png", Texture.class);
        app.assets.load("textures/wall_1.png", Texture.class);
        app.assets.load("textures/playerui.png", Texture.class);

        app.assets.load("textures/food_type_1.png", Texture.class);
        app.assets.load("textures/healthbox_base.png", Texture.class);

        app.assets.load("textures/antirad_mixture.png", Texture.class);
        app.assets.load("textures/attention_mixture.png", Texture.class);
        app.assets.load("textures/antitoxic_mixture.png", Texture.class);
        app.assets.load("textures/hpregen_mixture.png", Texture.class);

        app.assets.load("textures/armor_base.png", Texture.class);

        app.assets.load("textures/knife.png", Texture.class);
        app.assets.load("textures/security_gun.png", Texture.class);
        app.assets.load("textures/rapidgun.png", Texture.class);
        app.assets.load("textures/army_rifle.png", Texture.class);
        app.assets.load("textures/plasma_gun.png", Texture.class);
        app.assets.load("textures/shotgun.png", Texture.class);

        app.assets.load("textures/hpregen_indicator.png", Texture.class);
        app.assets.load("textures/toxic_indicator.png", Texture.class);
        app.assets.load("textures/radiation_indicator.png", Texture.class);
        app.assets.load("textures/bleed_indicator.png", Texture.class);
    }

    private void setupGameResources() {
        Player.texture = app.assets.get("textures/player1.png", Texture.class);
        Door.texture_unlocked = app.assets.get("textures/door1.png", Texture.class);
        Door.texture_lvl_1 = app.assets.get("textures/door_lvl_1.png", Texture.class);
        Door.texture_lvl_2 = app.assets.get("textures/door_lvl_2.png", Texture.class);
        Door.texture_lvl_3 = app.assets.get("textures/door_lvl_3.png", Texture.class);

        AcessCard.texture_lvl_1 = app.assets.get("textures/card_lvl_1.png", Texture.class);
        AcessCard.texture_lvl_2 = app.assets.get("textures/card_lvl_2.png", Texture.class);
        AcessCard.texture_lvl_3 = app.assets.get("textures/card_lvl_3.png", Texture.class);

        Chest.texture_lvl_1 = app.assets.get("textures/chest_lvl_1.png", Texture.class);
        Chest.texture_lvl_2 = app.assets.get("textures/chest_lvl_2.png", Texture.class);
        Chest.texture_lvl_3 = app.assets.get("textures/chest_lvl_3.png", Texture.class);

        PlayerInfoWindow.backgroundTexture = app.assets.get("textures/playerui.png", Texture.class);
        SwapItemsWindow.backgroundTexture = app.assets.get("textures/playerui.png", Texture.class);

        Armor.texture_base = app.assets.get("textures/armor_base.png", Texture.class);
        Consumable.texture_food_1 = app.assets.get("textures/food_type_1.png", Texture.class);
        Consumable.texture_health_1 = app.assets.get("textures/healthbox_base.png", Texture.class);

        Consumable.texture_antirad_mixture = app.assets.get("textures/antirad_mixture.png", Texture.class);
        Consumable.texture_attention_mixture = app.assets.get("textures/attention_mixture.png", Texture.class);
        Consumable.texture_antitoxic_mixture = app.assets.get("textures/antitoxic_mixture.png", Texture.class);
        Consumable.texture_regen_mixture = app.assets.get("textures/hpregen_mixture.png", Texture.class);

        Weapon.texture_knife = app.assets.get("textures/knife.png", Texture.class);
        Weapon.texture_security_gun = app.assets.get("textures/security_gun.png", Texture.class);
        Weapon.texture_rapidgun = app.assets.get("textures/rapidgun.png", Texture.class);
        Weapon.texture_army_rifle = app.assets.get("textures/army_rifle.png", Texture.class);
        Weapon.texture_plasmagun = app.assets.get("textures/plasma_gun.png", Texture.class);
        Weapon.texture_shotgun = app.assets.get("textures/shotgun.png", Texture.class);

        Slime.texture_type_1 = app.assets.get("textures/enemy_2_test.png", Texture.class);
        RadInsect.texture_type_1 = app.assets.get("textures/radinsect_1.png", Texture.class);
        Enemy.health_texture = app.assets.get("textures/playerui.png", Texture.class);

        GameScreen.hpregen_indicator_texture = app.assets.get("textures/hpregen_indicator.png", Texture.class);
        GameScreen.toxic_indicator_texture = app.assets.get("textures/toxic_indicator.png", Texture.class);
        GameScreen.radiation_indicator_texture = app.assets.get("textures/radiation_indicator.png", Texture.class);
        GameScreen.bleed_indicator_texture = app.assets.get("textures/bleed_indicator.png", Texture.class);

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
