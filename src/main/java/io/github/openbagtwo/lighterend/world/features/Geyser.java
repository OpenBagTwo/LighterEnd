package io.github.openbagtwo.lighterend.world.features;

import static net.minecraft.world.Heightmap.Type.WORLD_SURFACE;
import static net.minecraft.world.Heightmap.Type.WORLD_SURFACE_WG;

import io.github.openbagtwo.lighterend.BlockFixer;
import io.github.openbagtwo.lighterend.blocks.HydrothermalVent;
import io.github.openbagtwo.lighterend.blocks.TubeWorm;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import io.github.openbagtwo.lighterend.utils.Flags;
import io.github.openbagtwo.lighterend.utils.MiscUtils;
import io.github.openbagtwo.lighterend.utils.math.sdf.SDF;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFCoordsModify;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFDisplace;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFInvert;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFRotate;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFScale3D;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFSmoothUnion;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFSubtract;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFTranslate;
import io.github.openbagtwo.lighterend.utils.math.sdf.operators.SDFUnion;
import io.github.openbagtwo.lighterend.utils.math.sdf.primitives.SDFCappedCone;
import io.github.openbagtwo.lighterend.utils.math.sdf.primitives.SDFFlatland;
import io.github.openbagtwo.lighterend.utils.math.sdf.primitives.SDFSphere;
import io.github.openbagtwo.lighterend.world.gen.noise.OpenSimplexNoise;
import java.util.function.Function;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class Geyser extends Feature<DefaultFeatureConfig> {

  protected static final Function<BlockState, Boolean> REPLACE1;
  protected static final Function<BlockState, Boolean> REPLACE2;
  private static final Function<BlockState, Boolean> IGNORE;

  public Geyser() {
    super(DefaultFeatureConfig.CODEC);
  }

  @Override
  public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
    final Random random = context.getRandom();
    final StructureWorldAccess world = context.getWorld();
    final BlockPos pos = world.getTopPosition(WORLD_SURFACE_WG, context.getOrigin());
    final ChunkGenerator chunkGenerator = context.getGenerator();

    if (pos.getY() < 10) {
      return false;
    }

    Mutable bpos = new Mutable().set(pos);
    bpos.setY(bpos.getY() - 1);
    BlockState state = world.getBlockState(bpos);
    while (state.isIn(LighterEndTags.END_STONES)
        || !state.getFluidState().isEmpty() && bpos.getY() > 5) {
      bpos.setY(bpos.getY() - 1);
      state = world.getBlockState(bpos);
    }

    if (pos.getY() - bpos.getY() < 25) {
      return false;
    }

    int halfHeight = MathHelper.nextInt(random, 10, 20);
    float radius1 = halfHeight * 0.5F;
    float radius2 = halfHeight * 0.1F + 0.5F;
    SDF sdf = new SDFCappedCone().setHeight(halfHeight)
        .setRadius1(radius1)
        .setRadius2(radius2)
        .setBlock(LighterEndBlocks.SULPHUR.baseBlock);
    sdf = new SDFTranslate().setTranslate(0, halfHeight - 3, 0).setSource(sdf);

    int count = halfHeight;
    for (int i = 0; i < count; i++) {
      int py = i << 1;
      float delta = (float) i / (float) (count - 1);
      float radius = MathHelper.lerp(delta, radius1, radius2) * 1.3F;

      SDF bowl = new SDFCappedCone().setHeight(radius)
          .setRadius1(0)
          .setRadius2(radius)
          .setBlock(LighterEndBlocks.SULPHUR.baseBlock);

      SDF brimstone = new SDFCappedCone().setHeight(radius)
          .setRadius1(0)
          .setRadius2(radius)
          .setBlock(LighterEndBlocks.BRIMSTONE);
      brimstone = new SDFTranslate().setTranslate(0, 2F, 0).setSource(brimstone);
      bowl = new SDFSubtract().setSourceA(bowl).setSourceB(brimstone);
      bowl = new SDFUnion().setSourceA(brimstone).setSourceB(bowl);

      SDF water = new SDFCappedCone().setHeight(radius).setRadius1(0).setRadius2(radius)
          .setBlock(Blocks.WATER);
      water = new SDFTranslate().setTranslate(0, 4, 0).setSource(water);
      bowl = new SDFSubtract().setSourceA(bowl).setSourceB(water);
      bowl = new SDFUnion().setSourceA(water).setSourceB(bowl);

      final OpenSimplexNoise noise1 = new OpenSimplexNoise(random.nextLong());
      final OpenSimplexNoise noise2 = new OpenSimplexNoise(random.nextLong());

      bowl = new SDFCoordsModify().setFunction((vec) -> {
        float dx = (float) noise1.eval(vec.x() * 0.1, vec.y() * 0.1, vec.z() * 0.1);
        float dz = (float) noise2.eval(vec.x() * 0.1, vec.y() * 0.1, vec.z() * 0.1);
        vec.set(vec.x() + dx, vec.y(), vec.z() + dz);
      }).setSource(bowl);

      SDF cut = new SDFFlatland().setBlock(Blocks.AIR);
      cut = new SDFInvert().setSource(cut);
      cut = new SDFTranslate().setTranslate(0, radius - 2, 0).setSource(cut);
      bowl = new SDFSubtract().setSourceA(bowl).setSourceB(cut);

      bowl = new SDFTranslate().setTranslate(radius, py - radius, 0).setSource(bowl);
      bowl = new SDFRotate().setRotation(RotationAxis.POSITIVE_Y, i * 4F).setSource(bowl);
      sdf = new SDFUnion().setSourceA(sdf).setSourceB(bowl);
    }
    sdf.setReplaceFunction(REPLACE2).fillRecursive(world, pos);

    radius2 = radius2 * 0.5F;
    if (radius2 < 0.7F) {
      radius2 = 0.7F;
    }
    final OpenSimplexNoise noise = new OpenSimplexNoise(random.nextLong());

    SDF.Primitive obj1;
    SDF.Primitive obj2;

    obj1 = new SDFCappedCone().setHeight(halfHeight + 5).setRadius1(radius1 * 0.5F)
        .setRadius2(radius2);
    sdf = new SDFTranslate().setTranslate(0, halfHeight - 13, 0).setSource(obj1);
    sdf = new SDFDisplace().setFunction((vec) -> (float) noise.eval(
        vec.x() * 0.3F,
        vec.y() * 0.3F,
        vec.z() * 0.3F
    ) * 0.5F).setSource(sdf);

    obj2 = new SDFSphere().setRadius(radius1);
    SDF cave = new SDFScale3D().setScale(1.5F, 1, 1.5F).setSource(obj2);
    cave = new SDFDisplace().setFunction((vec) -> (float) noise.eval(
        vec.x() * 0.1F,
        vec.y() * 0.1F,
        vec.z() * 0.1F
    ) * 2F).setSource(cave);
    cave = new SDFTranslate().setTranslate(0, -halfHeight - 10, 0).setSource(cave);

    sdf = new SDFSmoothUnion().setRadius(5).setSourceA(cave).setSourceB(sdf);

    obj1.setBlock(Blocks.WATER);
    obj2.setBlock(Blocks.WATER);
    sdf.setReplaceFunction(REPLACE2);
    sdf.fillRecursive(world, pos);

    obj1.setBlock(LighterEndBlocks.BRIMSTONE);
    obj2.setBlock(LighterEndBlocks.BRIMSTONE);
    new SDFDisplace().setFunction((vec) -> -2F)
        .setSource(sdf)
        .setReplaceFunction(REPLACE1)
        .fillRecursiveIgnore(world, pos, IGNORE);

    obj1.setBlock(LighterEndBlocks.SULPHUR.baseBlock);
    obj2.setBlock(LighterEndBlocks.SULPHUR.baseBlock);
    new SDFDisplace().setFunction((vec) -> -4F)
        .setSource(cave)
        .setReplaceFunction(REPLACE1)
        .fillRecursiveIgnore(world, pos, IGNORE);

    obj1.setBlock(Blocks.END_STONE);
    obj2.setBlock(Blocks.END_STONE);
    new SDFDisplace().setFunction((vec) -> -6F)
        .setSource(cave)
        .setReplaceFunction(REPLACE1)
        .fillRecursiveIgnore(world, pos, IGNORE);

    world.setBlockState(pos, Blocks.WATER.getDefaultState(), Flags.SILENT);
    Mutable mut = new Mutable().set(pos);
    count = world.getTopY(WORLD_SURFACE, pos.getX(), pos.getZ()) - pos.getY();
    for (int i = 0; i < count; i++) {
      world.setBlockState(mut, Blocks.WATER.getDefaultState(), Flags.SILENT);
      for (Direction dir : Direction.Type.HORIZONTAL) {
        world.setBlockState(mut.offset(dir), Blocks.WATER.getDefaultState(), Flags.SILENT);
      }
      mut.setY(mut.getY() + 1);
    }

    for (int i = 0; i < 150; i++) {
      mut.set(pos)
          .move(
              MathHelper.floor(random.nextGaussian() * 4 + 0.5),
              -halfHeight - 10,
              MathHelper.floor(random.nextGaussian() * 4 + 0.5)
          );
      float distRaw = (float) Math.sqrt(
          Math.pow(mut.getX() - pos.getX(), 2) + Math.pow(mut.getZ() - pos.getZ(), 2)
      );
      int dist = MathHelper.floor(6 - distRaw) + random.nextInt(2);
      if (dist >= 0) {
        state = world.getBlockState(mut);
        while (
            !state.getFluidState().isEmpty()
                || state.isIn(LighterEndTags.AQUATIC_END_VEGETATION)
        ) {
          mut.setY(mut.getY() - 1);
          state = world.getBlockState(mut);
        }
        if (state.isIn(LighterEndTags.END_STONES) && !world.getBlockState(mut.up())
            .isOf(LighterEndBlocks.HYDROTHERMAL_VENT)) {
          for (int j = 0; j <= dist; j++) {
            world.setBlockState(
                mut,
                LighterEndBlocks.SULPHUR.baseBlock.getDefaultState(),
                Flags.SILENT
            );
            for (Direction dir : Direction.Type.HORIZONTAL.getShuffled(random)) {
              BlockPos p = mut.offset(dir);
              if (random.nextBoolean() && world.getBlockState(p).isOf(Blocks.WATER)) {
                world.setBlockState(
                    p,
                    LighterEndBlocks.TUBE_WORM.getDefaultState().with(TubeWorm.FACING, dir),
                    Flags.SILENT
                );
              }
            }
            mut.setY(mut.getY() + 1);
          }
          state = LighterEndBlocks.HYDROTHERMAL_VENT.getDefaultState()
              .with(HydrothermalVent.ACTIVATED, distRaw < 2);
          world.setBlockState(mut, state, Flags.SILENT);
          mut.setY(mut.getY() + 1);
          state = world.getBlockState(mut);
          while (state.isOf(Blocks.WATER)) {
            world.setBlockState(
                mut,
                LighterEndBlocks.VENT_BUBBLE_COLUMN.getDefaultState(),
                Flags.SILENT
            );
            mut.setY(mut.getY() + 1);
            state = world.getBlockState(mut);
          }
        }
      }
    }

    for (int i = 0; i < 10; i++) {
      mut.set(pos)
          .move(
              MathHelper.floor(random.nextGaussian() * 0.7 + 0.5),
              -halfHeight - 10,
              MathHelper.floor(random.nextGaussian() * 0.7 + 0.5)
          );
      float distRaw = (float) Math.sqrt(
          Math.pow(mut.getX() - pos.getX(), 2) + Math.pow(mut.getZ() - pos.getZ(), 2)
      );
      int dist = MathHelper.floor(6 - distRaw) + random.nextInt(2);
      if (dist >= 0) {
        state = world.getBlockState(mut);
        while (state.isOf(Blocks.WATER)) {
          mut.setY(mut.getY() - 1);
          state = world.getBlockState(mut);
        }
        if (state.isIn(LighterEndTags.END_STONES)) {
          for (int j = 0; j <= dist; j++) {
            world.setBlockState(
                mut,
                LighterEndBlocks.SULPHUR.baseBlock.getDefaultState(),
                Flags.SILENT
            );
            mut.setY(mut.getY() + 1);
          }
          state = LighterEndBlocks.HYDROTHERMAL_VENT.getDefaultState()
              .with(HydrothermalVent.ACTIVATED, distRaw < 2);
          world.setBlockState(mut, state, Flags.SILENT);
          mut.setY(mut.getY() + 1);
          state = world.getBlockState(mut);
          while (state.isOf(Blocks.WATER)) {
            world.setBlockState(
                mut,
                LighterEndBlocks.VENT_BUBBLE_COLUMN.getDefaultState(),
                Flags.SILENT
            );
            mut.setY(mut.getY() + 1);
            state = world.getBlockState(mut);
          }
        }
      }
    }

    FeatureContext<DefaultFeatureConfig> featureContext = new FeatureContext<>(
        null,
        world,
        chunkGenerator,
        random,
        pos,
        new DefaultFeatureConfig()
    );
    (new SulphurLake()).generate(featureContext);

    double distance = radius1 * 1.7;
    BlockPos start = pos.add((int) -distance, (int) (-halfHeight - 15 - distance), (int) -distance);
    BlockPos end = pos.add((int) distance, (int) (-halfHeight - 5 + distance), (int) distance);
    BlockFixer.fixBlocks(world, start, end);

    return true;
  }

  static {
    REPLACE1 = (state) -> state.isAir() || (state.isIn(LighterEndTags.END_STONES));

    REPLACE2 = (state) -> {
      if (state.isIn(LighterEndTags.END_STONES) || state.isOf(LighterEndBlocks.HYDROTHERMAL_VENT)
          || state.isOf(LighterEndBlocks.SULPHUR_CRYSTAL)) {
        return true;
      }
      return MiscUtils.replaceableOrPlant(state);
    };

    IGNORE = (state) -> state.isOf(Blocks.WATER)
        || state.isOf(Blocks.CAVE_AIR)
        || state.isOf(LighterEndBlocks.SULPHUR.baseBlock)
        || state.isOf(LighterEndBlocks.BRIMSTONE);
  }

}
