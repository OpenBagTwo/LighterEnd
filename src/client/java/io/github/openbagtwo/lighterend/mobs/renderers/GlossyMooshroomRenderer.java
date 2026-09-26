package io.github.openbagtwo.lighterend.mobs.renderers;

import static net.minecraft.client.renderer.entity.MushroomCowRenderer.BLOCK_DISPLAY_CONTEXT;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.blocks.Polypore;
import io.github.openbagtwo.lighterend.mobs.GlossyMooshroom;
import io.github.openbagtwo.lighterend.mobs.states.GlossyMooshroomRenderState;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.model.animal.cow.CowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;

public class GlossyMooshroomRenderer extends
    AgeableMobRenderer<GlossyMooshroom, LivingEntityRenderState, CowModel> {

  private static final List<Identifier> TEXTURES = Arrays.asList(
      LighterEnd.of("textures/entity/glossy_mooshroom.png"),
      LighterEnd.of("textures/entity/glossy_mooshroom_baby.png")
  );

  private static final List<RenderType> GLOW = Arrays.asList(
      RenderTypes.eyes(
          LighterEnd.of("textures/entity/glossy_mooshroom_glow.png")
      ),
      RenderTypes.eyes(
          LighterEnd.of("textures/entity/glossy_mooshroom_baby_glow.png")
      )
  );

  public GlossyMooshroomRenderer(EntityRendererProvider.Context context) {
    super(context, new CowModel(context.bakeLayer(ModelLayers.MOOSHROOM)),
        new CowModel(context.bakeLayer(ModelLayers.MOOSHROOM_BABY)), 0.7F);
    this.addLayer(new PolyporeFeatureRenderer(this, context.getBlockModelResolver()));
    this.addLayer(
        new EyesLayer<>(this) {
          @Override
          public RenderType renderType() {
            return GLOW.get(0);
          }

          @Override
          public void submit(
              PoseStack matrices,
              SubmitNodeCollector queue,
              int light,
              LivingEntityRenderState state,
              float limbAngle,
              float limbDistance
          ) {
            if (state instanceof GlossyMooshroomRenderState cowState) {
              queue.order(1)
                  .submitModel(
                      this.getParentModel(),
                      state,
                      matrices,
                      GLOW.get(
                          (cowState.variant % (GLOW.size() / 2)) * 2 + (cowState.isBaby ? 1 : 0)
                      ),
                      light,
                      OverlayTexture.NO_OVERLAY,
                      -1,
                      null,
                      state.outlineColor,
                      null
                  );
            }
          }
        });
  }

  public Identifier getTextureLocation(LivingEntityRenderState state) {
    int variant = 0;
    int baby = 0;
    if (state instanceof GlossyMooshroomRenderState cowState) {
      variant = cowState.variant;
      baby = cowState.isBaby ? 1 : 0;
    }
    return TEXTURES.get((variant % (TEXTURES.size() / 2)) * 2 + baby);
  }

  public GlossyMooshroomRenderState createRenderState() {
    return new GlossyMooshroomRenderState();
  }

  @Override
  public void extractRenderState(GlossyMooshroom cow, LivingEntityRenderState state, float f) {
    super.extractRenderState(cow, state, f);
    if (state instanceof GlossyMooshroomRenderState cowState) {
      cowState.variant = cow.getVariant();
      cowState.sheared = cow.isSheared();
    }
  }

  public static class PolyporeFeatureRenderer extends
      RenderLayer<LivingEntityRenderState, CowModel> {

    private final BlockModelResolver blockModelResolver;

    public PolyporeFeatureRenderer(
        RenderLayerParent<LivingEntityRenderState, CowModel> context,
        BlockModelResolver blockModelResolver) {
      super(context);
      this.blockModelResolver = blockModelResolver;
    }

    @Override
    public void submit(
        PoseStack matrixStack,
        SubmitNodeCollector queue,
        int light,
        LivingEntityRenderState state,
        float f,
        float g
    ) {
      if (state instanceof GlossyMooshroomRenderState cowState) {
        if (!cowState.sheared && !cowState.isBaby) {
          boolean glowingAndInvisible = cowState.appearsGlowing() && cowState.isInvisible;
          if (!cowState.isInvisible || glowingAndInvisible) {
            BlockState polyphore;
            if (cowState.variant == 0) {
              polyphore = LighterEndBlocks.AURANT_POLYPORE.defaultBlockState().setValue(
                  Polypore.FACING, Direction.WEST
              );
            } else if (cowState.variant == 1) {
              polyphore = LighterEndBlocks.PURPLE_POLYPORE.defaultBlockState().setValue(
                  Polypore.FACING, Direction.WEST
              );
            } else {
              return;
            }
            int overlay = LivingEntityRenderer.getOverlayCoords(cowState, 0.0F);
            this.blockModelResolver.update(
                cowState.leftShroom,
                polyphore.setValue(Polypore.FACING, Direction.WEST),
                BLOCK_DISPLAY_CONTEXT
            );
            matrixStack.pushPose();
            matrixStack.scale(-0.3F, -0.5F, 0.5F);
            matrixStack.translate(-2.25F, -1.5F, 0);
            this.renderMushroom(
                matrixStack,
                queue,
                light,
                glowingAndInvisible,
                cowState.outlineColor,
                cowState.leftShroom,
                overlay

            );
            matrixStack.popPose();

            this.blockModelResolver.update(
                cowState.rightShroom,
                polyphore.setValue(Polypore.FACING, Direction.EAST),
                BLOCK_DISPLAY_CONTEXT
            );
            matrixStack.pushPose();
            matrixStack.scale(0.3F, -0.5F, -0.5F);
            matrixStack.rotateAround(Axis.YP.rotationDegrees(180), 0, 0, 0);
            matrixStack.translate(1.25F, -1.5F, -0.2F);
            this.renderMushroom(
                matrixStack,
                queue,
                light,
                glowingAndInvisible,
                cowState.outlineColor,
                cowState.rightShroom,
                overlay
            );
            matrixStack.popPose();
          }
        }
      }
    }

    private void renderMushroom(
        PoseStack poseStack,
        SubmitNodeCollector submitNodeCollector,
        int lightCoords,
        boolean glowingAndInvisible,
        int outlineColor,
        BlockModelRenderState mushroomModel,
        int overlayCoords
    ) {
      if (glowingAndInvisible) {
        mushroomModel.submitOnlyOutline(poseStack, submitNodeCollector, lightCoords, overlayCoords,
            outlineColor);
      } else {
        mushroomModel.submit(poseStack, submitNodeCollector, lightCoords, overlayCoords,
            outlineColor);
      }
    }
  }
}
