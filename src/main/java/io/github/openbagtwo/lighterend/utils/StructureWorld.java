package io.github.openbagtwo.lighterend.utils;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class StructureWorld {

  private final Map<ChunkPos, Part> parts = Maps.newHashMap();
  private ChunkPos lastPos;
  private Part lastPart;
  private int minX = Integer.MAX_VALUE;
  private int minY = Integer.MAX_VALUE;
  private int minZ = Integer.MAX_VALUE;
  private int maxX = Integer.MIN_VALUE;
  private int maxY = Integer.MIN_VALUE;
  private int maxZ = Integer.MIN_VALUE;

  public StructureWorld() {
  }

  public StructureWorld(CompoundTag tag) {
    minX = tag.getIntOr("minX", Integer.MAX_VALUE);
    maxX = tag.getIntOr("maxX", Integer.MIN_VALUE);
    minY = tag.getIntOr("minY", Integer.MAX_VALUE);
    maxY = tag.getIntOr("maxY", Integer.MIN_VALUE);
    minZ = tag.getIntOr("minZ", Integer.MAX_VALUE);
    maxZ = tag.getIntOr("maxZ", Integer.MIN_VALUE);

    ListTag map = tag.getList("parts").get();
    map.forEach((element) -> {
      CompoundTag compound = (CompoundTag) element;
      Part part = new Part(compound);
      int x = compound.getInt("x").get();
      int z = compound.getInt("z").get();
      parts.put(new ChunkPos(x, z), part);
    });
  }

  public void setBlock(BlockPos pos, BlockState state) {
    ChunkPos cPos = new ChunkPos(pos.getX(), pos.getZ());

    if (cPos.equals(lastPos)) {
      lastPart.addBlock(pos, state);
      return;
    }

    Part part = parts.get(cPos);
    if (part == null) {
      part = new Part();
      parts.put(cPos, part);

      if (cPos.x() < minX) {
        minX = cPos.x();
      }
      if (cPos.x() > maxX) {
        maxX = cPos.x();
      }
      if (cPos.z() < minZ) {
        minZ = cPos.z();
      }
      if (cPos.z() > maxZ) {
        maxZ = cPos.z();
      }
    }
    if (pos.getY() < minY) {
      minY = pos.getY();
    }
    if (pos.getY() > maxY) {
      maxY = pos.getY();
    }
    part.addBlock(pos, state);

    lastPos = cPos;
    lastPart = part;
  }

  public boolean placeChunk(WorldGenLevel world, ChunkPos chunkPos) {
    Part part = parts.get(chunkPos);
    if (part != null) {
      ChunkAccess chunk = world.getChunk(chunkPos.x(), chunkPos.z());
      part.placeChunk(chunk);
      return true;
    }
    return false;
  }

  public CompoundTag toBNT() {
    CompoundTag tag = new CompoundTag();
    tag.putInt("minX", minX);
    tag.putInt("maxX", maxX);
    tag.putInt("minY", minY);
    tag.putInt("maxY", maxY);
    tag.putInt("minZ", minZ);
    tag.putInt("maxZ", maxZ);
    ListTag map = new ListTag();
    tag.put("parts", map);
    parts.forEach((pos, part) -> {
      map.add(part.toNBT(pos.x(), pos.z()));
    });
    return tag;
  }

  public BoundingBox getBounds() {
    if (minX == Integer.MAX_VALUE || maxX == Integer.MIN_VALUE || minZ == Integer.MAX_VALUE
        || maxZ == Integer.MIN_VALUE) {
      return BoundingBox.infinite();
    }
    return new BoundingBox(minX << 4, minY, minZ << 4, (maxX << 4) | 15, maxY, (maxZ << 4) | 15);
  }

  private static final class Part {

    Map<BlockPos, BlockState> blocks = Maps.newHashMap();

    public Part() {
    }

    public Part(CompoundTag tag) {
      ListTag map = tag.getList("blocks").get();
      ListTag map2 = tag.getList("states").get();
      BlockState[] states = new BlockState[map2.size()];
      for (int i = 0; i < states.length; i++) {
        states[i] = NbtUtils.readBlockState(
            BuiltInRegistries.BLOCK.freeze(),
            (CompoundTag) map2.get(i)
        );
      }

      map.forEach((element) -> {
        CompoundTag block = (CompoundTag) element;
        BlockPos pos = toBlockPos(block, "pos").orElse(null);
        if (pos != null) {
          int stateID = block.getInt("state").get();
          BlockState state =
              stateID < states.length ? states[stateID] : Block.stateById(stateID);
          blocks.put(pos, state);
        }
      });
    }

    void addBlock(BlockPos pos, BlockState state) {
      BlockPos inner = new BlockPos(pos.getX() & 15, pos.getY(), pos.getZ() & 15);
      blocks.put(inner, state);
    }

    void placeChunk(ChunkAccess chunk) {
      blocks.forEach((pos, state) -> {
        chunk.setBlockState(pos, state);
      });
    }

    CompoundTag toNBT(int x, int z) {
      CompoundTag tag = new CompoundTag();
      tag.putInt("x", x);
      tag.putInt("z", z);
      ListTag map = new ListTag();
      tag.put("blocks", map);
      ListTag stateMap = new ListTag();
      tag.put("states", stateMap);

      int[] id = new int[1];
      Map<BlockState, Integer> states = Maps.newHashMap();

      blocks.forEach((pos, state) -> {
        int stateID = states.getOrDefault(states, -1);
        if (stateID < 0) {
          stateID = id[0]++;
          states.put(state, stateID);
          stateMap.add(NbtUtils.writeBlockState(state));
        }

        CompoundTag block = new CompoundTag();
        block.put("pos", fromBlockPos(pos));
        block.putInt("state", stateID);
        map.add(block);
      });

      return tag;
    }
  }

  public static Optional<BlockPos> toBlockPos(CompoundTag compoundTag, String string) {
    int[] is = compoundTag.getIntArray(string).get();
    return is.length == 3 ? Optional.of(new BlockPos(is[0], is[1], is[2])) : Optional.empty();
  }

  public static IntArrayTag fromBlockPos(BlockPos blockPos) {
    return new IntArrayTag(new int[]{blockPos.getX(), blockPos.getY(), blockPos.getZ()});
  }
}
