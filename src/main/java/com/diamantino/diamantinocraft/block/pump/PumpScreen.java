package com.diamantino.diamantinocraft.block.pump;

import com.diamantino.diamantinocraft.DiamantinoCraft;
import com.diamantino.diamantinocraft.block.AbstractMachineBaseScreen;
import com.diamantino.diamantinocraft.util.TextUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import com.diamantino.diamantinocraft.block.refinery.RefineryTileEntity;
import com.diamantino.diamantinocraft.client.renderer.RenderUtils;

public class PumpScreen extends AbstractMachineBaseScreen<PumpContainer> {
    private static final ResourceLocation TEXTURE = DiamantinoCraft.getId("textures/gui/pump.png");

    public PumpScreen(PumpContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
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
            renderTooltip(TextUtil.fluidWithMax(container.getFluidInTank(), RefineryTileEntity.TANK_CAPACITY).getFormattedText(), mouseX, mouseY);
        }
        if (isPointInRegion(153, 17, 13, 51, mouseX, mouseY)) {
            renderTooltip(TextUtil.energyWithMax(container.getEnergyStored(), container.getMaxEnergyStored()).getFormattedText(), mouseX, mouseY);
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

        // Tank
        RenderUtils.renderGuiTank(container.getFluidInTank(), RefineryTileEntity.TANK_CAPACITY, xPos + 136, yPos + 18, 0, 12, 50);
    }
}
