package com.asdfgaems.core;

import com.asdfgaems.core.items.AcessCard;
import com.asdfgaems.core.items.Armor;
import com.asdfgaems.core.items.Consumable;
import com.asdfgaems.core.items.Weapon;
import com.asdfgaems.core.objects.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CancellationException;

public class World {
    private TileMap map;
    public Player player;
    public LinkedList<GameObject> gameObjects;
    public LinkedList<Enemy> enemies;
    public LinkedList<DroppedItem> droppedItems;

    private SpaceSurvival app;

    private boolean needProcess;
    private boolean processing;
    private boolean active;
    private float timer;

    public World(SpaceSurvival app) {
        this.app = app;
    }

    public void create() {
        gameObjects = new LinkedList<GameObject>();
        enemies = new LinkedList<Enemy>();
        droppedItems = new LinkedList<DroppedItem>();

        loadMap();
        droppedItems.add(new DroppedItem(this, "test", new Weapon(Weapon.WEAPON_SHOTGUN), 4, 4));
    }
    public void update(float dt) {
        if(!isActive()) return;

        player.update(dt);

        for(GameObject g : gameObjects) {
            g.update(dt);
        }
        for(Enemy e : enemies) {
            e.update(dt);
        }

        for(Enemy e : enemies) {
            if(e.getHp() <= 0.0f) {
                e.death();
                enemies.remove(e);
            }
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
        for(GameObject g : gameObjects) {
            if(g.needUpdate) {
                timer = g.processTurn();
                return;
            }
        }
        for(Enemy e : enemies) {
            if(e.needUpdate) {
                timer = e.processTurn();
                return;
            }
        }

        processing = false;
        if(player.isMoving()) processTurn();
    }

    private void processTurn() {
        processing = true;
        player.needUpdate = true;
        for(GameObject g : gameObjects) {
            g.needUpdate = true;
        }
        for(Enemy e : enemies) {
            e.needUpdate = true;
        }
        needProcess = false;
    }

    public void click() {
        if(!isActive()) return;

        player.move(new LinkedList<Vector2>());
        GameObject touched = getEnemy(app.getTouchedX(), app.getTouchedY());
        if(touched == null) touched = getObject(app.getTouchedX(), app.getTouchedY());
        if(touched == null) touched = getDroppedItem(app.getTouchedX(), app.getTouchedY());
        if(touched != null) {
            player.action(touched, touched.getIdName(), player.getDist(touched));

            if(touched.getClass() == Chest.class) {
                if(((Chest)touched).getLevel() == 0) app.gameScreen.showSwapItemsWindow(player, ((Chest)touched).inventory);
            }
            if(touched.getClass() == Door.class) {
                List<Vector2> path = getPath(player.x, player.y, app.getTouchedX(), app.getTouchedY());
                if(isCollidable(app.getTouchedX(), app.getTouchedY()) && path.size() >=1) path.remove(path.size()-1);
                player.move(path);
            }
            if(touched.getClass() == DroppedItem.class) {
                if(!player.inventory.isFull()) {
                    player.inventory.addItem(((DroppedItem)touched).getItem());
                    droppedItems.remove(touched);
                }
            }
        }

        if(touched == null) {
            List<Vector2> path = getPath(player.x, player.y, app.getTouchedX(), app.getTouchedY());
            if(isCollidable(app.getTouchedX(), app.getTouchedY()) && path.size() >=1) path.remove(path.size()-1);
            player.move(path);
        }
    }


    public boolean isCollidable(int x, int y) {
        if(map.getTile(x, y) == null) return true;

        if(map.getTile(x, y).collidable) return true;
        else {
            for(GameObject g: gameObjects) {
                if(g.x == x && g.y == y && g.collidable) return true;
            }
            for(Enemy e: enemies) {
                if(e.x == x && e.y == y && e.collidable) return true;
            }
            return false;
        }
    }

    public boolean isVisibleFrom(int startx, int starty, int endx, int endy) {
        LinkedList<Vector2> coords = new LinkedList<Vector2>();

        int x0 = startx;
        int y0 = starty;

        int x1 = endx;
        int y1 = endy;

        boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
        if(steep) {
            int tmpx = x0;
            x0 = y0;
            y0 = tmpx;

            int tmpy = x1;
            x1 = y1;
            y1 = tmpy;
        }

        if(x0 > x1) {
            int tmpx = x0;
            x0 = x1;
            x1 = tmpx;

            int tmpy = y0;
            y0 = y1;
            y1 = tmpy;
        }

        int dx = x1 - x0;
        int dy = Math.abs(y1 - y0);

        int error = dx/2;
        int ystep = (y0 < y1) ? 1 : -1;
        int y = y0;

        for(int x = x0; x <= x1; x++) {
            coords.add(new Vector2(steep ? y : x, steep ? x : y));
            error -= dy;
            if(error < 0) {
                y += ystep;
                error += dx;
            }
        }

        for(int i = 0; i < coords.size(); i ++) {
            if(map.getTile((int)coords.get(i).x, (int)coords.get(i).y).collidable) return false;
        }

        for(int i = 0; i < coords.size(); i ++) {
            GameObject object = getObject((int)coords.get(i).x, (int)coords.get(i).y);
            if(object != null) {
                if(object.collidable) return false;
            }
        }

        return true;
    }

    public void draw(SpriteBatch batch) {
        map.draw(batch);
        player.draw(batch);
        for(GameObject g : gameObjects) {
            g.draw(batch);
        }
        for(Enemy e : enemies) {
            e.draw(batch);
        }
        for(DroppedItem e : droppedItems) {
            e.draw(batch);
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
            String name = object.getProperties().get("name", String.class);
            if(type.equals("player")) {
                player = new Player(this, name, x, y);
                player.inventory = new Inventory(20);
                String items = object.getProperties().get("items", String.class);
                parseItems(items, player.inventory);
            }
            else if(type.equals("door")) {
                int doorLevel = Integer.parseInt(object.getProperties().get("level", String.class));
                gameObjects.add(new Door(this, name, x, y, doorLevel));
            }
            else if(type.equals("chest")) {
                int chestLevel = Integer.parseInt(object.getProperties().get("level", String.class));
                Chest chest = new Chest(this, name, x, y, chestLevel);

                String items = object.getProperties().get("items", String.class);
                parseItems(items, chest.inventory);

                gameObjects.add(chest);
            }
            else if(type.equals("slime")) {
                enemies.add(new Slime(this, name, x, y));
            }
            else if(type.equals("radinsect")) {
                enemies.add(new RadInsect(this, name, x, y));
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

            if(s.equals("antirad_mixture")) inv.addItem(new Consumable(Consumable.ANTIRAD_MIXTURE));
            if(s.equals("antitoxic_mixture")) inv.addItem(new Consumable(Consumable.ANTITOXIC_MIXTURE));
            if(s.equals("attention_mixture")) inv.addItem(new Consumable(Consumable.ATTENTION_MIXTURE));
            if(s.equals("hpregen_mixture")) inv.addItem(new Consumable(Consumable.REGEN_MIXTURE));


            if(s.equals("knife")) inv.addItem(new Weapon(Weapon.WEAPON_KNIFE));
            if(s.equals("security_gun")) inv.addItem(new Weapon(Weapon.WEAPON_SECURITY_GUN));
            if(s.equals("rapidgun")) inv.addItem(new Weapon(Weapon.WEAPON_RAPIDGUN));
            if(s.equals("army_rifle")) inv.addItem(new Weapon(Weapon.WEAPON_ARMY_RIFLE));
            if(s.equals("shotgun")) inv.addItem(new Weapon(Weapon.WEAPON_SHOTGUN));
            if(s.equals("plasmagun")) inv.addItem(new Weapon(Weapon.WEAPON_PLASMAGUN));
        }
    }

    public GameObject getObject(int x, int y) {
        for(GameObject g : gameObjects) {
            if(g.x == x && g.y == y) return g;
        }
        return null;
    }

    public Enemy getEnemy(int x, int y) {
        for(Enemy e : enemies) {
            if(e.x == x && e.y == y) return e;
        }
        return null;
    }

    public DroppedItem getDroppedItem(int x, int y) {
        for(DroppedItem e : droppedItems) {
            if(e.x == x && e.y == y) return e;
        }
        return null;
    }

    public List<Vector2> getPath(int startx, int starty, int endx, int endy) {
        PathFinder pf = new PathFinder(this);
        return pf.findPath(startx, starty, endx, endy);
    }

    public void requestTurnProcess() {
        if(!processing)needProcess = true;
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

    public void cancelPlayerMoveing() {
        player.move(new LinkedList<Vector2>());
    }



    public void dispose() {
        gameObjects.clear();
        player.dispose();
    }
}
