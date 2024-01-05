package org.ladysnake.pickyourpoison.mixin;

import org.ladysnake.pickyourpoison.common.PickYourPoison;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Unique
    private static final Identifier BATRACHOTOXIN_HEARTS = new Identifier("pickyourpoison", "textures/gui/batrachotoxin_hearts.png");
    @Unique
    private static final Identifier TORPOR_HEARTS = new Identifier("pickyourpoison", "textures/gui/torpor_hearts.png");
    @Unique
    private static final Identifier NUMBNESS_HEARTS = new Identifier("pickyourpoison", "textures/gui/numbness_hearts.png");

    @Inject(method = "drawHeart", at = @At("HEAD"), cancellable = true)
    private void pickyourpoison$drawCustomHeart(DrawContext context, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart, CallbackInfo ci) {
        if (!blinking && type == InGameHud.HeartType.NORMAL && MinecraftClient.getInstance().cameraEntity instanceof PlayerEntity player && (player.hasStatusEffect(PickYourPoison.BATRACHOTOXIN) || player.hasStatusEffect(PickYourPoison.TORPOR) || player.hasStatusEffect(PickYourPoison.NUMBNESS))) {
            Identifier textureId;
            if (player.hasStatusEffect(PickYourPoison.TORPOR)) {
                textureId = TORPOR_HEARTS;
            } else if (player.hasStatusEffect(PickYourPoison.BATRACHOTOXIN)) {
                textureId = BATRACHOTOXIN_HEARTS;
            } else if (player.hasStatusEffect(PickYourPoison.NUMBNESS)) {
                textureId = NUMBNESS_HEARTS;
            } else {
                return;
            }
            context.drawTexture(textureId, x, y, halfHeart ? 9 : 0, v, 9, 9);
            ci.cancel();
        }
    }
}