package io.github.openbagtwo.lighterend.mobs.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.mobs.EndSlime;
import io.github.openbagtwo.lighterend.mobs.EntityModels;
import io.github.openbagtwo.lighterend.mobs.models.EndSlimeModel;
import io.github.openbagtwo.lighterend.mobs.states.EndSlimeRenderState;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class EndSlimeRenderer extends
    MobRenderer<EndSlime, EndSlimeRenderState, EndSlimeModel> {

  private static final List<Identifier> TEXTURES = Arrays.asList(
      LighterEnd.of("textures/entity/end_slime/end_slime.png"),
      LighterEnd.of("textures/entity/end_slime/end_slime_mossy.png"),
      LighterEnd.of("textures/entity/end_slime/end_slime_lake.png"),
      LighterEnd.of("textures/entity/end_slime/end_slime_amber.png")
  );
  private static final List<RenderType> GLOW = Arrays.asList(
      RenderTypes.eyes(
          LighterEnd.of("textures/entity/end_slime/end_slime_glow.png")),
      RenderTypes.eyes(
          LighterEnd.of("textures/entity/end_slime/end_slime_glow.png")),
      RenderTypes.eyes(
          LighterEnd.of("textures/entity/end_slime/end_slime_lake_glow.png")),
      RenderTypes.eyes(
          LighterEnd.of("textures/entity/end_slime/end_slime_amber_glow.png"))
  );

  public EndSlimeRenderer(EntityRendererProvider.Context context) {
    super(context, new EndSlimeModel(context.bakeLayer(EntityModels.END_SLIME_MODEL), false),
        0.25F);
    this.addLayer(new OverlayFeatureRenderer(this, context.getModelSet()));
    this.addLayer(new EyesLayer<>(this) {
      @Override
      public RenderType renderType() {
        return GLOW.get(0);
      }

      @Override
      public void submit(
          PoseStack matrices,
          SubmitNodeCollector queue,
          int light,
          EndSlimeRenderState state,
          float limbAngle,
          float limbDistance
      ) {
        queue.order(1)
            .submitModel(
                this.getParentModel(),
                state,
                matrices,
                GLOW.get(state.variant % GLOW.size()),
                15728640,
                OverlayTexture.NO_OVERLAY,
                0xffffffff,
                null,
                state.outlineColor
            );
      }
    });
  }

  @Override
  protected float getShadowRadius(EndSlimeRenderState state) {
    return state.size * 0.25F;
  }

  @Override
  protected void scale(EndSlimeRenderState state, PoseStack matrixStack) {
    float f = 0.999F;
    matrixStack.scale(0.999F, 0.999F, 0.999F);
    matrixStack.translate(0.0F, 0.001F, 0.0F);
    float g = state.size;
    float h = state.stretch / (g * 0.5F + 1.0F);
    float i = 1.0F / (h + 1.0F);
    matrixStack.scale(i * g, 1.0F / i * g, i * g);
  }

  @Override
  public Identifier getTextureLocation(EndSlimeRenderState state) {
    return TEXTURES.get(state.variant % TEXTURES.size());
  }

  @Override
  public EndSlimeRenderState createRenderState() {
    return new EndSlimeRenderState();
  }

  @Override
  public void extractRenderState(EndSlime slime, EndSlimeRenderState state, float f) {
    super.extractRenderState(slime, state, f);
    state.variant = slime.getSlimeType();
    state.stretch = Mth.lerp(f, slime.oSquish, slime.squish);
    state.size = slime.getSize();
  }

  public static class OverlayFeatureRenderer extends
      RenderLayer<EndSlimeRenderState, EndSlimeModel> {

    private final EndSlimeModel model;

    public OverlayFeatureRenderer(
        RenderLayerParent<EndSlimeRenderState, EndSlimeModel> context,
        EntityModelSet loader) {
      super(context);
      this.model = new EndSlimeModel(loader.bakeLayer(EntityModels.END_SLIME_SHELL_MODEL), true);
    }

    @Override
    public void submit(
        PoseStack matrixStack,
        SubmitNodeCollector queue,
        int light,
        EndSlimeRenderState state,
        float limbAngle,
        float limbDistance
    ) {
      this.model.setupAnim(state);

      boolean renderAsModel = state.appearsGlowing() && state.isInvisible;
      if (!state.isInvisible || renderAsModel) {
        int j = LivingEntityRenderer.getOverlayCoords(state, 0.0F);
        if (renderAsModel) {
          queue.order(1)
              .submitModel(
                  this.model,
                  state,
                  matrixStack,
                  RenderTypes.entityTranslucent(TEXTURES.get(state.variant % TEXTURES.size())),
                  light,
                  j,
                  -1,
                  null,
                  state.outlineColor
              );
        } else {
          queue.order(1)
              .submitModel(
                  this.model,
                  state,
                  matrixStack,
                  RenderTypes.entityTranslucent(TEXTURES.get(state.variant % TEXTURES.size())),
                  light,
                  j,
                  -1,
                  null,
                  state.outlineColor
              );
        }
      }
    }
  }


}
