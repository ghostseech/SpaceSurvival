package com.asdfgaems.core.objects;

import com.asdfgaems.core.GameObject;
import com.asdfgaems.core.Inventory;
import com.asdfgaems.core.TileMap;
import com.asdfgaems.core.World;
import com.asdfgaems.core.items.Armor;
import com.asdfgaems.core.items.Consumable;
import com.asdfgaems.core.items.Weapon;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;

import java.util.LinkedList;
import java.util.List;

public class Player extends GameObject implements LiveObject{
    public static Texture texture;

    public Inventory inventory;
    private Vector2 dir;
    private List<Vector2> path;

    private float satiety;
    private float health;

    private float timer;

    private Armor armor;
    private Weapon weapon;

    private float radiation;
    private int regenerateTimer;
    private boolean poisoned;
    private boolean bleed;



    public Player(World world, String name,int x, int y) {
        super(world, name);
        this.x = x;
        this.y = y;
        this.dir = new Vector2();
        collidable = true;
        path = new LinkedList<Vector2>();

        health = 100.0f;
        satiety = 100.0f;
        regenerateTimer = 0;
    }

    @Override
    public float processTurn() {
        needUpdate = false;
        satiety -= 1.0f;
        if(satiety <=0.0f) satiety = 0.0f;
        if(satiety >= 40.0f) health +=0.5f;
        else if(satiety <= 0.0f) health-=0.5f;
        if(regenerateTimer > 0) {
            regenerateTimer--;
            health += 100.0f/40.0f;
        }
        if(poisoned) health--;
        if(radiation >= 50.0f) health--;

        if(health < 0.0f) health = 0.0f;
        if(health > 100.0f) health = 100.0f;


        if(path.size() > 0) {
            //needUpdate = true;
            Vector2 cur = path.get(0);
            path.remove(0);
            dir.x = cur.x - x;
            dir.y = cur.y - y;
        }

        x += dir.x;
        y += dir.y;

        dir.set(0.0f, 0.0f);

        return 0.3f;
    }

    public void move(int dirX, int dirY) {
        dir.x = dirX;
        dir.y = dirY;
    }

    public void move(List<Vector2> path) {
        this.path = path;
        world.requestTurnProcess();
    }

    public void action(GameObject object, String objectName, float dist) {
        if(object.getClass() == Door.class && dist <= 1.5f) {
            Door door = (Door)object;
            if(door.isLocked()) {
                door.action(this);
            }
        }
        else if(object.getClass() == Chest.class && dist <= 1.5f) {
            Chest chest = (Chest)object;
            if(chest.getLevel() != 0) {
                chest.unlock(this);
            }
        }
        if(world.isVisibleFrom(x, y, object.x, object.y))
        {
            if(object.getClass() == Slime.class || object.getClass() == RadInsect.class) {
            world.requestTurnProcess();
            if(weapon == null) ((LiveObject)object).damage(10.0f, this);
            else {
                if(getDist(object) <= weapon.getRange()) weapon.attack(world, object.x, object.y);
            }
            }
        }
    }
    public void consume(Consumable consumable) {
        if(consumable.getType() == Consumable.FOOD_RATION_1) {
            addSatiety(80.0f);
        }
        else if(consumable.getType() == Consumable.HEALTH_1) {
            bleed = false;
            health += 10.0f;
            radiation -= 20.0f;
            poisoned = false;
            if(health >= 100.0f) health = 100.0f;
        }
        else if(consumable.getType() == Consumable.REGEN_MIXTURE) {
            regenerateTimer = 10;
        }
        else if(consumable.getType() == Consumable.ANTIRAD_MIXTURE) {
            radiation -=40.0f;
            if(radiation < 0.0f) radiation = 0.0f;
        }
        else if(consumable.getType() == Consumable.ANTITOXIC_MIXTURE) {
            poisoned = false;
        }
        else if(consumable.getType() == Consumable.ATTENTION_MIXTURE) {

        }
    }
    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture, x * TileMap.TILE_SIZE, y * TileMap.TILE_SIZE, TileMap.TILE_SIZE, TileMap.TILE_SIZE);
    }

    @Override
    public void update(float dt) {

    }

    public boolean isBleed() {
        return bleed;
    }
    public boolean isRadiated() {
        return radiation >= 50.0f;
    }
    public boolean isHpRegen() {
        return regenerateTimer > 0;
    }
    public boolean isToxic() {
        return poisoned;
    }

    public void equipArmor(Armor armor) {
        this.armor = armor;
    }
    public Armor getArmor() {
        return armor;
    }

    public void equipWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
    public Weapon getWeapon() {
        return weapon;
    }

    public void addSatiety(float val) {
        satiety += val;
        if(satiety > 100.0f) satiety = 100.0f;
    }
    public float getSatiety() {
        return satiety;
    }

    public void damage(float damage, LiveObject object) {
        health -= damage;
        if(armor != null) health += damage * armor.getPhysResist();

        if(health < 0.0f) health = 0.0f;
        if(health > 100.0f) health = 100.0f;
        if(object.getClass() == Slime.class) {
            poisoned = true;
        }
        else if(object.getClass() == RadInsect.class) radiation += 10.0f;
    }

    public void addRadiation(float rad) {
        if(armor != null) {
            if(rad > armor.getEnergyResist()) radiation += (rad - armor.getRadResist());
        }
        else radiation += rad;
    }

    public void toxicate() {
        if(MathUtils.randomBoolean(armor.getToxicResist())) poisoned = true;
    }

    public boolean isMoving() {
        return path.size() > 0;
    }

    public void setHp(float hp) {
        health = hp;
    }
    public float getHp() {
        return health;
    }

    public void dispose() {
        inventory.dispose();
    }

    @Override
    public void death() {

    }

}
