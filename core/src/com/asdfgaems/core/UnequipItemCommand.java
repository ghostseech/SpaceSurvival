package com.asdfgaems.core;

import com.asdfgaems.core.items.Armor;
import com.asdfgaems.core.items.Item;
import com.asdfgaems.core.objects.Player;

public class UnequipItemCommand implements Command {
    private Player player;
    private Item item;

    public UnequipItemCommand(Player player, Item item) {
        this.player = player;
        this.item = item;
    }
    public void execute() {
        if(item == player.getArmor()) player.equipArmor(null);
    }
}
