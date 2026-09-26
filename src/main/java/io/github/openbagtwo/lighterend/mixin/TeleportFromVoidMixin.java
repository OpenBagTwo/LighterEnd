package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.registries.LighterEndData;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DeathProtectionComponent;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class TeleportFromVoidMixin {

  @Shadow
  public abstract ItemStack getStackInHand(Hand hand);

  @Inject(method = "tryUseDeathProtector", at = @At("HEAD"), cancellable = true)
  public void allowTeleportingTotem(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
    for (Hand hand : Hand.values()) {
      ItemStack handItem = this.getStackInHand(hand);
      DeathProtectionComponent totemFX = handItem.get(DataComponentTypes.DEATH_PROTECTION);
      if (totemFX != null && handItem.get(LighterEndData.TOTEM_TARGET) != null) {
        LivingEntity user = ((LivingEntity) (Object) this);
        if (user instanceof ServerPlayerEntity player) {
          player.incrementStat(Stats.USED.getOrCreateStat(handItem.getItem()));
          Criteria.USED_TOTEM.trigger(player, handItem);
          player.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
        }
        user.setHealth(1.0F);
        totemFX.applyDeathEffects(handItem, user);
        user.getWorld().sendEntityStatus(user, EntityStatuses.USE_TOTEM_OF_UNDYING);

        handItem.decrement(1);
        cir.setReturnValue(true);
        break;
      }
    }
  }
}
