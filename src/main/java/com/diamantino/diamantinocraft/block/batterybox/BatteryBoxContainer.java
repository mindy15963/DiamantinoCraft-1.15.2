package com.diamantino.diamantinocraft.block.batterybox;

import com.diamantino.diamantinocraft.block.AbstractEnergyStorageContainer;
import com.diamantino.diamantinocraft.block.AbstractMachineBaseTileEntity;
import com.diamantino.diamantinocraft.init.ModContainers;
import com.diamantino.diamantinocraft.util.InventoryUtilsComp;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraftforge.energy.CapabilityEnergy;

public class BatteryBoxContainer extends AbstractEnergyStorageContainer {
    final BatteryBoxTileEntity tileEntity;

    public BatteryBoxContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new BatteryBoxTileEntity(), new IntArray(AbstractMachineBaseTileEntity.FIELDS_COUNT));
    }

    public BatteryBoxContainer(int id, PlayerInventory playerInventory, BatteryBoxTileEntity tileEntity, IIntArray fieldsIn) {
        super(ModContainers.batteryBox, id, tileEntity, fieldsIn);
        this.tileEntity = tileEntity;

        this.addSlot(new Slot(this.tileEntity, 0, 71, 19));
        this.addSlot(new Slot(this.tileEntity, 1, 71, 37));
        this.addSlot(new Slot(this.tileEntity, 2, 71, 55));
        this.addSlot(new Slot(this.tileEntity, 3, 89, 19));
        this.addSlot(new Slot(this.tileEntity, 4, 89, 37));
        this.addSlot(new Slot(this.tileEntity, 5, 89, 55));

        InventoryUtilsComp.createPlayerSlots(playerInventory, 8, 84).forEach(this::addSlot);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            final int inventorySize = BatteryBoxTileEntity.INVENTORY_SIZE;
            final int playerInventoryEnd = inventorySize + 27;
            final int playerHotbarEnd = playerInventoryEnd + 9;

            if (index >= inventorySize) {
                if (this.isBattery(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 0, inventorySize, false)) {
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

    private boolean isBattery(ItemStack stack) {
        return stack.getCapability(CapabilityEnergy.ENERGY).isPresent();
    }
}
