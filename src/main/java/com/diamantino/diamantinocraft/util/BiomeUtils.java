package com.diamantino.diamantinocraft.util;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BiomeUtils {
    private static final Pattern CATEGORY_PATTERN = Pattern.compile("#", Pattern.LITERAL);

    private BiomeUtils() {}

    public static boolean matches(Biome biome, String input) {
        if (biome == from(input))
            return true;
        String category = CATEGORY_PATTERN.matcher(input).replaceFirst(Matcher.quoteReplacement(""));
        return biome.getCategory().name().equalsIgnoreCase(category);
    }

    public static boolean containedInList(Biome biome, Collection<? extends String> list, boolean valueIfListEmpty) {
        if (list.isEmpty()) return valueIfListEmpty;

        for (String str : list) {
            if (matches(biome, str)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static Biome from(String str) {
        ResourceLocation id = ResourceLocation.tryCreate(str);
        if (id != null) {
            return ForgeRegistries.BIOMES.getValue(id);
        }
        return null;
    }
}
