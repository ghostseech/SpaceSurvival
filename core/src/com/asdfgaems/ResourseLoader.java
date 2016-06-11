package com.asdfgaems;

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
        addFont("default", "ui/Arcon.ttf", Color.BLACK, Color.BLACK, 28, 0);
        addFont("highlighted", "ui/Arcon.ttf", Color.CYAN, Color.NAVY, 34, 2);

        addTexture("wall_1", "textures/tiles/wall_1.png");
        addTexture("floor_1", "textures/tiles/floor_1.png");
        addTexture("floor_2", "textures/tiles/floor_2.png");

        addTexture("door_opened", "textures/objects/door_opened.png");
        addTexture("door_0", "textures/objects/door_lvl_0.png");
        addTexture("door_1", "textures/objects/door_lvl_1.png");
        addTexture("door_2", "textures/objects/door_lvl_2.png");
        addTexture("door_3", "textures/objects/door_lvl_3.png");

        addTexture("chest_opened", "textures/objects/chest_opened.png");
        addTexture("chest_0", "textures/objects/chest_lvl_0.png");
        addTexture("chest_1", "textures/objects/chest_lvl_1.png");
        addTexture("chest_2", "textures/objects/chest_lvl_2.png");
        addTexture("chest_3", "textures/objects/chest_lvl_3.png");

        addTexture("player", "textures/objects/player1.png");

        addTexture("ui_background", "textures/ui/playerui.png");
        addTexture("inventory_button", "textures/ui/inventory_button.png");
        addTexture("close_window_button", "textures/ui/close_window_button.png");
        addTexture("playerstate_button", "textures/ui/playerstate_button.png");

        addTexture("player_actions_indicator", "textures/ui/actionsIndicator.png");
        addTexture("health", "textures/colors/red_color.png");
        addTexture("satiety", "textures/colors/brown_color.png");
        addTexture("highlight", "textures/colors/white_color.png");

        addTexture("acess_card_1", "textures/items/card_lvl_1.png");
        addTexture("acess_card_2", "textures/items/card_lvl_2.png");
        addTexture("acess_card_3", "textures/items/card_lvl_3.png");
        addTexture("medkit_1", "textures/items/healthbox_base.png");
        addTexture("food_ration_1", "textures/items/food_type_1.png");

        addTexture("cloth_armor", "textures/items/armor_base.png");
        addTexture("pistol", "textures/items/security_gun.png");

        addTexture("not_visited", "textures/tiles/notVisited.png");
        addTexture("not_visible", "textures/tiles/notVisible.png");

        addSkin("inventory", "default" , "skin2");
        addSkin("main", "default" , "skin1");

        addText("drop_action", "Drop");
        addText("move_action", "Move");
        addText("weapon_equip_action", "Equip");
        addText("armor_equip_action", "Equip");
        addText("weapon_unequip_action", "Unequip");
        addText("armor_unequip_action", "Unequip");

        addText("consume_action", "Consume");
        addText("health", "health");
        addText("satiety", "satiety");

        addTexture("radinsect", "textures/objects/radinsect_1.png");

        addTexture("bleeding", "textures/ui/bleed_indicator.png");
        addTexture("radiation", "textures/ui/radiation_indicator.png");

        addTexture("player_target", "textures/player_target.png");
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

    private void addFont(String id, String file, Color color, Color borderColor, int size, float borderWidth) {
        BitmapFont font;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(file));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = size;
        params.genMipMaps = true;
        params.color = color;
        params.borderColor = borderColor;
        params.borderWidth = borderWidth;
        font = generator.generateFont(params);

        fonts.put(id, font);
    }

    private void addText(String id, String text) {
        texts.put(id, text);
    }

}
