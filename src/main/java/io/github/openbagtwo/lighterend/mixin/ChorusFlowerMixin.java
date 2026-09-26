package io.github.openbagtwo.lighterend.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChorusFlowerBlock.class)
public abstract class ChorusFlowerMixin {

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
  }

  @Final
  @Shadow
  private Block plantBlock;

  @Shadow
  private static boolean isSurroundedByAir(
      WorldView world,
      BlockPos pos,
      @Nullable Direction exceptDirection
  ) {
    throw new AssertionError();
  }

  @Shadow
  protected abstract void grow(World world, BlockPos pos, int age);


  @WrapOperation(
      method = "randomTick",
      at = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"
      )
  )
  private boolean checkForEndSoil(
      BlockState state,
      Block block,
      Operation<Boolean> soFar
  ) {
    if (block == Blocks.END_STONE) {
      return soFar.call(state, block) || state.isIn(LighterEndTags.END_SOIL);
    }
    return soFar.call(state, block);
  }
}
