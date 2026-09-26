package io.github.openbagtwo.lighterend.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChorusFlowerBlock.class)
public abstract class ChorusFlowerMixin {

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
  }

  @Final
  @Shadow
  private Block plant;

  @Shadow
  private static boolean allNeighborsEmpty(
      LevelReader world,
      BlockPos pos,
      @Nullable Direction exceptDirection
  ) {
    throw new AssertionError();
  }

  @Shadow
  protected abstract void placeGrownFlower(Level world, BlockPos pos, int age);


  @WrapOperation(
      method = "randomTick",
      at = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
      )
  )
  private boolean checkForEndSoil(
      BlockState state,
      Block block,
      Operation<Boolean> soFar
  ) {
    if (block == Blocks.END_STONE) {
      return soFar.call(state, block) || state.is(LighterEndTags.END_SOIL);
    }
    return soFar.call(state, block);
  }
}
