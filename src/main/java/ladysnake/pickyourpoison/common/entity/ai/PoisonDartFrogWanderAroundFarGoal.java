package ladysnake.pickyourpoison.common.entity.ai;

import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.mob.PathAwareEntity;

public class PoisonDartFrogWanderAroundFarGoal extends WanderAroundFarGoal {
    public PoisonDartFrogWanderAroundFarGoal(PathAwareEntity pathAwareEntity, double d) {
        super(pathAwareEntity, d, 1);
    }

    @Override
    public boolean canStart() {
        return super.canStart() && (this.mob.isTouchingWater() || !this.mob.isOnGround());
    }

    @Override
    public boolean shouldContinue() {
        return super.shouldContinue() && (this.mob.isTouchingWater() || !this.mob.isOnGround());
    }
}
