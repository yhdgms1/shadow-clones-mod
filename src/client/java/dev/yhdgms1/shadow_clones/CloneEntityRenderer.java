package dev.yhdgms1.shadow_clones;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.feature.PlayerHeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.ArmorEntityModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.consume.UseAction;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.UUID;

public class CloneEntityRenderer extends MobEntityRenderer<CloneEntity, CloneRenderState, CloneEntityModel<CloneRenderState>> {
    public CloneEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new CloneEntityModel<>(ctx.getPart(ShadowClonesClient.MODEL_CLONE_LAYER)), 0.5f);

        boolean slim = true;

        this.addFeature(new ArmorFeatureRenderer(this, new ArmorEntityModel(ctx.getPart(slim ? EntityModelLayers.PLAYER_SLIM_INNER_ARMOR : EntityModelLayers.PLAYER_INNER_ARMOR)), new ArmorEntityModel(ctx.getPart(slim ? EntityModelLayers.PLAYER_SLIM_OUTER_ARMOR : EntityModelLayers.PLAYER_OUTER_ARMOR)), ctx.getEquipmentRenderer()));
        this.addFeature(new PlayerHeldItemFeatureRenderer(this));
        this.addFeature(new HeadFeatureRenderer(this, ctx.getEntityModels()));
        this.addFeature(new ElytraFeatureRenderer(this, ctx.getEntityModels(), ctx.getEquipmentRenderer()));
    }

    @Override
    public CloneRenderState createRenderState() {
        return new CloneRenderState();
    }

    @Override
    public void updateRenderState(CloneEntity entity, CloneRenderState state, float f) {
        super.updateRenderState(entity, state, f);
        BipedEntityRenderer.updateBipedRenderState(entity, state, f, this.itemModelResolver);

        String url = entity.getSkinURL();

        if (!url.isEmpty()) {
            TextureManager.LoadingTexture loadingTexture = TextureManager.loadTexture(url);

            if (loadingTexture.state == TextureManager.State.COMPLETE && !state.skinUpdated) {
                state.skinUpdated = true;
                loadingTexture.texture.ifPresent(identifier -> state.skinTexture = identifier);
            }
        } else {
            if (!state.skinUpdated) {
                state.skinUpdated = true;

                Optional<SkinTextures> skinTextures = TextureManager.getOfflineTextures(entity);

                skinTextures.ifPresent(textures -> {
                    state.skinTexture = textures.texture();
                });
            }
        }

        state.equippedHeadStack = entity.getEquippedStack(EquipmentSlot.HEAD);
        state.equippedChestStack = entity.getEquippedStack(EquipmentSlot.CHEST);
        state.equippedLegsStack = entity.getEquippedStack(EquipmentSlot.LEGS);
        state.equippedFeetStack = entity.getEquippedStack(EquipmentSlot.FEET);

        state.leftArmPose = getArmPose(entity, Arm.LEFT);
        state.rightArmPose = getArmPose(entity, Arm.RIGHT);

        state.displayName = Text.of(entity.getProfileName());
    }

    @Override
    public Identifier getTexture(CloneRenderState state) {
        return state.skinTexture;
    }

    @Override
    protected void scale(CloneRenderState playerEntityRenderState, MatrixStack matrixStack) {
        matrixStack.scale(0.9375F, 0.9375F, 0.9375F);
    }

    private static BipedEntityModel.ArmPose getArmPose(CloneEntity player, Arm arm) {
        ItemStack itemStack = player.getStackInHand(Hand.MAIN_HAND);
        ItemStack itemStack2 = player.getStackInHand(Hand.OFF_HAND);
        BipedEntityModel.ArmPose armPose = getArmPose(player, itemStack, Hand.MAIN_HAND);
        BipedEntityModel.ArmPose armPose2 = getArmPose(player, itemStack2, Hand.OFF_HAND);
        if (armPose.isTwoHanded()) {
            armPose2 = itemStack2.isEmpty() ? BipedEntityModel.ArmPose.EMPTY : BipedEntityModel.ArmPose.ITEM;
        }

        return player.getMainArm() == arm ? armPose : armPose2;
    }

    private static BipedEntityModel.ArmPose getArmPose(CloneEntity player, ItemStack stack, Hand hand) {
        if (stack.isEmpty()) {
            return BipedEntityModel.ArmPose.EMPTY;
        } else if (!player.handSwinging && stack.isOf(Items.CROSSBOW) && CrossbowItem.isCharged(stack)) {
            return BipedEntityModel.ArmPose.CROSSBOW_HOLD;
        } else {
            if (player.getActiveHand() == hand && player.getItemUseTimeLeft() > 0) {
                UseAction useAction = stack.getUseAction();
                if (useAction == UseAction.BLOCK) {
                    return BipedEntityModel.ArmPose.BLOCK;
                }

                if (useAction == UseAction.BOW) {
                    return BipedEntityModel.ArmPose.BOW_AND_ARROW;
                }

                if (useAction == UseAction.SPEAR) {
                    return BipedEntityModel.ArmPose.THROW_SPEAR;
                }

                if (useAction == UseAction.CROSSBOW) {
                    return BipedEntityModel.ArmPose.CROSSBOW_CHARGE;
                }

                if (useAction == UseAction.SPYGLASS) {
                    return BipedEntityModel.ArmPose.SPYGLASS;
                }

                if (useAction == UseAction.TOOT_HORN) {
                    return BipedEntityModel.ArmPose.TOOT_HORN;
                }

                if (useAction == UseAction.BRUSH) {
                    return BipedEntityModel.ArmPose.BRUSH;
                }
            }

            return BipedEntityModel.ArmPose.ITEM;
        }
    }

}
