package com.diamantino.diamantinocraft;

import com.diamantino.diamantinocraft.init.*;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import com.diamantino.diamantinocraft.event.Greetings;
import com.diamantino.diamantinocraft.util.generator.ModelGenerator;
import com.diamantino.diamantinocraft.config.Config;
import com.diamantino.diamantinocraft.item.CraftingItems;
import com.diamantino.diamantinocraft.network.Network;
import com.diamantino.diamantinocraft.world.SMWorldFeatures;

import javax.annotation.Nullable;
import java.util.Arrays;

class SideProxy implements IProxy {
    private MinecraftServer server = null;

    SideProxy() {
        // Add listeners for common events
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imcEnqueue);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::imcProcess);

        // Add listeners for registry events
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, ModBlocks::registerAll);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ContainerType.class, ModContainers::registerAll);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Fluid.class, ModFluids::registerFluids);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, ModItems::registerAll);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class, ModTileEntities::registerAll);

        // Other events
        MinecraftForge.EVENT_BUS.addListener(this::serverAboutToStart);

        Config.init();
        Network.init();

        ModRecipes.init();

        Greetings.addMessage(SideProxy::getBetaWelcomeMessage);
        Greetings.addMessage(ModBlocks::checkForMissingLootTables);
    }

    @Nullable
    private static ITextComponent getBetaWelcomeMessage(PlayerEntity p) {
        return Config.COMMON.showBetaWelcomeMessage.get()
                //? new StringTextComponent("Thanks for trying DiamantinoCraft! This mod is early in development, expect bugs and changes.")
                ? new StringTextComponent("")
                : null;
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(SMWorldFeatures::addFeaturesToBiomes);

        /*if (ModList.get().isLoaded("computercraft")) {
            SMechComputerCraftCompat.init();
        }*/

        if (DiamantinoCraft.isDevBuild()) {
            Arrays.stream(CraftingItems.values()).forEach(c -> ModelGenerator.create(c.asItem()));
            Arrays.stream(Metals.values()).forEach(m -> ModelGenerator.create(m.asBlock()));
        }
    }

    private void imcEnqueue(InterModEnqueueEvent event) {
    }

    private void imcProcess(InterModProcessEvent event) {
    }

    private void serverAboutToStart(FMLServerAboutToStartEvent event) {
        server = event.getServer();
    }

    @Override
    public MinecraftServer getServer() {
        return server;
    }

    static class Client extends SideProxy {
        Client() {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(ModItems::registerItemColors);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        }

        private void clientSetup(FMLClientSetupEvent event) {
            ModBlocks.registerRenderTypes(event);
            ModContainers.registerScreens(event);
            ModTileEntities.registerRenderers(event);
        }
    }

    static class Server extends SideProxy {
        Server() {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverSetup);
        }

        private void serverSetup(FMLDedicatedServerSetupEvent event) {
        }
    }
}
