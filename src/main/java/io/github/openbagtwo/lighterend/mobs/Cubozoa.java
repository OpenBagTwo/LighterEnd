package io.github.openbagtwo.lighterend.mobs;

import io.github.openbagtwo.lighterend.registries.LighterEndBiomes;
import io.github.openbagtwo.lighterend.registries.LighterEndData;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.fish.AbstractSchoolingFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Cubozoa extends AbstractSchoolingFish {

  private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(
      Cubozoa.class,
      EntityDataSerializers.INT
  );

  public Cubozoa(EntityType<Cubozoa> entityType, Level world) {
    super(entityType, world);
  }

  @Nullable
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
    this.setVariant(0);

    Holder<Biome> biome = world.getBiome(blockPosition());
    if (biome.is(LighterEndBiomes.SULPHUR_SPRINGS)) {
      this.entityData.set(VARIANT, 1);
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
    return new ItemStack(LighterEndItems.CUBOZOA_BUCKET);
  }

  public static AttributeSupplier.Builder createAttributes() {
    return LivingEntity.createLivingAttributes()
        .add(Attributes.MAX_HEALTH, 2.0)
        .add(Attributes.FOLLOW_RANGE, 16.0)
        .add(Attributes.MOVEMENT_SPEED, 0.5);
  }

  public int getVariant() {
    return this.entityData.get(VARIANT);
  }

  public void setVariant(int variant) {
    this.entityData.set(VARIANT, variant % 2);
  }

  @Override
  protected SoundEvent getFlopSound() {
    return LighterEndSounds.CUBOZOA_FLOP;
  }

  @Override
  public SoundEvent getAmbientSound() {
    return LighterEndSounds.CUBOZOA_IDLE;
  }

  @Nullable
  protected SoundEvent getHurtSound(DamageSource source) {
    return LighterEndSounds.CUBOZOA_HURT;
  }

  @Nullable
  protected SoundEvent getDeathSound() {
    return LighterEndSounds.CUBOZOA_DEATH;
  }

  @Override
  public void playerTouch(Player player) {
    if (player instanceof ServerPlayer serverPlayer
        && player.hurtServer(
        serverPlayer.level(),
        this.damageSources().mobAttack(this), 0.5F)
    ) {
      if (!this.isSilent()) {
        serverPlayer.connection
            .send(
                new ClientboundGameEventPacket(
                    ClientboundGameEventPacket.PUFFER_FISH_STING,
                    ClientboundGameEventPacket.DEMO_PARAM_INTRO
                )
            );
      }
      if (random.nextBoolean()) {
        player.addEffect(new MobEffectInstance(MobEffects.POISON, 20, 0));
      }
    }
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
