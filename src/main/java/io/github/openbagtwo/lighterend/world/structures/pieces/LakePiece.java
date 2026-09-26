package io.github.openbagtwo.lighterend.world.structures.pieces;

import com.google.common.collect.Maps;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndStructures;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import io.github.openbagtwo.lighterend.world.gen.noise.OpenSimplexNoise;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.material.FluidState;

public class LakePiece extends BasePiece {

  private static final BlockState ENDSTONE = Blocks.END_STONE.defaultBlockState();
  private static final BlockState WATER = Blocks.WATER.defaultBlockState();
  private static final BlockState SAND = Blocks.SAND.defaultBlockState();

  private final Map<Integer, Byte> heightmap = Maps.newHashMap();
  private OpenSimplexNoise noise;
  private BlockPos center;
  private float radius;
  private float aspect;
  private float depth;
  private int seed;

  public LakePiece(BlockPos center, float radius, float depth, RandomSource random,
      Holder<Biome> biome) {
    super(LighterEndStructures.LAKE_PIECE, random.nextInt(), null);
    this.center = center;
    this.radius = radius;
    this.depth = depth;
    this.seed = random.nextInt();
    this.noise = new OpenSimplexNoise(this.seed);
    this.aspect = radius / depth;
    makeBoundingBox();
  }

  public LakePiece(StructurePieceSerializationContext type, CompoundTag tag) {
    super(LighterEndStructures.LAKE_PIECE, tag);
    makeBoundingBox();
  }

  @Override
  protected void addAdditionalSaveData(CompoundTag tag) {
    tag.store("center", BlockPos.CODEC, center);
    tag.putFloat("radius", radius);
    tag.putFloat("depth", depth);
    tag.putInt("seed", seed);
  }

  @Override
  protected void fromNbt(CompoundTag tag) {
    center = tag.read("center", BlockPos.CODEC).orElse(BlockPos.ZERO);
    radius = tag.getFloat("radius").get();
    depth = tag.getFloat("depth").get();
    seed = tag.getInt("seed").get();
    noise = new OpenSimplexNoise(seed);
    aspect = radius / depth;
  }

  @Override
  public void postProcess(
      WorldGenLevel world,
      StructureManager arg,
      ChunkGenerator chunkGenerator,
      RandomSource random,
      BoundingBox blockBox,
      ChunkPos chunkPos,
      BlockPos blockPos
  ) {
    int minY = this.boundingBox.minY();
    int maxY = this.boundingBox.maxY();
    int sx = SectionPos.sectionToBlockCoord(chunkPos.x());
    int sz = SectionPos.sectionToBlockCoord(chunkPos.z());
    MutableBlockPos mut = new MutableBlockPos();
    ChunkAccess chunk = world.getChunk(chunkPos.x(), chunkPos.z());
    for (int x = 0; x < 16; x++) {
      mut.setX(x);
      int wx = x | sx;
      double nx = wx * 0.1;
      int x2 = wx - center.getX();
      for (int z = 0; z < 16; z++) {
        mut.setZ(z);
        int wz = z | sz;
        double nz = wz * 0.1;
        int z2 = wz - center.getZ();
        float clamp = getHeightClamp(world, 8, wx, wz);
        if (clamp < 0.01) {
          continue;
        }

        double n = noise.eval(nx, nz) * 1.5 + 1.5;
        double x3 = Math.pow(x2 + noise.eval(nx, nz, 100) * 10, 2);
        double z3 = Math.pow(z2 + noise.eval(nx, nz, -100) * 10, 2);

        for (int y = maxY; y >= minY; y--) {
          mut.setY((int) (y + n));
          double y2 = Math.pow((y - center.getY()) * aspect, 2);
          double r2 = radius * clamp;
          double r3 = r2 + 8;
          r2 *= r2;
          r3 = r3 * r3 + 100;
          double dist = x3 + y2 + z3;
          if (dist < r2) {
            BlockState state = chunk.getBlockState(mut);
            if (state.is(LighterEndTags.END_STONES) || state.isAir()) {
              state = mut.getY() < center.getY() ? WATER : CAVE_AIR;
              chunk.setBlockState(mut, state);
            }
          } else if (dist <= r3 && mut.getY() < center.getY()) {
            BlockState state = chunk.getBlockState(mut);
            BlockPos worldPos = mut.offset(sx, 0, sz);
            if (!state.isCollisionShapeFullBlock(world, worldPos) && !state.isRedstoneConductor(
                world,
                worldPos
            )) {
              state = chunk.getBlockState(mut.above(3));
              final BlockState stateAbove = chunk.getBlockState(mut.above());
              if (stateAbove.isAir() && state.isAir()) {
                state =
                    random.nextInt(10) == 0 ? ENDSTONE
                        : LighterEndBlocks.END_MOSS.defaultBlockState();
              } else if (stateAbove.isAir()) {
                state =
                    random.nextBoolean() ? ENDSTONE : LighterEndBlocks.END_MOSS.defaultBlockState();
              } else {
                state = state.getFluidState().isEmpty()
                    ? ENDSTONE
                    : SAND;
              }
              chunk.setBlockState(mut, state);
            }
          }
        }
      }
    }
    fixWater(world, chunk, mut, random, sx, sz);
  }

  private void fixWater(
      WorldGenLevel world,
      ChunkAccess chunk,
      MutableBlockPos mut,
      RandomSource random,
      int sx,
      int sz
  ) {
    int minY = this.boundingBox.minY();
    int maxY = this.boundingBox.maxY();
    for (int x = 0; x < 16; x++) {
      mut.setX(x);
      for (int z = 0; z < 16; z++) {
        mut.setZ(z);
        for (int y = minY; y <= maxY; y++) {
          mut.setY(y);
          FluidState state = chunk.getFluidState(mut);
          if (!state.isEmpty()) {
            mut.setY(y - 1);
            if (chunk.getBlockState(mut).isAir()) {
              mut.setY(y + 1);

              BlockState bState = chunk.getBlockState(mut);
              if (bState.isAir()) {
                bState = random.nextBoolean()
                    ? ENDSTONE
                    : LighterEndBlocks.END_MOSS.defaultBlockState();
              } else {
                bState = bState.getFluidState().isEmpty()
                    ? ENDSTONE
                    : SAND;
              }

              mut.setY(y);

              makeEndstonePillar(chunk, mut, bState);
            } else if (x > 1 && x < 15 && z > 1 && z < 15) {
              mut.setY(y);
              for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos wPos = mut.offset(dir.getStepX(), 0, dir.getStepZ());
                if (chunk.getBlockState(wPos).isAir()) {
                  mut.setY(y + 1);
                  BlockState bState = chunk.getBlockState(mut);
                  if (bState.isAir()) {
                    bState = random.nextBoolean()
                        ? ENDSTONE
                        : LighterEndBlocks.END_MOSS.defaultBlockState();
                  } else {
                    bState = bState.getFluidState().isEmpty()
                        ? ENDSTONE
                        : SAND;
                  }
                  mut.setY(y);
                  makeEndstonePillar(chunk, mut, bState);
                  break;
                }
              }
            } else if (chunk.getBlockState(mut.move(Direction.UP)).isAir()) {
              chunk.markPosForPostprocessing(mut.move(Direction.DOWN).immutable());
            }
          } else if (chunk.getBlockState(mut).isRandomlyTicking()) {
            chunk.markPosForPostprocessing(mut.immutable());
          }
        }
      }
    }
  }

  private void makeEndstonePillar(ChunkAccess chunk, MutableBlockPos mut, BlockState terrain) {
    chunk.setBlockState(mut, terrain);
    mut.setY(mut.getY() - 1);
    while (!chunk.getFluidState(mut).isEmpty()) {
      chunk.setBlockState(mut, ENDSTONE);
      mut.setY(mut.getY() - 1);
    }
  }

  private int getHeight(WorldGenLevel world, BlockPos pos) {
    int p = ((pos.getX() & 2047) << 11) | (pos.getZ() & 2047);
    int h = heightmap.getOrDefault(p, Byte.MIN_VALUE);
    if (h > Byte.MIN_VALUE) {
      return h;
    }

    h = world.getHeight(Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ());
    h = Mth.abs(h - center.getY());
    h = h < 8 ? 1 : 0;

    heightmap.put(p, (byte) h);
    return h;
  }

  private float getHeightClamp(WorldGenLevel world, int radius, int posX, int posZ) {
    MutableBlockPos mut = new MutableBlockPos();
    int r2 = radius * radius;
    float height = 0;
    float max = 0;
    for (int x = -radius; x <= radius; x++) {
      mut.setX(posX + x);
      int x2 = x * x;
      for (int z = -radius; z <= radius; z++) {
        mut.setZ(posZ + z);
        int z2 = z * z;
        if (x2 + z2 < r2) {
          float mult = 1 - (float) Math.sqrt(x2 + z2) / radius;
          max += mult;
          height += getHeight(world, mut) * mult;
        }
      }
    }
    height /= max;
    return Mth.clamp(height, 0, 1);
  }

  private void makeBoundingBox() {
    int minX = Mth.floor(center.getX() - radius - 8);
    int minY = Mth.floor(center.getY() - depth - 8);
    int minZ = Mth.floor(center.getZ() - radius - 8);
    int maxX = Mth.floor(center.getX() + radius + 8);
    int maxY = Mth.floor(center.getY() + depth);
    int maxZ = Mth.floor(center.getZ() + radius + 8);
    this.boundingBox = new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
  }
}
