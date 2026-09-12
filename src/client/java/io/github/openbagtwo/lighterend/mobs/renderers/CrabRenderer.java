package io.github.openbagtwo.lighterend.mobs.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.mobs.ChorusCrab;
import io.github.openbagtwo.lighterend.mobs.EntityModels;
import io.github.openbagtwo.lighterend.mobs.models.CrabModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class CrabRenderer extends
    AgeableMobRenderer<ChorusCrab, HoldingEntityRenderState, CrabModel> {

  private static final Identifier TEXTURE = LighterEnd.of("textures/entity/chorus_crab.png");

  public CrabRenderer(EntityRendererProvider.Context ctx) {
    super(
        ctx,
        new CrabModel(ctx.bakeLayer(EntityModels.CRAB_MODEL)),
        new CrabModel(ctx.bakeLayer(EntityModels.CRAB_BABY)),
        1.2f
    );
    this.addLayer(new HeldItemRenderer(this));
  }

  @Override
  public HoldingEntityRenderState createRenderState() {
    return new HoldingEntityRenderState();
  }

  @Override
  public Identifier getTextureLocation(HoldingEntityRenderState state) {
    return TEXTURE;
  }

  @Override
  public void extractRenderState(ChorusCrab crab, HoldingEntityRenderState state, float f) {
    super.extractRenderState(crab, state, f);
    HoldingEntityRenderState.extractHoldingEntityRenderState(crab, state, this.itemModelResolver);
  }

  public static class HeldItemRenderer extends
      RenderLayer<HoldingEntityRenderState, CrabModel> {

    public HeldItemRenderer(
        RenderLayerParent<HoldingEntityRenderState, CrabModel> context) {
      super(context);
    }

    @Override
    public void submit(
        PoseStack matrixStack,
        SubmitNodeCollector orderedRenderCommandQueue,
        int i,
        HoldingEntityRenderState state,
        float f,
        float g
    ) {
      ItemStackRenderState itemRenderState = state.heldItem;
      if (!itemRenderState.isEmpty()) {
        matrixStack.pushPose();
        matrixStack.translate(this.getParentModel().pincer_left.x / 16.0F,
            this.getParentModel().pincer_left.y / 16.0F,
            this.getParentModel().pincer_left.z / 16.0F);
        if (state.isBaby) {
          matrixStack.scale(1F, 1F, 1F);
          matrixStack.translate(0.1F, 1.4F, 0.55F);
        } else {
          matrixStack.scale(2F, 2F, 2F);
          matrixStack.translate(0.23F, 0.65F, -0.03F);
        }

        matrixStack.rotate(Axis.YN.rotationDegrees(160F));
        matrixStack.rotate(Axis.ZP.rotationDegrees(135F));

        itemRenderState.submit(
            matrixStack,
            orderedRenderCommandQueue,
            i,
            OverlayTexture.NO_OVERLAY,
            state.outlineColor
        );
        matrixStack.popPose();
      }
    }
  }
}
