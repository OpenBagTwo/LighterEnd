package io.github.openbagtwo.lighterend.mobs;

import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import io.github.openbagtwo.lighterend.utils.PosInfo;
import io.github.openbagtwo.lighterend.utils.math.MathUtils;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Dragonfly extends Animal {

  public Dragonfly(EntityType<Dragonfly> entityType, Level world) {
    super(entityType, world);
    this.moveControl = new FlyingMoveControl(this, 20, true);
    this.lookControl = new DragonflyLookControl(this);
    this.setPathfindingMalus(PathType.WATER, -1.0F);
    this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, -1.0F);
    this.xpReward = 1;
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Animal.createAnimalAttributes()
        .add(Attributes.MAX_HEALTH, 8.0D)
        .add(Attributes.FOLLOW_RANGE, 16.0D)
        .add(Attributes.FLYING_SPEED, 1.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.1D);
  }

  @Override
  public boolean canBeLeashed() {
    return true;
  }

  @Override
  protected @NotNull PathNavigation createNavigation(Level world) {
    FlyingPathNavigation birdNavigation = new FlyingPathNavigation(this, world) {
      public boolean isStableDestination(BlockPos pos) {
        BlockState state = this.level.getBlockState(pos);
        return state.isAir() || !state.is(BlockTags.BLOCKS_MOTION);
      }

      public void tick() {
        super.tick();
      }
    };
    birdNavigation.setCanOpenDoors(false);
    birdNavigation.setCanFloat(false);
    return birdNavigation;
  }

  @Override
  public float getWalkTargetValue(BlockPos pos, LevelReader world) {
    return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
  }

  @Override
  public boolean isFood(ItemStack itemStack) {
    return false;
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(1, new FloatGoal(this));
    this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
    this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.0D));
    this.goalSelector.addGoal(4, new WanderAroundGoal());
  }

  @Override
  public boolean isPushable() {
    return false;
  }

  @Override
  protected void checkFallDamage(double heightDifference, boolean onGround, BlockState state,
      BlockPos landedPosition) {
  }

  @Override
  protected Entity.@NotNull MovementEmission getMovementEmission() {
    return Entity.MovementEmission.EVENTS;
  }

  @Override
  public boolean isFlapping() {
    return this.isFlying();
  }

  public boolean isFlying() {
    return !this.onGround();
  }

  @Override
  public boolean isNoGravity() {
    return true;
  }

  @Override
  public SoundEvent getAmbientSound() {
    return LighterEndSounds.DRAGONFLY_IDLE;
  }

  @Nullable
  protected SoundEvent getHurtSound(DamageSource source) {
    return LighterEndSounds.DRAGONFLY_HURT;
  }

  @Nullable
  protected SoundEvent getDeathSound() {
    return LighterEndSounds.DRAGONFLY_DEATH;
  }

  @Override
  protected float getSoundVolume() {
    return Mth.nextFloat(random, 0.25F, 0.5F);
  }

  static class DragonflyLookControl extends LookControl {

    DragonflyLookControl(Mob entity) {
      super(entity);
    }

    protected boolean resetXRotOnTick() {
      return true;
    }
  }

  class WanderAroundGoal extends Goal {

    WanderAroundGoal() {
      this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
      return Dragonfly.this.navigation.isDone()
          && Dragonfly.this.random.nextInt(10) == 0;
    }

    public boolean canContinueToUse() {
      return Dragonfly.this.navigation.isInProgress();
    }

    public void start() {
      Vec3 vec3d = this.getRandomLocation();
      if (vec3d != null) {
        BlockPos pos = new BlockPos((int) vec3d.x, (int) vec3d.y, (int) vec3d.z);
        try {
          Path path = Dragonfly.this.navigation.createPath(pos, 1);
          if (path != null) {
            Dragonfly.this.navigation.moveTo(path, 1.0D);
          }
        } catch (Exception e) {
        }
      }
      super.start();
    }

    private Vec3 getRandomLocation() {
      int h = PosInfo.downRay(Dragonfly.this.level(),
          Dragonfly.this.blockPosition(), 16);
      Vec3 rotation = Dragonfly.this.getViewVector(0.0F);
      Vec3 airPos = HoverRandomPos.getPos(Dragonfly.this, 8, 7, rotation.x, rotation.z,
          1.5707964F, 3, 1);
      if (airPos != null) {
        if (isInVoid(airPos)) {
          for (int i = 0; i < 8; i++) {
            airPos = HoverRandomPos.getPos(
                Dragonfly.this,
                16,
                7,
                rotation.x,
                rotation.z,
                MathUtils.PI2,
                3,
                1
            );
            if (airPos != null && !isInVoid(airPos)) {
              return airPos;
            }
          }
          return null;
        }
        if (h > 5 && airPos.y() >= Dragonfly.this.blockPosition().getY()) {
          airPos = new Vec3(airPos.x, airPos.y - h * 0.5, airPos.z);
        }
        return airPos;
      }
      return AirAndWaterRandomPos.getPos(
          Dragonfly.this,
          8,
          4,
          -2,
          rotation.x,
          rotation.z,
          1.5707963705062866D
      );
    }

    private boolean isInVoid(Vec3 pos) {
      int h = PosInfo.downRay(
          Dragonfly.this.level(),
          new BlockPos((int) pos.x, (int) pos.y, (int) pos.z),
          128
      );
      return h > 100;
    }
  }

  @Override
  public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
    return LighterEndMobs.DRAGONFLY.mob.create(world, EntitySpawnReason.BREEDING);
  }

  @Override
  public boolean removeWhenFarAway(double d) {
    return !this.hasCustomName();
  }
}
