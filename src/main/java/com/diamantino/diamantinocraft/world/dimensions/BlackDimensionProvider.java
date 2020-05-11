package com.diamantino.diamantinocraft.world.dimensions;

import com.diamantino.diamantinocraft.init.ModBiomes;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;

import java.util.Random;
import java.util.Set;

public class BlackDimensionProvider extends BiomeProvider {

    private Random rand;

    public BlackDimensionProvider() {
        super(biomeList);
        rand = new Random();
    }

    private static final Set<Biome> biomeList = ImmutableSet.of(ModBiomes.blackBiome.get());

    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
        return ModBiomes.blackBiome.get();
    }
}
