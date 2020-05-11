package com.diamantino.diamantinocraft.block.crusher;

import com.diamantino.diamantinocraft.block.AbstractMachineTileEntity;
import com.diamantino.diamantinocraft.crafting.recipe.CrushingRecipe;
import com.diamantino.diamantinocraft.init.MachineType;
import com.diamantino.diamantinocraft.util.MachineTier;
import com.diamantino.diamantinocraft.util.TextUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.stream.IntStream;

public class CrusherTileEntity extends AbstractMachineTileEntity<CrushingRecipe> {
    // Energy constant
    private static final int MAX_ENERGY = 50_000;
    private static final int MAX_RECEIVE = 500;
    private static final int ENERGY_USED_PER_TICK = 30;

    // Inventory constants
    private static final int INPUT_SLOT_COUNT = 1;
    private static final int OUTPUT_SLOT_COUNT = 4;
    private static final int INVENTORY_SIZE = INPUT_SLOT_COUNT + OUTPUT_SLOT_COUNT;
    private static final int[] SLOTS_INPUT = {0};
    private static final int[] SLOTS_OUTPUT = IntStream.range(INPUT_SLOT_COUNT, INVENTORY_SIZE).toArray();
    private static final int[] SLOTS_ALL = IntStream.range(0, INVENTORY_SIZE).toArray();

    public CrusherTileEntity() {
        this(MachineTier.STANDARD);
    }

    public CrusherTileEntity(MachineTier tier) {
        super(MachineType.CRUSHER.getTileEntityType(tier), INVENTORY_SIZE, tier);
    }

    @Override
    protected int getEnergyUsedPerTick() {
        return ENERGY_USED_PER_TICK;
    }

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    @Override
    protected int[] getOutputSlots() {
        return SLOTS_OUTPUT;
    }

    @Nullable
    @Override
    protected CrushingRecipe getRecipe() {
        if (world == null) return null;
        return world.getRecipeManager().getRecipe(CrushingRecipe.RECIPE_TYPE, this, world).orElse(null);
    }

    @Override
    protected int getProcessTime(CrushingRecipe recipe) {
        return recipe.getProcessTime();
    }

    @Override
    protected Collection<ItemStack> getProcessResults(CrushingRecipe recipe) {
        return recipe.getResults(this);
    }

    @Override
    protected Collection<ItemStack> getPossibleProcessResult(CrushingRecipe recipe) {
        return recipe.getPossibleResults(this);
    }

    @SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.UP)
            return SLOTS_INPUT;
        if (side == Direction.DOWN)
            return SLOTS_OUTPUT;
        return SLOTS_ALL;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return index < INPUT_SLOT_COUNT;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return index >= INPUT_SLOT_COUNT;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return TextUtil.translate("container", "crusher");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory) {
        return new CrusherContainer(id, playerInventory, this, this.fields);
    }

    public static class Basic extends CrusherTileEntity {
        public Basic() {
            super(MachineTier.BASIC);
        }
    }
}
