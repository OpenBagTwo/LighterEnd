package io.github.openbagtwo.lighterend.world.features;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import io.github.openbagtwo.lighterend.utils.Flags;
import io.github.openbagtwo.lighterend.utils.MiscUtils;
import io.github.openbagtwo.lighterend.utils.PosInfo;
import io.github.openbagtwo.lighterend.utils.math.MathUtils;
import io.github.openbagtwo.lighterend.utils.math.sdf.SDF;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFRotate;
import io.github.openbagtwo.lighterend.utils.math.sdf.primitives.SDFHexPrism;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.joml.Vector3f;

public class AuroraCrystalFormation implements Feature {

  public AuroraCrystalFormation() {
  }

  public static final MapCodec<AuroraCrystalFormation> CODEC = MapCodec.unit(
      AuroraCrystalFormation::new
  );

  @Override
  public MapCodec<AuroraCrystalFormation> codec() {
    return CODEC;
  }

  @Override
  public boolean place(
      final WorldGenLevel world,
      final ChunkGenerator chunkGenerator,
      final RandomSource random,
      final BlockPos origin
  ) {
    int maxY = origin.getY() + PosInfo.upRay(world, origin, 16);
    int minY = origin.getY() - PosInfo.downRay(world, origin, 16);

    if (maxY - minY < 10) {
      return false;
    }

    int height = Mth.nextInt(random, 5, 25);

    BlockPos pos = new BlockPos(
        origin.getX(),
        Mth.nextInt(random, minY, minY + height / 2),
        origin.getZ());

    SDF prism = new SDFHexPrism().setHeight(height)
        .setRadius(Mth.nextFloat(random, 1.7F, 3F))
        .setBlock(LighterEndBlocks.AURORA_CRYSTAL);
    Vector3f vec = MathUtils.randomHorizontal(random);
    prism = new SDFRotate().setRotation(vec, random.nextFloat()).setSource(prism);
    prism.setReplaceFunction((state) ->
        state.is(LighterEndTags.END_STONES)
            || state.is(LighterEndTags.END_SOIL)
            || MiscUtils.replaceableOrPlant(state)
            || state.is(BlockTags.LEAVES));
    prism.fillRecursive(world, pos);
    world.setBlock(pos, LighterEndBlocks.AURORA_CRYSTAL.defaultBlockState(), Flags.SILENT);

    return true;
  }
}
