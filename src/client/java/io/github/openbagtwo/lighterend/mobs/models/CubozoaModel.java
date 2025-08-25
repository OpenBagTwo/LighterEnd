package io.github.openbagtwo.lighterend.mobs.models;

import io.github.openbagtwo.lighterend.mobs.states.CubozoaRenderState;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class CubozoaModel extends EntityModel<CubozoaRenderState> {

  private final static int TENTACLE_COUNT = 4;

  private final ModelPart model;
  private final ModelPart[] tentacle_center;
  private final ModelPart[] tentacle;
  private float scaleY;
  private float scaleXZ;

  public static TexturedModelData getTexturedModelData() {
    ModelData modelData = new ModelData();
    ModelPartData modelPartData = modelData.getRoot();

    ModelPartData bodyPart = modelPartData.addChild(
        EntityModelPartNames.BODY,
        ModelPartBuilder.create().uv(0, 17).cuboid(-2.0F, -12.5F, -2.0F, 4.0F, 4.0F, 4.0F),
        ModelTransform.origin(0.0F, 24.0F, 0.0F)
    );

    bodyPart.addChild(
        "main_cube_r1",
        ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, -7.0F, -5.0F, 10.0F, 7.0F, 10.0F),
        ModelTransform.of(0.0F, -14.0F, 0.0F, 0.0F, 0.0F, -3.1416F)
    );

    for (int i = 1; i <= TENTACLE_COUNT; i++) {
      ModelPartData tentaclePart = bodyPart.addChild(
          "tentacle_center_" + i,
          ModelPartBuilder.create(),
          ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, i * 1.5708F, 0.0F)
      );

      tentaclePart.addChild(
          "tentacle_" + i,
          ModelPartBuilder.create().uv(16, 17).cuboid(-4.0F, 0.0F, 0.0F, 8.0F, 7.0F, 0.0F),
          ModelTransform.origin(0.0F, -7.0F, 4.5F)
      );
    }

    return TexturedModelData.of(modelData, 48, 48);
  }

  public CubozoaModel(ModelPart modelPart) {
    super(modelPart, RenderLayer::getEntityTranslucent);
    tentacle = new ModelPart[TENTACLE_COUNT];
    tentacle_center = new ModelPart[TENTACLE_COUNT];

    model = modelPart.getChild(EntityModelPartNames.BODY);
    for (int i = 1; i <= TENTACLE_COUNT; i++) {
      tentacle_center[i - 1] = model.getChild("tentacle_center_" + i);
      tentacle[i - 1] = tentacle_center[i - 1].getChild("tentacle_" + i);
    }
  }

  @Override
  public void setAngles(CubozoaRenderState renderState) {
    super.setAngles(renderState);
    float sin = MathHelper.sin(renderState.age * 0.13F);
    scaleY = sin * 0.1F + 0.9F;
    scaleXZ = MathHelper.sin(renderState.age * 0.13F + 3.14F) * 0.1F + 0.9F;

    for (int i = 0; i < TENTACLE_COUNT; i++) {
      tentacle[i].pitch = sin * 0.15f;
    }
  }

  public void renderOverride(
      MatrixStack matrices,
      VertexConsumer vertices,
      int light,
      int overlay,
      int color
  ) {
    matrices.push();
    matrices.scale(scaleXZ, scaleY, scaleXZ);
    model.render(matrices, vertices, light, overlay);
    matrices.pop();
  }
}
