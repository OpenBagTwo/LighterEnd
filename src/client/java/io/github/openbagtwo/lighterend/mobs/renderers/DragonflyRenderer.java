package io.github.openbagtwo.lighterend.mobs.renderers;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.mobs.Dragonfly;
import io.github.openbagtwo.lighterend.mobs.EntityModels;
import io.github.openbagtwo.lighterend.mobs.models.DragonflyModel;
import io.github.openbagtwo.lighterend.mobs.states.DragonflyRenderState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.util.Identifier;

public class DragonflyRenderer extends
    MobEntityRenderer<Dragonfly, DragonflyRenderState, DragonflyModel> {

  private static final Identifier TEXTURE = LighterEnd.of("textures/entity/dragonfly.png");
  private static final RenderLayer GLOW = RenderLayer.getEyes(
      LighterEnd.of("textures/entity/dragonfly_glow.png")
  );

  public DragonflyRenderer(EntityRendererFactory.Context ctx) {
    super(ctx, new DragonflyModel(ctx.getPart(EntityModels.DRAGONFLY_MODEL)), 0.5f);
    this.addFeature(new EyesFeatureRenderer<>(this) {
      @Override
      public RenderLayer getEyesTexture() {
        return GLOW;
      }
    });
  }

  @Override
  public DragonflyRenderState createRenderState() {
    return new DragonflyRenderState();
  }

  @Override
  public Identifier getTexture(DragonflyRenderState state) {
    return TEXTURE;
  }

  @Override
  public void updateRenderState(Dragonfly fly, DragonflyRenderState state, float f) {
    super.updateRenderState(fly, state, f);
    state.animationProgress += f / 2;
  }
}
