package io.github.openbagtwo.lighterend.mobs.renderers;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.mobs.EntityModels;
import io.github.openbagtwo.lighterend.mobs.SilkMoth;
import io.github.openbagtwo.lighterend.mobs.models.SilkMothModel;
import io.github.openbagtwo.lighterend.mobs.states.SilkMothRenderState;
import net.minecraft.client.render.entity.AgeableMobEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class SilkMothRenderer extends
    AgeableMobEntityRenderer<SilkMoth, SilkMothRenderState, SilkMothModel> {

  private static final Identifier TEXTURE = Identifier.of(LighterEnd.MOD_ID,
      "textures/entity/silk_moth.png");

  public SilkMothRenderer(EntityRendererFactory.Context ctx) {
    super(
        ctx,
        new SilkMothModel(ctx.getPart(EntityModels.SILK_MOTH_MODEL)),
        new SilkMothModel(ctx.getPart(EntityModels.SILK_MOTH_BABY)),
        0.5f
    );
  }

  @Override
  public SilkMothRenderState createRenderState() {
    return new SilkMothRenderState();
  }

  @Override
  public Identifier getTexture(SilkMothRenderState state) {
    return TEXTURE;
  }
}
