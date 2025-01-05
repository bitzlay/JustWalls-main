package com.starspath.justwalls.mixin;

import com.starspath.justwalls.blocks.abstracts.StructureBlock;
import com.starspath.justwalls.world.DamageBlockSaveData;
import com.tacz.guns.entity.EntityKineticBullet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityKineticBullet.class)
public abstract class EntityKineticBulletMixin {
    @Inject(method = "onHitBlock", at = @At("HEAD"), cancellable = true, remap = false)
    private void onHitStructureBlock(BlockHitResult result, Vec3 startVec, Vec3 endVec, CallbackInfo ci) {
        EntityKineticBullet bullet = (EntityKineticBullet)(Object)this;
        Level level = bullet.level();
        BlockPos pos = result.getBlockPos();
        BlockState blockState = level.getBlockState(pos);

        if(blockState.getBlock() instanceof StructureBlock structureBlock) {
            BlockPos masterPos = structureBlock.getMasterPos(level, pos, blockState);
            if(masterPos != null) {
                BlockState masterState = level.getBlockState(masterPos);
                DamageBlockSaveData damageBlockSaveData = DamageBlockSaveData.get(level);

                float damage = bullet.getDamage(result.getLocation());

                if(damageBlockSaveData.damageBlock(level, masterPos, (int) damage) <= 0) {
                    level.destroyBlock(masterPos, true);
                    damageBlockSaveData.removeBlock(masterPos);
                }

                ci.cancel();
            }
        }
    }
}