package ladysnake.pickyourpoison.cca;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public final class PickYourPoisonEntityComponents implements EntityComponentInitializer {
    public static final ComponentKey<NumbnessRetributionComponent> NUMBNESS_DAMAGE =
            ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier("pickyourpoison", "numbnessdamage"), NumbnessRetributionComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(LivingEntity.class, NUMBNESS_DAMAGE, world -> new NumbnessRetributionComponent());
    }
}