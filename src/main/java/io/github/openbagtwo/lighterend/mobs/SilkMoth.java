package io.github.openbagtwo.lighterend.mobs;

import com.google.common.collect.Lists;
import io.github.openbagtwo.lighterend.blocks.entities.SilkMothNestEntity;
import io.github.openbagtwo.lighterend.registries.LighterEndBlockEntities;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import io.github.openbagtwo.lighterend.registries.LighterEndSounds;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SilkMoth extends Animal {

  /**
   * the distance beyond which the moth will look for a new hive
   */
  public static double MAX_DISTANCE_FROM_HIVE = 48;

  /**
   * the maximum number of ticks a moth is allowed to search for a hive
   */
  public static int MAX_TICKS_TO_FIND_HIVE = 200;

  public static int MIN_TICKS_BETWEEN_ENTERING_HIVE = 2400;

  @Nullable
  protected BlockPos hivePos;

  protected int ticksLeftUntilEnterHive;
  protected int ticksLeftUntilFindHive;
  protected int ticksInsideWater;

  protected MoveToHiveGoal moveToHiveGoal;


  public SilkMoth(EntityType<? extends SilkMoth> entityType, Level world) {
    super(entityType, world);
    this.moveControl = new FlyingMoveControl(this, 20, true);
    this.lookControl = new LookControl(this);
    this.setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, -1.0F);
    this.setPathfindingMalus(PathType.WATER, -1.0F);
    this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
    this.setPathfindingMalus(PathType.COCOA, -1.0F);
    this.setPathfindingMalus(PathType.FENCE, -1.0F);
    this.xpReward = 1;
  }

  public static AttributeSupplier.Builder createAttributes() {
    return Animal.createAnimalAttributes()
        .add(Attributes.MAX_HEALTH, 2.0D)
        .add(Attributes.FOLLOW_RANGE, 16.0D)
        .add(Attributes.FLYING_SPEED, 0.4D)
        .add(Attributes.MOVEMENT_SPEED, 0.1D);
  }

  @Override
  protected PathNavigation createNavigation(Level world) {
    FlyingPathNavigation birdNavigation = new FlyingPathNavigation(this, world) {
      @Override
      public boolean isStableDestination(BlockPos pos) {
        BlockState state = this.level.getBlockState(pos);
        return !state.isAir();
      }

    };
    birdNavigation.setCanOpenDoors(false);
    birdNavigation.setCanFloat(false);
    birdNavigation.setRequiredPathLength(48.0F);
    return birdNavigation;
  }

  @Override
  public float getWalkTargetValue(BlockPos pos, LevelReader world) {
    return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
  }

  @Override
  protected void registerGoals() {
    this.goalSelector.addGoal(0, new EnterHiveGoal());
    this.goalSelector.addGoal(1, new BreedGoal(this, 1.0));
    this.goalSelector.addGoal(
        2,
        new TemptGoal(this, 1.25, Ingredient.of(LighterEndBlocks.TENANEA_FLOWER), false)
    );
    this.goalSelector.addGoal(2, new ValidateHiveGoal());
    this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.25));
    this.goalSelector.addGoal(3, new FindHiveGoal());
    this.moveToHiveGoal = new MoveToHiveGoal();
    this.goalSelector.addGoal(3, moveToHiveGoal);
    this.goalSelector.addGoal(4, new WanderAroundGoal());
    this.goalSelector.addGoal(5, new FloatGoal(this));
  }

  @Override
  public void addAdditionalSaveData(ValueOutput view) {
    super.addAdditionalSaveData(view);
    if (this.hivePos != null) {
      view.storeNullable("hive_pos", BlockPos.CODEC, this.hivePos);
    }
  }

  @Override
  protected void readAdditionalSaveData(ValueInput view) {
    super.readAdditionalSaveData(view);
    this.hivePos = view.read("hive_pos", BlockPos.CODEC).orElse(null);
  }

  @Override
  protected void customServerAiStep(ServerLevel world) {
    if (this.isInWater()) {
      this.ticksInsideWater++;
    } else {
      this.ticksInsideWater = 0;
    }

    if (this.ticksInsideWater > 20) {
      this.hurtServer(world, this.damageSources().drown(), 1.0F);
    }
  }

  @Override
  public void aiStep() {
    super.aiStep();
    if (!this.level().isClientSide()) {
      if (this.ticksLeftUntilEnterHive > 0) {
        this.ticksLeftUntilEnterHive--;
      }

      if (this.ticksLeftUntilFindHive > 0) {
        this.ticksLeftUntilFindHive--;
      }

      if (this.tickCount % 20 == 0 && this.getHive() == null) {
        this.hivePos = null;
      }
    }
  }

  @Override
  public boolean isFood(ItemStack itemStack) {
    return itemStack.is(LighterEndBlocks.TENANEA_FLOWER.asItem());
  }

  @Override
  protected void checkFallDamage(double heightDifference, boolean onGround, BlockState state,
      BlockPos landedPosition) {
  }

  @Override
  protected Entity.MovementEmission getMovementEmission() {
    return Entity.MovementEmission.EVENTS;
  }

  @Override
  public boolean canBeLeashed() {
    return true;
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
  public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
    return LighterEndMobs.SILK_MOTH.mob.create(world, EntitySpawnReason.BREEDING);
  }

  @Nullable
  SilkMothNestEntity getHive() {
    if (this.hivePos == null) {
      return null;
    }
    if (!this.hivePos.closerThan(this.blockPosition(), MAX_DISTANCE_FROM_HIVE)) {
      return null;
    }
    return this.level().getBlockEntity(
        this.hivePos, LighterEndBlockEntities.SILK_MOTH_NEST
    ).orElse(null);

  }

  public boolean canEnterHive() {
    if (this.ticksLeftUntilEnterHive > 0) {
      return false;
    }
    SilkMothNestEntity hive = this.getHive();
    return hive == null || !hive.isNearFire();  // if the hive doesn't exist, then it's not on fire
  }

  public void setHive(BlockPos hivePos) {
    this.hivePos = hivePos;
  }

  public void clearHivePos() {
    this.hivePos = null;
    this.ticksLeftUntilFindHive = MAX_TICKS_TO_FIND_HIVE;
  }

  public void resetCannotEnterHiveTicks() {
    this.ticksLeftUntilEnterHive = MIN_TICKS_BETWEEN_ENTERING_HIVE;
  }

  protected void startMovingTo(BlockPos pos) {
    Vec3 vec3d = Vec3.atBottomCenterOf(pos);
    int i = 0;
    BlockPos blockPos = this.blockPosition();
    int j = (int) vec3d.y - blockPos.getY();
    if (j > 2) {
      i = 4;
    } else if (j < -2) {
      i = -4;
    }

    int k = 6;
    int l = 8;
    int m = blockPos.distManhattan(pos);
    if (m < 15) {
      k = m / 2;
      l = m / 2;
    }

    Vec3 vec3d2 = AirRandomPos.getPosTowards(this, k, l, i, vec3d, (float) (Math.PI / 10));
    if (vec3d2 != null) {
      this.navigation.setMaxVisitedNodesMultiplier(0.5F);
      this.navigation.moveTo(vec3d2.x, vec3d2.y, vec3d2.z, 1.0);
    }
  }

  // AI Goals

  class EnterHiveGoal extends Goal {

    @Override
    public boolean canUse() {
      if (
          SilkMoth.this.hivePos != null && SilkMoth.this.canEnterHive()
              && SilkMoth.this.hivePos.closerToCenterThan(SilkMoth.this.position(), 2.0)
      ) {
        SilkMothNestEntity nest = SilkMoth.this.getHive();
        if (nest != null && nest.getOccupancy() < SilkMothNestEntity.MAX_MOTH_COUNT) {
          return true;
        }
        SilkMoth.this.hivePos = null;
      }
      return false;
    }

    @Override
    public boolean canContinueToUse() {
      return false;
    }

    @Override
    public void start() {
      SilkMothNestEntity nest = SilkMoth.this.getHive();
      if (nest != null) {
        nest.tryEnterHive(SilkMoth.this);
      }
    }
  }

  class ValidateHiveGoal extends Goal {

    private final int ticksUntilNextValidate = Mth.nextInt(SilkMoth.this.random, 20, 40);
    private long lastValidateTime = -1L;

    @Override
    public void start() {
      if (SilkMoth.this.hivePos != null && SilkMoth.this.level()
          .isLoaded(SilkMoth.this.hivePos) && SilkMoth.this.getHive() == null) {
        SilkMoth.this.clearHivePos();
      }

      this.lastValidateTime = SilkMoth.this.level().getGameTime();
    }

    @Override
    public boolean canUse() {
      return SilkMoth.this.level().getGameTime()
          > this.lastValidateTime + this.ticksUntilNextValidate;
    }

    @Override
    public boolean canContinueToUse() {
      return false;
    }
  }

  class MoveToHiveGoal extends Goal {

    int ticks;
    final List<BlockPos> possibleHives = Lists.newArrayList();

    @Nullable
    private Path path;
    private int ticksUntilLost;

    public MoveToHiveGoal() {
      this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
      return (
          SilkMoth.this.hivePos != null
              && SilkMoth.this.hivePos.closerThan(SilkMoth.this.blockPosition(),
              MAX_DISTANCE_FROM_HIVE)
              && SilkMoth.this.canEnterHive()
              && !this.isCloseEnough(SilkMoth.this.hivePos)
              && SilkMoth.this.level().getBlockState(SilkMoth.this.hivePos)
              .is(LighterEndBlocks.SILK_MOTH_NEST)
      );
    }

    @Override
    public boolean canContinueToUse() {
      return this.canUse();
    }

    @Override
    public void start() {
      this.ticks = 0;
      this.ticksUntilLost = 0;
      super.start();
    }

    @Override
    public void stop() {
      this.ticks = 0;
      this.ticksUntilLost = 0;
      SilkMoth.this.navigation.stop();
      SilkMoth.this.navigation.resetMaxVisitedNodesMultiplier();
    }

    @Override
    public void tick() {
      if (SilkMoth.this.hivePos != null) {
        this.ticks++;
        if (this.ticks > this.adjustedTickDelay(MIN_TICKS_BETWEEN_ENTERING_HIVE)) {
          this.makeChosenHivePossibleHive();
        } else if (!SilkMoth.this.navigation.isInProgress()) {
          if (!SilkMoth.this.hivePos.closerToCenterThan(SilkMoth.this.position(), 16)) {
            if (!SilkMoth.this.hivePos.closerToCenterThan(SilkMoth.this.position(),
                MAX_DISTANCE_FROM_HIVE)) {
              SilkMoth.this.clearHivePos();
            } else {
              SilkMoth.this.startMovingTo(SilkMoth.this.hivePos);
            }
          } else {
            boolean bl = this.startMovingToFar(SilkMoth.this.hivePos);
            if (!bl) {
              this.makeChosenHivePossibleHive();
            } else if (
                this.path != null && SilkMoth.this.navigation.getPath().sameAs(this.path)
            ) {
              this.ticksUntilLost++;
              if (this.ticksUntilLost > 60) {
                SilkMoth.this.clearHivePos();
                this.ticksUntilLost = 0;
              }
            } else {
              this.path = SilkMoth.this.navigation.getPath();
            }
          }
        }
      }
    }

    private boolean startMovingToFar(BlockPos pos) {
      int i = pos.closerToCenterThan(SilkMoth.this.position(), 3) ? 1 : 2;
      SilkMoth.this.navigation.setMaxVisitedNodesMultiplier(10.0F);
      SilkMoth.this.navigation.moveTo(pos.getX(), pos.getY(), pos.getZ(), i, 1.0);
      return SilkMoth.this.navigation.getPath() != null
          && SilkMoth.this.navigation.getPath().canReach();
    }

    boolean isPossibleHive(BlockPos pos) {
      return this.possibleHives.contains(pos);
    }

    private void addPossibleHive(BlockPos pos) {
      this.possibleHives.add(pos);

      while (this.possibleHives.size() > 3) {
        this.possibleHives.remove(0);
      }
    }

    void clearPossibleHives() {
      this.possibleHives.clear();
    }

    private void makeChosenHivePossibleHive() {
      if (SilkMoth.this.hivePos != null) {
        this.addPossibleHive(SilkMoth.this.hivePos);
      }

      SilkMoth.this.clearHivePos();
    }

    private boolean isCloseEnough(BlockPos pos) {
      if (pos.closerThan(SilkMoth.this.blockPosition(), 2)) {
        return true;
      } else {
        Path path = SilkMoth.this.navigation.getPath();
        return path != null && path.getTarget().equals(pos) && path.canReach()
            && path.isDone();
      }
    }
  }

  class FindHiveGoal extends Goal {

    @Override
    public boolean canUse() {
      return SilkMoth.this.ticksLeftUntilFindHive <= 0
          && SilkMoth.this.hivePos == null
          && SilkMoth.this.canEnterHive();
    }

    @Override
    public boolean canContinueToUse() {
      return false;
    }

    @Override
    public void start() {
      SilkMoth.this.ticksLeftUntilFindHive = MAX_TICKS_TO_FIND_HIVE;
      List<BlockPos> list = this.getNearbyFreeHives();
      if (!list.isEmpty()) {
        for (BlockPos blockPos : list) {
          if (!SilkMoth.this.moveToHiveGoal.isPossibleHive(blockPos)) {
            SilkMoth.this.hivePos = blockPos;
            return;
          }
        }

        SilkMoth.this.moveToHiveGoal.clearPossibleHives();
        SilkMoth.this.setHive(list.getFirst());
      }
    }

    private List<BlockPos> getNearbyFreeHives() {
      BlockPos blockPos = SilkMoth.this.blockPosition();
      Level world = SilkMoth.this.level();

      List<BlockPos> nearbyHives = new ArrayList<>();
      for (int dy = 0; dy <= 10 && dy >= -10; dy = (dy <= 0 ? 1 : 0) - dy) {
        for (int dx = 0;
            dx <= MAX_DISTANCE_FROM_HIVE - Math.abs(dy)
                && dx >= -MAX_DISTANCE_FROM_HIVE + Math.abs(dy);
            dx = (dx <= 0 ? 1 : 0) - dx) {
          for (int dz = 0; dz <= MAX_DISTANCE_FROM_HIVE - Math.abs(dy) - Math.abs(dx)
              && dz >= -MAX_DISTANCE_FROM_HIVE + Math.abs(dy) + Math.abs(dx);
              dz = (dz <= 0 ? 1 : 0) - dz) {
            BlockPos blockPos2 = blockPos.offset(dx, dy, dz);
            if (!blockPos2.closerThan(blockPos, MAX_DISTANCE_FROM_HIVE)) {
              continue;
            }
            if (world.getBlockEntity(blockPos2) instanceof SilkMothNestEntity nest) {
              if (nest.getOccupancy() < SilkMothNestEntity.MAX_MOTH_COUNT) {
                nearbyHives.add(blockPos2);
              }
            }
          }
        }
      }
      nearbyHives.sort(Comparator.comparingDouble(pos -> pos.distSqr(blockPos)));
      return nearbyHives;
    }
  }

  class WanderAroundGoal extends Goal {

    WanderAroundGoal() {
      this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
      return SilkMoth.this.navigation.isDone() && SilkMoth.this.random.nextInt(10) == 0;
    }

    @Override
    public boolean canContinueToUse() {
      return SilkMoth.this.navigation.isInProgress();
    }

    @Override
    public void start() {
      Vec3 vec3d = this.getRandomLocation();
      if (vec3d != null) {
        SilkMoth.this.navigation.moveTo(
            SilkMoth.this.navigation.createPath(BlockPos.containing(vec3d), 1), 1.0);
      }
    }

    @Nullable
    private Vec3 getRandomLocation() {
      Vec3 vec3d2;
      if (SilkMoth.this.getHive() != null && SilkMoth.this.hivePos.closerThan(
          SilkMoth.this.blockPosition(), this.getMaxWanderDistance())) {
        Vec3 vec3d = Vec3.atCenterOf(SilkMoth.this.hivePos);
        vec3d2 = vec3d.subtract(SilkMoth.this.position()).normalize();
      } else {
        vec3d2 = SilkMoth.this.getViewVector(0.0F);
      }

      Vec3 vec3d3 = HoverRandomPos.getPos(SilkMoth.this, 8, 7, vec3d2.x, vec3d2.z,
          (float) (Math.PI / 2), 3, 1);
      return vec3d3 != null ? vec3d3
          : AirAndWaterRandomPos.getPos(SilkMoth.this, 8, 4, -2, vec3d2.x, vec3d2.z,
              (float) (Math.PI / 2));
    }

    private int getMaxWanderDistance() {
      int i = (SilkMoth.this.hivePos == null) ? 16 : 24;
      return 48 - i;
    }
  }

  @Override
  public SoundEvent getAmbientSound() {
    return LighterEndSounds.SILK_MOTH_IDLE;
  }

  @Nullable
  protected SoundEvent getHurtSound(DamageSource source) {
    return LighterEndSounds.SILK_MOTH_HURT;
  }

  @Nullable
  protected SoundEvent getDeathSound() {
    return LighterEndSounds.SILK_MOTH_DEATH;
  }

}
