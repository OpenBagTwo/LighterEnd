package io.github.openbagtwo.lighterend.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.openbagtwo.lighterend.blocks.PedestalRenderer.RenderState;
import io.github.openbagtwo.lighterend.blocks.entities.PedestalDisplay;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class PedestalRenderer implements BlockEntityRenderer<PedestalDisplay, RenderState> {

  private final ItemModelResolver itemModelManager;

  public PedestalRenderer(BlockEntityRendererProvider.Context context) {
    itemModelManager = context.itemModelResolver();
  }

  @Override
  public RenderState createRenderState() {
    return new RenderState();
  }

  @Override
  public void extractRenderState(
      PedestalDisplay blockEntity,
      RenderState state,
      float tickProgress,
      Vec3 cameraPos,
      @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
    BlockEntityRenderer.super.extractRenderState(
        blockEntity,
        state,
        tickProgress,
        cameraPos,
        crumblingOverlay
    );

    state.lightPosition = blockEntity.getBlockPos();
    state.blockEntityWorld = blockEntity.getLevel();
    state.rotation = blockEntity.getRenderingRotation();

    itemModelManager.updateForTopItem(
        state.itemRenderState,
        blockEntity.getItem(0),
        ItemDisplayContext.FIXED,
        blockEntity.getLevel(),
        null,
        0
    );
  }

  @Override
  public void submit(
      RenderState state, PoseStack matrices,
      SubmitNodeCollector queue, CameraRenderState cameraState) {
    matrices.pushPose();

    matrices.translate(0.5f, 1.4f, 0.5f);
    matrices.rotate(Axis.YP.rotationDegrees(state.rotation));

    state.itemRenderState.submit(matrices, queue,
        getLightLevel(state.blockEntityWorld, state.blockPos),
        OverlayTexture.NO_OVERLAY, 0);

    matrices.popPose();
  }

  private int getLightLevel(Level world, BlockPos pos) {
    int bLight = world.getBrightness(LightLayer.BLOCK, pos);
    int sLight = world.getBrightness(LightLayer.SKY, pos);
    return LightCoordsUtil.pack(bLight, sLight);
  }

  public static class RenderState extends BlockEntityRenderState {

    public BlockPos lightPosition;
    public Level blockEntityWorld;
    public float rotation;

    final ItemStackRenderState itemRenderState = new ItemStackRenderState();
  }
}
