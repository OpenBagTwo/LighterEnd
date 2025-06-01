package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

public class AuroraCrystalRenderer {

  public static final Vec3i[] COLORS = new Vec3i[]{
      new Vec3i(247, 77, 161),
      new Vec3i(120, 184, 255),
      new Vec3i(120, 255, 168),
      new Vec3i(243, 58, 255)
  };

  public static BlockColorProvider getBlockColor() {
    return (state, world, pos, tintIndex) -> {
      if (pos == null) {
        pos = BlockPos.ORIGIN;
      }

      long i = (long) pos.getX() + (long) pos.getY() + (long) pos.getZ();
      double delta = i * 0.1;
      int index = MathHelper.floor(delta);
      int index2 = (index + 1) & 3;
      delta -= index;
      index &= 3;

      Vec3i color1 = COLORS[index];
      Vec3i color2 = COLORS[index2];

      int r = MathHelper.floor(MathHelper.lerp(delta, color1.getX(), color2.getX()));
      int g = MathHelper.floor(MathHelper.lerp(delta, color1.getY(), color2.getY()));
      int b = MathHelper.floor(MathHelper.lerp(delta, color1.getZ(), color2.getZ()));

      return color(r, g, b);
    };
  }

  public static void initialize() {
    BlockRenderLayerMap.putBlocks(
        BlockRenderLayer.TRANSLUCENT,
        LighterEndBlocks.AURORA_CRYSTAL
    );
  }

  //Utils (TODO: refactor out as needed)

  private static int color(int r, int g, int b) {
    return (255 << 24) | (r << 16) | (g << 8) | b;
  }


}
