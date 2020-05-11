package com.diamantino.diamantinocraft.util;

import com.diamantino.diamantinocraft.item.CanisterItem;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemHandlerHelper;
import com.diamantino.diamantinocraft.block.refinery.RefineryTileEntity;

import java.util.function.Predicate;

public final class InventoryUtils {
    private InventoryUtils() {throw new IllegalAccessError("Utility class");}

    /**
     * Gets the total number of matching items in all slots in the inventory.
     *
     * @param inventory  The inventory
     * @param ingredient The items to match ({@link net.minecraft.item.crafting.Ingredient}, etc.)
     * @return The number of items in all matching item stacks
     */
    public static int getTotalCount(IInventory inventory, Predicate<ItemStack> ingredient) {
        int total = 0;
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty() && ingredient.test(stack)) {
                total += stack.getCount();
            }
        }
        return total;
    }

    /**
     * Consumes (removes) items from the inventory. This is useful for machines, which may have
     * multiple input slots and recipes that consume multiple of one item.
     *
     * @param inventory The inventory
     * @param ingredient The items to match ({@link net.minecraft.item.crafting.Ingredient}, etc.)
     * @param amount The total number of items to remove
     */
    public static void consumeItems(IInventory inventory, Predicate<ItemStack> ingredient, int amount) {
        int amountLeft = amount;
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty() && ingredient.test(stack)) {
                int toRemove = Math.min(amountLeft, stack.getCount());

                stack.shrink(toRemove);
                if (stack.isEmpty()) {
                    inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                }

                amountLeft -= toRemove;
                if (amountLeft == 0) {
                    return;
                }
            }
        }
    }

    public static boolean canItemsStack(ItemStack a, ItemStack b) {
        // Determine if the item stacks can be merged
        if (a.isEmpty() || b.isEmpty()) return true;
        return ItemHandlerHelper.canItemStacksStack(a, b) && a.getCount() + b.getCount() <= a.getMaxStackSize();
    }

    public static boolean mergeItem(IInventory inventory, ItemStack stack, int slot) {
        ItemStack current = inventory.getStackInSlot(slot);
        if (current.isEmpty()) {
            inventory.setInventorySlotContents(slot, stack);
            return true;
        } else if (canItemsStack(stack, current)) {
            current.grow(stack.getCount());
            return true;
        }
        return false;
    }

    public static boolean isFilledFluidContainer(ItemStack stack) {
        Item item = stack.getItem();
        return (item instanceof BucketItem && ((BucketItem) item).getFluid() != Fluids.EMPTY)
        || (item instanceof CanisterItem && !((CanisterItem) item).getFluid(stack).isEmpty());
    }

    public static boolean isEmptyFluidContainer(ItemStack stack) {
        Item item = stack.getItem();
        return (item instanceof BucketItem && ((BucketItem) item).getFluid() == Fluids.EMPTY)
                || (item instanceof CanisterItem && ((CanisterItem) item).getFluid(stack).isEmpty());
    }

    public static boolean canFluidsStack(FluidStack stack, FluidStack output) {
        return output.isEmpty() || (output.isFluidEqual(stack) && output.getAmount() + stack.getAmount() <= RefineryTileEntity.TANK_CAPACITY);
    }
}
