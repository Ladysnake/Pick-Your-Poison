package org.ladysnake.pickyourpoison.client;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import org.ladysnake.pickyourpoison.client.render.model.FrogOnHeadModel;
import org.ladysnake.pickyourpoison.common.item.PoisonDartFrogBowlItem;
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
import net.minecraft.util.Pair;

import java.util.Optional;

public class TrinketsCompat {
    public static void registerFrogTrinketRenderers(PoisonDartFrogBowlItem... items) {
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

    public static boolean shouldNotRenderHelmet(LivingEntity livingEntity) {
        Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(livingEntity);
        if (component.isPresent()) {
            for (Pair<SlotReference, ItemStack> pair : component.get().getAllEquipped()) {
                if (pair.getRight().getItem() instanceof PoisonDartFrogBowlItem)
                    return true;
            }
        }
        return false;
    }
}
