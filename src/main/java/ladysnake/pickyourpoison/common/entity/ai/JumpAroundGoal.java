package ladysnake.pickyourpoison.common.entity.ai;

import ladysnake.pickyourpoison.common.entity.PoisonDartFrogEntity;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class JumpAroundGoal extends Goal {
    public static final int DEFAULT_CHANCE = 120;
    protected final PoisonDartFrogEntity frogEntity;
    protected final double speed;
    private final boolean canDespawn;
    protected double targetX;
    protected double targetY;
    protected double targetZ;
    protected int chance;
    protected boolean ignoringChance;

    public JumpAroundGoal(PoisonDartFrogEntity mob, double speed) {
        this(mob, speed, 120);
    }

    public JumpAroundGoal(PoisonDartFrogEntity mob, double speed, int chance) {
        this(mob, speed, chance, true);
    }

    public JumpAroundGoal(PoisonDartFrogEntity entity, double speed, int chance, boolean canDespawn) {
        this.frogEntity = entity;
        this.speed = speed;
        this.chance = chance;
        this.canDespawn = canDespawn;
        this.setControls(EnumSet.of(Control.JUMP));
    }

    public boolean canStart() {
        if (this.frogEntity.hasPassengers()) {
            return false;
        } else {
            if (!this.ignoringChance) {
                if (this.canDespawn && this.frogEntity.getDespawnCounter() >= 100) {
                    return false;
                }

                if (this.frogEntity.getRandom().nextInt(toGoalTicks(this.frogEntity.fleeTime > 0 ? 1 : chance)) != 0) {
                    return false;
                }
            }

            Vec3d vec3d = this.getWanderTarget();
            if (vec3d == null) {
                return false;
            } else {
                this.targetX = vec3d.x;
                this.targetY = vec3d.y;
                this.targetZ = vec3d.z;
                this.ignoringChance = false;
                return frogEntity.isOnGround();
            }
        }
    }

    @Nullable
    protected Vec3d getWanderTarget() {
        return NoPenaltyTargeting.find(this.frogEntity, 5, 2);
    }

    public boolean shouldContinue() {
        return !this.frogEntity.getNavigation().isIdle() && !this.frogEntity.hasPassengers();
    }

    public void start() {
        Vec3d targetPos = new Vec3d(targetX, targetY, targetZ);
        Vec3d dirVec = targetPos.subtract(frogEntity.getPos()).normalize();
        Vec3d velVec = new Vec3d(dirVec.getX() * 0.6f, 0.6f, dirVec.getZ() * 0.6f);
        this.frogEntity.setVelocity(velVec);

        frogEntity.setBodyYaw((float) -(Math.atan2(dirVec.getX(), dirVec.getZ()) * 180f / Math.PI));
        frogEntity.setYaw((float) -(Math.atan2(dirVec.getX(), dirVec.getZ()) * 180f / Math.PI));
    }

    public void stop() {
        super.stop();
    }

    public void ignoreChanceOnce() {
        this.ignoringChance = true;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }
}
