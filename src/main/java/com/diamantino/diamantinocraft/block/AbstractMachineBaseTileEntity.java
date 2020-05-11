package com.diamantino.diamantinocraft.block;

import com.diamantino.diamantinocraft.api.RedstoneMode;
import com.diamantino.diamantinocraft.item.MachineUpgradeItem;
import com.diamantino.diamantinocraft.util.MachineTier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IItemProvider;
import com.diamantino.diamantinocraft.utils.EnumUtils;

public abstract class AbstractMachineBaseTileEntity extends AbstractEnergyInventoryTileEntity {
    public static final int FIELDS_COUNT = 5;
    protected final MachineTier tier;

    protected RedstoneMode redstoneMode = RedstoneMode.IGNORED;

    protected final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                //Minecraft actually sends fields as shorts, so we need to split energy into 2 fields
                case 0:
                    // Energy lower bytes
                    return AbstractMachineBaseTileEntity.this.getEnergyStored() & 0xFFFF;
                case 1:
                    // Energy upper bytes
                    return (AbstractMachineBaseTileEntity.this.getEnergyStored() >> 16) & 0xFFFF;
                case 2:
                    // Max energy lower bytes
                    return AbstractMachineBaseTileEntity.this.getMaxEnergyStored() & 0xFFFF;
                case 3:
                    // Max energy upper bytes
                    return (AbstractMachineBaseTileEntity.this.getMaxEnergyStored() >> 16) & 0xFFFF;
                case 4:
                    return AbstractMachineBaseTileEntity.this.redstoneMode.ordinal();
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 4:
                    AbstractMachineBaseTileEntity.this.redstoneMode = EnumUtils.byOrdinal(value, RedstoneMode.IGNORED);
                    break;
            }
        }

        @Override
        public int size() {
            return FIELDS_COUNT;
        }
    };
    
    protected AbstractMachineBaseTileEntity(TileEntityType<?> typeIn, int inventorySize, int maxEnergy, int maxReceive, int maxExtract, MachineTier tier) {
        super(typeIn, inventorySize + tier.getUpgradeSlots(), maxEnergy, maxReceive, maxExtract);
        this.tier = tier;
    }

    public RedstoneMode getRedstoneMode() {
        return redstoneMode;
    }

    public void setRedstoneMode(RedstoneMode redstoneMode) {
        this.redstoneMode = redstoneMode;
    }

    public MachineTier getMachineTier() {
        return tier;
    }

    public int getUpgradeCount(IItemProvider upgradeItem) {
        int count = 0;
        for (int i = getSizeInventory() - tier.getUpgradeSlots(); i < getSizeInventory(); ++i) {
            ItemStack stack = getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() == upgradeItem.asItem()) {
                count += stack.getCount();
            }
        }
        return count;
    }

    protected float getUpgradesEnergyMultiplier() {
        float cost = 1f;
        for (int i = getSizeInventory() - tier.getUpgradeSlots(); i < getSizeInventory(); ++i) {
            ItemStack stack = getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof MachineUpgradeItem) {
                cost += stack.getCount() * ((MachineUpgradeItem) stack.getItem()).getUpgrade().getEnergyUsageMultiplier();
            }
        }
        return cost;
    }

    @Override
    public IIntArray getFields() {
        return fields;
    }

    @Override
    public void read(CompoundNBT tags) {
        super.read(tags);
        this.redstoneMode = EnumUtils.byOrdinal(tags.getByte("RedstoneMode"), RedstoneMode.IGNORED);
    }

    @Override
    public CompoundNBT write(CompoundNBT tags) {
        super.write(tags);
        tags.putByte("RedstoneMode", (byte) this.redstoneMode.ordinal());
        return tags;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
        CompoundNBT tags = packet.getNbtCompound();
        this.redstoneMode = EnumUtils.byOrdinal(tags.getByte("RedstoneMode"), RedstoneMode.IGNORED);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tags = super.getUpdateTag();
        tags.putByte("RedstoneMode", (byte) this.redstoneMode.ordinal());
        return tags;
    }
}
