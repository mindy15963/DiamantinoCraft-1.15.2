package com.diamantino.diamantinocraft.block.generator.coal;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import com.diamantino.diamantinocraft.DiamantinoCraft;
import com.diamantino.diamantinocraft.block.AbstractMachineBaseScreen;
import com.diamantino.diamantinocraft.util.TextUtil;

public class CoalGeneratorScreen extends AbstractMachineBaseScreen<CoalGeneratorContainer> {
    public static final ResourceLocation TEXTURE = DiamantinoCraft.getId("textures/gui/coal_generator.png");

    public CoalGeneratorScreen(CoalGeneratorContainer container, PlayerInventory playerInventory, ITextComponent titleIn) {
        super(container, playerInventory, titleIn);
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
        if (isPointInRegion(153, 17, 13, 51, mouseX, mouseY)) {
            ITextComponent text = TextUtil.energyWithMax(container.getEnergyStored(), container.tileEntity.getMaxEnergyStored());
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

        // Fuel remaining
        if (container.isBurning()) {
            int height = getFlameIconHeight();
            blit(xPos + 81, yPos + 53 + 12 - height, 176, 12 - height, 14, height + 1);
        }

        // Energy meter
        int energyBarHeight = container.getEnergyBarHeight();
        if (energyBarHeight > 0) {
            blit(xPos + 154, yPos + 68 - energyBarHeight, 176, 31, 12, energyBarHeight);
        }

        // Debug text
//        int y = 5;
//        for (String line : container.tileEntity.getDebugText()) {
//            font.drawString(line, 5, y, 0xFFFFFF);
//            y += 10;
//        }
    }

    private int getFlameIconHeight() {
        int total = container.getTotalBurnTime();
        if (total == 0) total = 200;
        return container.getBurnTime() * 13 / total;
    }
}
