/*
 * Silent Lib -- InitialSpawnItems
 * Copyright (C) 2018 SilentChaos512
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 3
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.diamantino.diamantinocraft.event;

import com.diamantino.diamantinocraft.DiamantinoCraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import com.diamantino.diamantinocraft.util.PlayerUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public final class InitialSpawnItems {
    private static final InitialSpawnItems INSTANCE = new InitialSpawnItems();
    private static final String NBT_KEY = DiamantinoCraft.MOD_ID + ".SpawnItemsGiven";

    private final Map<ResourceLocation, Function<PlayerEntity, Collection<ItemStack>>> spawnItems = new HashMap<>();

    private InitialSpawnItems() {
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerLoggedIn);
    }

    /**
     * Add a spawn item. If the supplier returns an empty stack ({@link ItemStack#EMPTY}), nothing
     * is given. If the item is given, the player will never receive a spawn item with the same
     * {@code key} again. If no item is given, the key will not be marked as given.
     *
     * @param key   The key to uniquely identify the spawn item
     * @param stack The {@link ItemStack} supplier. If it returns an empty stack, nothing is given.
     * @deprecated Use {@link #add(ResourceLocation, Function)} instead
     */
    @Deprecated
    public static void add(ResourceLocation key, Supplier<ItemStack> stack) {
        INSTANCE.spawnItems.put(key, p -> {
            ItemStack s = stack.get();
            return s.isEmpty() ? Collections.emptyList() : Collections.singleton(s);
        });
    }

    /**
     * Add a spawn item. If the supplier returns an empty stack ({@link ItemStack#EMPTY}), nothing
     * is given. If the item is given, the player will never receive a spawn item with the same
     * {@code key} again. If no item is given, the key will not be marked as given.
     *
     * @param key         The key to uniquely identify the spawn item
     * @param itemFactory The item stack producer. Should not contain empty stacks.
     */
    public static void add(ResourceLocation key, Function<PlayerEntity, Collection<ItemStack>> itemFactory) {
        INSTANCE.spawnItems.put(key, itemFactory);
    }

    private void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        CompoundNBT givenItems = PlayerUtils.getPersistedDataSubcompound(player, NBT_KEY);

        spawnItems.forEach((key, factory) -> handleSpawnItems(player, givenItems, key, factory.apply(player)));
    }

    private static void handleSpawnItems(PlayerEntity player, CompoundNBT givenItems, ResourceLocation key, Collection<ItemStack> items) {
        if (items.isEmpty()) return;

        String nbtKey = key.toString().replace(':', '.');
        if (!givenItems.getBoolean(nbtKey)) {
            items.forEach(stack -> {
                DiamantinoCraft.LOGGER.debug("Giving player {} spawn item \"{}\": {}", player.getScoreboardName(), nbtKey, stack);
                PlayerUtils.giveItem(player, stack);
                givenItems.putBoolean(nbtKey, true);
            });
        }
    }
}
