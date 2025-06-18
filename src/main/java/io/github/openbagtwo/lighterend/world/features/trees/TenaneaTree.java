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
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.state.property.Properties;
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

public class TenaneaTree extends Feature<DefaultFeatureConfig> {

  private static final Function<BlockState, Boolean> REPLACE;
  private static final Function<BlockState, Boolean> IGNORE;
  private static final List<Vector3f> SPLINE;

  public TenaneaTree() {
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

    float size = MathHelper.nextInt(random, 7, 10);
    int count = (int) (size * 0.45F);
    float var = MathUtils.PI2 / (float) (count * 3);
    float start = MathHelper.nextFloat(random, 0, MathUtils.PI2);
    for (int i = 0; i < count; i++) {
      float angle =
          (float) i / (float) count * MathUtils.PI2 + MathHelper.nextFloat(random, 0, var) + start;
      List<Vector3f> spline = MathUtils.copySpline(SPLINE);
      MathUtils.rotateSpline(spline, angle);
      MathUtils.scale(spline, size + MathHelper.nextFloat(random, 0, size * 0.5F));
      MathUtils.offsetParts(spline, random, 1F, 0, 1F);
      MathUtils.fillSpline(spline, world, LighterEndBlocks.TENANEA.wood.getDefaultState(),
          pos,
          REPLACE);
      Vector3f last = spline.getLast();
      float leavesRadius = (size * 0.3F + MathHelper.nextFloat(random, 0.8F, 1.5F)) * 1.4F;
      OpenSimplexNoise noise = new OpenSimplexNoise(random.nextLong());
      leavesBall(world, pos.add((int) last.x(), (int) last.y(), (int) last.z()), leavesRadius,
          random, noise);
    }

    return true;
  }

  private void leavesBall(
      StructureWorldAccess world,
      BlockPos pos,
      float radius,
      Random random,
      OpenSimplexNoise noise
  ) {
    SDF sphere = new SDFSphere().setRadius(radius)
        .setBlock(LighterEndBlocks.TENANEA_LEAVES.getDefaultState()
            .with(LeavesBlock.DISTANCE, 6));
    SDF sub = new SDFScale().setScale(5).setSource(sphere);
    sub = new SDFTranslate().setTranslate(0, -radius * 5, 0).setSource(sub);
    sphere = new SDFSubtract().setSourceA(sphere).setSourceB(sub);
    sphere = new SDFScale3D().setScale(1, 0.75F, 1).setSource(sphere);
    sphere = new SDFDisplace().setFunction((vec) -> (float) noise.eval(
        vec.x() * 0.2,
        vec.y() * 0.2,
        vec.z() * 0.2
    ) * 2F).setSource(sphere);
    sphere = new SDFDisplace().setFunction((vec) -> MathHelper.nextFloat(random, -1.5F, 1.5F))
        .setSource(sphere);

    Mutable mut = new Mutable();
    for (Direction d1 : PosInfo.HORIZONTAL) {
      BlockPos p = mut.set(pos).move(Direction.UP).move(d1).toImmutable();
      world.setBlockState(p, LighterEndBlocks.TENANEA.wood.getDefaultState(), Flags.SILENT);
      for (Direction d2 : PosInfo.HORIZONTAL) {
        mut.set(p).move(Direction.UP).move(d2);
        world.setBlockState(p, LighterEndBlocks.TENANEA.wood.getDefaultState(), Flags.SILENT);
      }
    }

    BlockState top = LighterEndBlocks.TENANEA_FLOWER.getDefaultState()
        .with(Properties.TIP, false);
    BlockState middle = LighterEndBlocks.TENANEA_FLOWER.getDefaultState()
        .with(Properties.TIP, false);
    BlockState bottom = LighterEndBlocks.TENANEA_FLOWER.getDefaultState()
        .with(Properties.TIP, true);

    List<BlockPos> support = Lists.newArrayList();
    sphere.addPostProcess((info) -> {
      if (random.nextInt(6) == 0 && info.getStateDown().isAir()) {
        BlockPos d = info.getPos().down();
        support.add(d);
      }
      if (random.nextInt(5) == 0) {
        for (Direction dir : Direction.values()) {
          BlockState state = info.getState(dir, 2);
          if (state.isAir()) {
            return info.getState();
          }
        }
        info.setState(LighterEndBlocks.TENANEA.wood.getDefaultState());
      }

      if (Arrays.asList(LighterEndBlocks.TENANEA.log, LighterEndBlocks.TENANEA.wood)
          .contains(info.getState().getBlock())) {
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
    world.setBlockState(pos, LighterEndBlocks.TENANEA.wood.getDefaultState(), Flags.SILENT);

    support.forEach((bpos) -> {
      BlockState state = world.getBlockState(bpos);
      if (state.isAir()) {
        int count = MathHelper.nextInt(random, 3, 8);
        mut.set(bpos);
        if (world.getBlockState(mut.up()).isOf(LighterEndBlocks.TENANEA_LEAVES)) {
          world.setBlockState(mut, top, Flags.SILENT);
          for (int i = 1; i < count; i++) {
            mut.setY(mut.getY() - 1);
            if (world.isAir(mut.down())) {
              world.setBlockState(mut, middle, Flags.SILENT);
            } else {
              break;
            }
          }
          world.setBlockState(mut, bottom, Flags.SILENT);
        }
      }
    });
  }

  static {
    REPLACE = (state) -> {
      if (state.getBlock() == LighterEndBlocks.TENANEA_LEAVES) {
        return true;
      }
      return MiscUtils.replaceableOrPlant(state);
    };

    IGNORE = (state) -> Arrays.asList(LighterEndBlocks.TENANEA.log, LighterEndBlocks.TENANEA.wood)
        .contains(state.getBlock());

    SPLINE = Lists.newArrayList(
        new Vector3f(0.00F, 0.00F, 0.00F),
        new Vector3f(0.10F, 0.35F, 0.00F),
        new Vector3f(0.20F, 0.50F, 0.00F),
        new Vector3f(0.30F, 0.55F, 0.00F),
        new Vector3f(0.42F, 0.70F, 0.00F),
        new Vector3f(0.50F, 1.00F, 0.00F)
    );
  }
}
