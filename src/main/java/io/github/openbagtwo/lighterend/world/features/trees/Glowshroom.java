package io.github.openbagtwo.lighterend.world.features.trees;

import io.github.openbagtwo.lighterend.blocks.Fur;
import io.github.openbagtwo.lighterend.blocks.GlowshroomCap;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import io.github.openbagtwo.lighterend.utils.Flags;
import io.github.openbagtwo.lighterend.utils.MiscUtils;
import io.github.openbagtwo.lighterend.utils.math.MathUtils;
import io.github.openbagtwo.lighterend.utils.math.sdf.SDF;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFCoordsModify;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFFlatWave;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFScale;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFScale3D;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFSmoothUnion;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFSubtract;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFTranslate;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFUnion;
import io.github.openbagtwo.lighterend.utils.math.sdf.primitives.SDFCappedCone;
import io.github.openbagtwo.lighterend.utils.math.sdf.primitives.SDFSphere;
import io.github.openbagtwo.lighterend.world.gen.noise.OpenSimplexNoise;
import java.util.List;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.joml.Vector3f;

public class Glowshroom extends Feature<NoneFeatureConfiguration> {

  public Glowshroom() {
    super(NoneFeatureConfiguration.CODEC);
  }

  private static final Function<BlockState, Boolean> REPLACE;
  private static final Vector3f CENTER = new Vector3f();
  private static final SDF.BinaryOperator FUNCTION;
  private static final SDFTranslate HEAD_POS;
  private static final SDFFlatWave ROOTS_ROT;

  private static final SDF.Primitive CONE1;
  private static final SDF.Primitive CONE2;
  private static final SDF.Primitive CONE_GLOW;
  private static final SDF.Primitive ROOTS;

  @Override
  public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featureConfig) {
    final RandomSource random = featureConfig.random();
    final BlockPos blockPos = featureConfig.origin();
    final WorldGenLevel world = featureConfig.level();
    BlockState down = world.getBlockState(blockPos.below());
    if (!down.is(LighterEndTags.END_SOIL)) {
      return false;
    }

    CONE1.setBlock(LighterEndBlocks.GLOWSHROOM_CAP);
    CONE2.setBlock(LighterEndBlocks.GLOWSHROOM_CAP);
    CONE_GLOW.setBlock(LighterEndBlocks.GLOWSHROOM_HYMENOPHORE);
    ROOTS.setBlock(LighterEndBlocks.GLOWSHROOM.wood);

    float height = Mth.nextFloat(random, 10F, 25F);
    int count = Mth.floor(height / 4);
    List<Vector3f> spline = MathUtils.makeSpline(0, 0, 0, 0, height, 0, count);
    MathUtils.offsetParts(spline, random, 1F, 0, 1F);
    SDF sdf = MathUtils.buildSDF(spline, 2.1F, 1.5F,
        (pos) -> LighterEndBlocks.GLOWSHROOM.log.defaultBlockState());
    Vector3f pos = spline.get(spline.size() - 1);
    float scale = Mth.nextFloat(random, 0.75F, 1.1F);

    if (!MathUtils.canGenerate(spline, scale, blockPos, world, REPLACE)) {
      return false;
    }
    world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Flags.SILENT);

    CENTER.set(blockPos.getX(), 0, blockPos.getZ());
    HEAD_POS.setTranslate(pos.x(), pos.y(), pos.z());
    ROOTS_ROT.setAngle(random.nextFloat() * MathUtils.PI2);
    FUNCTION.setSourceA(sdf);

    new SDFScale().setScale(scale).setSource(FUNCTION).setReplaceFunction(REPLACE)
        .addPostProcess(
            (info) -> {
              if (
                  info.getState().is(LighterEndBlocks.GLOWSHROOM.log)
                      || info.getState().is(LighterEndBlocks.GLOWSHROOM.wood)
              ) {
                if (
                    random.nextBoolean()
                        && info.getStateUp().getBlock() == LighterEndBlocks.GLOWSHROOM_CAP
                ) {
                  info.setState(LighterEndBlocks.GLOWSHROOM_CAP.defaultBlockState()
                      .setValue(GlowshroomCap.TRANSITION, true));
                  return info.getState();
                } else if (
                    !(
                        info.getStateUp().is(LighterEndBlocks.GLOWSHROOM.log)
                            || info.getStateUp().is(LighterEndBlocks.GLOWSHROOM.wood)
                    ) ||
                        !(
                            info.getStateDown().is(LighterEndBlocks.GLOWSHROOM.log)
                                || info.getStateDown().is(LighterEndBlocks.GLOWSHROOM.wood)
                        )
                ) {
                  info.setState(LighterEndBlocks.GLOWSHROOM.wood.defaultBlockState());
                  return info.getState();
                }
              } else if (info.getState().getBlock() == LighterEndBlocks.GLOWSHROOM_CAP) {
                if (
                    info.getStateDown().is(LighterEndBlocks.GLOWSHROOM.log)
                        || info.getStateDown().is(LighterEndBlocks.GLOWSHROOM.wood)
                ) {
                  info.setState(LighterEndBlocks.GLOWSHROOM_CAP.defaultBlockState()
                      .setValue(GlowshroomCap.TRANSITION, true));
                  return info.getState();
                }

                info.setState(LighterEndBlocks.GLOWSHROOM_CAP.defaultBlockState());
                return info.getState();
              } else if (info.getState().getBlock() == LighterEndBlocks.GLOWSHROOM_HYMENOPHORE) {
                for (Direction dir : Direction.Plane.HORIZONTAL) {
                  if (info.getState(dir) == Blocks.AIR.defaultBlockState()) {
                    info.setBlockPos(
                        info.getPos().relative(dir),
                        LighterEndBlocks.GLOWSHROOM_FUR.defaultBlockState().setValue(Fur.FACING, dir)
                    );
                  }
                }

                if (info.getStateDown().getBlock() != LighterEndBlocks.GLOWSHROOM_HYMENOPHORE) {
                  info.setBlockPos(
                      info.getPos().below(),
                      LighterEndBlocks.GLOWSHROOM_FUR.defaultBlockState()
                          .setValue(Fur.FACING, Direction.DOWN)
                  );
                }
              }
              return info.getState();
            }
        ).fillRecursive(world, blockPos);

    return true;
  }

  static {
    SDFCappedCone cone1 = new SDFCappedCone().setHeight(2.5F).setRadius1(1.5F).setRadius2(2.5F);
    SDFCappedCone cone2 = new SDFCappedCone().setHeight(3F).setRadius1(2.5F).setRadius2(13F);
    SDF posedCone2 = new SDFTranslate().setTranslate(0, 5, 0).setSource(cone2);
    SDF posedCone3 = new SDFTranslate().setTranslate(0, 12F, 0)
        .setSource(new SDFScale().setScale(2).setSource(cone2));
    SDF upCone = new SDFSubtract().setSourceA(posedCone2).setSourceB(posedCone3);
    SDF wave = new SDFFlatWave().setRaysCount(12).setIntensity(1.3F).setSource(upCone);
    SDF cones = new SDFSmoothUnion().setRadius(3).setSourceA(cone1).setSourceB(wave);

    CONE1 = cone1;
    CONE2 = cone2;

    SDF innerCone = new SDFTranslate().setTranslate(0, 1.25F, 0).setSource(upCone);
    innerCone = new SDFScale3D().setScale(1.2F, 1F, 1.2F).setSource(innerCone);
    cones = new SDFUnion().setSourceA(cones).setSourceB(innerCone);

    SDF glowCone = new SDFCappedCone().setHeight(3F).setRadius1(2F).setRadius2(12.5F);
    CONE_GLOW = (SDF.Primitive) glowCone;
    glowCone = new SDFTranslate().setTranslate(0, 4.25F, 0).setSource(glowCone);
    glowCone = new SDFSubtract().setSourceA(glowCone).setSourceB(posedCone3);

    cones = new SDFUnion().setSourceA(cones).setSourceB(glowCone);

    OpenSimplexNoise noise = new OpenSimplexNoise(1234);
    cones = new SDFCoordsModify().setFunction(
        (pos) -> {
          float dist = Mth.sqrt(pos.x() * pos.x() + pos.z() * pos.z());
          float y = pos.y() + (float) noise.eval(
              pos.x() * 0.1 + CENTER.x(),
              pos.z() * 0.1 + CENTER.z()
          ) * dist * 0.3F - dist * 0.15F;
          pos.set(pos.x(), y, pos.z());
        }
    ).setSource(cones);

    HEAD_POS = (SDFTranslate) new SDFTranslate().setSource(
        new SDFTranslate().setTranslate(0, 2.5F, 0)
            .setSource(cones));

    SDF roots = new SDFSphere().setRadius(4F);
    ROOTS = (SDF.Primitive) roots;
    roots = new SDFScale3D().setScale(1, 0.7F, 1).setSource(roots);
    ROOTS_ROT = (SDFFlatWave) new SDFFlatWave().setRaysCount(5).setIntensity(1.5F).setSource(roots);

    FUNCTION = new SDFSmoothUnion().setRadius(4)
        .setSourceB(new SDFUnion().setSourceA(HEAD_POS).setSourceB(ROOTS_ROT));

    REPLACE = MiscUtils::replaceableOrPlant;
  }

}
