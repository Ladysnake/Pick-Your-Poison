package ladysnake.pickyourpoison.mixin;

import ladysnake.pickyourpoison.cca.PickYourPoisonEntityComponents;
import ladysnake.pickyourpoison.common.PickYourPoison;
import ladysnake.pickyourpoison.common.damage.PoisonDamageSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LivingEntity.class, PlayerEntity.class})
public class PlayerEntityFromLivingEntityMixin {
    @Inject(method = "applyDamage", at = @At("HEAD"), cancellable = true)
    protected void applyDamage(DamageSource source, float amount, CallbackInfo callbackInfo) {
        LivingEntity livingEntity = LivingEntity.class.cast(this);
        if (livingEntity.hasStatusEffect(PickYourPoison.COMATOSE)) {
            livingEntity.removeStatusEffect(PickYourPoison.COMATOSE);
            callbackInfo.cancel();
        }

        if (source != PoisonDamageSource.BACKLASH && livingEntity.hasStatusEffect(PickYourPoison.NUMBNESS) && amount > 0.1f) {
            if (!livingEntity.isInvulnerableTo(source)) {
                PickYourPoisonEntityComponents.NUMBNESS_DAMAGE.get(this).setDamageAccumulated(PickYourPoisonEntityComponents.NUMBNESS_DAMAGE.get(this).getDamageAccumulated() + amount);
                callbackInfo.cancel();
            }
        }
    }

}
