package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndLootTables;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Sniffer.class)
public abstract class EndMossSplootMixin extends LivingEntity {

  protected EndMossSplootMixin(EntityType<? extends LivingEntity> entityType, Level world) {
    super(entityType, world);
  }

  @Shadow
  protected abstract BlockPos getHeadBlock();

  @Accessor("DATA_DROP_SEED_AT_TICK")
  public static EntityDataAccessor<Integer> getFinishDigTime() {
    throw new AssertionError();
  }

  @Inject(method = "dropSeed", at = @At("HEAD"), cancellable = true)
  public void dropEndSplootLoot(CallbackInfo ci) {
    if (this.level() instanceof ServerLevel serverWorld
        && this.entityData.get(getFinishDigTime()) == this.tickCount) {
      BlockPos blockPos = this.getHeadBlock();
      if (serverWorld.getBlockState(blockPos.below()).is(LighterEndBlocks.END_MOSS)) {
        this.dropFromGiftLootTable(serverWorld, LighterEndLootTables.END_MOSS_SPLOOT_LOOT,
            (serverWorldx, itemStack) -> {
              ItemEntity itemEntity = new ItemEntity(this.level(), blockPos.getX(),
                  blockPos.getY(), blockPos.getZ(), itemStack);
              itemEntity.setDefaultPickUpDelay();
              serverWorldx.addFreshEntity(itemEntity);
            });
        this.playSound(SoundEvents.SNIFFER_DROP_SEED, 1.0F, 1.0F);
        ci.cancel();
      }
    }
  }

}
