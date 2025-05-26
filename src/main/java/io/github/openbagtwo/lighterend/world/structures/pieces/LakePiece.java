package io.github.openbagtwo.lighterend.world.structures.pieces;

import com.google.common.collect.Maps;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndStructures;
import io.github.openbagtwo.lighterend.tags.LighterEndTags;
import io.github.openbagtwo.lighterend.utils.PosInfo;
import io.github.openbagtwo.lighterend.world.gen.noise.OpenSimplexNoise;
import java.util.Map;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.StructureContext;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class LakePiece extends BasePiece {

  private static final BlockState ENDSTONE = Blocks.END_STONE.getDefaultState();
  private static final BlockState WATER = Blocks.WATER.getDefaultState();
  private final Map<Integer, Byte> heightmap = Maps.newHashMap();
  private OpenSimplexNoise noise;
  private BlockPos center;
  private float radius;
  private float aspect;
  private float depth;
  private int seed;

  public LakePiece(BlockPos center, float radius, float depth, Random random,
      RegistryEntry<Biome> biome) {
    super(LighterEndStructures.LAKE_PIECE, random.nextInt(), null);
    this.center = center;
    this.radius = radius;
    this.depth = depth;
    this.seed = random.nextInt();
    this.noise = new OpenSimplexNoise(this.seed);
    this.aspect = radius / depth;
    makeBoundingBox();
  }

  public LakePiece(StructureContext type, NbtCompound tag) {
    super(LighterEndStructures.LAKE_PIECE, tag);
    makeBoundingBox();
  }

  @Override
  protected void addAdditionalSaveData(NbtCompound tag) {
    tag.put("center", BlockPos.CODEC, center);
    tag.putFloat("radius", radius);
    tag.putFloat("depth", depth);
    tag.putInt("seed", seed);
  }

  @Override
  protected void fromNbt(NbtCompound tag) {
    center = tag.get("center", BlockPos.CODEC).orElse(BlockPos.ORIGIN);
    radius = tag.getFloat("radius").get();
    depth = tag.getFloat("depth").get();
    seed = tag.getInt("seed").get();
    noise = new OpenSimplexNoise(seed);
    aspect = radius / depth;
  }

  @Override
  public void generate(
      StructureWorldAccess world,
      StructureAccessor arg,
      ChunkGenerator chunkGenerator,
      Random random,
      BlockBox blockBox,
      ChunkPos chunkPos,
      BlockPos blockPos
  ) {
    int minY = this.boundingBox.getMinY();
    int maxY = this.boundingBox.getMaxY();
    int sx = ChunkSectionPos.getBlockCoord(chunkPos.x);
    int sz = ChunkSectionPos.getBlockCoord(chunkPos.z);
    Mutable mut = new Mutable();
    Chunk chunk = world.getChunk(chunkPos.x, chunkPos.z);
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
            if (state.isIn(LighterEndTags.END_STONES) || state.isAir()) {
              state = mut.getY() < center.getY() ? WATER : AIR;
              chunk.setBlockState(mut, state);
            }
          } else if (dist <= r3 && mut.getY() < center.getY()) {
            BlockState state = chunk.getBlockState(mut);
            BlockPos worldPos = mut.add(sx, 0, sz);
            if (!state.isFullCube(world, worldPos) && !state.isSolidBlock(
                world,
                worldPos
            )) {
              state = chunk.getBlockState(mut.up(3));
              final BlockState stateAbove = chunk.getBlockState(mut.up());
              if (stateAbove.isAir() && state.isAir()) {
                state =
                    random.nextInt(10) == 0 ? ENDSTONE
                        : LighterEndBlocks.END_MOSS.getDefaultState();
              } else if (stateAbove.isAir()) {
                state =
                    random.nextBoolean() ? ENDSTONE : LighterEndBlocks.END_MOSS.getDefaultState();
              } else {
                state = state.getFluidState().isEmpty()
                    ? ENDSTONE
                    : Blocks.SAND.getDefaultState();
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
      StructureWorldAccess world,
      Chunk chunk,
      Mutable mut,
      Random random,
      int sx,
      int sz
  ) {
    int minY = this.boundingBox.getMinY();
    int maxY = this.boundingBox.getMaxY();
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
                    : LighterEndBlocks.END_MOSS.getDefaultState();
              } else {
                bState = bState.getFluidState().isEmpty()
                    ? ENDSTONE
                    : Blocks.SAND.getDefaultState();
              }

              mut.setY(y);

              makeEndstonePillar(chunk, mut, bState);
            } else if (x > 1 && x < 15 && z > 1 && z < 15) {
              mut.setY(y);
              for (Direction dir : PosInfo.HORIZONTAL) {
                BlockPos wPos = mut.add(dir.getOffsetX(), 0, dir.getOffsetZ());
                if (chunk.getBlockState(wPos).isAir()) {
                  mut.setY(y + 1);
                  BlockState bState = chunk.getBlockState(mut);
                  if (bState.isAir()) {
                    bState = random.nextBoolean()
                        ? ENDSTONE
                        : LighterEndBlocks.END_MOSS.getDefaultState();
                  } else {
                    bState = bState.getFluidState().isEmpty()
                        ? ENDSTONE
                        : Blocks.SAND.getDefaultState();
                  }
                  mut.setY(y);
                  makeEndstonePillar(chunk, mut, bState);
                  break;
                }
              }
            } else if (chunk.getBlockState(mut.move(Direction.UP)).isAir()) {
              chunk.markBlockForPostProcessing(mut.move(Direction.DOWN).toImmutable());
            }
          } else if (chunk.getBlockState(mut).hasRandomTicks()) {
            chunk.markBlockForPostProcessing(mut.toImmutable());
          }
        }
      }
    }
  }

  private void makeEndstonePillar(Chunk chunk, Mutable mut, BlockState terrain) {
    chunk.setBlockState(mut, terrain);
    mut.setY(mut.getY() - 1);
    while (!chunk.getFluidState(mut).isEmpty()) {
      chunk.setBlockState(mut, ENDSTONE);
      mut.setY(mut.getY() - 1);
    }
  }

  private int getHeight(StructureWorldAccess world, BlockPos pos) {
    int p = ((pos.getX() & 2047) << 11) | (pos.getZ() & 2047);
    int h = heightmap.getOrDefault(p, Byte.MIN_VALUE);
    if (h > Byte.MIN_VALUE) {
      return h;
    }

    h = world.getTopY(Type.WORLD_SURFACE_WG, pos.getX(), pos.getZ());
    h = MathHelper.abs(h - center.getY());
    h = h < 8 ? 1 : 0;

    heightmap.put(p, (byte) h);
    return h;
  }

  private float getHeightClamp(StructureWorldAccess world, int radius, int posX, int posZ) {
    Mutable mut = new Mutable();
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
    return MathHelper.clamp(height, 0, 1);
  }

  private void makeBoundingBox() {
    int minX = MathHelper.floor(center.getX() - radius - 8);
    int minY = MathHelper.floor(center.getY() - depth - 8);
    int minZ = MathHelper.floor(center.getZ() - radius - 8);
    int maxX = MathHelper.floor(center.getX() + radius + 8);
    int maxY = MathHelper.floor(center.getY() + depth);
    int maxZ = MathHelper.floor(center.getZ() + radius + 8);
    this.boundingBox = new BlockBox(minX, minY, minZ, maxX, maxY, maxZ);
  }
}
