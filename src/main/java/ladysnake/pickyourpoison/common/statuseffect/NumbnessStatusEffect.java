package ladysnake.pickyourpoison.common.statuseffect;

import ladysnake.pickyourpoison.cca.PickYourPoisonEntityComponents;
import ladysnake.pickyourpoison.common.damage.PoisonDamageSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class NumbnessStatusEffect extends StatusEffect {
    public NumbnessStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);

        entity.damage(PoisonDamageSource.BACKLASH, PickYourPoisonEntityComponents.NUMBNESS_DAMAGE.get(entity).getDamageAccumulated());
        if (entity.getHealth() <= 0) {
            entity.setHealth(1);
        }

        PickYourPoisonEntityComponents.NUMBNESS_DAMAGE.get(entity).setDamageAccumulated(0f);
    }
}
