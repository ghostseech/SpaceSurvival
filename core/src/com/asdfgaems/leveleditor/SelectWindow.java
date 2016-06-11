package com.asdfgaems.leveleditor;

import com.asdfgaems.ResourseLoader;
import com.asdfgaems.Vars;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class SelectWindow extends Group {
    private ScrollPane scrollPane;
    private Table table;

    private Label selectedLabel;

    private String selectedId;

    public SelectWindow() {
        table = new Table();
        table.align(Align.left);
        scrollPane = new ScrollPane(table);
        scrollPane.setScrollingDisabled(false, true);
        scrollPane.setSize(Vars.slotSize * 2, Vars.slotSize * 1.05f);
        addActor(scrollPane);


        selectedLabel = new Label("", ResourseLoader.instance.getSkin("main"));
        Label.LabelStyle style = selectedLabel.getStyle();
        style.font = ResourseLoader.instance.getFont("highlighted");
        selectedLabel.setStyle(style);
        selectedLabel.setPosition(0.0f,0.0f);

        addActor(selectedLabel);

        addButton("null", "close_window_button");
        selectedId = "null";
        selectedLabel.setText("null");
    }

    public void addButton(final String id, String textureId) {
        ImageButton button = new ImageButton(ResourseLoader.instance.getSkin("main"));

        Image image = new Image(ResourseLoader.instance.getTextureRegion(textureId));
        button.add(image).size(Vars.slotSize, Vars.slotSize);
        button.setSize(Vars.slotSize, Vars.slotSize);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selectedId = id;
                selectedLabel.setText(selectedId);
            }
        });

        table.setSize(table.getWidth() + Vars.slotSize, Vars.slotSize);
        table.add(button);
    }

    public void addButton(String id) {
        addButton(id, id);
    }

    public String getSelected() {
        return selectedId;
    }
}
