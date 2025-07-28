package dev.yhdgms1.shadow_clones;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ShadowClonesClient implements ClientModInitializer {
	public static final EntityModelLayer MODEL_CLONE_LAYER = new EntityModelLayer(ShadowClones.CLONE_ID, "main");
	public static final KeyBinding SPAWN_CLONES_KEY_BINDING = KeyBindingHelper.registerKeyBinding(
			new KeyBinding("key.shadow_clones.spawn", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_N, "category.shadow_clones.main")
	);

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(ShadowClones.CLONE, CloneEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(MODEL_CLONE_LAYER, CloneEntityModel::getTexturedModelData);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (SPAWN_CLONES_KEY_BINDING.wasPressed()) {
				ClientPlayNetworking.send(new SummonShadowClonesC2SPayload());
			}
		});
	}
}