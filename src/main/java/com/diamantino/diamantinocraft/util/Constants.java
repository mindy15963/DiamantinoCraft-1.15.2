package com.diamantino.diamantinocraft.util;

import com.diamantino.diamantinocraft.DiamantinoCraft;
import net.minecraft.util.ResourceLocation;

public final class Constants {
    public static final ResourceLocation ALLOY_SMELTING = DiamantinoCraft.getId("alloy_smelting");
    public static final ResourceLocation COMPRESSING = DiamantinoCraft.getId("compressing");
    public static final ResourceLocation CRUSHING = DiamantinoCraft.getId("crushing");
    public static final ResourceLocation DRYING = DiamantinoCraft.getId("drying");
    public static final ResourceLocation MIXING = DiamantinoCraft.getId("mixing");
    public static final ResourceLocation REFINING = DiamantinoCraft.getId("refining");
    public static final ResourceLocation SOLIDIFYING = DiamantinoCraft.getId("solidifying");

    // Machine upgrades
    public static final int UPGRADES_PER_SLOT = 1;
    public static final float UPGRADE_PROCESSING_SPEED_AMOUNT = 0.5f;
    public static final float UPGRADE_SECONDARY_OUTPUT_AMOUNT = 0.1f;
    public static final float UPGRADE_ENERGY_EFFICIENCY_AMOUNT = -0.15f;
    public static final int UPGRADE_RANGE_AMOUNT = 2;

    private Constants() {throw new IllegalAccessError("Utility class");}
}
