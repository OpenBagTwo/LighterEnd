package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.block.ChorusPlantBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
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

  @Shadow
  protected abstract void die(World world, BlockPos pos);

  /**
   * @author OpenBagTwo
   * @reason allow flowers to grow on end soil
   */
  @Overwrite
  public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
    BlockPos blockPos = pos.up();
    if (world.isAir(blockPos) && blockPos.getY() <= world.getTopYInclusive()) {
      int i = state.get(ChorusFlowerBlock.AGE);
      if (i < 5) {
        boolean bl = false;
        boolean bl2 = false;
        BlockState blockState = world.getBlockState(pos.down());
        if (blockState.isOf(Blocks.END_STONE) || blockState.isIn(LighterEndTags.END_SOIL)) {
          bl = true;
        } else if (blockState.isOf(this.plantBlock)) {
          int j = 1;

          for (int k = 0; k < 4; k++) {
            BlockState blockState2 = world.getBlockState(pos.down(j + 1));
            if (!blockState2.isOf(this.plantBlock)) {
              if (blockState2.isOf(Blocks.END_STONE) || blockState2.isIn(LighterEndTags.END_SOIL)) {
                bl2 = true;
              }
              break;
            }

            j++;
          }

          if (j < 2 || j <= random.nextInt(bl2 ? 5 : 4)) {
            bl = true;
          }
        } else if (blockState.isAir()) {
          bl = true;
        }

        if (bl && isSurroundedByAir(world, blockPos, null) && world.isAir(pos.up(2))) {
          world.setBlockState(pos, ChorusPlantBlock.withConnectionProperties(world, pos,
              this.plantBlock.getDefaultState()), Block.NOTIFY_LISTENERS);
          this.grow(world, blockPos, i);
        } else if (i < 4) {
          int j = random.nextInt(4);
          if (bl2) {
            j++;
          }

          boolean bl3 = false;

          for (int l = 0; l < j; l++) {
            Direction direction = Direction.Type.HORIZONTAL.random(random);
            BlockPos blockPos2 = pos.offset(direction);
            if (world.isAir(blockPos2) && world.isAir(blockPos2.down()) && isSurroundedByAir(world,
                blockPos2, direction.getOpposite())) {
              this.grow(world, blockPos2, i + 1);
              bl3 = true;
            }
          }

          if (bl3) {
            world.setBlockState(pos, ChorusPlantBlock.withConnectionProperties(world, pos,
                this.plantBlock.getDefaultState()), Block.NOTIFY_LISTENERS);
          } else {
            this.die(world, pos);
          }
        } else {
          this.die(world, pos);
        }
      }
    }
  }
}
