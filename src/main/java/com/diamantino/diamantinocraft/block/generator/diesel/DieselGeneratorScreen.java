package com.diamantino.diamantinocraft.block.generator.diesel;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import com.diamantino.diamantinocraft.DiamantinoCraft;
import com.diamantino.diamantinocraft.block.AbstractMachineBaseScreen;
import com.diamantino.diamantinocraft.client.renderer.RenderUtils;
import com.diamantino.diamantinocraft.util.TextUtil;

public class DieselGeneratorScreen extends AbstractMachineBaseScreen<DieselGeneratorContainer> {
    public static final ResourceLocation TEXTURE = DiamantinoCraft.getId("textures/gui/fluid_generator.png");

    public DieselGeneratorScreen(DieselGeneratorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public ResourceLocation getGuiTexture() {
        return TEXTURE;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        if (isPointInRegion(135, 17, 13, 51, mouseX, mouseY)) {
            ITextComponent text = TextUtil.fluidWithMax(container.getFluidInTank(), 4000);
            renderTooltip(text.getFormattedText(), mouseX, mouseY);
        }
        if (isPointInRegion(153, 17, 13, 51, mouseX, mouseY)) {
            ITextComponent text = TextUtil.energyWithMax(container.getEnergyStored(), container.getMaxEnergyStored());
            renderTooltip(text.getFormattedText(), mouseX, mouseY);
        }
        super.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        if (minecraft == null) return;
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;

        // Energy meter
        int energyBarHeight = container.getEnergyBarHeight();
        if (energyBarHeight > 0) {
            blit(xPos + 154, yPos + 68 - energyBarHeight, 176, 31, 12, energyBarHeight);
        }

        // Fluid tank
        RenderUtils.renderGuiTank(container.getFluidInTank(), 4000, xPos + 136, yPos + 18, 0, 12, 50);
    }
}
