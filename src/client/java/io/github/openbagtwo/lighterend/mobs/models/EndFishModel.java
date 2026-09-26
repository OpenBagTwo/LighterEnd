package io.github.openbagtwo.lighterend.mobs.models;

import io.github.openbagtwo.lighterend.mobs.states.EndFishRenderState;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;

public class EndFishModel extends EntityModel<EndFishRenderState> {

  private final ModelPart model;
  private final ModelPart fin_top;
  private final ModelPart fin_bottom;
  private final ModelPart flipper;
  private final ModelPart fin_right;
  private final ModelPart fin_left;

  public static TexturedModelData getTexturedModelData() {
    ModelData modelData = new ModelData();
    ModelPartData modelPartData = modelData.getRoot();

    ModelPartData bodyPart = modelPartData.addChild(
        EntityModelPartNames.BODY,
        ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -2.0F, -4.0F, 2.0F, 4.0F, 8.0F),
        ModelTransform.origin(0.0F, 20.0F, 0.0F)
    );

    bodyPart.addChild(
        EntityModelPartNames.TOP_FIN,
        ModelPartBuilder.create().uv(0, 6).cuboid(0.0F, -8.0F, 0.0F, 0.0F, 8.0F, 6.0F),
        ModelTransform.of(0.0F, -2.0F, -4.0F, -0.6981F, 0.0F, 0.0F)
    );

    bodyPart.addChild(
        EntityModelPartNames.BOTTOM_FIN,
        ModelPartBuilder.create().uv(0, 6).cuboid(0.0F, 0.0F, 0.0F, 0.0F, 8.0F, 6.0F),
        ModelTransform.of(0.0F, 2.0F, -4.0F, 0.6981F, 0.0F, 0.0F)
    );

    bodyPart.addChild(
        EntityModelPartNames.TAIL_FIN,
        ModelPartBuilder.create().uv(0, 15).cuboid(0.0F, -5.0F, 0.0F, 0.0F, 5.0F, 5.0F),
        ModelTransform.of(0.0F, 0.0F, 2.0F, -0.7854F, 0.0F, 0.0F)
    );

    bodyPart.addChild(
        EntityModelPartNames.RIGHT_FIN,
        ModelPartBuilder.create().uv(0, 25).cuboid(-3.7071F, 0.7071F, -1.5F, 3.0F, 0.0F, 3.0F),
        ModelTransform.of(-1.0F, 0.0F, -1.0F, 1.5708F, 0.7854F, 0.0F)
    );

    bodyPart.addChild(
        EntityModelPartNames.LEFT_FIN,
        ModelPartBuilder.create().mirrored().uv(0, 25)
            .cuboid(0.7071F, 0.7071F, -1.5F, 3.0F, 0.0F, 3.0F),
        ModelTransform.of(-1.0F, 0.0F, -1.0F, 1.5708F, -0.7854F, 0.0F)
    );

    return TexturedModelData.of(modelData, 32, 32);
  }

  public EndFishModel(ModelPart modelPart) {
    super(modelPart);

    model = modelPart.getChild(EntityModelPartNames.BODY);
    fin_top = model.getChild(EntityModelPartNames.TOP_FIN);
    fin_bottom = model.getChild(EntityModelPartNames.BOTTOM_FIN);
    flipper = model.getChild(EntityModelPartNames.TAIL_FIN);
    fin_right = model.getChild(EntityModelPartNames.RIGHT_FIN);
    fin_left = model.getChild(EntityModelPartNames.LEFT_FIN);
  }

  @Override
  public void setAngles(EndFishRenderState state) {
    float s1 = (float) Math.sin(state.age * 0.1);
    float s2 = (float) Math.sin(state.age * 0.05);
    flipper.yaw = s1 * 0.3F;
    fin_top.pitch = s2 * 0.02F - 0.6981F;
    fin_bottom.pitch = 0.6981F - s2 * 0.02F;
    fin_left.yaw = s1 * 0.3F - 0.7854F;
    fin_right.yaw = 0.7854F - s1 * 0.3F;
  }
}
