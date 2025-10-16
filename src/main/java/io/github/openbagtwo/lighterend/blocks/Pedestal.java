package io.github.openbagtwo.lighterend.blocks;

import com.mojang.serialization.MapCodec;
import io.github.openbagtwo.lighterend.blocks.entities.PedestalDisplay;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class Pedestal extends BlockWithEntity {

  private static final VoxelShape SHAPE;

  public static final MapCodec<Pedestal> CODEC = Pedestal.createCodec(Pedestal::new);

  public Pedestal(Settings settings) {
    super(
        settings
            .pistonBehavior(PistonBehavior.BLOCK)
    );
  }

  @Override
  protected VoxelShape getOutlineShape(
      BlockState state,
      BlockView world,
      BlockPos pos,
      ShapeContext context
  ) {
    return SHAPE;
  }

  @Override
  protected MapCodec<? extends BlockWithEntity> getCodec() {
    return CODEC;
  }

  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new PedestalDisplay(pos, state);
  }

  @Override
  protected ActionResult onUseWithItem(
      ItemStack stack,
      BlockState state,
      World world,
      BlockPos pos,
      PlayerEntity player,
      Hand hand,
      BlockHitResult hit
  ) {
    if (world.getBlockEntity(pos) instanceof PedestalDisplay display) {

      boolean makeSound = false;
      ItemStack toInsert = ItemStack.EMPTY;

      if (!stack.isEmpty()) {
        toInsert = stack.copyWithCount(1);
        stack.decrement(1);
        makeSound = true;
      }
      if (!display.isEmpty()) {
        ItemStack stackOnPedestal = display.getStack(0);
        if (!player.getInventory().insertStack(stackOnPedestal)) {
          player.dropItem(stackOnPedestal, false);
        }
        makeSound = true;
        display.clear();
      }
      if (makeSound) {
        display.setStack(0, toInsert);
        display.markDirty();
        world.playSound(player, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 2f);
        world.updateListeners(pos, state, state, 0);
      }
    }

    return ActionResult.SUCCESS;
  }

  static {
    VoxelShape basinUp = Block.createCuboidShape(2, 3, 2, 14, 4, 14);
    VoxelShape basinDown = Block.createCuboidShape(0, 0, 0, 16, 3, 16);
    VoxelShape pedestalDefault = Block.createCuboidShape(1, 12, 1, 15, 14, 15);
    VoxelShape pillarDefault = Block.createCuboidShape(3, 0, 3, 13, 12, 13);
    VoxelShape basin = VoxelShapes.union(basinDown, basinUp);
    SHAPE = VoxelShapes.union(basin, pillarDefault, pedestalDefault);
  }

}
