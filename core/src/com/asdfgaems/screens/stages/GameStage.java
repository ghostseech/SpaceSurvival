package com.asdfgaems.screens.stages;

import com.asdfgaems.screens.Vars;
import com.asdfgaems.screens.actors.Door;
import com.asdfgaems.screens.actors.GameActor;
import com.asdfgaems.screens.actors.TileMap;
import com.asdfgaems.screens.actors.characters.LiveActor;
import com.asdfgaems.screens.actors.characters.Player;
import com.asdfgaems.screens.actors.items.Item;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.LinkedList;

public class GameStage extends Stage implements GestureDetector.GestureListener {
    private GuiStage gui = null;
    private TileMap tileMap;
    private OrthographicCamera camera;
    private Player player;

    private LinkedList<Item> items;
    private LinkedList<GameActor> objects;
    private LinkedList<LiveActor> actors;

    private Group itemsLayer;
    private Group objectsLayer;
    private Group actorsLayer;


    private float delay = 0.0f;


    public void setGui(GuiStage gui) {
        this.gui = gui;
    }

    public GameStage() {
        this.objects = new LinkedList<GameActor>();
        this.actors = new LinkedList<LiveActor>();
        this.items = new LinkedList<Item>();

        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, Vars.WIDTH, Vars.HEIGHT);
        getViewport().setCamera(camera);
        loadLevel();
    }

    public void loadLevel() {
        tileMap = new TileMap();
        itemsLayer = new Group();
        objectsLayer = new Group();
        actorsLayer = new Group();

        addActor(tileMap);
        addActor(itemsLayer);
        addActor(objectsLayer);
        addActor(actorsLayer);

        tileMap.load("maps/test_map_2.tmx");
        loadObjects("maps/test_map_2.tmx");
        spawnItem(new Item("aa", "acess_card_1", this, true, 1, 7, 5));
        player.getInventory().addItem("acess_card_1", 1, true);
    }
    @Override
    public boolean pan(float x, float y, float deltax, float deltay) {
        if(!gui.isHidden()) return false;

        camera.translate(-deltax, deltay, 0);
        camera.update();
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if(!gui.isHidden() || player.getActionsPoints() == 0 || delay > 0.0f) return false;

        Vector3 v = camera.unproject(new Vector3(x, y, 0));

        int touchX = (int)(v.x / Vars.tileSize);
        int touchY = (int)(v.y / Vars.tileSize);

        if(Vector2.dst(player.getTileX(), player.getTileY(), touchX, touchY) <= 1.5f) {

            GameActor object = getActor(touchX, touchY);
            if (object == null) object = getObject(touchX, touchY);
            if (object == null) object = getItem(touchX, touchY);
            if (object == null) {
                if(!isCollidable(touchX, touchY, false, true, true, true)) player.move(touchX - player.getTileX(), touchY - player.getTileY());
                delay = Vars.turnAnimaionTime;
            }
            else {
               playerAction(object);
            }
        }

        return false;
    }

    private void playerAction(GameActor object) {
        if (object instanceof Door) {
            Door d = (Door)object;
            if (d.getLevel() == 0) {
                player.move(object.getTileX() - player.getTileX(), object.getTileY() - player.getTileY());
                delay = Vars.turnAnimaionTime;
            }
            else if (d.getLevel() == 1){
                if(player.getInventory().removeItem("acess_card_1", 1)) d.setLevel(0);
            }
            else if (d.getLevel() == 2){
                if(player.getInventory().removeItem("acess_card_2", 1)) d.setLevel(0);
            }
            else if (d.getLevel() == 3){
                if(player.getInventory().removeItem("acess_card_3", 1)) d.setLevel(0);
            }

        }
        else if(object instanceof Item) {
            Item i = (Item)object;
            removeObject(i);
            player.getInventory().addItem(i.getId(), i.getQuantity(), i.isStackable());
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(delay > 0.0f) delay -= delta;
        else {
            if(player.getActionsPoints() == 0) {
                for(LiveActor actor : actors) {
                    if(actor.getActionsPoints() > 0) {
                        //TODO: обработка ходов врагов(Ai.control(actor) { actor.postAction(); ...})
                        delay = Vars.turnAnimaionTime;
                        return;
                    }
                }
                nextTurn();
            }
        }
    }

    private void nextTurn() {
        player.postTurn();
        for(LiveActor actor : actors) actor.postTurn();

    }

    public boolean isCollidable(int x, int y, boolean ignoreTiles, boolean ignoreObjects, boolean ignoreDoors, boolean ignoreEnemies) {
        if(!ignoreTiles && tileMap.getTile(x, y).collidable) return true;

        if(!ignoreObjects) for(GameActor a : objects) {
            if(a.getTileX() == x && a.getTileY() == y) {
                if(!ignoreDoors && a instanceof Door) return true;
                if(!(a instanceof Door)) return true;
            }
        }
        else {
            if(!ignoreDoors) for(GameActor a : objects) {
                if(a.getTileX() == x && a.getTileY() == y) {
                    if(a instanceof Door) return true;
                }
            }
        }


        if(!ignoreEnemies) for(LiveActor a : actors) {
            if(a.getTileX() == x && a.getTileY() == y) return true;
        }

        return false;
    }

    public Item getItem(int x, int y) {
        for(Item a : items) {
            if(a.getTileX() == x && a.getTileY() == y) return a;
        }
        return null;
    }

    public GameActor getObject(int x, int y) {
        for(GameActor a : objects) {
            if(a.getTileX() == x && a.getTileY() == y) return a;
        }
        return null;
    }

    public LiveActor getActor(int x, int y) {
        for(LiveActor a : actors) {
            if(a.getTileX() == x && a.getTileY() == y) return a;
        }
        return null;
    }

    public Player getPlayer() {
        return player;
    }

    private void loadObjects(String levelid) {
        TiledMap tiledMap;
        tiledMap = new TmxMapLoader().load(levelid);
        MapObjects objects = tiledMap.getLayers().get(2).getObjects();
        for(int i = 0; i < objects.getCount(); i++) {
            parseObject(objects.get(i));
        }
    }

    private void parseObject(MapObject object) {
        int x = (int)(object.getProperties().get("x", Float.class) / 32.0f);
        int y = (int)(object.getProperties().get("y", Float.class) / 32.0f) + 1;

        String type = object.getProperties().get("type", String.class);
        String name = object.getProperties().get("name", String.class);
        if(type.equals("player")) {
            player = new Player(this, x, y);
            addActor(player);
        }
        if(type.equals("door")) {
            int level = Integer.parseInt(object.getProperties().get("level", String.class));
            Door door = Door.create(name, this, level, x ,y);
            spawnObject(door);
        }
    }

    private void spawnActor(LiveActor actor) {
        actors.add(actor);
        actorsLayer.addActor(actor);
    }

    private void spawnObject(GameActor object) {
        objects.add(object);
        objectsLayer.addActor(object);
    }

    private void spawnItem(Item item) {
        items.add(item);
        itemsLayer.addActor(item);
    }

    private void removeObject(GameActor a) {
        if(a instanceof LiveActor) {
            actors.remove(a);
            a.remove();
        }
        else if(a instanceof Item) {
            items.remove(a);
            a.remove();
        }
        else {
            objects.remove(a);
            a.remove();
        }
    }

    public void dropItem(String id, int quantity) {
        spawnItem(createItem(id, quantity, player.getTileX(), player.getTileY()));
    }

    private Item createItem(String id, int quantity, int x, int y) {
        Item item = null;
        if(id.equals("acess_card_1")) {
            item = new Item("dropped", id, this, true, quantity, x, y);
        }
        return item;
    }

    @Override
    public boolean keyDown(int keyCode) {
        if(keyCode == Input.Keys.I) {
            if(gui.isHidden()) gui.showInventory();
            else gui.hideAllWindows();
        }
        return false;
    }

    @Override
    public boolean touchDown(float v, float v1, int i, int i1) {
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
