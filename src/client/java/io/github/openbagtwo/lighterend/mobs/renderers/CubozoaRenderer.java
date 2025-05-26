package io.github.openbagtwo.lighterend.mobs.renderers;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.mobs.Cubozoa;
import io.github.openbagtwo.lighterend.mobs.EntityModels;
import io.github.openbagtwo.lighterend.mobs.models.CubozoaModel;
import io.github.openbagtwo.lighterend.mobs.states.CubozoaRenderState;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class CubozoaRenderer extends
    MobEntityRenderer<Cubozoa, CubozoaRenderState, CubozoaModel> {

  private static final List<Identifier> TEXTURES = Arrays.asList(
      Identifier.of(LighterEnd.MOD_ID, "textures/entity/cubozoa/cubozoa.png"),
      Identifier.of(LighterEnd.MOD_ID, "textures/entity/cubozoa/cubozoa_sulphur.png")
  );
  private static final List<RenderLayer> GLOW = Arrays.asList(
      RenderLayer.getEyes(
          Identifier.of(LighterEnd.MOD_ID, "textures/entity/cubozoa/cubozoa_glow.png")),
      RenderLayer.getEyes(
          Identifier.of(LighterEnd.MOD_ID, "extures/entity/cubozoa/cubozoa_sulphur_glow.png"))
  );

  public CubozoaRenderer(EntityRendererFactory.Context ctx) {
    super(ctx, new CubozoaModel(ctx.getPart(EntityModels.CUBOZOA_MODEL)), 0.5F);
    this.addFeature(new EyesFeatureRenderer<>(this) {
      @Override
      public RenderLayer getEyesTexture() {
        return GLOW.get(0);
      }

      @Override
      public void render(
          MatrixStack matrices,
          VertexConsumerProvider vertexConsumers,
          int light,
          CubozoaRenderState state,
          float limbAngle,
          float limbDistance
      ) {
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(GLOW.get(state.variant));
        this.getContextModel()
            .renderOverride(
                matrices,
                vertexConsumer,
                15728640,
                OverlayTexture.DEFAULT_UV,
                0xffffffff
            );
      }
    });
  }

  @Override
  public CubozoaRenderState createRenderState() {
    return new CubozoaRenderState();
  }

  @Override
  public Identifier getTexture(CubozoaRenderState state) {
    return TEXTURES.get(state.variant);
  }

  @Override
  public void updateRenderState(Cubozoa fish, CubozoaRenderState state, float f) {
    super.updateRenderState(fish, state, f);
    state.variant = fish.getVariant();
    state.animationProgress += f / 2;
  }
}
