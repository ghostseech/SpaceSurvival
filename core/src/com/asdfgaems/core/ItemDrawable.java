package com.asdfgaems.core;

import com.asdfgaems.core.objects.Item;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class ItemDrawable {
    private Stage stage;
    private Skin skin;
    private Item item;

    private Label itemName;
    private Label itemDescription;
    private Image itemIcon;

    private Table table;

    private float x;
    private float y;
    private float width;
    private float height;
    private float buttonHeight;
    private float iconSize;


    public ItemDrawable(Stage stage, Skin skin, Item item, float x, float y, float width, float height, float buttonHeight, float iconSize) {
        this.stage = stage;
        this.skin = skin;
        this.item = item;

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.buttonHeight = buttonHeight;
        this.iconSize = iconSize;

        create();
    }

    public Item getItem() {
        return item;
    }
    public void show() {
        stage.addActor(itemName);
        stage.addActor(itemDescription);
        stage.addActor(itemIcon);
        stage.addActor(table);
    }

    public void hide() {
        stage.getActors().removeValue(itemName, true);
        stage.getActors().removeValue(itemDescription, true);
        stage.getActors().removeValue(itemIcon, true);
        stage.getActors().removeValue(table, true);
    }

    public void addCommandButton(String text, final Command command) {
        TextButton button = new TextButton(text, skin);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                command.execute();
            }
        });
        table.add(button).size(width, buttonHeight/2);
        table.row();
    }

    public void create() {
        itemIcon = new Image(item.getTexture());
        itemIcon.setSize(iconSize, iconSize);
        itemIcon.setPosition(x, y);

        itemName = new Label(item.getName(), skin);
        itemName.setPosition(x + iconSize * 1.5f, y);
        itemName.setSize(width - iconSize, height);
        //itemName.setAlignment(Align.center);

        itemDescription = new Label(item.getDescription(), skin);
        itemDescription.setSize(width, height);
        itemDescription.setPosition(x, y - height);
        itemDescription.setWrap(true);

        table = new Table();
        table.setSize(width, buttonHeight * 8);
        table.setPosition(x, y - buttonHeight * 9);
        table.align(Align.top);
    }
}
