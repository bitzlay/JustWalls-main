package com.starspath.justwalls.mixin;

import com.starspath.justwalls.BlockHPConfig;
import com.starspath.justwalls.blocks.abstracts.StructureBlock;
import com.starspath.justwalls.world.DamageBlockSaveData;
import com.tacz.guns.entity.EntityKineticBullet;
import com.tacz.guns.particles.BulletHoleOption;
import com.tacz.guns.resource.pojo.data.gun.BulletData;
import com.tacz.guns.util.ExplodeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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
    @Shadow private boolean explosion;
    @Shadow private float explosionDamage;
    @Shadow private float explosionRadius;
    @Shadow private boolean explosionKnockback;
    @Shadow private boolean explosionDestroyBlock;
    @Shadow private boolean igniteBlock;
    @Shadow private ResourceLocation ammoId;
    @Shadow private ResourceLocation gunId;
    @Shadow private ResourceLocation gunDisplayId;

    @ModifyVariable(
            method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;ZLcom/tacz/guns/resource/pojo/data/gun/GunData;Lcom/tacz/guns/resource/pojo/data/gun/BulletData;)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    private static BulletData modifyBulletData(BulletData bulletData) {
        return bulletData;
    }

    @Inject(
            method = "onHitBlock",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void onHitStructureBlock(BlockHitResult result, Vec3 startVec, Vec3 endVec, CallbackInfo ci) {
        EntityKineticBullet bullet = (EntityKineticBullet)(Object)this;
        Level level = bullet.level();
        BlockPos pos = result.getBlockPos();
        BlockState blockState = level.getBlockState(pos);
        Block block = blockState.getBlock();

        if(block instanceof StructureBlock structureBlock || BlockHPConfig.hasCustomHP(block)) {
            DamageBlockSaveData damageBlockSaveData = DamageBlockSaveData.get(level);
            BlockPos targetPos = block instanceof StructureBlock structureBlock?
                    structureBlock.getMasterPos(level, pos, blockState) : pos;

            int damage = this.blockDamage;
            if(bullet.getAmmoId().getPath().contains("rpg")) {
                damage *= 50;
            }

            if(targetPos != null && damageBlockSaveData.damageBlock(level, targetPos, damage) <= 0) {
                level.destroyBlock(targetPos, true);
                damageBlockSaveData.removeBlock(targetPos);
            }

            Vec3 hitVec = result.getLocation();
            if(level instanceof ServerLevel serverLevel) {
                BulletHoleOption bulletHoleOption = new BulletHoleOption(
                        result.getDirection(),
                        result.getBlockPos(),
                        this.ammoId.toString(),
                        this.gunId.toString(),
                        this.gunDisplayId.toString()
                );
                serverLevel.sendParticles(bulletHoleOption, hitVec.x, hitVec.y, hitVec.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);

                if(this.igniteBlock) {
                    serverLevel.sendParticles(ParticleTypes.FLAME, hitVec.x, hitVec.y, hitVec.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                }
            }

            if(this.explosion) {
                ExplodeUtil.createExplosion(
                        bullet.getOwner(),
                        bullet,
                        this.explosionDamage,
                        this.explosionRadius,
                        this.explosionKnockback,
                        this.explosionDestroyBlock,
                        result.getLocation()
                );
            }

            bullet.discard();
            ci.cancel();
        }
    }
}