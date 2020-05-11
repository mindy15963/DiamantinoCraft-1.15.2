package com.diamantino.diamantinocraft.world.biomes;

import com.diamantino.diamantinocraft.init.ModBlocks;
import com.diamantino.diamantinocraft.utils.Color;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class BlackBiome extends Biome {

    static final ConfiguredSurfaceBuilder SURFACE_BUILDER = new ConfiguredSurfaceBuilder<>(SurfaceBuilder.DEFAULT, new SurfaceBuilderConfig(ModBlocks.blackDirt.getDefaultState(), ModBlocks.blackStone.getDefaultState(), ModBlocks.blackSand.getDefaultState()));
    static final RainType PRECIPATATION = RainType.NONE;
    static final Category CATEGORY = Category.EXTREME_HILLS;
    static final double DEPTH = -0.35F;
    static final double SCALE = 0.00F;
    static final float TEMPERATURE = 3F;
    static final float DOWNFALL = 0.0F;
    static final int WATER_COLOR = Color.BLACK.getColor();
    static final int WATER_FOG_COLOR = Color.BLACK.getColor();
    static final String PARENT = null;

    public BlackBiome() {

        super(new Builder().surfaceBuilder(SURFACE_BUILDER).precipitation(PRECIPATATION).category(CATEGORY).depth((float) DEPTH).waterColor(WATER_COLOR).scale((float) SCALE).temperature(TEMPERATURE).downfall(DOWNFALL).waterFogColor(WATER_FOG_COLOR).parent(PARENT));

    }

}
