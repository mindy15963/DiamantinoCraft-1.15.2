package com.diamantino.diamantinocraft.init;

import com.diamantino.diamantinocraft.DiamantinoCraft;
import com.diamantino.diamantinocraft.item.*;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import com.diamantino.diamantinocraft.item.*;
import com.diamantino.diamantinocraft.util.color.ColorGetter;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class ModItems {
    public static BucketItem oilBucket;
    public static BucketItem dieselBucket;
    public static BucketItem slimeBucket;
    public static NoPlaceBucketItem ethaneBucket;
    public static NoPlaceBucketItem polyethyleneBucket;
    public static BatteryItem battery;
    public static HandPumpItem handPump;
    public static CanisterItem canister = new CanisterItem();

    static final Map<String, BlockItem> BLOCKS_TO_REGISTER = new LinkedHashMap<>();

    private ModItems() {}

    public static void registerAll(RegistryEvent.Register<Item> event) {
        BLOCKS_TO_REGISTER.forEach(ModItems::register);

        oilBucket = register("oil_bucket", createBucketItem(() -> ModFluids.OIL));
        dieselBucket = register("diesel_bucket", createBucketItem(() -> ModFluids.DIESEL));
        slimeBucket = register("slime_bucket", createBucketItem(() -> ModFluids.SLIME));
        ethaneBucket = register("ethane_bucket", createNoPlaceBucketItem(() -> ModFluids.ETHANE));
        polyethyleneBucket = register("polyethylene_bucket", createNoPlaceBucketItem(() -> ModFluids.POLYETHYLENE));

        battery = register("battery", new BatteryItem());
        handPump = register("hand_pump", new HandPumpItem());
        register("canister", canister);

        Arrays.stream(CraftingItems.values()).forEach(c -> register(c.getName(), c.asItem()));

        Arrays.stream(MachineUpgrades.values()).forEach(u -> register(u.getName(), u.asItem()));

        register("wrench", new WrenchItem());
        register("debug_item", new DebugItem());
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerItemColors(ColorHandlerEvent.Item event) {
        event.getItemColors().register((stack, tintIndex) -> {
            if (tintIndex == 1) {
                return ColorGetter.getColor(canister.getFluid(stack).getFluid());
            }
            return 0xFFFFFF;
        }, canister);
    }

    private static BucketItem createBucketItem(Supplier<FlowingFluid> fluid) {
        return new BucketItem(fluid, new Item.Properties().group(DiamantinoCraft.ITEM_GROUP).maxStackSize(1).containerItem(Items.BUCKET));
    }

    private static NoPlaceBucketItem createNoPlaceBucketItem(Supplier<Fluid> fluid) {
        return new NoPlaceBucketItem(fluid, new Item.Properties().group(DiamantinoCraft.ITEM_GROUP).maxStackSize(1).containerItem(Items.BUCKET));
    }

    private static <T extends Item> T register(String name, T item) {
        ResourceLocation id = DiamantinoCraft.getId(name);
        item.setRegistryName(id);
        ForgeRegistries.ITEMS.register(item);
        return item;
    }
}
