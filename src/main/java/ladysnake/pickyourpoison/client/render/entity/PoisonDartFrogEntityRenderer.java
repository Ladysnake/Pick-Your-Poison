package ladysnake.pickyourpoison.client.render.entity;

import ladysnake.pickyourpoison.client.render.model.PoisonDartFrogEntityModel;
import ladysnake.pickyourpoison.common.entity.PoisonDartFrogEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class PoisonDartFrogEntityRenderer extends GeoEntityRenderer<PoisonDartFrogEntity> {
    public PoisonDartFrogEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new PoisonDartFrogEntityModel());
        this.shadowRadius = 0.35f;
    }

    @Override
    public RenderLayer getRenderType(PoisonDartFrogEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(this.getTextureLocation(animatable));
    }
}
