package com.diamantino.diamantinocraft;

import com.diamantino.diamantinocraft.config.Config;
import com.diamantino.diamantinocraft.init.ModBiomes;
import com.diamantino.diamantinocraft.init.ModDimensions;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import com.diamantino.diamantinocraft.init.ModBlocks;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.Random;

@Mod(DiamantinoCraft.MOD_ID)
@Mod.EventBusSubscriber(modid = DiamantinoCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DiamantinoCraft {
    public static final String MOD_ID = "diamantinocraft";
    public static final String MOD_NAME = "DiamantinoCraft";

    public static final ResourceLocation blackDimType = new ResourceLocation(MOD_ID, "black_dimension");

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    public static final Random RANDOM = new Random();

    public static DimensionType type;

    public static DiamantinoCraft INSTANCE;
    public static IProxy PROXY;

    public static ItemGroup ITEM_GROUP = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.crusher);
        }
    };

    public DiamantinoCraft() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        INSTANCE = this;

        PROXY = DistExecutor.runForDist(() -> () -> new SideProxy.Client(), () -> () -> new SideProxy.Server());

        ModDimensions.MOD_DIMENSIONS.register(modEventBus);
        ModBiomes.BIOMES.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public static void onRegisterBiomes (final RegistryEvent.Register<Biome> event) {
        ModBiomes.registerBiomes();
    }

    public static String getVersion() {
        Optional<? extends ModContainer> o = ModList.get().getModContainerById(MOD_ID);
        if (o.isPresent()) {
            return o.get().getModInfo().getVersion().toString();
        }
        return "NONE";
    }

    public static boolean isDevBuild() {
        return "NONE".equals(getVersion());
    }


    public static ResourceLocation getId(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static DimensionType getMiningDimension() {
        type = DimensionType.byName(blackDimType);
        /*if (type == null) {
            LOGGER.warn("Could not find mining dimension");
            LOGGER.warn("This world may be created without the mining dimension mod installed");
            LOGGER.info("Registering mining dimension");
            type = addDimensionType();
        }*/
        return type;
    }


    public static DimensionType getOverworldDimension() { return DimensionType.byName(new ResourceLocation((String)Config.OVERWORLD_DIMENSION.get())); }
}
