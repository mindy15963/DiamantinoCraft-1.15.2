package com.diamantino.diamantinocraft.block.crusher;

import com.diamantino.diamantinocraft.block.AbstractMachineContainer;
import com.diamantino.diamantinocraft.block.AbstractMachineTileEntity;
import com.diamantino.diamantinocraft.init.MachineType;
import com.diamantino.diamantinocraft.util.InventoryUtilsComp;
import com.diamantino.diamantinocraft.util.MachineTier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import com.diamantino.diamantinocraft.inventory.SlotOutputOnly;

public class CrusherContainer extends AbstractMachineContainer<CrusherTileEntity> {
    public CrusherContainer(int id, PlayerInventory playerInventory, MachineTier tier) {
        this(id, playerInventory, MachineType.CRUSHER.create(tier), new IntArray(AbstractMachineTileEntity.FIELDS_COUNT));
    }

    public CrusherContainer(int id, PlayerInventory playerInventory, CrusherTileEntity tileEntity, IIntArray fieldsIn) {
        super(MachineType.CRUSHER.getContainerType(tileEntity.getMachineTier()), id, tileEntity, fieldsIn);

        this.addSlot(new Slot(this.tileEntity, 0, 26, 35));
        this.addSlot(new SlotOutputOnly(this.tileEntity, 1, 80, 35));
        this.addSlot(new SlotOutputOnly(this.tileEntity, 2, 98, 35));
        this.addSlot(new SlotOutputOnly(this.tileEntity, 3, 116, 35));
        this.addSlot(new SlotOutputOnly(this.tileEntity, 4, 134, 35));

        InventoryUtilsComp.createPlayerSlots(playerInventory, 8, 84).forEach(this::addSlot);

        this.addUpgradeSlots();
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            final int inventorySize = 5;
            final int playerInventoryEnd = inventorySize + 27;
            final int playerHotbarEnd = playerInventoryEnd + 9;

            if (index > 0 && index < inventorySize) {
                if (!this.mergeItemStack(itemstack1, inventorySize, playerHotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index != 0) {
                if (this.isCrushingIngredient(itemstack1)) {
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

    private boolean isCrushingIngredient(ItemStack stack) {
        // TODO
        return true;
    }
}
