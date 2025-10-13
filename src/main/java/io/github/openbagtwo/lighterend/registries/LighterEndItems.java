package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.items.Fur;
import io.github.openbagtwo.lighterend.items.LighterEndFoodComponents;
import io.github.openbagtwo.lighterend.items.Matchstick;
import io.github.openbagtwo.lighterend.items.TPTotem;
import io.github.openbagtwo.lighterend.registries.LighterEndData.SilkLevelComponent;
import java.util.function.Function;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.Items;
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
  public static Item SILK_MOTH_NEST = register(
      "silk_moth_nest",
      settings -> new BlockItem(
          LighterEndBlocks.SILK_MOTH_NEST,
          settings
              .component(LighterEndData.MOTHS, LighterEndData.MothsComponent.DEFAULT)
              .component(LighterEndData.SILK_LEVEL, new SilkLevelComponent(0))
      ),
      new Settings()
  );

  public static final Item UMBRELLA_JUICE = register(
      "umbrella_juice",
      new Settings()
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
  public static final Item GLOW_BARB = register("glow_barb");

  public static final Item END_LILY_LEAF = register("end_lily_leaf");
  public static final Item DRIED_END_LILY_LEAF = register("end_lily_leaf_dried");

  public static final Item GLOWSHROOM_FUR = register(
      "mossy_glowshroom_fur",
      settings -> new Fur(LighterEndBlocks.GLOWSHROOM_FUR, settings),
      new Settings()
  );
  public static final Item AGAVE_FUR = register(
      "blue_vine_fur",
      settings -> new Fur(LighterEndBlocks.AGAVE_FUR, settings),
      new Settings()
  );

  public static final Item END_CREAM = register("end_cream");
  public static final Item END_POWDER = register("end_powder");

  public static final Item CRAB_CLAW = register("crab_claw");
  public static final Item CRAB_MEAT = register(
      "crab_meat",
      new Settings().food(
          LighterEndFoodComponents.CRAB_MEAT,
          LighterEndFoodComponents.RAW_CRAB_MEAT_EFFECT
      )
  );
  public static final Item CRAB_CAKE = register(
      "crab_cake",
      new Settings().food(LighterEndFoodComponents.CRAB_CAKE)
  );

  public static final Item TOTEM_OF_TELEPORTATION = register(
      "totem_of_teleportation",
      TPTotem::new,
      new Settings()
  );

  public static final Item CRYSTALLINE_SULPHUR = register("sulphur_crystalline");

  public static final Item MATCHSTICK = register("matchstick", Matchstick::new, new Settings());

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
