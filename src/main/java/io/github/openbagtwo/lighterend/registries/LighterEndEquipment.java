package io.github.openbagtwo.lighterend.registries;

import com.google.common.collect.Maps;
import io.github.openbagtwo.lighterend.Items.ArmoredElytra;
import io.github.openbagtwo.lighterend.LighterEnd;
import java.util.Map;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class LighterEndEquipment {


  public static final RegistryKey<EquipmentAsset> SILK_MATERIAL = RegistryKey.of(
      RegistryKey.ofRegistry(Identifier.ofVanilla("equipment_asset")),
      LighterEnd.of("silk")
  );

  public static final ArmorMaterial SILK_ARMOR = new ArmorMaterial(
      10, // durability base puts it between gold and iron
      Maps.newEnumMap(
          Map.of(
              EquipmentType.BOOTS, 1,  // nerf boot defense
              EquipmentType.LEGGINGS, 6,
              EquipmentType.CHESTPLATE, 8,
              EquipmentType.HELMET, 3,
              EquipmentType.BODY, 4  // on par with chainmail
          )
      ),
      25, // enchantability on par with gold
      LighterEndSounds.EQUIP_SILK,
      0.0F,  // no toughness
      0.0F,  // no knockback protection,
      LighterEndTags.REPAIRS_SILK_ARMOR,
      SILK_MATERIAL
  );  // Note: most of these stats will never be used

  public static final Item SILK_ELYTRA = LighterEndItems.register(
      "silk_elytra",
      settings -> new ArmoredElytra(settings, SILK_ARMOR, 100, 0.95F, false),
      new Settings()
  );

  public static void initialize() {
  }

}
