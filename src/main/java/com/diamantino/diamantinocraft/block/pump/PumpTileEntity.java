package com.diamantino.diamantinocraft.block.pump;

import com.diamantino.diamantinocraft.api.IFluidContainer;
import com.diamantino.diamantinocraft.api.RedstoneMode;
import com.diamantino.diamantinocraft.block.AbstractMachineBaseTileEntity;
import com.diamantino.diamantinocraft.init.ModTileEntities;
import com.diamantino.diamantinocraft.item.MachineUpgrades;
import com.diamantino.diamantinocraft.util.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import com.diamantino.diamantinocraft.utils.EnumUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PumpTileEntity extends AbstractMachineBaseTileEntity {
    public static final int ENERGY_PER_BUCKET = 500;
    public static final int PUMP_DELAY = TimeUtils.ticksFromSeconds(5);
    public static final int FIELDS_COUNT = 9;

    protected final IIntArray fields = new IIntArray() {
        @SuppressWarnings("deprecation") // Use of Registry
        @Override
        public int get(int index) {
            switch (index) {
                //Minecraft actually sends fields as shorts, so we need to split energy into 2 fields
                case 0:
                    // Energy lower bytes
                    return getEnergyStored() & 0xFFFF;
                case 1:
                    // Energy upper bytes
                    return (getEnergyStored() >> 16) & 0xFFFF;
                case 2:
                    return getMaxEnergyStored() & 0xFFFF;
                case 3:
                    return (getMaxEnergyStored() >> 16) & 0xFFFF;
                case 4:
                    return redstoneMode.ordinal();
                case 7:
                    return Registry.FLUID.getId(tank.getFluid().getFluid());
                case 8:
                    return tank.getFluid().getAmount();
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 4:
                    redstoneMode = EnumUtils.byOrdinal(value, RedstoneMode.IGNORED);
                    break;
            }
        }

        @Override
        public int size() {
            return FIELDS_COUNT;
        }
    };

    private final FluidTank tank = new FluidTank(4000);
    private final LazyOptional<IFluidHandler> fluidCap = LazyOptional.of(() -> tank);

    public PumpTileEntity() {
        super(ModTileEntities.pump, 2, 10_000, 100, 0, MachineTier.STANDARD);
    }

    private int getHorizontalRange() {
        return 3 + getUpgradeCount(MachineUpgrades.RANGE) * Constants.UPGRADE_RANGE_AMOUNT;
    }

    private int getVerticalRange() {
        return 64;
    }

    private int getEnergyPerOperation() {
        return (int) (ENERGY_PER_BUCKET * getUpgradesEnergyMultiplier());
    }

    private int getPumpDelay() {
        int upgrades = getUpgradeCount(MachineUpgrades.PROCESSING_SPEED);
        return (int) (PUMP_DELAY / (1f + upgrades * Constants.UPGRADE_PROCESSING_SPEED_AMOUNT));
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;

        tryFillFluidContainer();

        // Only pump fluids occasionally
        if (!canMachineRun() || world.getGameTime() % getPumpDelay() != 0) return;

        // TODO: Could probably optimize this to not iterate over the entire region each time
        try (BlockPos.PooledMutable blockPos = BlockPos.PooledMutable.retain()) {
            for (int y = pos.getY(); y > Math.max(0, pos.getY() - getVerticalRange()); --y) {
                int range = getHorizontalRange();
                for (int x = pos.getX() - range; x <= pos.getX() + range; ++x) {
                    for (int z = pos.getZ() - range; z <= pos.getZ() + range; ++z) {
                        blockPos.setPos(x, y, z);
                        BlockState state = world.getBlockState(blockPos);
                        if (tryPumpFluid(blockPos, x, y, z, state)) {
                            return;
                        }
                    }
                }
            }
        }
    }

    private boolean tryPumpFluid(BlockPos.Mutable blockPos, int x, int y, int z, BlockState state) {
        if (state.getBlock() instanceof IBucketPickupHandler) {
            assert world != null;
            Fluid fluid = ((IBucketPickupHandler) state.getBlock()).pickupFluid(world, blockPos, state);
            FluidStack fluidStack = new FluidStack(fluid, 1000);

            if (!fluidStack.isEmpty() && tank.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE) == 1000) {
                tank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                energy.consumeEnergy(getEnergyPerOperation());
                return true;
            }
        }
        return false;
    }

    private void tryFillFluidContainer() {
        // Fill empty fluid containers with output fluids
        ItemStack input = getStackInSlot(0);
        if (input.isEmpty()) return;

        FluidStack fluidInInput = IFluidContainer.getBucketOrContainerFluid(input);
        if (!fluidInInput.isEmpty()) return;

        FluidStack fluidInTank = tank.getFluidInTank(0);
        if (fluidInTank.getAmount() >= 1000) {
            ItemStack filled = IFluidContainer.fillBucketOrFluidContainer(input, fluidInTank);
            if (!filled.isEmpty() && InventoryUtils.mergeItem(this, filled, 1)) {
                tank.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                input.shrink(1);
            }
        }
    }

    private boolean canMachineRun() {
        return world != null
                && getEnergyStored() >= getEnergyPerOperation()
                && tank.getCapacity() - tank.getFluidAmount() >= 1000
                && redstoneMode.shouldRun(world.getRedstonePowerFromNeighbors(pos) > 0);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{0, 1};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return index == 0 && InventoryUtils.isEmptyFluidContainer(itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return index == 1;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return TextUtil.translate("container", "pump");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new PumpContainer(id, player, this, this.fields);
    }

    @Override
    public void read(CompoundNBT tags) {
        this.tank.readFromNBT(tags.getCompound("Tank"));
        super.read(tags);
    }

    @Override
    public CompoundNBT write(CompoundNBT tags) {
        tags.put("Tank", this.tank.writeToNBT(new CompoundNBT()));
        return super.write(tags);
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.removed && cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.orEmpty(cap, fluidCap.cast());
        }
        return super.getCapability(cap, side);
    }
}
