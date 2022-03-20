package ladysnake.pickyourpoison.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import ladysnake.pickyourpoison.common.PickYourPoison;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public class InGameHudMixin extends DrawableHelper {
    @Unique
    private static final Identifier BATRACHOTOXIN_HEARTS = new Identifier("pickyourpoison", "textures/gui/batrachotoxin_hearts.png");
    @Unique
    private static final Identifier TORPOR_HEARTS = new Identifier("pickyourpoison", "textures/gui/torpor_hearts.png");
    @Unique
    private static final Identifier NUMBNESS_HEARTS = new Identifier("pickyourpoison", "textures/gui/numbness_hearts.png");

    @Inject(method = "drawHeart", at = @At("HEAD"), cancellable = true)
    private void pickyourpoison$drawCustomHeart(MatrixStack matrices, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart, CallbackInfo ci) {
        if (!blinking && type == InGameHud.HeartType.NORMAL && MinecraftClient.getInstance().cameraEntity instanceof PlayerEntity player && (player.hasStatusEffect(PickYourPoison.BATRACHOTOXIN) || player.hasStatusEffect(PickYourPoison.TORPOR) || player.hasStatusEffect(PickYourPoison.NUMBNESS))) {
            if (player.hasStatusEffect(PickYourPoison.TORPOR)) {
                RenderSystem.setShaderTexture(0, TORPOR_HEARTS);
            }
            if (player.hasStatusEffect(PickYourPoison.BATRACHOTOXIN)) {
                RenderSystem.setShaderTexture(0, BATRACHOTOXIN_HEARTS);
            }
            if (player.hasStatusEffect(PickYourPoison.NUMBNESS)) {
                RenderSystem.setShaderTexture(0, NUMBNESS_HEARTS);
            }
            drawTexture(matrices, x, y, halfHeart ? 9 : 0, v, 9, 9);
            RenderSystem.setShaderTexture(0, GUI_ICONS_TEXTURE);
            ci.cancel();
        }
    }
}