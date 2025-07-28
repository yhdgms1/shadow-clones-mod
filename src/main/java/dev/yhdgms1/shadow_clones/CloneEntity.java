package dev.yhdgms1.shadow_clones;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CloneEntity extends PathAwareEntity {
    private static final TrackedData<String> UUID = DataTracker.registerData(CloneEntity.class, TrackedDataHandlerRegistry.STRING);
    private static final TrackedData<String> SKIN_NAME = DataTracker.registerData(CloneEntity.class, TrackedDataHandlerRegistry.STRING);

    protected CloneEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);

        this.setCustomNameVisible(true);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);

        builder.add(SKIN_NAME, "");
        builder.add(UUID, "");
    }

    public void setUserId(String id) {
        this.dataTracker.set(UUID, id);
    }

    public String getUserId() {
        return this.dataTracker.get(UUID);
    }

    public void setSkinName(String name) {
        this.dataTracker.set(SKIN_NAME, name);
    }

    public String getSkinName() {
        return this.dataTracker.get(SKIN_NAME);
    }

    @Nullable
    public GameProfile getProfile() {
        String id = this.dataTracker.get(UUID);
        String name = this.dataTracker.get(SKIN_NAME);

        if (id.isEmpty() || name.isEmpty()) {
            return null;
        }

        return new GameProfile(java.util.UUID.fromString(id), name);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.MAX_HEALTH, 1.0).add(EntityAttributes.ARMOR, 0).add(EntityAttributes.ARMOR_TOUGHNESS, 0).add(EntityAttributes.BURNING_TIME, 0);
    }
}