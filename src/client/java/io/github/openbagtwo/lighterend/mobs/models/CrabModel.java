// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

package io.github.openbagtwo.lighterend.mobs.models;

import java.util.Set;
import net.minecraft.client.model.BabyModelTransform;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.MeshTransformer;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

public class CrabModel extends EntityModel<LivingEntityRenderState> {

  public static final MeshTransformer BABY_TRANSFORMER = new BabyModelTransform(
      false,
      10.0F,
      0,
      Set.of()
  );

  private final ModelPart body;
  private final ModelPart stalk_left;
  private final ModelPart stalk_right;
  private final ModelPart claw_left;
  public final ModelPart pincer_left;
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

  public static LayerDefinition getTexturedModelData() {
    MeshDefinition modelData = new MeshDefinition();
    PartDefinition modelPartData = modelData.getRoot();
    PartDefinition body = modelPartData.addOrReplaceChild("body", CubeListBuilder.create(),
        PartPose.offset(5.0F, 18.0F, 6.0F));

    PartDefinition abdomen_r1 = body.addOrReplaceChild("abdomen_r1",
        CubeListBuilder.create().texOffs(0, 46)
            .addBox(-4.0F, -13.0F, -4.0F, 12.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(-7.0F, 7.0F, 10.0F, -0.3054F, 0.0F, 0.0F));

    PartDefinition thorax_r1 = body.addOrReplaceChild("thorax_r1",
        CubeListBuilder.create().texOffs(0, 0)
            .addBox(-8.0F, -14.0F, -4.0F, 16.0F, 10.0F, 17.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(-5.0F, 2.0F, -6.0F, -0.3054F, 0.0F, 0.0F));

    PartDefinition stalk_left = body.addOrReplaceChild("stalk_left", CubeListBuilder.create(),
        PartPose.offset(-1.4F, -11.4F, -5.25F));

    PartDefinition cube_r1 = stalk_left.addOrReplaceChild("cube_r1",
        CubeListBuilder.create().texOffs(84, 35)
            .addBox(-1.0F, -9.0F, 0.0F, 2.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(-0.6F, 0.4F, -0.25F, 0.1745F, -0.0175F, 0.1222F));

    PartDefinition stalk_right = body.addOrReplaceChild("stalk_right", CubeListBuilder.create(),
        PartPose.offset(-8.6F, -11.4F, -5.25F));

    PartDefinition cube_r2 = stalk_right.addOrReplaceChild("cube_r2",
        CubeListBuilder.create().texOffs(38, 84)
            .addBox(-1.0F, -9.0F, 0.0F, 2.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(0.6F, 0.4F, -0.25F, 0.1745F, 0.0175F, -0.1222F));

    PartDefinition claw_left = body.addOrReplaceChild("claw_left", CubeListBuilder.create(),
        PartPose.offset(-2.5F, -4.5F, -6.0F));

    PartDefinition cube_r3 = claw_left.addOrReplaceChild("cube_r3",
        CubeListBuilder.create().texOffs(42, 27)
            .addBox(-7.0F, -4.0F, -12.0F, 7.0F, 5.0F, 14.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(12.5F, -0.5F, -9.0F, 0.1309F, 0.4363F, 0.0F));

    PartDefinition cube_r4 = claw_left.addOrReplaceChild("cube_r4",
        CubeListBuilder.create().texOffs(66, 6)
            .addBox(15.0F, -11.0F, 3.0F, 12.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(-12.5F, 11.5F, 8.0F, 0.0F, 0.7505F, -0.1745F));

    PartDefinition pincer_left = claw_left.addOrReplaceChild("pincer_left",
        CubeListBuilder.create(),
        PartPose.offset(4.5F, -1.5F, -18.0F));

    PartDefinition cube_r5 = pincer_left.addOrReplaceChild("cube_r5",
        CubeListBuilder.create().texOffs(76, 46)
            .addBox(0.5F, -3.0F, -3.0F, 6.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(-3.0F, 3.0F, 2.0F, 0.4363F, 0.48F, 0.0F));

    PartDefinition claw_right = body.addOrReplaceChild("claw_right", CubeListBuilder.create(),
        PartPose.offset(-7.5F, -4.5F, -6.0F));

    PartDefinition cube_r6 = claw_right.addOrReplaceChild("cube_r6",
        CubeListBuilder.create().texOffs(0, 27)
            .addBox(-7.0F, -4.0F, -12.0F, 7.0F, 5.0F, 14.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(-6.5F, -0.5F, -6.0F, 0.1309F, -0.4363F, 0.0F));

    PartDefinition cube_r7 = claw_right.addOrReplaceChild("cube_r7",
        CubeListBuilder.create().texOffs(66, 0)
            .addBox(-13.0F, -12.0F, -5.0F, 12.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(-3.5F, 9.5F, 4.0F, 0.0F, -0.7505F, 0.1745F));

    PartDefinition pincer_right = claw_right.addOrReplaceChild("pincer_right",
        CubeListBuilder.create(),
        PartPose.offset(-4.5F, -1.5F, -18.0F));

    PartDefinition cube_r8 = pincer_right.addOrReplaceChild("cube_r8",
        CubeListBuilder.create().texOffs(38, 73)
            .addBox(0.0F, -3.0F, -3.0F, 6.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(-3.0F, 3.0F, -1.0F, 0.4363F, -0.48F, 0.0F));

    PartDefinition leg_left_front = body.addOrReplaceChild("leg_left_front",
        CubeListBuilder.create(),
        PartPose.offset(1.0F, -5.0F, -5.0F));

    PartDefinition cube_r9 = leg_left_front.addOrReplaceChild("cube_r9",
        CubeListBuilder.create().texOffs(26, 73)
            .addBox(-2.9074F, -5.6014F, 0.0F, 3.0F, 17.0F, 3.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(14.0F, -0.75F, -4.0F, 0.0F, 0.192F, -0.2967F));

    PartDefinition cube_r10 = leg_left_front.addOrReplaceChild("cube_r10",
        CubeListBuilder.create().texOffs(36, 55)
            .addBox(-14.0F, -4.0F, 0.0F, 15.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(13.25F, -2.75F, -5.0F, -0.0698F, 0.192F, -0.3491F));

    PartDefinition leg_left_mid = body.addOrReplaceChild("leg_left_mid", CubeListBuilder.create(),
        PartPose.offset(0.0F, -3.0F, 1.0F));

    PartDefinition cube_r11 = leg_left_mid.addOrReplaceChild("cube_r11",
        CubeListBuilder.create().texOffs(58, 73)
            .addBox(-3.0F, -6.5F, 0.0F, 3.0F, 14.0F, 3.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(13.25F, 1.0F, -2.0F, 0.0F, 0.0F, -0.3491F));

    PartDefinition cube_r12 = leg_left_mid.addOrReplaceChild("cube_r12",
        CubeListBuilder.create().texOffs(0, 61)
            .addBox(-13.75F, -4.75F, 0.0F, 13.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(14.0F, -2.0F, -3.0F, 0.0F, 0.0F, -0.3491F));

    PartDefinition leg_left_rear = body.addOrReplaceChild("leg_left_rear", CubeListBuilder.create(),
        PartPose.offset(3.0F, -3.0F, 5.0F));

    PartDefinition cube_r13 = leg_left_rear.addOrReplaceChild("cube_r13",
        CubeListBuilder.create().texOffs(84, 21)
            .addBox(-3.0F, -3.5F, 0.0F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(8.25F, 1.0F, 1.0F, 0.0F, -0.3491F, -0.3491F));

    PartDefinition cube_r14 = leg_left_rear.addOrReplaceChild("cube_r14",
        CubeListBuilder.create().texOffs(0, 70)
            .addBox(-13.75F, -4.75F, 0.0F, 8.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(14.0F, -2.0F, 1.5F, 0.0F, -0.2618F, -0.3491F));

    PartDefinition leg_right_front = body.addOrReplaceChild("leg_right_front",
        CubeListBuilder.create(),
        PartPose.offset(-11.0F, -5.0F, -5.0F));

    PartDefinition cube_r15 = leg_right_front.addOrReplaceChild("cube_r15",
        CubeListBuilder.create().texOffs(72, 64)
            .addBox(-0.8264F, -5.0F, 0.9848F, 3.0F, 17.0F, 3.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(-12.75F, -1.0F, -5.0F, 0.0F, -0.192F, 0.2967F));

    PartDefinition cube_r16 = leg_right_front.addOrReplaceChild("cube_r16",
        CubeListBuilder.create().texOffs(36, 46)
            .addBox(-14.0F, -4.0F, 0.0F, 15.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(-0.75F, 2.0F, -2.25F, 0.0F, -0.192F, 0.3491F));

    PartDefinition leg_right_mid = body.addOrReplaceChild("leg_right_mid", CubeListBuilder.create(),
        PartPose.offset(-11.0F, -4.0F, 1.0F));

    PartDefinition cube_r17 = leg_right_mid.addOrReplaceChild("cube_r17",
        CubeListBuilder.create().texOffs(0, 79)
            .addBox(1.0F, -7.0F, 0.0F, 3.0F, 14.0F, 3.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(-15.0F, 2.0F, -2.0F, 0.0F, 0.0F, 0.3491F));

    PartDefinition cube_r18 = leg_right_mid.addOrReplaceChild("cube_r18",
        CubeListBuilder.create().texOffs(36, 64)
            .addBox(-12.0F, -1.0F, 0.0F, 13.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(-0.75F, -1.0F, -3.0F, 0.0F, 0.0F, 0.3491F));

    PartDefinition leg_right_rear = body.addOrReplaceChild("leg_right_rear",
        CubeListBuilder.create(),
        PartPose.offset(-13.0F, -3.0F, 5.0F));

    PartDefinition cube_r19 = leg_right_rear.addOrReplaceChild("cube_r19",
        CubeListBuilder.create().texOffs(12, 79)
            .addBox(1.0F, -7.0F, 0.0F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(-10.0F, 4.0F, 1.0F, 0.0F, 0.3491F, 0.3491F));

    PartDefinition cube_r20 = leg_right_rear.addOrReplaceChild("cube_r20",
        CubeListBuilder.create().texOffs(66, 12)
            .addBox(-7.0F, -1.0F, 0.0F, 8.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)),
        PartPose.offsetAndRotation(-0.75F, -1.0F, -2.0F, 0.0F, 0.2618F, 0.3491F));
    return LayerDefinition.create(modelData, 128, 128);
  }

  @Override
  public void setupAnim(LivingEntityRenderState state) {
    super.setupAnim(state);
    float f = state.walkAnimationPos * 4;
    float g = state.walkAnimationSpeed * 3;

    float[] angles = new float[4];

    for (int i = 0; i < 4; i++) {
      angles[i] = -(Mth.cos(f * 2.0F + (float) (i * Math.PI / 2)) * 0.4F) * g;
    }

    leg_right_front.yRot += angles[0];
    leg_right_front.zRot += angles[0];

    leg_right_mid.yRot += angles[1];
    leg_right_mid.zRot += angles[1];
    leg_left_front.yRot += angles[1];
    leg_left_front.zRot += angles[1];

    leg_right_rear.yRot += angles[2];
    leg_right_rear.zRot += angles[2];
    leg_left_mid.yRot += angles[2];
    leg_left_mid.zRot += angles[2];

    leg_left_rear.yRot += angles[3];
    leg_left_rear.zRot += angles[3];
  }
}
