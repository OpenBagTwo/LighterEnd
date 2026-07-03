package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.world.features.UnderwaterPlants;
import net.minecraft.block.Blocks;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
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
    if (
        LighterEnd.CONFIG.bonemealingUnderwaterInEndProducesEndVegetation()
            && world.getBiome(blockPos).isIn(BiomeTags.IS_END)
    ) {
      if (world.getBlockState(blockPos).isOf(Blocks.WATER)
          && world.getFluidState(blockPos).getLevel() == 8) {
        if (!(world instanceof ServerWorld)) {
          cir.setReturnValue(true);
        } else {
          FeatureContext<DefaultFeatureConfig> context = new FeatureContext<>(null,
              (ServerWorld) world,
              ((ServerWorld) world).getChunkManager().getChunkGenerator(), world.getRandom(),
              blockPos,
              new DefaultFeatureConfig());

          boolean success = new UnderwaterPlants().generate(context);
          if (success) {
            stack.decrement(1);
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
