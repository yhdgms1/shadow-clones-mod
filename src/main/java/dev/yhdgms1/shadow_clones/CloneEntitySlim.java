package dev.yhdgms1.shadow_clones;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

public class CloneEntitySlim extends CloneEntity {
    protected CloneEntitySlim(EntityType<? extends CloneEntity> entityType, World world) {
        super(entityType, world);
    }
}