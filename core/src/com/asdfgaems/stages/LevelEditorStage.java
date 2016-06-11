package com.asdfgaems.stages;

import com.asdfgaems.App;
import com.asdfgaems.Vars;
import com.asdfgaems.leveleditor.TileLayer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LevelEditorStage extends Stage  implements GestureDetector.GestureListener {
    private App app;
    private LevelEditorGuiStage gui;

    private OrthographicCamera camera;

    private TileLayer floorLayer;

    private EditorMode mode;

    public enum EditorMode {
        floorEditor,
        wallEditor,
        objectEditor,
        lightsEditor;
    }

    public LevelEditorStage(final App app) {
        this.app = app;
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, Vars.WIDTH, Vars.HEIGHT);
        setViewport(new ScreenViewport());
        getViewport().setCamera(camera);

        mode = EditorMode.floorEditor;

        floorLayer = new TileLayer();
        addActor(floorLayer);
    }

    public void setGui(LevelEditorGuiStage gui) {
        this.gui = gui;
        gui.setStage(this);
    }

    @Override
    public boolean touchDown(float v, float v1, int i, int i1) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 v = camera.unproject(new Vector3(x, y, 0));

        int touchX = (int)(v.x / Vars.tileSize);
        int touchY = (int)(v.y / Vars.tileSize);

        switch (mode) {
            case floorEditor:
                floorLayer.setTile(touchX, touchY, gui.getFloorSelectedId());
                break;
            case wallEditor:
                break;
            case objectEditor:
                break;
        }
        return false;
    }

    @Override
    public boolean longPress(float v, float v1) {
        return false;
    }

    @Override
    public boolean fling(float v, float v1, int i) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltax, float deltay) {
        camera.translate(-deltax, deltay, 0);
        camera.update();
        return false;
    }


    @Override
    public boolean panStop(float v, float v1, int i, int i1) {
        return false;
    }

    @Override
    public boolean zoom(float v, float v1) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 vector2, Vector2 vector21, Vector2 vector22, Vector2 vector23) {
        return false;
    }
}
