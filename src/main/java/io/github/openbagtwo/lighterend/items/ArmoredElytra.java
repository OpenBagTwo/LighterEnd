package io.github.openbagtwo.lighterend.items;

import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.util.Rarity;
import net.minecraft.util.Unit;

public class ArmoredElytra extends Item {

  public final float glideDecay;

  public ArmoredElytra(
      Settings settings,
      ArmorMaterial material,
      int durability,
      float glideDecay,
      boolean fireproof
  ) {
    super(applySettings(settings, material, durability, fireproof));
    this.glideDecay = glideDecay;
  }

  private static Settings applySettings(
      Settings settings,
      ArmorMaterial material,
      int durability,
      boolean fireproof
  ) {
    settings = settings
        .armor(material, EquipmentType.CHESTPLATE)
        .maxDamage(durability)
        .rarity(Rarity.EPIC)
        .component(DataComponentTypes.GLIDER, Unit.INSTANCE)
        .component(
            DataComponentTypes.EQUIPPABLE,
            EquippableComponent.builder(EquipmentSlot.CHEST)
                .equipSound(LighterEndSounds.EQUIP_SILK)
                .model(material.assetId())
                .damageOnHurt(true)
                .build()
        );
    if (fireproof) {
      settings = settings.fireproof();
    }
    return settings;
  }

}
