package io.github.openbagtwo.lighterend.mobs.models;

import java.util.Set;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.BabyModelTransformer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.ModelTransformer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.math.MathHelper;

public class SilkMothModel extends EntityModel<LivingEntityRenderState> {

  public static final ModelTransformer BABY_TRANSFORMER = new BabyModelTransformer(
      true,
      10.0F,
      0,
      Set.of("head")
  );

  private final ModelPart legsL;
  private final ModelPart cube_r1;
  private final ModelPart cube_r2;
  private final ModelPart cube_r3;
  private final ModelPart legsR;
  private final ModelPart cube_r4;
  private final ModelPart cube_r5;
  private final ModelPart cube_r6;
  private final ModelPart head_pivot;
  private final ModelPart tendril_r_r1;
  private final ModelPart tendril_r_r2;
  private final ModelPart bb_main;
  private final ModelPart wingR_r1;
  private final ModelPart wingL_r1;
  private final ModelPart abdomen_r1;

  public static TexturedModelData getTexturedModelData() {
    ModelData modelData = new ModelData();
    ModelPartData modelPartData = modelData.getRoot();

    ModelPartData legsL = modelPartData.addChild(
        EntityModelPartNames.LEFT_LEG,
        ModelPartBuilder.create().uv(0, 0),
        ModelTransform.of(1.5f, 19.9f, -0.45f, 0.0f, 0.0f, 0.6981f)
    );

    legsL.addChild(
        "cube_r1",
        ModelPartBuilder.create().uv(0, 13).cuboid(0.0216f, 0.0f, -0.5976f, 3.0f, 0.0f, 1.0f),
        ModelTransform.of(0.0f, 0.0f, -1.0f, 0.0f, 0.2182f, 0.3927f)
    );

    legsL.addChild(
        "cube_r2",
        ModelPartBuilder.create().uv(0, 15).cuboid(0.0f, 0.0f, -0.6f, 3.0f, 0.0f, 1.0f),
        ModelTransform.of(0.5f, 0.1f, -0.05f, 0.0f, 0.0f, 0.3927f)
    );

    legsL.addChild(
        "cube_r3",
        ModelPartBuilder.create().uv(0, 14).cuboid(0.0f, 0.0f, -0.5f, 3.0f, 0.0f, 1.0f),
        ModelTransform.of(0.0f, 0.0f, 0.9f, 0.0f, -0.2182f, 0.3927f)
    );

    ModelPartData legsR = modelPartData.addChild(
        EntityModelPartNames.RIGHT_LEG,
        ModelPartBuilder.create().uv(0, 0),
        ModelTransform.of(-1.5f, 19.9f, -0.55f, 0.0f, 3.1416f, -0.6545f)
    );

    legsR.addChild(
        "cube_r4",
        ModelPartBuilder.create().uv(0, 10).cuboid(0.0f, 0.0f, -0.5f, 3.0f, 0.0f, 1.0f),
        ModelTransform.of(0.0f, 0.0f, -1.0f, 0.0f, 0.2182f, 0.3927f)
    );

    legsR.addChild(
        "cube_r5",
        ModelPartBuilder.create().uv(0, 11).cuboid(0.0f, 0.0f, -0.4f, 3.0f, 0.0f, 1.0f),
        ModelTransform.of(0.5f, 0.1f, -0.05f, 0.0f, 0.0f, 0.3927f)
    );

    legsR.addChild(
        "cube_r6",
        ModelPartBuilder.create().uv(0, 12).cuboid(0.0216f, 0.0f, -0.4024f, 3.0f, 0.0f, 1.0f),
        ModelTransform.of(0.0f, 0.0f, 0.9f, 0.0f, -0.2182f, 0.3927f)
    );

    ModelPartData head_pivot = modelPartData.addChild(
        EntityModelPartNames.HEAD,
        ModelPartBuilder.create().uv(15, 10).cuboid(-1.5f, -1.5f, -2.0f, 3.0f, 3.0f, 3.0f),
        ModelTransform.origin(0.0f, 18.0f, -3.0f)
    );

    head_pivot.addChild(
        "tendril_r_r1",
        ModelPartBuilder.create().mirrored().uv(23, 0).cuboid(-1.5f, -5.0f, 0.0f, 3.0f, 5.0f, 0.0f),
        ModelTransform.of(1.0f, -1.15f, -1.0f, 0.0f, 0.0f, 0.3927f)
    );

    head_pivot.addChild(
        "tendril_r_r2",
        ModelPartBuilder.create().uv(23, 0).cuboid(-1.5f, -5.0f, 0.0f, 3.0f, 5.0f, 0.0f),
        ModelTransform.of(-1.0f, -1.15f, -1.0f, 0.0f, 0.0f, -0.3927f)
    );

    ModelPartData bb_main = modelPartData.addChild(
        EntityModelPartNames.BODY,
        ModelPartBuilder.create().uv(19, 19).cuboid(-2.5f, -8.5f, -3.0f, 5.0f, 5.0f, 3.0f),
        ModelTransform.origin(0.0f, 24.0f, 0.0f)
    );

    bb_main.addChild(
        EntityModelPartNames.RIGHT_WING,
        ModelPartBuilder.create().mirrored().uv(0, 5).cuboid(-7.0f, 0.0f, -3.0f, 9.0f, 0.0f, 5.0f),
        ModelTransform.of(-1.5f, -6.5f, 0.5f, 0.0f, 0.0f, 0.3927f)
    );

    bb_main.addChild(
        EntityModelPartNames.LEFT_WING,
        ModelPartBuilder.create().uv(0, 5).cuboid(-2.0f, 0.0f, -3.0f, 9.0f, 0.0f, 5.0f),
        ModelTransform.of(1.5f, -6.5f, 0.5f, 0.0f, 0.0f, -0.3927f)
    );

    bb_main.addChild(
        "abdomen_r1",
        ModelPartBuilder.create().uv(0, 10).cuboid(-3.0f, -4.0f, -1.0f, 4.0f, 4.0f, 7.0f),
        ModelTransform.of(1.0f, -3.9f, 0.0f, -0.3927f, 0.0f, 0.0f)
    );

    return TexturedModelData.of(modelData, 64, 64);
  }

  public SilkMothModel(ModelPart modelPart) {
    super(modelPart);

    legsL = modelPart.getChild(EntityModelPartNames.LEFT_LEG);
    cube_r1 = legsL.getChild("cube_r1");
    cube_r2 = legsL.getChild("cube_r2");
    cube_r3 = legsL.getChild("cube_r3");
    legsR = modelPart.getChild(EntityModelPartNames.RIGHT_LEG);
    cube_r4 = legsR.getChild("cube_r4");
    cube_r5 = legsR.getChild("cube_r5");
    cube_r6 = legsR.getChild("cube_r6");
    head_pivot = modelPart.getChild(EntityModelPartNames.HEAD);
    tendril_r_r1 = head_pivot.getChild("tendril_r_r1");
    tendril_r_r2 = head_pivot.getChild("tendril_r_r2");
    bb_main = modelPart.getChild(EntityModelPartNames.BODY);
    wingR_r1 = bb_main.getChild(EntityModelPartNames.RIGHT_WING);
    wingL_r1 = bb_main.getChild(EntityModelPartNames.LEFT_WING);
    abdomen_r1 = bb_main.getChild("abdomen_r1");
  }

  @Override
  public void setAngles(LivingEntityRenderState state) {
    wingR_r1.roll = MathHelper.sin(state.age * 2F) * 0.4F + 0.3927F;
    wingL_r1.roll = -wingR_r1.roll;
    head_pivot.pitch = MathHelper.sin(state.age * 0.03F) * 0.1F;
    tendril_r_r1.roll = MathHelper.sin(state.age * 0.07F) * 0.2F + 0.3927F;
    tendril_r_r2.roll = -tendril_r_r1.roll;
    abdomen_r1.pitch = MathHelper.sin(state.age * 0.05F) * 0.1F - 0.3927F;
    legsR.roll = MathHelper.sin(state.age * 0.07F) * 0.1F - 0.6545F;
    legsL.roll = -legsR.roll;
  }
}
