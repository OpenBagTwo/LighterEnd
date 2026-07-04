package io.github.openbagtwo.lighterend.mobs.renderers;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.mobs.ChorusCrab;
import io.github.openbagtwo.lighterend.mobs.EntityModels;
import io.github.openbagtwo.lighterend.mobs.models.CrabModel;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.AgeableMobEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.state.ItemHolderEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class CrabRenderer extends
    AgeableMobEntityRenderer<ChorusCrab, ItemHolderEntityRenderState, CrabModel> {

  private static final Identifier TEXTURE = LighterEnd.of("textures/entity/chorus_crab.png");

  public CrabRenderer(EntityRendererFactory.Context ctx) {
    super(
        ctx,
        new CrabModel(ctx.getPart(EntityModels.CRAB_MODEL)),
        new CrabModel(ctx.getPart(EntityModels.CRAB_BABY)),
        1.2f
    );
    this.addFeature(new HeldItemRenderer(this));
  }

  @Override
  public ItemHolderEntityRenderState createRenderState() {
    return new ItemHolderEntityRenderState();
  }

  @Override
  public Identifier getTexture(ItemHolderEntityRenderState state) {
    return TEXTURE;
  }

  @Override
  public void updateRenderState(ChorusCrab crab, ItemHolderEntityRenderState state, float f) {
    super.updateRenderState(crab, state, f);
    ItemHolderEntityRenderState.update(crab, state, this.itemModelResolver);
  }

  public static class HeldItemRenderer extends
      FeatureRenderer<ItemHolderEntityRenderState, CrabModel> {

    public HeldItemRenderer(
        FeatureRendererContext<ItemHolderEntityRenderState, CrabModel> context) {
      super(context);
    }

    public void render(
        MatrixStack matrixStack,
        VertexConsumerProvider vertexConsumerProvider,
        int i,
        ItemHolderEntityRenderState state,
        float f,
        float g
    ) {
      ItemRenderState itemRenderState = state.itemRenderState;
      if (!itemRenderState.isEmpty()) {
        matrixStack.push();
        matrixStack.translate(this.getContextModel().pincer_left.originX / 16.0F,
            this.getContextModel().pincer_left.originY / 16.0F,
            this.getContextModel().pincer_left.originZ / 16.0F);
        if (state.baby) {
          matrixStack.scale(1F, 1F, 1F);
          matrixStack.translate(0.1F, 1.4F, 0.55F);
        } else {
          matrixStack.scale(2F, 2F, 2F);
          matrixStack.translate(0.23F, 0.65F, -0.03F);
        }

        matrixStack.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(160F));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(135F));

        itemRenderState.render(matrixStack, vertexConsumerProvider, i, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
      }
    }
  }
}
