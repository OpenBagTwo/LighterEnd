package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.config.Config;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.tags.LighterEndTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoneMealItem.class)
public abstract class UnderwaterBonemealMixin {

  @Inject(method = "useOnGround(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z", at = @At("HEAD"), cancellable = true)
  private static void generateEndPlants(
      ItemStack stack, World world, BlockPos blockPos, @Nullable Direction facing,
      CallbackInfoReturnable<Boolean> cir) {
    Config config = Config.loadConfiguration();
    if (
        config.bonemealingUnderwaterInEndProducesEndVegetation()
            && world.getBiome(blockPos).isIn(BiomeTags.IS_END)
    ) {
      if (world.getBlockState(blockPos).isOf(Blocks.WATER)
          && world.getFluidState(blockPos).getLevel() == 8) {
        if (!(world instanceof ServerWorld)) {
          cir.setReturnValue(true);
        } else {
          Random random = world.getRandom();

          label80:
          for (int i = 0; i < 128; i++) {
            BlockPos blockPos2 = blockPos;
            BlockState blockState = LighterEndBlocks.CHARNIA_CYAN.getDefaultState();

            for (int j = 0; j < i / 16; j++) {
              blockPos2 = blockPos2.add(random.nextInt(3) - 1,
                  (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
              if (world.getBlockState(blockPos2).isFullCube(world, blockPos2)) {
                continue label80;
              }
            }

            blockState = Registries.BLOCK
                .getRandomEntry(LighterEndTags.AQUATIC_END_VEGETATION, world.random)
                .map(blockEntry -> (blockEntry.value()).getDefaultState())
                .orElse(blockState);

            if (blockState.canPlaceAt(world, blockPos2)) {
              BlockState blockState2 = world.getBlockState(blockPos2);
              if (blockState2.isOf(Blocks.WATER)
                  && world.getFluidState(blockPos2).getLevel() == 8) {
                world.setBlockState(blockPos2, blockState, Block.NOTIFY_ALL);
              }
            }
          }

          stack.decrement(1);
          cir.setReturnValue(true);
        }
      } else {
        cir.setReturnValue(false);
      }
      cir.cancel();
    }
  }
}
