package io.github.openbagtwo.lighterend.mobs;

import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ChorusCrab extends SpiderEntity {

  public ChorusCrab(EntityType<? extends ChorusCrab> entityType, World world) {
    super(entityType, world);
  }

  public static DefaultAttributeContainer.Builder createCrabAttributes() {
    return HostileEntity.createHostileAttributes()
        .add(EntityAttributes.MAX_HEALTH, 32.)
        .add(EntityAttributes.MOVEMENT_SPEED, 0.1)
        .add(EntityAttributes.ATTACK_DAMAGE, 2.0)
        .add(EntityAttributes.ATTACK_KNOCKBACK, 3.0)
        .add(EntityAttributes.ATTACK_SPEED, 0.1)
        .add(EntityAttributes.ARMOR, 8.)
        .add(EntityAttributes.ENTITY_INTERACTION_RANGE, 1.5)
        .add(EntityAttributes.BLOCK_INTERACTION_RANGE, 1.5)
        .add(EntityAttributes.KNOCKBACK_RESISTANCE, 5.0);
  }

  @Override
  protected void initGoals() {
    this.goalSelector.add(1, new SwimGoal(this));
    this.goalSelector.add(2,
        new FleeEntityGoal(this, PlayerEntity.class, 3.0F, 0.1F, 0.3, (entity) -> true)
    );

    this.goalSelector.add(4, new MeleeAttackGoal(this, 0.5, false));
    this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8));
    this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
    this.goalSelector.add(6, new LookAroundGoal(this));
  }

  @Override
  protected SoundEvent getAmbientSound() {
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

    if (world.getRandom().nextInt(512) == 0) {
      EndermanEntity rider = EntityType.ENDERMAN.create(this.getWorld(), SpawnReason.JOCKEY);
      if (rider != null) {
        rider.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0F);
        rider.initialize(world, difficulty, spawnReason, null);
        rider.startRiding(this);
      }
    }

    return entityData;
  }

}
