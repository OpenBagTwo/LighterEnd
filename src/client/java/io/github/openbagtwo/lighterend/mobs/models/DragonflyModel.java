package io.github.openbagtwo.lighterend.mobs.models;

import io.github.openbagtwo.lighterend.mobs.states.DragonflyRenderState;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;

public class DragonflyModel extends EntityModel<DragonflyRenderState> {

  private final ModelPart model;
  private final ModelPart head;
  private final ModelPart tail;
  private final ModelPart tail_2;
  private final ModelPart wing_1;
  private final ModelPart wing_2;
  private final ModelPart wing_3;
  private final ModelPart wing_4;
  private final ModelPart legs_1;
  private final ModelPart legs_2;

  public static TexturedModelData getTexturedModelData() {
    ModelData modelData = new ModelData();
    ModelPartData modelPartData = modelData.getRoot();

    ModelPartData bodyPart = modelPartData.addChild(
        EntityModelPartNames.BODY,
        ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, 0.0F, 4.0F, 4.0F, 9.0F),
        ModelTransform.origin(2.0F, 21.5F, -4.0F)
    );

    bodyPart.addChild(
        EntityModelPartNames.HEAD,
        ModelPartBuilder.create().uv(17, 0).cuboid(-1.5F, -1.5F, -2.5F, 3.0F, 3.0F, 3.0F),
        ModelTransform.of(-2.0F, -2.0F, 0.0F, 0.3491F, 0.0F, 0.0F)
    );

    ModelPartData tailPart = bodyPart.addChild(
        EntityModelPartNames.TAIL,
        ModelPartBuilder.create().uv(26, 0).cuboid(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 7.0F),
        ModelTransform.origin(-2.0F, -2.0F, 9.0F)
    );

    tailPart.addChild(
        EntityModelPartNames.TAIL_FIN,
        ModelPartBuilder.create().uv(36, 0).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 10.0F),
        ModelTransform.origin(0.0F, 0.0F, 7.0F)
    );

    bodyPart.addChild(
        EntityModelPartNames.LEFT_WING,
        ModelPartBuilder.create().uv(0, 13).cuboid(-15.0F, 0.0F, -3.0F, 15.0F, 0.0F, 4.0F),
        ModelTransform.origin(-2.0F, -4.0F, 4.0F)
    );

    bodyPart.addChild(
        EntityModelPartNames.RIGHT_WING,
        ModelPartBuilder.create().mirrored().uv(0, 13).cuboid(0.0F, 0.0F, -3.0F, 15.0F, 0.0F, 4.0F),
        ModelTransform.origin(-2.0F, -4.0F, 4.0F)
    );

    bodyPart.addChild(
        EntityModelPartNames.LEFT_WING_BASE,
        ModelPartBuilder.create().uv(4, 17).cuboid(-12.0F, 0.0F, -2.5F, 12.0F, 0.0F, 3.0F),
        ModelTransform.origin(-2.0F, -4.0F, 8.0F)
    );

    bodyPart.addChild(
        EntityModelPartNames.RIGHT_WING_BASE,
        ModelPartBuilder.create().mirrored().uv(4, 17).cuboid(0.0F, 0.0F, -2.5F, 12.0F, 0.0F, 3.0F),
        ModelTransform.origin(-2.0F, -4.0F, 8.0F)
    );

    bodyPart.addChild(
        EntityModelPartNames.LEFT_LEG,
        ModelPartBuilder.create().uv(50, 1).cuboid(0.0F, 0.0F, 0.0F, 0.0F, 3.0F, 6.0F),
        ModelTransform.of(-1.0F, 0.0F, 1.0F, 0.0F, 0.0F, -0.5236F)
    );

    bodyPart.addChild(
        EntityModelPartNames.RIGHT_LEG,
        ModelPartBuilder.create().uv(50, 1).cuboid(0.0F, 0.0F, 0.0F, 0.0F, 3.0F, 6.0F),
        ModelTransform.of(-3.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.5236F)
    );

    return TexturedModelData.of(modelData, 64, 64);
  }

  public DragonflyModel(ModelPart modelPart) {
    super(modelPart);

    model = modelPart.getChild(EntityModelPartNames.BODY);
    head = model.getChild(EntityModelPartNames.HEAD);
    tail = model.getChild(EntityModelPartNames.TAIL);
    tail_2 = tail.getChild(EntityModelPartNames.TAIL_FIN);
    wing_1 = model.getChild(EntityModelPartNames.LEFT_WING);
    wing_2 = model.getChild(EntityModelPartNames.RIGHT_WING);
    wing_3 = model.getChild(EntityModelPartNames.LEFT_WING_BASE);
    wing_4 = model.getChild(EntityModelPartNames.RIGHT_WING_BASE);
    legs_1 = model.getChild(EntityModelPartNames.LEFT_LEG);
    legs_2 = model.getChild(EntityModelPartNames.RIGHT_LEG);
  }

  @Override
  public void setAngles(DragonflyRenderState renderState) {
    float progress = renderState.animationProgress * 2F;

    wing_1.roll = 0.3491F + (float) Math.sin(progress) * 0.3491F;
    wing_2.roll = -wing_1.roll;

    wing_3.roll = 0.3491F + (float) Math.cos(progress) * 0.3491F;
    wing_4.roll = -wing_3.roll;

    progress = renderState.animationProgress * 0.05F;

    head.pitch = 0.3491F + (float) Math.sin(progress * 0.7F) * 0.1F;
    tail.pitch = (float) Math.cos(progress) * 0.05F - 0.05F;
    tail_2.pitch = -tail.pitch * 1.5F;
  }
}
