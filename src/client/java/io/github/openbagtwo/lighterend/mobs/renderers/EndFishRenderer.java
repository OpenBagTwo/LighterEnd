package io.github.openbagtwo.lighterend.mobs.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.mobs.EndFish;
import io.github.openbagtwo.lighterend.mobs.EntityModels;
import io.github.openbagtwo.lighterend.mobs.models.EndFishModel;
import io.github.openbagtwo.lighterend.mobs.states.EndFishRenderState;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class EndFishRenderer extends
    MobRenderer<EndFish, EndFishRenderState, EndFishModel> {

  private static final List<Identifier> TEXTURES = Arrays.asList(
      LighterEnd.of("textures/entity/end_fish/end_fish_0.png"),
      LighterEnd.of("textures/entity/end_fish/end_fish_1.png"),
      LighterEnd.of("textures/entity/end_fish/end_fish_2.png"),
      LighterEnd.of("textures/entity/end_fish/end_fish_3.png"),
      LighterEnd.of("textures/entity/end_fish/end_fish_4.png"),
      LighterEnd.of("textures/entity/end_fish/end_fish_5.png"),
      LighterEnd.of("textures/entity/end_fish/end_fish_6.png"),
      LighterEnd.of("textures/entity/end_fish/end_fish_7.png")
  );
  private static final List<RenderType> GLOW = Arrays.asList(
      RenderTypes.eyes(
          LighterEnd.of("textures/entity/end_fish/end_fish_0_glow.png")),
      RenderTypes.eyes(
          LighterEnd.of("textures/entity/end_fish/end_fish_1_glow.png")),
      RenderTypes.eyes(
          LighterEnd.of("textures/entity/end_fish/end_fish_2_glow.png")),
      RenderTypes.eyes(
          LighterEnd.of("textures/entity/end_fish/end_fish_3_glow.png")),
      RenderTypes.eyes(
          LighterEnd.of("textures/entity/end_fish/end_fish_4_glow.png")),
      RenderTypes.eyes(
          LighterEnd.of("textures/entity/end_fish/end_fish_5_glow.png")),
      RenderTypes.eyes(
          LighterEnd.of("textures/entity/end_fish/end_fish_6_glow.png")),
      RenderTypes.eyes(
          LighterEnd.of("textures/entity/end_fish/end_fish_7_glow.png"))
  );

  public EndFishRenderer(EntityRendererProvider.Context ctx) {
    super(ctx, new EndFishModel(ctx.bakeLayer(EntityModels.END_FISH_MODEL)), 0.5F);
    this.addLayer(new EyesLayer<>(this) {
      @Override
      public RenderType renderType() {
        return GLOW.get(0);
      }

      @Override
      public void submit(
          PoseStack matrices,
          SubmitNodeCollector queue,
          int light,
          EndFishRenderState state,
          float limbAngle,
          float limbDistance
      ) {
        queue.order(1)
            .submitModel(
                this.getParentModel(),
                state,
                matrices,
                GLOW.get(state.variant % GLOW.size()),
                15728640,
                OverlayTexture.NO_OVERLAY,
                0xffffffff,
                null,
                state.outlineColor
            );
      }
    });
  }

  @Override
  public EndFishRenderState createRenderState() {
    return new EndFishRenderState();
  }

  @Override
  public Identifier getTextureLocation(EndFishRenderState state) {
    return TEXTURES.get(state.variant % TEXTURES.size());
  }

  @Override
  public void extractRenderState(EndFish fish, EndFishRenderState state, float f) {
    super.extractRenderState(fish, state, f);
    state.variant = fish.getVariant();
  }
}
