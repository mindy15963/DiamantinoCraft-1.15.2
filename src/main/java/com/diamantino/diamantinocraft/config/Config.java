package com.diamantino.diamantinocraft.config;

import com.diamantino.diamantinocraft.init.Ores;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;
import com.diamantino.diamantinocraft.utils.config.BooleanValue;
import com.diamantino.diamantinocraft.utils.config.ConfigSpecWrapper;
import com.diamantino.diamantinocraft.utils.config.IntValue;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public final class Config {
    private static final ConfigSpecWrapper WRAPPER = ConfigSpecWrapper.create(FMLPaths.CONFIGDIR.get().resolve("diamantinocraft-common.toml"));

    public static final Common COMMON = new Common(WRAPPER);

    public static ForgeConfigSpec.ConfigValue<String> OVERWORLD_DIMENSION;
    public static final ServerConfig SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    public static Pair specPairServer;

    public static class Common {
        public final BooleanValue showBetaWelcomeMessage;
        public final IntValue worldGenOilLakeChance;
        public final IntValue worldGenSlimeLakeChance;
        private final BooleanValue oreWorldGenMasterSwitch;
        private final Map<Ores, OreConfig> oreConfigs = new EnumMap<>(Ores.class);

        public final IntValue fluidGeneratorInjectionVolume;

        public Common(ConfigSpecWrapper wrapper) {
            showBetaWelcomeMessage = wrapper
                    .builder("general.showBetaWelcomeMessage")
                    .comment("Shows a message in chat warning the player that the mod is early in development")
                    .define(true);

            wrapper.comment("world", "All world generation settings require you to restart Minecraft!");

            worldGenOilLakeChance = wrapper
                    .builder("world.oilLake.chance")
                    .comment("Chance of oil lakes spawning (1 in chance). Higher numbers = less common. Set 0 to disable.",
                            "Water is 4, lava is 80. Oil lakes will spawn underground about 90% of the time.",
                            "Note that disabling oil will make some items uncraftable unless recipes are changed")
                    .defineInRange(6, 0, Integer.MAX_VALUE);

            worldGenSlimeLakeChance = wrapper
                    .builder("world.slimeLake.chance")
                    .comment("Chance of slime lakes spawning (1 in chance). Higher numbers = less common. Set 0 to disable.",
                            "Water is 4, lava is 80. Slime lakes will spawn underground about 90% of the time.")
                    .defineInRange(15, 0, Integer.MAX_VALUE);

            oreWorldGenMasterSwitch = wrapper
                    .builder("world.masterSwitch")
                    .comment("Set to 'false' to completely disable ore generation from this mod, ignoring all other settings.",
                            "You can also enable/disable ores individually, but this is useful if you plan to use another mod for ore generation.")
                    .define(true);

            Arrays.stream(Ores.values()).forEach(ore -> oreConfigs.put(ore, new OreConfig(ore, wrapper, oreWorldGenMasterSwitch)));

            fluidGeneratorInjectionVolume = wrapper
                    .builder("machine.fluidGenerators.injectionVolume")
                    .comment("The amount of fluid (in milliBuckets, or mB) fluid generators consume at once.",
                            "Lower values reduce waste, but may cause lag as the generator more frequently turns on/off.",
                            "A generator with less fluid in the tank will not be able to run.")
                    .defineInRange(100, 1, 1000);
        }

        public Optional<OreConfig> getOreConfig(Ores ore) {
            if (oreConfigs.containsKey(ore))
                return Optional.of(oreConfigs.get(ore));
            return Optional.empty();
        }
    }

    static {
        specPairServer = (new ForgeConfigSpec.Builder()).configure(ServerConfig::new);
        SERVER_SPEC = (ForgeConfigSpec) specPairServer.getRight();
        SERVER = (ServerConfig) specPairServer.getLeft();
    }

    public static class ServerConfig {
        public ServerConfig(ForgeConfigSpec.Builder builder) {
            Config.OVERWORLD_DIMENSION = builder.comment("The dimension from where you can teleport to the mining dimension and back").define("overworld_dimension", "minecraft:overworld");
        }
    }

    private Config() {
    }

    public static void init() {
        WRAPPER.validate();
        WRAPPER.validate();
    }
}
