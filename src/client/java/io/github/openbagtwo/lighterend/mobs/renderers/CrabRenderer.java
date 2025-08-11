package io.github.openbagtwo.lighterend.mobs.renderers;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.mobs.ChorusCrab;
import io.github.openbagtwo.lighterend.mobs.EntityModels;
import io.github.openbagtwo.lighterend.mobs.models.CrabModel;
import io.github.openbagtwo.lighterend.mobs.states.CrabRenderState;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class CrabRenderer extends
    MobEntityRenderer<ChorusCrab, CrabRenderState, CrabModel> {

  private static final Identifier TEXTURE = LighterEnd.of("textures/entity/chorus_crab.png");

  public CrabRenderer(EntityRendererFactory.Context ctx) {
    super(ctx, new CrabModel(ctx.getPart(EntityModels.CRAB_MODEL)), 0.5f);
  }

  @Override
  public CrabRenderState createRenderState() {
    return new CrabRenderState();
  }

  @Override
  public Identifier getTexture(CrabRenderState state) {
    return TEXTURE;
  }

  @Override
  public void updateRenderState(ChorusCrab crab, CrabRenderState state, float f) {
    super.updateRenderState(crab, state, f);
    state.animationProgress += f / 2;
  }
}
