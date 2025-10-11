package io.github.openbagtwo.lighterend.items;

import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.CandleBlock;
import net.minecraft.block.CandleCakeBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class Matchstick extends Item {

  public Matchstick(Settings settings) {
    super(settings);
  }

  @Override
  public ActionResult useOnBlock(ItemUsageContext context) {
    PlayerEntity playerEntity = context.getPlayer();
    World world = context.getWorld();
    BlockPos blockPos = context.getBlockPos();
    BlockState blockState = world.getBlockState(blockPos);
    if (!CampfireBlock.canBeLit(blockState) && !CandleBlock.canBeLit(blockState)
        && !CandleCakeBlock.canBeLit(blockState)) {
      BlockPos blockPos2 = blockPos.offset(context.getSide());
      if (AbstractFireBlock.canPlaceAt(world, blockPos2, context.getHorizontalPlayerFacing())) {
        world.playSound(playerEntity, blockPos2, LighterEndSounds.MATCH_STRIKE,
            SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
        BlockState blockState2 = AbstractFireBlock.getState(world, blockPos2);
        world.setBlockState(blockPos2, blockState2, Block.NOTIFY_ALL_AND_REDRAW);
        world.emitGameEvent(playerEntity, GameEvent.BLOCK_PLACE, blockPos);
        ItemStack itemStack = context.getStack();
        if (playerEntity instanceof ServerPlayerEntity) {
          Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) playerEntity, blockPos2, itemStack);
          itemStack.decrementUnlessCreative(1, playerEntity);
        }

        return ActionResult.SUCCESS;
      } else {
        return ActionResult.FAIL;
      }
    } else {
      world.playSound(playerEntity, blockPos, LighterEndSounds.MATCH_STRIKE, SoundCategory.BLOCKS,
          1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
      world.setBlockState(blockPos, blockState.with(Properties.LIT, true),
          Block.NOTIFY_ALL_AND_REDRAW);
      world.emitGameEvent(playerEntity, GameEvent.BLOCK_CHANGE, blockPos);
      if (playerEntity != null) {
        context.getStack().decrementUnlessCreative(1, playerEntity);
      }

      return ActionResult.SUCCESS;
    }
  }

}
