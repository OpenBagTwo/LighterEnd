package io.github.openbagtwo.lighterend.mobs.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.mobs.Cubozoa;
import io.github.openbagtwo.lighterend.mobs.EntityModels;
import io.github.openbagtwo.lighterend.mobs.models.CubozoaModel;
import io.github.openbagtwo.lighterend.mobs.states.CubozoaRenderState;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class CubozoaRenderer extends
    MobRenderer<Cubozoa, CubozoaRenderState, CubozoaModel> {

  private static final List<Identifier> TEXTURES = Arrays.asList(
      LighterEnd.of("textures/entity/cubozoa/cubozoa.png"),
      LighterEnd.of("textures/entity/cubozoa/cubozoa_sulphur.png")
  );
  private static final List<RenderType> GLOW = Arrays.asList(
      RenderTypes.eyes(
          LighterEnd.of("textures/entity/cubozoa/cubozoa_glow.png")),
      RenderTypes.eyes(
          LighterEnd.of("textures/entity/cubozoa/cubozoa_sulphur_glow.png"))
  );

  public CubozoaRenderer(EntityRendererProvider.Context ctx) {
    super(ctx, new CubozoaModel(ctx.bakeLayer(EntityModels.CUBOZOA_MODEL)), 0.5F);
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
          CubozoaRenderState state,
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
  public CubozoaRenderState createRenderState() {
    return new CubozoaRenderState();
  }

  @Override
  public Identifier getTextureLocation(CubozoaRenderState state) {
    return TEXTURES.get(state.variant % TEXTURES.size());
  }

  @Override
  public void extractRenderState(Cubozoa fish, CubozoaRenderState state, float f) {
    super.extractRenderState(fish, state, f);
    state.variant = fish.getVariant();
  }
}
