package io.github.openbagtwo.lighterend.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.jetbrains.annotations.Nullable;

public class BuriedBlob extends Feature<BuriedBlob.Config> {

  public BuriedBlob() {
    super(Config.CODEC);
  }

  @Override
  public boolean generate(FeatureContext<BuriedBlob.Config> context) {
    Config config = context.getConfig();
    StructureWorldAccess structureWorldAccess = context.getWorld();
    Random random = context.getRandom();
    Block block = config.target.getBlock();
    var blockPos = moveDownToTarget(
        structureWorldAccess,
        context.getOrigin().mutableCopy()
            .clamp(
                Direction.Axis.Y,
                structureWorldAccess.getBottomY() + 1,
                structureWorldAccess.getTopYInclusive()
            ),
        block
    );
    if (blockPos == null) {
      return false;
    } else {
      blockPos.move(Direction.DOWN, config.depth);
      int i = config.getRadius().get(random);
      int j = config.getRadius().get(random);
      int k = config.getRadius().get(random);
      int l = Math.max(i, Math.max(j, k));
      boolean bl = false;

      for (BlockPos blockPos2 : BlockPos.iterateOutwards(blockPos, i, j, k)) {
        if (blockPos2.getManhattanDistance(blockPos) > l) {
          break;
        }

        BlockState blockState = structureWorldAccess.getBlockState(blockPos2);
        if (blockState.isOf(block)) {
          this.setBlockState(structureWorldAccess, blockPos2, config.state);
          bl = true;
        }
      }

      return bl;
    }
  }

  @Nullable
  private static BlockPos.Mutable moveDownToTarget(
      WorldAccess world,
      BlockPos.Mutable mutablePos,
      Block target
  ) {
    while (mutablePos.getY() > world.getBottomY() + 1) {
      BlockState blockState = world.getBlockState(mutablePos);
      if (blockState.isOf(target)) {
        return mutablePos;
      }
      mutablePos.move(Direction.DOWN);
    }

    return null;
  }

  public static class Config implements FeatureConfig {

    public static final Codec<Config> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
                BlockState.CODEC.fieldOf("target").forGetter(config -> config.target),
                BlockState.CODEC.fieldOf("state").forGetter(config -> config.state),
                IntProvider.createValidatingCodec(0, 12).fieldOf("radius")
                    .forGetter(config -> config.radius),
                Codec.INT.fieldOf("depth").forGetter(config -> config.depth)
            )
            .apply(instance, Config::new)
    );

    public final BlockState target;
    public final BlockState state;
    private final IntProvider radius;
    private final int depth;

    public Config(BlockState target, BlockState state, IntProvider radius, int depth) {
      this.target = target;
      this.state = state;
      this.radius = radius;
      this.depth = depth;
    }

    public IntProvider getRadius() {
      return this.radius;
    }
  }
}
