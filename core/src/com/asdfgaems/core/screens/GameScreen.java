package com.asdfgaems.core.screens;

import com.asdfgaems.core.SpaceSurvival;
import com.asdfgaems.core.TileMap;
import com.asdfgaems.core.objects.Player;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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

public class GameScreen implements Screen {
    private SpaceSurvival app;

    private TileMap map;
    private Player player;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;

    public GameScreen(SpaceSurvival app) {
        this.app = app;
    }

    @Override
    public void show() {
        initMap();
        Player.texture = app.assets.get("textures/player1.png", Texture.class);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        app.batch.begin();
        map.draw(app.batch);
        player.draw(app.batch);
        app.batch.end();
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

    private void initMap() {
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
