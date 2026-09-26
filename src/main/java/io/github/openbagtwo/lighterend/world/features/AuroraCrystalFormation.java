package io.github.openbagtwo.lighterend.world.features;

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
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.joml.Vector3f;

public class AuroraCrystalFormation extends Feature<NoneFeatureConfiguration> {

  public AuroraCrystalFormation() {
    super(NoneFeatureConfiguration.CODEC);
  }

  @Override
  public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featureConfig) {
    final RandomSource random = featureConfig.random();
    BlockPos pos = featureConfig.origin();
    final WorldGenLevel world = featureConfig.level();
    int maxY = pos.getY() + PosInfo.upRay(world, pos, 16);
    int minY = pos.getY() - PosInfo.downRay(world, pos, 16);

    if (maxY - minY < 10) {
      return false;
    }

    int height = Mth.nextInt(random, 5, 25);

    pos = new BlockPos(
        pos.getX(),
        Mth.nextInt(random, minY, minY + height / 2),
        pos.getZ());

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
