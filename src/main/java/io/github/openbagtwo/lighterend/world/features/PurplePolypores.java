package io.github.openbagtwo.lighterend.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.openbagtwo.lighterend.blocks.Polypore;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.utils.Flags;
import io.github.openbagtwo.lighterend.utils.PosInfo;
import io.github.openbagtwo.lighterend.world.features.PurplePolypores.Config;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class PurplePolypores extends Feature<Config> {

  public PurplePolypores() {
    super(Config.CODEC);
  }

  @Override
  public boolean generate(FeatureContext<Config> featureConfig) {
    Config cfg = featureConfig.getConfig();
    final Random random = featureConfig.getRandom();
    final BlockPos center = featureConfig.getOrigin();
    final StructureWorldAccess world = featureConfig.getWorld();
    int maxY = world.getTopY(Heightmap.Type.WORLD_SURFACE, center.getX(), center.getZ());
    int minY = PosInfo.upRay(world, new BlockPos(center.getX(), 0, center.getZ()), maxY);
    if (maxY < 10 || maxY < minY) {
      return false;
    }
    int py = MathHelper.nextInt(random, minY, maxY);

    Mutable mut = new Mutable();
    for (int x = -cfg.radius; x <= cfg.radius; x++) {
      mut.setX(center.getX() + x);
      for (int y = -cfg.radius; y <= cfg.radius; y++) {
        mut.setY(py + y);
        for (int z = -cfg.radius; z <= cfg.radius; z++) {
          mut.setZ(center.getZ() + z);
          if (random.nextInt(4) == 0 && world.isAir(mut)) {
            for (Direction dir : Direction.Type.HORIZONTAL.getShuffled(random)) {
              if (world.getBlockState(mut.offset(dir.getOpposite())).isIn(BlockTags.LOGS)) {
                world.setBlockState(
                    mut,
                    LighterEndBlocks.PURPLE_POLYPORE.getDefaultState().with(Polypore.FACING, dir),
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

  public static class Config implements FeatureConfig {

    public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance
        .group(
            Codec.INT.fieldOf("radius").forGetter(o -> o.radius)
        )
        .apply(instance, Config::new));

    public final int radius;

    public Config(int radius) {
      this.radius = radius;
    }
  }

}
