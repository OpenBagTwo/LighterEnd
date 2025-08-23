package io.github.openbagtwo.lighterend.mobs.models;

import io.github.openbagtwo.lighterend.mobs.states.EndSlimeRenderState;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.util.math.MathHelper;

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

  public static TexturedModelData getOuterTexturedModelData() {
    ModelData modelData = new ModelData();
    ModelPartData modelPartData = modelData.getRoot();
    modelPartData.addChild(
        EntityModelPartNames.CUBE,
        ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, 16.0F, -4.0F, 8.0F, 8.0F, 8.0F),
        ModelTransform.NONE);
    return TexturedModelData.of(modelData, 64, 32);
  }

  public static TexturedModelData getInnerTexturedModelData() {
    ModelData modelData = new ModelData();
    ModelPartData modelPartData = modelData.getRoot();
    modelPartData.addChild(EntityModelPartNames.CUBE,
        ModelPartBuilder.create().uv(0, 16).cuboid(-3.0F, 17.0F, -3.0F, 6.0F, 6.0F, 6.0F),
        ModelTransform.NONE);
    modelPartData.addChild(
        EntityModelPartNames.RIGHT_EYE,
        ModelPartBuilder.create().uv(32, 0).cuboid(-3.25F, 18.0F, -3.5F, 2.0F, 2.0F, 2.0F),
        ModelTransform.NONE
    );
    modelPartData.addChild(EntityModelPartNames.LEFT_EYE,
        ModelPartBuilder.create().uv(32, 4).cuboid(1.25F, 18.0F, -3.5F, 2.0F, 2.0F, 2.0F),
        ModelTransform.NONE);
    modelPartData.addChild(EntityModelPartNames.MOUTH,
        ModelPartBuilder.create().uv(32, 8).cuboid(0.0F, 21.0F, -3.5F, 1.0F, 1.0F, 1.0F),
        ModelTransform.NONE);

    ModelPartData flowerPart = modelPartData.addChild(
        "flower",
        ModelPartBuilder.create(),
        ModelTransform.NONE
    );
    ModelPartData cropPart = modelPartData.addChild("crop", ModelPartBuilder.create(),
        ModelTransform.NONE);

    for (int i = 0; i < 6; i++) {
      final ModelPartData parent = i < 4 ? flowerPart : cropPart;
      final float rot = MathHelper.RADIANS_PER_DEGREE * (i < 4 ? (i * 45F) : ((i - 4) * 90F + 45F));

      ModelPartData petalRotPart = parent.addChild(
          "petalRot_" + i,
          ModelPartBuilder.create(),
          ModelTransform.of(0, 0, 0, 0, rot, 0)
      );

      petalRotPart.addChild(
          "petal_" + i,
          ModelPartBuilder.create().uv(40, 0).cuboid(0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 0.0F),
          ModelTransform.origin(-4, 8, 0)
      );
    }

    return TexturedModelData.of(modelData, 64, 32);
  }
}
