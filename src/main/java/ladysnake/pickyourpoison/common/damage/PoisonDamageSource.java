package ladysnake.pickyourpoison.common.damage;

import net.minecraft.entity.damage.DamageSource;

public class PoisonDamageSource extends DamageSource {
    public static final DamageSource BATRACHOTOXIN = new PoisonDamageSource("batrachotoxin").setBypassesArmor().setUnblockable();
    public static final DamageSource STIMULATION = new PoisonDamageSource("stimulation").setBypassesArmor().setUnblockable();
    public static final DamageSource BACKLASH = new PoisonDamageSource("backlash");

    protected PoisonDamageSource(String name) {
        super(name);
    }
}
