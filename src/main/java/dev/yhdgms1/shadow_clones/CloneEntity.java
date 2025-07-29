package dev.yhdgms1.shadow_clones;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

public class CloneEntity extends PathAwareEntity {
    private static final TrackedData<String> OWNER_ID = DataTracker.registerData(CloneEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> PROFILE_NAME = DataTracker.registerData(CloneEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> SKIN_URL = DataTracker.registerData(CloneEntity.class, TrackedDataHandlerRegistry.STRING);

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

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.MAX_HEALTH, 1.0).add(EntityAttributes.BURNING_TIME, 0);
    }
}