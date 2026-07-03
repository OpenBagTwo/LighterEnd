package io.github.openbagtwo.lighterend.world.features.trees;

import com.google.common.collect.Lists;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import io.github.openbagtwo.lighterend.utils.Flags;
import io.github.openbagtwo.lighterend.utils.MiscUtils;
import io.github.openbagtwo.lighterend.utils.PosInfo;
import io.github.openbagtwo.lighterend.utils.math.MathUtils;
import io.github.openbagtwo.lighterend.utils.math.sdf.SDF;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFDisplace;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFScale;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFScale3D;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFSubtract;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFTranslate;
import io.github.openbagtwo.lighterend.utils.math.sdf.primitives.SDFSphere;
import io.github.openbagtwo.lighterend.world.gen.noise.OpenSimplexNoise;
import java.util.List;
import java.util.function.Function;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.joml.Vector3f;

public class DragonTree extends Feature<DefaultFeatureConfig> {

  private static final Function<BlockState, Boolean> REPLACE;
  private static final Function<BlockState, Boolean> IGNORE;
  private static final Function<PosInfo, BlockState> POST;
  private static final List<Vector3f> BRANCH;
  private static final List<Vector3f> SIDE1;
  private static final List<Vector3f> SIDE2;
  private static final List<Vector3f> ROOT;

  public DragonTree() {
    super(DefaultFeatureConfig.CODEC);
  }

  @Override
  public boolean generate(FeatureContext<DefaultFeatureConfig> featureConfig) {
    final Random random = featureConfig.getRandom();
    final BlockPos pos = featureConfig.getOrigin();
    final StructureWorldAccess world = featureConfig.getWorld();
    if (!world.getBlockState(pos.down()).isIn(LighterEndTags.END_SOIL)) {
      return false;
    }

    float size = MathHelper.nextFloat(random, 10, 25);
    List<Vector3f> spline = MathUtils.makeSpline(0, 0, 0, 0, size, 0, 6);
    MathUtils.offsetParts(spline, random, 1F, 0, 1F);

    if (!MathUtils.canGenerate(spline, pos, world, REPLACE)) {
      return false;
    }
    world.setBlockState(pos, Blocks.AIR.getDefaultState(), Flags.SILENT);

    Vector3f last = MathUtils.getPos(spline, 3.5F);
    OpenSimplexNoise noise = new OpenSimplexNoise(random.nextLong());
    float radius = size * MathHelper.nextFloat(random, 0.5F, 0.7F);
    makeCap(world, pos.add((int) last.x(), (int) last.y(), (int) last.z()), radius, random, noise);

    last = spline.get(0);
    makeRoots(world, pos.add((int) last.x(), (int) last.y(), (int) last.z()), radius, random);

    radius = MathHelper.nextFloat(random, 1.2F, 2.3F);
    SDF function = MathUtils.buildSDF(
        spline,
        radius,
        1.2F,
        (bpos) -> LighterEndBlocks.DRAGON.wood.getDefaultState()
    );

    function.setReplaceFunction(REPLACE);
    function.addPostProcess(POST);
    function.fillRecursiveIgnore(world, pos, IGNORE);

    return true;
  }

  private void makeCap(StructureWorldAccess world, BlockPos pos, float radius, Random random,
      OpenSimplexNoise noise) {
    int count = (int) radius;
    int offset = (int) (BRANCH.get(BRANCH.size() - 1).y() * radius);
    for (int i = 0; i < count; i++) {
      float angle = (float) i / (float) count * MathHelper.TAU;
      float scale = radius * MathHelper.nextFloat(random, 0.85F, 1.15F);

      List<Vector3f> branch = MathUtils.copySpline(BRANCH);
      MathUtils.rotateSpline(branch, angle);
      MathUtils.scale(branch, scale);
      MathUtils.fillSpline(branch, world, LighterEndBlocks.DRAGON.wood.getDefaultState(), pos,
          REPLACE);

      branch = MathUtils.copySpline(SIDE1);
      MathUtils.rotateSpline(branch, angle);
      MathUtils.scale(branch, scale);
      MathUtils.fillSpline(branch, world, LighterEndBlocks.DRAGON.wood.getDefaultState(), pos,
          REPLACE);

      branch = MathUtils.copySpline(SIDE2);
      MathUtils.rotateSpline(branch, angle);
      MathUtils.scale(branch, scale);
      MathUtils.fillSpline(branch, world, LighterEndBlocks.DRAGON.wood.getDefaultState(), pos,
          REPLACE);
    }
    leavesBall(world, pos.up(offset), radius * 1.15F + 2, random, noise);
  }

  private void makeRoots(StructureWorldAccess world, BlockPos pos, float radius, Random random) {
    int count = (int) (radius * 1.5F);
    for (int i = 0; i < count; i++) {
      float angle = (float) i / (float) count * MathHelper.TAU;
      float scale = radius * MathHelper.nextFloat(random, 0.85F, 1.15F);

      List<Vector3f> branch = MathUtils.copySpline(ROOT);
      MathUtils.rotateSpline(branch, angle);
      MathUtils.scale(branch, scale);
      Vector3f last = branch.get(branch.size() - 1);
      if (world.getBlockState(pos.add((int) last.x(), (int) last.y(), (int) last.z()))
          .isIn(LighterEndTags.END_STONES)) {
        MathUtils.fillSpline(
            branch,
            world,
            LighterEndBlocks.DRAGON.wood.getDefaultState(),
            pos,
            REPLACE
        );
      }
    }
  }

  private void leavesBall(
      StructureWorldAccess world,
      BlockPos pos,
      float radius,
      Random random,
      OpenSimplexNoise noise
  ) {
    SDF sphere = new SDFSphere().setRadius(radius)
        .setBlock(LighterEndBlocks.DRAGON_LEAVES.getDefaultState()
            .with(LeavesBlock.DISTANCE, 6));
    SDF sub = new SDFScale().setScale(5).setSource(sphere);
    sub = new SDFTranslate().setTranslate(0, -radius * 5, 0).setSource(sub);
    sphere = new SDFSubtract().setSourceA(sphere).setSourceB(sub);
    sphere = new SDFScale3D().setScale(1, 0.5F, 1).setSource(sphere);
    sphere = new SDFDisplace().setFunction((vec) -> (float) noise.eval(
        vec.x() * 0.2,
        vec.y() * 0.2,
        vec.z() * 0.2
    ) * 1.5F).setSource(sphere);
    sphere = new SDFDisplace().setFunction((vec) -> random.nextFloat() * 3F - 1.5F)
        .setSource(sphere);
    Mutable mut = new Mutable();
    sphere.addPostProcess((info) -> {
      if (random.nextInt(5) == 0) {
        for (Direction dir : Direction.values()) {
          BlockState state = info.getState(dir, 2);
          if (state.isAir()) {
            return info.getState();
          }
        }
        info.setState(LighterEndBlocks.DRAGON.wood.getDefaultState());
        for (int x = -6; x < 7; x++) {
          int ax = Math.abs(x);
          mut.setX(x + info.getPos().getX());
          for (int z = -6; z < 7; z++) {
            int az = Math.abs(z);
            mut.setZ(z + info.getPos().getZ());
            for (int y = -6; y < 7; y++) {
              int ay = Math.abs(y);
              int d = ax + ay + az;
              if (d < 7) {
                mut.setY(y + info.getPos().getY());
                BlockState state = info.getState(mut);
                if (state.getBlock() instanceof LeavesBlock) {
                  int distance = state.get(LeavesBlock.DISTANCE);
                  if (d < distance) {
                    info.setState(mut, state.with(LeavesBlock.DISTANCE, d));
                  }
                }
              }
            }
          }
        }
      }
      return info.getState();
    });
    sphere.fillRecursiveIgnore(world, pos, IGNORE);

    if (radius > 5) {
      int count = (int) (radius * 2.5F);
      for (int i = 0; i < count; i++) {
        BlockPos p = pos.add(
            (int) (random.nextGaussian() * 1),
            (int) (random.nextGaussian() * 1),
            (int) (random.nextGaussian() * 1)
        );
        boolean place = true;
        for (Direction d : Direction.values()) {
          BlockState state = world.getBlockState(p.offset(d));
          if (
              !state.isOf(LighterEndBlocks.DRAGON.wood)
                  && !state.isOf(LighterEndBlocks.DRAGON.log)
                  && !state.isOf(LighterEndBlocks.DRAGON_LEAVES)
          ) {
            place = false;
            break;
          }
        }
        if (place) {
          world.setBlockState(p, LighterEndBlocks.DRAGON.wood.getDefaultState(), Flags.SILENT);
        }
      }
    }

    world.setBlockState(pos, LighterEndBlocks.DRAGON.wood.getDefaultState(), Flags.SILENT);
  }

  static {
    REPLACE = (state) -> {
			/*if (state.is(CommonBlockTags.END_STONES)) {
				return true;
			}*/
      if (state.getBlock() == LighterEndBlocks.DRAGON_LEAVES) {
        return true;
      }
      return MiscUtils.replaceableOrPlant(state);
    };

    IGNORE = state -> (
        state.isOf(LighterEndBlocks.DRAGON.wood) || state.isOf(LighterEndBlocks.DRAGON.log)
    );

    POST = (info) -> {
      if (
          (
              info.getStateUp().isOf(LighterEndBlocks.DRAGON.wood)
                  || info.getStateUp().isOf(LighterEndBlocks.DRAGON.log)
          ) && (
              info.getStateDown().isOf(LighterEndBlocks.DRAGON.wood)
                  || info.getStateUp().isOf(LighterEndBlocks.DRAGON.log)
          )
      ) {
        return LighterEndBlocks.DRAGON.log.getDefaultState();
      }

      return info.getState();
    };

    BRANCH = Lists.newArrayList(
        new Vector3f(0, 0, 0),
        new Vector3f(0.1F, 0.3F, 0),
        new Vector3f(0.4F, 0.6F, 0),
        new Vector3f(0.8F, 0.8F, 0),
        new Vector3f(1, 1, 0)
    );
    SIDE1 = Lists.newArrayList(new Vector3f(0.4F, 0.6F, 0), new Vector3f(0.8F, 0.8F, 0),
        new Vector3f(1, 1, 0));
    SIDE2 = MathUtils.copySpline(SIDE1);

    Vector3f offset1 = new Vector3f(-0.4F, -0.6F, 0);
    Vector3f offset2 = new Vector3f(0.4F, 0.6F, 0);

    MathUtils.offset(SIDE1, offset1);
    MathUtils.offset(SIDE2, offset1);
    MathUtils.rotateSpline(SIDE1, 0.5F);
    MathUtils.rotateSpline(SIDE2, -0.5F);
    MathUtils.offset(SIDE1, offset2);
    MathUtils.offset(SIDE2, offset2);

    ROOT = Lists.newArrayList(
        new Vector3f(0F, 1F, 0),
        new Vector3f(0.1F, 0.7F, 0),
        new Vector3f(0.3F, 0.3F, 0),
        new Vector3f(0.7F, 0.05F, 0),
        new Vector3f(0.8F, -0.2F, 0)
    );
    MathUtils.offset(ROOT, new Vector3f(0, -0.45F, 0));
  }

}
