package com.diamantino.diamantinocraft.block.mixer;

import com.diamantino.diamantinocraft.api.IFluidContainer;
import com.diamantino.diamantinocraft.block.AbstractFluidMachineTileEntity;
import com.diamantino.diamantinocraft.crafting.recipe.MixingRecipe;
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

public class MixerTileEntity extends AbstractFluidMachineTileEntity<MixingRecipe> {
    public static final int FIELDS_COUNT = 17;
    public static final int TANK_CAPACITY = 4_000;
    public static final int ENERGY_PER_TICK = 100;

    public MixerTileEntity() {
        super(ModTileEntities.mixer, 4, 5, TANK_CAPACITY, MachineTier.STANDARD);
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;

        tryFillTanks();
        tryFillFluidContainer();

        super.tick();
    }

    private void tryFillTanks() {
        // Try fill feedstock tanks with fluid containers
        ItemStack input = getStackInSlot(0);
        if (input.isEmpty()) return;

        FluidStack fluidStack = IFluidContainer.getBucketOrContainerFluid(input);
        int tankIndex = findTankWithFluid(fluidStack);
        if (tankIndex > -1) {
            if (canAcceptFluidContainer(input, fluidStack, tankIndex)) {
                fillTankWithContainer(input, fluidStack, tanks[tankIndex]);
            }
        } else {
            for (int i = 0; i < getInputTanks(); ++i) {
                if (canAcceptFluidContainer(input, fluidStack, i)) {
                    fillTankWithContainer(input, fluidStack, tanks[i]);
                    break;
                }
            }
        }
    }

    private int findTankWithFluid(FluidStack fluidStack) {
        for (int i = 0; i < getInputTanks(); ++i) {
            FluidStack fluidInTank = getFluidInTank(i);
            if (!fluidInTank.isEmpty() && fluidInTank.getFluid() == fluidStack.getFluid()) {
                return i;
            }
        }
        return -1;
    }

    private void fillTankWithContainer(ItemStack input, FluidStack fluidStack, IFluidHandler tank) {
        tank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);

        ItemStack containerItem = input.getContainerItem();
        input.shrink(1);

        ItemStack output = getStackInSlot(1);
        if (output.isEmpty()) {
            setInventorySlotContents(1, containerItem);
        } else {
            output.grow(1);
        }
    }

    private boolean canAcceptFluidContainer(ItemStack input, FluidStack fluid, int tank) {
        ItemStack output = getStackInSlot(1);
        return !fluid.isEmpty()
                && this.isFluidValid(tank, fluid)
                && tanks[tank].fill(fluid, IFluidHandler.FluidAction.SIMULATE) == fluid.getAmount()
                && (output.isEmpty() || InventoryUtils.canItemsStack(input.getContainerItem(), output))
                && (output.isEmpty() || output.getCount() < output.getMaxStackSize());
    }

    private void tryFillFluidContainer() {
        // Fill empty fluid containers with output fluids
        ItemStack input = getStackInSlot(2);
        if (input.isEmpty()) return;

        FluidStack fluidInInput = IFluidContainer.getBucketOrContainerFluid(input);
        if (!fluidInInput.isEmpty()) return;

        FluidStack fluidInTank = getFluidInTank(4);
        if (fluidInTank.getAmount() >= 1000) {
            ItemStack filled = IFluidContainer.fillBucketOrFluidContainer(input, fluidInTank);
            if (!filled.isEmpty() && InventoryUtils.mergeItem(this, filled, 3)) {
                tanks[4].drain(1000, IFluidHandler.FluidAction.EXECUTE);
                input.shrink(1);
            }
        }
    }

    @Override
    protected int getEnergyUsedPerTick() {
        return ENERGY_PER_TICK;
    }

    @Override
    protected int getInputTanks() {
        return 4;
    }

    @Override
    protected int getOutputTanks() {
        return 1;
    }

    @Nullable
    @Override
    public MixingRecipe getRecipe() {
        if (world == null) return null;
        return world.getRecipeManager().getRecipe(MixingRecipe.RECIPE_TYPE, this, world).orElse(null);
    }

    @Override
    protected int getProcessTime(MixingRecipe recipe) {
        return recipe.getProcessTime();
    }

    @Override
    protected Collection<FluidStack> getFluidResults(MixingRecipe recipe) {
        return recipe.getFluidResults(this);
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
        return TextUtil.translate("container", "mixer");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new MixerContainer(id, player, this, this.fields);
    }
}
