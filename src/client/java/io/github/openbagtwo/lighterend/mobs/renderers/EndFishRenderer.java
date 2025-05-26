package io.github.openbagtwo.lighterend.mobs.renderers;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.mobs.EndFish;
import io.github.openbagtwo.lighterend.mobs.EntityModels;
import io.github.openbagtwo.lighterend.mobs.models.EndFishModel;
import io.github.openbagtwo.lighterend.mobs.states.EndFishRenderState;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class EndFishRenderer extends
    MobEntityRenderer<EndFish, EndFishRenderState, EndFishModel> {

  private static final List<Identifier> TEXTURES = Arrays.asList(
      Identifier.of(LighterEnd.MOD_ID, "textures/entity/end_fish/end_fish_0.png"),
      Identifier.of(LighterEnd.MOD_ID, "textures/entity/end_fish/end_fish_1.png"),
      Identifier.of(LighterEnd.MOD_ID, "textures/entity/end_fish/end_fish_2.png"),
      Identifier.of(LighterEnd.MOD_ID, "textures/entity/end_fish/end_fish_3.png"),
      Identifier.of(LighterEnd.MOD_ID, "textures/entity/end_fish/end_fish_4.png"),
      Identifier.of(LighterEnd.MOD_ID, "textures/entity/end_fish/end_fish_5.png"),
      Identifier.of(LighterEnd.MOD_ID, "textures/entity/end_fish/end_fish_6.png"),
      Identifier.of(LighterEnd.MOD_ID, "textures/entity/end_fish/end_fish_7.png")
  );

  public EndFishRenderer(EntityRendererFactory.Context ctx) {
    super(ctx, new EndFishModel(ctx.getPart(EntityModels.END_FISH_MODEL)), 0.5F);
  }

  @Override
  public EndFishRenderState createRenderState() {
    return new EndFishRenderState();
  }

  @Override
  public Identifier getTexture(EndFishRenderState state) {
    return TEXTURES.get(state.variant);
  }

  @Override
  public void updateRenderState(EndFish fish, EndFishRenderState state, float f) {
    state.variant = fish.getVariant();
  }
}
