package io.github.openbagtwo.lighterend.utils;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Optional;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.Chunk;

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

  public StructureWorld(NbtCompound tag) {
    minX = tag.getInt("minX", Integer.MAX_VALUE);
    maxX = tag.getInt("maxX", Integer.MIN_VALUE);
    minY = tag.getInt("minY", Integer.MAX_VALUE);
    maxY = tag.getInt("maxY", Integer.MIN_VALUE);
    minZ = tag.getInt("minZ", Integer.MAX_VALUE);
    maxZ = tag.getInt("maxZ", Integer.MIN_VALUE);

    NbtList map = tag.getList("parts").get();
    map.forEach((element) -> {
      NbtCompound compound = (NbtCompound) element;
      Part part = new Part(compound);
      int x = compound.getInt("x").get();
      int z = compound.getInt("z").get();
      parts.put(new ChunkPos(x, z), part);
    });
  }

  public void setBlock(BlockPos pos, BlockState state) {
    ChunkPos cPos = new ChunkPos(pos);

    if (cPos.equals(lastPos)) {
      lastPart.addBlock(pos, state);
      return;
    }

    Part part = parts.get(cPos);
    if (part == null) {
      part = new Part();
      parts.put(cPos, part);

      if (cPos.x < minX) {
        minX = cPos.x;
      }
      if (cPos.x > maxX) {
        maxX = cPos.x;
      }
      if (cPos.z < minZ) {
        minZ = cPos.z;
      }
      if (cPos.z > maxZ) {
        maxZ = cPos.z;
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

  public boolean placeChunk(StructureWorldAccess world, ChunkPos chunkPos) {
    Part part = parts.get(chunkPos);
    if (part != null) {
      Chunk chunk = world.getChunk(chunkPos.x, chunkPos.z);
      part.placeChunk(chunk);
      return true;
    }
    return false;
  }

  public NbtCompound toBNT() {
    NbtCompound tag = new NbtCompound();
    tag.putInt("minX", minX);
    tag.putInt("maxX", maxX);
    tag.putInt("minY", minY);
    tag.putInt("maxY", maxY);
    tag.putInt("minZ", minZ);
    tag.putInt("maxZ", maxZ);
    NbtList map = new NbtList();
    tag.put("parts", map);
    parts.forEach((pos, part) -> {
      map.add(part.toNBT(pos.x, pos.z));
    });
    return tag;
  }

  public BlockBox getBounds() {
    if (minX == Integer.MAX_VALUE || maxX == Integer.MIN_VALUE || minZ == Integer.MAX_VALUE
        || maxZ == Integer.MIN_VALUE) {
      return BlockBox.infinite();
    }
    return new BlockBox(minX << 4, minY, minZ << 4, (maxX << 4) | 15, maxY, (maxZ << 4) | 15);
  }

  private static final class Part {

    Map<BlockPos, BlockState> blocks = Maps.newHashMap();

    public Part() {
    }

    public Part(NbtCompound tag) {
      NbtList map = tag.getList("blocks").get();
      NbtList map2 = tag.getList("states").get();
      BlockState[] states = new BlockState[map2.size()];
      for (int i = 0; i < states.length; i++) {
        states[i] = NbtHelper.toBlockState(
            Registries.BLOCK.freeze(),
            (NbtCompound) map2.get(i)
        );
      }

      map.forEach((element) -> {
        NbtCompound block = (NbtCompound) element;
        BlockPos pos = toBlockPos(block, "pos").orElse(null);
        if (pos != null) {
          int stateID = block.getInt("state").get();
          BlockState state =
              stateID < states.length ? states[stateID] : Block.getStateFromRawId(stateID);
          blocks.put(pos, state);
        }
      });
    }

    void addBlock(BlockPos pos, BlockState state) {
      BlockPos inner = new BlockPos(pos.getX() & 15, pos.getY(), pos.getZ() & 15);
      blocks.put(inner, state);
    }

    void placeChunk(Chunk chunk) {
      blocks.forEach((pos, state) -> {
        chunk.setBlockState(pos, state);
      });
    }

    NbtCompound toNBT(int x, int z) {
      NbtCompound tag = new NbtCompound();
      tag.putInt("x", x);
      tag.putInt("z", z);
      NbtList map = new NbtList();
      tag.put("blocks", map);
      NbtList stateMap = new NbtList();
      tag.put("states", stateMap);

      int[] id = new int[1];
      Map<BlockState, Integer> states = Maps.newHashMap();

      blocks.forEach((pos, state) -> {
        int stateID = states.getOrDefault(states, -1);
        if (stateID < 0) {
          stateID = id[0]++;
          states.put(state, stateID);
          stateMap.add(NbtHelper.fromBlockState(state));
        }

        NbtCompound block = new NbtCompound();
        block.put("pos", fromBlockPos(pos));
        block.putInt("state", stateID);
        map.add(block);
      });

      return tag;
    }
  }

  public static Optional<BlockPos> toBlockPos(NbtCompound compoundTag, String string) {
    int[] is = compoundTag.getIntArray(string).get();
    return is.length == 3 ? Optional.of(new BlockPos(is[0], is[1], is[2])) : Optional.empty();
  }

  public static NbtIntArray fromBlockPos(BlockPos blockPos) {
    return new NbtIntArray(new int[]{blockPos.getX(), blockPos.getY(), blockPos.getZ()});
  }
}
