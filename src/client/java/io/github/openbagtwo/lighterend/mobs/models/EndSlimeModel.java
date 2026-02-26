package io.github.openbagtwo.lighterend.mobs.models;

import io.github.openbagtwo.lighterend.mobs.states.EndSlimeRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class EndSlimeModel extends EntityModel<EndSlimeRenderState> {

  private final ModelPart flower;
  private final ModelPart crop;


  public EndSlimeModel(ModelPart modelPart, boolean outer) {
    super(modelPart);
    if (outer) {
      flower = null;
      crop = null;
    } else {
      flower = modelPart.getChild("flower");
      crop = modelPart.getChild("crop");
    }

  }

  public static LayerDefinition getOuterTexturedModelData() {
    MeshDefinition modelData = new MeshDefinition();
    PartDefinition modelPartData = modelData.getRoot();
    modelPartData.addOrReplaceChild(
        PartNames.CUBE,
        CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 16.0F, -4.0F, 8.0F, 8.0F, 8.0F),
        PartPose.ZERO);
    return LayerDefinition.create(modelData, 64, 32);
  }

  public static LayerDefinition getInnerTexturedModelData() {
    MeshDefinition modelData = new MeshDefinition();
    PartDefinition modelPartData = modelData.getRoot();
    modelPartData.addOrReplaceChild(PartNames.CUBE,
        CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, 17.0F, -3.0F, 6.0F, 6.0F, 6.0F),
        PartPose.ZERO);
    modelPartData.addOrReplaceChild(
        PartNames.RIGHT_EYE,
        CubeListBuilder.create().texOffs(32, 0).addBox(-3.25F, 18.0F, -3.5F, 2.0F, 2.0F, 2.0F),
        PartPose.ZERO
    );
    modelPartData.addOrReplaceChild(PartNames.LEFT_EYE,
        CubeListBuilder.create().texOffs(32, 4).addBox(1.25F, 18.0F, -3.5F, 2.0F, 2.0F, 2.0F),
        PartPose.ZERO);
    modelPartData.addOrReplaceChild(PartNames.MOUTH,
        CubeListBuilder.create().texOffs(32, 8).addBox(0.0F, 21.0F, -3.5F, 1.0F, 1.0F, 1.0F),
        PartPose.ZERO);

    PartDefinition flowerPart = modelPartData.addOrReplaceChild(
        "flower",
        CubeListBuilder.create(),
        PartPose.ZERO
    );
    PartDefinition cropPart = modelPartData.addOrReplaceChild("crop", CubeListBuilder.create(),
        PartPose.ZERO);

    for (int i = 0; i < 6; i++) {
      final PartDefinition parent = i < 4 ? flowerPart : cropPart;
      final float rot = Mth.DEG_TO_RAD * (i < 4 ? (i * 45F) : ((i - 4) * 90F + 45F));

      PartDefinition petalRotPart = parent.addOrReplaceChild(
          "petalRot_" + i,
          CubeListBuilder.create(),
          PartPose.offsetAndRotation(0, 0, 0, 0, rot, 0)
      );

      petalRotPart.addOrReplaceChild(
          "petal_" + i,
          CubeListBuilder.create().texOffs(40, 0).addBox(0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 0.0F),
          PartPose.offset(-4, 8, 0)
      );
    }

    return LayerDefinition.create(modelData, 64, 32);
  }
}
