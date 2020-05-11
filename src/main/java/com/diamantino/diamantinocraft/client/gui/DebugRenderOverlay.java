/*
 * SilentLib - DebugRenderer
 * Copyright (C) 2018 SilentChaos512
 *
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.diamantino.diamantinocraft.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import com.diamantino.diamantinocraft.utils.Anchor;
import com.diamantino.diamantinocraft.utils.Color;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Draws text directly to the screen for debugging purposes
 */
public abstract class DebugRenderOverlay extends AbstractGui {
    protected static final String SPLITTER = "=";

    private static final int DEFAULT_UPDATE_FREQUENCY = 10;
    private static final int DEFAULT_SPLIT_WIDTH = 100;
    private static final int LINE_HEIGHT = 10;

    private List<String> debugText = new ArrayList<>();
    private int textWidth;
    private int textHeight;
    @Deprecated
    private int startX = 3;
    @Deprecated
    private int startY = 3;
    private int ticksPassed = 0;

    protected DebugRenderOverlay() {
        MinecraftForge.EVENT_BUS.addListener(this::renderTick);
        MinecraftForge.EVENT_BUS.addListener(this::clientTick);
    }

    @Deprecated
    protected DebugRenderOverlay(int startX, int startY) {
        this();
        this.startX = startX;
        this.startY = startY;
    }

    /**
     * Gets the text to display, one line per element in the array. Lines containing a single '=' will be split into two
     * columns.
     * @return The list of lines to display, or an empty array.
     */
    @Nonnull
    public abstract List<String> getDebugText();

    /**
     * Gets the scale of the text, where 1 would be default size. Must be greater than zero.
     * @return The text scale
     */
    public abstract float getTextScale();

    public Anchor getAnchorPoint() {
        return Anchor.TOP_LEFT;
    }

    public int getMarginSize() {
        return 3;
    }

    /**
     * The frequency (in ticks) that the debug text should be updated. Higher numbers mean less frequent.
     * @return The delay in ticks between text updates
     */
    @Nonnegative
    public int getUpdateFrequency() {
        return DEFAULT_UPDATE_FREQUENCY;
    }

    @Nonnegative
    public int getSplitWidth() {
        return DEFAULT_SPLIT_WIDTH;
    }

    /**
     * Determine if the overlay is displayed or not.
     * @return True if the overlay should not be displayed, false if it should.
     */
    public abstract boolean isHidden();

    @SuppressWarnings({"WeakerAccess", "SameParameterValue"})
    protected void drawLine(FontRenderer font, String line, int x, int y, int color) {
        String[] array = line.split(SPLITTER);
        if (array.length == 2) {
            font.drawStringWithShadow(array[0].trim(), x, y, color);
            font.drawStringWithShadow(array[1].trim(), x + getSplitWidth(), y, color);
        } else {
            font.drawStringWithShadow(line, x, y, color);
        }
    }

    @Deprecated
    public int getStartX() {
        return startX;
    }

    @Deprecated
    public int getStartY() {
        return startY;
    }

    public void renderTick(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (isHidden() || debugText.isEmpty() || mc.isGamePaused() || mc.gameSettings.showDebugInfo || event.getType() != RenderGameOverlayEvent.ElementType.ALL)
            return;

        // Get text scale, sanity-check the value
        float scale = getTextScale();
        if (scale <= 0f) return;

        FontRenderer font = mc.fontRenderer;

        RenderSystem.pushMatrix();
        RenderSystem.scalef(scale, scale, 1);

        // Divide by text scale to correct position. But it's still a bit off?
        MainWindow mainWindow = mc.getMainWindow();
        int x = (int) (getAnchorPoint().getX(mainWindow.getScaledWidth(), textWidth, getMarginSize()) / getTextScale());
        int y = (int) (getAnchorPoint().getY(mainWindow.getScaledHeight(), textHeight, getMarginSize()) / getTextScale());
        for (String line : debugText) {
            drawLine(font, line, x, y, Color.VALUE_WHITE);
            y += LINE_HEIGHT;
        }

        RenderSystem.popMatrix();
    }

    public void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;

        if (getUpdateFrequency() == 0 || (++ticksPassed) % getUpdateFrequency() == 0) {
            debugText = getDebugText();

            // Recalculate width and height
            FontRenderer font = Minecraft.getInstance().fontRenderer;
            textWidth = 0;
            textHeight = LINE_HEIGHT * debugText.size();
            for (String line : debugText) {
                String[] array = line.split(SPLITTER);
                if (array.length == 2) {
                    int width = getSplitWidth() + font.getStringWidth(array[1]);
                    textWidth = Math.max(textWidth, width);
                } else {
                    textWidth = Math.max(textWidth, font.getStringWidth(line));
                }
            }
        }
    }
}
