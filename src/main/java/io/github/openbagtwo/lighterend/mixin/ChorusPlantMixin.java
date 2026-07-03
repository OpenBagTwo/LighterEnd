package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChorusPlantBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChorusPlantBlock.class)
public abstract class ChorusPlantMixin extends PipeBlock {

  protected ChorusPlantMixin(float radius, Properties settings) {
    super(radius, settings);
  }

  @Inject(method = "updateShape", at = @At("HEAD"), cancellable = true)
  public void checkBelowForEndSoil(
      BlockState state,
      LevelReader world,
      ScheduledTickAccess tickView,
      BlockPos pos,
      Direction direction,
      BlockPos neighborPos,
      BlockState neighborState,
      RandomSource random,
      CallbackInfoReturnable<BlockState> cir) {
    if ((state.canSurvive(world, pos))
        && direction == Direction.DOWN
        && neighborState.is(LighterEndTags.END_SOIL)) {
      cir.setReturnValue(state.setValue(PROPERTY_BY_DIRECTION.get(direction), true));
      cir.cancel();
    }
  }

  @Inject(method = "getStateWithConnections", at = @At("RETURN"), cancellable = true)
  private static void withConnectedEndSoil(
      BlockGetter world, BlockPos pos, BlockState state, CallbackInfoReturnable<BlockState> cir
  ) {
    BlockState down = world.getBlockState(pos.below());
    Block block = state.getBlock();
    cir.setReturnValue(
        cir.getReturnValue().trySetValue(
            DOWN,
            down.is(block)
                || down.is(Blocks.CHORUS_FLOWER)
                || down.is(Blocks.END_STONE)
                || down.is(LighterEndTags.END_SOIL)
        )
    );
  }

  @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
  public void placeOnEndSoil(
      BlockState state,
      LevelReader world,
      BlockPos pos,
      CallbackInfoReturnable<Boolean> cir
  ) {
    if (world.getBlockState(pos.below()).is(LighterEndTags.END_SOIL)) {
      cir.setReturnValue(true);
      cir.cancel();
    }
    for (Direction direction : Direction.Plane.HORIZONTAL) {
      BlockPos horizontal = pos.relative(direction);
      if (world.getBlockState(horizontal).is(Blocks.CHORUS_PLANT)) {
        BlockState diagonal = world.getBlockState(horizontal.below());
        if (
            diagonal.is(Blocks.CHORUS_PLANT)
                || diagonal.is(Blocks.END_STONE)
                || diagonal.is(
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
