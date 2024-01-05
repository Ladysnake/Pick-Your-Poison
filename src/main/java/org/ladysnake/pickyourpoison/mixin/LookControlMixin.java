package org.ladysnake.pickyourpoison.mixin;

import org.ladysnake.pickyourpoison.common.PickYourPoison;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LookControl.class)
public class LookControlMixin {
    @Shadow
    @Final
    protected MobEntity entity;

    @Inject(method = "lookAt(DDDFF)V", at = @At("HEAD"), cancellable = true)
    public void lookAt(double x, double y, double z, float maxYawChange, float maxPitchChange, CallbackInfo callbackInfo) {
        if (this.entity.hasStatusEffect(PickYourPoison.COMATOSE)) {
            callbackInfo.cancel();
        }
    }
}