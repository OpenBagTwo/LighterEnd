package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.blocks.entities.PedestalDisplay;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Prediction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SelectableSlotContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Pedestal extends BaseEntityBlock implements SelectableSlotContainer {

  private static final VoxelShape SHAPE;

  public Pedestal(Properties settings) {
    super(settings.pushReaction(PushReaction.IMMOVEABLE));
  }

  @Override
  protected VoxelShape getShape(
      BlockState state,
      BlockGetter world,
      BlockPos pos,
      CollisionContext context
  ) {
    return SHAPE;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new PedestalDisplay(pos, state);
  }

  @Override
  protected InteractionResult useItemOn(
      ItemStack stack,
      BlockState state,
      Level world,
      BlockPos pos,
      Player player,
      InteractionHand hand,
      BlockHitResult hit
  ) {
    if ((world.getBlockEntity(pos) instanceof PedestalDisplay display) && (!world.isClientSide())) {

      boolean makeSound = false;
      ItemStack toInsert = ItemStack.EMPTY;

      if (!stack.isEmpty()) {
        toInsert = stack.copyWithCount(1);
        stack.shrink(1);
        makeSound = true;
      }
      if (!display.isEmpty()) {
        ItemStack stackOnPedestal = display.getItem(0);
        if (!player.getInventory().add(stackOnPedestal)) {
          player.drop(stackOnPedestal, false, Prediction.PREDICTED);
        }
        makeSound = true;
        display.clearContent();
      }
      if (makeSound) {
        display.setItem(0, toInsert);
        display.setChanged();
        player.getInventory().setChanged();
        world.playSound(player, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
        world.gameEvent(GameEvent.ENTITY_INTERACT, pos, GameEvent.Context.of(state));
        world.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
      }
    }

    return InteractionResult.SUCCESS;
  }

  @Override
  public int getRows() {
    return 1;
  }

  @Override
  public int getColumns() {
    return 1;
  }

  static {
    VoxelShape basinUp = Block.box(2, 3, 2, 14, 4, 14);
    VoxelShape basinDown = Block.box(0, 0, 0, 16, 3, 16);
    VoxelShape pedestalDefault = Block.box(1, 12, 1, 15, 14, 15);
    VoxelShape pillarDefault = Block.box(3, 0, 3, 13, 12, 13);
    VoxelShape basin = Shapes.or(basinDown, basinUp);
    SHAPE = Shapes.or(basin, pillarDefault, pedestalDefault);
  }


}
