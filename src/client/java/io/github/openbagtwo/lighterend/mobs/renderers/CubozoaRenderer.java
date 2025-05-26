package io.github.openbagtwo.lighterend.mobs.renderers;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.mobs.Cubozoa;
import io.github.openbagtwo.lighterend.mobs.EntityModels;
import io.github.openbagtwo.lighterend.mobs.models.CubozoaModel;
import io.github.openbagtwo.lighterend.mobs.states.CubozoaRenderState;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class CubozoaRenderer extends
    MobEntityRenderer<Cubozoa, CubozoaRenderState, CubozoaModel> {

  private static final List<Identifier> TEXTURES = Arrays.asList(
      Identifier.of(LighterEnd.MOD_ID, "textures/entity/cubozoa/cubozoa.png"),
      Identifier.of(LighterEnd.MOD_ID, "textures/entity/cubozoa/cubozoa_sulphur.png")
  );

  public CubozoaRenderer(EntityRendererFactory.Context ctx) {
    super(ctx, new CubozoaModel(ctx.getPart(EntityModels.CUBOZOA_MODEL)), 0.5F);
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
    state.variant = fish.getVariant();
  }
}
