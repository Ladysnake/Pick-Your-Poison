package org.ladysnake.pickyourpoison.client.render.model;// Made with Blockbench 4.1.5
// Exported for Minecraft version 1.17 with Mojang mappings
// Paste this class into your mod and generate all required imports


import org.ladysnake.pickyourpoison.common.PickYourPoison;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class FrogOnHeadModel extends Model {
    public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(new Identifier(PickYourPoison.MODID, "frog"), "main");
    private final ModelPart frog;

    public FrogOnHeadModel(ModelPart root) {
        super(RenderLayer::getEntityTranslucent);
        this.frog = root.getChild("frog");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        ModelPartData frog = modelPartData.addChild("frog", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -11.6163F, 2.4155F));

        ModelPartData cube_r1 = frog.addChild("cube_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-2.5F, -4.0F, -8F, 5.0F, 3.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.6163F, -2.4155F, -0.3927F, 0.0F, 0.0F));

        ModelPartData poach = frog.addChild("poach", ModelPartBuilder.create().uv(0, 22).cuboid(-1.5F, -0.5F, -3.5F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.0685F, -4.839F, -0.3927F, 0.0F, 0.0F));

        ModelPartData front_legs = frog.addChild("front_legs", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -0.3837F, -2.9155F));

        ModelPartData right = front_legs.addChild("right", ModelPartBuilder.create().uv(10, 17).cuboid(-1.0F, -0.01F, -2.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 0.0F, 0.0F));

        ModelPartData flat = right.addChild("flat", ModelPartBuilder.create().uv(-2, 6).cuboid(-2.5F, 0.99F, -5F, 4.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.5F, 3.0F, 0.5F));

        ModelPartData left = front_legs.addChild("left", ModelPartBuilder.create().uv(10, 17).cuboid(0.0F, -0.01F, -2.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 0.0F, 0.0F));

        ModelPartData flat2 = left.addChild("flat2", ModelPartBuilder.create().uv(-2, 6).mirrored().cuboid(-1.5F, 0.99F, -5F, 4.0F, 0.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.5F, 3.0F, 0.5F));

        ModelPartData back_legs = frog.addChild("back_legs", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.6163F, 2.0845F));

        ModelPartData right2 = back_legs.addChild("right2", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -0.01F, -3.5F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 0.0F, -1.0F));

        ModelPartData flat4 = right2.addChild("flat4", ModelPartBuilder.create().uv(0, 13).cuboid(-3.0F, 0.99F, -6F, 5.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 2.0F, 1.5F));

        ModelPartData left2 = back_legs.addChild("left2", ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, -0.01F, -3.5F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 0.0F, -1.0F));

        ModelPartData flat3 = left2.addChild("flat3", ModelPartBuilder.create().uv(10, 13).cuboid(-2.0F, 0.99F, -6F, 5.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, 2.0F, 1.5F));

        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        matrixStack.push();
        matrixStack.scale(0.75f, 0.75f, 0.75f);
        matrixStack.translate(0f, -0.21f, 0f);
        frog.render(matrixStack, buffer, packedLight, packedOverlay);
        matrixStack.pop();
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }
}