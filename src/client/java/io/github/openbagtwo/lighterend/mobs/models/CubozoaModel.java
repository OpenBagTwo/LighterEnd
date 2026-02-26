package io.github.openbagtwo.lighterend.mobs.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.openbagtwo.lighterend.mobs.states.CubozoaRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;

public class CubozoaModel extends EntityModel<CubozoaRenderState> {

  private final static int TENTACLE_COUNT = 4;

  private final ModelPart model;
  private final ModelPart[] tentacle_center;
  private final ModelPart[] tentacle;
  private float scaleY;
  private float scaleXZ;

  public static LayerDefinition getTexturedModelData() {
    MeshDefinition modelData = new MeshDefinition();
    PartDefinition modelPartData = modelData.getRoot();

    PartDefinition bodyPart = modelPartData.addOrReplaceChild(
        PartNames.BODY,
        CubeListBuilder.create().texOffs(0, 17).addBox(-2.0F, -12.5F, -2.0F, 4.0F, 4.0F, 4.0F),
        PartPose.offset(0.0F, 24.0F, 0.0F)
    );

    bodyPart.addOrReplaceChild(
        "main_cube_r1",
        CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -7.0F, -5.0F, 10.0F, 7.0F, 10.0F),
        PartPose.offsetAndRotation(0.0F, -14.0F, 0.0F, 0.0F, 0.0F, -3.1416F)
    );

    for (int i = 1; i <= TENTACLE_COUNT; i++) {
      PartDefinition tentaclePart = bodyPart.addOrReplaceChild(
          "tentacle_center_" + i,
          CubeListBuilder.create(),
          PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, i * 1.5708F, 0.0F)
      );

      tentaclePart.addOrReplaceChild(
          "tentacle_" + i,
          CubeListBuilder.create().texOffs(16, 17).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 7.0F, 0.0F),
          PartPose.offset(0.0F, -7.0F, 4.5F)
      );
    }

    return LayerDefinition.create(modelData, 48, 48);
  }

  public CubozoaModel(ModelPart modelPart) {
    super(modelPart, RenderTypes::entityTranslucent);
    tentacle = new ModelPart[TENTACLE_COUNT];
    tentacle_center = new ModelPart[TENTACLE_COUNT];

    model = modelPart.getChild(PartNames.BODY);
    for (int i = 1; i <= TENTACLE_COUNT; i++) {
      tentacle_center[i - 1] = model.getChild("tentacle_center_" + i);
      tentacle[i - 1] = tentacle_center[i - 1].getChild("tentacle_" + i);
    }
  }

  @Override
  public void setupAnim(CubozoaRenderState renderState) {
    super.setupAnim(renderState);
    float sin = Mth.sin(renderState.ageInTicks * 0.13F);
    scaleY = sin * 0.1F + 0.9F;
    scaleXZ = Mth.sin(renderState.ageInTicks * 0.13F + 3.14F) * 0.1F + 0.9F;

    for (int i = 0; i < TENTACLE_COUNT; i++) {
      tentacle[i].xRot = sin * 0.15f;
    }
  }

  public void renderOverride(
      PoseStack matrices,
      VertexConsumer vertices,
      int light,
      int overlay,
      int color
  ) {
    matrices.pushPose();
    matrices.scale(scaleXZ, scaleY, scaleXZ);
    model.render(matrices, vertices, light, overlay);
    matrices.popPose();
  }
}
