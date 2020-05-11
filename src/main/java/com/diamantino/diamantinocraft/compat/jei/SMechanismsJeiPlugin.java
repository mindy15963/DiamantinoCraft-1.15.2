package com.diamantino.diamantinocraft.compat.jei;

import com.diamantino.diamantinocraft.crafting.recipe.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import com.diamantino.diamantinocraft.DiamantinoCraft;
import com.diamantino.diamantinocraft.block.alloysmelter.AlloySmelterContainer;
import com.diamantino.diamantinocraft.block.alloysmelter.AlloySmelterScreen;
import com.diamantino.diamantinocraft.block.compressor.CompressorContainer;
import com.diamantino.diamantinocraft.block.compressor.CompressorScreen;
import com.diamantino.diamantinocraft.block.crusher.CrusherContainer;
import com.diamantino.diamantinocraft.block.crusher.CrusherScreen;
import com.diamantino.diamantinocraft.block.electricfurnace.ElectricFurnaceContainer;
import com.diamantino.diamantinocraft.block.electricfurnace.ElectricFurnaceScreen;
import com.diamantino.diamantinocraft.block.mixer.MixerScreen;
import com.diamantino.diamantinocraft.block.refinery.RefineryScreen;
import com.diamantino.diamantinocraft.block.solidifier.SolidifierScreen;
import com.diamantino.diamantinocraft.crafting.recipe.*;
import com.diamantino.diamantinocraft.init.ModBlocks;
import com.diamantino.diamantinocraft.util.Constants;

import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class SMechanismsJeiPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_UID = DiamantinoCraft.getId("plugin/main");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new AlloySmeltingRecipeCategoryJei(guiHelper));
        registration.addRecipeCategories(new CompressingRecipeCategoryJei(guiHelper));
        registration.addRecipeCategories(new CrushingRecipeCategoryJei(guiHelper));
        registration.addRecipeCategories(new DryingRecipeCategoryJei(guiHelper));
        registration.addRecipeCategories(new MixingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new RefiningRecipeCategory(guiHelper));
        registration.addRecipeCategories(new SolidifyingRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(getRecipesOfType(AlloySmeltingRecipe.RECIPE_TYPE), Constants.ALLOY_SMELTING);
        registration.addRecipes(getRecipesOfType(CompressingRecipe.RECIPE_TYPE), Constants.COMPRESSING);
        registration.addRecipes(getRecipesOfType(CrushingRecipe.RECIPE_TYPE), Constants.CRUSHING);
        registration.addRecipes(getRecipesOfType(DryingRecipe.RECIPE_TYPE), Constants.DRYING);
        registration.addRecipes(getRecipesOfType(MixingRecipe.RECIPE_TYPE), Constants.MIXING);
        registration.addRecipes(getRecipesOfType(RefiningRecipe.RECIPE_TYPE), Constants.REFINING);
        registration.addRecipes(getRecipesOfType(SolidifyingRecipe.RECIPE_TYPE), Constants.SOLIDIFYING);
    }

    private static List<IRecipe<?>> getRecipesOfType(IRecipeType<?> recipeType) {
        return Minecraft.getInstance().world.getRecipeManager().getRecipes().stream()
                .filter(r -> r.getType() == recipeType)
                .collect(Collectors.toList());
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(AlloySmelterScreen.class, 90, 32, 28, 23, Constants.ALLOY_SMELTING);
        registration.addRecipeClickArea(CompressorScreen.class, 78, 32, 28, 23, Constants.COMPRESSING);
        registration.addRecipeClickArea(CrusherScreen.class, 45, 32, 28, 23, Constants.CRUSHING);
        registration.addRecipeClickArea(ElectricFurnaceScreen.class, 78, 32, 28, 23,
                VanillaRecipeCategoryUid.BLASTING, VanillaRecipeCategoryUid.FURNACE);
        registration.addRecipeClickArea(MixerScreen.class, 92, 31, 24, 23, Constants.MIXING);
        registration.addRecipeClickArea(RefineryScreen.class, 43, 31, 24, 23, Constants.REFINING);
        registration.addRecipeClickArea(SolidifierScreen.class, 79, 31, 24, 23, Constants.SOLIDIFYING);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(AlloySmelterContainer.class, Constants.ALLOY_SMELTING, 0, 4, 5, 36);
        registration.addRecipeTransferHandler(CompressorContainer.class, Constants.COMPRESSING, 0, 1, 2, 36);
        registration.addRecipeTransferHandler(CrusherContainer.class, Constants.CRUSHING, 0, 1, 5, 36);
        registration.addRecipeTransferHandler(ElectricFurnaceContainer.class, Constants.REFINING, 0, 1, 2, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.alloySmelter), Constants.ALLOY_SMELTING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.basicAlloySmelter), Constants.ALLOY_SMELTING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.compressor), Constants.COMPRESSING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.crusher), Constants.CRUSHING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.basicCrusher), Constants.CRUSHING);
        ModBlocks.DRYING_RACKS.forEach(block -> registration.addRecipeCatalyst(new ItemStack(block), Constants.DRYING));
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.electricFurnace),
                VanillaRecipeCategoryUid.BLASTING, VanillaRecipeCategoryUid.FURNACE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.mixer), Constants.MIXING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.refinery), Constants.REFINING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.solidifier), Constants.SOLIDIFYING);
    }
}
