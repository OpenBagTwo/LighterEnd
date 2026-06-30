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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.cow.AbstractCow;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.entity.monster.cubemob.Slime;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

public class LighterEndMobs {

  public static final LighterEndMob<SilkMoth> SILK_MOTH = new LighterEndMob<>("silk_moth",
      EntityType.Builder.of(SilkMoth::new, MobCategory.CREATURE).sized(
          0.6F, 0.6F).eyeHeight(0.3F).clientTrackingRange(8));

  public static final LighterEndMob<Dragonfly> DRAGONFLY = new LighterEndMob<>("dragonfly",
      EntityType.Builder.of(Dragonfly::new, MobCategory.AMBIENT).sized(
          0.6F, 0.5F).eyeHeight(0.25F).clientTrackingRange(8));

  public static final LighterEndMob<EndFish> END_FISH = new LighterEndMob<>("end_fish",
      EntityType.Builder.of(EndFish::new, MobCategory.WATER_AMBIENT).sized(
          0.5F, 0.5F).eyeHeight(0.25F).clientTrackingRange(4));

  public static final LighterEndMob<Cubozoa> CUBOZOA = new LighterEndMob<>("cubozoa",
      EntityType.Builder.of(Cubozoa::new, MobCategory.WATER_CREATURE).sized(
          0.6F, 1.0F).eyeHeight(0.5F).clientTrackingRange(4));

  public static final LighterEndMob<EndSlime> END_SLIME = new LighterEndMob<>(
      "end_slime",
      EntityType.Builder.of(EndSlime::new, MobCategory.MONSTER).sized(0.5F, 0.5F)
          .eyeHeight(0.325F).spawnDimensionsScale(4.0F).clientTrackingRange(10)
  );

  public static final LighterEndMob<GlossyMooshroom> MOOSHROOM = new LighterEndMob<>(
      "glossy_mooshroom",
      EntityType.Builder.of(GlossyMooshroom::new, MobCategory.CREATURE)
          .sized(0.9F, 1.4F)
          .eyeHeight(1.3F)
          .passengerAttachments(1.36875F)
          .clientTrackingRange(10)
  );

  public static final LighterEndMob<ChorusCrab> CHORUS_CRAB = new LighterEndMob<>(
      "chorus_crab",
      EntityType.Builder.of(ChorusCrab::new, MobCategory.CREATURE)
          .sized(2.0F, 1.2F)
          .eyeHeight(1.1F)
          .passengerAttachments(new Vec3(0, 0.9F, -0.5F))
          .clientTrackingRange(4)
  );

  public static class LighterEndMob<T extends Entity> {

    public final ResourceKey<EntityType<?>> id;
    public final EntityType<T> mob;
    public final Item spawnEgg;

    public LighterEndMob(String name, EntityType.Builder<T> settings) {
      id = ResourceKey.create(Registries.ENTITY_TYPE, LighterEnd.of(name));
      mob = Registry.register(BuiltInRegistries.ENTITY_TYPE,
          id,
          settings.build(id)
      );
      spawnEgg = LighterEndItems.register(
          name + "_spawn_egg",
          (properties) -> new SpawnEggItem(properties.spawnEgg(mob)),
          new Properties()
      );
    }
  }

  public static void initialize() {
    FabricDefaultAttributeRegistry.register(SILK_MOTH.mob, SilkMoth.createAttributes());
    FabricDefaultAttributeRegistry.register(DRAGONFLY.mob, Dragonfly.createAttributes());
    FabricDefaultAttributeRegistry.register(END_FISH.mob, EndFish.createAttributes());
    FabricDefaultAttributeRegistry.register(CUBOZOA.mob, Cubozoa.createAttributes());
    FabricDefaultAttributeRegistry.register(END_SLIME.mob, EndSlime.createAttributes());
    FabricDefaultAttributeRegistry.register(MOOSHROOM.mob, AbstractCow.createAttributes());
    FabricDefaultAttributeRegistry.register(CHORUS_CRAB.mob, ChorusCrab.createCrabAttributes());

    SpawnPlacements.register(
        DRAGONFLY.mob,
        SpawnPlacementTypes.NO_RESTRICTIONS,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        LighterEndMobs::canDragonflySpawn
    );
    SpawnPlacements.register(
        END_FISH.mob,
        SpawnPlacementTypes.IN_WATER,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        LighterEndMobs::canAquaticMobSpawn
    );
    SpawnPlacements.register(
        CUBOZOA.mob,
        SpawnPlacementTypes.IN_WATER,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        LighterEndMobs::canAquaticMobSpawn
    );
    SpawnPlacements.register(
        END_SLIME.mob,
        SpawnPlacementTypes.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        LighterEndMobs::canSlimeSpawn
    );
    SpawnPlacements.register(
        MOOSHROOM.mob,
        SpawnPlacementTypes.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        LighterEndMobs::canPassiveSpawn
    );
    SpawnPlacements.register(
        CHORUS_CRAB.mob,
        SpawnPlacementTypes.ON_GROUND,
        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        LighterEndMobs::canCrabSpawn
    );
  }

  public static boolean canAquaticMobSpawn(EntityType<? extends WaterAnimal> type,
      LevelAccessor world, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
    return world.getFluidState(pos.below()).is(FluidTags.WATER)
        && world.getBlockState(pos.above()).is(Blocks.WATER);
  }

  public static boolean canSlimeSpawn(
      EntityType<? extends Slime> type,
      LevelAccessor world, EntitySpawnReason reason, BlockPos pos, RandomSource random
  ) {
    if (!world.getBlockState(pos.below()).is(LighterEndTags.SLIME_SPAWNABLE)) {
      return false;
    }
    return random.nextInt(4) == 0;
  }

  public static boolean canDragonflySpawn(
      EntityType<? extends Entity> type,
      LevelAccessor world, EntitySpawnReason reason, BlockPos pos, RandomSource random
  ) {
    return random.nextInt(32) == 0;
  }

  public static boolean canCrabSpawn(
      EntityType<? extends Entity> type,
      LevelAccessor world, EntitySpawnReason reason, BlockPos pos, RandomSource random
  ) {
    //TODO: check for nearby water
    return random.nextInt(16) == 0;
  }

  public static boolean canPassiveSpawn(
      EntityType<? extends Entity> type,
      LevelAccessor world, EntitySpawnReason reason, BlockPos pos, RandomSource random
  ) {
    return true;
  }

}
