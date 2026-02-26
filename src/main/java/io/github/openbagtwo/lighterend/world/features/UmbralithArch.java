package io.github.openbagtwo.lighterend.world.features;

import com.mojang.math.Axis;
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
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class UmbralithArch extends Feature<NoneFeatureConfiguration> {

  public UmbralithArch() {
    super(NoneFeatureConfiguration.CODEC);
  }

  @Override
  public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
    final WorldGenLevel world = featurePlaceContext.level();
    BlockPos origin = featurePlaceContext.origin();
    RandomSource random = featurePlaceContext.random();

    BlockPos pos = world.getHeightmapPos(Types.WORLD_SURFACE_WG,
        new BlockPos((origin.getX() & 0xFFFFFFF0) | 7, 0, (origin.getZ() & 0xFFFFFFF0) | 7));

    if (!world.getBlockState(pos.below(5)).is(LighterEndTags.END_STONES)) {
      return false;
    }

    float bigRadius = Mth.nextFloat(random, 10F, 20F);
    float smallRadius = Mth.nextFloat(random, 3F, 7F);
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
        return LighterEndBlocks.UMBRALITH.baseBlock.defaultBlockState();
      }
      return info.getState();
    });

    float side = (bigRadius + smallRadius + 3F) * 2;
    if (side > 47) {
      side = 47;
    }
    arch.fillArea(world, pos, AABB.ofSize(Vec3.atCenterOf(pos), side, side, side));

    return true;
  }

  public static class Thin extends Feature<NoneFeatureConfiguration> {

    public Thin() {
      super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
      final WorldGenLevel world = featurePlaceContext.level();
      BlockPos origin = featurePlaceContext.origin();
      RandomSource random = featurePlaceContext.random();

      BlockPos pos = world.getHeightmapPos(Types.WORLD_SURFACE_WG,
          new BlockPos((origin.getX() & 0xFFFFFFF0) | 7, 0, (origin.getZ() & 0xFFFFFFF0) | 7));

      if (!world.getBlockState(pos.below(5)).is(LighterEndTags.END_STONES)) {
        return false;
      }

      SDF sdf = null;
      float bigRadius = Mth.nextFloat(random, 15F, 20F);
      float variation = bigRadius * 0.3F;
      int count = Mth.nextInt(random, 2, 4);

      for (int i = 0; i < count; i++) {
        float smallRadius = Mth.nextFloat(random, 0.6F, 1.3F);
        SDF arch = new SDFTorus().setBigRadius(bigRadius - random.nextFloat() * variation)
            .setSmallRadius(smallRadius)
            .setBlock(LighterEndBlocks.UMBRALITH.baseBlock);
        float angle =
            (i - count * 0.5F) * 0.3F + random.nextFloat() * 0.05F + (float) Math.PI * 0.5F;
        arch = new SDFRotate().setRotation(Axis.XP, angle).setSource(arch);
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
        return Mth.clamp(offset * 3, -10F, 0F);
      }).setSource(sdf);

      float side = (bigRadius + 2.5F) * 2;
      if (side > 47) {
        side = 47;
      }
      sdf.fillArea(world, pos, AABB.ofSize(Vec3.atCenterOf(pos), side, side, side));
      return true;
    }
  }
}
