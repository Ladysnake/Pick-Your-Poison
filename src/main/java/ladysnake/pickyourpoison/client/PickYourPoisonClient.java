package ladysnake.pickyourpoison.client;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import ladysnake.pickyourpoison.client.render.entity.PoisonDartEntityRenderer;
import ladysnake.pickyourpoison.client.render.entity.PoisonDartFrogEntityRenderer;
import ladysnake.pickyourpoison.client.render.model.FrogOnHeadModel;
import ladysnake.pickyourpoison.common.PickYourPoison;
import ladysnake.pickyourpoison.common.item.PoisonDartFrogBowlItem;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

public class PickYourPoisonClient implements ClientModInitializer {
    private static final String FROGGY_PLAYERS_URL = "https://doctor4t.uuid.gg/pyp-data";
    public static final ArrayList<UUID> FROGGY_PLAYERS_CLIENT = new ArrayList<>();

    private static final ManagedShaderEffect BLACK_SCREEN = ShaderEffectManager.getInstance()
            .manage(new Identifier("pickyourpoison", "shaders/post/blackscreen.json"));

    @Override
    public void onInitializeClient() {
        // FROGGY COSMETICS
        new ClientFroggyPlayerListLoaderThread().start();

        // MODEL LAYERS
        EntityRendererRegistry.INSTANCE.register(PickYourPoison.POISON_DART_FROG, PoisonDartFrogEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(PickYourPoison.POISON_DART, PoisonDartEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(FrogOnHeadModel.MODEL_LAYER, FrogOnHeadModel::getTexturedModelData);

        // COMA SHADER
        ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
            if (MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.hasStatusEffect(PickYourPoison.COMATOSE) && !MinecraftClient.getInstance().player.isSpectator() && !MinecraftClient.getInstance().player.isCreative() && !MinecraftClient.getInstance().player.isSpectator() && !MinecraftClient.getInstance().player.isCreative()) {
                BLACK_SCREEN.render(tickDelta);
            }
        });

        // TRINKETS COMPAT
        if (PickYourPoison.isTrinketsLoaded) {
            registerFrogTrinketRenderers(PickYourPoison.getAllFrogBowls());
        }
    }

    private static class ClientFroggyPlayerListLoaderThread extends Thread {
        public ClientFroggyPlayerListLoaderThread() {
            setName("Pick Your Poison Equippable Frogs Thread");
            setDaemon(true);
        }

        @Override
        public void run() {
            try (BufferedInputStream stream = IOUtils.buffer(new URL(FROGGY_PLAYERS_URL).openStream())) {
                Properties properties = new Properties();
                properties.load(stream);
                synchronized (FROGGY_PLAYERS_CLIENT) {
                    FROGGY_PLAYERS_CLIENT.clear();
                    for (Object o : PickYourPoison.JsonReader.readJsonFromUrl(FROGGY_PLAYERS_URL).toList()) {
                        FROGGY_PLAYERS_CLIENT.add(UUID.fromString((String) o));
                    }
//                    System.out.println(FROGGY_PLAYERS);
                }
            } catch (IOException e) {
                System.out.println("Failed to load froggy list.");
            }
        }

    }

    private static void registerFrogTrinketRenderers(PoisonDartFrogBowlItem... items) {
        for (PoisonDartFrogBowlItem item : items) {
            TrinketRendererRegistry.registerRenderer(item, new TrinketRenderer() {
                private Model model = null;

                @Override
                public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
                    if (model == null) {
                        model = new FrogOnHeadModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(FrogOnHeadModel.MODEL_LAYER));
                    } else if (!entity.isInvisible() && PickYourPoisonClient.FROGGY_PLAYERS_CLIENT.contains(entity.getUuid())) {
                        matrices.push();
                        ((PlayerEntityModel<AbstractClientPlayerEntity>) contextModel).head.rotate(matrices);
                        model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(item.texture)), light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
                        matrices.pop();
                    }
                }
            });
        }
    }
}
