package com.diamantino.diamantinocraft.item;

import com.diamantino.diamantinocraft.DiamantinoCraft;
import net.minecraft.item.Rarity;

public class BatteryItem extends EnergyStoringItem {
    private static final int MAX_ENERGY = 500_000;
    private static final int MAX_TRANSFER = 500;

    public BatteryItem() {
        super(new Properties().group(DiamantinoCraft.ITEM_GROUP).maxStackSize(1).rarity(Rarity.UNCOMMON), MAX_ENERGY, MAX_TRANSFER);
    }
}
