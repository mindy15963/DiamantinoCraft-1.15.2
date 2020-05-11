package com.diamantino.diamantinocraft.init;

import com.diamantino.diamantinocraft.DiamantinoCraft;
import com.diamantino.diamantinocraft.block.generator.coal.CoalGeneratorTileEntity;
import com.diamantino.diamantinocraft.block.generator.diesel.DieselGeneratorTileEntity;
import com.diamantino.diamantinocraft.block.generator.lava.LavaGeneratorTileEntity;
import com.diamantino.diamantinocraft.block.teleporter.TeleporterTileEntity;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import com.diamantino.diamantinocraft.block.batterybox.BatteryBoxTileEntity;
import com.diamantino.diamantinocraft.block.compressor.CompressorTileEntity;
import com.diamantino.diamantinocraft.block.dryingrack.DryingRackBlock;
import com.diamantino.diamantinocraft.block.dryingrack.DryingRackTileEntity;
import com.diamantino.diamantinocraft.block.dryingrack.DryingRackTileEntityRenderer;
import com.diamantino.diamantinocraft.block.electricfurnace.ElectricFurnaceTileEntity;
import com.diamantino.diamantinocraft.block.mixer.MixerTileEntity;
import com.diamantino.diamantinocraft.block.pump.PumpTileEntity;
import com.diamantino.diamantinocraft.block.refinery.RefineryTileEntity;
import com.diamantino.diamantinocraft.block.solidifier.SolidifierTileEntity;
import com.diamantino.diamantinocraft.block.wire.WireTileEntity;

import java.util.function.Supplier;

public final class ModTileEntities {
    public static TileEntityType<BatteryBoxTileEntity> batteryBox;
    public static TileEntityType<CoalGeneratorTileEntity> coalGenerator;
    public static TileEntityType<CompressorTileEntity> compressor;
    public static TileEntityType<DieselGeneratorTileEntity> dieselGenerator;
    public static TileEntityType<DryingRackTileEntity> dryingRack;
    public static TileEntityType<ElectricFurnaceTileEntity> electricFurnace;
    public static TileEntityType<LavaGeneratorTileEntity> lavaGenerator;
    public static TileEntityType<MixerTileEntity> mixer;
    public static TileEntityType<PumpTileEntity> pump;
    public static TileEntityType<RefineryTileEntity> refinery;
    public static TileEntityType<SolidifierTileEntity> solidifier;
    public static TileEntityType<WireTileEntity> wire;
    public static TileEntityType<TeleporterTileEntity> teleporter;

    private ModTileEntities() {}

    public static void registerAll(RegistryEvent.Register<TileEntityType<?>> event) {
        register("basic_alloy_smelter", MachineType.ALLOY_SMELTER.getBasicTileEntityType());
        register("alloy_smelter", MachineType.ALLOY_SMELTER.getStandardTileEntityType());
        register("basic_crusher", MachineType.CRUSHER.getBasicTileEntityType());
        register("crusher", MachineType.CRUSHER.getStandardTileEntityType());
        batteryBox = register("battery_box", BatteryBoxTileEntity::new, ModBlocks.batteryBox);
        coalGenerator = register("coal_generator", CoalGeneratorTileEntity::new, ModBlocks.coalGenerator);
        compressor = register("compressor", CompressorTileEntity::new, ModBlocks.compressor);
        dieselGenerator = register("diesel_generator", DieselGeneratorTileEntity::new, ModBlocks.dieselGenerator);
        dryingRack = register("drying_rack", DryingRackTileEntity::new, ModBlocks.DRYING_RACKS.toArray(new DryingRackBlock[0]));
        electricFurnace = register("electric_furnace", ElectricFurnaceTileEntity::new, ModBlocks.electricFurnace);
        lavaGenerator = register("lava_generator", LavaGeneratorTileEntity::new, ModBlocks.lavaGenerator);
        mixer = register("mixer", MixerTileEntity::new, ModBlocks.mixer);
        pump = register("pump", PumpTileEntity::new, ModBlocks.pump);
        refinery = register("refinery", RefineryTileEntity::new, ModBlocks.refinery);
        solidifier = register("solidifier", SolidifierTileEntity::new, ModBlocks.solidifier);
        wire = register("wire", WireTileEntity::new, ModBlocks.wire);
        teleporter = register("teleporter", TeleporterTileEntity::new, ModBlocks.teleporter);
    }

    private static <T extends TileEntity> TileEntityType<T> register(String name, Supplier<T> tileFactory, Block... blocks) {
        TileEntityType<T> type = TileEntityType.Builder.create(tileFactory, blocks).build(null);
        return register(name, type);
    }

    private static <T extends TileEntity> TileEntityType<T> register(String name, TileEntityType<T> type) {
        if (type.getRegistryName() == null) {
            type.setRegistryName(DiamantinoCraft.getId(name));
        }
        ForgeRegistries.TILE_ENTITIES.register(type);
        return type;
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(dryingRack, DryingRackTileEntityRenderer::new);
    }
}
