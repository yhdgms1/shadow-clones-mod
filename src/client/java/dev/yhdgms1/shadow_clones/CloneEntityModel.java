package dev.yhdgms1.shadow_clones;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class CloneEntityModel<S extends CloneRenderState> extends BipedEntityModel<S> {
    public final ModelPart leftSleeve;
    public final ModelPart rightSleeve;
    public final ModelPart leftPants;
    public final ModelPart rightPants;
    public final ModelPart jacket;

    public CloneEntityModel(ModelPart root) {
        this(root, RenderLayer::getEntityTranslucent);
    }

    public CloneEntityModel(ModelPart root, Function<Identifier, RenderLayer> function) {
        super(root, function);

        this.leftSleeve = this.leftArm.getChild("left_sleeve");
        this.rightSleeve = this.rightArm.getChild("right_sleeve");
        this.leftPants = this.leftLeg.getChild("left_pants");
        this.rightPants = this.rightLeg.getChild("right_pants");
        this.jacket = this.body.getChild("jacket");
    }

    public static void addSleeveTexturedModelData(ModelPartData modelPartData, Dilation dilation) {
        ModelPartData modelPartData2;
        ModelPartData modelPartData3;

        modelPartData2 = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.origin(5.0F, 2.0F, 0.0F));
        modelPartData3 = modelPartData.getChild("right_arm");
        modelPartData2.addChild("left_sleeve", ModelPartBuilder.create().uv(48, 48).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.NONE);
        modelPartData3.addChild("right_sleeve", ModelPartBuilder.create().uv(40, 32).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.NONE);
    }

    public static void addSleeveTexturedModelDataSlim(ModelPartData modelPartData, Dilation dilation) {
        ModelPartData modelPartData2;
        ModelPartData modelPartData3;

        modelPartData2 = modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation), ModelTransform.origin(5.0F, 2.0F, 0.0F));
        modelPartData3 = modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation), ModelTransform.origin(-5.0F, 2.0F, 0.0F));
        modelPartData2.addChild("left_sleeve", ModelPartBuilder.create().uv(48, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.NONE);
        modelPartData3.addChild("right_sleeve", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.NONE);
    }

    public static void addRestTexturedModelData(ModelPartData modelPartData, Dilation dilation) {
        ModelPartData modelPartData2;
        ModelPartData modelPartData3;

        modelPartData2 = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(16, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.origin(1.9F, 12.0F, 0.0F));
        modelPartData3 = modelPartData.getChild("right_leg");
        modelPartData2.addChild("left_pants", ModelPartBuilder.create().uv(0, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.NONE);
        modelPartData3.addChild("right_pants", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.NONE);

        ModelPartData modelPartData4 = modelPartData.getChild("body");
        modelPartData4.addChild("jacket", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.NONE);
    }

    public static TexturedModelData getTexturedModelData() {
        Dilation dilation = new Dilation(0, 0, 0);
        ModelData modelData = BipedEntityModel.getModelData(dilation, 0.0F);

        ModelPartData modelPartData = modelData.getRoot();

        addSleeveTexturedModelData(modelPartData, dilation);
        addRestTexturedModelData(modelPartData, dilation);

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(S state) {
        super.setAngles(state);
    }

    @Override
    public void setArmAngle(Arm arm, MatrixStack matrices) {
        this.getRootPart().applyTransform(matrices);

        ModelPart modelPart = arm == Arm.LEFT ? this.leftArm : this.rightArm;

        modelPart.applyTransform(matrices);
    }
}