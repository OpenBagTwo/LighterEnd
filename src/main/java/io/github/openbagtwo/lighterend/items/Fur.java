package io.github.openbagtwo.lighterend.items;

import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.Block;

public class Fur extends BlockItem {

  public Fur(Block furBlock, Properties settings) {
    super(
        furBlock,
        settings.component(
            DataComponents.EQUIPPABLE,
            Equippable.builder(EquipmentSlot.HEAD)
                .setSwappable(false)
                .setEquipSound(LighterEndSounds.EQUIP_FUR)
                .build()
        )
    );
  }
}
