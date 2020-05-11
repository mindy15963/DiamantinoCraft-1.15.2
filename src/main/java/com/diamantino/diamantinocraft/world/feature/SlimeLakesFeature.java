package com.diamantino.diamantinocraft.world.feature;

import com.diamantino.diamantinocraft.utils.MathUtils;
import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.BlockStateFeatureConfig;
import net.minecraft.world.gen.feature.LakesFeature;

import java.util.Random;
import java.util.function.Function;

public class SlimeLakesFeature extends LakesFeature {
    public static SlimeLakesFeature INSTANCE = new SlimeLakesFeature(BlockStateFeatureConfig::func_227271_a_);

    public SlimeLakesFeature(Function<Dynamic<?>, ? extends BlockStateFeatureConfig> p_i51485_1_) {
        super(p_i51485_1_);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, BlockStateFeatureConfig config) {
        // Occasionally allow surface lakes
        if (MathUtils.tryPercentage(0.1))
            return super.place(worldIn, generator, rand, pos, config);
        // Place around Y 20-40
        return super.place(worldIn, generator, rand, new BlockPos(pos.getX(), rand.nextInt(20) + 20, pos.getZ()), config);
    }
}
