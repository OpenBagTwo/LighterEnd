package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.world.features.UnderwaterPlants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BoneMealItem.class)
public abstract class UnderwaterBonemealMixin {

  @Inject(method = "growWaterPlant(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z", at = @At("HEAD"), cancellable = true)
  private static void generateEndPlants(
      ItemStack stack, Level world, BlockPos blockPos, @Nullable Direction facing,
      CallbackInfoReturnable<Boolean> cir) {
    if (
        LighterEnd.CONFIG.bonemealingUnderwaterInEndProducesEndVegetation()
            && world.getBiome(blockPos).is(BiomeTags.IS_END)
    ) {
      if (world.getBlockState(blockPos).is(Blocks.WATER)
          && world.getFluidState(blockPos).getAmount() == 8) {
        if (!(world instanceof ServerLevel)) {
          cir.setReturnValue(true);
        } else {
          FeaturePlaceContext<NoneFeatureConfiguration> context = new FeaturePlaceContext<>(null,
              (ServerLevel) world,
              ((ServerLevel) world).getChunkSource().getGenerator(), world.getRandom(),
              blockPos,
              new NoneFeatureConfiguration());

          boolean success = new UnderwaterPlants().place(context);
          if (success) {
            stack.shrink(1);
          }
          cir.setReturnValue(success);
        }
      } else {
        cir.setReturnValue(false);
      }
      cir.cancel();
    }
  }
}
