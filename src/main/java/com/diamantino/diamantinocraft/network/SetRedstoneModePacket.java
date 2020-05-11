package com.diamantino.diamantinocraft.network;

import com.diamantino.diamantinocraft.api.RedstoneMode;
import com.diamantino.diamantinocraft.block.AbstractMachineBaseContainer;
import com.diamantino.diamantinocraft.block.AbstractMachineBaseTileEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.network.NetworkEvent;
import com.diamantino.diamantinocraft.utils.EnumUtils;

import java.util.function.Supplier;

public class SetRedstoneModePacket {
    private RedstoneMode mode;

    public SetRedstoneModePacket() {
    }

    public SetRedstoneModePacket(RedstoneMode mode) {
        this.mode = mode;
    }

    public static SetRedstoneModePacket fromBytes(PacketBuffer buffer) {
        SetRedstoneModePacket packet = new SetRedstoneModePacket();
        packet.mode = EnumUtils.byOrdinal(buffer.readByte(), RedstoneMode.IGNORED);
        return packet;
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeByte(this.mode.ordinal());
    }

    public static void handle(SetRedstoneModePacket packet, Supplier<NetworkEvent.Context> context) {
        ServerPlayerEntity player = context.get().getSender();
        context.get().enqueueWork(() -> handlePacket(packet, player));
        context.get().setPacketHandled(true);
    }

    private static void handlePacket(SetRedstoneModePacket packet, ServerPlayerEntity player) {
        if (player != null) {
            if (player.openContainer instanceof AbstractMachineBaseContainer) {
                TileEntity tileEntity = ((AbstractMachineBaseContainer) player.openContainer).getTileEntity();
                if (tileEntity instanceof AbstractMachineBaseTileEntity) {
                    ((AbstractMachineBaseTileEntity) tileEntity).setRedstoneMode(packet.mode);
                }
            }
        }
    }
}
