package com.diamantino.diamantinocraft.block.wire;

import com.diamantino.diamantinocraft.DiamantinoCraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = DiamantinoCraft.MOD_ID)
public final class WireNetworkManager {
    private static final Collection<LazyOptional<WireNetwork>> NETWORK_LIST = Collections.synchronizedList(new ArrayList<>());

    private WireNetworkManager() {throw new IllegalAccessError("Utility class");}

    @SuppressWarnings("ConstantConditions")
    @Nullable
    public static WireNetwork get(IWorldReader world, BlockPos pos) {
        return getLazy(world, pos).orElse(null);
    }

    public static LazyOptional<WireNetwork> getLazy(IWorldReader world, BlockPos pos) {
        synchronized (NETWORK_LIST) {
            for (LazyOptional<WireNetwork> network : NETWORK_LIST) {
                if (network.isPresent()) {
                    WireNetwork net = network.orElseThrow(IllegalStateException::new);
                    if (net.contains(world, pos)) {
//                    SilentMechanisms.LOGGER.debug("get network {}", network);
                        return network;
                    }
                }
            }
        }

        // Create new
        WireNetwork network = WireNetwork.buildNetwork(world, pos);
        LazyOptional<WireNetwork> lazy = LazyOptional.of(() -> network);
        NETWORK_LIST.add(lazy);
        DiamantinoCraft.LOGGER.debug("create network {}", network);
        return lazy;
    }

    public static void invalidateNetwork(IWorldReader world, BlockPos pos) {
        Collection<LazyOptional<WireNetwork>> toRemove = NETWORK_LIST.stream()
                .filter(n -> n != null && n.isPresent() && n.orElseThrow(IllegalStateException::new).contains(world, pos))
                .collect(Collectors.toList());
        toRemove.forEach(WireNetworkManager::invalidateNetwork);
    }

    private static void invalidateNetwork(LazyOptional<WireNetwork> network) {
        DiamantinoCraft.LOGGER.debug("invalidateNetwork {}", network);
        NETWORK_LIST.removeIf(n -> n.isPresent() && n.equals(network));
        network.ifPresent(WireNetwork::invalidate);
        network.invalidate();
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        // Send energy from wire networks to connected blocks
        NETWORK_LIST.stream()
                .filter(n -> n != null && n.isPresent())
                .forEach(n -> n.ifPresent(WireNetwork::sendEnergy));
    }
}
