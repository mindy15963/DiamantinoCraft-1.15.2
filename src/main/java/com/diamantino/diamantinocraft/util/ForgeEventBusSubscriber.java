package com.diamantino.diamantinocraft.util;

import com.diamantino.diamantinocraft.DiamantinoCraft;
import com.diamantino.diamantinocraft.init.ModDimensions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DiamantinoCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventBusSubscriber {

    @SubscribeEvent
    public static void registerDimension(final RegisterDimensionsEvent event) {
        if(DimensionType.byName(DiamantinoCraft.blackDimType) == null) {
            DimensionManager.registerDimension(DiamantinoCraft.blackDimType, ModDimensions.blackDimension.get(), null, true);
        }
    }

}
