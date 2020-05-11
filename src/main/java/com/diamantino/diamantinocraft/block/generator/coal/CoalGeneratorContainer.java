package com.diamantino.diamantinocraft.block.generator.coal;

import com.diamantino.diamantinocraft.util.InventoryUtilsComp;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import com.diamantino.diamantinocraft.block.AbstractMachineBaseContainer;
import com.diamantino.diamantinocraft.block.AbstractMachineTileEntity;
import com.diamantino.diamantinocraft.init.ModContainers;

public class CoalGeneratorContainer extends AbstractMachineBaseContainer<CoalGeneratorTileEntity> {
    final CoalGeneratorTileEntity tileEntity;

    public CoalGeneratorContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new CoalGeneratorTileEntity(), new IntArray(AbstractMachineTileEntity.FIELDS_COUNT));
    }

    public CoalGeneratorContainer(int id, PlayerInventory playerInventory, CoalGeneratorTileEntity tileEntity, IIntArray fieldsIn) {
        super(ModContainers.coalGenerator, id, tileEntity, fieldsIn);
        this.tileEntity = tileEntity;

        this.addSlot(new Slot(this.tileEntity, 0, 80, 33));

        InventoryUtilsComp.createPlayerSlots(playerInventory, 8, 84).forEach(this::addSlot);

        this.addUpgradeSlots();
    }

    public int getBurnTime() {
        return fields.get(5);
    }

    public int getTotalBurnTime() {
        return fields.get(6);
    }

    public boolean isBurning() {
        return getBurnTime() > 0;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            final int inventorySize = 1;
            final int playerInventoryEnd = inventorySize + 27;
            final int playerHotbarEnd = playerInventoryEnd + 9;

            if (index != 0) {
                if (this.isFuel(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < playerInventoryEnd) {
                    if (!this.mergeItemStack(itemstack1, playerInventoryEnd, playerHotbarEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < playerHotbarEnd && !this.mergeItemStack(itemstack1, inventorySize, playerInventoryEnd, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, inventorySize, playerHotbarEnd, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    private boolean isFuel(ItemStack stack) {
        return CoalGeneratorTileEntity.isFuel(stack);
    }
}
