package ladysnake.pickyourpoison.mixin;

import ladysnake.pickyourpoison.common.damage.DamageSourcesExt;
import ladysnake.pickyourpoison.common.damage.PoisonDamageSources;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DamageSources.class)
public class DamageSourcesMixin implements DamageSourcesExt {
    @Unique
    private PoisonDamageSources pickYourPoison$damageSources;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(DynamicRegistryManager registryManager, CallbackInfo ci) {
        this.pickYourPoison$damageSources = new PoisonDamageSources((DamageSources) (Object) this);
    }
    @Override
    public PoisonDamageSources pypSources() {
        return this.pickYourPoison$damageSources;
    }
}
