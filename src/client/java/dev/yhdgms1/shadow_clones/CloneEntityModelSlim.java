package dev.yhdgms1.shadow_clones;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class CloneEntityModelSlim<S extends CloneRenderState> extends CloneEntityModel<S> {
    public CloneEntityModelSlim(ModelPart root) {
        this(root, RenderLayer::getEntityTranslucent);
    }

    public CloneEntityModelSlim(ModelPart root, Function<Identifier, RenderLayer> function) {
        super(root, function);
    }

    public static TexturedModelData getTexturedModelData() {
        Dilation dilation = new Dilation(0, 0, 0);
        ModelData modelData = BipedEntityModel.getModelData(dilation, 0.0F);

        ModelPartData modelPartData = modelData.getRoot();

        CloneEntityModel.addSleeveTexturedModelDataSlim(modelPartData, dilation);
        CloneEntityModel.addRestTexturedModelData(modelPartData, dilation);

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setArmAngle(Arm arm, MatrixStack matrices) {
        this.getRootPart().applyTransform(matrices);

        ModelPart modelPart = arm == Arm.LEFT ? this.leftArm : this.rightArm;

        float f = 0.5F * (float)(arm == Arm.RIGHT ? 1 : -1);
        modelPart.originX += f;
        modelPart.applyTransform(matrices);
        modelPart.originX -= f;
    }
}