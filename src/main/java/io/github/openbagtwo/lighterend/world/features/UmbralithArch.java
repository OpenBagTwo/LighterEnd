package io.github.openbagtwo.lighterend.world.features;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import io.github.openbagtwo.lighterend.utils.math.MathUtils;
import io.github.openbagtwo.lighterend.utils.math.sdf.SDF;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFCoordsModify;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFDisplace;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFRotate;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFUnion;
import io.github.openbagtwo.lighterend.utils.math.sdf.primitives.SDFTorus;
import io.github.openbagtwo.lighterend.world.gen.noise.OpenSimplexNoise;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class UmbralithArch extends Feature<DefaultFeatureConfig> {

  public UmbralithArch() {
    super(DefaultFeatureConfig.CODEC);
  }

  @Override
  public boolean generate(FeatureContext<DefaultFeatureConfig> featurePlaceContext) {
    final StructureWorldAccess world = featurePlaceContext.getWorld();
    BlockPos origin = featurePlaceContext.getOrigin();
    Random random = featurePlaceContext.getRandom();

    BlockPos pos = world.getTopPosition(Type.WORLD_SURFACE_WG,
        new BlockPos((origin.getX() & 0xFFFFFFF0) | 7, 0, (origin.getZ() & 0xFFFFFFF0) | 7));

    if (!world.getBlockState(pos.down(5)).isIn(LighterEndTags.END_STONES)) {
      return false;
    }

    float bigRadius = MathHelper.nextFloat(random, 10F, 20F);
    float smallRadius = MathHelper.nextFloat(random, 3F, 7F);
    if (smallRadius + bigRadius > 23) {
      smallRadius = 23 - bigRadius;
    }
    SDF arch = new SDFTorus().setBigRadius(bigRadius).setSmallRadius(smallRadius)
        .setBlock(LighterEndBlocks.UMBRALITH.baseBlock);
    arch = new SDFRotate()
        .setRotation(MathUtils.randomHorizontal(random), (float) Math.PI * 0.5F)
        .setSource(arch);

    final float smallRadiusF = smallRadius;
    OpenSimplexNoise noise = new OpenSimplexNoise(random.nextLong());
    arch = new SDFDisplace().setFunction((vec) -> (float) (Math.abs(noise.eval(
        vec.x() * 0.1,
        vec.y() * 0.1,
        vec.z() * 0.1
    )) * 3F + Math.abs(noise.eval(
        vec.x() * 0.3,
        vec.y() * 0.3 + 100,
        vec.z() * 0.3
    )) * 1.3F) - smallRadiusF * Math.abs(1 - vec.y() / bigRadius)).setSource(arch);

    arch.addPostProcess((info) -> {
      if (info.getStateUp().isAir()) {
        return LighterEndBlocks.UMBRALITH.baseBlock.getDefaultState();
      }
      return info.getState();
    });

    float side = (bigRadius + smallRadius + 3F) * 2;
    if (side > 47) {
      side = 47;
    }
    arch.fillArea(world, pos, Box.of(Vec3d.ofCenter(pos), side, side, side));

    return true;
  }

  public static class Thin extends Feature<DefaultFeatureConfig> {

    public Thin() {
      super(DefaultFeatureConfig.CODEC);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> featurePlaceContext) {
      final StructureWorldAccess world = featurePlaceContext.getWorld();
      BlockPos origin = featurePlaceContext.getOrigin();
      Random random = featurePlaceContext.getRandom();

      BlockPos pos = world.getTopPosition(Type.WORLD_SURFACE_WG,
          new BlockPos((origin.getX() & 0xFFFFFFF0) | 7, 0, (origin.getZ() & 0xFFFFFFF0) | 7));

      if (!world.getBlockState(pos.down(5)).isIn(LighterEndTags.END_STONES)) {
        return false;
      }

      SDF sdf = null;
      float bigRadius = MathHelper.nextFloat(random, 15F, 20F);
      float variation = bigRadius * 0.3F;
      int count = MathHelper.nextInt(random, 2, 4);

      for (int i = 0; i < count; i++) {
        float smallRadius = MathHelper.nextFloat(random, 0.6F, 1.3F);
        SDF arch = new SDFTorus().setBigRadius(bigRadius - random.nextFloat() * variation)
            .setSmallRadius(smallRadius)
            .setBlock(LighterEndBlocks.UMBRALITH.baseBlock);
        float angle =
            (i - count * 0.5F) * 0.3F + random.nextFloat() * 0.05F + (float) Math.PI * 0.5F;
        arch = new SDFRotate().setRotation(RotationAxis.POSITIVE_X, angle).setSource(arch);
        sdf = sdf == null ? arch : new SDFUnion().setSourceA(sdf).setSourceB(arch);
      }

      sdf = new SDFRotate().setRotation(MathUtils.randomHorizontal(random),
              random.nextFloat() * MathUtils.PI2)
          .setSource(sdf);

      OpenSimplexNoise noise = new OpenSimplexNoise(random.nextLong());
      sdf = new SDFCoordsModify().setFunction(vec -> {
        float dx = (float) noise.eval(vec.y() * 0.02, vec.z() * 0.02);
        float dy = (float) noise.eval(vec.x() * 0.02, vec.z() * 0.02);
        float dz = (float) noise.eval(vec.x() * 0.02, vec.y() * 0.02);
        vec.add(dx * 10, dy * 10, dz * 10);
      }).setSource(sdf);
      sdf = new SDFDisplace().setFunction(vec -> {
        float offset = vec.y() / bigRadius - 0.5F;
        return MathHelper.clamp(offset * 3, -10F, 0F);
      }).setSource(sdf);

      float side = (bigRadius + 2.5F) * 2;
      if (side > 47) {
        side = 47;
      }
      sdf.fillArea(world, pos, Box.of(Vec3d.ofCenter(pos), side, side, side));
      return true;
    }
  }
}
