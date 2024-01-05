package org.ladysnake.pickyourpoison.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.ladysnake.pickyourpoison.common.PickYourPoison;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo callbackInfo) {
        if (PickYourPoison.isComatose(MinecraftClient.getInstance().player) && !MinecraftClient.getInstance().isPaused()) {
            KeyBinding.unpressAll();
            callbackInfo.cancel();
        }
    }
}
