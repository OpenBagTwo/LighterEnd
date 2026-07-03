package io.github.openbagtwo.lighterend.world.features.trees;

import com.google.common.collect.Lists;
import io.github.openbagtwo.lighterend.blocks.UmbrellaMembrane;
import io.github.openbagtwo.lighterend.blocks.UmbrellaTreeCluster;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import io.github.openbagtwo.lighterend.utils.Flags;
import io.github.openbagtwo.lighterend.utils.MiscUtils;
import io.github.openbagtwo.lighterend.utils.math.MathUtils;
import io.github.openbagtwo.lighterend.utils.math.sdf.SDF;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFFlatWave;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFScale;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFScale3D;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFSmoothUnion;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFSubtract;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFTranslate;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFUnion;
import io.github.openbagtwo.lighterend.utils.math.sdf.primitives.SDFSphere;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import net.minecraft.block.BlockState;
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

public class UmbrellaTree extends Feature<DefaultFeatureConfig> {

  private static final Function<BlockState, Boolean> REPLACE;
  private static final List<Vector3f> SPLINE;
  private static final List<Vector3f> ROOT;

  public UmbrellaTree() {
    super(DefaultFeatureConfig.CODEC);
  }

  @Override
  public boolean generate(FeatureContext<DefaultFeatureConfig> featureConfig) {
    final Random random = featureConfig.getRandom();
    final BlockPos pos = featureConfig.getOrigin();
    final StructureWorldAccess world = featureConfig.getWorld();
    final DefaultFeatureConfig config = featureConfig.getConfig();
    if (!world.getBlockState(pos.down()).isIn(LighterEndTags.END_SOIL)) {
      return false;
    }

    BlockState wood = LighterEndBlocks.UMBRELLA.wood.getDefaultState();
    BlockState membrane = LighterEndBlocks.UMBRELLA_MEMBRANE.getDefaultState()
        .with(UmbrellaMembrane.COLOR, 1);
    BlockState center = LighterEndBlocks.UMBRELLA_MEMBRANE.getDefaultState()
        .with(UmbrellaMembrane.COLOR, 0);
    BlockState fruit = LighterEndBlocks.UMBRELLA_TREE_CLUSTER.getDefaultState()
        .with(UmbrellaTreeCluster.NATURAL, true);

    float size = MathHelper.nextFloat(random, 10, 20);
    int count = (int) (size * 0.15F);
    float var = MathUtils.PI2 / (float) (count * 3);
    float start = MathHelper.nextFloat(random, 0, MathUtils.PI2);
    SDF sdf = null;
    List<Center> centers = Lists.newArrayList();

    float scale = 1;
    if (config != null) {
      scale = MathHelper.nextFloat(random, 1F, 1.7F);
    }

    for (int i = 0; i < count; i++) {
      float angle =
          (float) i / (float) count * MathUtils.PI2 + MathHelper.nextFloat(random, 0, var) + start;
      List<Vector3f> spline = MathUtils.copySpline(SPLINE);
      float sizeXZ = size + MathHelper.nextFloat(random, 0, size * 0.5F) * 0.7F;
      MathUtils.scale(spline, sizeXZ, sizeXZ * MathHelper.nextFloat(random, 1F, 2F), sizeXZ);
      MathUtils.rotateSpline(spline, angle);
      MathUtils.offsetParts(spline, random, 0.5F, 0, 0.5F);

      if (MathUtils.canGenerate(spline, pos, world, REPLACE)) {
        float rScale = (scale - 1) * 0.4F + 1;
        SDF branch = MathUtils.buildSDF(spline, 1.2F * rScale, 0.8F * rScale, (bpos) -> wood);

        Vector3f vec = spline.get(spline.size() - 1);
        float radius = size + MathHelper.nextFloat(random, 0, size * 0.5F) * 0.4F;

        sdf = (sdf == null) ? branch : new SDFUnion().setSourceA(sdf).setSourceB(branch);
        SDF mem = makeMembrane(radius, random, membrane, center);

        float px = MathHelper.floor(vec.x()) + 0.5F;
        float py = MathHelper.floor(vec.y()) + 0.5F;
        float pz = MathHelper.floor(vec.z()) + 0.5F;
        mem = new SDFTranslate().setTranslate(px, py, pz).setSource(mem);
        sdf = new SDFSmoothUnion().setRadius(2).setSourceA(sdf).setSourceB(mem);
        centers.add(new Center(
            pos.getX() + (double) (px * scale),
            pos.getY() + (double) (py * scale),
            pos.getZ() + (double) (pz * scale),
            radius * scale
        ));
      }
    }

    if (sdf == null) {
      return false;
    }

    if (scale > 1) {
      sdf = new SDFScale().setScale(scale).setSource(sdf);
    }

    sdf.setReplaceFunction(REPLACE).addPostProcess((info) -> {
      if (
          Arrays.asList(LighterEndBlocks.UMBRELLA.wood, LighterEndBlocks.UMBRELLA.log)
              .contains(info.getStateUp().getBlock())
              && Arrays.asList(LighterEndBlocks.UMBRELLA.wood, LighterEndBlocks.UMBRELLA.log)
              .contains(info.getStateDown().getBlock())
      ) {
        return LighterEndBlocks.UMBRELLA.log.getDefaultState();
      } else if (info.getState().equals(membrane)) {
        Center min = centers.get(0);
        double d = Double.MAX_VALUE;
        BlockPos bpos = info.getPos();
        for (Center c : centers) {
          double d2 = c.distance(bpos.getX(), bpos.getZ());
          if (d2 < d) {
            d = d2;
            min = c;
          }
        }
        int color = MathHelper.floor(d / min.radius * 7);
        color = MathHelper.clamp(color, 1, 7);
        return info.getState().with(UmbrellaMembrane.COLOR, color);
      }
      return info.getState();
    }).fillRecursive(world, pos);
    makeRoots(world, pos, (size * 0.5F + 3) * scale, random, wood);

    for (Center c : centers) {
      if (!world.getBlockState(new BlockPos((int) c.px, (int) c.py, (int) c.pz)).isAir()) {
        count = MathHelper.floor(MathHelper.nextFloat(random, 5F, 10F) * scale);
        float startAngle = random.nextFloat() * MathUtils.PI2;
        for (int i = 0; i < count; i++) {
          float angle = (float) i / count * MathUtils.PI2 + startAngle;
          float dist = MathHelper.nextFloat(random, 1.5F, 2.5F) * scale;
          double px = c.px + Math.sin(angle) * dist;
          double pz = c.pz + Math.cos(angle) * dist;
          makeFruits(world, px, c.py - 1, pz, fruit);
        }
      }
    }

    return true;
  }

  private void makeRoots(StructureWorldAccess world, BlockPos pos, float radius, Random random,
      BlockState wood) {
    int count = (int) (radius * 1.5F);
    for (int i = 0; i < count; i++) {
      float angle = (float) i / (float) count * MathUtils.PI2;
      float scale = radius * MathHelper.nextFloat(random, 0.85F, 1.15F);

      List<Vector3f> branch = MathUtils.copySpline(ROOT);
      MathUtils.rotateSpline(branch, angle);
      MathUtils.scale(branch, scale);
      Vector3f last = branch.get(branch.size() - 1);
      if (world.getBlockState(pos.add((int) last.x(), (int) last.y(), (int) last.z()))
          .isIn(LighterEndTags.END_STONES)) {
        MathUtils.fillSplineForce(branch, world, wood, pos, REPLACE);
      }
    }
  }

  private SDF makeMembrane(
      float radius,
      Random random,
      BlockState membrane,
      BlockState center
  ) {
    SDF sphere = new SDFSphere().setRadius(radius).setBlock(membrane);
    SDF sub = new SDFTranslate().setTranslate(0, -4, 0).setSource(sphere);
    sphere = new SDFSubtract().setSourceA(sphere).setSourceB(sub);
    sphere = new SDFScale3D().setScale(1, 0.5F, 1).setSource(sphere);
    sphere = new SDFTranslate().setTranslate(0, 1 - radius * 0.5F, 0).setSource(sphere);

    float angle = random.nextFloat() * MathUtils.PI2;
    int count = (int) MathHelper.nextFloat(random, radius, radius * 2);
    if (count < 5) {
      count = 5;
    }
    sphere = new SDFFlatWave().setAngle(angle).setRaysCount(count).setIntensity(0.6F)
        .setSource(sphere);

    SDF cent = new SDFSphere().setRadius(2.5F).setBlock(center);
    sphere = new SDFUnion().setSourceA(sphere).setSourceB(cent);

    return sphere;
  }

  private void makeFruits(StructureWorldAccess world, double px, double py, double pz,
      BlockState fruit) {
    Mutable mut = new Mutable().set(px, py, pz);
    for (int i = 0; i < 8; i++) {
      mut.move(Direction.DOWN);
      if (world.isAir(mut)) {
        BlockState state = world.getBlockState(mut.up());
        if (state.isOf(LighterEndBlocks.UMBRELLA_MEMBRANE)
            && state.get(UmbrellaMembrane.COLOR) < 2) {
          world.setBlockState(mut, fruit, Flags.SILENT);
        }
        break;
      }
    }
  }

  static {
    SPLINE = Lists.newArrayList(
        new Vector3f(0.00F, 0.00F, 0.00F),
        new Vector3f(0.10F, 0.35F, 0.00F),
        new Vector3f(0.20F, 0.50F, 0.00F),
        new Vector3f(0.30F, 0.55F, 0.00F),
        new Vector3f(0.42F, 0.70F, 0.00F),
        new Vector3f(0.50F, 1.00F, 0.00F)
    );

    ROOT = Lists.newArrayList(
        new Vector3f(0.1F, 0.70F, 0),
        new Vector3f(0.3F, 0.30F, 0),
        new Vector3f(0.7F, 0.05F, 0),
        new Vector3f(0.8F, -0.20F, 0)
    );
    MathUtils.offset(ROOT, new Vector3f(0, -0.45F, 0));

    REPLACE = (state) -> {
      if (state.isOf(LighterEndBlocks.UMBRELLA_MEMBRANE)) {
        return true;
      }
      return MiscUtils.replaceableOrPlant(state);
    };
  }

  private static class Center {

    final double px;
    final double py;
    final double pz;
    final float radius;

    Center(double x, double y, double z, float radius) {
      this.px = x;
      this.py = y;
      this.pz = z;
      this.radius = radius;
    }

    double distance(float x, float z) {
      return Math.sqrt(Math.pow(px - x, 2) + Math.pow(pz - z, 2));
    }
  }
}
