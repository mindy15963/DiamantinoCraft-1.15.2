package com.diamantino.diamantinocraft.init;

import com.diamantino.diamantinocraft.DiamantinoCraft;
import com.diamantino.diamantinocraft.world.dimensions.BlackDimension;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModDimensions {

    public static final DeferredRegister<ModDimension> MOD_DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, DiamantinoCraft.MOD_ID);

    public static final RegistryObject<ModDimension> blackDimension = MOD_DIMENSIONS.register("blackdim", () -> new BlackDimension());

}
