package io.github.openbagtwo.lighterend.registries;

import com.google.common.collect.Maps;
import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.items.ArmoredElytra;
import java.util.Map;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;

public class LighterEndEquipment {


  public static final ResourceKey<EquipmentAsset> SILK_MATERIAL = ResourceKey.create(
      ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("equipment_asset")),
      LighterEnd.of("silk")
  );

  public static final ArmorMaterial SILK_ARMOR = new ArmorMaterial(
      10, // durability base puts it between gold and iron
      Maps.newEnumMap(
          Map.of(
              ArmorType.BOOTS, 1,  // nerf boot defense
              ArmorType.LEGGINGS, 6,
              ArmorType.CHESTPLATE, 8,
              ArmorType.HELMET, 3,
              ArmorType.BODY, 4  // on par with chainmail
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
      new Properties()
  );

  public static void initialize() {
  }

}
