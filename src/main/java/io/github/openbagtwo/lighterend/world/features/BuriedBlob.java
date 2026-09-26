package io.github.openbagtwo.lighterend.world.features;

import com.mojang.serialization.Codec;
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
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import org.jetbrains.annotations.Nullable;

public class BuriedBlob extends Feature<BuriedBlob.Config> {

  public BuriedBlob() {
    super(Config.CODEC);
  }

  @Override
  public boolean place(FeaturePlaceContext<BuriedBlob.Config> context) {
    Config config = context.config();
    WorldGenLevel structureWorldAccess = context.level();
    RandomSource random = context.random();
    Block block = config.target.getBlock();
    var blockPos = moveDownToTarget(
        structureWorldAccess,
        context.origin().mutable()
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
      blockPos.move(Direction.DOWN, config.depth);
      int i = config.getRadius().sample(random);
      int j = config.getRadius().sample(random);
      int k = config.getRadius().sample(random);
      int l = Math.max(i, Math.max(j, k));
      boolean bl = false;

      for (BlockPos blockPos2 : BlockPos.withinManhattan(blockPos, i, j, k)) {
        if (blockPos2.distManhattan(blockPos) > l) {
          break;
        }

        BlockState blockState = structureWorldAccess.getBlockState(blockPos2);
        if (blockState.is(block)) {
          this.setBlock(structureWorldAccess, blockPos2, config.state);
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

  public static class Config implements FeatureConfiguration {

    public static final Codec<Config> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
                BlockState.CODEC.fieldOf("target").forGetter(config -> config.target),
                BlockState.CODEC.fieldOf("state").forGetter(config -> config.state),
                IntProviders.codec(0, 12).fieldOf("radius")
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
