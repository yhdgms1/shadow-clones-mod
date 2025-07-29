package dev.yhdgms1.shadow_clones;

import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
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
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.consume.UseAction;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class CloneEntityRendererSlim extends MobEntityRenderer<CloneEntitySlim, CloneRenderState, CloneEntityModelSlim<CloneRenderState>> {
    public CloneEntityRendererSlim(EntityRendererFactory.Context ctx) {
        super(ctx, new CloneEntityModelSlim<>(ctx.getPart(ShadowClonesClient.MODEL_CLONE_SLIM_LAYER)), 0.5f);

        this.addFeature(new ArmorFeatureRenderer(this, new ArmorEntityModel(ctx.getPart(EntityModelLayers.PLAYER_SLIM_INNER_ARMOR)), new ArmorEntityModel(ctx.getPart(EntityModelLayers.PLAYER_SLIM_OUTER_ARMOR)), ctx.getEquipmentRenderer()));
        this.addFeature(new PlayerHeldItemFeatureRenderer(this));
        this.addFeature(new HeadFeatureRenderer(this, ctx.getEntityModels()));
        this.addFeature(new ElytraFeatureRenderer(this, ctx.getEntityModels(), ctx.getEquipmentRenderer()));
    }

    @Override
    public CloneRenderState createRenderState() {
        return new CloneRenderState();
    }

    @Override
    public void updateRenderState(CloneEntitySlim entity, CloneRenderState state, float f) {
        super.updateRenderState(entity, state, f);
        BipedEntityRenderer.updateBipedRenderState(entity, state, f, this.itemModelResolver);
        CloneEntityRenderer.updateRenderStateShared(entity, state);
    }

    @Override
    public Identifier getTexture(CloneRenderState state) {
        return state.skinTexture;
    }

    @Override
    protected void scale(CloneRenderState playerEntityRenderState, MatrixStack matrixStack) {
        matrixStack.scale(0.9375F, 0.9375F, 0.9375F);
    }
}
