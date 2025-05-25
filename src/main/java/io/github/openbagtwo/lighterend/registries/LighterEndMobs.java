package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.mobs.SilkMoth;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class LighterEndMobs {

  public static final LighterEndMob<SilkMoth> SILK_MOTH = new LighterEndMob("silk_moth",
      EntityType.Builder.create(SilkMoth::new, SpawnGroup.CREATURE).dimensions(
          0.6F, 0.6F).eyeHeight(0.3F).maxTrackingRange(8));

  public static class LighterEndMob<T extends Entity> {

    public final EntityType<T> mob;
    public final Item spawnEgg;

    public LighterEndMob(String name, EntityType.Builder<T> settings) {
      mob = Registry.register(Registries.ENTITY_TYPE,
          Identifier.of(LighterEnd.MOD_ID, name),
          settings.build(
              RegistryKey.of(
                  RegistryKeys.ENTITY_TYPE, Identifier.of(
                      LighterEnd.MOD_ID, name))));
      spawnEgg = LighterEndItems.register(
          name + "_spawn_egg",
          (properties) -> new SpawnEggItem((EntityType<? extends MobEntity>) mob, properties),
          new Settings()
      );
    }
  }

  public static void initialize() {
    FabricDefaultAttributeRegistry.register(SILK_MOTH.mob, SilkMoth.createAttributes());
  }

}
