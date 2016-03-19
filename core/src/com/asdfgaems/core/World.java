package com.asdfgaems.core;

import com.asdfgaems.core.items.AcessCard;
import com.asdfgaems.core.items.Armor;
import com.asdfgaems.core.items.Consumable;
import com.asdfgaems.core.objects.Chest;
import com.asdfgaems.core.objects.Door;
import com.asdfgaems.core.objects.Player;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class World {
    private TileMap map;
    public Player player;
    public HashMap<String, GameObject> gameObjects;
    private SpaceSurvival app;

    private boolean needProcess;
    private boolean processing;
    private boolean active;
    private float timer;

    public World(SpaceSurvival app) {
        this.app = app;
    }

    public void create() {
        gameObjects = new HashMap<String, GameObject>();
        loadMap();
    }
    public void update(float dt) {
        if(!isActive()) return;

        player.update(dt);

        for(Map.Entry<String, GameObject> entry : gameObjects.entrySet()) {
            entry.getValue().update(dt);
        }

        if(needProcess) processTurn();

        if(timer > 0.0f) {
            timer -= dt;
            return;
        }

        if(player.needUpdate) {
            timer = player.processTurn();
            return;
        }

        for(Map.Entry<String, GameObject> entry : gameObjects.entrySet()) {
            if(entry.getValue().needUpdate) {
                timer = entry.getValue().processTurn();
                return;
            }
        }
    }

    private void processTurn() {
        processing = true;
        player.needUpdate = true;
        for(Map.Entry<String, GameObject> entry : gameObjects.entrySet()) {
            entry.getValue().needUpdate = true;
        }
        needProcess = false;
    }

    public void click() {
        if(!isActive()) return;
        GameObject touched = app.getTocuhed();
        if(touched != null) {
            player.action(touched, "", player.getDist(touched));
        }

        List<Vector2> path = getPath(player.x, player.y, app.getTouchedX(), app.getToucedY());
        if(isCollidable(app.getTouchedX(), app.getToucedY()) && path.size() >=1) path.remove(path.size()-1);
        player.move(path);
    }


    public boolean isCollidable(int x, int y) {
        if(map.getTile(x, y) == null) return true;

        if(map.getTile(x, y).collidable) return true;
        else {
            for(Map.Entry<String, GameObject> entry : gameObjects.entrySet()) {
                if(entry.getValue().x == x && entry.getValue().y == y && entry.getValue().collidable) return true;
            }
            return false;
        }
    }

    public boolean isVisibleFrom(int startx, int starty, int endx, int endy) {
        for(int x = 0; x < Math.abs(startx - endx); x++) {
            for(int y = 0; y < Math.abs(starty - endy); y++) {

            }
        }
        return true;
    }


    public void draw(SpriteBatch batch) {
        map.draw(batch);
        player.draw(batch);
        for(Map.Entry<String, GameObject> entry : gameObjects.entrySet()) {
            entry.getValue().draw(batch);
        }
    }
    public void loadMap() {
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
                player = new Player(this, x, y);
                player.inventory = new Inventory(20);
                String items = object.getProperties().get("items", String.class);
                parseItems(items, player.inventory);
            }
            else if(type.equals("door")) {
                int doorLevel = Integer.parseInt(object.getProperties().get("level", String.class));
                gameObjects.put(object.getName(), new Door(this, x, y, doorLevel));
            }
            else if(type.equals("chest")) {
                int chestLevel = Integer.parseInt(object.getProperties().get("level", String.class));
                Chest chest = new Chest(this, x, y, chestLevel);

                String items = object.getProperties().get("items", String.class);
                parseItems(items, chest.inventory);

                gameObjects.put(object.getName(), chest);
            }
        }
    }

    private void parseItems(String items, Inventory inv) {
        String[] itamsArray = items.split("\\s+");
        for(String s: itamsArray) {
            if(s.equals("card_1")) inv.addItem(new AcessCard(1));
            if(s.equals("card_2")) inv.addItem(new AcessCard(2));
            if(s.equals("card_3")) inv.addItem(new AcessCard(3));
            if(s.equals("armor_base")) inv.addItem(new Armor(Armor.ARMOR_BASE));
            if(s.equals("food_ration_1")) inv.addItem(new Consumable(Consumable.FOOD_RATION_1));
            if(s.equals("health_1")) inv.addItem(new Consumable(Consumable.HEALTH_1));
        }
    }

    public List<Vector2> getPath(int startx, int starty, int endx, int endy) {
        PathFinder pf = new PathFinder(this);
        return pf.findPath(startx, starty, endx, endy);
    }

    public void requestTurnProcess() {
        needProcess = true;
    }
    public boolean isActive() {
        return active;
    }

    public void activate() {
        active = true;
    }

    public void deactivate() {
        active = false;
    }
    public int getMapWidth() {
        return map.width;
    }
    public int getMapHeight() {
        return map.height;
    }

    public void dispose() {
        gameObjects.clear();
        player.dispose();
    }
}
