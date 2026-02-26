package io.github.openbagtwo.lighterend.items;

import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;

public class Matchstick extends Item {

  public Matchstick(Properties settings) {
    super(settings);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Player playerEntity = context.getPlayer();
    Level world = context.getLevel();
    BlockPos blockPos = context.getClickedPos();
    BlockState blockState = world.getBlockState(blockPos);
    if (!CampfireBlock.canLight(blockState) && !CandleBlock.canLight(blockState)
        && !CandleCakeBlock.canLight(blockState)) {
      BlockPos blockPos2 = blockPos.relative(context.getClickedFace());
      if (BaseFireBlock.canBePlacedAt(world, blockPos2, context.getHorizontalDirection())) {
        world.playSound(playerEntity, blockPos2, LighterEndSounds.MATCH_STRIKE,
            SoundSource.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
        BlockState blockState2 = BaseFireBlock.getState(world, blockPos2);
        world.setBlock(blockPos2, blockState2, Block.UPDATE_ALL_IMMEDIATE);
        world.gameEvent(playerEntity, GameEvent.BLOCK_PLACE, blockPos);
        ItemStack itemStack = context.getItemInHand();
        if (playerEntity instanceof ServerPlayer) {
          CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) playerEntity, blockPos2, itemStack);
          itemStack.consume(1, playerEntity);
        }

        return InteractionResult.SUCCESS;
      } else {
        return InteractionResult.FAIL;
      }
    } else {
      world.playSound(playerEntity, blockPos, LighterEndSounds.MATCH_STRIKE, SoundSource.BLOCKS,
          1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
      world.setBlock(blockPos, blockState.setValue(BlockStateProperties.LIT, true),
          Block.UPDATE_ALL_IMMEDIATE);
      world.gameEvent(playerEntity, GameEvent.BLOCK_CHANGE, blockPos);
      if (playerEntity != null) {
        context.getItemInHand().consume(1, playerEntity);
      }

      return InteractionResult.SUCCESS;
    }
  }

}
