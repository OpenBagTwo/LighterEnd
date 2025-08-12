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

  public CrabModel(ModelPart root) {
    super(root);
    this.body = root.getChild("body");
    this.righthind = this.body.getChild("righthind");
    this.lefthind = this.body.getChild("lefthind");
    this.leftfore = this.body.getChild("leftfore");
    this.rightfore = this.body.getChild("rightfore");
  }

  public static TexturedModelData getTexturedModelData() {
    ModelData modelData = new ModelData();
    ModelPartData modelPartData = modelData.getRoot();
    ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0)
            .cuboid(-8.0F, -11.0F, 4.0F, 24.0F, 8.0F, 16.0F, new Dilation(0.0F))
            .uv(48, 31).cuboid(-4.0F, -11.0F, 20.0F, 16.0F, 8.0F, 8.0F, new Dilation(0.0F))
            .uv(0, 24).cuboid(-8.0F, -7.0F, -12.0F, 8.0F, 8.0F, 16.0F, new Dilation(0.0F))
            .uv(0, 48).cuboid(8.0F, -7.0F, -12.0F, 8.0F, 8.0F, 16.0F, new Dilation(0.0F)),
        ModelTransform.origin(-4.0F, 23.0F, -10.0F));

    ModelPartData face_r1 = body.addChild("face_r1", ModelPartBuilder.create().uv(48, 24)
            .cuboid(-12.0F, 0.0F, 0.0F, 24.0F, 0.0F, 7.0F, new Dilation(0.0F)),
        ModelTransform.of(4.0F, -11.0F, 5.0F, 2.1817F, 0.0F, 0.0F));

    ModelPartData righthind = body.addChild("righthind", ModelPartBuilder.create().uv(48, 63)
            .cuboid(-12.0F, -2.0F, -2.1F, 14.0F, 4.0F, 4.0F, new Dilation(0.0F))
            .uv(32, 72).cuboid(-12.0F, 2.0F, -2.1F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)),
        ModelTransform.origin(-8.0F, -5.0F, 18.0F));

    ModelPartData lefthind = body.addChild("lefthind", ModelPartBuilder.create().uv(48, 71)
            .cuboid(-2.0F, -2.0F, -2.1F, 14.0F, 4.0F, 4.0F, new Dilation(0.0F))
            .uv(48, 79).cuboid(8.0F, 2.0F, -2.1F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)),
        ModelTransform.origin(16.0F, -5.0F, 18.0F));

    ModelPartData leftfore = body.addChild("leftfore", ModelPartBuilder.create().uv(48, 47)
            .cuboid(-2.0F, -2.0F, -2.0F, 14.0F, 4.0F, 4.0F, new Dilation(0.0F))
            .uv(0, 72).cuboid(8.0F, 2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)),
        ModelTransform.origin(16.0F, -5.0F, 10.0F));

    ModelPartData rightfore = body.addChild("rightfore", ModelPartBuilder.create().uv(48, 55)
            .cuboid(-12.0F, -2.0F, -8.0F, 14.0F, 4.0F, 4.0F, new Dilation(0.0F))
            .uv(16, 72).cuboid(-12.0F, 2.0F, -8.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)),
        ModelTransform.origin(-8.0F, -5.0F, 16.0F));
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
