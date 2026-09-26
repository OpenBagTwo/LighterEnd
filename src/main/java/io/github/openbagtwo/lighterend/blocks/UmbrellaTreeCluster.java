package io.github.openbagtwo.lighterend.blocks;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class UmbrellaTreeCluster extends Block {

  public static final BooleanProperty NATURAL = BooleanProperty.of("natural");

  public UmbrellaTreeCluster(Settings settings) {
    super(settings.mapColor(MapColor.PURPLE).strength(1.0F).sounds(BlockSoundGroup.WART_BLOCK)
        .luminance((bs) -> 15)
    );
    setDefaultState(stateManager.getDefaultState().with(NATURAL, false));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
    stateManager.add(NATURAL);
  }

  @Override
  protected ActionResult onUseWithItem(
      ItemStack stack,
      BlockState state,
      World world,
      BlockPos pos,
      PlayerEntity player,
      Hand hand,
      BlockHitResult blockHitResult
  ) {
    if (stack.getItem() == Items.GLASS_BOTTLE) {
      if (!player.isCreative()) {
        stack.decrement(1);
      }
      stack = new ItemStack(LighterEndItems.UMBRELLA_JUICE);
      player.giveItemStack(stack);
      world.playSoundClient(
          pos.getX() + 0.5,
          pos.getY() + 0.5,
          pos.getZ() + 0.5,
          SoundEvents.ITEM_BOTTLE_FILL,
          SoundCategory.BLOCKS,
          1,
          1,
          false
      );
      world.setBlockState(pos,
          LighterEndBlocks.UMBRELLA_TREE_CLUSTER_EMPTY.getDefaultState().with(NATURAL,
              state.get(NATURAL)), Block.NOTIFY_ALL);

      return ActionResult.SUCCESS;
    }
    return ActionResult.FAIL;
  }

  public static class EmptyCluster extends Block {

    public static final BooleanProperty NATURAL = UmbrellaTreeCluster.NATURAL;

    public EmptyCluster(Settings settings) {
      super(settings.mapColor(MapColor.PURPLE).strength(1.0F).sounds(BlockSoundGroup.WART_BLOCK)
          .ticksRandomly());
      setDefaultState(stateManager.getDefaultState().with(NATURAL, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
      stateManager.add(NATURAL);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
      if (state.get(NATURAL) && random.nextInt(16) == 0) {
        world.setBlockState(pos,
            LighterEndBlocks.UMBRELLA_TREE_CLUSTER.getDefaultState()
                .with(NATURAL, state.get(NATURAL)));
      }
    }
  }
}
