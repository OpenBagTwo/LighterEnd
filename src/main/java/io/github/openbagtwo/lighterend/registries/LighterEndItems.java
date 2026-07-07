package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.items.LighterEndFoodComponents;
import io.github.openbagtwo.lighterend.items.Matchstick;
import io.github.openbagtwo.lighterend.items.TPTotem;
import io.github.openbagtwo.lighterend.registries.LighterEndData.SilkLevelComponent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.material.Fluids;

public class LighterEndItems {

  public static Map<Item, ResourceKey<Item>> idLookup = new HashMap<>();

  public static final Item AURORA_CRYSTAL_SHARD = register(
      "aurora_crystal_shard",
      new Properties().trimMaterial(LighterEndTrimming.AURORA)
  );
  public static final Item LUMECORN_EAR = register("lumecorn_rod");
  public static final Item POPPED_LUMECORN = register(
      "lumecorn_popped",
      new Properties().food(
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
      new Properties()
  );

  public static final Item UMBRELLA_JUICE = register(
      "umbrella_juice",
      new Properties()
          .food(LighterEndFoodComponents.UMBRELLA_JUICE_NUTRITION,
              LighterEndFoodComponents.UMBRELLA_JUICE_EFFECT)
          .usingConvertsTo(Items.GLASS_BOTTLE)
          .craftRemainder(Items.GLASS_BOTTLE)
          .stacksTo(16)
  );
  public static final Item END_FISH_BUCKET = register("bucket_end_fish",
      settings -> new MobBucketItem(
          LighterEndMobs.END_FISH.mob, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, settings),
      new Item.Properties().stacksTo(1)
          .component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY));
  public static final Item CUBOZOA_BUCKET = register("bucket_cubozoa",
      settings -> new MobBucketItem(LighterEndMobs.CUBOZOA.mob, Fluids.WATER,
          SoundEvents.BUCKET_EMPTY_FISH, settings),
      new Item.Properties().stacksTo(1)
          .component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY));
  public static final Item RAW_END_FISH = register("end_fish", new Properties()
      .food(Foods.TROPICAL_FISH));
  public static final Item GLOW_BARB = register("glow_barb");

  public static final Item END_LILY_LEAF = register("end_lily_leaf");
  public static final Item DRIED_END_LILY_LEAF = register("end_lily_leaf_dried");

  public static final Item END_CREAM = register("end_cream");
  public static final Item END_POWDER = register("end_powder");

  public static final Item CRAB_CLAW = register("crab_claw");
  public static final Item CRAB_MEAT = register(
      "crab_meat",
      new Properties().food(
          LighterEndFoodComponents.CRAB_MEAT,
          LighterEndFoodComponents.RAW_CRAB_MEAT_EFFECT
      )
  );
  public static final Item CRAB_CAKE = register(
      "crab_cake",
      new Properties().food(LighterEndFoodComponents.CRAB_CAKE)
  );

  public static final Item TOTEM_OF_TELEPORTATION = register(
      "totem_of_teleportation",
      TPTotem::new,
      new Properties()
  );

  public static final Item CRYSTALLINE_SULPHUR = register("sulphur_crystalline");

  public static final Item MATCHSTICK = register("matchstick", Matchstick::new, new Properties());

  public static final Item SHADOW_BERRY_SEEDS = register(
      "shadow_berry_seeds",
      settings -> new BlockItem(
          LighterEndBlocks.SHADOW_BERRY,
          settings.useItemDescriptionPrefix()
      ),
      new Properties()
  );
  public static final Item SHADOW_BERRY = register(
      "shadow_berry",
      new Properties().food(LighterEndFoodComponents.SHADOW_BERRY)
  );
  public static final Item SHADOW_BERRY_COOKED = register(
      "shadow_berry_cooked",
      new Properties().food(LighterEndFoodComponents.SHADOW_BERRY_COOKED)
  );
  public static final Item SHADOW_BERRY_JAM = register(
      "shadow_berry_jam",
      new Properties().food(
              LighterEndFoodComponents.SHADOW_BERRY_JAM,
              LighterEndFoodComponents.SHADOW_BERRY_JAM_EFFECT
          ).usingConvertsTo(Items.GLASS_BOTTLE)
          .craftRemainder(Items.GLASS_BOTTLE)
          .stacksTo(16)
  );

  public static Item register(String name) {
    return register(name, new Properties());
  }

  public static Item register(String name, Properties settings) {
    return register(name, Item::new, settings);
  }

  public static Item register(
      String name,
      Function<Properties, Item> factory,
      Properties settings
  ) {
    ResourceKey<Item> id = ResourceKey.create(Registries.ITEM, LighterEnd.of(name));
    Item item = factory.apply(settings.setId(id));
    if (item instanceof BlockItem blockItem) {
      blockItem.registerBlocks(Item.BY_BLOCK, item);
    }
    return register(id, item);
  }

  public static Item register(ResourceKey<Item> id, Item item) {
    idLookup.put(item, id);
    return Registry.register(BuiltInRegistries.ITEM, id, item);
  }

  public static void initialize() {
  }

}
