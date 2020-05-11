package com.diamantino.diamantinocraft.init;

import com.diamantino.diamantinocraft.DiamantinoCraft;
import com.diamantino.diamantinocraft.block.generator.coal.CoalGeneratorContainer;
import com.diamantino.diamantinocraft.block.generator.coal.CoalGeneratorScreen;
import com.diamantino.diamantinocraft.block.generator.diesel.DieselGeneratorContainer;
import com.diamantino.diamantinocraft.block.generator.diesel.DieselGeneratorScreen;
import com.diamantino.diamantinocraft.block.generator.lava.LavaGeneratorContainer;
import com.diamantino.diamantinocraft.block.generator.lava.LavaGeneratorScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import com.diamantino.diamantinocraft.block.alloysmelter.AlloySmelterScreen;
import com.diamantino.diamantinocraft.block.batterybox.BatteryBoxContainer;
import com.diamantino.diamantinocraft.block.batterybox.BatteryBoxScreen;
import com.diamantino.diamantinocraft.block.compressor.CompressorContainer;
import com.diamantino.diamantinocraft.block.compressor.CompressorScreen;
import com.diamantino.diamantinocraft.block.crusher.CrusherScreen;
import com.diamantino.diamantinocraft.block.electricfurnace.ElectricFurnaceContainer;
import com.diamantino.diamantinocraft.block.electricfurnace.ElectricFurnaceScreen;
import com.diamantino.diamantinocraft.block.mixer.MixerContainer;
import com.diamantino.diamantinocraft.block.mixer.MixerScreen;
import com.diamantino.diamantinocraft.block.pump.PumpContainer;
import com.diamantino.diamantinocraft.block.pump.PumpScreen;
import com.diamantino.diamantinocraft.block.refinery.RefineryContainer;
import com.diamantino.diamantinocraft.block.refinery.RefineryScreen;
import com.diamantino.diamantinocraft.block.solidifier.SolidifierContainer;
import com.diamantino.diamantinocraft.block.solidifier.SolidifierScreen;
import com.diamantino.diamantinocraft.util.MachineTier;

public final class ModContainers {
    public static ContainerType<BatteryBoxContainer> batteryBox;
    public static ContainerType<CoalGeneratorContainer> coalGenerator;
    public static ContainerType<CompressorContainer> compressor;
    public static ContainerType<DieselGeneratorContainer> dieselGenerator;
    public static ContainerType<ElectricFurnaceContainer> electricFurnace;
    public static ContainerType<LavaGeneratorContainer> lavaGenerator;
    public static ContainerType<MixerContainer> mixer;
    public static ContainerType<PumpContainer> pump;
    public static ContainerType<RefineryContainer> refinery;
    public static ContainerType<SolidifierContainer> solidifier;

    private ModContainers() {}

    public static void registerAll(RegistryEvent.Register<ContainerType<?>> event) {
        register("basic_alloy_smelter", MachineType.ALLOY_SMELTER.getContainerType(MachineTier.BASIC));
        register("alloy_smelter", MachineType.ALLOY_SMELTER.getContainerType(MachineTier.STANDARD));
        batteryBox = register("battery_box", BatteryBoxContainer::new);
        coalGenerator = register("coal_generator", CoalGeneratorContainer::new);
        compressor = register("compressor", CompressorContainer::new);
        register("basic_crusher", MachineType.CRUSHER.getContainerType(MachineTier.BASIC));
        register("crusher", MachineType.CRUSHER.getContainerType(MachineTier.STANDARD));
        dieselGenerator = register("diesel_generator", DieselGeneratorContainer::new);
        electricFurnace = register("electric_furnace", ElectricFurnaceContainer::new);
        lavaGenerator = register("lava_generator", LavaGeneratorContainer::new);
        mixer = register("mixer", MixerContainer::new);
        pump = register("pump", PumpContainer::new);
        refinery = register("refinery", RefineryContainer::new);
        solidifier = register("solidifier", SolidifierContainer::new);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerScreens(FMLClientSetupEvent event) {
        ScreenManager.registerFactory(MachineType.ALLOY_SMELTER.getContainerType(MachineTier.BASIC), AlloySmelterScreen::new);
        ScreenManager.registerFactory(MachineType.ALLOY_SMELTER.getContainerType(MachineTier.STANDARD), AlloySmelterScreen::new);
        ScreenManager.registerFactory(batteryBox, BatteryBoxScreen::new);
        ScreenManager.registerFactory(coalGenerator, CoalGeneratorScreen::new);
        ScreenManager.registerFactory(compressor, CompressorScreen::new);
        ScreenManager.registerFactory(MachineType.CRUSHER.getContainerType(MachineTier.BASIC), CrusherScreen::new);
        ScreenManager.registerFactory(MachineType.CRUSHER.getContainerType(MachineTier.STANDARD), CrusherScreen::new);
        ScreenManager.registerFactory(dieselGenerator, DieselGeneratorScreen::new);
        ScreenManager.registerFactory(electricFurnace, ElectricFurnaceScreen::new);
        ScreenManager.registerFactory(lavaGenerator, LavaGeneratorScreen::new);
        ScreenManager.registerFactory(mixer, MixerScreen::new);
        ScreenManager.registerFactory(pump, PumpScreen::new);
        ScreenManager.registerFactory(refinery, RefineryScreen::new);
        ScreenManager.registerFactory(solidifier, SolidifierScreen::new);
    }

    private static <C extends Container> ContainerType<C> register(String name, ContainerType.IFactory<C> containerFactory) {
        ContainerType<C> type = new ContainerType<>(containerFactory);
        return register(name, type);
    }

    private static <C extends Container> ContainerType<C> register(String name, ContainerType<C> containerType) {
        containerType.setRegistryName(DiamantinoCraft.getId(name));
        ForgeRegistries.CONTAINERS.register(containerType);
        return containerType;
    }
}
