package com.asdfgaems.core.items;

import com.asdfgaems.core.GameObject;
import com.asdfgaems.core.World;
import com.asdfgaems.core.objects.LiveObject;
import com.asdfgaems.core.objects.Slime;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Weapon extends Item{
    public static final int WEAPON_KNIFE = 1;
    public static final int WEAPON_SECURITY_GUN = 2;
    public static final int WEAPON_ARMY_RIFLE = 3;
    public static final int WEAPON_RAPIDGUN = 4;
    public static final int WEAPON_SHOTGUN = 5;
    public static final int WEAPON_PLASMAGUN = 6;

    public static Texture texture_knife;
    public static Texture texture_security_gun;
    public static Texture texture_army_rifle;
    public static Texture texture_rapidgun;
    public static Texture texture_shotgun;
    public static Texture texture_plasmagun;

    private int type;

    private float damage;
    private float range;


    public Weapon(int type) {
        this.type = type;

        if(type == WEAPON_KNIFE) {
            damage = 30.0f;
            range = 1.5f;
        }
        else if(type == WEAPON_SECURITY_GUN) {
            damage = 50.0f;
            range = 14.0f;
        }
        else if(type == WEAPON_ARMY_RIFLE) {
            damage = 120.0f;
            range = 14.0f;
        }
        else if(type == WEAPON_RAPIDGUN) {
            damage = 80.0f;
            range = 14.0f;
        }
        else if(type == WEAPON_SHOTGUN) {
            damage = 100.0f;
            range = 14.0f;
        }
        else if(type == WEAPON_PLASMAGUN) {
            damage = 200.0f;
            range = 14.0f;
        }

    }
    public void draw(SpriteBatch batch, float x, float y, float size) {

    }

    public Texture getTexture() {
        if(type == WEAPON_KNIFE) return texture_knife;
        if(type == WEAPON_SECURITY_GUN) return texture_security_gun;
        if(type == WEAPON_ARMY_RIFLE) return texture_army_rifle;
        if(type == WEAPON_RAPIDGUN) return texture_rapidgun;
        if(type == WEAPON_SHOTGUN) return texture_shotgun;
        if(type == WEAPON_PLASMAGUN) return texture_plasmagun;

        return texture_knife;
    }
    public boolean equals(Item item) {
        if(item.getClass() != Weapon.class) return false;

        if(((Weapon)item).type == type) return true;
        else return false;
    }
    public String getName() {
        if(type == WEAPON_KNIFE) return "Knife";
        if(type == WEAPON_SECURITY_GUN) return "Security gun";
        if(type == WEAPON_ARMY_RIFLE) return "Army rifle";
        if(type == WEAPON_RAPIDGUN) return "Rapidgun";
        if(type == WEAPON_SHOTGUN) return "Shotgun";
        if(type == WEAPON_PLASMAGUN) return "Plasmagun";
        return "";
    }
    public String getDescription() {
        if(type == WEAPON_KNIFE) return "Simple weapon";
        if(type == WEAPON_SECURITY_GUN) return "Station security weapon";
        if(type == WEAPON_ARMY_RIFLE) return "Effective army weapon";
        if(type == WEAPON_RAPIDGUN) return "Good weapon on short distance";
        if(type == WEAPON_SHOTGUN) return "Army weapon";
        if(type == WEAPON_PLASMAGUN) return "Experimental weapon";
        return "";
    }
    public void attack(World world, int x, int y) {
        if(type == WEAPON_KNIFE) {
            GameObject object = world.getEnemy(x, y);
            ((LiveObject)object).damage(damage, null);
        }
        else if(type == WEAPON_SECURITY_GUN) {
            GameObject object = world.getEnemy(x, y);
            ((LiveObject)object).damage(damage, null);
        }
        else if(type == WEAPON_ARMY_RIFLE) {
            GameObject object = world.getEnemy(x, y);
            ((LiveObject)object).damage(damage, null);
        }
        else if(type == WEAPON_RAPIDGUN) {
            GameObject object = world.getEnemy(x, y);
            ((LiveObject)object).damage(damage, null);
        }
        else if(type == WEAPON_SHOTGUN) {
            GameObject object = world.getEnemy(x, y);
            ((LiveObject)object).damage(damage, null);
        }
        else if(type == WEAPON_PLASMAGUN) {
            GameObject object = world.getEnemy(x, y);
            ((LiveObject)object).damage(damage, null);
        }
    }

    public float getRange() {
        return range;
    }
    public float getDamage() {
        return damage;
    }
}
