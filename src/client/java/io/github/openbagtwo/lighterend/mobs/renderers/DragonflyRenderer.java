package io.github.openbagtwo.lighterend.mobs.renderers;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.mobs.Dragonfly;
import io.github.openbagtwo.lighterend.mobs.EntityModels;
import io.github.openbagtwo.lighterend.mobs.models.DragonflyModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public class DragonflyRenderer extends
    MobRenderer<Dragonfly, LivingEntityRenderState, DragonflyModel> {

  private static final Identifier TEXTURE = LighterEnd.of("textures/entity/dragonfly.png");
  private static final RenderType GLOW = RenderTypes.eyes(
      LighterEnd.of("textures/entity/dragonfly_glow.png")
  );

  public DragonflyRenderer(EntityRendererProvider.Context ctx) {
    super(ctx, new DragonflyModel(ctx.bakeLayer(EntityModels.DRAGONFLY_MODEL)), 0.5f);
    this.addLayer(new EyesLayer<>(this) {
      @Override
      public RenderType renderType() {
        return GLOW;
      }
    });
  }

  @Override
  public LivingEntityRenderState createRenderState() {
    return new LivingEntityRenderState();
  }

  @Override
  public Identifier getTextureLocation(LivingEntityRenderState state) {
    return TEXTURE;
  }
}
