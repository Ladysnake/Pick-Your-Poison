package ladysnake.pickyourpoison.client;

import ladysnake.pickyourpoison.client.render.entity.PoisonDartEntityRenderer;
import ladysnake.pickyourpoison.client.render.entity.PoisonDartFrogEntityRenderer;
import ladysnake.pickyourpoison.client.render.model.FrogOnHeadModel;
import ladysnake.pickyourpoison.common.PickYourPoison;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class PickYourPoisonClient implements ClientModInitializer {
    private static final ManagedShaderEffect BLACK_SCREEN = ShaderEffectManager.getInstance()
            .manage(new Identifier("pickyourpoison", "shaders/post/blackscreen.json"));

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(PickYourPoison.POISON_DART_FROG, PoisonDartFrogEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(PickYourPoison.POISON_DART, PoisonDartEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(FrogOnHeadModel.MODEL_LAYER, FrogOnHeadModel::getTexturedModelData);

        ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
            if (MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.hasStatusEffect(PickYourPoison.COMATOSE) && !MinecraftClient.getInstance().player.isSpectator() && !MinecraftClient.getInstance().player.isCreative() && !MinecraftClient.getInstance().player.isSpectator() && !MinecraftClient.getInstance().player.isCreative()) {
                BLACK_SCREEN.render(tickDelta);
            }
        });
    }
}
