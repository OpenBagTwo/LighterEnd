package io.github.openbagtwo.lighterend.mobs;

import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enderman;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ChorusCrab extends Animal {

  private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(
      ChorusCrab.class,
      EntityDataSerializers.BYTE
  );

  public ChorusCrab(EntityType<? extends ChorusCrab> entityType, Level world) {
    super(entityType, world);
  }

  public static AttributeSupplier.Builder createCrabAttributes() {
    return Monster.createMonsterAttributes()
        .add(Attributes.MAX_HEALTH, 32.0)
        .add(Attributes.MOVEMENT_SPEED, 0.1)
        .add(Attributes.JUMP_STRENGTH, 0)
        .add(Attributes.STEP_HEIGHT, 2.0)
        .add(Attributes.TEMPT_RANGE, 8.0)
        .add(Attributes.ATTACK_DAMAGE, 2.0)
        .add(Attributes.ATTACK_KNOCKBACK, 3.0)
        .add(Attributes.ATTACK_SPEED, 0.1)
        .add(Attributes.ARMOR, 8.0)
        .add(Attributes.ENTITY_INTERACTION_RANGE, 1.5)
        .add(Attributes.BLOCK_INTERACTION_RANGE, 1.5)
        .add(Attributes.KNOCKBACK_RESISTANCE, 5.0);
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(1, new FloatGoal(this));
    this.goalSelector.addGoal(1, new HurtByTargetGoal(this));
    this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 0.5, false));
    this.goalSelector.addGoal(3, new MateGoal(this));
    this.goalSelector.addGoal(
        4,
        new TemptGoal(this, 0.8F, Ingredient.of(Items.CHORUS_FLOWER), true)
    );
    this.goalSelector.addGoal(
        5,
        new AvoidEntityGoal(this, Player.class, 3.0F, 0.25F, 1F, (entity) -> true)
    );
    this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.8));
    this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
    this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
  }

  @Override
  public SoundEvent getAmbientSound() {
    return LighterEndSounds.CRAB_IDLE;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource source) {
    return LighterEndSounds.CRAB_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return LighterEndSounds.CRAB_DEATH;
  }

  @Override
  protected void playStepSound(BlockPos pos, BlockState state) {
    this.playSound(LighterEndSounds.CRAB_STEP, 0.15F, 1.0F);
  }

  @Override
  public boolean canBeAffected(MobEffectInstance effect) {
    if (effect.is(MobEffects.POISON)) {
      return !this.is(EntityTypeTags.IGNORES_POISON_AND_REGEN);
    }
    return super.canBeAffected(effect);
  }

  @Nullable
  @Override
  public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor world,
      DifficultyInstance difficulty,
      EntitySpawnReason spawnReason,
      @Nullable SpawnGroupData entityData
  ) {
    entityData = super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    if (spawnReason == EntitySpawnReason.BREEDING) {
      this.setPersistenceRequired();
    } else if (world.getRandom().nextInt(512) == 0) {
      Enderman rider = EntityTypes.ENDERMAN.create(this.level(), EntitySpawnReason.JOCKEY);
      if (rider != null) {
        rider.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
        rider.finalizeSpawn(world, difficulty, spawnReason, null);
        rider.startRiding(this);
      }
    }
    this.setCanPickUpLoot(true);
    this.populateDefaultEquipmentSlots(world.getRandom(), difficulty);
    this.enchantSpawnedWeapon(world, world.getRandom(), difficulty);

    return entityData;
  }

  @Override
  protected void populateDefaultEquipmentSlots(RandomSource random,
      DifficultyInstance localDifficulty) {
    if (random.nextInt(512) == 0) {
      this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
      this.setGuaranteedDrop(EquipmentSlot.MAINHAND);

    }
  }

  @Override
  public boolean canHoldItem(ItemStack stack) {
    return stack.is(ItemTags.SWORDS);
  }

  @Override
  public @Nullable AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
    return LighterEndMobs.CHORUS_CRAB.mob.create(world, EntitySpawnReason.BREEDING);
  }

  @Override
  public float getWalkTargetValue(BlockPos pos, LevelReader world) {
    return world.getBlockState(pos.below()).is(BlockTags.SAND) ? 12.0F
        : world.getBlockState(pos.below()).is(LighterEndTags.END_SOIL) ? 10.0F
            : world.getPathfindingCostFromLightLevels(pos);
  }

  @Override
  public boolean isFood(ItemStack stack) {
    return stack.is(Items.CHORUS_FLOWER);
  }

  @Override
  protected PathNavigation createNavigation(Level world) {
    return new WallClimberNavigation(this, world);
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    super.defineSynchedData(builder);
    builder.define(CLIMBING, (byte) 0);
  }

  @Override
  public void tick() {
    super.tick();
    if (!this.level().isClientSide()) {
      this.setClimbingWall(this.horizontalCollision);
    }
    if (this.getAge() >= 0 && this.isPassenger()) {
      this.removeVehicle();
    }
  }

  @Override
  public boolean canMate(Animal other) {
    if (this.isVehicle() || other.isVehicle()) {
      return false;
    }
    return super.canMate(other);
  }

  @Override
  public void finalizeSpawnChildFromBreeding(ServerLevel world, Animal other,
      @Nullable AgeableMob baby) {
    super.finalizeSpawnChildFromBreeding(world, other, baby);
    if (baby != null) {
      baby.startRiding(this, true, true);
    }
  }

  public void setClimbingWall(boolean climbing) {
    byte b = this.entityData.get(CLIMBING);
    if (climbing) {
      b = (byte) (b | 1);
    } else {
      b = (byte) (b & -2);
    }

    this.entityData.set(CLIMBING, b);
  }

  @Override
  public boolean removeWhenFarAway(double d) {
    return !this.hasCustomName() && !this.isVehicle();
  }

  @Override
  public void setInLove(@Nullable Player player) {
    this.setPersistenceRequired();
    super.setInLove(player);
  }

  class MateGoal extends BreedGoal {

    public MateGoal(Animal crab) {
      super(crab, 1.0F);
    }

    @Override
    public boolean canUse() {
      if (this.animal.isVehicle()) {
        return false;
      }
      return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
      if (!this.partner.isVehicle()) {
        return false;
      }
      return super.canContinueToUse();
    }

    @Override
    public void tick() {
      super.tick();
      if (this.animal.distanceToSqr(this.partner) < 9.0) {  // kludge to fix failure to breed
        this.breed();
      }
    }
  }
}
