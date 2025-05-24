package io.github.openbagtwo.lighterend.world.features.trees;

import com.google.common.collect.Lists;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.utils.Flags;
import io.github.openbagtwo.lighterend.utils.math.sdf.SDF;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFDisplace;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFScale;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFScale3D;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFSubtract;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFTranslate;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFUnion;
import io.github.openbagtwo.lighterend.utils.math.sdf.primitives.SDFLine;
import io.github.openbagtwo.lighterend.utils.math.sdf.primitives.SDFSphere;
import io.github.openbagtwo.lighterend.world.gen.noise.OpenSimplexNoise;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.joml.Vector3f;

public class TenaneaTree extends Feature<DefaultFeatureConfig> {

  private static final BlockState AIR = Blocks.AIR.getDefaultState();
  private static final BlockState WATER = Blocks.WATER.getDefaultState();

  public static final Direction[] HORIZONTAL = new Direction[]{
      Direction.NORTH,
      Direction.EAST,
      Direction.SOUTH,
      Direction.WEST
  };
  private static final Direction[] DIRECTIONS = Direction.values();
  private static final Function<BlockState, Boolean> REPLACE;
  private static final Function<BlockState, Boolean> IGNORE;
  private static final List<Vector3f> SPLINE;

  private static final float PI2 = (float) Math.PI * 2.0f;


  public TenaneaTree() {
    super(DefaultFeatureConfig.CODEC);
  }

  @Override
  public boolean generate(FeatureContext<DefaultFeatureConfig> featureConfig) {

    final Random random = featureConfig.getRandom();
    final BlockPos pos = featureConfig.getOrigin();
    final StructureWorldAccess world = featureConfig.getWorld();

    float size = MathHelper.nextInt(random, 7, 10);
    int count = (int) (size * 0.45F);
    float var = PI2 / (float) (count * 3);
    float start = MathHelper.nextFloat(random, 0, PI2);
    for (int i = 0; i < count; i++) {
      float angle =
          (float) i / (float) count * PI2 + MathHelper.nextFloat(random, 0, var) + start;
      List<Vector3f> spline = copySpline(SPLINE);
      rotateSpline(spline, angle);
      scale(spline, size + MathHelper.nextFloat(random, 0, size * 0.5F));
      offsetParts(spline, random, 1F, 0, 1F);
      fillSpline(spline, world, LighterEndBlocks.TENANEA.wood.getDefaultState(),
          pos,
          REPLACE);
      Vector3f last = spline.get(spline.size() - 1);
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
    for (Direction d1 : HORIZONTAL) {
      BlockPos p = mut.set(pos).move(Direction.UP).move(d1).toImmutable();
      world.setBlockState(p, LighterEndBlocks.TENANEA.wood.getDefaultState(), Flags.SILENT);
      for (Direction d2 : HORIZONTAL) {
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
			/*if (state.is(CommonBlockTags.END_STONES)) {
				return true;
			}*/
      if (state.getBlock() == LighterEndBlocks.TENANEA_LEAVES) {
        return true;
      }
      return replaceableOrPlant(state);
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

  // Utils (TODO: refactor out as needed)

  private static int getYOnSurface(StructureWorldAccess world, int x, int z) {
    return world.getTopY(Type.WORLD_SURFACE, x, z);
  }

  private static int getYOnSurfaceWG(StructureWorldAccess world, int x, int z) {
    return world.getTopY(Type.WORLD_SURFACE_WG, x, z);
  }

  private static BlockPos getPosOnSurface(StructureWorldAccess world, BlockPos pos) {
    return world.getTopPosition(Type.WORLD_SURFACE, pos);
  }

  private static BlockPos getPosOnSurfaceWG(StructureWorldAccess world, BlockPos pos) {
    return world.getTopPosition(Type.WORLD_SURFACE_WG, pos);
  }

  private static BlockPos getPosOnSurfaceRaycast(StructureWorldAccess world, BlockPos pos) {
    return getPosOnSurfaceRaycast(world, pos, 256);
  }

  private static BlockPos getPosOnSurfaceRaycast(StructureWorldAccess world, BlockPos pos,
      int dist) {
    int h = downRay(world, pos, dist);
    return pos.down(h);
  }

  private static int downRay(WorldAccess world, BlockPos pos, int maxDist) {
    int length = 0;
    for (int j = 1; j < maxDist && (world.isAir(pos.down(j))); j++) {
      length++;
    }
    return length;
  }

  private static <T> void shuffle(T[] array, net.minecraft.util.math.random.Random random) {
    for (int i = 0; i < array.length; i++) {
      int i2 = random.nextInt(array.length);
      T element = array[i];
      array[i] = array[i2];
      array[i2] = element;
    }
  }

  public static List<Vector3f> makeSpline(float x1, float y1, float z1, float x2, float y2,
      float z2, int points) {
    List<Vector3f> spline = Lists.newArrayList();
    spline.add(new Vector3f(x1, y1, z1));
    int count = points - 1;
    for (int i = 1; i < count; i++) {
      float delta = (float) i / (float) count;
      float x = MathHelper.lerp(delta, x1, x2);
      float y = MathHelper.lerp(delta, y1, y2);
      float z = MathHelper.lerp(delta, z1, z2);
      spline.add(new Vector3f(x, y, z));
    }
    spline.add(new Vector3f(x2, y2, z2));
    return spline;
  }

  public static List<Vector3f> smoothSpline(List<Vector3f> spline, int segmentPoints) {
    List<Vector3f> result = Lists.newArrayList();
    Vector3f start = spline.get(0);
    for (int i = 1; i < spline.size(); i++) {
      Vector3f end = spline.get(i);
      for (int j = 0; j < segmentPoints; j++) {
        float delta = (float) j / segmentPoints;
        delta = 0.5F - 0.5F * MathHelper.cos(delta * 3.14159F);
        result.add(lerp(start, end, delta));
      }
      start = end;
    }
    result.add(start);
    return result;
  }

  private static Vector3f lerp(Vector3f start, Vector3f end, float delta) {
    float x = MathHelper.lerp(delta, start.x(), end.x());
    float y = MathHelper.lerp(delta, start.y(), end.y());
    float z = MathHelper.lerp(delta, start.z(), end.z());
    return new Vector3f(x, y, z);
  }

  public static void offsetParts(List<Vector3f> spline, Random random, float dx, float dy,
      float dz) {
    int count = spline.size();
    for (int i = 1; i < count; i++) {
      Vector3f pos = spline.get(i);
      float x = pos.x() + (float) random.nextGaussian() * dx;
      float y = pos.y() + (float) random.nextGaussian() * dy;
      float z = pos.z() + (float) random.nextGaussian() * dz;
      pos.set(x, y, z);
    }
  }

  public static void powerOffset(List<Vector3f> spline, float distance, float power) {
    int count = spline.size();
    float max = count + 1;
    for (int i = 1; i < count; i++) {
      Vector3f pos = spline.get(i);
      float x = (float) i / max;
      float y = pos.y() + (float) Math.pow(x, power) * distance;
      pos.set(pos.x(), y, pos.z());
    }
  }

  public static SDF buildSDF(
      List<Vector3f> spline,
      float radius1,
      float radius2,
      Function<BlockPos, BlockState> placerFunction
  ) {
    int count = spline.size();
    float max = count - 2;
    SDF result = null;
    Vector3f start = spline.get(0);
    for (int i = 1; i < count; i++) {
      Vector3f pos = spline.get(i);
      float delta = (float) (i - 1) / max;
      SDF line = new SDFLine().setRadius(MathHelper.lerp(delta, radius1, radius2))
          .setStart(start.x(), start.y(), start.z())
          .setEnd(pos.x(), pos.y(), pos.z())
          .setBlock(placerFunction);
      result = result == null ? line : new SDFUnion().setSourceA(result).setSourceB(line);
      start = pos;
    }
    return result;
  }

  public static SDF buildSDF(
      List<Vector3f> spline,
      Function<Float, Float> radiusFunction,
      Function<BlockPos, BlockState> placerFunction
  ) {
    int count = spline.size();
    float max = count - 2;
    SDF result = null;
    Vector3f start = spline.get(0);
    for (int i = 1; i < count; i++) {
      Vector3f pos = spline.get(i);
      float delta = (float) (i - 1) / max;
      SDF line = new SDFLine().setRadius(radiusFunction.apply(delta))
          .setStart(start.x(), start.y(), start.z())
          .setEnd(pos.x(), pos.y(), pos.z())
          .setBlock(placerFunction);
      result = result == null ? line : new SDFUnion().setSourceA(result).setSourceB(line);
      start = pos;
    }
    return result;
  }

  public static boolean fillSpline(
      List<Vector3f> spline,
      StructureWorldAccess world,
      BlockState state,
      BlockPos pos,
      Function<BlockState, Boolean> replace
  ) {
    Vector3f startPos = spline.get(0);
    for (int i = 1; i < spline.size(); i++) {
      Vector3f endPos = spline.get(i);
      if (!(fillLine(startPos, endPos, world, state, pos, replace))) {
        return false;
      }
      startPos = endPos;
    }

    return true;
  }

  public static void fillSplineForce(
      List<Vector3f> spline,
      StructureWorldAccess world,
      BlockState state,
      BlockPos pos,
      Function<BlockState, Boolean> replace
  ) {
    Vector3f startPos = spline.get(0);
    for (int i = 1; i < spline.size(); i++) {
      Vector3f endPos = spline.get(i);
      fillLineForce(startPos, endPos, world, state, pos, replace);
      startPos = endPos;
    }
  }

  public static boolean fillLine(
      Vector3f start,
      Vector3f end,
      StructureWorldAccess world,
      BlockState state,
      BlockPos pos,
      Function<BlockState, Boolean> replace
  ) {
    float dx = end.x() - start.x();
    float dy = end.y() - start.y();
    float dz = end.z() - start.z();
    float max = Math.max(Math.max(Math.abs(dx), Math.abs(dy)), Math.abs(dz));
    int count = MathHelper.floor(max + 1);
    dx /= max;
    dy /= max;
    dz /= max;
    float x = start.x();
    float y = start.y();
    float z = start.z();
    boolean down = Math.abs(dy) > 0.2;

    BlockState bState;
    Mutable bPos = new Mutable();
    for (int i = 0; i < count; i++) {
      bPos.set(x + pos.getX(), y + pos.getY(), z + pos.getZ());
      bState = world.getBlockState(bPos);
      if (bState.equals(state) || replace.apply(bState)) {
        world.setBlockState(bPos, state, Flags.SILENT);
        bPos.setY(bPos.getY() - 1);
        bState = world.getBlockState(bPos);
        if (down && bState.equals(state) || replace.apply(bState)) {
          world.setBlockState(bPos, state, Flags.SILENT);
        }
      } else {
        return false;
      }
      x += dx;
      y += dy;
      z += dz;
    }
    bPos.set(end.x() + pos.getX(), end.y() + pos.getY(), end.z() + pos.getZ());
    bState = world.getBlockState(bPos);
    if (bState.equals(state) || replace.apply(bState)) {
      world.setBlockState(bPos, state, Flags.SILENT);
      bPos.setY(bPos.getY() - 1);
      bState = world.getBlockState(bPos);
      if (down && bState.equals(state) || replace.apply(bState)) {
        world.setBlockState(bPos, state, Flags.SILENT);
      }
      return true;
    } else {
      return false;
    }
  }

  public static void fillLineForce(
      Vector3f start,
      Vector3f end,
      StructureWorldAccess world,
      BlockState state,
      BlockPos pos,
      Function<BlockState, Boolean> replace
  ) {
    float dx = end.x() - start.x();
    float dy = end.y() - start.y();
    float dz = end.z() - start.z();
    float max = Math.max(Math.max(Math.abs(dx), Math.abs(dy)), Math.abs(dz));
    int count = MathHelper.floor(max + 1);
    dx /= max;
    dy /= max;
    dz /= max;
    float x = start.x();
    float y = start.y();
    float z = start.z();
    boolean down = Math.abs(dy) > 0.2;

    BlockState bState;
    Mutable bPos = new Mutable();
    for (int i = 0; i < count; i++) {
      bPos.set(x + pos.getX(), y + pos.getY(), z + pos.getZ());
      bState = world.getBlockState(bPos);
      if (replace.apply(bState)) {
        world.setBlockState(bPos, state, Flags.SILENT);
        bPos.setY(bPos.getY() - 1);
        bState = world.getBlockState(bPos);
        if (down && replace.apply(bState)) {
          world.setBlockState(bPos, state, Flags.SILENT);
        }
      }
      x += dx;
      y += dy;
      z += dz;
    }
    bPos.set(end.x() + pos.getX(), end.y() + pos.getY(), end.z() + pos.getZ());
    bState = world.getBlockState(bPos);
    if (replace.apply(bState)) {
      world.setBlockState(bPos, state, Flags.SILENT);
      bPos.setY(bPos.getY() - 1);
      bState = world.getBlockState(bPos);
      if (down && replace.apply(bState)) {
        world.setBlockState(bPos, state, Flags.SILENT);
      }
    }
  }

  public static boolean canGenerate(
      List<Vector3f> spline,
      float scale,
      BlockPos start,
      StructureWorldAccess world,
      Function<BlockState, Boolean> canReplace
  ) {
    int count = spline.size();
    Vector3f vec = spline.get(0);
    Mutable mut = new Mutable();
    float x1 = start.getX() + vec.x() * scale;
    float y1 = start.getY() + vec.y() * scale;
    float z1 = start.getZ() + vec.z() * scale;
    for (int i = 1; i < count; i++) {
      vec = spline.get(i);
      float x2 = start.getX() + vec.x() * scale;
      float y2 = start.getY() + vec.y() * scale;
      float z2 = start.getZ() + vec.z() * scale;

      for (float py = y1; py < y2; py += 3) {
        if (py - start.getY() < 10) {
          continue;
        }
        float lerp = (py - y1) / (y2 - y1);
        float x = MathHelper.lerp(lerp, x1, x2);
        float z = MathHelper.lerp(lerp, z1, z2);
        mut.set(x, py, z);
        if (!canReplace.apply(world.getBlockState(mut))) {
          return false;
        }
      }

      x1 = x2;
      y1 = y2;
      z1 = z2;
    }
    return true;
  }

  public static boolean canGenerate(
      List<Vector3f> spline,
      BlockPos start,
      StructureWorldAccess world,
      Function<BlockState, Boolean> canReplace
  ) {
    int count = spline.size();
    Vector3f vec = spline.get(0);
    Mutable mut = new Mutable();
    float x1 = start.getX() + vec.x();
    float y1 = start.getY() + vec.y();
    float z1 = start.getZ() + vec.z();
    for (int i = 1; i < count; i++) {
      vec = spline.get(i);
      float x2 = start.getX() + vec.x();
      float y2 = start.getY() + vec.y();
      float z2 = start.getZ() + vec.z();

      for (float py = y1; py < y2; py += 3) {
        if (py - start.getY() < 10) {
          continue;
        }
        float lerp = (py - y1) / (y2 - y1);
        float x = MathHelper.lerp(lerp, x1, x2);
        float z = MathHelper.lerp(lerp, z1, z2);
        mut.set(x, py, z);
        if (!canReplace.apply(world.getBlockState(mut))) {
          return false;
        }
      }

      x1 = x2;
      y1 = y2;
      z1 = z2;
    }
    return true;
  }

  public static Vector3f getPos(List<Vector3f> spline, float index) {
    int i = (int) index;
    int last = spline.size() - 1;
    if (i >= last) {
      return spline.get(last);
    }
    float delta = index - i;
    Vector3f p1 = spline.get(i);
    Vector3f p2 = spline.get(i + 1);
    float x = MathHelper.lerp(delta, p1.x(), p2.x());
    float y = MathHelper.lerp(delta, p1.y(), p2.y());
    float z = MathHelper.lerp(delta, p1.z(), p2.z());
    return new Vector3f(x, y, z);
  }

  public static void rotateSpline(List<Vector3f> spline, float angle) {
    for (Vector3f v : spline) {
      float sin = (float) Math.sin(angle);
      float cos = (float) Math.cos(angle);
      float x = v.x() * cos + v.z() * sin;
      float z = v.x() * sin + v.z() * cos;
      v.set(x, v.y(), z);
    }
  }

  public static List<Vector3f> copySpline(List<Vector3f> spline) {
    List<Vector3f> result = new ArrayList<Vector3f>(spline.size());
    for (Vector3f v : spline) {
      result.add(new Vector3f(v.x(), v.y(), v.z()));
    }
    return result;
  }

  public static void scale(List<Vector3f> spline, float scale) {
    scale(spline, scale, scale, scale);
  }

  public static void scale(List<Vector3f> spline, float x, float y, float z) {
    for (Vector3f v : spline) {
      v.set(v.x() * x, v.y() * y, v.z() * z);
    }
  }

  public static void offset(List<Vector3f> spline, Vector3f offset) {
    for (Vector3f v : spline) {
      v.set(offset.x() + v.x(), offset.y() + v.y(), offset.z() + v.z());
    }
  }

  public static Boolean replaceableOrPlant(BlockState state) {
    final Block block = state.getBlock();

    if (state.getPistonBehavior() == PistonBehavior.DESTROY && block.getHardness() == 0) {
      return true;
    }

    if (state.getSoundGroup() == BlockSoundGroup.GRASS
        || state.getSoundGroup() == BlockSoundGroup.WET_GRASS
        || state.getSoundGroup() == BlockSoundGroup.CROP
        || state.getSoundGroup() == BlockSoundGroup.CAVE_VINES

    ) {
      return true;
    }

    return state.isReplaceable();
  }

}
