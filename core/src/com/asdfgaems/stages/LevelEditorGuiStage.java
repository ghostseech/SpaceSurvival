package com.asdfgaems.stages;

import com.asdfgaems.App;
import com.asdfgaems.ResourseLoader;
import com.asdfgaems.Vars;
import com.asdfgaems.leveleditor.SelectWindow;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class LevelEditorGuiStage extends Stage {
    private App app;
    private LevelEditorStage stage;
    private SelectWindow floorSelectWindow;

    private TextButton setFloorEditMode;
    private TextButton setTileEditMode;
    private TextButton setObjectsEditMode;

    public LevelEditorGuiStage(final App app) {
        this.app = app;
        floorSelectWindow = new SelectWindow();
        floorSelectWindow.addButton("floor_1");
        floorSelectWindow.addButton("floor_2");
        floorSelectWindow.setPosition(40, 50);


        setFloorEditMode = new TextButton("Floor edit", ResourseLoader.instance.getSkin("main"));
        setFloorEditMode.setSize(Vars.slotSize, Vars.slotSize/2);
        setFloorEditMode.setPosition(Vars.slotSize/2, Vars.slotSize * 8);
        addActor(setFloorEditMode);

        setTileEditMode = new TextButton("Tile edit", ResourseLoader.instance.getSkin("main"));
        setTileEditMode.setSize(Vars.slotSize, Vars.slotSize/2);
        setTileEditMode.setPosition(Vars.slotSize/2, Vars.slotSize * 7);
        addActor(setTileEditMode);

        setObjectsEditMode = new TextButton("Object edit", ResourseLoader.instance.getSkin("main"));
        setObjectsEditMode.setSize(Vars.slotSize, Vars.slotSize/2);
        setObjectsEditMode.setPosition(Vars.slotSize/2, Vars.slotSize * 6);
        addActor(setObjectsEditMode);

        addActor(floorSelectWindow);
    }

    public String getFloorSelectedId() {
        return floorSelectWindow.getSelected();
    }

    public void setStage(LevelEditorStage stage) {
        this.stage = stage;
    }
}
