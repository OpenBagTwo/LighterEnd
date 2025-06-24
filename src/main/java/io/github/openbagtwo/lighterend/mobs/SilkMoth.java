package io.github.openbagtwo.lighterend.mobs;

import com.google.common.collect.Lists;
import io.github.openbagtwo.lighterend.blocks.entities.SilkMothNestEntity;
import io.github.openbagtwo.lighterend.registries.LighterEndBlockEntities;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.NoWaterTargeting;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class SilkMoth extends AnimalEntity implements Flutterer {

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


  public SilkMoth(EntityType<? extends SilkMoth> entityType, World world) {
    super(entityType, world);
    this.moveControl = new FlightMoveControl(this, 20, true);
    this.lookControl = new LookControl(this);
    this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
    this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
    this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
    this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
    this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
    this.experiencePoints = 1;
  }

  public static DefaultAttributeContainer.Builder createAttributes() {
    return AnimalEntity.createAnimalAttributes()
        .add(EntityAttributes.MAX_HEALTH, 2.0D)
        .add(EntityAttributes.FOLLOW_RANGE, 16.0D)
        .add(EntityAttributes.FLYING_SPEED, 0.4D)
        .add(EntityAttributes.MOVEMENT_SPEED, 0.1D);
  }

  @Override
  protected EntityNavigation createNavigation(World world) {
    BirdNavigation birdNavigation = new BirdNavigation(this, world) {
      @Override
      public boolean isValidPosition(BlockPos pos) {
        BlockState state = this.world.getBlockState(pos);
        return !state.isAir();
      }

    };
    birdNavigation.setCanPathThroughDoors(false);
    birdNavigation.setCanSwim(false);
    birdNavigation.setMaxFollowRange(48.0F);
    return birdNavigation;
  }

  @Override
  public float getPathfindingFavor(BlockPos pos, WorldView world) {
    return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
  }

  @Override
  protected void initGoals() {
    this.goalSelector.add(0, new EnterHiveGoal());
    this.goalSelector.add(1, new AnimalMateGoal(this, 1.0));
    this.goalSelector.add(
        2,
        new TemptGoal(this, 1.25, Ingredient.ofItems(LighterEndBlocks.TENANEA_FLOWER), false)
    );
    this.goalSelector.add(2, new ValidateHiveGoal());
    this.goalSelector.add(3, new FollowParentGoal(this, 1.25));
    this.goalSelector.add(3, new FindHiveGoal());
    this.moveToHiveGoal = new MoveToHiveGoal();
    this.goalSelector.add(3, moveToHiveGoal);
    this.goalSelector.add(4, new WanderAroundGoal());
    this.goalSelector.add(5, new SwimGoal(this));
  }

  @Override
  public void writeCustomDataToNbt(NbtCompound nbt) {
    super.writeCustomDataToNbt(nbt);
    if (this.hivePos != null) {
      nbt.put("hive_pos", NbtHelper.fromBlockPos(this.hivePos));
    }
  }

  @Override
  public void readCustomDataFromNbt(NbtCompound nbt) {
    super.readCustomDataFromNbt(nbt);
    this.hivePos = NbtHelper.toBlockPos(nbt, "hive_pos").orElse(null);
  }

  @Override
  protected void mobTick(ServerWorld world) {
    if (this.isTouchingWater()) {
      this.ticksInsideWater++;
    } else {
      this.ticksInsideWater = 0;
    }

    if (this.ticksInsideWater > 20) {
      this.damage(world, this.getDamageSources().drown(), 1.0F);
    }
  }

  @Override
  public void tickMovement() {
    super.tickMovement();
    if (!this.getWorld().isClient) {
      if (this.ticksLeftUntilEnterHive > 0) {
        this.ticksLeftUntilEnterHive--;
      }

      if (this.ticksLeftUntilFindHive > 0) {
        this.ticksLeftUntilFindHive--;
      }

      if (this.age % 20 == 0 && this.getHive() == null) {
        this.hivePos = null;
      }
    }
  }

  @Override
  public boolean isBreedingItem(ItemStack itemStack) {
    return itemStack.isOf(LighterEndBlocks.TENANEA_FLOWER.asItem());
  }

  @Override
  protected void fall(double heightDifference, boolean onGround, BlockState state,
      BlockPos landedPosition) {
  }

  @Override
  protected Entity.MoveEffect getMoveEffect() {
    return Entity.MoveEffect.EVENTS;
  }

  @Override
  public boolean canBeLeashed() {
    return true;
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
  public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
    return LighterEndMobs.SILK_MOTH.mob.create(world, SpawnReason.BREEDING);
  }

  @Nullable
  SilkMothNestEntity getHive() {
    if (this.hivePos == null) {
      return null;
    }
    if (!this.hivePos.isWithinDistance(this.getBlockPos(), MAX_DISTANCE_FROM_HIVE)) {
      return null;
    }
    return this.getWorld().getBlockEntity(
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
    Vec3d vec3d = Vec3d.ofBottomCenter(pos);
    int i = 0;
    BlockPos blockPos = this.getBlockPos();
    int j = (int) vec3d.y - blockPos.getY();
    if (j > 2) {
      i = 4;
    } else if (j < -2) {
      i = -4;
    }

    int k = 6;
    int l = 8;
    int m = blockPos.getManhattanDistance(pos);
    if (m < 15) {
      k = m / 2;
      l = m / 2;
    }

    Vec3d vec3d2 = NoWaterTargeting.find(this, k, l, i, vec3d, (float) (Math.PI / 10));
    if (vec3d2 != null) {
      this.navigation.setRangeMultiplier(0.5F);
      this.navigation.startMovingTo(vec3d2.x, vec3d2.y, vec3d2.z, 1.0);
    }
  }

  // AI Goals

  class EnterHiveGoal extends Goal {

    @Override
    public boolean canStart() {
      if (
          SilkMoth.this.hivePos != null && SilkMoth.this.canEnterHive()
              && SilkMoth.this.hivePos.isWithinDistance(SilkMoth.this.getPos(), 2.0)
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
    public boolean shouldContinue() {
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

    private final int ticksUntilNextValidate = MathHelper.nextInt(SilkMoth.this.random, 20, 40);
    private long lastValidateTime = -1L;

    @Override
    public void start() {
      if (SilkMoth.this.hivePos != null && SilkMoth.this.getWorld()
          .isPosLoaded(SilkMoth.this.hivePos) && SilkMoth.this.getHive() == null) {
        SilkMoth.this.clearHivePos();
      }

      this.lastValidateTime = SilkMoth.this.getWorld().getTime();
    }

    @Override
    public boolean canStart() {
      return SilkMoth.this.getWorld().getTime()
          > this.lastValidateTime + this.ticksUntilNextValidate;
    }

    @Override
    public boolean shouldContinue() {
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
      this.setControls(EnumSet.of(Goal.Control.MOVE));
    }

    @Override
    public boolean canStart() {
      return (
          SilkMoth.this.hivePos != null
              && SilkMoth.this.hivePos.isWithinDistance(SilkMoth.this.getBlockPos(),
              MAX_DISTANCE_FROM_HIVE)
              && SilkMoth.this.canEnterHive()
              && !this.isCloseEnough(SilkMoth.this.hivePos)
              && SilkMoth.this.getWorld().getBlockState(SilkMoth.this.hivePos)
              .isOf(LighterEndBlocks.SILK_MOTH_NEST)
      );
    }

    @Override
    public boolean shouldContinue() {
      return this.canStart();
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
      SilkMoth.this.navigation.resetRangeMultiplier();
    }

    @Override
    public void tick() {
      if (SilkMoth.this.hivePos != null) {
        this.ticks++;
        if (this.ticks > this.getTickCount(MIN_TICKS_BETWEEN_ENTERING_HIVE)) {
          this.makeChosenHivePossibleHive();
        } else if (!SilkMoth.this.navigation.isFollowingPath()) {
          if (!SilkMoth.this.hivePos.isWithinDistance(SilkMoth.this.getPos(), 16)) {
            if (!SilkMoth.this.hivePos.isWithinDistance(SilkMoth.this.getPos(),
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
                this.path != null && SilkMoth.this.navigation.getCurrentPath().equalsPath(this.path)
            ) {
              this.ticksUntilLost++;
              if (this.ticksUntilLost > 60) {
                SilkMoth.this.clearHivePos();
                this.ticksUntilLost = 0;
              }
            } else {
              this.path = SilkMoth.this.navigation.getCurrentPath();
            }
          }
        }
      }
    }

    private boolean startMovingToFar(BlockPos pos) {
      int i = pos.isWithinDistance(SilkMoth.this.getPos(), 3) ? 1 : 2;
      SilkMoth.this.navigation.setRangeMultiplier(10.0F);
      SilkMoth.this.navigation.startMovingTo(pos.getX(), pos.getY(), pos.getZ(), i, 1.0);
      return SilkMoth.this.navigation.getCurrentPath() != null
          && SilkMoth.this.navigation.getCurrentPath().reachesTarget();
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
      if (pos.isWithinDistance(SilkMoth.this.getBlockPos(), 2)) {
        return true;
      } else {
        Path path = SilkMoth.this.navigation.getCurrentPath();
        return path != null && path.getTarget().equals(pos) && path.reachesTarget()
            && path.isFinished();
      }
    }
  }

  class FindHiveGoal extends Goal {

    @Override
    public boolean canStart() {
      return SilkMoth.this.ticksLeftUntilFindHive <= 0
          && SilkMoth.this.hivePos == null
          && SilkMoth.this.canEnterHive();
    }

    @Override
    public boolean shouldContinue() {
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
      BlockPos blockPos = SilkMoth.this.getBlockPos();
      World world = SilkMoth.this.getWorld();

      List<BlockPos> nearbyHives = new ArrayList<>();
      for (int dy = 0; dy <= 10 && dy >= -10; dy = (dy <= 0 ? 1 : 0) - dy) {
        for (int dx = 0;
            dx <= MAX_DISTANCE_FROM_HIVE - Math.abs(dy)
                && dx >= -MAX_DISTANCE_FROM_HIVE + Math.abs(dy);
            dx = (dx <= 0 ? 1 : 0) - dx) {
          for (int dz = 0; dz <= MAX_DISTANCE_FROM_HIVE - Math.abs(dy) - Math.abs(dx)
              && dz >= -MAX_DISTANCE_FROM_HIVE + Math.abs(dy) + Math.abs(dx);
              dz = (dz <= 0 ? 1 : 0) - dz) {
            BlockPos blockPos2 = blockPos.add(dx, dy, dz);
            if (!blockPos2.isWithinDistance(blockPos, MAX_DISTANCE_FROM_HIVE)) {
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
      nearbyHives.sort(Comparator.comparingDouble(pos -> pos.getSquaredDistance(blockPos)));
      return nearbyHives;
    }
  }

  class WanderAroundGoal extends Goal {

    WanderAroundGoal() {
      this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
      return SilkMoth.this.navigation.isIdle() && SilkMoth.this.random.nextInt(10) == 0;
    }

    @Override
    public boolean shouldContinue() {
      return SilkMoth.this.navigation.isFollowingPath();
    }

    @Override
    public void start() {
      Vec3d vec3d = this.getRandomLocation();
      if (vec3d != null) {
        SilkMoth.this.navigation.startMovingAlong(
            SilkMoth.this.navigation.findPathTo(BlockPos.ofFloored(vec3d), 1), 1.0);
      }
    }

    @Nullable
    private Vec3d getRandomLocation() {
      Vec3d vec3d2;
      if (SilkMoth.this.getHive() != null && SilkMoth.this.hivePos.isWithinDistance(
          SilkMoth.this.getBlockPos(), this.getMaxWanderDistance())) {
        Vec3d vec3d = Vec3d.ofCenter(SilkMoth.this.hivePos);
        vec3d2 = vec3d.subtract(SilkMoth.this.getPos()).normalize();
      } else {
        vec3d2 = SilkMoth.this.getRotationVec(0.0F);
      }

      Vec3d vec3d3 = AboveGroundTargeting.find(SilkMoth.this, 8, 7, vec3d2.x, vec3d2.z,
          (float) (Math.PI / 2), 3, 1);
      return vec3d3 != null ? vec3d3
          : NoPenaltySolidTargeting.find(SilkMoth.this, 8, 4, -2, vec3d2.x, vec3d2.z,
              (float) (Math.PI / 2));
    }

    private int getMaxWanderDistance() {
      int i = (SilkMoth.this.hivePos == null) ? 16 : 24;
      return 48 - i;
    }
  }

}
