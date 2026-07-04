package io.github.openbagtwo.lighterend.mixin;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.registries.LighterEndLootTables;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FishingBobberEntity.class)
public abstract class EndFishingMixin extends Entity {

  public EndFishingMixin(EntityType<?> type, World world) {
    super(type, world);
  }

  @ModifyVariable(method = "use", at = @At("STORE"))
  public LootTable useInEnd(LootTable baseFishingTable) {
    if (DimensionTypes.THE_END.equals(
        this.getWorld().getDimensionEntry().getKey().orElse(null))
        && LighterEnd.CONFIG.endFishingHasCustomLootTable()) {
      return this.getWorld().getServer().getReloadableRegistries().getLootTable(
          LighterEndLootTables.END_FISHING);
    }
    return baseFishingTable;

  }

}
