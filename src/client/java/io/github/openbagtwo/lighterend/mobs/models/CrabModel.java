// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

package io.github.openbagtwo.lighterend.mobs.models;

import java.util.Set;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.BabyModelTransformer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelTransformer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.math.MathHelper;

public class CrabModel extends EntityModel<LivingEntityRenderState> {

  public static final ModelTransformer BABY_TRANSFORMER = new BabyModelTransformer(
      false,
      10.0F,
      0,
      Set.of()
  );

  private final ModelPart body;
  private final ModelPart stalk_left;
  private final ModelPart stalk_right;
  private final ModelPart claw_left;
  private final ModelPart pincer_left;
  private final ModelPart claw_right;
  private final ModelPart pincer_right;
  private final ModelPart leg_left_front;
  private final ModelPart leg_left_mid;
  private final ModelPart leg_left_rear;
  private final ModelPart leg_right_front;
  private final ModelPart leg_right_mid;
  private final ModelPart leg_right_rear;

  public CrabModel(ModelPart root) {
    super(root);
    this.body = root.getChild("body");
    this.stalk_left = this.body.getChild("stalk_left");
    this.stalk_right = this.body.getChild("stalk_right");
    this.claw_left = this.body.getChild("claw_left");
    this.pincer_left = this.claw_left.getChild("pincer_left");
    this.claw_right = this.body.getChild("claw_right");
    this.pincer_right = this.claw_right.getChild("pincer_right");
    this.leg_left_front = this.body.getChild("leg_left_front");
    this.leg_left_mid = this.body.getChild("leg_left_mid");
    this.leg_left_rear = this.body.getChild("leg_left_rear");
    this.leg_right_front = this.body.getChild("leg_right_front");
    this.leg_right_mid = this.body.getChild("leg_right_mid");
    this.leg_right_rear = this.body.getChild("leg_right_rear");
  }

  public static TexturedModelData getTexturedModelData() {
    ModelData modelData = new ModelData();
    ModelPartData modelPartData = modelData.getRoot();
    ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create(),
        ModelTransform.origin(5.0F, 18.0F, 6.0F));

    ModelPartData abdomen_r1 = body.addChild("abdomen_r1", ModelPartBuilder.create().uv(0, 46)
            .cuboid(-4.0F, -13.0F, -4.0F, 12.0F, 9.0F, 6.0F, new Dilation(0.0F)),
        ModelTransform.of(-7.0F, 7.0F, 10.0F, -0.3054F, 0.0F, 0.0F));

    ModelPartData thorax_r1 = body.addChild("thorax_r1", ModelPartBuilder.create().uv(0, 0)
            .cuboid(-8.0F, -14.0F, -4.0F, 16.0F, 10.0F, 17.0F, new Dilation(0.0F)),
        ModelTransform.of(-5.0F, 2.0F, -6.0F, -0.3054F, 0.0F, 0.0F));

    ModelPartData stalk_left = body.addChild("stalk_left", ModelPartBuilder.create(),
        ModelTransform.origin(-1.4F, -11.4F, -5.25F));

    ModelPartData cube_r1 = stalk_left.addChild("cube_r1", ModelPartBuilder.create().uv(84, 35)
            .cuboid(-1.0F, -9.0F, 0.0F, 2.0F, 9.0F, 0.0F, new Dilation(0.0F)),
        ModelTransform.of(-0.6F, 0.4F, -0.25F, 0.1745F, -0.0175F, 0.1222F));

    ModelPartData stalk_right = body.addChild("stalk_right", ModelPartBuilder.create(),
        ModelTransform.origin(-8.6F, -11.4F, -5.25F));

    ModelPartData cube_r2 = stalk_right.addChild("cube_r2", ModelPartBuilder.create().uv(38, 84)
            .cuboid(-1.0F, -9.0F, 0.0F, 2.0F, 9.0F, 0.0F, new Dilation(0.0F)),
        ModelTransform.of(0.6F, 0.4F, -0.25F, 0.1745F, 0.0175F, -0.1222F));

    ModelPartData claw_left = body.addChild("claw_left", ModelPartBuilder.create(),
        ModelTransform.origin(-2.5F, -4.5F, -6.0F));

    ModelPartData cube_r3 = claw_left.addChild("cube_r3", ModelPartBuilder.create().uv(42, 27)
            .cuboid(-7.0F, -4.0F, -12.0F, 7.0F, 5.0F, 14.0F, new Dilation(0.0F)),
        ModelTransform.of(12.5F, -0.5F, -9.0F, 0.1309F, 0.4363F, 0.0F));

    ModelPartData cube_r4 = claw_left.addChild("cube_r4", ModelPartBuilder.create().uv(66, 6)
            .cuboid(15.0F, -11.0F, 3.0F, 12.0F, 3.0F, 3.0F, new Dilation(0.0F)),
        ModelTransform.of(-12.5F, 11.5F, 8.0F, 0.0F, 0.7505F, -0.1745F));

    ModelPartData pincer_left = claw_left.addChild("pincer_left", ModelPartBuilder.create(),
        ModelTransform.origin(4.5F, -1.5F, -18.0F));

    ModelPartData cube_r5 = pincer_left.addChild("cube_r5", ModelPartBuilder.create().uv(76, 46)
            .cuboid(0.5F, -3.0F, -3.0F, 6.0F, 7.0F, 4.0F, new Dilation(0.0F)),
        ModelTransform.of(-3.0F, 3.0F, 2.0F, 0.4363F, 0.48F, 0.0F));

    ModelPartData claw_right = body.addChild("claw_right", ModelPartBuilder.create(),
        ModelTransform.origin(-7.5F, -4.5F, -6.0F));

    ModelPartData cube_r6 = claw_right.addChild("cube_r6", ModelPartBuilder.create().uv(0, 27)
            .cuboid(-7.0F, -4.0F, -12.0F, 7.0F, 5.0F, 14.0F, new Dilation(0.0F)),
        ModelTransform.of(-6.5F, -0.5F, -6.0F, 0.1309F, -0.4363F, 0.0F));

    ModelPartData cube_r7 = claw_right.addChild("cube_r7", ModelPartBuilder.create().uv(66, 0)
            .cuboid(-13.0F, -12.0F, -5.0F, 12.0F, 3.0F, 3.0F, new Dilation(0.0F)),
        ModelTransform.of(-3.5F, 9.5F, 4.0F, 0.0F, -0.7505F, 0.1745F));

    ModelPartData pincer_right = claw_right.addChild("pincer_right", ModelPartBuilder.create(),
        ModelTransform.origin(-4.5F, -1.5F, -18.0F));

    ModelPartData cube_r8 = pincer_right.addChild("cube_r8", ModelPartBuilder.create().uv(38, 73)
            .cuboid(0.0F, -3.0F, -3.0F, 6.0F, 7.0F, 4.0F, new Dilation(0.0F)),
        ModelTransform.of(-3.0F, 3.0F, -1.0F, 0.4363F, -0.48F, 0.0F));

    ModelPartData leg_left_front = body.addChild("leg_left_front", ModelPartBuilder.create(),
        ModelTransform.origin(1.0F, -5.0F, -5.0F));

    ModelPartData cube_r9 = leg_left_front.addChild("cube_r9", ModelPartBuilder.create().uv(26, 73)
            .cuboid(-2.9074F, -5.6014F, 0.0F, 3.0F, 17.0F, 3.0F, new Dilation(0.0F)),
        ModelTransform.of(14.0F, -0.75F, -4.0F, 0.0F, 0.192F, -0.2967F));

    ModelPartData cube_r10 = leg_left_front.addChild("cube_r10",
        ModelPartBuilder.create().uv(36, 55)
            .cuboid(-14.0F, -4.0F, 0.0F, 15.0F, 4.0F, 5.0F, new Dilation(0.0F)),
        ModelTransform.of(13.25F, -2.75F, -5.0F, -0.0698F, 0.192F, -0.3491F));

    ModelPartData leg_left_mid = body.addChild("leg_left_mid", ModelPartBuilder.create(),
        ModelTransform.origin(0.0F, -3.0F, 1.0F));

    ModelPartData cube_r11 = leg_left_mid.addChild("cube_r11", ModelPartBuilder.create().uv(58, 73)
            .cuboid(-3.0F, -6.5F, 0.0F, 3.0F, 14.0F, 3.0F, new Dilation(0.0F)),
        ModelTransform.of(13.25F, 1.0F, -2.0F, 0.0F, 0.0F, -0.3491F));

    ModelPartData cube_r12 = leg_left_mid.addChild("cube_r12", ModelPartBuilder.create().uv(0, 61)
            .cuboid(-13.75F, -4.75F, 0.0F, 13.0F, 4.0F, 5.0F, new Dilation(0.0F)),
        ModelTransform.of(14.0F, -2.0F, -3.0F, 0.0F, 0.0F, -0.3491F));

    ModelPartData leg_left_rear = body.addChild("leg_left_rear", ModelPartBuilder.create(),
        ModelTransform.origin(3.0F, -3.0F, 5.0F));

    ModelPartData cube_r13 = leg_left_rear.addChild("cube_r13", ModelPartBuilder.create().uv(84, 21)
            .cuboid(-3.0F, -3.5F, 0.0F, 3.0F, 11.0F, 3.0F, new Dilation(0.0F)),
        ModelTransform.of(8.25F, 1.0F, 1.0F, 0.0F, -0.3491F, -0.3491F));

    ModelPartData cube_r14 = leg_left_rear.addChild("cube_r14", ModelPartBuilder.create().uv(0, 70)
            .cuboid(-13.75F, -4.75F, 0.0F, 8.0F, 4.0F, 5.0F, new Dilation(0.0F)),
        ModelTransform.of(14.0F, -2.0F, 1.5F, 0.0F, -0.2618F, -0.3491F));

    ModelPartData leg_right_front = body.addChild("leg_right_front", ModelPartBuilder.create(),
        ModelTransform.origin(-11.0F, -5.0F, -5.0F));

    ModelPartData cube_r15 = leg_right_front.addChild("cube_r15",
        ModelPartBuilder.create().uv(72, 64)
            .cuboid(-0.8264F, -5.0F, 0.9848F, 3.0F, 17.0F, 3.0F, new Dilation(0.0F)),
        ModelTransform.of(-12.75F, -1.0F, -5.0F, 0.0F, -0.192F, 0.2967F));

    ModelPartData cube_r16 = leg_right_front.addChild("cube_r16",
        ModelPartBuilder.create().uv(36, 46)
            .cuboid(-14.0F, -4.0F, 0.0F, 15.0F, 4.0F, 5.0F, new Dilation(0.0F)),
        ModelTransform.of(-0.75F, 2.0F, -2.25F, 0.0F, -0.192F, 0.3491F));

    ModelPartData leg_right_mid = body.addChild("leg_right_mid", ModelPartBuilder.create(),
        ModelTransform.origin(-11.0F, -4.0F, 1.0F));

    ModelPartData cube_r17 = leg_right_mid.addChild("cube_r17", ModelPartBuilder.create().uv(0, 79)
            .cuboid(1.0F, -7.0F, 0.0F, 3.0F, 14.0F, 3.0F, new Dilation(0.0F)),
        ModelTransform.of(-15.0F, 2.0F, -2.0F, 0.0F, 0.0F, 0.3491F));

    ModelPartData cube_r18 = leg_right_mid.addChild("cube_r18", ModelPartBuilder.create().uv(36, 64)
            .cuboid(-12.0F, -1.0F, 0.0F, 13.0F, 4.0F, 5.0F, new Dilation(0.0F)),
        ModelTransform.of(-0.75F, -1.0F, -3.0F, 0.0F, 0.0F, 0.3491F));

    ModelPartData leg_right_rear = body.addChild("leg_right_rear", ModelPartBuilder.create(),
        ModelTransform.origin(-13.0F, -3.0F, 5.0F));

    ModelPartData cube_r19 = leg_right_rear.addChild("cube_r19",
        ModelPartBuilder.create().uv(12, 79)
            .cuboid(1.0F, -7.0F, 0.0F, 3.0F, 11.0F, 3.0F, new Dilation(0.0F)),
        ModelTransform.of(-10.0F, 4.0F, 1.0F, 0.0F, 0.3491F, 0.3491F));

    ModelPartData cube_r20 = leg_right_rear.addChild("cube_r20",
        ModelPartBuilder.create().uv(66, 12)
            .cuboid(-7.0F, -1.0F, 0.0F, 8.0F, 4.0F, 5.0F, new Dilation(0.0F)),
        ModelTransform.of(-0.75F, -1.0F, -2.0F, 0.0F, 0.2618F, 0.3491F));
    return TexturedModelData.of(modelData, 128, 128);
  }

  @Override
  public void setAngles(LivingEntityRenderState state) {
    super.setAngles(state);
    float f = state.limbSwingAnimationProgress * 4;
    float g = state.limbSwingAmplitude * 3;

    float[] angles = new float[4];

    for (int i = 0; i < 4; i++) {
      angles[i] = -(MathHelper.cos(f * 2.0F + (float) (i * Math.PI / 2)) * 0.4F) * g;
    }

    leg_right_front.yaw += angles[0];
    leg_right_front.roll += angles[0];

    leg_right_mid.yaw += angles[1];
    leg_right_mid.roll += angles[1];
    leg_left_front.yaw += angles[1];
    leg_left_front.roll += angles[1];

    leg_right_rear.yaw += angles[2];
    leg_right_rear.roll += angles[2];
    leg_left_mid.yaw += angles[2];
    leg_left_mid.roll += angles[2];

    leg_left_rear.yaw += angles[3];
    leg_left_rear.roll += angles[3];
  }
}
