package ladysnake.pickyourpoison.common.damage;

import ladysnake.pickyourpoison.common.PickYourPoison;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class PoisonDamageSources {
    public static final RegistryKey<DamageType> BATRACHOTOXIN = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, PickYourPoison.id("batrachotoxin"));
    public static final RegistryKey<DamageType> STIMULATION = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, PickYourPoison.id("stimulation"));
    public static final RegistryKey<DamageType> BACKLASH = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, PickYourPoison.id("backlash"));

    private final DamageSource backlash;
    private final DamageSource batrachotoxin;
    private final DamageSource stimulation;

    public PoisonDamageSources(DamageSources damageSources) {
        this.backlash = damageSources.create(BACKLASH);
        this.batrachotoxin = damageSources.create(BATRACHOTOXIN);
        this.stimulation = damageSources.create(STIMULATION);
    }

    public DamageSource backlash() {
        return backlash;
    }

    public DamageSource batrachotoxin() {
        return batrachotoxin;
    }

    public DamageSource stimulation() {
        return stimulation;
    }
}
