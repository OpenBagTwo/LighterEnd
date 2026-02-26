package io.github.openbagtwo.lighterend.mobs.renderers;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.mobs.EntityModels;
import io.github.openbagtwo.lighterend.mobs.SilkMoth;
import io.github.openbagtwo.lighterend.mobs.models.SilkMothModel;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class SilkMothRenderer extends
    AgeableMobRenderer<SilkMoth, LivingEntityRenderState, SilkMothModel> {

  private static final Identifier TEXTURE = LighterEnd.of("textures/entity/silk_moth.png");

  public SilkMothRenderer(EntityRendererProvider.Context ctx) {
    super(
        ctx,
        new SilkMothModel(ctx.bakeLayer(EntityModels.SILK_MOTH_MODEL)),
        new SilkMothModel(ctx.bakeLayer(EntityModels.SILK_MOTH_BABY)),
        0.5f
    );
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
