package io.github.openbagtwo.lighterend.utils;

import java.util.Map;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

public class PosInfo implements Comparable<PosInfo> {

  private static final BlockState AIR = Blocks.AIR.getDefaultState();
  private final Map<BlockPos, PosInfo> blocks;
  private final Map<BlockPos, PosInfo> add;
  private final BlockPos pos;
  private BlockState state;

  private static final ThreadLocal<Mutable> TL_POS = ThreadLocal.withInitial(() -> new Mutable());

  public static PosInfo create(Map<BlockPos, PosInfo> blocks, Map<BlockPos, PosInfo> add,
      BlockPos pos) {
    return new PosInfo(blocks, add, pos);
  }

  private PosInfo(Map<BlockPos, PosInfo> blocks, Map<BlockPos, PosInfo> add, BlockPos pos) {
    this.blocks = blocks;
    this.add = add;
    this.pos = pos;
    blocks.put(pos, this);
  }

  public static int downRay(WorldAccess world, BlockPos pos, int maxDist) {
    int length = 0;
    for (int j = 1; j < maxDist && (world.isAir(pos.down(j))); j++) {
      length++;
    }
    return length;
  }

  public static int downRayRep(WorldAccess world, BlockPos pos, int maxDist) {
    final Mutable POS = TL_POS.get();
    POS.set(pos);
    for (int j = 1; j < maxDist && (world.getBlockState(POS)).isReplaceable(); j++) {
      POS.setY(POS.getY() - 1);
    }
    return pos.getY() - POS.getY();
  }

  public static int upRay(WorldAccess world, BlockPos pos, int maxDist) {
    int length = 0;
    for (int j = 1; j < maxDist && (world.isAir(pos.up(j))); j++) {
      length++;
    }
    return length;
  }

  public BlockState getState() {
    return state;
  }

  public BlockState getState(BlockPos pos) {
    PosInfo info = blocks.get(pos);
    if (info == null) {
      info = add.get(pos);
      return info == null ? AIR : info.getState();
    }
    return info.getState();
  }

  public void setState(BlockState state) {
    this.state = state;
  }

  public void setState(BlockPos pos, BlockState state) {
    PosInfo info = blocks.get(pos);
    if (info != null) {
      info.setState(state);
    }
  }

  public BlockState getState(Direction dir) {
    PosInfo info = blocks.get(pos.offset(dir));
    if (info == null) {
      info = add.get(pos.offset(dir));
      return info == null ? AIR : info.getState();
    }
    return info.getState();
  }

  public BlockState getState(Direction dir, int distance) {
    PosInfo info = blocks.get(pos.offset(dir, distance));
    if (info == null) {
      return AIR;
    }
    return info.getState();
  }

  public BlockState getStateUp() {
    return getState(Direction.UP);
  }

  public BlockState getStateDown() {
    return getState(Direction.DOWN);
  }

  @Override
  public int hashCode() {
    return pos.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof PosInfo)) {
      return false;
    }
    return pos.equals(((PosInfo) obj).pos);
  }

  @Override
  public int compareTo(PosInfo info) {
    return this.pos.getY() - info.pos.getY();
  }

  public BlockPos getPos() {
    return pos;
  }

  public void setBlockPos(BlockPos pos, BlockState state) {
    PosInfo info = new PosInfo(blocks, add, pos);
    info.state = state;
    add.put(pos, info);
  }
}
