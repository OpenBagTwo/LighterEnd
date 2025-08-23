package io.github.openbagtwo.lighterend.mobs;

import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import io.github.openbagtwo.lighterend.utils.PosInfo;
import io.github.openbagtwo.lighterend.utils.math.MathUtils;
import java.util.EnumSet;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;

public class Dragonfly extends AnimalEntity implements Flutterer {

  public Dragonfly(EntityType<Dragonfly> entityType, World world) {
    super(entityType, world);
    this.moveControl = new FlightMoveControl(this, 20, true);
    this.lookControl = new DragonflyLookControl(this);
    this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
    this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
    this.experiencePoints = 1;
  }

  public static DefaultAttributeContainer.Builder createAttributes() {
    return AnimalEntity.createAnimalAttributes()
        .add(EntityAttributes.MAX_HEALTH, 8.0D)
        .add(EntityAttributes.FOLLOW_RANGE, 16.0D)
        .add(EntityAttributes.FLYING_SPEED, 1.0D)
        .add(EntityAttributes.MOVEMENT_SPEED, 0.1D);
  }

  @Override
  public boolean canBeLeashed() {
    return true;
  }

  @Override
  protected @NotNull EntityNavigation createNavigation(World world) {
    BirdNavigation birdNavigation = new BirdNavigation(this, world) {
      public boolean isValidPosition(BlockPos pos) {
        BlockState state = this.world.getBlockState(pos);
        return state.isAir() || !state.blocksMovement();
      }

      public void tick() {
        super.tick();
      }
    };
    birdNavigation.setCanOpenDoors(false);
    birdNavigation.setCanSwim(false);
    return birdNavigation;
  }

  @Override
  public float getPathfindingFavor(BlockPos pos, WorldView world) {
    return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
  }

  @Override
  public boolean isBreedingItem(ItemStack itemStack) {
    return false;
  }

  @Override
  protected void initGoals() {
    this.goalSelector.add(1, new SwimGoal(this));
    this.goalSelector.add(2, new AnimalMateGoal(this, 1.0D));
    this.goalSelector.add(3, new FollowParentGoal(this, 1.0D));
    this.goalSelector.add(4, new WanderAroundGoal());
  }

  @Override
  public boolean isPushable() {
    return false;
  }

  @Override
  protected void fall(double heightDifference, boolean onGround, BlockState state,
      BlockPos landedPosition) {
  }

  @Override
  protected Entity.@NotNull MoveEffect getMoveEffect() {
    return Entity.MoveEffect.EVENTS;
  }

  @Override
  public boolean isInAir() {
    return !this.isOnGround();
  }

  @Override
  public boolean hasNoGravity() {
    return true;
  }

  @Override
  public SoundEvent getAmbientSound() {
    return LighterEndSounds.DRAGONFLY_IDLE;
  }

  @Override
  protected float getSoundVolume() {
    return MathHelper.nextFloat(random, 0.25F, 0.5F);
  }

  static class DragonflyLookControl extends LookControl {

    DragonflyLookControl(MobEntity entity) {
      super(entity);
    }

    protected boolean shouldStayHorizontal() {
      return true;
    }
  }

  class WanderAroundGoal extends Goal {

    WanderAroundGoal() {
      this.setControls(EnumSet.of(Goal.Control.MOVE));
    }

    public boolean canStart() {
      return Dragonfly.this.navigation.isIdle()
          && Dragonfly.this.random.nextInt(10) == 0;
    }

    public boolean shouldContinue() {
      return Dragonfly.this.navigation.isFollowingPath();
    }

    public void start() {
      Vec3d vec3d = this.getRandomLocation();
      if (vec3d != null) {
        BlockPos pos = new BlockPos((int) vec3d.x, (int) vec3d.y, (int) vec3d.z);
        try {
          Path path = Dragonfly.this.navigation.findPathTo(pos, 1);
          if (path != null) {
            Dragonfly.this.navigation.startMovingAlong(path, 1.0D);
          }
        } catch (Exception e) {
        }
      }
      super.start();
    }

    private Vec3d getRandomLocation() {
      int h = PosInfo.downRay(Dragonfly.this.getEntityWorld(),
          Dragonfly.this.getBlockPos(), 16);
      Vec3d rotation = Dragonfly.this.getRotationVec(0.0F);
      Vec3d airPos = AboveGroundTargeting.find(Dragonfly.this, 8, 7, rotation.x, rotation.z,
          1.5707964F, 3, 1);
      if (airPos != null) {
        if (isInVoid(airPos)) {
          for (int i = 0; i < 8; i++) {
            airPos = AboveGroundTargeting.find(
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
        if (h > 5 && airPos.getY() >= Dragonfly.this.getBlockPos().getY()) {
          airPos = new Vec3d(airPos.x, airPos.y - h * 0.5, airPos.z);
        }
        return airPos;
      }
      return NoPenaltySolidTargeting.find(
          Dragonfly.this,
          8,
          4,
          -2,
          rotation.x,
          rotation.z,
          1.5707963705062866D
      );
    }

    private boolean isInVoid(Vec3d pos) {
      int h = PosInfo.downRay(
          Dragonfly.this.getEntityWorld(),
          new BlockPos((int) pos.x, (int) pos.y, (int) pos.z),
          128
      );
      return h > 100;
    }
  }

  @Override
  public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
    return LighterEndMobs.DRAGONFLY.mob.create(world, SpawnReason.BREEDING);
  }

  @Override
  public boolean canImmediatelyDespawn(double d) {
    return !this.hasCustomName();
  }
}
