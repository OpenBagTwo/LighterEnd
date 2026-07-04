package io.github.openbagtwo.lighterend.mobs;

import io.github.openbagtwo.lighterend.registries.LighterEndBiomes;
import io.github.openbagtwo.lighterend.utils.PosInfo;
import java.util.EnumSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.conversion.EntityConversionContext;
import net.minecraft.entity.conversion.EntityConversionType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

public class EndSlime extends SlimeEntity {

  private static final TrackedData<Integer> VARIANT = DataTracker.registerData(
      EndSlime.class,
      TrackedDataHandlerRegistry.INTEGER
  );

  public EndSlime(EntityType<EndSlime> entityType, World world) {
    super(entityType, world);
    this.moveControl = new EndSlimeMoveControl(this);
  }

  protected void initGoals() {
    this.goalSelector.add(1, new SwimmingGoal());
    this.goalSelector.add(2, new FaceTowardTargetGoal());
    this.goalSelector.add(3, new RandomLookGoal());
    this.goalSelector.add(5, new MoveGoal(this));
    this.targetSelector.add(
        1,
        new ActiveTargetGoal<>(
            this,
            PlayerEntity.class,
            10,
            true,
            false,
            (target, world) -> Math.abs(target.getY() - this.getY()) <= 4.0D
        )
    );
    this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
  }

  public static DefaultAttributeContainer.Builder createAttributes() {
    return LivingEntity
        .createLivingAttributes()
        .add(EntityAttributes.MAX_HEALTH, 1.0D)
        .add(EntityAttributes.ATTACK_DAMAGE, 1.0D)
        .add(EntityAttributes.FOLLOW_RANGE, 16.0D)
        .add(EntityAttributes.MOVEMENT_SPEED, 0.15D);
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

    RegistryEntry<Biome> biome = world.getBiome(getBlockPos());
    if (biome.getKey().isPresent()) {
      if (biome.matchesKey(LighterEndBiomes.FOGGY_MUSHROOMLANDS)) {
        this.setMossy();
      } else if (biome.matchesKey(LighterEndBiomes.UMBRELLA_JUNGLE)) {
        this.setJungle();
      }
//      else if (biome.matchesKey(EndBiomes.AMBER_LAND.key)) {
//        this.setAmber();
//      }
      this.calculateDimensions();
    }
    return data;
  }

  @Override
  protected void initDataTracker(DataTracker.Builder builder) {
    super.initDataTracker(builder);
    builder.add(VARIANT, 0);
  }

  @Override
  public void writeCustomData(WriteView view) {
    super.writeCustomData(view);
    view.putInt("Variant", this.getSlimeType());
  }

  @Override
  protected void readCustomData(ReadView view) {
    super.readCustomData(view);
    this.setSlimeType(view.getInt("Variant", 0));
  }

  @Override
  protected ParticleEffect getParticles() {
    return ParticleTypes.PORTAL;
  }

  public int getSlimeType() {
    return this.dataTracker.get(VARIANT);
  }

  public void setSlimeType(int value) {
    this.dataTracker.set(VARIANT, value);
  }

  protected void setMossy() {
    this.setSlimeType(1);
  }

  protected void setJungle() {
    this.setSlimeType(0);
  }

  @Override
  public void remove(Entity.RemovalReason reason) {
    int i = this.getSize();
    if (!this.getWorld().isClient() && i > 1 && this.isDead()) {
      float f = this.getDimensions(this.getPose()).width();
      float g = f / 2.0F;
      int j = i / 2;
      int k = 2 + this.random.nextInt(3);
      Team team = this.getScoreboardTeam();

      for (int l = 0; l < k; l++) {
        float h = (l % 2 - 0.5F) * g;
        float m = (l / 2 - 0.5F) * g;
        this.convertTo(this.getType(),
            new EntityConversionContext(EntityConversionType.SPLIT_ON_DEATH, false, false, team),
            SpawnReason.TRIGGERED, newSlime -> {
              newSlime.setSize(j, true);
              if (newSlime instanceof EndSlime babyEndSlime) {
                babyEndSlime.setSlimeType(this.getSlimeType());
              }
              newSlime.refreshPositionAndAngles(this.getX() + h, this.getY() + 0.5, this.getZ() + m,
                  this.random.nextFloat() * 360.0F, 0.0F);
            });
      }
    }
    if ((reason == Entity.RemovalReason.KILLED || reason == Entity.RemovalReason.DISCARDED)
        && this.getWorld() instanceof ServerWorld serverWorld) {
      this.onRemoval(serverWorld, reason);
    }
    this.setRemoved(reason);
    this.brain.forgetAll();
  }

  class MoveGoal extends Goal {

    private final EndSlime slime;

    public MoveGoal(EndSlime slime) {
      this.setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE));
      this.slime = slime;
    }

    public boolean canStart() {
      if (EndSlime.this.hasVehicle()) {
        return false;
      }

      float yaw = EndSlime.this.getHeadYaw();
      float speed = EndSlime.this.getMovementSpeed();
      if (speed > 0.1) {
        float dx = MathHelper.sin(-yaw * 0.017453292F);
        float dz = MathHelper.cos(-yaw * 0.017453292F);
        BlockPos pos = EndSlime.this.getBlockPos().add(
            (int) (dx * speed * 4),
            0,
            (int) (dz * speed * 4)
        );
        int down = PosInfo.downRay(EndSlime.this.getWorld(), pos, 16);
        return down < 5;
      }

      return true;
    }

    @Override
    public void tick() {
      if (this.slime.getMoveControl() instanceof EndSlimeMoveControl slimeMoveControl) {
        slimeMoveControl.move(1.0);
      }
    }
  }

  class SwimmingGoal extends Goal {

    public SwimmingGoal() {
      this.setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE));
      EndSlime.this.getNavigation().setCanSwim(true);
    }

    public boolean canStart() {
      return (EndSlime.this.isTouchingWater() || EndSlime.this.isInLava())
          && EndSlime.this.getMoveControl() instanceof EndSlimeMoveControl;
    }

    public void tick() {
      if (EndSlime.this.getRandom().nextFloat() < 0.8F) {
        EndSlime.this.getJumpControl().setActive();
      }

      ((EndSlimeMoveControl) EndSlime.this.getMoveControl()).move(1.2D);
    }
  }

  class RandomLookGoal extends Goal {

    private float targetYaw;
    private int timer;

    public RandomLookGoal() {
      this.setControls(EnumSet.of(Goal.Control.LOOK));
    }

    public boolean canStart() {
      return EndSlime.this.getTarget() == null && (EndSlime.this.isOnGround()
          || EndSlime.this.isTouchingWater() || EndSlime.this
          .isInLava() || EndSlime.this.hasStatusEffect(StatusEffects.LEVITATION))
          && EndSlime.this.getMoveControl() instanceof EndSlimeMoveControl;
    }

    public void tick() {
      if (--this.timer <= 0) {
        this.timer = 40 + EndSlime.this.getRandom().nextInt(60);
        this.targetYaw = (float) EndSlime.this.getRandom().nextInt(360);
      }

      ((EndSlimeMoveControl) EndSlime.this.getMoveControl()).look(this.targetYaw, false);
    }
  }

  class FaceTowardTargetGoal extends Goal {

    private int ticksLeft;

    public FaceTowardTargetGoal() {
      this.setControls(EnumSet.of(Goal.Control.LOOK));
    }

    public boolean canStart() {
      LivingEntity livingEntity = EndSlime.this.getTarget();
      if (livingEntity == null) {
        return false;
      } else if (!livingEntity.isAlive()) {
        return false;
      } else {
        return (!(livingEntity instanceof PlayerEntity)
            || !((PlayerEntity) livingEntity).getAbilities().invulnerable) && EndSlime.this
            .getMoveControl() instanceof EndSlimeMoveControl;
      }
    }

    public void start() {
      this.ticksLeft = 300;
      super.start();
    }

    public boolean shouldContinue() {
      LivingEntity livingEntity = EndSlime.this.getTarget();
      if (livingEntity == null) {
        return false;
      } else if (!livingEntity.isAlive()) {
        return false;
      } else if (livingEntity instanceof PlayerEntity
          && ((PlayerEntity) livingEntity).getAbilities().invulnerable) {
        return false;
      } else {
        return --this.ticksLeft > 0;
      }
    }

    public void tick() {
      EndSlime.this.lookAtEntity(EndSlime.this.getTarget(), 10.0F, 10.0F);
      ((EndSlimeMoveControl) EndSlime.this.getMoveControl()).look(
          EndSlime.this.getYaw(),
          EndSlime.this.canAttack()
      );
    }
  }

  class EndSlimeMoveControl extends MoveControl {

    private float targetYaw;
    private int ticksUntilJump;
    private boolean jumpOften;

    public EndSlimeMoveControl(EndSlime slime) {
      super(slime);
      this.targetYaw = 180.0F * slime.getYaw() / 3.1415927F;
    }

    public void look(float targetYaw, boolean jumpOften) {
      this.targetYaw = targetYaw;
      this.jumpOften = jumpOften;
    }

    public void move(double speed) {
      this.speed = speed;
      this.state = MoveControl.State.MOVE_TO;
    }

    public void tick() {
      this.entity.setYaw(this.wrapDegrees(this.entity.getYaw(), this.targetYaw, 90.0F));
      this.entity.headYaw = this.entity.getYaw();
      this.entity.bodyYaw = this.entity.getYaw();
      if (this.state != MoveControl.State.MOVE_TO) {
        this.entity.setForwardSpeed(0.0F);
      } else {
        this.state = MoveControl.State.WAIT;
        if (this.entity.isOnGround()) {
          this.entity.setMovementSpeed((float) (this.speed * this.entity.getAttributeValue(
              EntityAttributes.MOVEMENT_SPEED)));
          if (this.ticksUntilJump-- <= 0) {
            this.ticksUntilJump = EndSlime.this.getTicksUntilNextJump();
            if (this.jumpOften) {
              this.ticksUntilJump /= 3;
            }

            EndSlime.this.getJumpControl().setActive();
            if (EndSlime.this.makesJumpSound()) {
              EndSlime.this.playSound(
                  EndSlime.this.getJumpSound(),
                  EndSlime.this.getSoundVolume(),
                  getJumpSoundPitch()
              );
            }
          } else {
            EndSlime.this.sidewaysSpeed = 0.0F;
            EndSlime.this.forwardSpeed = 0.0F;
            this.entity.setMovementSpeed(0.0F);
          }
        } else {
          this.entity.setMovementSpeed((float) (this.speed * this.entity.getAttributeValue(
              EntityAttributes.MOVEMENT_SPEED)));
        }

      }
    }

    private float getJumpSoundPitch() {
      float f = EndSlime.this.isSmall() ? 1.4F : 0.8F;
      return ((EndSlime.this.random.nextFloat() - EndSlime.this.random.nextFloat()) * 0.2F + 1.0F)
          * f;
    }
  }

}
