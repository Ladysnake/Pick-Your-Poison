package ladysnake.pickyourpoison.common.entity;

import com.google.common.collect.ImmutableList;
import ladysnake.pickyourpoison.common.PickYourPoison;
import ladysnake.pickyourpoison.common.entity.ai.JumpAroundGoal;
import ladysnake.pickyourpoison.common.entity.ai.PoisonDartFrogWanderAroundFarGoal;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class PoisonDartFrogEntity extends AnimalEntity implements GeoEntity {
    public static final List<Type> NATURAL_TYPES = ImmutableList.of(
            Type.BLUE, Type.GOLDEN, Type.GREEN, Type.ORANGE, Type.CRIMSON, Type.RED
    );
    private static final TrackedData<String> TYPE = DataTracker.registerData(PoisonDartFrogEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenPlay("animation.poison_dart_frog.idle");
    private static final RawAnimation SWIM_ANIM = RawAnimation.begin().thenPlay("animation.poison_dart_frog.swim");
    private static final RawAnimation JUMP_ANIM = RawAnimation.begin().thenPlay("animation.poison_dart_frog.jump");
    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    public int fleeTime = 0;
    public boolean fromBowl = false;

    public PoisonDartFrogEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    public static Type getRandomNaturalType(Random random) {
        return NATURAL_TYPES.get(random.nextInt(NATURAL_TYPES.size()));
    }

    public static DefaultAttributeContainer.Builder createPoisonDartFrogAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 6.0D);
    }

    public static StatusEffectInstance getFrogPoisonEffect(Type type) {
        switch (type) {
            case BLUE -> {
                return new StatusEffectInstance(PickYourPoison.COMATOSE, 400); // 20s
            }
            case GOLDEN -> {
                return new StatusEffectInstance(PickYourPoison.BATRACHOTOXIN, 600); // 30s
            }
            case GREEN -> {
                return new StatusEffectInstance(PickYourPoison.NUMBNESS, 400); // 20s
            }
            case ORANGE -> {
                return new StatusEffectInstance(PickYourPoison.VULNERABILITY, 600); // 30s
            }
            case CRIMSON -> {
                return new StatusEffectInstance(PickYourPoison.TORPOR, 600); // 30s
            }
            case RED -> {
                return new StatusEffectInstance(PickYourPoison.STIMULATION, 400); // 20s
            }
            case LUXINTRUS -> {
                return new StatusEffectInstance(StatusEffects.BLINDNESS, 1200); // 60s
            }
        }

        return null;
    }

    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(1, new PoisonDartFrogWanderAroundFarGoal(this, 0.8D));
        this.goalSelector.add(2, new JumpAroundGoal(this, 1.0D));
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        if (fallDistance > 10f) {
            return super.handleFallDamage(fallDistance, damageMultiplier, damageSource);
        }
        return false;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar animationData) {
        animationData.add(new AnimationController<GeoAnimatable>(this, "controller", 0, this::animate));
    }

    private <A extends GeoAnimatable> PlayState animate(AnimationState<A> event) {
        if (this.isOnGround()) {
            event.getController().setAnimation(IDLE_ANIM);
        } else if (this.isTouchingWater()) {
            event.getController().setAnimation(SWIM_ANIM);
        } else if (this.age > 4) {
            event.getController().setAnimation(JUMP_ANIM);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animationCache;
    }

    protected void initDataTracker() {
        super.initDataTracker();

        if (this.random.nextInt(100) == 0) {
            this.dataTracker.startTracking(TYPE, Type.LUXINTRUS.toString());
        } else {
            this.dataTracker.startTracking(TYPE, getRandomNaturalType(random).toString());
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.fleeTime > 0) {
            this.fleeTime--;
        }

        this.setNoDrag(!this.isOnGround());

        // turn into rana
        if (this.getPoisonDartFrogType() != Type.RANA && this.hasCustomName()) {
            if (this.getCustomName().getString().equalsIgnoreCase("rana")) {
                this.setPoisonDartFrogType(Type.RANA);
            }
        }

    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (player.getStackInHand(hand).getItem() == Items.BOWL) {
            Item item = Items.BOWL;

            switch (this.getPoisonDartFrogType()) {
                case BLUE -> item = PickYourPoison.BLUE_POISON_DART_FROG_BOWL;
                case GOLDEN -> item = PickYourPoison.GOLDEN_POISON_DART_FROG_BOWL;
                case GREEN -> item = PickYourPoison.GREEN_POISON_DART_FROG_BOWL;
                case ORANGE -> item = PickYourPoison.ORANGE_POISON_DART_FROG_BOWL;
                case CRIMSON -> item = PickYourPoison.CRIMSON_POISON_DART_FROG_BOWL;
                case RED -> item = PickYourPoison.RED_POISON_DART_FROG_BOWL;
                case LUXINTRUS -> item = PickYourPoison.LUXALAMANDER_BOWL;
                case RANA -> item = PickYourPoison.RANA_BOWL;
            }

            ItemStack itemStack = new ItemStack(item);
            if (this.hasCustomName()) {
                itemStack.setCustomName(this.getCustomName());
            }

            if (!player.getAbilities().creativeMode) {
                if (player.getStackInHand(hand).getCount() > 1) {
                    player.getStackInHand(hand).decrement(1);
                    if (!player.getInventory().insertStack(itemStack)) {
                        player.dropItem(itemStack, true);
                    }
                } else {
                    player.setStackInHand(hand, itemStack);
                }
            } else {
                if (!player.getInventory().insertStack(itemStack)) {
                    player.dropItem(itemStack, true);
                }
            }

            this.getWorld().playSound(player, player.getBlockPos(), PickYourPoison.ITEM_POISON_DART_FROG_BOWL_FILL, SoundCategory.NEUTRAL, 1.0f, 1.0f);
            this.discard();
            return ActionResult.SUCCESS;
        }

        return super.interactMob(player, hand);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!getWorld().isClient() && source.getAttacker() != null && !source.isIn(DamageTypeTags.IS_PROJECTILE) && source.getAttacker() instanceof LivingEntity attacker && getFrogPoisonEffect(this.getPoisonDartFrogType()) != null) {
            attacker.addStatusEffect(getFrogPoisonEffect(this.getPoisonDartFrogType()));
        }

        this.fleeTime = 100 + random.nextInt(60);
        return super.damage(source, amount);
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
        if (!this.getWorld().isClient && onGround && this.fallDistance > 0.0F) {
            this.removeSoulSpeedBoost();
            this.addSoulSpeedBoostIfNeeded();
        }

        if (onGround) {
            if (this.fallDistance > 0.0F) {
                landedState.getBlock().onLandedUpon(this.getWorld(), landedState, landedPosition, this, this.fallDistance);
                if (!landedState.isIn(BlockTags.OCCLUDES_VIBRATION_SIGNALS)) {
                    this.emitGameEvent(GameEvent.HIT_GROUND);
                }
            }

            this.onLanding();
        } else if (heightDifference < 0.0D) {
            this.fallDistance = (float) ((double) this.fallDistance - heightDifference);
        }
    }

    public Type getPoisonDartFrogType() {
        return Type.valueOf(this.dataTracker.get(TYPE));
    }

    public void setPoisonDartFrogType(Type type) {
        this.dataTracker.set(TYPE, type.toString());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);

        this.fleeTime = tag.getInt("FleeTime");
        this.fromBowl = tag.getBoolean("FromBowl");
        if (tag.contains("FrogType")) {
            this.setPoisonDartFrogType(Type.valueOf(tag.getString("FrogType")));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);

        tag.putInt("FleeTime", fleeTime);
        tag.putBoolean("FromBowl", fromBowl);
        tag.putString("FrogType", this.getPoisonDartFrogType().toString());
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return PickYourPoison.ENTITY_POISON_DART_FROG_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return PickYourPoison.ENTITY_POISON_DART_FROG_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return PickYourPoison.ENTITY_POISON_DART_FROG_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        StatusEffect statusEffect = effect.getEffectType();
        return statusEffect != StatusEffects.POISON && statusEffect != PickYourPoison.BATRACHOTOXIN && statusEffect != PickYourPoison.COMATOSE && statusEffect != PickYourPoison.NUMBNESS && statusEffect != PickYourPoison.STIMULATION && statusEffect != PickYourPoison.TORPOR && statusEffect != PickYourPoison.VULNERABILITY;
    }

    public enum Type {
        BLUE,
        GOLDEN,
        GREEN,
        ORANGE,
        CRIMSON,
        RED,
        LUXINTRUS,
        RANA
    }

}
