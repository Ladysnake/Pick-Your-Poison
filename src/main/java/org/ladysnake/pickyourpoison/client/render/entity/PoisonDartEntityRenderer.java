/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package org.ladysnake.pickyourpoison.client.render.entity;

import org.ladysnake.pickyourpoison.common.PickYourPoison;
import org.ladysnake.pickyourpoison.common.entity.PoisonDartEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(value = EnvType.CLIENT)
public class PoisonDartEntityRenderer extends ProjectileEntityRenderer<PoisonDartEntity> {
    public static final Identifier TEXTURE = new Identifier(PickYourPoison.MODID, "textures/entity/projectiles/poison_dart.png");

    public PoisonDartEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(PoisonDartEntity poisonDartEntity) {
        return TEXTURE;
    }
}

