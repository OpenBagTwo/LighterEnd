package io.github.openbagtwo.lighterend.mobs;

import io.github.openbagtwo.lighterend.registries.LighterEndBiomes;
import io.github.openbagtwo.lighterend.registries.LighterEndData;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.fish.AbstractSchoolingFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EndFish extends AbstractSchoolingFish {

  public static final int VARIANTS_NORMAL = 5;
  public static final int VARIANTS_SULPHUR = 3;
  public static final int VARIANTS = VARIANTS_NORMAL + VARIANTS_SULPHUR;
  private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(
      EndFish.class,
      EntityDataSerializers.INT
  );

  public EndFish(EntityType<EndFish> entityType, Level world) {
    super(entityType, world);
  }

  @Override
  public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor world,
      DifficultyInstance difficulty,
      EntitySpawnReason spawnReason,
      @Nullable SpawnGroupData entityData
  ) {
    if (spawnReason == EntitySpawnReason.BUCKET) {
      return entityData;
    }

    this.setVariant(random.nextInt(VARIANTS_NORMAL));

    Holder<Biome> biome = world.getBiome(blockPosition());
    if (biome.is(LighterEndBiomes.SULPHUR_SPRINGS)) {
      this.setVariant(random.nextInt(VARIANTS_SULPHUR) + VARIANTS_NORMAL);
    }

    SpawnGroupData data = super.finalizeSpawn(world, difficulty, spawnReason, entityData);

    this.refreshDimensions();
    return data;
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    super.defineSynchedData(builder);
    builder.define(VARIANT, 0);
  }

  @Override
  public void addAdditionalSaveData(ValueOutput view) {
    super.addAdditionalSaveData(view);
    view.putInt("Variant", this.getVariant());
  }

  @Override
  protected void readAdditionalSaveData(ValueInput view) {
    super.readAdditionalSaveData(view);
    this.setVariant(view.getIntOr("Variant", 0));
  }

  @Override
  public void saveToBucketTag(ItemStack stack) {
    super.saveToBucketTag(stack);
    stack.copyFrom(LighterEndData.VARIANT, this);
  }

  @Override
  protected void applyImplicitComponents(DataComponentGetter from) {
    this.applyImplicitComponentIfPresent(from, LighterEndData.VARIANT);
    super.applyImplicitComponents(from);
  }

  @Override
  public @NotNull ItemStack getBucketItemStack() {
    return new ItemStack(LighterEndItems.END_FISH_BUCKET);
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return LighterEndSounds.END_FISH_IDLE;
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
    if (random.nextInt(8) == 0 && getInBlockState().is(Blocks.WATER)) {
      double x = getX() + random.nextGaussian() * 0.2;
      double y = getY() + random.nextGaussian() * 0.2;
      double z = getZ() + random.nextGaussian() * 0.2;
      this.level().addParticle(ParticleTypes.BUBBLE, x, y, z, 0, 0, 0);
    }
  }

  public static AttributeSupplier.Builder createAttributes() {
    return LivingEntity.createLivingAttributes()
        .add(Attributes.MAX_HEALTH, 2.0)
        .add(Attributes.FOLLOW_RANGE, 16.0)
        .add(Attributes.MOVEMENT_SPEED, 0.75);
  }

  public int getVariant() {
    return this.entityData.get(VARIANT);
  }

  public void setVariant(int variant) {
    this.entityData.set(VARIANT, variant % VARIANTS);
  }

  @Nullable
  @Override
  public <T> T get(DataComponentType<? extends T> type) {
    return type == LighterEndData.VARIANT ?
        castComponentValue(type, new LighterEndData.Variant(this.getVariant())) : super.get(type);
  }

  @Override
  protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
    if (type == LighterEndData.VARIANT) {
      this.setVariant(castComponentValue(LighterEndData.VARIANT, value).variant());
      return true;
    } else {
      return super.applyImplicitComponent(type, value);
    }
  }
}
