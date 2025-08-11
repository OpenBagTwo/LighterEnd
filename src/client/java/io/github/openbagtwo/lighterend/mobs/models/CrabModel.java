// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

package io.github.openbagtwo.lighterend.mobs.models;

import io.github.openbagtwo.lighterend.mobs.states.CrabRenderState;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModel;

public class CrabModel extends EntityModel<CrabRenderState> {

  private final ModelPart body;
  private final ModelPart righthind;
  private final ModelPart rightfore;
  private final ModelPart leftfore;
  private final ModelPart lefthind;
  private final ModelPart bb_main;

  public CrabModel(ModelPart root) {
    super(root);
    this.body = root.getChild("body");
    this.righthind = root.getChild("righthind");
    this.rightfore = root.getChild("rightfore");
    this.leftfore = root.getChild("leftfore");
    this.lefthind = root.getChild("lefthind");
    this.bb_main = root.getChild("bb_main");
  }

  public static TexturedModelData getTexturedModelData() {
    ModelData modelData = new ModelData();
    ModelPartData modelPartData = modelData.getRoot();
    ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0)
            .cuboid(-8.0F, -11.0F, 4.0F, 24.0F, 8.0F, 16.0F, new Dilation(0.0F))
            .uv(48, 32).cuboid(-4.0F, -11.0F, 20.0F, 16.0F, 8.0F, 8.0F, new Dilation(0.0F)),
        ModelTransform.origin(-4.0F, 23.0F, -10.0F));

    ModelPartData righthind = modelPartData.addChild("righthind",
        ModelPartBuilder.create().uv(48, 64)
            .cuboid(-12.0F, -7.0F, 0.0F, 12.0F, 4.0F, 4.0F, new Dilation(0.0F))
            .uv(64, 72).cuboid(-12.0F, -3.0F, 0.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)),
        ModelTransform.origin(-12.0F, 23.0F, 6.0F));

    ModelPartData rightfore = modelPartData.addChild("rightfore",
        ModelPartBuilder.create().uv(48, 56)
            .cuboid(-4.0F, -7.0F, -8.0F, 12.0F, 4.0F, 4.0F, new Dilation(0.0F))
            .uv(48, 72).cuboid(-4.0F, -3.0F, -8.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)),
        ModelTransform.origin(-20.0F, 23.0F, 6.0F));

    ModelPartData leftfore = modelPartData.addChild("leftfore", ModelPartBuilder.create().uv(48, 48)
            .cuboid(32.0F, -7.0F, -8.0F, 12.0F, 4.0F, 4.0F, new Dilation(0.0F))
            .uv(32, 72).cuboid(40.0F, -3.0F, -8.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)),
        ModelTransform.origin(-20.0F, 23.0F, 6.0F));

    ModelPartData lefthind = modelPartData.addChild("lefthind", ModelPartBuilder.create().uv(0, 72)
            .cuboid(24.0F, -7.0F, 0.0F, 12.0F, 4.0F, 4.0F, new Dilation(0.0F))
            .uv(0, 80).cuboid(32.0F, -3.0F, 0.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)),
        ModelTransform.origin(-12.0F, 23.0F, 6.0F));

    ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(0, 24)
            .cuboid(-12.0F, -8.0F, -22.0F, 8.0F, 8.0F, 16.0F, new Dilation(0.0F))
            .uv(0, 48).cuboid(4.0F, -8.0F, -22.0F, 8.0F, 8.0F, 16.0F, new Dilation(0.0F)),
        ModelTransform.origin(0.0F, 24.0F, 0.0F));

    ModelPartData face_r1 = bb_main.addChild("face_r1", ModelPartBuilder.create().uv(49, 25)
            .cuboid(-12.0F, 0.0F, 0.0F, 24.0F, 0.0F, 7.0F, new Dilation(0.0F)),
        ModelTransform.of(0.0F, -12.0F, -5.0F, 2.1817F, 0.0F, 0.0F));
    return TexturedModelData.of(modelData, 128, 128);
  }

  @Override
  public void setAngles(CrabRenderState renderState) {
  }
}
