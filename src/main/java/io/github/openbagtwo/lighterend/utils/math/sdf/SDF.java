package io.github.openbagtwo.lighterend.utils.math.sdf;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.openbagtwo.lighterend.utils.Flags;
import io.github.openbagtwo.lighterend.utils.PosInfo;
import io.github.openbagtwo.lighterend.utils.StructureWorld;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public abstract class SDF {

  private final List<Function<PosInfo, BlockState>> postProcesses = Lists.newArrayList();
  private Function<BlockState, Boolean> canReplace = (state) -> state.canBeReplaced();

  public abstract float getDistance(float x, float y, float z);

  public abstract BlockState getBlockState(BlockPos pos);

  public SDF addPostProcess(Function<PosInfo, BlockState> postProcess) {
    this.postProcesses.add(postProcess);
    return this;
  }

  public SDF setReplaceFunction(Function<BlockState, Boolean> canReplace) {
    this.canReplace = canReplace;
    return this;
  }

  public void fillRecursive(ServerLevelAccessor world, BlockPos start) {
    Map<BlockPos, PosInfo> mapWorld = Maps.newHashMap();
    Map<BlockPos, PosInfo> addInfo = Maps.newHashMap();
    Set<BlockPos> blocks = Sets.newHashSet();
    Set<BlockPos> ends = Sets.newHashSet();
    Set<BlockPos> add = Sets.newHashSet();
    ends.add(new BlockPos(0, 0, 0));
    boolean run = true;

    MutableBlockPos bPos = new MutableBlockPos();

    while (run) {
      for (BlockPos center : ends) {
        for (Direction dir : Direction.values()) {
          bPos.set(center).move(dir);
          BlockPos wpos = bPos.offset(start);

          if (!blocks.contains(bPos) && canReplace.apply(world.getBlockState(wpos))) {
            if (this.getDistance(bPos.getX(), bPos.getY(), bPos.getZ()) < 0) {
              BlockState state = getBlockState(wpos);
              PosInfo.create(mapWorld, addInfo, wpos).setState(state);
              add.add(bPos.immutable());
            }
          }
        }
      }

      blocks.addAll(ends);
      ends.clear();
      ends.addAll(add);
      add.clear();

      run &= !ends.isEmpty();
    }

    List<PosInfo> infos = new ArrayList<PosInfo>(mapWorld.values());
    if (infos.size() > 0) {
      Collections.sort(infos);
      postProcesses.forEach((postProcess) -> {
        infos.forEach((info) -> {
          info.setState(postProcess.apply(info));
        });
      });
      infos.forEach((info) -> {
        world.setBlock(info.getPos(), info.getState(), Flags.SILENT);
      });

      infos.clear();
      infos.addAll(addInfo.values());
      Collections.sort(infos);
      postProcesses.forEach((postProcess) -> {
        infos.forEach((info) -> {
          info.setState(postProcess.apply(info));
        });
      });
      infos.forEach((info) -> {
        if (canReplace.apply(world.getBlockState(info.getPos()))) {
          world.setBlock(info.getPos(), info.getState(), Flags.SILENT);
        }
      });
    }
  }

  public void fillArea(ServerLevelAccessor world, BlockPos center, AABB box) {
    Map<BlockPos, PosInfo> mapWorld = Maps.newHashMap();
    Map<BlockPos, PosInfo> addInfo = Maps.newHashMap();

    MutableBlockPos mut = new MutableBlockPos();
    for (int y = (int) box.minY; y <= box.maxY; y++) {
      mut.setY(y);
      for (int x = (int) box.minX; x <= box.maxX; x++) {
        mut.setX(x);
        for (int z = (int) box.minZ; z <= box.maxZ; z++) {
          mut.setZ(z);
          if (canReplace.apply(world.getBlockState(mut))) {
            BlockPos fpos = mut.subtract(center);
            if (this.getDistance(fpos.getX(), fpos.getY(), fpos.getZ()) < 0) {
              PosInfo.create(mapWorld, addInfo, mut.immutable()).setState(getBlockState(mut));
            }
          }
        }
      }
    }

    List<PosInfo> infos = new ArrayList<PosInfo>(mapWorld.values());
    if (infos.size() > 0) {
      Collections.sort(infos);
      postProcesses.forEach((postProcess) -> {
        infos.forEach((info) -> {
          info.setState(postProcess.apply(info));
        });
      });
      infos.forEach((info) -> {
        world.setBlock(info.getPos(), info.getState(), Flags.SILENT);
      });

      infos.clear();
      infos.addAll(addInfo.values());
      Collections.sort(infos);
      postProcesses.forEach((postProcess) -> {
        infos.forEach((info) -> {
          info.setState(postProcess.apply(info));
        });
      });
      infos.forEach((info) -> {
        if (canReplace.apply(world.getBlockState(info.getPos()))) {
          world.setBlock(info.getPos(), info.getState(), Flags.SILENT);
        }
      });
    }
  }

  public void fillRecursiveIgnore(ServerLevelAccessor world, BlockPos start,
      Function<BlockState, Boolean> ignore) {
    Map<BlockPos, PosInfo> mapWorld = Maps.newHashMap();
    Map<BlockPos, PosInfo> addInfo = Maps.newHashMap();
    Set<BlockPos> blocks = Sets.newHashSet();
    Set<BlockPos> ends = Sets.newHashSet();
    Set<BlockPos> add = Sets.newHashSet();
    ends.add(new BlockPos(0, 0, 0));
    boolean run = true;

    MutableBlockPos bPos = new MutableBlockPos();

    while (run) {
      for (BlockPos center : ends) {
        for (Direction dir : Direction.values()) {
          bPos.set(center).move(dir);
          BlockPos wpos = bPos.offset(start);
          BlockState state = world.getBlockState(wpos);
          boolean ign = ignore.apply(state);
          if (!blocks.contains(bPos) && (ign || canReplace.apply(state))) {
            if (this.getDistance(bPos.getX(), bPos.getY(), bPos.getZ()) < 0) {
              PosInfo.create(mapWorld, addInfo, wpos).setState(ign ? state : getBlockState(bPos));
              add.add(bPos.immutable());
            }
          }
        }
      }

      blocks.addAll(ends);
      ends.clear();
      ends.addAll(add);
      add.clear();

      run &= !ends.isEmpty();
    }

    List<PosInfo> infos = new ArrayList<PosInfo>(mapWorld.values());
    if (infos.size() > 0) {
      Collections.sort(infos);
      postProcesses.forEach((postProcess) -> {
        infos.forEach((info) -> {
          info.setState(postProcess.apply(info));
        });
      });
      infos.forEach((info) -> {
        world.setBlock(info.getPos(), info.getState(), Flags.SILENT);
      });

      infos.clear();
      infos.addAll(addInfo.values());
      Collections.sort(infos);
      postProcesses.forEach((postProcess) -> {
        infos.forEach((info) -> {
          info.setState(postProcess.apply(info));
        });
      });
      infos.forEach((info) -> {
        if (canReplace.apply(world.getBlockState(info.getPos()))) {
          world.setBlock(info.getPos(), info.getState(), Flags.SILENT);
        }
      });
    }
  }

  public void fillRecursive(StructureWorld world, BlockPos start) {
    Map<BlockPos, PosInfo> mapWorld = Maps.newHashMap();
    Map<BlockPos, PosInfo> addInfo = Maps.newHashMap();
    Set<BlockPos> blocks = Sets.newHashSet();
    Set<BlockPos> ends = Sets.newHashSet();
    Set<BlockPos> add = Sets.newHashSet();
    ends.add(new BlockPos(0, 0, 0));
    boolean run = true;

    MutableBlockPos bPos = new MutableBlockPos();

    while (run) {
      for (BlockPos center : ends) {
        for (Direction dir : Direction.values()) {
          bPos.set(center).move(dir);
          BlockPos wpos = bPos.offset(start);

          if (!blocks.contains(bPos)) {
            if (this.getDistance(bPos.getX(), bPos.getY(), bPos.getZ()) < 0) {
              BlockState state = getBlockState(wpos);
              PosInfo.create(mapWorld, addInfo, wpos).setState(state);
              add.add(bPos.immutable());
            }
          }
        }
      }

      blocks.addAll(ends);
      ends.clear();
      ends.addAll(add);
      add.clear();

      run &= !ends.isEmpty();
    }

    List<PosInfo> infos = new ArrayList<PosInfo>(mapWorld.values());
    Collections.sort(infos);
    postProcesses.forEach((postProcess) -> {
      infos.forEach((info) -> {
        info.setState(postProcess.apply(info));
      });
    });
    infos.forEach((info) -> {
      world.setBlock(info.getPos(), info.getState());
    });

    infos.clear();
    infos.addAll(addInfo.values());
    Collections.sort(infos);
    postProcesses.forEach((postProcess) -> {
      infos.forEach((info) -> {
        info.setState(postProcess.apply(info));
      });
    });
    infos.forEach((info) -> {
      world.setBlock(info.getPos(), info.getState());
    });
  }

  public Set<BlockPos> getPositions(ServerLevelAccessor world, BlockPos start) {
    Set<BlockPos> blocks = Sets.newHashSet();
    Set<BlockPos> ends = Sets.newHashSet();
    Set<BlockPos> add = Sets.newHashSet();
    ends.add(new BlockPos(0, 0, 0));
    boolean run = true;

    MutableBlockPos bPos = new MutableBlockPos();

    while (run) {
      for (BlockPos center : ends) {
        for (Direction dir : Direction.values()) {
          bPos.set(center).move(dir);
          BlockPos wpos = bPos.offset(start);
          BlockState state = world.getBlockState(wpos);
          if (!blocks.contains(wpos) && canReplace.apply(state)) {
            if (this.getDistance(bPos.getX(), bPos.getY(), bPos.getZ()) < 0) {
              add.add(bPos.immutable());
            }
          }
        }
      }

      ends.forEach((end) -> blocks.add(end.offset(start)));
      ends.clear();
      ends.addAll(add);
      add.clear();

      run &= !ends.isEmpty();
    }

    return blocks;
  }

  public abstract static class Primitive extends SDF {

    protected Function<BlockPos, BlockState> placerFunction;

    public Primitive setBlock(Function<BlockPos, BlockState> placerFunction) {
      this.placerFunction = placerFunction;
      return this;
    }

    public Primitive setBlock(BlockState state) {
      this.placerFunction = (pos) -> {
        return state;
      };
      return this;
    }

    public Primitive setBlock(Block block) {
      this.placerFunction = (pos) -> {
        return block.defaultBlockState();
      };
      return this;
    }

    public BlockState getBlockState(BlockPos pos) {
      return placerFunction.apply(pos);
    }
  }

  public abstract static class UnaryOperator extends SDF {

    protected SDF source;

    public UnaryOperator setSource(SDF source) {
      this.source = source;
      return this;
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
      return source.getBlockState(pos);
    }
  }

  public abstract static class BinaryOperator extends SDF {

    protected SDF sourceA;
    protected SDF sourceB;
    protected boolean firstValue;

    public BinaryOperator setSourceA(SDF sourceA) {
      this.sourceA = sourceA;
      return this;
    }

    public BinaryOperator setSourceB(SDF sourceB) {
      this.sourceB = sourceB;
      return this;
    }

    protected void selectValue(float a, float b) {
      firstValue = a < b;
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
      if (firstValue) {
        return sourceA.getBlockState(pos);
      } else {
        return sourceB.getBlockState(pos);
      }
    }
  }
}
