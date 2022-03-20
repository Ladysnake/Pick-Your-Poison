package ladysnake.pickyourpoison.mixin;

import ladysnake.pickyourpoison.common.PickYourPoison;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyVariable(method = "setTarget", at = @At("HEAD"))
    public LivingEntity setTarget(LivingEntity target) {
        if (this.hasStatusEffect(PickYourPoison.COMATOSE)) {
            return null;
        }
        return target;
    }
}
