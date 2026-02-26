package io.github.openbagtwo.lighterend.mobs;

import io.github.openbagtwo.lighterend.registries.LighterEndBiomes;
import io.github.openbagtwo.lighterend.utils.PosInfo;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.ConversionType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.scores.PlayerTeam;
import org.jetbrains.annotations.Nullable;

public class EndSlime extends Slime {

  private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(
      EndSlime.class,
      EntityDataSerializers.INT
  );

  public EndSlime(EntityType<EndSlime> entityType, Level world) {
    super(entityType, world);
    this.moveControl = new EndSlimeMoveControl(this);
  }

  protected void registerGoals() {
    this.goalSelector.addGoal(1, new SwimmingGoal());
    this.goalSelector.addGoal(2, new FaceTowardTargetGoal());
    this.goalSelector.addGoal(3, new RandomLookGoal());
    this.goalSelector.addGoal(5, new MoveGoal(this));
    this.targetSelector.addGoal(
        1,
        new NearestAttackableTargetGoal<>(
            this,
            Player.class,
            10,
            true,
            false,
            (target, world) -> Math.abs(target.getY() - this.getY()) <= 4.0D
        )
    );
    this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
  }

  public static AttributeSupplier.Builder createAttributes() {
    return LivingEntity
        .createLivingAttributes()
        .add(Attributes.MAX_HEALTH, 1.0D)
        .add(Attributes.ATTACK_DAMAGE, 1.0D)
        .add(Attributes.FOLLOW_RANGE, 16.0D)
        .add(Attributes.MOVEMENT_SPEED, 0.15D);
  }

  @Nullable
  @Override
  public SpawnGroupData finalizeSpawn(
      ServerLevelAccessor world,
      DifficultyInstance difficulty,
      EntitySpawnReason spawnReason,
      @Nullable SpawnGroupData entityData
  ) {
    SpawnGroupData data = super.finalizeSpawn(world, difficulty, spawnReason, entityData);

    Holder<Biome> biome = world.getBiome(blockPosition());
    if (biome.unwrapKey().isPresent()) {
      if (biome.is(LighterEndBiomes.FOGGY_MUSHROOMLANDS)) {
        this.setMossy();
      } else if (biome.is(LighterEndBiomes.UMBRELLA_JUNGLE)) {
        this.setJungle();
      }
//      else if (biome.matchesKey(EndBiomes.AMBER_LAND.key)) {
//        this.setAmber();
//      }
      this.refreshDimensions();
    }
    return data;
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    super.defineSynchedData(builder);
    builder.define(VARIANT, 0);
  }

  @Override
  public void addAdditionalSaveData(ValueOutput view) {
    super.addAdditionalSaveData(view);
    view.putInt("Variant", this.getSlimeType());
  }

  @Override
  protected void readAdditionalSaveData(ValueInput view) {
    super.readAdditionalSaveData(view);
    this.setSlimeType(view.getIntOr("Variant", 0));
  }

  @Override
  protected ParticleOptions getParticleType() {
    return ParticleTypes.PORTAL;
  }

  public int getSlimeType() {
    return this.entityData.get(VARIANT);
  }

  public void setSlimeType(int value) {
    this.entityData.set(VARIANT, value);
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
    if (!this.level().isClientSide() && i > 1 && this.isDeadOrDying()) {
      float f = this.getDimensions(this.getPose()).width();
      float g = f / 2.0F;
      int j = i / 2;
      int k = 2 + this.random.nextInt(3);
      PlayerTeam team = this.getTeam();

      for (int l = 0; l < k; l++) {
        float h = (l % 2 - 0.5F) * g;
        float m = (l / 2 - 0.5F) * g;
        this.convertTo(this.getType(),
            new ConversionParams(ConversionType.SPLIT_ON_DEATH, false, false, team),
            EntitySpawnReason.TRIGGERED, newSlime -> {
              newSlime.setSize(j, true);
              if (newSlime instanceof EndSlime babyEndSlime) {
                babyEndSlime.setSlimeType(this.getSlimeType());
              }
              newSlime.snapTo(this.getX() + h, this.getY() + 0.5, this.getZ() + m,
                  this.random.nextFloat() * 360.0F, 0.0F);
            });
      }
    }
    if ((reason == Entity.RemovalReason.KILLED || reason == Entity.RemovalReason.DISCARDED)
        && this.level() instanceof ServerLevel serverWorld) {
      this.triggerOnDeathMobEffects(serverWorld, reason);
    }
    this.setRemoved(reason);
    this.brain.clearMemories();
  }

  class MoveGoal extends Goal {

    private final EndSlime slime;

    public MoveGoal(EndSlime slime) {
      this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
      this.slime = slime;
    }

    public boolean canUse() {
      if (EndSlime.this.isPassenger()) {
        return false;
      }

      float yaw = EndSlime.this.getYHeadRot();
      float speed = EndSlime.this.getSpeed();
      if (speed > 0.1) {
        float dx = Mth.sin(-yaw * 0.017453292F);
        float dz = Mth.cos(-yaw * 0.017453292F);
        BlockPos pos = EndSlime.this.blockPosition().offset(
            (int) (dx * speed * 4),
            0,
            (int) (dz * speed * 4)
        );
        int down = PosInfo.downRay(EndSlime.this.level(), pos, 16);
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
      this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
      EndSlime.this.getNavigation().setCanFloat(true);
    }

    public boolean canUse() {
      return (EndSlime.this.isInWater() || EndSlime.this.isInLava())
          && EndSlime.this.getMoveControl() instanceof EndSlimeMoveControl;
    }

    public void tick() {
      if (EndSlime.this.getRandom().nextFloat() < 0.8F) {
        EndSlime.this.getJumpControl().jump();
      }

      ((EndSlimeMoveControl) EndSlime.this.getMoveControl()).move(1.2D);
    }
  }

  class RandomLookGoal extends Goal {

    private float targetYaw;
    private int timer;

    public RandomLookGoal() {
      this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    public boolean canUse() {
      return EndSlime.this.getTarget() == null && (EndSlime.this.onGround()
          || EndSlime.this.isInWater() || EndSlime.this
          .isInLava() || EndSlime.this.hasEffect(MobEffects.LEVITATION))
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
      this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    public boolean canUse() {
      LivingEntity livingEntity = EndSlime.this.getTarget();
      if (livingEntity == null) {
        return false;
      } else if (!livingEntity.isAlive()) {
        return false;
      } else {
        return (!(livingEntity instanceof Player)
            || !((Player) livingEntity).getAbilities().invulnerable) && EndSlime.this
            .getMoveControl() instanceof EndSlimeMoveControl;
      }
    }

    public void start() {
      this.ticksLeft = 300;
      super.start();
    }

    public boolean canContinueToUse() {
      LivingEntity livingEntity = EndSlime.this.getTarget();
      if (livingEntity == null) {
        return false;
      } else if (!livingEntity.isAlive()) {
        return false;
      } else if (livingEntity instanceof Player
          && ((Player) livingEntity).getAbilities().invulnerable) {
        return false;
      } else {
        return --this.ticksLeft > 0;
      }
    }

    public void tick() {
      EndSlime.this.lookAt(EndSlime.this.getTarget(), 10.0F, 10.0F);
      ((EndSlimeMoveControl) EndSlime.this.getMoveControl()).look(
          EndSlime.this.getYRot(),
          EndSlime.this.isDealsDamage()
      );
    }
  }

  class EndSlimeMoveControl extends MoveControl {

    private float targetYaw;
    private int ticksUntilJump;
    private boolean jumpOften;

    public EndSlimeMoveControl(EndSlime slime) {
      super(slime);
      this.targetYaw = 180.0F * slime.getYRot() / 3.1415927F;
    }

    public void look(float targetYaw, boolean jumpOften) {
      this.targetYaw = targetYaw;
      this.jumpOften = jumpOften;
    }

    public void move(double speed) {
      this.speedModifier = speed;
      this.operation = MoveControl.Operation.MOVE_TO;
    }

    public void tick() {
      this.mob.setYRot(this.rotlerp(this.mob.getYRot(), this.targetYaw, 90.0F));
      this.mob.yHeadRot = this.mob.getYRot();
      this.mob.yBodyRot = this.mob.getYRot();
      if (this.operation != MoveControl.Operation.MOVE_TO) {
        this.mob.setZza(0.0F);
      } else {
        this.operation = MoveControl.Operation.WAIT;
        if (this.mob.onGround()) {
          this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(
              Attributes.MOVEMENT_SPEED)));
          if (this.ticksUntilJump-- <= 0) {
            this.ticksUntilJump = EndSlime.this.getJumpDelay();
            if (this.jumpOften) {
              this.ticksUntilJump /= 3;
            }

            EndSlime.this.getJumpControl().jump();
            if (EndSlime.this.doPlayJumpSound()) {
              EndSlime.this.playSound(
                  EndSlime.this.getJumpSound(),
                  EndSlime.this.getSoundVolume(),
                  getJumpSoundPitch()
              );
            }
          } else {
            EndSlime.this.xxa = 0.0F;
            EndSlime.this.zza = 0.0F;
            this.mob.setSpeed(0.0F);
          }
        } else {
          this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(
              Attributes.MOVEMENT_SPEED)));
        }

      }
    }

    private float getJumpSoundPitch() {
      float f = EndSlime.this.isTiny() ? 1.4F : 0.8F;
      return ((EndSlime.this.random.nextFloat() - EndSlime.this.random.nextFloat()) * 0.2F + 1.0F)
          * f;
    }
  }

}
