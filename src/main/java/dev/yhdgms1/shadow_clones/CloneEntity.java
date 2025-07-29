package dev.yhdgms1.shadow_clones;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class CloneEntity extends PathAwareEntity implements Angerable {
    private static final TrackedData<String> OWNER_ID = DataTracker.registerData(CloneEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> PROFILE_NAME = DataTracker.registerData(CloneEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> SKIN_URL = DataTracker.registerData(CloneEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<Integer> ANGER_TIME = DataTracker.registerData(CloneEntity.class, TrackedDataHandlerRegistry.INTEGER);

    @Nullable
    private UUID angryAt;

    protected CloneEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);

        this.setCustomNameVisible(true);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);

        builder.add(OWNER_ID, "");
        builder.add(PROFILE_NAME, "");
        builder.add(SKIN_URL, "");
        builder.add(ANGER_TIME, 0);
    }

    public void setOwnerId(String url) {
        this.dataTracker.set(OWNER_ID, url);
    }

    public String getOwnerId() {
        return this.dataTracker.get(OWNER_ID);
    }

    public void setSkinURL(String url) {
        this.dataTracker.set(SKIN_URL, url);
    }

    public String getSkinURL() {
        return this.dataTracker.get(SKIN_URL);
    }

    public void setProfileName(String name) {
        this.dataTracker.set(PROFILE_NAME, name);
    }

    public String getProfileName() {
        return this.dataTracker.get(PROFILE_NAME);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient) {
            ServerWorld world = (ServerWorld)this.getWorld();

            this.tickAngerLogic(world, true);
        }
    }

    @Override
    public int getAngerTime() {
        return this.dataTracker.get(ANGER_TIME);
    }

    @Override
    public void setAngerTime(int angerTime) {
        this.dataTracker.set(ANGER_TIME, angerTime);
    }

    @Nullable
    public UUID getAngryAt() {
        return this.angryAt;
    }

    public void setAngryAt(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }

    @Override
    public void chooseRandomAngerTime() {
        setAngerTime(Constants.ANGER_TIME);
    }

    @Override
    public void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(4, new PounceAtTargetGoal(this, 0.2F));
        this.goalSelector.add(5, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(10, new LookAroundGoal(this));
        this.targetSelector.add(4, new ActiveTargetGoal(this, PlayerEntity.class, 2, true, false, this::shouldAngerAt));
        this.targetSelector.add(8, new UniversalAngerGoal(this, true));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.MAX_HEALTH, 1.0).add(EntityAttributes.BURNING_TIME, 0).add(EntityAttributes.ATTACK_DAMAGE, 1).add(EntityAttributes.ATTACK_SPEED, 1).add(EntityAttributes.ATTACK_KNOCKBACK, 1);
    }
}