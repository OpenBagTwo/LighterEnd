package io.github.openbagtwo.lighterend.registries;

import com.google.common.collect.Maps;
import io.github.openbagtwo.lighterend.Items.ArmoredElytra;
import io.github.openbagtwo.lighterend.Items.LighterEndFoodComponents;
import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.tags.LighterEndTags;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class LighterEndItems {

  public static final Item AURORA_CRYSTAL_SHARD = register("aurora_crystal_shard");
  public static final Item LUMECORN_EAR = register("lumecorn_rod");
  public static final Item POPPED_LUMECORN = register("lumecorn_popped", new Settings().food(
      LighterEndFoodComponents.POPPED_LUMECORN_NUTRITION,
      LighterEndFoodComponents.POPPED_LUMECORN_EFFECT)
  );
  public static final Item SILK = register("silk_fiber");
  public static final Item SILK_MATRIX = register("silk_matrix");

  public static final Item UMBRELLA_JUICE = register("umbrella_juice", new Settings()
      .food(LighterEndFoodComponents.UMBRELLA_JUICE_NUTRITION,
          LighterEndFoodComponents.UMBRELLA_JUICE_EFFECT)
      .useRemainder(Items.GLASS_BOTTLE)
      .maxCount(16)
  );
  public static final Item END_FISH_BUCKET = register("bucket_end_fish",
      settings -> new EntityBucketItem(
          LighterEndMobs.END_FISH.mob, Fluids.WATER, SoundEvents.ITEM_BUCKET_EMPTY_FISH, settings),
      new Item.Settings().maxCount(1)
          .component(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT));
  public static final Item CUBOZOA_BUCKET = register("bucket_cubozoa",
      settings -> new EntityBucketItem(LighterEndMobs.CUBOZOA.mob, Fluids.WATER,
          SoundEvents.ITEM_BUCKET_EMPTY_FISH, settings),
      new Item.Settings().maxCount(1)
          .component(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT));
  public static final Item RAW_END_FISH = register("end_fish", new Settings()
      .food(FoodComponents.TROPICAL_FISH));

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
  );

  public static final Item SILK_HELMET = register("silk_helmet",
      (new Item.Settings()).armor(SILK_ARMOR, EquipmentType.HELMET));
  public static final Item SILK_CHESTPLATE = register("silk_chestplate",
      (new Item.Settings()).armor(SILK_ARMOR, EquipmentType.CHESTPLATE));
  public static final Item SILK_LEGGINGS = register("silk_leggings",
      (new Item.Settings()).armor(SILK_ARMOR, EquipmentType.LEGGINGS));
  public static final Item SILK_BOOTS = register("silk_boots",
      (new Item.Settings()).armor(SILK_ARMOR, EquipmentType.BOOTS));
  public static final Item SILK_ELYTRA = register(
      "silk_elytra",
      settings -> new ArmoredElytra(settings, SILK_ARMOR, 100, 0.95F, false),
      new Settings()
  );

  public static Item register(String name) {
    return register(name, new Settings());
  }

  public static Item register(String name, Settings settings) {
    return register(name, Item::new, settings);
  }

  public static Item register(String name, Function<Settings, Item> factory, Settings settings) {
    Identifier id = LighterEnd.of(name);
    RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
    return Registry.register(Registries.ITEM, key, factory.apply(settings.registryKey(key)));
  }

  public static void initialize() {
  }

}
