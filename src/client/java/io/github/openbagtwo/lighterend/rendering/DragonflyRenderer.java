package io.github.openbagtwo.lighterend.rendering;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.mobs.Dragonfly;
import io.github.openbagtwo.lighterend.rendering.models.DragonflyModel;
import io.github.openbagtwo.lighterend.rendering.states.DragonflyRenderState;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class DragonflyRenderer extends
    MobEntityRenderer<Dragonfly, DragonflyRenderState, DragonflyModel> {

  private static final Identifier TEXTURE = Identifier.of(LighterEnd.MOD_ID,
      "textures/entity/dragonfly.png");

  public DragonflyRenderer(EntityRendererFactory.Context ctx) {
    super(ctx, new DragonflyModel(ctx.getPart(EntityModels.DRAGONFLY_MODEL)), 0.5f);
  }

  @Override
  public DragonflyRenderState createRenderState() {
    return new DragonflyRenderState();
  }

  @Override
  public Identifier getTexture(DragonflyRenderState state) {
    return TEXTURE;
  }
}
