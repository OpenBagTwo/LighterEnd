package io.github.openbagtwo.lighterend.mobs;

import io.github.openbagtwo.lighterend.registries.LighterEndItems;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
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
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
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
  private static final TrackedData<Integer> SCALE = DataTracker.registerData(
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
    EntityData data = super.initialize(world, difficulty, spawnReason, entityData);

    // TODO: reenable once sulphur springs are back in the game
//    RegistryEntry<Biome> biome = world.getBiome(getBlockPos());
//    if (biome.matchesKey(EndBiomes.SULPHUR_SPRINGS.key)) {
//      this.dataTracker.set(VARIANT, 1);
//    }

    this.calculateDimensions();
    return data;
  }

  @Override
  protected void initDataTracker(DataTracker.Builder builder) {
    super.initDataTracker(builder);
    builder.add(VARIANT, 0);
    builder.add(SCALE, this.getRandom().nextInt(16));
  }

  @Override
  public void writeCustomData(WriteView view) {
    super.writeCustomData(view);
    view.putInt("Variant", getVariant());
    view.putInt("Scale", this.dataTracker.get(SCALE));
  }

  @Override
  protected void readCustomData(ReadView view) {
    super.readCustomData(view);
    if (view.getInt("Variant", -1) != -1) {
      this.dataTracker.set(VARIANT, view.getInt("Variant", -1));
    }
    if (view.getInt("Scale", -1) != -1) {
      this.dataTracker.set(SCALE, view.getInt("Scale", -1));
    }
  }

  @Override
  public void copyDataToStack(ItemStack itemStack) {
    super.copyDataToStack(itemStack);
    NbtComponent.set(DataComponentTypes.BUCKET_ENTITY_DATA, itemStack, (tag) -> {
      tag.putInt("Variant", dataTracker.get(VARIANT));
      tag.putInt("Scale", dataTracker.get(SCALE));
    });
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
    return (int) this.dataTracker.get(VARIANT);
  }

  @Override
  protected SoundEvent getFlopSound() {
    return LighterEndSounds.CUBOZOA_FLOP;
  }

  @Override
  public void onPlayerCollision(PlayerEntity player) {
    if (player instanceof ServerPlayerEntity serverPlayer
        && player.damage(
        serverPlayer.getWorld(),
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
}
