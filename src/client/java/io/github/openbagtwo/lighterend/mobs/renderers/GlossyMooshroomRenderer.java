package io.github.openbagtwo.lighterend.mobs.renderers;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.blocks.Polypore;
import io.github.openbagtwo.lighterend.mobs.GlossyMooshroom;
import io.github.openbagtwo.lighterend.mobs.states.GlossyMooshroomRenderState;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.AgeableMobEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class GlossyMooshroomRenderer extends
    AgeableMobEntityRenderer<GlossyMooshroom, LivingEntityRenderState, CowEntityModel> {

  private static final List<Identifier> TEXTURES = Arrays.asList(
      LighterEnd.of("textures/entity/glossy_mooshroom.png")
  );

  private static final List<RenderLayer> GLOW = Arrays.asList(
      RenderLayer.getEyes(
          LighterEnd.of("textures/entity/glossy_mooshroom_glow.png")
      )
  );

  public GlossyMooshroomRenderer(EntityRendererFactory.Context context) {
    super(context, new CowEntityModel(context.getPart(EntityModelLayers.MOOSHROOM)),
        new CowEntityModel(context.getPart(EntityModelLayers.MOOSHROOM_BABY)), 0.7F);
    this.addFeature(new PolyporeFeatureRenderer(this, context.getBlockRenderManager()));
    this.addFeature(
        new EyesFeatureRenderer<>(this) {
          @Override
          public RenderLayer getEyesTexture() {
            return GLOW.get(0);
          }

          @Override
          public void render(
              MatrixStack matrices,
              VertexConsumerProvider vertexConsumers,
              int light,
              LivingEntityRenderState state,
              float limbAngle,
              float limbDistance
          ) {
            if (state instanceof GlossyMooshroomRenderState cowState) {
              VertexConsumer vertexConsumer = vertexConsumers.getBuffer(GLOW.get(cowState.variant));
              this.getContextModel()
                  .render(
                      matrices,
                      vertexConsumer,
                      15728640,
                      OverlayTexture.DEFAULT_UV,
                      0xffffffff
                  );
            }
          }
        });
  }

  public Identifier getTexture(LivingEntityRenderState state) {
    int variant = 0;
    if (state instanceof GlossyMooshroomRenderState cowState) {
      variant = cowState.variant;
    }
    return TEXTURES.get(variant);
  }

  public GlossyMooshroomRenderState createRenderState() {
    return new GlossyMooshroomRenderState();
  }

  public void updateRenderState(GlossyMooshroom cow, LivingEntityRenderState state, float f) {
    super.updateRenderState(cow, state, f);
    if (state instanceof GlossyMooshroomRenderState cowState) {
      cowState.variant = cow.getVariant();
      cowState.sheared = cow.isSheared();
    }
  }

  public static class PolyporeFeatureRenderer extends
      FeatureRenderer<LivingEntityRenderState, CowEntityModel> {

    private final BlockRenderManager blockRenderManager;

    public PolyporeFeatureRenderer(
        FeatureRendererContext<LivingEntityRenderState, CowEntityModel> context,
        BlockRenderManager blockRenderManager) {
      super(context);
      this.blockRenderManager = blockRenderManager;
    }

    public void render(
        MatrixStack matrixStack,
        VertexConsumerProvider vertexConsumerProvider,
        int i,
        LivingEntityRenderState state,
        float f,
        float g
    ) {
      if (state instanceof GlossyMooshroomRenderState cowState) {
        if (!cowState.sheared && !cowState.baby) {
          boolean bl = cowState.hasOutline && cowState.invisible;
          if (!cowState.invisible || bl) {
            BlockState polyphore;
            if (cowState.variant == 0) {
              polyphore = LighterEndBlocks.AURANT_POLYPORE.getDefaultState().with(
                  Polypore.FACING, Direction.WEST
              );
            } else if (cowState.variant == 1) {
              polyphore = LighterEndBlocks.PURPLE_POLYPORE.getDefaultState().with(
                  Polypore.FACING, Direction.WEST
              );
            } else {
              return;
            }
            int j = LivingEntityRenderer.getOverlay(cowState, 0.0F);
            BlockStateModel blockStateModel = this.blockRenderManager.getModel(polyphore);
            matrixStack.push();
            matrixStack.scale(-0.3F, -0.5F, 0.5F);
            matrixStack.translate(-2.25F, -1.5F, 0);
            this.renderMushroom(matrixStack, vertexConsumerProvider, i, bl, polyphore, j,
                blockStateModel);
            matrixStack.pop();

            blockStateModel = this.blockRenderManager.getModel(
                polyphore.with(Polypore.FACING, Direction.EAST)
            );
            matrixStack.push();
            matrixStack.scale(0.3F, -0.5F, -0.5F);
            matrixStack.translate(-2.25F, -1.5F, -0.2F);
            this.renderMushroom(matrixStack, vertexConsumerProvider, i, bl, polyphore, j,
                blockStateModel);
            matrixStack.pop();
          }
        }
      }
    }

    private void renderMushroom(
        MatrixStack matrices,
        VertexConsumerProvider vertexConsumers,
        int light,
        boolean renderAsModel,
        BlockState mushroomState,
        int overlay,
        BlockStateModel mushroomModel
    ) {
      if (renderAsModel) {
        BlockModelRenderer.render(
            matrices.peek(),
            vertexConsumers.getBuffer(
                RenderLayer.getOutline(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)),
            mushroomModel, 0.0F, 0.0F, 0.0F, light, overlay
        );
      } else {
        this.blockRenderManager.renderBlockAsEntity(mushroomState, matrices, vertexConsumers, light,
            overlay);
      }
    }
  }
}
