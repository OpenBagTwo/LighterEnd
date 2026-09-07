package io.github.openbagtwo.lighterend.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.jetbrains.annotations.Nullable;

public class BuriedBlob implements Feature {

  final BlockState target;
  final BlockState state;
  final IntProvider radius;
  final int depth;

  public BuriedBlob(BlockState target, BlockState state, IntProvider radius, int depth) {
    this.target = target;
    this.state = state;
    this.radius = radius;
    this.depth = depth;
  }

  @Override
  public MapCodec<BuriedBlob> codec() {
    return CODEC;
  }

  @Override
  public boolean place(
      final WorldGenLevel structureWorldAccess,
      final ChunkGenerator chunkGenerator,
      final RandomSource random,
      final BlockPos origin
  ) {
    Block block = this.target.getBlock();
    var blockPos = moveDownToTarget(
        structureWorldAccess,
        origin.mutable()
            .clamp(
                Direction.Axis.Y,
                structureWorldAccess.getMinY() + 1,
                structureWorldAccess.getMaxY()
            ),
        block
    );
    if (blockPos == null) {
      return false;
    } else {
      blockPos.move(Direction.DOWN, this.depth);
      int i = this.radius.sample(random);
      int j = this.radius.sample(random);
      int k = this.radius.sample(random);
      int l = Math.max(i, Math.max(j, k));
      boolean bl = false;

      for (BlockPos blockPos2 : BlockPos.withinBoxByManhattanDistance(blockPos, i, j, k)) {
        if (blockPos2.distManhattan(blockPos) > l) {
          break;
        }

        BlockState blockState = structureWorldAccess.getBlockState(blockPos2);
        if (blockState.is(block)) {
          this.setBlock(structureWorldAccess, blockPos2, this.state);
          bl = true;
        }
      }

      return bl;
    }
  }

  @Nullable
  private static BlockPos.MutableBlockPos moveDownToTarget(
      LevelAccessor world,
      BlockPos.MutableBlockPos mutablePos,
      Block target
  ) {
    while (mutablePos.getY() > world.getMinY() + 1) {
      BlockState blockState = world.getBlockState(mutablePos);
      if (blockState.is(target)) {
        return mutablePos;
      }
      mutablePos.move(Direction.DOWN);
    }

    return null;
  }

  public static final MapCodec<BuriedBlob> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
              BlockState.CODEC.fieldOf("target").forGetter(blob -> blob.target),
              BlockState.CODEC.fieldOf("state").forGetter(blob -> blob.state),
              IntProviders.codec(0, 12).fieldOf("radius")
                  .forGetter(blob -> blob.radius),
              Codec.INT.fieldOf("depth").forGetter(blob -> blob.depth)
          )
          .apply(instance, BuriedBlob::new)
  );
}
