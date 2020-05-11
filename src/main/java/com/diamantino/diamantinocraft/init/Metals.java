package com.diamantino.diamantinocraft.init;

import net.minecraft.block.Block;
import com.diamantino.diamantinocraft.block.IBlockProvider;
import com.diamantino.diamantinocraft.block.MetalBlock;
import com.diamantino.diamantinocraft.utils.Lazy;

import java.util.Locale;

/**
 * Holds metal storage blocks. Could possibly store items like ingots and nuggets, but not really
 * necessary right now.
 */
public enum Metals implements IBlockProvider {
    COPPER,
    TIN,
    SILVER,
    LEAD,
    NICKEL,
    PLATINUM,
    ZINC,
    BISMUTH,
    ALUMINUM,
    URANIUM,
    BRONZE,
    BRASS,
    INVAR,
    ELECTRUM,
    STEEL,
    BISMUTH_BRASS,
    ALUMINUM_STEEL,
    BISMUTH_STEEL,
    SIGNALUM,
    LUMIUM,
    ENDERIUM,
    OSMIUM,
    ;

    private final Lazy<Block> block;

    Metals() {
        this.block = Lazy.of(MetalBlock::new);
    }

    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    @Override
    public Block asBlock() {
        return block.get();
    }
}
