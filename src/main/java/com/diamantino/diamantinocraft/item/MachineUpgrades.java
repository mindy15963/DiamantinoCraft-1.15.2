package com.diamantino.diamantinocraft.item;

import com.diamantino.diamantinocraft.api.IMachineUpgrade;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import com.diamantino.diamantinocraft.util.Lazy;
import com.diamantino.diamantinocraft.util.Constants;

import java.util.Locale;

public enum MachineUpgrades implements IItemProvider, IMachineUpgrade {
    PROCESSING_SPEED(Constants.UPGRADE_PROCESSING_SPEED_AMOUNT, 0.5f),
    OUTPUT_CHANCE(Constants.UPGRADE_SECONDARY_OUTPUT_AMOUNT, 0.25f),
    ENERGY_CAPACITY(0, 0.0f, false),
    ENERGY_EFFICIENCY(Constants.UPGRADE_ENERGY_EFFICIENCY_AMOUNT, Constants.UPGRADE_ENERGY_EFFICIENCY_AMOUNT),
    RANGE(Constants.UPGRADE_RANGE_AMOUNT, 0.15f, false)
    ;

    private final Lazy<Item> item;
    private final float upgradeValue;
    private final float energyUsage;
    private final boolean displayValueAsPercentage;

    MachineUpgrades(float upgradeValue, float energyUsage) {
        this(upgradeValue, energyUsage, true);
    }

    MachineUpgrades(float upgradeValue, float energyUsage, boolean displayValueAsPercentage) {
        this.item = Lazy.of(() -> new MachineUpgradeItem(this));
        this.upgradeValue = upgradeValue;
        this.energyUsage = energyUsage;
        this.displayValueAsPercentage = displayValueAsPercentage;
    }

    @Override
    public float getEnergyUsageMultiplier() {
        return energyUsage;
    }

    @Override
    public float getUpgradeValue() {
        return upgradeValue;
    }

    @Override
    public boolean displayValueAsPercentage() {
        return displayValueAsPercentage;
    }

    public String getName() {
        return name().toLowerCase(Locale.ROOT) + "_upgrade";
    }

    @Override
    public Item asItem() {
        return item.get();
    }
}
