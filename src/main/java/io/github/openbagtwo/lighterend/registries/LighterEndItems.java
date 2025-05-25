package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.Items.FoodComponents;
import io.github.openbagtwo.lighterend.LighterEnd;
import java.util.function.Function;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class LighterEndItems {

  public static final Item AURORA_CRYSTAL_SHARD = register("aurora_crystal_shard");
  public static final Item LUMECORN_EAR = register("lumecorn_rod");
  public static final Item POPPED_LUMECORN = register("lumecorn_popped", new Settings().food(
      FoodComponents.POPPED_LUMECORN_NUTRITION, FoodComponents.POPPED_LUMECORN_EFFECT)
  );
  public static final Item SILK = register("silk_fiber");
  public static final Item SILK_MATRIX = register("silk_matrix");

  public static final Item UMBRELLA_JUICE = register("umbrella_juice", new Settings()
      .food(FoodComponents.UMBRELLA_JUICE_NUTRITION, FoodComponents.UMBRELLA_JUICE_EFFECT)
      .useRemainder(Items.GLASS_BOTTLE)
      .maxCount(16)
  );

  public static Item register(String name) {
    return register(name, new Settings());
  }

  public static Item register(String name, Settings settings) {
    return register(name, Item::new, settings);
  }

  public static Item register(String name, Function<Settings, Item> factory, Settings settings) {
    Identifier id = Identifier.of(LighterEnd.MOD_ID, name);
    RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
    return Registry.register(Registries.ITEM, key, factory.apply(settings.registryKey(key)));
  }

  public static void initialize() {
  }

}
