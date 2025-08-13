package io.github.openbagtwo.lighterend.mobs.renderers;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.mobs.ChorusCrab;
import io.github.openbagtwo.lighterend.mobs.EntityModels;
import io.github.openbagtwo.lighterend.mobs.models.CrabModel;
import net.minecraft.client.render.entity.AgeableMobEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;

public class CrabRenderer extends
    AgeableMobEntityRenderer<ChorusCrab, LivingEntityRenderState, CrabModel> {

  private static final Identifier TEXTURE = LighterEnd.of("textures/entity/chorus_crab.png");

  public CrabRenderer(EntityRendererFactory.Context ctx) {
    super(ctx, new CrabModel(ctx.getPart(EntityModels.CRAB_MODEL)),
        new CrabModel(ctx.getPart(EntityModels.CRAB_BABY)), 1.2f);
  }

  @Override
  public LivingEntityRenderState createRenderState() {
    return new LivingEntityRenderState();
  }

  @Override
  public Identifier getTexture(LivingEntityRenderState state) {
    return TEXTURE;
  }

  @Override
  public void updateRenderState(ChorusCrab crab, LivingEntityRenderState state, float f) {
    super.updateRenderState(crab, state, f);
  }
}
