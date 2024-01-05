package ladysnake.pickyourpoison.mixin;

import ladysnake.pickyourpoison.cca.PickYourPoisonEntityComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "applyDamage", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/player/PlayerEntity;getHealth()F", ordinal = 0), cancellable = true)
    private void pickyourpoison$saveFromNumbness(DamageSource source, float amount, CallbackInfo ci) {
        if (!getWorld().isClient && getHealth() - amount <= 0) {
            PickYourPoisonEntityComponents.NUMBNESS_DAMAGE.maybeGet(this).ifPresent(retributionComponent -> {
                if (retributionComponent.getDamageAccumulated() > 0 && !retributionComponent.isFromLicking()) {
                    this.setHealth(1.0f);
                    ci.cancel();
                }
            });
        }
    }
}
