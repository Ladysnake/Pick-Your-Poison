package ladysnake.pickyourpoison.client.render.model;

import java.util.Locale;

import ladysnake.pickyourpoison.common.PickYourPoison;
import ladysnake.pickyourpoison.common.entity.PoisonDartFrogEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PoisonDartFrogEntityModel extends AnimatedGeoModel<PoisonDartFrogEntity> {
    @Override
    public Identifier getModelResource(PoisonDartFrogEntity poisonDartFrog) {
        return new Identifier(PickYourPoison.MODID, "geo/entity/poison_dart_frog.geo.json");
    }

    @Override
    public Identifier getTextureResource(PoisonDartFrogEntity poisonDartFrog) {
        return new Identifier(PickYourPoison.MODID, "textures/entity/" + poisonDartFrog.getPoisonDartFrogType().toString().toLowerCase(Locale.ROOT) + ".png");
    }

    @Override
    public Identifier getAnimationResource(PoisonDartFrogEntity poisonDartFrog) {
        return new Identifier(PickYourPoison.MODID, "animations/entity/poison_dart_frog.animation.json");
    }

    @Override
    public void setLivingAnimations(PoisonDartFrogEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
    }
}
