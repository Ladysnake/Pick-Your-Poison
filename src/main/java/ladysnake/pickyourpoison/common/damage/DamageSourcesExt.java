package ladysnake.pickyourpoison.common.damage;

import net.minecraft.entity.damage.DamageSources;

/**
 * Allows access to {@link PoisonDamageSources} from {@link DamageSources}
 *
 * <p>Interface injected into {@link DamageSources}
 */
public interface DamageSourcesExt {
    default PoisonDamageSources pypSources() {
        throw new IllegalStateException("Not transformed");
    }
}
