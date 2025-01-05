package com.starspath.justwalls.mixin;

import com.starspath.justwalls.blocks.abstracts.StructureBlock;
import com.starspath.justwalls.world.DamageBlockSaveData;
import com.tacz.guns.entity.EntityKineticBullet;
import com.tacz.guns.resource.pojo.data.gun.BulletData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityKineticBullet.class)
public abstract class EntityKineticBulletMixin {
    @Shadow private int blockDamage;

    @ModifyVariable(
            method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;ZLcom/tacz/guns/resource/pojo/data/gun/GunData;Lcom/tacz/guns/resource/pojo/data/gun/BulletData;)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    private static BulletData modifyBulletData(BulletData bulletData) {
        return bulletData;
    }

    @Inject(method = "onHitBlock", at = @At("HEAD"), cancellable = true, remap = false)
    private void onHitStructureBlock(BlockHitResult result, Vec3 startVec, Vec3 endVec, CallbackInfo ci) {
        EntityKineticBullet bullet = (EntityKineticBullet)(Object)this;
        Level level = bullet.level();
        BlockPos pos = result.getBlockPos();
        BlockState blockState = level.getBlockState(pos);

        if(blockState.getBlock() instanceof StructureBlock structureBlock) {
            BlockPos masterPos = structureBlock.getMasterPos(level, pos, blockState);
            if(masterPos != null) {
                DamageBlockSaveData damageBlockSaveData = DamageBlockSaveData.get(level);

                if(damageBlockSaveData.damageBlock(level, masterPos, this.blockDamage) <= 0) {
                    level.destroyBlock(masterPos, true);
                    damageBlockSaveData.removeBlock(masterPos);
                }

                ci.cancel();
            }
        }
    }
}