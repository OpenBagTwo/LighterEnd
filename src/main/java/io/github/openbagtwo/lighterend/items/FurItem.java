package io.github.openbagtwo.lighterend.items;

import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

public class FurItem extends BlockItem {

  public FurItem(Block furBlock, Properties settings) {
    super(
        furBlock,
        settings.component(
            DataComponents.EQUIPPABLE,
            Equippable.builder(EquipmentSlot.HEAD)
                .setSwappable(false)
                .setEquipSound(LighterEndSounds.EQUIP_FUR)
                .build()
        ).compostable(ContextIntProviders.COMPOSTABLE_MEDIUM)
    );
  }
}
