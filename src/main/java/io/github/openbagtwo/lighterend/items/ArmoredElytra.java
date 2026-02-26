package io.github.openbagtwo.lighterend.items;

import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.Equippable;

public class ArmoredElytra extends Item {

  public final float glideDecay;

  public ArmoredElytra(
      Properties settings,
      ArmorMaterial material,
      int durability,
      float glideDecay,
      boolean fireproof
  ) {
    super(applySettings(settings, material, durability, fireproof));
    this.glideDecay = glideDecay;
  }

  private static Properties applySettings(
      Properties settings,
      ArmorMaterial material,
      int durability,
      boolean fireproof
  ) {
    settings = settings
        .humanoidArmor(material, ArmorType.CHESTPLATE)
        .durability(durability)
        .rarity(Rarity.EPIC)
        .component(DataComponents.GLIDER, Unit.INSTANCE)
        .component(
            DataComponents.EQUIPPABLE,
            Equippable.builder(EquipmentSlot.CHEST)
                .setEquipSound(LighterEndSounds.EQUIP_SILK)
                .setAsset(material.assetId())
                .setDamageOnHurt(true)
                .build()
        );
    if (fireproof) {
      settings = settings.fireResistant();
    }
    return settings;
  }

}
