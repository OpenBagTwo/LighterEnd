package io.github.openbagtwo.lighterend.registries;

import io.github.openbagtwo.lighterend.LighterEnd;
import io.github.openbagtwo.lighterend.mobs.ChorusCrab;
import io.github.openbagtwo.lighterend.mobs.Cubozoa;
import io.github.openbagtwo.lighterend.mobs.Dragonfly;
import io.github.openbagtwo.lighterend.mobs.EndFish;
import io.github.openbagtwo.lighterend.mobs.EndSlime;
import io.github.openbagtwo.lighterend.mobs.GlossyMooshroom;
import io.github.openbagtwo.lighterend.mobs.SilkMoth;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.AbstractCowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;

public class LighterEndMobs {

  public static final LighterEndMob<SilkMoth> SILK_MOTH = new LighterEndMob<>("silk_moth",
      EntityType.Builder.create(SilkMoth::new, SpawnGroup.CREATURE).dimensions(
          0.6F, 0.6F).eyeHeight(0.3F).maxTrackingRange(8));

  public static final LighterEndMob<Dragonfly> DRAGONFLY = new LighterEndMob<>("dragonfly",
      EntityType.Builder.create(Dragonfly::new, SpawnGroup.AMBIENT).dimensions(
          0.6F, 0.5F).eyeHeight(0.25F).maxTrackingRange(8));

  public static final LighterEndMob<EndFish> END_FISH = new LighterEndMob<>("end_fish",
      EntityType.Builder.create(EndFish::new, SpawnGroup.WATER_AMBIENT).dimensions(
          0.5F, 0.5F).eyeHeight(0.25F).maxTrackingRange(4));

  public static final LighterEndMob<Cubozoa> CUBOZOA = new LighterEndMob<>("cubozoa",
      EntityType.Builder.create(Cubozoa::new, SpawnGroup.WATER_AMBIENT).dimensions(
          0.6F, 1.0F).eyeHeight(0.5F).maxTrackingRange(4));

  public static final LighterEndMob<EndSlime> END_SLIME = new LighterEndMob<>(
      "end_slime",
      EntityType.Builder.create(EndSlime::new, SpawnGroup.MONSTER).dimensions(0.5F, 0.5F)
          .eyeHeight(0.325F).spawnBoxScale(4.0F).maxTrackingRange(10)
  );

  public static final LighterEndMob<GlossyMooshroom> MOOSHROOM = new LighterEndMob<>(
      "glossy_mooshroom",
      EntityType.Builder.create(GlossyMooshroom::new, SpawnGroup.CREATURE)
          .dimensions(0.9F, 1.4F)
          .eyeHeight(1.3F)
          .passengerAttachments(1.36875F)
          .maxTrackingRange(10)
  );

  public static final LighterEndMob<ChorusCrab> CHORUS_CRAB = new LighterEndMob<>(
      "chorus_crab",
      EntityType.Builder.create(ChorusCrab::new, SpawnGroup.CREATURE)
          .dimensions(2.8F, 0.75F)
          .eyeHeight(0.75F)
          .passengerAttachments(0.75F)
          .maxTrackingRange(4)
  );

  public static class LighterEndMob<T extends Entity> {

    public final EntityType<T> mob;
    public final Item spawnEgg;

    public LighterEndMob(String name, EntityType.Builder<T> settings) {
      mob = Registry.register(Registries.ENTITY_TYPE,
          LighterEnd.of(name),
          settings.build(
              RegistryKey.of(
                  RegistryKeys.ENTITY_TYPE, LighterEnd.of(name))));
      spawnEgg = LighterEndItems.register(
          name + "_spawn_egg",
          (properties) -> new SpawnEggItem((EntityType<? extends MobEntity>) mob, properties),
          new Settings()
      );
    }
  }

  public static void initialize() {
    FabricDefaultAttributeRegistry.register(SILK_MOTH.mob, SilkMoth.createAttributes());
    FabricDefaultAttributeRegistry.register(DRAGONFLY.mob, Dragonfly.createAttributes());
    FabricDefaultAttributeRegistry.register(END_FISH.mob, EndFish.createAttributes());
    FabricDefaultAttributeRegistry.register(CUBOZOA.mob, Cubozoa.createAttributes());
    FabricDefaultAttributeRegistry.register(END_SLIME.mob, EndSlime.createAttributes());
    FabricDefaultAttributeRegistry.register(MOOSHROOM.mob, AbstractCowEntity.createCowAttributes());
    FabricDefaultAttributeRegistry.register(CHORUS_CRAB.mob, ChorusCrab.createCrabAttributes());

    SpawnRestriction.register(
        DRAGONFLY.mob,
        SpawnLocationTypes.UNRESTRICTED,
        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
        LighterEndMobs::canDragonflySpawn
    );
    SpawnRestriction.register(
        END_FISH.mob,
        SpawnLocationTypes.IN_WATER,
        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
        LighterEndMobs::canAquaticMobSpawn
    );
    SpawnRestriction.register(
        CUBOZOA.mob,
        SpawnLocationTypes.IN_WATER,
        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
        LighterEndMobs::canAquaticMobSpawn
    );
    SpawnRestriction.register(
        END_SLIME.mob,
        SpawnLocationTypes.ON_GROUND,
        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
        LighterEndMobs::canSlimeSpawn
    );
    SpawnRestriction.register(
        MOOSHROOM.mob,
        SpawnLocationTypes.ON_GROUND,
        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
        LighterEndMobs::canPassiveSpawn
    );
    SpawnRestriction.register(
        CHORUS_CRAB.mob,
        SpawnLocationTypes.ON_GROUND,
        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
        LighterEndMobs::canCrabSpawn
    );
  }

  public static boolean canAquaticMobSpawn(EntityType<? extends WaterCreatureEntity> type,
      WorldAccess world, SpawnReason reason, BlockPos pos, Random random) {
    return world.getFluidState(pos.down()).isIn(FluidTags.WATER)
        && world.getBlockState(pos.up()).isOf(Blocks.WATER);
  }

  public static boolean canSlimeSpawn(
      EntityType<? extends SlimeEntity> type,
      WorldAccess world, SpawnReason reason, BlockPos pos, Random random
  ) {
    if (!world.getBlockState(pos.down()).isIn(LighterEndTags.SLIME_SPAWNABLE)) {
      return false;
    }
    return random.nextInt(4) == 0;
  }

  public static boolean canDragonflySpawn(
      EntityType<? extends Entity> type,
      WorldAccess world, SpawnReason reason, BlockPos pos, Random random
  ) {
    return random.nextInt(32) == 0;
  }

  public static boolean canCrabSpawn(
      EntityType<? extends Entity> type,
      WorldAccess world, SpawnReason reason, BlockPos pos, Random random
  ) {
    //TODO: check for nearby water
    return random.nextInt(16) == 0;
  }

  public static boolean canPassiveSpawn(
      EntityType<? extends Entity> type,
      WorldAccess world, SpawnReason reason, BlockPos pos, Random random
  ) {
    return true;
  }

}
