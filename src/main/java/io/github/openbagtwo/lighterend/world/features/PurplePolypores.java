package io.github.openbagtwo.lighterend.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.openbagtwo.lighterend.blocks.Polypore;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.utils.Flags;
import io.github.openbagtwo.lighterend.utils.PosInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;


public class PurplePolypores implements Feature {

  final int radius;

  public PurplePolypores(int radius) {
    this.radius = radius;
  }

  @Override
  public MapCodec<PurplePolypores> codec() {
    return CODEC;
  }

  @Override
  public boolean place(
      final WorldGenLevel world,
      final ChunkGenerator chunkGenerator,
      final RandomSource random,
      final BlockPos center
  ) {
    int maxY = world.getHeight(Heightmap.Types.WORLD_SURFACE, center.getX(), center.getZ());
    int minY = PosInfo.upRay(world, new BlockPos(center.getX(), 0, center.getZ()), maxY);
    if (maxY < 10 || maxY < minY) {
      return false;
    }
    int py = Mth.nextInt(random, minY, maxY);

    MutableBlockPos mut = new MutableBlockPos();
    for (int x = -this.radius; x <= this.radius; x++) {
      mut.setX(center.getX() + x);
      for (int y = -this.radius; y <= this.radius; y++) {
        mut.setY(py + y);
        for (int z = -this.radius; z <= this.radius; z++) {
          mut.setZ(center.getZ() + z);
          if (random.nextInt(4) == 0 && world.isEmptyBlock(mut)) {
            for (Direction dir : Direction.Plane.HORIZONTAL.shuffledCopy(random)) {
              if (world.getBlockState(mut.relative(dir.getOpposite())).is(BlockTags.LOGS)) {
                world.setBlock(
                    mut,
                    LighterEndBlocks.PURPLE_POLYPORE.defaultBlockState()
                        .setValue(Polypore.FACING, dir),
                    Flags.SILENT
                );
                break;
              }
            }
          }
        }
      }
    }
    return true;
  }

  public static final MapCodec<PurplePolypores> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance
          .group(
              Codec.INT.fieldOf("radius").forGetter(o -> o.radius)
          )
          .apply(instance, PurplePolypores::new));
}
