package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

public class Brimstone extends Block {

  public static final BooleanProperty ACTIVATED = BooleanProperty.of("active");

  public Brimstone(Settings settings) {
    super(
        settings
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresTool()
            .strength(3.0F, 9.0F)
            .mapColor(MapColor.BROWN)
            .ticksRandomly()
    );
    setDefaultState(stateManager.getDefaultState().with(ACTIVATED, false));
  }

  @Override
  protected void appendProperties(Builder<Block, BlockState> builder) {
    builder.add(ACTIVATED);
  }

  @Override
  protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
    boolean deactivate = true;
    for (Direction dir : DIRECTIONS) {
      if (world.getFluidState(pos.offset(dir)).getFluid().equals(Fluids.WATER)) {
        deactivate = false;
        break;
      }
    }
    if (state.get(ACTIVATED)) {
      if (deactivate) {
        world.setBlockState(pos, getDefaultState().with(ACTIVATED, false));
      } else if (state.get(ACTIVATED) && random.nextInt(16) == 0) {
        Direction dir = Direction.random(random);
        BlockPos side = pos.offset(dir);
        BlockState sideState = world.getBlockState(side);
        if (sideState.isOf(LighterEndBlocks.SULPHUR_CRYSTAL)) {
          if (
              sideState.get(SulphurCrystal.STAGE) < SulphurCrystal.MAX_STAGE
                  && sideState.get(SulphurCrystal.WATERLOGGED)
          ) {
            int age = sideState.get(SulphurCrystal.STAGE) + 1;
            world.setBlockState(side, sideState.with(SulphurCrystal.STAGE, age));
          }
        } else if (sideState.isOf(Blocks.WATER)
        ) {
          BlockState crystal = LighterEndBlocks.SULPHUR_CRYSTAL.getDefaultState()
              .with(SulphurCrystal.FACING, dir)
              .with(SulphurCrystal.WATERLOGGED, true)
              .with(SulphurCrystal.STAGE, 0);
          world.setBlockState(side, crystal);
        }
      }
    } else if (!deactivate && !state.get(ACTIVATED)) {
      world.setBlockState(pos, getDefaultState().with(ACTIVATED, true));
    }
  }
}
