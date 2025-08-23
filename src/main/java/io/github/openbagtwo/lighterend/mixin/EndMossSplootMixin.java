package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.passive.SnifferEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnifferEntity.class)
public abstract class EndMossSplootMixin extends LivingEntity {

  protected EndMossSplootMixin(EntityType<? extends LivingEntity> entityType, World world) {
    super(entityType, world);
  }

  @Shadow
  protected abstract BlockPos getDigPos();

  @Accessor("FINISH_DIG_TIME")
  public static TrackedData<Integer> getFinishDigTime() {
    throw new AssertionError();
  }

  @Inject(method = "dropSeeds", at = @At("HEAD"), cancellable = true)
  public void dropEndSplootLoot(CallbackInfo ci) {
    if (this.getEntityWorld() instanceof ServerWorld serverWorld
        && this.dataTracker.get(getFinishDigTime()) == this.age) {
      BlockPos blockPos = this.getDigPos();
      if (serverWorld.getBlockState(blockPos.down()).isOf(LighterEndBlocks.END_MOSS)) {
        this.forEachGiftedItem(serverWorld, LighterEndLootTables.END_MOSS_SPLOOT_LOOT,
            (serverWorldx, itemStack) -> {
              ItemEntity itemEntity = new ItemEntity(this.getEntityWorld(), blockPos.getX(),
                  blockPos.getY(), blockPos.getZ(), itemStack);
              itemEntity.setToDefaultPickupDelay();
              serverWorldx.spawnEntity(itemEntity);
            });
        this.playSound(SoundEvents.ENTITY_SNIFFER_DROP_SEED, 1.0F, 1.0F);
        ci.cancel();
      }
    }
  }

}
