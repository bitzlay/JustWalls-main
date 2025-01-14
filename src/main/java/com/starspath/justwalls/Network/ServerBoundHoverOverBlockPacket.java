package com.starspath.justwalls.Network;

import com.starspath.justwalls.BlockHPConfig;
import com.starspath.justwalls.blocks.abstracts.StructureBlock;
import com.starspath.justwalls.init.PacketHandler;
import com.starspath.justwalls.world.DamageBlockSaveData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerBoundHoverOverBlockPacket {
    private final int x, y, z;

    public ServerBoundHoverOverBlockPacket(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ServerBoundHoverOverBlockPacket(FriendlyByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    public boolean handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer serverPlayer = context.get().getSender();
            if (serverPlayer != null) {
                Level serverLevel = serverPlayer.level();
                BlockPos blockPos = new BlockPos(x, y, z);
                BlockState blockState = serverLevel.getBlockState(blockPos);
                Block block = blockState.getBlock();
                DamageBlockSaveData damageBlockSaveData = DamageBlockSaveData.get(serverLevel);

                boolean isHPBlock = block instanceof StructureBlock || BlockHPConfig.hasCustomHP(block);
                BlockPos masterPos = blockPos;

                if (block instanceof StructureBlock structureBlock) {
                    BlockPos tempMasterPos = structureBlock.getMasterPos(serverLevel, blockPos, blockState);
                    if (tempMasterPos != null) {
                        masterPos = tempMasterPos;
                    }
                }

                int maxHp = damageBlockSaveData.getDefaultResistance(serverLevel, masterPos);
                int currentHP = damageBlockSaveData.hasBlock(masterPos) ? damageBlockSaveData.getBlockHP(masterPos) : maxHp;

                PacketHandler.INSTANCE.sendTo(
                        new ClientBoundStructureHPPacket(currentHP, maxHp, isHPBlock),
                        serverPlayer.connection.connection,
                        NetworkDirection.PLAY_TO_CLIENT
                );
            }
        });
        return true;
    }
}