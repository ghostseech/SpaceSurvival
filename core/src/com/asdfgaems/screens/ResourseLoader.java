package com.asdfgaems.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.HashMap;

public class ResourseLoader {
    public static ResourseLoader instance = new ResourseLoader();

    private HashMap<String, TextureRegion> textures;
    private HashMap<String, Skin> skins;
    private HashMap<String, BitmapFont> fonts;
    private HashMap<String, String> texts;

    public ResourseLoader() {
        textures = new HashMap<String, TextureRegion>();
        fonts = new HashMap<String, BitmapFont>();
        skins = new HashMap<String, Skin>();
        texts = new HashMap<String, String>();
    }

    public void load() {
        addFont("default", "ui/Arcon.ttf", Color.BLACK, 24);

        addTexture("wall_1", "textures/tiles/wall_1.png");
        addTexture("floor_1", "textures/tiles/floor_1.png");
        addTexture("floor_2", "textures/tiles/floor_2.png");

        addTexture("door_0", "textures/objects/door1.png");
        addTexture("door_1", "textures/objects/door_lvl_1.png");
        addTexture("door_2", "textures/objects/door_lvl_2.png");
        addTexture("door_3", "textures/objects/door_lvl_3.png");

        addTexture("player", "textures/objects/player1.png");

        addTexture("ui_background", "textures/ui/playerui.png");
        addTexture("inventory_button", "textures/ui/inventory_button.png");
        addTexture("close_window_button", "textures/ui/close_window_button.png");

        addTexture("acess_card_1", "textures/items/card_lvl_1.png");

        addSkin("inventory", "default" , "skin2");
        addSkin("main", "default" , "skin1");

        addText("drop_action", "Drop");
    }

    public Skin getSkin(String id) {
        return skins.get(id);
    }

    public TextureRegion getTextureRegion(String id) {
        return textures.get(id);
    }

    public BitmapFont getFont(String id) {
        return fonts.get(id);
    }

    public String getText(String id) {
        return texts.get(id);
    }

    private void addTexture(String id, String texture) {
        textures.put(id, new TextureRegion(new Texture(texture)));
    }

    private void addSkin(String id, String fontId, String dir) {
        Skin skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal("ui/"+ dir +"/uiskin.atlas")));
        skin.add("default-font", getFont(fontId));
        skin.load(Gdx.files.internal("ui/"+ dir +"/uiskin.json"));

        skins.put(id, skin);
    }

    private void addFont(String id, String file, Color color, int size) {
        BitmapFont font;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(file));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = size;
        params.genMipMaps = true;
        params.color = color;
        font = generator.generateFont(params);

        fonts.put(id, font);
    }

    private void addText(String id, String text) {
        texts.put(id, text);
    }

}
