package com.diamantino.diamantinocraft.network.internal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import com.diamantino.diamantinocraft.client.gui.nbt.DisplayNBTScreen;

import java.util.function.Supplier;

public class DisplayNBTPacket {
    private CompoundNBT nbt;
    private ITextComponent title;

    public DisplayNBTPacket() {
    }

    public DisplayNBTPacket(CompoundNBT nbt, ITextComponent title) {
        this.nbt = nbt;
        this.title = title;
    }

    public static DisplayNBTPacket fromBytes(PacketBuffer buffer) {
        DisplayNBTPacket packet = new DisplayNBTPacket();
        packet.nbt = buffer.readCompoundTag();
        packet.title = buffer.readTextComponent();
        return packet;
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeCompoundTag(this.nbt);
        buffer.writeTextComponent(this.title);
    }

    public static void handle(DisplayNBTPacket packet, Supplier<NetworkEvent.Context> context) {
        ClientWrapper.handle(packet);
        context.get().setPacketHandled(true);
    }

    private static class ClientWrapper {
        private static void handle(DisplayNBTPacket packet) {
            ClientPlayerEntity player = Minecraft.getInstance().player;

            if (player != null) {
                DisplayNBTScreen screen = new DisplayNBTScreen(packet.nbt, packet.title);
                Minecraft.getInstance().displayGuiScreen(screen);
            }
        }
    }
}
