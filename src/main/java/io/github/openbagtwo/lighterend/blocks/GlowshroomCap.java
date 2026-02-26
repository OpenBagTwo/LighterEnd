package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class GlowshroomCap extends Block {

  public static final BooleanProperty TRANSITION = BooleanProperty.create("transition");

  public GlowshroomCap(Properties settings) {
    super(
        settings
            .mapColor(MapColor.WOOD)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F)
            .sound(SoundType.WOOD)
    );
    this.registerDefaultState(this.stateDefinition.any().setValue(TRANSITION, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(TRANSITION);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    BlockState below = ctx.getLevel().getBlockState(ctx.getClickedPos().below());
    return this.defaultBlockState()
        .setValue(
            TRANSITION,
            below.is(LighterEndBlocks.GLOWSHROOM.wood)
                || below.is(LighterEndBlocks.GLOWSHROOM.log)
        );
  }


}
