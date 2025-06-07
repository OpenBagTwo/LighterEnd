package io.github.openbagtwo.lighterend.mobs;

import io.github.openbagtwo.lighterend.registries.LighterEndData;
import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import net.minecraft.component.ComponentType;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Cubozoa extends SchoolingFishEntity {

  private static final TrackedData<Integer> VARIANT = DataTracker.registerData(
      Cubozoa.class,
      TrackedDataHandlerRegistry.INTEGER
  );

  public Cubozoa(EntityType<Cubozoa> entityType, World world) {
    super(entityType, world);
  }

  @Nullable
  @Override
  public EntityData initialize(
      ServerWorldAccess world,
      LocalDifficulty difficulty,
      SpawnReason spawnReason,
      @Nullable EntityData entityData
  ) {
    if (spawnReason == SpawnReason.BUCKET) {
      return entityData;
    }
    this.setVariant(0);

    // TODO: reenable once sulphur springs are back in the game
//    RegistryEntry<Biome> biome = world.getBiome(getBlockPos());
//    if (biome.matchesKey(EndBiomes.SULPHUR_SPRINGS.key)) {
//      this.dataTracker.set(VARIANT, 1);
//    }

    EntityData data = super.initialize(world, difficulty, spawnReason, entityData);

    this.calculateDimensions();
    return data;
  }

  @Override
  protected void initDataTracker(DataTracker.Builder builder) {
    super.initDataTracker(builder);
    builder.add(VARIANT, 0);
  }

  @Override
  public void writeCustomDataToNbt(NbtCompound nbt) {
    super.writeCustomDataToNbt(nbt);
    nbt.putInt("Variant", this.getVariant());
  }

  @Override
  public void readCustomDataFromNbt(NbtCompound nbt) {
    super.readCustomDataFromNbt(nbt);
    this.setVariant(nbt.getInt("Variant", 0));
  }

  @Override
  public void copyDataToStack(ItemStack stack) {
    super.copyDataToStack(stack);
    stack.copy(LighterEndData.VARIANT, this);
  }

  @Override
  protected void copyComponentsFrom(ComponentsAccess from) {
    this.copyComponentFrom(from, LighterEndData.VARIANT);
    super.copyComponentsFrom(from);
  }

  @Override
  public @NotNull ItemStack getBucketItem() {
    return new ItemStack(LighterEndItems.CUBOZOA_BUCKET);
  }

  public static DefaultAttributeContainer.Builder createAttributes() {
    return LivingEntity.createLivingAttributes()
        .add(EntityAttributes.MAX_HEALTH, 2.0)
        .add(EntityAttributes.FOLLOW_RANGE, 16.0)
        .add(EntityAttributes.MOVEMENT_SPEED, 0.5);
  }

  public int getVariant() {
    return this.dataTracker.get(VARIANT);
  }

  public void setVariant(int variant) {
    this.dataTracker.set(VARIANT, variant % 2);
  }

  @Override
  protected SoundEvent getFlopSound() {
    return LighterEndSounds.CUBOZOA_FLOP;
  }

  @Override
  public void onPlayerCollision(PlayerEntity player) {
    if (player instanceof ServerPlayerEntity serverPlayer
        && player.damage(
        serverPlayer.getServerWorld(),
        this.getDamageSources().mobAttack(this), 0.5F)
    ) {
      if (!this.isSilent()) {
        serverPlayer.networkHandler
            .sendPacket(
                new GameStateChangeS2CPacket(
                    GameStateChangeS2CPacket.PUFFERFISH_STING,
                    GameStateChangeS2CPacket.DEMO_OPEN_SCREEN
                )
            );
      }
      if (random.nextBoolean()) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 20, 0));
      }
    }
  }

  @Nullable
  @Override
  public <T> T get(ComponentType<? extends T> type) {
    return type == LighterEndData.VARIANT ?
        castComponentValue(type, new LighterEndData.Variant(this.getVariant())) : super.get(type);
  }

  @Override
  protected <T> boolean setApplicableComponent(ComponentType<T> type, T value) {
    if (type == LighterEndData.VARIANT) {
      this.setVariant(castComponentValue(LighterEndData.VARIANT, value).variant());
      return true;
    } else {
      return super.setApplicableComponent(type, value);
    }
  }
}
