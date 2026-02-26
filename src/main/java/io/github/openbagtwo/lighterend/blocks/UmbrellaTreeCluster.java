package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

public class UmbrellaTreeCluster extends Block {

  public static final BooleanProperty NATURAL = BooleanProperty.create("natural");

  public UmbrellaTreeCluster(Properties settings) {
    super(settings.mapColor(MapColor.COLOR_PURPLE).strength(1.0F).sound(SoundType.WART_BLOCK)
        .lightLevel((bs) -> 15)
    );
    registerDefaultState(stateDefinition.any().setValue(NATURAL, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
    stateManager.add(NATURAL);
  }

  @Override
  protected InteractionResult useItemOn(
      ItemStack stack,
      BlockState state,
      Level world,
      BlockPos pos,
      Player player,
      InteractionHand hand,
      BlockHitResult blockHitResult
  ) {
    if (stack.getItem() == Items.GLASS_BOTTLE) {
      if (!player.isCreative()) {
        stack.shrink(1);
      }
      stack = new ItemStack(LighterEndItems.UMBRELLA_JUICE);
      player.addItem(stack);
      world.playLocalSound(
          pos.getX() + 0.5,
          pos.getY() + 0.5,
          pos.getZ() + 0.5,
          SoundEvents.BOTTLE_FILL,
          SoundSource.BLOCKS,
          1,
          1,
          false
      );
      world.setBlock(pos,
          LighterEndBlocks.UMBRELLA_TREE_CLUSTER_EMPTY.defaultBlockState().setValue(NATURAL,
              state.getValue(NATURAL)), Block.UPDATE_ALL);

      return InteractionResult.SUCCESS;
    }
    return InteractionResult.FAIL;
  }

  public static class EmptyCluster extends Block {

    public static final BooleanProperty NATURAL = UmbrellaTreeCluster.NATURAL;

    public EmptyCluster(Properties settings) {
      super(settings.mapColor(MapColor.COLOR_PURPLE).strength(1.0F).sound(SoundType.WART_BLOCK)
          .randomTicks());
      registerDefaultState(stateDefinition.any().setValue(NATURAL, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
      stateManager.add(NATURAL);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
      if (state.getValue(NATURAL) && random.nextInt(16) == 0) {
        world.setBlockAndUpdate(pos,
            LighterEndBlocks.UMBRELLA_TREE_CLUSTER.defaultBlockState()
                .setValue(NATURAL, state.getValue(NATURAL)));
      }
    }
  }
}
