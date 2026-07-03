package io.github.openbagtwo.lighterend.mobs.renderers;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.mobs.EndSlime;
import io.github.openbagtwo.lighterend.mobs.EntityModels;
import io.github.openbagtwo.lighterend.mobs.models.EndSlimeModel;
import io.github.openbagtwo.lighterend.mobs.states.EndSlimeRenderState;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class EndSlimeRenderer extends
    MobEntityRenderer<EndSlime, EndSlimeRenderState, EndSlimeModel> {

  private static final List<Identifier> TEXTURES = Arrays.asList(
      LighterEnd.of("textures/entity/end_slime/end_slime.png"),
      LighterEnd.of("textures/entity/end_slime/end_slime_mossy.png"),
      LighterEnd.of("textures/entity/end_slime/end_slime_lake.png"),
      LighterEnd.of("textures/entity/end_slime/end_slime_amber.png")
  );
  private static final List<RenderLayer> GLOW = Arrays.asList(
      RenderLayer.getEyes(
          LighterEnd.of("textures/entity/end_slime/end_slime_glow.png")),
      RenderLayer.getEyes(
          LighterEnd.of("textures/entity/end_slime/end_slime_glow.png")),
      RenderLayer.getEyes(
          LighterEnd.of("textures/entity/end_slime/end_slime_lake_glow.png")),
      RenderLayer.getEyes(
          LighterEnd.of("textures/entity/end_slime/end_slime_amber_glow.png"))
  );

  public EndSlimeRenderer(EntityRendererFactory.Context context) {
    super(context, new EndSlimeModel(context.getPart(EntityModels.END_SLIME_MODEL), false), 0.25F);
    this.addFeature(new OverlayFeatureRenderer(this, context.getEntityModels()));
    this.addFeature(new EyesFeatureRenderer<>(this) {
      @Override
      public RenderLayer getEyesTexture() {
        return GLOW.get(0);
      }

      @Override
      public void render(
          MatrixStack matrices,
          OrderedRenderCommandQueue queue,
          int light,
          EndSlimeRenderState state,
          float limbAngle,
          float limbDistance
      ) {
        queue.getBatchingQueue(1)
            .submitModel(
                this.getContextModel(),
                state,
                matrices,
                GLOW.get(state.variant % GLOW.size()),
                15728640,
                OverlayTexture.DEFAULT_UV,
                0xffffffff,
                null,
                state.outlineColor,
                null
            );
      }
    });
  }

  @Override
  protected float getShadowRadius(EndSlimeRenderState state) {
    return state.size * 0.25F;
  }

  @Override
  protected void scale(EndSlimeRenderState state, MatrixStack matrixStack) {
    float f = 0.999F;
    matrixStack.scale(0.999F, 0.999F, 0.999F);
    matrixStack.translate(0.0F, 0.001F, 0.0F);
    float g = state.size;
    float h = state.stretch / (g * 0.5F + 1.0F);
    float i = 1.0F / (h + 1.0F);
    matrixStack.scale(i * g, 1.0F / i * g, i * g);
  }

  @Override
  public Identifier getTexture(EndSlimeRenderState state) {
    return TEXTURES.get(state.variant % TEXTURES.size());
  }

  @Override
  public EndSlimeRenderState createRenderState() {
    return new EndSlimeRenderState();
  }

  @Override
  public void updateRenderState(EndSlime slime, EndSlimeRenderState state, float f) {
    super.updateRenderState(slime, state, f);
    state.variant = slime.getSlimeType();
    state.stretch = MathHelper.lerp(f, slime.lastStretch, slime.stretch);
    state.size = slime.getSize();
  }

  public static class OverlayFeatureRenderer extends
      FeatureRenderer<EndSlimeRenderState, EndSlimeModel> {

    private final EndSlimeModel model;

    public OverlayFeatureRenderer(
        FeatureRendererContext<EndSlimeRenderState, EndSlimeModel> context,
        LoadedEntityModels loader) {
      super(context);
      this.model = new EndSlimeModel(loader.getModelPart(EntityModels.END_SLIME_SHELL_MODEL), true);
    }

    @Override
    public void render(
        MatrixStack matrixStack,
        OrderedRenderCommandQueue queue,
        int light,
        EndSlimeRenderState state,
        float limbAngle,
        float limbDistance
    ) {
      this.model.setAngles(state);

      boolean renderAsModel = state.hasOutline() && state.invisible;
      if (!state.invisible || renderAsModel) {
        int j = LivingEntityRenderer.getOverlay(state, 0.0F);
        if (renderAsModel) {
          queue.getBatchingQueue(1)
              .submitModel(
                  this.model,
                  state,
                  matrixStack,
                  RenderLayer.getEntityTranslucent(TEXTURES.get(state.variant % TEXTURES.size())),
                  light,
                  j,
                  -1,
                  null,
                  state.outlineColor,
                  null
              );
        } else {
          queue.getBatchingQueue(1)
              .submitModel(
                  this.model,
                  state,
                  matrixStack,
                  RenderLayer.getEntityTranslucent(TEXTURES.get(state.variant % TEXTURES.size())),
                  light,
                  j,
                  -1,
                  null,
                  state.outlineColor,
                  null
              );
        }
      }
    }
  }


}
