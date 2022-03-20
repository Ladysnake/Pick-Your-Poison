package ladysnake.pickyourpoison.mixin;

import ladysnake.pickyourpoison.common.entity.PoisonDartEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin {
    @Unique
    private int targetCount = -1;

    @ModifyArg(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setStuckArrowCount(I)V"))
    private int pickyourpoison$cancelStuckArrowFromDart(int stuckArrowCount) {
        if (targetCount >= 0) {
            stuckArrowCount = targetCount;
            targetCount = -1;
        }
        return stuckArrowCount;
    }

    @Inject(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setStuckArrowCount(I)V", shift = At.Shift.BEFORE))
    private void pickyourpoison$storeTargetCount(EntityHitResult entityHitResult, CallbackInfo ci) {
        if (entityHitResult.getEntity() instanceof LivingEntity living && PersistentProjectileEntity.class.cast(this) instanceof PoisonDartEntity) {
            targetCount = living.getStuckArrowCount();
        }
    }
}