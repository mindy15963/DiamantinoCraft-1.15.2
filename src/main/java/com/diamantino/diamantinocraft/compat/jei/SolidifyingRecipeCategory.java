package com.diamantino.diamantinocraft.compat.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import com.diamantino.diamantinocraft.block.solidifier.SolidifierScreen;
import com.diamantino.diamantinocraft.block.solidifier.SolidifierTileEntity;
import com.diamantino.diamantinocraft.crafting.recipe.SolidifyingRecipe;
import com.diamantino.diamantinocraft.init.ModBlocks;
import com.diamantino.diamantinocraft.init.ModItems;
import com.diamantino.diamantinocraft.util.Constants;
import com.diamantino.diamantinocraft.util.TextUtil;

import java.util.*;
import java.util.stream.Collectors;

public class SolidifyingRecipeCategory implements IRecipeCategory<SolidifyingRecipe> {
    private static final int GUI_START_X = 33;
    private static final int GUI_START_Y = 13;
    private static final int GUI_WIDTH = 106;
    private static final int GUI_HEIGHT = 65;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final String localizedName;

    public SolidifyingRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(SolidifierScreen.TEXTURE, GUI_START_X, GUI_START_Y, GUI_WIDTH, GUI_HEIGHT);
        icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.solidifier));
        arrow = guiHelper.drawableBuilder(SolidifierScreen.TEXTURE, 176, 14, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
        localizedName = TextUtil.translate("jei", "category.solidifying").getFormattedText();
    }

    @Override
    public ResourceLocation getUid() {
        return Constants.SOLIDIFYING;
    }

    @Override
    public Class<? extends SolidifyingRecipe> getRecipeClass() {
        return SolidifyingRecipe.class;
    }

    @Override
    public String getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(SolidifyingRecipe recipe, IIngredients ingredients) {
        // Fluids (in tanks)
        ingredients.setInputLists(VanillaTypes.FLUID, Collections.singletonList(recipe.getIngredient().getFluids()));
        ingredients.setOutputLists(VanillaTypes.FLUID, recipe.getFluidOutputs().stream().map(Collections::singletonList).collect(Collectors.toList()));

        // Input fluid containers
        ImmutableList<ItemStack> emptyContainers = ImmutableList.of(new ItemStack(Items.BUCKET), new ItemStack(ModItems.canister));
        List<ItemStack> feedstockContainers = new ArrayList<>();
        recipe.getIngredient().getFluids().forEach(fluid -> addFluidContainers(feedstockContainers, fluid.getFluid()));
        ingredients.setInputLists(VanillaTypes.ITEM, Collections.singletonList(feedstockContainers));

        // Output fluid containers and recipe result
        ingredients.setOutputLists(VanillaTypes.ITEM, Arrays.asList(
                emptyContainers,
                Collections.singletonList(recipe.getRecipeOutput())
        ));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SolidifyingRecipe recipe, IIngredients ingredients) {
        IGuiFluidStackGroup fluidStacks = recipeLayout.getFluidStacks();
        final int capacity = SolidifierTileEntity.TANK_CAPACITY;
        fluidStacks.init(0, true, 25, 5, 12, 50, capacity, true, null);

        fluidStacks.set(0, ingredients.getInputs(VanillaTypes.FLUID).get(0));

        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        itemStacks.init(0, true, 2, 2);
        itemStacks.init(1, false, 2, 45);
        itemStacks.init(2, false, 82, 22);

        itemStacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        itemStacks.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
        itemStacks.set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(1));
    }

    @Override
    public void draw(SolidifyingRecipe recipe, double mouseX, double mouseY) {
        arrow.draw(79 - GUI_START_X, 35 - GUI_START_Y);
    }

    private static void addFluidContainers(Collection<ItemStack> list, Fluid fluid) {
        ItemStack bucket = new ItemStack(fluid.getFilledBucket());
        if (!bucket.isEmpty()) {
            list.add(bucket);
        }
        list.add(ModItems.canister.getStack(fluid));
    }
}
