package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.registries.LighterEndData;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class TeleportFromVoidMixin {

  @Shadow
  public abstract ItemStack getItemInHand(InteractionHand hand);

  @Inject(method = "checkTotemDeathProtection", at = @At("HEAD"), cancellable = true)
  public void allowTeleportingTotem(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
    for (InteractionHand hand : InteractionHand.values()) {
      ItemStack handItem = this.getItemInHand(hand);
      DeathProtection totemFX = handItem.get(DataComponents.DEATH_PROTECTION);
      if (totemFX != null && handItem.get(LighterEndData.TOTEM_TARGET) != null) {
        LivingEntity user = ((LivingEntity) (Object) this);
        if (user instanceof ServerPlayer player) {
          player.awardStat(Stats.ITEM_USED.get(handItem.getItem()));
          CriteriaTriggers.USED_TOTEM.trigger(player, handItem);
          player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        }
        user.setHealth(1.0F);
        totemFX.applyEffects(handItem, user);
        user.level().broadcastEntityEvent(user, EntityEvent.PROTECTED_FROM_DEATH);

        handItem.shrink(1);
        cir.setReturnValue(true);
        break;
      }
    }
  }
}
