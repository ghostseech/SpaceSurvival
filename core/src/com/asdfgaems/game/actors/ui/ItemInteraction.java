package com.asdfgaems.game.actors.ui;

import com.asdfgaems.ResourseLoader;
import com.asdfgaems.Vars;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class ItemInteraction extends Table {
    private ItemActionListener actionListener;
    private Image icon;
    private ItemSlot item;
    private Label nameLabel;
    private Label infoLabel;
    private NinePatch background;

    public ItemInteraction(final ItemActionListener actionListener, ItemSlot item) {
        ResourseLoader loader = ResourseLoader.instance;
        background(new TextureRegionDrawable(loader.getTextureRegion("ui_background")));

        align(Align.top);
        setSize(Vars.slotSize * 3, Vars.slotSize * 5);

        background = new NinePatch(ResourseLoader.instance.getSkin("main").getRegion("default-round"), 3, 3, 3, 3);

        setBackground(new NinePatchDrawable(background));

        this.actionListener = actionListener;
        this.item = item;

        icon = new Image(ResourseLoader.instance.getTextureRegion(item.getId()));

        nameLabel = new Label(loader.getText(item.getId() + "_name") + "(" + String.valueOf(item.getQuantity()) + ")", loader.getSkin("main"));
        infoLabel = new Label(loader.getText(item.getId() + "_description"), loader.getSkin("main"));

        add(icon).size(Vars.slotSize, Vars.slotSize);
        add(nameLabel);
        row();
        add(infoLabel);
        row();

    }

    public void addActionButton(final String id) {
        TextButton button = new TextButton(ResourseLoader.instance.getText(id), ResourseLoader.instance.getSkin("main"));
        add(button).size(Vars.slotSize * 2, Vars.slotSize / 2 * 1.5f);
        row();
        button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    actionListener.doItemAction(id);
                }
            });

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        background.draw(batch, getX(), getY(), getWidth(), getHeight());
        super.draw(batch, parentAlpha);
    }
}
