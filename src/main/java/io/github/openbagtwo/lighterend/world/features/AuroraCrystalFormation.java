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
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.joml.Vector3f;

public class AuroraCrystalFormation extends Feature<DefaultFeatureConfig> {

  public AuroraCrystalFormation() {
    super(DefaultFeatureConfig.CODEC);
  }

  @Override
  public boolean generate(FeatureContext<DefaultFeatureConfig> featureConfig) {
    final Random random = featureConfig.getRandom();
    BlockPos pos = featureConfig.getOrigin();
    final StructureWorldAccess world = featureConfig.getWorld();
    int maxY = pos.getY() + PosInfo.upRay(world, pos, 16);
    int minY = pos.getY() - PosInfo.downRay(world, pos, 16);

    if (maxY - minY < 10) {
      return false;
    }

    int height = MathHelper.nextInt(random, 5, 25);

    pos = new BlockPos(
        pos.getX(),
        MathHelper.nextInt(random, minY, minY + height / 2),
        pos.getZ());

    SDF prism = new SDFHexPrism().setHeight(height)
        .setRadius(MathHelper.nextFloat(random, 1.7F, 3F))
        .setBlock(LighterEndBlocks.AURORA_CRYSTAL);
    Vector3f vec = MathUtils.randomHorizontal(random);
    prism = new SDFRotate().setRotation(vec, random.nextFloat()).setSource(prism);
    prism.setReplaceFunction((state) ->
        state.isIn(LighterEndTags.END_STONES)
            || state.isIn(LighterEndTags.END_SOIL)
            || MiscUtils.replaceableOrPlant(state)
            || state.isIn(BlockTags.LEAVES));
    prism.fillRecursive(world, pos);
    world.setBlockState(pos, LighterEndBlocks.AURORA_CRYSTAL.getDefaultState(), Flags.SILENT);

    return true;
  }
}
