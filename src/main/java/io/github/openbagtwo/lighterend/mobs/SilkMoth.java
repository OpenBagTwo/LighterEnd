package io.github.openbagtwo.lighterend.mobs;

import io.github.openbagtwo.lighterend.blocks.SilkMothNest;
import io.github.openbagtwo.lighterend.registries.LighterEndBlocks;
import io.github.openbagtwo.lighterend.registries.LighterEndMobs;
import java.util.EnumSet;
import net.minecraft.block.Block;
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
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SilkMoth extends AnimalEntity implements Flutterer {

  private BlockPos hivePos;
  private BlockPos entrance;

  public SilkMoth(EntityType<? extends SilkMoth> entityType, World world) {
    super(entityType, world);
    this.moveControl = new FlightMoveControl(this, 20, true);
    this.lookControl = new MothLookControl(this);
    this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
    this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
    this.experiencePoints = 1;
  }

  public static DefaultAttributeContainer.Builder createAttributes() {
    return AnimalEntity.createAnimalAttributes()
        .add(EntityAttributes.MAX_HEALTH, 2.0D)
        .add(EntityAttributes.FOLLOW_RANGE, 16.0D)
        .add(EntityAttributes.FLYING_SPEED, 0.4D)
        .add(EntityAttributes.MOVEMENT_SPEED, 0.1D);
  }

  public void setHive(World world, BlockPos hive) {
    this.hivePos = hive;
  }

  @Override
  public boolean canBeLeashed() {
    return true;
  }

  @Override
  public void writeCustomData(WriteView view) {
    super.writeCustomData(view);
    if (this.hivePos != null) {
      view.putNullable("hive_pos", BlockPos.CODEC, this.hivePos);
    }
  }

  @Override
  protected void readCustomData(ReadView view) {
    super.readCustomData(view);
    this.hivePos = view.read("hive_pos", BlockPos.CODEC).orElse(null);
  }

  @Override
  protected void initGoals() {
    this.goalSelector.add(1, new ReturnToHiveGoal());
    this.goalSelector.add(2, new AnimalMateGoal(this, 1.0D));
    this.goalSelector.add(3,
        new TemptGoal(this, 1.25, Ingredient.ofItems(LighterEndBlocks.TENANEA_FLOWER), false));
    this.goalSelector.add(5, new FollowParentGoal(this, 1.25D));
    this.goalSelector.add(8, new WanderAroundGoal());
    this.goalSelector.add(9, new SwimGoal(this));
  }

  @Override
  protected EntityNavigation createNavigation(World world) {
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
  public boolean isPushable() {
    return true;
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

  class MothLookControl extends LookControl {

    MothLookControl(MobEntity entity) {
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
      Vec3d vec3d = null;
      if (SilkMoth.this.hivePos != null) {
        if (SilkMoth.this.getPos()
            .squaredDistanceTo(
                SilkMoth.this.hivePos.getX(),
                SilkMoth.this.hivePos.getY(),
                SilkMoth.this.hivePos.getZ()
            ) > 16) {
          vec3d = SilkMoth.this.getPos()
              .add(random.nextGaussian() * 2, 0, random.nextGaussian() * 2);
        }
      }
      vec3d = vec3d == null ? this.getRandomLocation() : vec3d;
      if (vec3d != null) {
        try {
          SilkMoth.this.navigation.startMovingAlong(SilkMoth.this.navigation.findPathTo(
              new BlockPos((int) vec3d.x, (int) vec3d.y, (int) vec3d.z),
              1
          ), 1.0D);
        } catch (Exception e) {
        }
      }
    }

    @Nullable
    private Vec3d getRandomLocation() {
      Vec3d vec3d3 = SilkMoth.this.getRotationVec(0.0F);
      Vec3d vec3d4 = AboveGroundTargeting.find(SilkMoth.this, 8, 7, vec3d3.x, vec3d3.z,
          1.5707964F, 3, 1);
      return vec3d4 != null ? vec3d4 : NoPenaltySolidTargeting.find(
          SilkMoth.this,
          8,
          4,
          -2,
          vec3d3.x,
          vec3d3.z,
          1.5707963705062866D
      );
    }
  }

  class ReturnToHiveGoal extends Goal {

    ReturnToHiveGoal() {
      this.setControls(EnumSet.of(Goal.Control.MOVE));
    }

    @Override
    public boolean canStart() {
      return SilkMoth.this.hivePos != null &&
          SilkMoth.this.navigation.isIdle() &&
          SilkMoth.this.random.nextInt(8) == 0 &&
          SilkMoth.this.getPos().squaredDistanceTo(
              SilkMoth.this.hivePos.getX(),
              SilkMoth.this.hivePos.getY(),
              SilkMoth.this.hivePos.getZ()
          ) < 16384;
    }

    @Override
    public boolean shouldContinue() {
      return SilkMoth.this.navigation.isFollowingPath() && getWorld().getBlockState(entrance)
          .isAir() && (getWorld().getBlockState(hivePos).isOf(LighterEndBlocks.SILK_MOTH_NEST));
    }

    @Override
    public void start() {
      BlockState state = SilkMoth.this.getWorld().getBlockState(SilkMoth.this.hivePos);
      if (!state.isOf(LighterEndBlocks.SILK_MOTH_NEST)) {
        SilkMoth.this.hivePos = null;
        return;
      }
      try {
        SilkMoth.this.entrance = SilkMoth.this.hivePos.offset(
            state.get(Properties.HORIZONTAL_FACING));
        SilkMoth.this.navigation.startMovingAlong(
            SilkMoth.this.navigation.findPathTo(entrance, 1), 1.0D);
      } catch (Exception e) {
      }
    }

    @Override
    public void tick() {
      super.tick();
      if (SilkMoth.this.entrance == null) {
        return;
      }
      double dx = Math.abs(SilkMoth.this.entrance.getX() - SilkMoth.this.getX());
      double dy = Math.abs(SilkMoth.this.entrance.getY() - SilkMoth.this.getY());
      double dz = Math.abs(SilkMoth.this.entrance.getZ() - SilkMoth.this.getZ());
      if (dx + dy + dz < 1) {
        BlockState state = SilkMoth.this.getWorld().getBlockState(hivePos);
        if (state.isOf(LighterEndBlocks.SILK_MOTH_NEST)) {
          int fullness = state.get(SilkMothNest.FULLNESS);

          if (fullness < 3 && SilkMoth.this.random.nextBoolean()) {
            fullness += MathHelper.nextInt(random, 1, 2);
            if (fullness > 3) {
              fullness = 3;
            }
            SilkMoth.this.getWorld().setBlockState(SilkMoth.this.hivePos,
                state.with(SilkMothNest.FULLNESS, fullness),
                Block.NOTIFY_ALL);
          }
          SilkMoth.this.getWorld().playSound(
              null,
              SilkMoth.this.entrance,
              SoundEvents.BLOCK_BEEHIVE_ENTER,
              SoundCategory.BLOCKS,
              1,
              1
          );
          SilkMoth.this.discard();
        } else {
          SilkMoth.this.hivePos = null;
        }
      }
    }
  }

  @Override
  public boolean isBreedingItem(ItemStack itemStack) {
    return itemStack.isOf(LighterEndBlocks.TENANEA_FLOWER.asItem());
  }

  @Override
  public ActionResult interactMob(PlayerEntity player, Hand interactionHand) {
    return super.interactMob(player, interactionHand);
  }
}
