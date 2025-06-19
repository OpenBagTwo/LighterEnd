package io.github.openbagtwo.lighterend.mobs;

import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class EndFish extends SchoolingFishEntity {

  public static final int VARIANTS_NORMAL = 5;
  public static final int VARIANTS_SULPHUR = 3;
  public static final int VARIANTS = VARIANTS_NORMAL + VARIANTS_SULPHUR;
  private static final TrackedData<Integer> VARIANT = DataTracker.registerData(
      EndFish.class,
      TrackedDataHandlerRegistry.INTEGER
  );
  private static final TrackedData<Boolean> FROM_BUCKET = DataTracker.registerData(
      EndFish.class,
      TrackedDataHandlerRegistry.BOOLEAN
  );

  public EndFish(EntityType<EndFish> entityType, World world) {
    super(entityType, world);
  }

  @Override
  public EntityData initialize(
      ServerWorldAccess world,
      LocalDifficulty difficulty,
      SpawnReason spawnReason,
      EntityData entityData
  ) {
    EntityData data = super.initialize(world, difficulty, spawnReason, entityData);

    // TODO: reenable once sulphur springs are back in the game
//    RegistryEntry<Biome> biome = world.getBiome(getBlockPos());
//    if (biome.matchesKey(EndBiomes.SULPHUR_SPRINGS.key)) {
//      this.dataTracker.set(VARIANT, (random.nextInt(VARIANTS_SULPHUR) + VARIANTS_NORMAL));
//    }

    this.calculateDimensions();
    return data;
  }

  @Override
  protected void initDataTracker(DataTracker.Builder builder) {
    super.initDataTracker(builder);
    builder.add(VARIANT, this.getRandom().nextInt(VARIANTS_NORMAL));
    builder.add(FROM_BUCKET, false);
  }

  @Override
  public void writeCustomData(WriteView view) {
    super.writeCustomData(view);
    view.putInt("Variant", getVariant());
    view.putBoolean("FromBucket", this.isFromBucket());
  }

  @Override
  protected void readCustomData(ReadView view) {
    super.readCustomData(view);
    if (view.getInt("Variant", -1) != -1) {
      this.dataTracker.set(VARIANT, view.getInt("Variant", -1));
    }
    this.setFromBucket(view.getBoolean("FromBucket", false));
  }

  @Override
  public void copyDataToStack(ItemStack itemStack) {
    super.copyDataToStack(itemStack);
    NbtComponent.set(DataComponentTypes.BUCKET_ENTITY_DATA, itemStack, (tag) -> {
      tag.putInt("variant", dataTracker.get(VARIANT));
    });
  }

  @Override
  public @NotNull ItemStack getBucketItem() {
    return new ItemStack(LighterEndItems.END_FISH_BUCKET);
  }

  @Override
  protected @NotNull SoundEvent getFlopSound() {
    return LighterEndSounds.END_FISH_FLOP;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return LighterEndSounds.END_FISH_DEATH;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource source) {
    return LighterEndSounds.END_FISH_HURT;
  }

  @Override
  public void tick() {
    super.tick();
    if (random.nextInt(8) == 0 && getBlockStateAtPos().isOf(Blocks.WATER)) {
      double x = getX() + random.nextGaussian() * 0.2;
      double y = getY() + random.nextGaussian() * 0.2;
      double z = getZ() + random.nextGaussian() * 0.2;
      getWorld().addParticleClient(ParticleTypes.BUBBLE, x, y, z, 0, 0, 0);
    }
  }

  public static DefaultAttributeContainer.Builder createAttributes() {
    return LivingEntity.createLivingAttributes()
        .add(EntityAttributes.MAX_HEALTH, 2.0)
        .add(EntityAttributes.FOLLOW_RANGE, 16.0)
        .add(EntityAttributes.MOVEMENT_SPEED, 0.75);
  }

  public int getVariant() {
    return this.dataTracker.get(VARIANT);
  }
}
