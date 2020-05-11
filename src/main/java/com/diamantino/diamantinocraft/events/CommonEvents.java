package com.diamantino.diamantinocraft.events;

import com.diamantino.diamantinocraft.DiamantinoCraft;
import com.diamantino.diamantinocraft.item.BatteryItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.diamantino.diamantinocraft.capability.EnergyStorageImplBase;

@Mod.EventBusSubscriber(modid = DiamantinoCraft.MOD_ID)
public class CommonEvents {
    @SubscribeEvent
    public static void onAttachItemCaps(AttachCapabilitiesEvent<Item> event) {
        if (event.getObject() instanceof BatteryItem) {
            event.addCapability(DiamantinoCraft.getId("energy"), new EnergyStorageImplBase(500_000, 10_000, 10_000));
        }
    }
}
