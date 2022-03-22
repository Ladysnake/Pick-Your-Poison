package ladysnake.pickyourpoison.mixin;

import ladysnake.pickyourpoison.common.PickYourPoison;
import ladysnake.pickyourpoison.common.damage.PoisonDamageSource;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {
    @Shadow
    private int prevFoodLevel;

    @Shadow
    private int foodLevel;

    @Shadow
    private float saturationLevel;

    @Shadow
    private float exhaustion;

    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    public void update(PlayerEntity player, CallbackInfo callbackInfo) {
        if (player.hasStatusEffect(PickYourPoison.STIMULATION)) {
            int stimulationLevel = player.getStatusEffect(PickYourPoison.STIMULATION).getAmplifier() + 1;

            Difficulty difficulty = player.world.getDifficulty();
            this.prevFoodLevel = this.foodLevel;
            if (this.exhaustion > 4.0f) {
                this.exhaustion -= 4.0f;
                if (this.saturationLevel > 0.0f) {
                    this.saturationLevel = Math.max(this.saturationLevel - 1.0f, 0.0f);
                } else if (difficulty != Difficulty.PEACEFUL) {
                    player.damage(PoisonDamageSource.STIMULATION, stimulationLevel * 2f);
                }
            }

            callbackInfo.cancel();
        }
    }
}
