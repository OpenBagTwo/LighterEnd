package io.github.openbagtwo.lighterend.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class SilkMothNest extends Block {

  public static final MapCodec<SilkMothNest> CODEC = createCodec(SilkMothNest::new);
  public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;
  public static final int MAX_FULLNESS = 3;
  public static final IntProperty FULLNESS = IntProperty.of("fullness", 0, MAX_FULLNESS);

  public static final VoxelShape OUTLINE_SHAPE = VoxelShapes.union(
      Block.createCuboidShape(0, 0, 0, 16, 13, 16),
      Block.createCuboidShape(3, 12, 3, 13, 16, 13)
  );


  @Override
  public MapCodec<SilkMothNest> getCodec() {
    return CODEC;
  }

  public SilkMothNest(AbstractBlock.Settings settings) {
    super(
        settings
            .mapColor(MapColor.TERRACOTTA_YELLOW)
            .instrument(NoteBlockInstrument.BASS)
            .strength(0.3F)
            .sounds(BlockSoundGroup.WOOD)
            .nonOpaque()
            .burnable()
    );
    this.setDefaultState(
        this.stateManager.getDefaultState().with(FULLNESS, 0).with(FACING, Direction.NORTH));
  }

  @Override
  protected boolean hasComparatorOutput(BlockState state) {
    return true;
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    builder.add(FULLNESS, FACING);
  }


  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return state.with(FACING, rotation.rotate(state.get(FACING)));
  }

  @Override
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    return state.rotate(mirror.getRotation(state.get(FACING)));
  }

  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos,
      ShapeContext context) {
    return SilkMothNest.OUTLINE_SHAPE;
  }
}
