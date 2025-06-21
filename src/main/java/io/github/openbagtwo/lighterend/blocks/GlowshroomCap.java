package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;

public class GlowshroomCap extends Block {

  public static final BooleanProperty TRANSITION = BooleanProperty.of("transition");

  public GlowshroomCap(Settings settings) {
    super(
        settings
            .mapColor(MapColor.OAK_TAN)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F)
            .sounds(BlockSoundGroup.WOOD)
    );
    this.setDefaultState(this.stateManager.getDefaultState().with(TRANSITION, false));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    builder.add(TRANSITION);
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    BlockState below = ctx.getWorld().getBlockState(ctx.getBlockPos().down());
    return this.getDefaultState()
        .with(
            TRANSITION,
            below.isOf(LighterEndBlocks.GLOWSHROOM.wood)
                || below.isOf(LighterEndBlocks.GLOWSHROOM.log)
        );
  }


}
