package dev.yhdgms1.shadow_clones;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class ShadowClones implements ModInitializer {
	public static final String MOD_ID = "shadow_clones";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Identifier CLONE_ID = Identifier.of(MOD_ID, "clone");
	public static final RegistryKey<EntityType<?>> CLONE_KEY = RegistryKey.of(RegistryKeys.ENTITY_TYPE, CLONE_ID);
	public static final EntityType<CloneEntity> CLONE = Registry.register(
			Registries.ENTITY_TYPE,
			CLONE_ID,
			EntityType.Builder.create(CloneEntity::new, SpawnGroup.MISC).dimensions(0.6f, 1.8f).build(CLONE_KEY)
	);

	@Override
	public void onInitialize() {
		FabricDefaultAttributeRegistry.register(CLONE, CloneEntity.createAttributes());

		PayloadTypeRegistry.playC2S().register(SummonShadowClonesC2SPayload.ID, SummonShadowClonesC2SPayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(SummonShadowClonesC2SPayload.ID, (payload, context) -> {
			ServerPlayerEntity player = context.player();
			ServerWorld world = player.getWorld();
			BlockPos pos = player.getBlockPos();

			float health = player.getHealth();

			// todo: place clones nicely
			// todo: for each clone take 1 hp from player

			GameProfile gameProfile = player.getGameProfile();
			String profileId = gameProfile.getId().toString();
			String profileName = gameProfile.getName();

			CloneEntity entity = new CloneEntity(ShadowClones.CLONE, world);

			for (EquipmentSlot slot : EquipmentSlot.values()) {
				EquipmentSlot.Type type = slot.getType();

				if (type == EquipmentSlot.Type.HUMANOID_ARMOR || type == EquipmentSlot.Type.HAND) {
					entity.equipStack(slot, player.getEquippedStack(slot).copy());
					entity.setEquipmentDropChance(slot, 0);
				}
			}

			entity.setUserId(profileId);
			entity.setSkinName(profileName);
			entity.setPos(pos.getX(), pos.getY(), pos.getZ() + 2);

			world.spawnEntity(entity);
		});

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			String userId = handler.player.getGameProfile().getId().toString();

			for (ServerWorld world : server.getWorlds()) {
				for (CloneEntity clone : world.getEntitiesByType(TypeFilter.instanceOf(CloneEntity.class), (entity) -> entity.getUserId().equals(userId))) {
					clone.kill(world);
				}
			}
		});

		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
			String userId = newPlayer.getGameProfile().getId().toString();
			MinecraftServer server = newPlayer.getServer();

			if (server != null) {
				for (ServerWorld world : server.getWorlds()) {
					for (CloneEntity clone : world.getEntitiesByType(TypeFilter.instanceOf(CloneEntity.class), (entity) -> entity.getUserId().equals(userId))) {
						clone.kill(world);
					}
				}
			}
		});

	}
}