package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;

public class Brimstone extends Block {

  public static final BooleanProperty ACTIVATED = BooleanProperty.create("active");

  public Brimstone(Properties settings) {
    super(
        settings
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(3.0F, 9.0F)
            .mapColor(MapColor.COLOR_BROWN)
            .randomTicks()
    );
    registerDefaultState(stateDefinition.any().setValue(ACTIVATED, false));
  }

  @Override
  protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
    builder.add(ACTIVATED);
  }

  @Override
  protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
    boolean deactivate = true;
    for (Direction dir : UPDATE_SHAPE_ORDER) {
      if (world.getFluidState(pos.relative(dir)).getType().equals(Fluids.WATER)) {
        deactivate = false;
        break;
      }
    }
    if (state.getValue(ACTIVATED)) {
      if (deactivate) {
        world.setBlockAndUpdate(pos, defaultBlockState().setValue(ACTIVATED, false));
      } else if (state.getValue(ACTIVATED) && random.nextInt(16) == 0) {
        Direction dir = Direction.getRandom(random);
        BlockPos side = pos.relative(dir);
        BlockState sideState = world.getBlockState(side);
        if (sideState.is(LighterEndBlocks.SULPHUR_CRYSTAL)) {
          if (
              sideState.getValue(SulphurCrystal.STAGE) < SulphurCrystal.MAX_STAGE
                  && sideState.getValue(SulphurCrystal.WATERLOGGED)
          ) {
            int age = sideState.getValue(SulphurCrystal.STAGE) + 1;
            world.setBlockAndUpdate(side, sideState.setValue(SulphurCrystal.STAGE, age));
          }
        } else if (sideState.is(Blocks.WATER)
        ) {
          BlockState crystal = LighterEndBlocks.SULPHUR_CRYSTAL.defaultBlockState()
              .setValue(SulphurCrystal.FACING, dir)
              .setValue(SulphurCrystal.WATERLOGGED, true)
              .setValue(SulphurCrystal.STAGE, 0);
          world.setBlockAndUpdate(side, crystal);
        }
      }
    } else if (!deactivate && !state.getValue(ACTIVATED)) {
      world.setBlockAndUpdate(pos, defaultBlockState().setValue(ACTIVATED, true));
    }
  }
}
