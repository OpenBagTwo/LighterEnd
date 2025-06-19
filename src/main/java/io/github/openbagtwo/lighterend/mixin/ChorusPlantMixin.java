package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChorusPlantBlock;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChorusPlantBlock.class)
public abstract class ChorusPlantMixin extends ConnectingBlock {

  protected ChorusPlantMixin(float radius, Settings settings) {
    super(radius, settings);
  }

  @Inject(method = "getStateForNeighborUpdate", at = @At("HEAD"), cancellable = true)
  public void checkBelowForEndSoil(
      BlockState state,
      WorldView world,
      ScheduledTickView tickView,
      BlockPos pos,
      Direction direction,
      BlockPos neighborPos,
      BlockState neighborState,
      Random random,
      CallbackInfoReturnable<BlockState> cir) {
    if ((state.canPlaceAt(world, pos))
        && direction == Direction.DOWN
        && neighborState.isIn(LighterEndTags.END_SOIL)) {
      cir.setReturnValue(state.with(FACING_PROPERTIES.get(direction), true));
      cir.cancel();
    }
  }

  @Inject(method = "withConnectionProperties", at = @At("RETURN"), cancellable = true)
  private static void withConnectedEndSoil(
      BlockView world, BlockPos pos, BlockState state, CallbackInfoReturnable<BlockState> cir
  ) {
    BlockState down = world.getBlockState(pos.down());
    Block block = state.getBlock();
    cir.setReturnValue(
        cir.getReturnValue().withIfExists(
            DOWN,
            down.isOf(block)
                || down.isOf(Blocks.CHORUS_FLOWER)
                || down.isOf(Blocks.END_STONE)
                || down.isIn(LighterEndTags.END_SOIL)
        )
    );
  }

  @Inject(method = "canPlaceAt", at = @At("HEAD"), cancellable = true)
  public void placeOnEndSoil(
      BlockState state,
      WorldView world,
      BlockPos pos,
      CallbackInfoReturnable<Boolean> cir
  ) {
    if (world.getBlockState(pos.down()).isIn(LighterEndTags.END_SOIL)) {
      cir.setReturnValue(true);
      cir.cancel();
    }
    for (Direction direction : Direction.Type.HORIZONTAL) {
      BlockPos horizontal = pos.offset(direction);
      if (world.getBlockState(horizontal).isOf(Blocks.CHORUS_PLANT)) {
        BlockState diagonal = world.getBlockState(horizontal.down());
        if (
            diagonal.isOf(Blocks.CHORUS_PLANT)
                || diagonal.isOf(Blocks.END_STONE)
                || diagonal.isIn(
                LighterEndTags.END_SOIL)
        ) {
          cir.setReturnValue(true);
          cir.cancel();
          break;
        }
      }
    }
  }
}
