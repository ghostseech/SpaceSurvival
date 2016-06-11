package com.asdfgaems.stages;

import com.asdfgaems.Vars;
import com.asdfgaems.game.PathFinder;
import com.asdfgaems.game.actors.PlayerPath;
import com.asdfgaems.game.actors.characters.RadInsect;
import com.asdfgaems.game.actors.items.Consumable;
import com.asdfgaems.game.actors.items.armor.ClothArmor;
import com.asdfgaems.game.actors.items.weapon.Pistol;
import com.asdfgaems.game.actors.objects.Chest;
import com.asdfgaems.game.actors.objects.Door;
import com.asdfgaems.game.actors.GameActor;
import com.asdfgaems.game.actors.TileMap;
import com.asdfgaems.game.actors.characters.LiveActor;
import com.asdfgaems.game.actors.characters.Player;
import com.asdfgaems.game.actors.items.Item;
import com.asdfgaems.game.actors.ui.Inventory;
import com.asdfgaems.utils.LightMap;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import rlforj.los.IFovAlgorithm;
import rlforj.los.ILosBoard;
import rlforj.los.PrecisePermissive;

import java.util.LinkedList;
import java.util.List;

public class GameStage extends Stage implements GestureDetector.GestureListener, ILosBoard {
    private GuiStage gui = null;
    private TileMap tileMap;
    private OrthographicCamera camera;
    private Player player;
    private PathFinder pathFinder;

    private PlayerPath playerPath;
    private Vector2 playerTarget;

    private LinkedList<Item> items;
    private LinkedList<GameActor> objects;
    private LinkedList<LiveActor> actors;

    private Group itemsLayer;
    private Group objectsLayer;
    private Group actorsLayer;

    private LinkedList<Door> closedDoors;

    private IFovAlgorithm fovAlgorithm;

    private LightMap lightMap;

    private float delay = 0.0f;


    public void setGui(GuiStage gui) {
        this.gui = gui;
        gui.setPlayerInventory(player.getInventory());
        gui.setPlayer(player);
    }

    public GameStage() {
        this.objects = new LinkedList<GameActor>();
        this.actors = new LinkedList<LiveActor>();
        this.items = new LinkedList<Item>();
        this.closedDoors = new LinkedList<Door>();

        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, Vars.WIDTH, Vars.HEIGHT);
        setViewport(new ScreenViewport());
        getViewport().setCamera(camera);
        getViewport().setWorldSize(Vars.WIDTH * Vars.gameScale, Vars.HEIGHT * Vars.gameScale);
        playerPath = new PlayerPath();
        loadLevel();
        pathFinder = new PathFinder(this);
        pathFinder.update(objects, actors);

        playerTarget = new Vector2(-1, -1);


        player.getParameters().addBuff("bleeding", 10);
        player.getParameters().addBuff("radiation", 5);

        lightMap = new LightMap(tileMap.getMapWidth(), tileMap.getMapHeight());
        addActor(lightMap);
        fovAlgorithm = new PrecisePermissive();
        updateLights();
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
        addActor(playerPath);

        tileMap.load("maps/test_map_2.tmx");
        loadObjects("maps/test_map_2.tmx");
        spawnItem(new Item("acess_card_1", this, true, 1, 7, 5));
        spawnItem(new Pistol(this, 13, 5));
        spawnItem(new ClothArmor(this, 4, 5));
        spawnActor(new RadInsect("ss", this, 3, 3));
        player.getInventory().addItem(new Item("acess_card_1", this, true, 1, 0, 0));
        cameraToPlayer();
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
        if(!gui.isHidden()) return false;

        Vector3 v = camera.unproject(new Vector3(x, y, 0));

        int touchX = (int)(v.x / Vars.tileSize);
        int touchY = (int)(v.y / Vars.tileSize);
        if(!lightMap.isVisited(touchX, touchY)) {
            playerTarget.set(-1, -1);
            return false;
        }

        if(Vector2.dst(player.getTileX(), player.getTileY(), touchX, touchY) <= 1.5f) {
            Item i = getItem(touchX, touchY);
            if (i != null && player.getInventory().haveSpaceForItem(i)) {
                GameActor a = getObject(touchX, touchY);
                LiveActor b = getActor(touchX, touchY);

                boolean hereActor;
                boolean hereObject;
                boolean hereOpenedDoor;
                hereActor = b != null;
                if(hereActor) {
                    if(b instanceof Player) hereActor = false;
                }
                hereObject = a != null;

                if (a != null) {
                    if (a instanceof Door) {
                        if (!((Door) a).isClosed()) {
                            hereOpenedDoor = true;
                        } else hereOpenedDoor = false;
                    } else hereOpenedDoor = false;
                } else hereOpenedDoor = false;

                if (hereOpenedDoor) hereObject = false;

                if (!hereActor && !hereObject) {
                    removeObject(i);
                    player.getInventory().addItem(i);
                } else {
                    playerTarget.x = touchX;
                    playerTarget.y = touchY;
                }
            }
            else {
                playerTarget.x = touchX;
                playerTarget.y = touchY;
            }
        }
        else {
            playerTarget.x = touchX;
            playerTarget.y = touchY;
        }
        return false;
    }

    private void playerAction(GameActor object) {
        //------------------------------------------------------------------
        //-----------------------------ACTIONS WITH DOOR
        //------------------------------------------------------------------
        if (object instanceof Door) {
            Door d = (Door)object;
            if (d.getLevel() == 0) {
                if(d.isClosed()) {
                    d.open();
                    postPlayerAction();
                    updateLights();
                }
                else {
                    player.move(object.getTileX() - player.getTileX(), object.getTileY() - player.getTileY());
                    delay = Vars.turnAnimaionTime;
                    pathFinder.update(objects, actors);
                    closedDoors.remove(d);
                    updateLights();
                }
            }
            else if (d.getLevel() == 1) {
                if(player.getInventory().removeItem("acess_card_1", 1)) {
                    d.setLevel(0);
                    postPlayerAction();
                }
            }
            else if (d.getLevel() == 2) {
                if(player.getInventory().removeItem("acess_card_2", 1)) {
                    d.setLevel(0);
                    postPlayerAction();
                }
            }
            else if (d.getLevel() == 3) {
                if(player.getInventory().removeItem("acess_card_3", 1)) {
                    d.setLevel(0);
                    postPlayerAction();
                }
            }
        }
        //------------------------------------------------------------------
        //-----------------------------ACTIONS WITH CHEST
        //------------------------------------------------------------------
        else if(object instanceof Chest) {
            Chest c = (Chest)object;
            if(c.getLevel() == 0) {
                gui.openChest(c.getInventory());
            }
            else if(c.getLevel() == 1) {
                if(player.getInventory().removeItem("acess_card_1", 1)) {
                    c.setLevel(0);
                    postPlayerAction();
                }
            }
            else if(c.getLevel() == 2) {
                if(player.getInventory().removeItem("acess_card_2", 1)) {
                    c.setLevel(0);
                    postPlayerAction();
                }
            }
            else if(c.getLevel() == 3) {
                if(player.getInventory().removeItem("acess_card_3", 1)) {
                    c.setLevel(0);
                    postPlayerAction();
                }
            }
        }
        //------------------------------------------------------------------
        //-----------------------------ACTIONS WITH ITEM
        //------------------------------------------------------------------
        /*else if(object instanceof Item) {
            Item i = (Item)object;
            removeObject(i);
            player.getInventory().addItem(i);
            postPlayerAction();
        }*/
    }

    @Override
    public void act(float delta) {
        if(!gui.isHidden()) return;
        super.act(delta);

        if(delay > 0.0f) delay -= delta;
        else {
            if(player.getActionsPoints() == 0) {
                for(LiveActor actor : actors) {
                    if(actor.getActionsPoints() > 0) {
                        updateLights();
                        actor.behavoir();
                        pathFinder.update(objects, actors);
                        if(actor.isVisible()) delay = Vars.turnAnimaionTime;
                        removeDeadActors();
                        return;
                    }
                }
                nextTurn();
            }
            else {
                movePlayerToTarget();
            }
        }
    }

    private void removeDeadActors() {
        for(LiveActor a : actors) {
            if(a.getParameters().getHealth() <= 0.0f) removeObject(a);
        }
    }

    private void movePlayerToTarget() {
        LiveActor a = getActor((int)playerTarget.x, (int)playerTarget.y);
        if(a == player) a = null;

        if(a != null && a.isVisible()) {
            if(player.getParameters().getAttackRange() >= Vector2.dst(player.getTileX(), player.getTileY(), a.getTileX(), a.getTileY())) {
                if(player.getWeapon() != null) {
                    if(player.getWeapon().getRequiredAmmo().equals("null"))player.getParameters().attack(a.getTileX(), a.getTileY());
                    else {
                        if(player.getInventory().removeItem(player.getWeapon().getRequiredAmmo(), 1)) {
                            player.getParameters().attack(a.getTileX(), a.getTileY());
                            gui.onAttack();
                        }
                    }
                }
                else player.getParameters().attack(a.getTileX(), a.getTileY());

                postPlayerAction();
                playerTarget.set(-1, -1);
                playerPath.removePath();
            }
            else playerMoving();
        }
        else playerMoving();
    }

    private void playerMoving() {
        if(playerTarget.x != -1 && playerTarget.y != -1) {
            List<Vector2> path = pathFinder.findPath((int)player.getTileX(), (int)player.getTileY(), (int)playerTarget.x, (int)playerTarget.y, PathFinder.liveActorIgnoreList);
            if(path.size() >= 1) playerPath.setPath(path);
            if(path.size() > 0) {
                Vector2 cur = path.get(0);
                Vector2 dir = new Vector2(0,0);
                dir.x = cur.x - player.getTileX();
                dir.y = cur.y - player.getTileY();
                if(path.size() == 1) {
                    playerTarget = new Vector2(-1, -1);
                }

                if(Vector2.dst(player.getTileX(), player.getTileY(), cur.x, cur.y) <= 1.5f) {
                    GameActor object = getActor((int)cur.x, (int)cur.y);
                    if(object == player) object = null;
                    if (object == null) object = getObject((int)cur.x, (int)cur.y);
                    if (object == null) {
                        if(!isCollidable((int)cur.x, (int)cur.y, false, true, true, true)) {
                            player.move((int)cur.x - player.getTileX(), (int)cur.y - player.getTileY());
                            path.remove(0);
                            if(path.size() == 0) playerPath.setPath(path);
                            delay = Vars.turnAnimaionTime;
                            pathFinder.update(objects, actors);
                            updateLights();
                        }
                    }
                    else {
                        playerAction(object);
                        path.remove(0);
                        if(path.size() == 0) playerPath.setPath(path);
                    }

                }
            }
        }
        else  playerPath.removePath();
    }


    private void nextTurn() {
        player.postTurn();
        for(LiveActor actor : actors) actor.postTurn();
        autoClosingDoors();
    }

    private void autoClosingDoors() {
        for(GameActor a : objects) {
            if(a instanceof Door) {
                LiveActor b = getActor(a.getTileX(), a.getTileY());
                if((a.getTileX() != player.getTileX() || a.getTileY() != player.getTileY()) && b == null) {
                    if(((Door) a).autoClose() && !closedDoors.contains(a)){
                        closedDoors.add((Door)a);
                        updateLights();
                    }
                }
            }
        }
    }

    public boolean isCollidable(int x, int y, boolean ignoreTiles, boolean ignoreObjects, boolean ignoreDoors, boolean ignoreEnemies) {
        if(x >= getWorldWidth() || y >= getWorldHeight() || x < 0 || y < 0) return true;
        if(!ignoreTiles && tileMap.getTile(x, y).collidable) return true;
        GameActor a = null;
        if(!ignoreEnemies) a = getActor(x, y);
        if(a != null) return true;

        if(!ignoreObjects && !ignoreDoors) {
            a = getObject(x, y);
            if(a != null) return true;
        }

        if(!ignoreDoors && ignoreObjects) {
            a = getObject(x, y);
            if(a != null) {
                if(a instanceof Door) {
                    if(((Door)a).getLevel() != 0) return true;
                }
            }
        }

        if(ignoreDoors && !ignoreObjects) {
            a = getObject(x, y);
            if(a != null) {
                if(! (a instanceof Door)) return  true;
            }
        }

        return false;
    }

    public Item getItem(int x, int y) {
        Array<Item> a = getItemsList(x, y);
        if(a.size > 0) return a.get(a.size - 1);
        return null;
    }

    public Array<Item> getItemsList(int x, int y) {
        Array<Item> array = new Array<Item>();
        for(Item a : items) {
            if(a.getTileX() == x && a.getTileY() == y) array.add(a);
        }
        return array;
    }

    public GameActor getObject(int x, int y) {
        for(GameActor a : objects) {
            if(a.contains(x, y)) return a;
        }
        return null;
    }

    public LiveActor getActor(int x, int y) {
        for(LiveActor a : actors) {
            if(a.contains(x, y)) return a;
        }
        if(player.getTileX() == x && player.getTileY() == y) return player;
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
            String itemsString = object.getProperties().get("items", String.class);
            parseItems(player.getInventory(), itemsString);
            addActor(player);
        }
        if(type.equals("door")) {
            int level = Integer.parseInt(object.getProperties().get("level", String.class));
            Door door = new Door(name, this, level, x, y);
            spawnObject(door);
        }
        if(type.equals("chest")) {
            int level = Integer.parseInt(object.getProperties().get("level", String.class));
            Chest chest = new Chest(name, this, level, x, y);
            String itemsString = object.getProperties().get("items", String.class);
            parseItems(chest.getInventory(), itemsString);
            spawnObject(chest);
        }
        if(type.equals("radinsect")) {
            RadInsect radInsect = new RadInsect(name, this, x, y);
            spawnActor(radInsect);
        }
    }

    private void parseItems(Inventory inventory, String itemsString) {
        String[] itamsArray = itemsString.split("\\s+");
        for(String s: itamsArray) {
            if(s.equals("card_1")) inventory.addItem(new Item("acess_card_1", this, true, 1, 0, 0));
            if(s.equals("card_2")) inventory.addItem(new Item("acess_card_2", this, true, 1, 0, 0));
            if(s.equals("card_3")) inventory.addItem(new Item("acess_card_3", this, true, 1, 0, 0));
            if(s.equals("medkit_1")) inventory.addItem(new Consumable("medkit_1", this, 1, 0, 0));
            if(s.equals("food_ration_1")) inventory.addItem(new Consumable("food_ration_1", this, 1, 0, 0));

            /*if(s.equals("armor_base")) inv.addItem(new Armor(Armor.ARMOR_BASE));
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
            if(s.equals("plasmagun")) inv.addItem(new Weapon(Weapon.WEAPON_PLASMAGUN));*/
        }
    }

    private void spawnActor(LiveActor actor) {
        actors.add(actor);
        actorsLayer.addActor(actor);
    }

    private void spawnObject(GameActor object) {
        objects.add(object);
        objectsLayer.addActor(object);
        if(object instanceof Door) closedDoors.add((Door)object);
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

    public void dropItem(Item item) {
        item.setTilePosition(player.getTileX(), player.getTileY());

        if(item.isStackable()) {
            Array<Item> array = getItemsList(player.getTileX(), player.getTileY());
            for(Item a : array) {
                if(a.compareItemId(item)) {
                    a.addQuantity(item.getQuantity());
                    return;
                }
            }
        }
        spawnItem(item);
    }

    public boolean isActorVisible(LiveActor actor) {
        for(int x = actor.getTileX(); x < actor.getTileX() + actor.getTileWidth(); x ++) {
            for(int y = actor.getTileY(); y < actor.getTileY() + actor.getTileHeight(); y ++) {
                if(lightMap.isVisible(x, y)) return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyDown(int keyCode) {
        return false;
    }

    public void postPlayerAction() {
        delay = Vars.turnAnimaionTime;
        pathFinder.update(objects, actors);
        player.postAction();
        removeDeadActors();
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public int getWorldWidth() {
        return tileMap.getMapWidth();
    }

    public int getWorldHeight() {
        return tileMap.getMapHeight();
    }

    @Override
    public boolean touchDown(float v, float v1, int i, int i1) {
        return false;
    }

    @Override
    public boolean longPress(float v, float v1) {
        //System.out.println("LONGPRESS");
        return false;
    }

    public void cameraToPlayer() {
        camera.position.x = player.getX();
        camera.position.y = player.getY();
        camera.update();
    }

    @Override
    public boolean contains(int x, int y) {
        return x >= 0 && x < tileMap.getMapWidth() && y >= 0 && y < tileMap.getMapHeight();
    }

    @Override
    public boolean isObstacle(int x, int y) {
        if(!contains(x, y)) return false;
        if(tileMap.getTile(x, y).collidable) return true;
        for(Door d : closedDoors) {
            if(d.isClosed() && d.getTileX() == x && d.getTileY() == y) return true;
        }
        return false;
    }

    @Override
    public void visit(int x, int y) {
        lightMap.visit(x, y);
    }

    public void updateLights() {
        lightMap.resetLights();
        fovAlgorithm.visitFieldOfView(this, player.getTileX(), player.getTileY(), (int)player.getParameters().getVisionRange());

        for(LiveActor a : actors) {
            if(isActorVisible(a)) a.setVisible(true);
            else a.setVisible(false);
        }
    }

    public void setPlayerTarget(int x, int y) {
        playerTarget.set(x, y);
    }

    public void removePlayerTarget() {
        playerTarget.set(-1, -1);
    }

    public List<Vector2> getPath(int startx, int staryy, int endx, int endy) {
        return pathFinder.findPath(startx, staryy, endx, endy, PathFinder.liveActorIgnoreList);
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
            if(tileMap.getTile((int)coords.get(i).x, (int)coords.get(i).y).collidable) return false;
        }

        for(int i = 0; i < coords.size(); i ++) {
            GameActor object = getObject((int)coords.get(i).x, (int)coords.get(i).y);
            if(object != null) {
                if(object instanceof Door) {
                    if(((Door)object).isClosed()) return false;
                }
                else return true;
            }
        }

        return true;
    }

    public boolean isPlayerReady() {
        return (player.getActionsPoints() > 0) && (delay <= 0.0f);
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
