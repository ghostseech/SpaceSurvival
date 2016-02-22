package com.asdfgaems.core.screens;

import com.asdfgaems.core.GameObject;
import com.asdfgaems.core.Inventory;
import com.asdfgaems.core.SpaceSurvival;
import com.asdfgaems.core.TileMap;
import com.asdfgaems.core.objects.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class GameScreen implements Screen {
    private SpaceSurvival app;

    private TileMap map;
    private Player player;

    private HashMap<String, GameObject> gameObjects;

    //private
    private Stage ingameUi;
    private Vector<Image> playerItems;

    public GameScreen(SpaceSurvival app) {
        this.app = app;
        this.gameObjects = new HashMap<String, GameObject>();
        this.ingameUi = new Stage(new StretchViewport(app.V_WIDTH, app.V_HEIGHT));
    }

    @Override
    public void show() {
        initMap();
        setupUi();
        Gdx.input.setInputProcessor(ingameUi);
    }

    @Override
    public void render(float v) {
        update(v);

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        app.camera.update();
        app.batch.setProjectionMatrix(app.camera.combined);
        app.batch.begin();
        map.draw(app.batch);
        player.draw(app.batch);
        for(Map.Entry<String, GameObject> entry : gameObjects.entrySet()) {
            entry.getValue().draw(app.batch);
        }
        app.batch.end();
        ingameUi.draw();
    }

    private void update(float dt) {
        int tmpx = 0;
        int tmpy = 0;
        if(Gdx.input.isKeyJustPressed(Input.Keys.W)) tmpy += 1;
        else if(Gdx.input.isKeyJustPressed(Input.Keys.S)) tmpy -= 1;
        else if(Gdx.input.isKeyJustPressed(Input.Keys.D)) tmpx += 1;
        else if(Gdx.input.isKeyJustPressed(Input.Keys.A)) tmpx -= 1;

        if(Gdx.input.isTouched()) {
            float mouseX = ((float)Gdx.input.getX() / Gdx.graphics.getWidth() * app.V_WIDTH + app.camera.position.x - (float)app.V_WIDTH/2) / 64.0f;
            float mouseY = ((1.0f - (float)Gdx.input.getY() / (float)Gdx.graphics.getHeight()) * app.V_HEIGHT + app.camera.position.y - (float)app.V_HEIGHT/2) / 64.0f;
            GameObject toched = getTouchedObject((int)mouseX, (int)mouseY);
            if(toched != null) {
                double distToPlayer = Math.sqrt((double)((toched.x - player.x)*(toched.x - player.x) + (toched.y - player.y)*(toched.y - player.y)));
                player.action(toched, "", distToPlayer);
            }
        }

        if((tmpx != 0 || tmpy != 0) && !isCollidable(player.x + tmpx, player.y + tmpy)) {
            player.move(tmpx, tmpy);
            processTurn();
        }
        app.camera.position.x = player.x * TileMap.TILE_SIZE;
        app.camera.position.y = player.y * TileMap.TILE_SIZE;

        for(int i = 0; i < player.inventory.getSize(); i++) {
            Item cur = player.inventory.getItem(i);
            if(cur != null) playerItems.get(i).setDrawable(new TextureRegionDrawable(new TextureRegion(cur.getTexture())));
        }
        ingameUi.act(dt);
    }

    private void processTurn() {
        player.processTurn();
        for(Map.Entry<String, GameObject> entry : gameObjects.entrySet()) {
            entry.getValue().processTurn();
        }
    }

    private void setupUi() {
        TextButton inventoryButton = new TextButton("aaa", app.skin, "default");
        inventoryButton.setPosition(100.0f, 100.0f);
        inventoryButton.setSize(100.0f, 100.0f);

        playerItems = new Vector<Image>();
        for(int i = 0; i < 10; i++) {
            Image tmp = new Image();
            tmp.setSize(64.0f, 64.0f);
            playerItems.add(tmp);
        }


        Table playerTable = new Table();
        playerTable.add(inventoryButton);
        playerTable.setSize(1000.0f, 500.0f);
        TextureRegionDrawable t = new TextureRegionDrawable(new TextureRegion(app.assets.get("textures/playerui.png", Texture.class)));
        playerTable.background(t);
        for(int i = 0; i < 10; i++) {
            playerTable.add(playerItems.get(i));
        }
        ingameUi.addActor(playerTable);


        //Label backgroundUi = new Label("", app.skin);
        //backgroundUi.
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

    public GameObject getTouchedObject(int x, int y) {
        for(Map.Entry<String, GameObject> entry : gameObjects.entrySet()) {
            if(entry.getValue().x == x && entry.getValue().y == y) return entry.getValue();
        }
        return null;
    }

    public boolean isCollidable(int x, int y) {
        if(map.getTile(x, y).collidable) return true;
        else {
            for(Map.Entry<String, GameObject> entry : gameObjects.entrySet()) {
                if(entry.getValue().x == x && entry.getValue().y == y && entry.getValue().collidable) return true;
            }
            return false;
        }
    }
    private void initMap() {
        TiledMap tiledMap;
        int width;
        int height;

        tiledMap = new TmxMapLoader().load("maps/test_map_2.tmx");

        //FLOOR INITIALIZATION
        TiledMapTileLayer floorLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
        width = floorLayer.getWidth();
        height = floorLayer.getHeight();

        map = new TileMap(width, height);

        for(int x = 0; x < width; x++)
            for(int y = 0; y < height; y++) {
                String type = floorLayer.getCell(x,y).getTile().getProperties().get("type", String.class);
                Texture floorTexture = null;
                if(type.equals("floor_1")) {
                    floorTexture = app.assets.get("textures/floor_1.png", Texture.class);
                }
                else if(type.equals("floor_2")) floorTexture = app.assets.get("textures/floor_2.png", Texture.class);
                map.getTile(x, y).background = floorTexture;
            }
        //WALLS INITIALIZATION
        TiledMapTileLayer wallLayer = (TiledMapTileLayer)tiledMap.getLayers().get(1);

        for(int x = 0; x < width; x++)
            for(int y = 0; y < height; y++) {
                if(wallLayer.getCell(x,y) == null) continue;
                String type = wallLayer.getCell(x,y).getTile().getProperties().get("type", String.class);
                Texture tiletexture = null;
                if(type.equals("wall_1")) tiletexture = app.assets.get("textures/wall_1.png", Texture.class);
                map.getTile(x, y).texture = tiletexture;
                map.getTile(x, y).collidable = true;
            }
        //OBJECTS INITIALIZATION
        MapObjects objects = tiledMap.getLayers().get(2).getObjects();

        for(int i = 0; i < objects.getCount(); i++) {
            MapObject object = objects.get(i);

            int x = (int)(object.getProperties().get("x", Float.class) / 32.0f);
            int y = (int)(object.getProperties().get("y", Float.class) / 32.0f) + 1;

            String type = object.getProperties().get("type", String.class);
            if(type.equals("player")) {
                player = new Player(x, y);
                player.inventory = new Inventory(10);
                player.inventory.addItem(new AcessCard(1));
                player.inventory.addItem(new AcessCard(2));
                player.inventory.addItem(new AcessCard(3));
            }
            else if(type.equals("door")) {
                int doorLevel = Integer.parseInt(object.getProperties().get("level", String.class));
                gameObjects.put(object.getName(), new Door(x, y, doorLevel));
            }
            else if(type.equals("chest")) {
                int chestLevel = Integer.parseInt(object.getProperties().get("level", String.class));
                gameObjects.put(object.getName(), new Chest(x, y, chestLevel));
            }
        }
        //for(MapObject object : objects) {
         //   String type = object.getProperties().get("type", String.class);
            //(TiledMapTileMapObject)object.
            //if(type.equals("player")) player.x = (TiledMapTileMapObject)object.
        //}

    }

    @Override
    public void dispose() {

    }
}
