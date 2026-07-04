package io.github.openbagtwo.lighterend.mobs;

import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import io.github.openbagtwo.lighterend.registries.LighterEndTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.SpiderNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class ChorusCrab extends AnimalEntity {

  private static final TrackedData<Byte> CLIMBING = DataTracker.registerData(
      ChorusCrab.class,
      TrackedDataHandlerRegistry.BYTE
  );

  public ChorusCrab(EntityType<? extends ChorusCrab> entityType, World world) {
    super(entityType, world);
  }

  public static DefaultAttributeContainer.Builder createCrabAttributes() {
    return HostileEntity.createHostileAttributes()
        .add(EntityAttributes.MAX_HEALTH, 32.0)
        .add(EntityAttributes.MOVEMENT_SPEED, 0.1)
        .add(EntityAttributes.JUMP_STRENGTH, 0)
        .add(EntityAttributes.STEP_HEIGHT, 2.0)
        .add(EntityAttributes.TEMPT_RANGE, 8.0)
        .add(EntityAttributes.ATTACK_DAMAGE, 2.0)
        .add(EntityAttributes.ATTACK_KNOCKBACK, 3.0)
        .add(EntityAttributes.ATTACK_SPEED, 0.1)
        .add(EntityAttributes.ARMOR, 8.0)
        .add(EntityAttributes.ENTITY_INTERACTION_RANGE, 1.5)
        .add(EntityAttributes.BLOCK_INTERACTION_RANGE, 1.5)
        .add(EntityAttributes.KNOCKBACK_RESISTANCE, 5.0);
  }

  @Override
  protected void initGoals() {
    this.goalSelector.add(1, new SwimGoal(this));
    this.goalSelector.add(1, new RevengeGoal(this));
    this.goalSelector.add(2, new MeleeAttackGoal(this, 0.5, false));
    this.goalSelector.add(3, new MateGoal(this));
    this.goalSelector.add(
        4,
        new TemptGoal(this, 0.8F, Ingredient.ofItems(Items.CHORUS_FLOWER), true)
    );
    this.goalSelector.add(
        5,
        new FleeEntityGoal(this, PlayerEntity.class, 3.0F, 0.25F, 1F, (entity) -> true)
    );
    this.goalSelector.add(8, new WanderAroundFarGoal(this, 0.8));
    this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
    this.goalSelector.add(10, new LookAroundGoal(this));
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
  public boolean canHaveStatusEffect(StatusEffectInstance effect) {
    if (effect.equals(StatusEffects.POISON)) {
      return !this.getType().isIn(EntityTypeTags.IGNORES_POISON_AND_REGEN);
    }
    return super.canHaveStatusEffect(effect);
  }

  @Nullable
  @Override
  public EntityData initialize(
      ServerWorldAccess world,
      LocalDifficulty difficulty,
      SpawnReason spawnReason,
      @Nullable EntityData entityData
  ) {
    entityData = super.initialize(world, difficulty, spawnReason, entityData);
    if (spawnReason == SpawnReason.BREEDING) {
      this.setPersistent();
    } else if (world.getRandom().nextInt(512) == 0) {
      EndermanEntity rider = EntityType.ENDERMAN.create(this.getWorld(), SpawnReason.JOCKEY);
      if (rider != null) {
        rider.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0F);
        rider.initialize(world, difficulty, spawnReason, null);
        rider.startRiding(this);
      }
    }
    this.setCanPickUpLoot(true);
    this.initEquipment(world.getRandom(), difficulty);
    this.enchantMainHandItem(world, world.getRandom(), difficulty);

    return entityData;
  }

  @Override
  protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
    if (random.nextInt(512) == 0) {
      this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
      this.setDropGuaranteed(EquipmentSlot.MAINHAND);

    }
  }

  @Override
  public boolean canPickupItem(ItemStack stack) {
    return stack.isIn(ItemTags.SWORDS);
  }

  @Override
  public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
    return LighterEndMobs.CHORUS_CRAB.mob.create(world, SpawnReason.BREEDING);
  }

  @Override
  public float getPathfindingFavor(BlockPos pos, WorldView world) {
    return world.getBlockState(pos.down()).isIn(BlockTags.SAND) ? 12.0F
        : world.getBlockState(pos.down()).isIn(LighterEndTags.END_SOIL) ? 10.0F
            : world.getPhototaxisFavor(pos);
  }

  @Override
  public boolean isBreedingItem(ItemStack stack) {
    return stack.isOf(Items.CHORUS_FLOWER);
  }

  @Override
  protected EntityNavigation createNavigation(World world) {
    return new SpiderNavigation(this, world);
  }

  @Override
  protected void initDataTracker(DataTracker.Builder builder) {
    super.initDataTracker(builder);
    builder.add(CLIMBING, (byte) 0);
  }

  @Override
  public void tick() {
    super.tick();
    if (!this.getWorld().isClient()) {
      this.setClimbingWall(this.horizontalCollision);
    }
    if (this.getBreedingAge() >= 0 && this.hasVehicle()) {
      this.dismountVehicle();
    }
  }

  @Override
  public boolean canBreedWith(AnimalEntity other) {
    if (this.hasPassengers() || other.hasPassengers()) {
      return false;
    }
    return super.canBreedWith(other);
  }

  @Override
  public void breed(ServerWorld world, AnimalEntity other, @Nullable PassiveEntity baby) {
    super.breed(world, other, baby);
    if (baby != null) {
      baby.startRiding(this, true);
    }
  }

  public void setClimbingWall(boolean climbing) {
    byte b = this.dataTracker.get(CLIMBING);
    if (climbing) {
      b = (byte) (b | 1);
    } else {
      b = (byte) (b & -2);
    }

    this.dataTracker.set(CLIMBING, b);
  }

  class MateGoal extends AnimalMateGoal {

    public MateGoal(AnimalEntity crab) {
      super(crab, 1.0F);
    }

    @Override
    public boolean canStart() {
      if (this.animal.hasPassengers()) {
        return false;
      }
      return super.canStart();
    }

    @Override
    public boolean shouldContinue() {
      if (!this.mate.hasPassengers()) {
        return false;
      }
      return super.shouldContinue();
    }

    @Override
    public void tick() {
      super.tick();
      if (this.animal.squaredDistanceTo(this.mate) < 9.0) {  // kludge to fix failure to breed
        this.breed();
      }
    }
  }
}
