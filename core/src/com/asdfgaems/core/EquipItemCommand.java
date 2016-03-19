package com.asdfgaems.core;

import com.asdfgaems.core.items.Armor;
import com.asdfgaems.core.items.Item;
import com.asdfgaems.core.objects.Player;

public class EquipItemCommand implements Command {
    private Player player;
    private Item item;

    public EquipItemCommand(Player player, Item item) {
        this.player = player;
        this.item = item;
    }
    public void execute() {
        if(item.getClass() == Armor.class) player.equipArmor((Armor)item);
    }
}
