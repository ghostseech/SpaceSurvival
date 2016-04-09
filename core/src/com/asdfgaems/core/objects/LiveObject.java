package com.asdfgaems.core.objects;

public interface LiveObject {
    public void damage(float damage, LiveObject object);

    public void setHp(float hp);
    public float getHp();
    public void death();
}
