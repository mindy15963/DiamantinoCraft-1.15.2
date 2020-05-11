package com.diamantino.diamantinocraft.item;

import com.diamantino.diamantinocraft.DiamantinoCraft;
import io.netty.util.internal.StringUtil;
import net.minecraft.item.Food;
import net.minecraft.item.Foods;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import com.diamantino.diamantinocraft.utils.Lazy;
import net.minecraft.util.StringUtils;
import org.apache.http.util.TextUtils;

import javax.annotation.Nullable;

public enum CraftingItems implements IItemProvider {
    // Mod metals
    REDSTONE_ALLOY_INGOT,
    REDSTONE_ALLOY_NUGGET,
    REDSTONE_ALLOY_DUST,
    REFINED_IRON_INGOT,
    COMPRESSED_IRON_INGOT,
    // Base metals
    IRON_CHUNKS,
    IRON_DUST,
    GOLD_CHUNKS,
    GOLD_DUST,
    COPPER_CHUNKS,
    COPPER_DUST,
    COPPER_INGOT,
    COPPER_NUGGET,
    TIN_CHUNKS,
    TIN_DUST,
    TIN_INGOT,
    TIN_NUGGET,
    SILVER_CHUNKS,
    SILVER_DUST,
    SILVER_INGOT,
    SILVER_NUGGET,
    LEAD_CHUNKS,
    LEAD_DUST,
    LEAD_INGOT,
    LEAD_NUGGET,
    NICKEL_CHUNKS,
    NICKEL_DUST,
    NICKEL_INGOT,
    NICKEL_NUGGET,
    PLATINUM_CHUNKS,
    PLATINUM_DUST,
    PLATINUM_INGOT,
    PLATINUM_NUGGET,
    ZINC_CHUNKS,
    ZINC_DUST,
    ZINC_INGOT,
    ZINC_NUGGET,
    BISMUTH_CHUNKS,
    BISMUTH_DUST,
    BISMUTH_INGOT,
    BISMUTH_NUGGET,
    BAUXITE_CHUNKS,
    ALUMINUM_DUST,
    ALUMINUM_INGOT,
    ALUMINUM_NUGGET,
    URANIUM_CHUNKS,
    URANIUM_DUST,
    URANIUM_INGOT,
    URANIUM_NUGGET,
    OSMIUM_INGOT,
    OSMIUM_CHUNKS,
    OSMIUM_DUST,
    OSMIUM_NUGGET,
    // Alloys
    BRONZE_DUST,
    BRONZE_INGOT,
    BRONZE_NUGGET,
    BRASS_DUST,
    BRASS_INGOT,
    BRASS_NUGGET,
    INVAR_DUST,
    INVAR_INGOT,
    INVAR_NUGGET,
    ELECTRUM_DUST,
    ELECTRUM_INGOT,
    ELECTRUM_NUGGET,
    STEEL_DUST,
    STEEL_INGOT,
    STEEL_NUGGET,
    BISMUTH_BRASS_DUST,
    BISMUTH_BRASS_INGOT,
    BISMUTH_BRASS_NUGGET,
    ALUMINUM_STEEL_DUST,
    ALUMINUM_STEEL_INGOT,
    ALUMINUM_STEEL_NUGGET,
    BISMUTH_STEEL_DUST,
    BISMUTH_STEEL_INGOT,
    BISMUTH_STEEL_NUGGET,
    SIGNALUM_DUST,
    SIGNALUM_INGOT,
    SIGNALUM_NUGGET,
    LUMIUM_DUST,
    LUMIUM_INGOT,
    LUMIUM_NUGGET,
    ENDERIUM_DUST,
    ENDERIUM_INGOT,
    ENDERIUM_NUGGET,
    // Other dusts
    COAL_DUST,
    // Crafting items
    CIRCUIT_BOARD,
    HEATING_ELEMENT,
    SOLDER,
    PLASTIC_PELLETS,
    PLASTIC_SHEET,
    UPGRADE_CASE,
    // Others
    ZOMBIE_LEATHER,
    BEEF_JERKY(Foods.COOKED_BEEF),
    PORK_JERKY(Foods.COOKED_PORKCHOP),
    CHICKEN_JERKY(Foods.COOKED_CHICKEN),
    MUTTON_JERKY(Foods.COOKED_MUTTON),
    RABBIT_JERKY(Foods.COOKED_RABBIT),
    COD_JERKY(Foods.COOKED_COD),
    SALMON_JERKY(Foods.SALMON),
    ;

    private final Lazy<Item> item;

    CraftingItems() {
        this(null);
    }

    CraftingItems(@Nullable Food food) {
        this.item = Lazy.of(() -> new Item(createProperties(food)));
    }

    private static Item.Properties createProperties(@Nullable Food food) {
        Item.Properties result = new Item.Properties().group(DiamantinoCraft.ITEM_GROUP);
        if (food != null) {
            result.food(food);
        }
        return result;
    }

    public String getName() {
        return (name().toLowerCase());
    }

    @Override
    public Item asItem() {
        return item.get();
    }
}
