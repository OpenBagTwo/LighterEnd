package io.github.openbagtwo.lighterend.Items;

import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import net.minecraft.block.Block;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BlockItem;

public class Fur extends BlockItem {

  public Fur(Block furBlock, Settings settings) {
    super(
        furBlock,
        settings.component(
            DataComponentTypes.EQUIPPABLE,
            EquippableComponent.builder(EquipmentSlot.HEAD)
                .swappable(false)
                .equipSound(LighterEndSounds.EQUIP_FUR)
                .build()
        )
    );
  }
}
