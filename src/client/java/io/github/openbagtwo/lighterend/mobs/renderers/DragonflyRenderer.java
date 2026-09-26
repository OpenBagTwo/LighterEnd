package io.github.openbagtwo.lighterend.mobs.renderers;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.mobs.Dragonfly;
import io.github.openbagtwo.lighterend.mobs.EntityModels;
import io.github.openbagtwo.lighterend.mobs.models.DragonflyModel;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;

public class DragonflyRenderer extends
    MobEntityRenderer<Dragonfly, LivingEntityRenderState, DragonflyModel> {

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
  public LivingEntityRenderState createRenderState() {
    return new LivingEntityRenderState();
  }

  @Override
  public Identifier getTexture(LivingEntityRenderState state) {
    return TEXTURE;
  }
}
