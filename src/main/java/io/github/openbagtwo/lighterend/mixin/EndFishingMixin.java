package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.registries.LighterEndLootTables;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FishingHook.class)
public abstract class EndFishingMixin extends Entity {

  public EndFishingMixin(EntityType<?> type, Level world) {
    super(type, world);
  }

  @ModifyVariable(method = "retrieve", at = @At("STORE"))
  public LootTable useInEnd(LootTable baseFishingTable) {
    if (BuiltinDimensionTypes.END.equals(
        this.level().dimensionTypeRegistration().unwrapKey().orElse(null))
        && LighterEnd.CONFIG.endFishingHasCustomLootTable()) {
      return this.level().getServer().reloadableRegistries().getLootTable(
          LighterEndLootTables.END_FISHING);
    }
    return baseFishingTable;

  }

}
