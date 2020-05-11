package com.diamantino.diamantinocraft.block.refinery;

import com.diamantino.diamantinocraft.api.IFluidContainer;
import com.diamantino.diamantinocraft.block.AbstractFluidMachineTileEntity;
import com.diamantino.diamantinocraft.crafting.recipe.RefiningRecipe;
import com.diamantino.diamantinocraft.init.ModTileEntities;
import com.diamantino.diamantinocraft.util.InventoryUtils;
import com.diamantino.diamantinocraft.util.MachineTier;
import com.diamantino.diamantinocraft.util.TextUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.Collection;

public class RefineryTileEntity extends AbstractFluidMachineTileEntity<RefiningRecipe> {
    public static final int FIELDS_COUNT = 17;
    public static final int TANK_CAPACITY = 4_000;
    public static final int ENERGY_PER_TICK = 100;

    public RefineryTileEntity() {
        super(ModTileEntities.refinery, 4, 5, TANK_CAPACITY, MachineTier.STANDARD);
    }

    @Nullable
    @Override
    public RefiningRecipe getRecipe() {
        if (world == null) return null;
        return world.getRecipeManager().getRecipe(RefiningRecipe.RECIPE_TYPE, this, world).orElse(null);
    }

    @Override
    protected int getProcessTime(RefiningRecipe recipe) {
        return recipe.getProcessTime();
    }

    @Override
    protected Collection<FluidStack> getFluidResults(RefiningRecipe recipe) {
        return recipe.getFluidResults(this);
    }

    @Override
    protected int getEnergyUsedPerTick() {
        return ENERGY_PER_TICK;
    }

    @Override
    protected int getInputTanks() {
        return 1;
    }

    @Override
    protected int getOutputTanks() {
        return 4;
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;

        tryFillTank();
        tryFillFluidContainer();

        super.tick();
    }

    private void tryFillTank() {
        // Try fill feedstock tank with fluid containers
        ItemStack input = getStackInSlot(0);
        if (input.isEmpty()) return;

        FluidStack fluidStack = IFluidContainer.getBucketOrContainerFluid(input);
        if (canAcceptFluidContainer(input, fluidStack)) {
            this.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);

            ItemStack containerItem = input.getContainerItem();
            input.shrink(1);

            ItemStack output = getStackInSlot(1);
            if (output.isEmpty()) {
                setInventorySlotContents(1, containerItem);
            } else {
                output.grow(1);
            }
        }
    }

    private boolean canAcceptFluidContainer(ItemStack input, FluidStack fluid) {
        ItemStack output = getStackInSlot(1);
        return !fluid.isEmpty()
                && this.isFluidValid(0, fluid)
                && this.fill(fluid, IFluidHandler.FluidAction.SIMULATE) == 1000
                && (output.isEmpty() || InventoryUtils.canItemsStack(input.getContainerItem(), output))
                && (output.isEmpty() || output.getCount() < output.getMaxStackSize());
    }

    private void tryFillFluidContainer() {
        // Fill empty fluid containers with output fluids
        ItemStack input = getStackInSlot(2);
        if (input.isEmpty()) return;

        FluidStack fluidInInput = IFluidContainer.getBucketOrContainerFluid(input);
        if (!fluidInInput.isEmpty()) return;

        for (int i = 1; i < 5; ++i) {
            FluidStack fluidInTank = getFluidInTank(i);
            if (fluidInTank.getAmount() >= 1000) {
                ItemStack filled = IFluidContainer.fillBucketOrFluidContainer(input, fluidInTank);
                if (!filled.isEmpty() && InventoryUtils.mergeItem(this, filled, 3)) {
                    tanks[i].drain(1000, IFluidHandler.FluidAction.EXECUTE);
                    input.shrink(1);
                    return;
                }
            }
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{0, 1, 2, 3};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, @Nullable Direction direction) {
        return (index == 0 && InventoryUtils.isFilledFluidContainer(stack)) || (index == 2 && InventoryUtils.isEmptyFluidContainer(stack));
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return index == 1 || index == 3;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return TextUtil.translate("container", "refinery");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new RefineryContainer(id, player, this, this.fields);
    }
}
