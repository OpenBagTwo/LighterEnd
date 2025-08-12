// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

package io.github.openbagtwo.lighterend.mobs.models;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.math.MathHelper;

public class CrabModel extends EntityModel<LivingEntityRenderState> {

  private final ModelPart body;
  private final ModelPart righthind;
  private final ModelPart lefthind;
  private final ModelPart leftfore;
  private final ModelPart rightfore;
  private final ModelPart leftpincer;
  private final ModelPart rightpincer;

  public CrabModel(ModelPart root) {
    super(root);
    this.body = root.getChild("body");
    this.righthind = this.body.getChild("righthind");
    this.lefthind = this.body.getChild("lefthind");
    this.leftfore = this.body.getChild("leftfore");
    this.rightfore = this.body.getChild("rightfore");
    this.leftpincer = this.body.getChild("leftpincer");
    this.rightpincer = this.body.getChild("rightpincer");
  }

  public static TexturedModelData getTexturedModelData() {
    ModelData modelData = new ModelData();
    ModelPartData modelPartData = modelData.getRoot();
    ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0)
            .cuboid(-8.0F, -11.9F, 4.0F, 24.0F, 9.0F, 16.0F, new Dilation(0.0F))
            .uv(0, 59).cuboid(-4.0F, -11.0F, 20.0F, 16.0F, 8.0F, 8.0F, new Dilation(0.0F)),
        ModelTransform.origin(-4.0F, 23.0F, -10.0F));

    ModelPartData face_r1 = body.addChild("face_r1", ModelPartBuilder.create().uv(0, 51)
            .cuboid(-12.0F, 0.0F, 0.0F, 24.0F, 0.0F, 7.0F, new Dilation(0.0F)),
        ModelTransform.of(4.0F, -11.0F, 5.0F, 2.1817F, 0.0F, 0.0F));

    ModelPartData righthind = body.addChild("righthind", ModelPartBuilder.create().uv(0, 76)
            .cuboid(-12.0F, -2.0F, -2.1F, 14.0F, 4.0F, 4.0F, new Dilation(0.0F))
            .uv(81, 9).cuboid(-12.0F, 2.0F, -2.1F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)),
        ModelTransform.origin(-8.0F, -5.0F, 18.0F));

    ModelPartData lefthind = body.addChild("lefthind", ModelPartBuilder.create().uv(37, 77)
            .cuboid(-2.0F, -2.0F, -2.1F, 14.0F, 4.0F, 4.0F, new Dilation(0.0F))
            .uv(0, 85).cuboid(8.0F, 2.0F, -2.1F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)),
        ModelTransform.origin(16.0F, -5.0F, 18.0F));

    ModelPartData leftfore = body.addChild("leftfore", ModelPartBuilder.create().uv(49, 59)
            .cuboid(-2.0F, -2.0F, -2.0F, 14.0F, 4.0F, 4.0F, new Dilation(0.0F))
            .uv(74, 77).cuboid(8.0F, 2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)),
        ModelTransform.origin(16.0F, -5.0F, 10.0F));

    ModelPartData rightfore = body.addChild("rightfore", ModelPartBuilder.create().uv(49, 68)
            .cuboid(-12.0F, -2.0F, -2.0F, 14.0F, 4.0F, 4.0F, new Dilation(0.0F))
            .uv(81, 0).cuboid(-12.0F, 2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)),
        ModelTransform.origin(-8.0F, -5.0F, 10.0F));

    ModelPartData leftpincer = body.addChild("leftpincer", ModelPartBuilder.create().uv(49, 26)
            .cuboid(-4.0F, -1.0F, -16.0F, 8.0F, 8.0F, 16.0F, new Dilation(0.0F)),
        ModelTransform.origin(12.0F, -6.0F, 4.0F));

    ModelPartData rightpincer = body.addChild("rightpincer", ModelPartBuilder.create().uv(0, 26)
            .cuboid(-4.0F, -2.0F, -16.0F, 8.0F, 8.0F, 16.0F, new Dilation(0.0F)),
        ModelTransform.origin(-4.0F, -5.0F, 4.0F));
    return TexturedModelData.of(modelData, 128, 128);
  }

  @Override
  public void setAngles(LivingEntityRenderState state) {
    super.setAngles(state);
    float f = state.limbSwingAnimationProgress * 4;
    float g = state.limbSwingAmplitude * 6;
    float h = -(MathHelper.cos(f * 2.0F + 0.0F) * 0.4F) * g;
    float i = -(MathHelper.cos(f * 2.0F + (float) Math.PI) * 0.4F) * g;
    float j = -(MathHelper.cos(f * 2.0F + (float) (Math.PI / 2)) * 0.4F) * g;

    leftfore.yaw += h;
    lefthind.yaw += i;
    rightfore.yaw -= i;
    righthind.yaw -= j;
  }
}
